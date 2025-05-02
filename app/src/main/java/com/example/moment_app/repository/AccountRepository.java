package com.example.moment_app.repository;

import android.content.Context;

import com.example.moment_app.api.AccountApiService;
import com.example.moment_app.api.ApiClient;
import com.example.moment_app.models.request.AccountInfoRequest;
import com.example.moment_app.models.request.ChangePasswordRequest;
import com.example.moment_app.models.request.RegisterRequest;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.AuthenticationResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepository {
    private final AccountApiService accountApiService;

    public AccountRepository(Context context) {
        accountApiService = ApiClient.getClient(context).create(AccountApiService.class);
    }

    public void register(RegisterRequest request, AccountRepository.AccountCallback callback) {
        accountApiService.register(request).enqueue(new Callback<ApiResponse<AuthenticationResponse>>() {
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


    public void updateAccountInfo(AccountInfoRequest request, AccountRepository.AccountInfoCallback callback) {
        accountApiService.updateAccountInfo(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                try {

                    if (response.body() != null) {
                        // Trường hợp response 200 + body
                        callback.onSuccess(response.body());
                    } else if (response.errorBody() != null) {
                        // Trường hợp response != 200 nhưng có dữ liệu JSON trong errorBody
                        Gson gson = new Gson();
                        ApiResponse<Void> errorResponse = gson.fromJson(
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
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void changeUserName(AccountInfoRequest request, AccountRepository.AccountInfoCallback callback) {
        accountApiService.changeUserName(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                try {

                    if (response.body() != null) {
                        // Trường hợp response 200 + body
                        callback.onSuccess(response.body());
                    } else if (response.errorBody() != null) {
                        // Trường hợp response != 200 nhưng có dữ liệu JSON trong errorBody
                        Gson gson = new Gson();
                        ApiResponse<Void> errorResponse = gson.fromJson(
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
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void changePassword (ChangePasswordRequest request, AccountRepository.AccountInfoCallback callback) {
        accountApiService.changePassword(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                try {

                    if (response.body() != null) {
                        // Trường hợp response 200 + body
                        callback.onSuccess(response.body());
                    } else if (response.errorBody() != null) {
                        // Trường hợp response != 200 nhưng có dữ liệu JSON trong errorBody
                        Gson gson = new Gson();
                        ApiResponse<Void> errorResponse = gson.fromJson(
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
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


    public interface AccountInfoCallback {
        void onSuccess(ApiResponse<Void> response);
        void onError(Throwable t);
    }



    public interface AccountCallback {
        void onSuccess(ApiResponse<AuthenticationResponse> authentication);
        void onError(Throwable t);
    }

}
