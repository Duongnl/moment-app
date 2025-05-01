package com.example.moment_app.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moment_app.R;
import com.example.moment_app.adapters.HomeAdapter;
import com.example.moment_app.models.request.AuthenticationRequest;
import com.example.moment_app.models.request.PhotoFilterRequest;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.AuthenticationResponse;
import com.example.moment_app.models.response.PhotoResponse;
import com.example.moment_app.repository.AuthenticationRepository;
import com.example.moment_app.repository.PhotoRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private PhotoRepository photoRepository;

    private TextView textViewPost;

    private   RecyclerView recyclerViewPhoto;
    private int currentPage = 0;
    private boolean isLoading = false;

    ImageView imageViewPhoto;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewPhoto = view.findViewById(R.id.recyclerViewPhoto);
        recyclerViewPhoto.setLayoutManager(new LinearLayoutManager(getContext()));
        imageViewPhoto = view.findViewById(R.id.imageViewPhoto);

        textViewPost = view.findViewById(R.id.editTextPost);

        textViewPost.setOnClickListener(v -> {
            Fragment postFragment = new PostFragment(); // fragment bạn muốn mở

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerMain, postFragment) // ID của container chứa Fragment
                    .addToBackStack(null) // cho phép quay lại bằng nút back
                    .commit();
        });


        photoRepository = new PhotoRepository(requireContext());

        // Tạo đối tượng Date hiện tại
        Date now = new Date();
        // Tạo formatter với định dạng ISO 8601 và UTC
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));  // dùng UTC để có hậu tố Z
        String currentTimeISO8601 = sdf.format(now);

        PhotoFilterRequest request = new PhotoFilterRequest(0, currentTimeISO8601);

        photoRepository.getListPhotoFriends(request, new PhotoRepository.PhotoCallback() {
            @Override
            public void onSuccess(ApiResponse<List<PhotoResponse>> response) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(),  String.valueOf(response.getStatus())  , Toast.LENGTH_SHORT).show();

                    if (response.getStatus() == 200) {

                        HomeAdapter adapter = new HomeAdapter(getContext(), response.getResult());
                        recyclerViewPhoto.setAdapter(adapter);

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

        recyclerViewPhoto.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    int visibleThreshold = 5; // load trước khi chạm đáy 5 item

                    if ((totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {
                        loadMoreData();
                    }
                }
            }
        });




    }

    private void loadMoreData() {
        isLoading = true;
        currentPage++;

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentTimeISO8601 = sdf.format(now);

        PhotoFilterRequest request = new PhotoFilterRequest(currentPage, currentTimeISO8601);

        photoRepository.getListPhotoFriends(request, new PhotoRepository.PhotoCallback() {
            @Override
            public void onSuccess(ApiResponse<List<PhotoResponse>> response) {
                requireActivity().runOnUiThread(() -> {
                    if (response.getStatus() == 200 && response.getResult() != null) {
                        // Thêm dữ liệu vào adapter
                        HomeAdapter adapter = (HomeAdapter) recyclerViewPhoto.getAdapter();
                        if (adapter != null) {
                            adapter.addData(response.getResult()); // phải có hàm này trong adapter
                        }
                    }
                    isLoading = false;
                });
            }

            @Override
            public void onError(Throwable t) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Lỗi khi load thêm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    isLoading = false;
                });
            }
        });
    }



}