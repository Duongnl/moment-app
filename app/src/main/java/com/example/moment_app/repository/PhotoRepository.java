package com.example.moment_app.repository;

import android.content.Context;

import com.example.moment_app.api.ApiClient;
import com.example.moment_app.api.PhotoApiService;
import com.example.moment_app.models.request.PhotoFilterRequest;
import com.example.moment_app.models.request.PostRequest;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.PhotoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoRepository {
    private final PhotoApiService photoApiService;

    public PhotoRepository(Context context) {
        photoApiService = ApiClient.getClient(context).create(PhotoApiService.class);
    }

    public void getListPhotoFriends(PhotoFilterRequest request, PhotoCallback callback) {
        photoApiService.getListPhotoFriends(request).enqueue(new Callback<ApiResponse<List<PhotoResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PhotoResponse>>> call, Response<ApiResponse<List<PhotoResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Không lấy được dữ liệu ảnh"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PhotoResponse>>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


    public void post(PostRequest request, PostCallback callback) {
        photoApiService.post(request).enqueue(new Callback<ApiResponse<PhotoResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PhotoResponse>> call, Response<ApiResponse<PhotoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Không lấy được dữ liệu ảnh"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PhotoResponse>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void changeAvatar(PostRequest request, AvtCallback callback) {
        photoApiService.changeAvatar(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Không lấy được dữ liệu ảnh"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }



    public interface AvtCallback {
        void onSuccess(ApiResponse<Void> photos);
        void onError(Throwable t);
    }

    public interface PhotoCallback {
        void onSuccess(ApiResponse<List<PhotoResponse>> photos);
        void onError(Throwable t);
    }

    public interface PostCallback {
        void onSuccess(ApiResponse<PhotoResponse> post);
        void onError(Throwable t);
    }

}
