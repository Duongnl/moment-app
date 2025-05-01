package com.example.moment_app.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.bumptech.glide.Glide;
import com.example.moment_app.R;
import com.example.moment_app.adapters.HomeAdapter;
import com.example.moment_app.models.request.PhotoFilterRequest;
import com.example.moment_app.models.request.PostRequest;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.PhotoResponse;
import com.example.moment_app.models.response.UserResponse;
import com.example.moment_app.repository.AccountRepository;
import com.example.moment_app.repository.AuthenticationRepository;
import com.example.moment_app.repository.PhotoRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {
    private AuthenticationRepository authenticationRepository;
    private Button buttonBack;

    private ImageView imageViewPostPhoto;

    private EditText editTextCaptionPost;

    private  Button buttonTakePhoto;

    private Button buttonPost;

    private PhotoRepository photoRepository;


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri photoUri;

    private ImageView imageViewAvtIndividual;
    private TextView textViewNameIndividual;

    private File photoFile;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String publicIdImg;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
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
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        imageViewAvtIndividual = view.findViewById(R.id.imageViewAvt);
        textViewNameIndividual = view.findViewById(R.id.textViewNameIndividual);
        editTextCaptionPost = view.findViewById(R.id.editTextCaptionPost);
        imageViewPostPhoto =  view.findViewById(R.id.imageViewPostPhoto);
        buttonPost = view.findViewById(R.id.buttonPost);
        buttonTakePhoto = view.findViewById(R.id.buttonTakePhoto);
        buttonTakePhoto.setOnClickListener(v -> checkCameraPermission());

        buttonBack  = view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            Fragment homeFragment = new HomeFragment(); // fragment bạn muốn mở

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerMain, homeFragment) // ID của container chứa Fragment
                    .addToBackStack(null) // cho phép quay lại bằng nút back
                    .commit();

        });

        photoRepository = new PhotoRepository(requireContext());
        buttonPost.setOnClickListener(v -> {
            // Để bật lại button
            buttonPost.setEnabled(false);
            buttonPost.setText("Đang đăng bài ....");

            uploadImageToCloudinary(photoFile);
        });






        authenticationRepository = new AuthenticationRepository(requireContext());

        authenticationRepository.getMyInfo(new AuthenticationRepository.UserCallback() {
            @Override
            public void onSuccess(ApiResponse<UserResponse> response) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Status: " + response.getStatus(), Toast.LENGTH_SHORT).show();

                    if (response.getStatus() == 200) {
                        // Lấy thông tin người dùng
                        textViewNameIndividual.setText(response.getResult().getName());
                        String imageUrl = "https://res.cloudinary.com/moment-images/image/upload/" + response.getResult().getUrlPhoto();

                        Glide.with(requireContext())
                                .load(imageUrl)
                                .circleCrop()
                                .into(imageViewAvtIndividual);

                    } else {

                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                });
            }
        });

    }
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            Toast.makeText(getContext(), "Cần cấp quyền camera để chụp ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
                photoUri = FileProvider.getUriForFile(requireContext(),
                        requireContext().getPackageName() + ".provider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                Toast.makeText(getContext(), "Lỗi tạo file ảnh", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "IMG_" + timeStamp;
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileName, ".jpg", storageDir);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageViewPostPhoto.setVisibility(View.VISIBLE);
            imageViewPostPhoto.setImageURI(photoUri);
            buttonPost.setVisibility(View.VISIBLE);
        }
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
                    request.setCaption(editTextCaptionPost.getText().toString().trim());
                    request.setUrl(publicIdImg);

                    photoRepository.post(request, new PhotoRepository.PostCallback() {
                        @Override
                        public void onSuccess(ApiResponse<PhotoResponse> response) {
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(),  String.valueOf(response.getStatus())  , Toast.LENGTH_SHORT).show();

                                if (response.getStatus() == 200) {

                                    Fragment homeFragment = new HomeFragment(); // fragment bạn muốn mở
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