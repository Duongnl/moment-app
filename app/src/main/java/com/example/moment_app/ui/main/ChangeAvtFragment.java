package com.example.moment_app.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.Manifest;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moment_app.R;
import com.example.moment_app.models.request.PostRequest;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.PhotoResponse;
import com.example.moment_app.repository.PhotoRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeAvtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeAvtFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1; // Mã yêu cầu
    private static final int PERMISSION_REQUEST_CODE = 2; // Mã yêu cầu quyền
    private File selectedImageFile;

    private String publicIdImg;
    private  Button buttonSaveAvt;

    private Button buttonBackChangeAvt;
    private ImageView imageViewAvtNew;
    private  Button buttonLoadAvt;

    private PhotoRepository photoRepository;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangeAvtFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeAvtFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeAvtFragment newInstance(String param1, String param2) {
        ChangeAvtFragment fragment = new ChangeAvtFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_avt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonBackChangeAvt = view.findViewById(R.id.buttonBackChangeAvt);
       imageViewAvtNew = view.findViewById(R.id.imageViewAvtNew);
       buttonLoadAvt = view.findViewById(R.id.buttonLoadAvt);
        buttonSaveAvt = view.findViewById(R.id.buttonSaveAvt);
        photoRepository = new PhotoRepository(requireContext());
        buttonSaveAvt.setVisibility(View.GONE);



        buttonBackChangeAvt.setOnClickListener(v -> {
            Fragment setting = new SettingFragment(); // fragment bạn muốn mở

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerMain, setting) // ID của container chứa Fragment
                    .addToBackStack(null) // cho phép quay lại bằng nút back
                    .commit();

        });


        // Kiểm tra quyền trước khi mở thư viện ảnh
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa cấp quyền, yêu cầu quyền
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }


        buttonLoadAvt.setOnClickListener(v -> {
            // Tạo intent để mở thư viện ảnh
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*"); // Chỉ chọn hình ảnh
            startActivityForResult(intent, PICK_IMAGE_REQUEST); // Bắt đầu Activity với mã yêu cầu
        });


        buttonSaveAvt.setOnClickListener(v -> {
            // Để bật lại button
            buttonSaveAvt.setEnabled(false);
            buttonSaveAvt.setText("Đang cập nhật ....");
            uploadImageToCloudinary(selectedImageFile);
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            // Lấy URI của ảnh được chọn
            Uri selectedImageUri = data.getData();

            try {
                selectedImageFile = uriToTempFile(selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Không thể xử lý ảnh", Toast.LENGTH_SHORT).show();
                return;
            }
            // Sử dụng Glide để tải ảnh vào CircleImageView
            Glide.with(requireContext())
                    .load(selectedImageUri)
                    .into(imageViewAvtNew); // Hiển thị ảnh lên CircleImageView

            buttonSaveAvt.setVisibility(View.VISIBLE);
        }
    }

    // Kiểm tra kết quả yêu cầu quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, có thể tiếp tục mở thư viện ảnh
            } else {
                // Quyền bị từ chối, bạn có thể thông báo cho người dùng
                Toast.makeText(requireContext(), "Bạn cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File uriToTempFile(Uri uri) throws IOException {
        InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
        File tempFile = File.createTempFile("upload_", ".jpg", requireContext().getCacheDir());
        OutputStream outputStream = new FileOutputStream(tempFile);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();
        return tempFile;
    }


    public void uploadImageToCloudinary(File imageFile) {
        OkHttpClient client = new OkHttpClient();

        // Tạo request body
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imageFile.getName(),
                        RequestBody.create(imageFile, MediaType.parse("image/*")))
                .addFormDataPart("upload_preset", "moment")
                .addFormDataPart("folder", "moment-folder")
                .build();

        // Tạo request
        Request request = new Request.Builder()
                .url("https://api.cloudinary.com/v1_1/moment-images/image/upload")
                .post(requestBody)
                .build();

        // Gửi request bất đồng bộ
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("Cloudinary", "Upload failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("Cloudinary", "Upload success: " + responseData);

                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        String publicId = jsonObject.getString("public_id");
                        Log.d("Cloudinary", "public_id: " + publicId);
                        publicIdImg = publicId;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Cloudinary", "JSON parse error: " + e.getMessage());
                    }

                    PostRequest request = new PostRequest();
                    request.setUrl(publicIdImg);

                    photoRepository.changeAvatar(request, new PhotoRepository.AvtCallback() {
                        @Override
                        public void onSuccess(ApiResponse<Void> response) {
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(),  String.valueOf(response.getStatus())  , Toast.LENGTH_SHORT).show();

                                if (response.getStatus() == 200) {

                                    Fragment homeFragment = new SettingFragment(); // fragment bạn muốn mở
                                    requireActivity()
                                            .getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragmentContainerMain, homeFragment) // ID của container chứa Fragment
                                            .addToBackStack(null) // cho phép quay lại bằng nút back
                                            .commitAllowingStateLoss();

                                } else {

                                }
                                // TODO: Chuyển màn hình hoặc lưu token, v.v.
                            });
                        }
                        @Override
                        public void onError(Throwable t) {
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                System.out.println("fail response"+ t.getMessage());
                            });
                        }
                    });



                } else {
                    Log.e("Cloudinary", "Upload error: " + response.code() + " - " + response.message());
                }
            }
        });
    }


}