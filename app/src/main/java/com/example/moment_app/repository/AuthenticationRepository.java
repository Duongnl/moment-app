package com.example.moment_app.repository;

import android.content.Context;

import com.example.moment_app.api.ApiClient;
import com.example.moment_app.api.AuthenticationApiService;
import com.example.moment_app.api.PhotoApiService;
import com.example.moment_app.models.request.AuthenticationRequest;
import com.example.moment_app.models.request.PhotoFilterRequest;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.AuthenticationResponse;
import com.example.moment_app.models.response.PhotoResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationRepository {

    private final AuthenticationApiService authenticationApiService;

    public AuthenticationRepository(Context context) {
        authenticationApiService = ApiClient.getClient(context).create(AuthenticationApiService.class);
    }

    public void authenticate(AuthenticationRequest request, AuthenticationRepository.AuthenticationCallback callback) {
        authenticationApiService.authenticate(request).enqueue(new Callback<ApiResponse<AuthenticationResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthenticationResponse>> call, Response<ApiResponse<AuthenticationResponse>> response) {
                try {

                    if (response.body() != null) {
                        // Trường hợp response 200 + body
                        callback.onSuccess(response.body());
                    } else if (response.errorBody() != null) {
                        // Trường hợp response != 200 nhưng có dữ liệu JSON trong errorBody
                        Gson gson = new Gson();
                        ApiResponse<AuthenticationResponse> errorResponse = gson.fromJson(
                                response.errorBody().charStream(),
                                new TypeToken<ApiResponse<AuthenticationResponse>>() {}.getType()
                        );
                        callback.onSuccess(errorResponse); // vẫn trả về trong onSuccess để xử lý chung
                    } else {
                        callback.onError(new Exception("Không có dữ liệu phản hồi từ server"));
                    }

                } catch (Exception e) {
                    callback.onError(e);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public interface AuthenticationCallback {
        void onSuccess(ApiResponse<AuthenticationResponse> authentication);
        void onError(Throwable t);
    }

}
