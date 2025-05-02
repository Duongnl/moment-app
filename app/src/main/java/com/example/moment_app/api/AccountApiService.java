package com.example.moment_app.api;

import com.example.moment_app.models.request.AccountInfoRequest;
import com.example.moment_app.models.request.RegisterRequest;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.AuthenticationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AccountApiService {

    @POST("account/register")
    Call<ApiResponse<AuthenticationResponse>> register(@Body RegisterRequest request );


    @PUT("account/setting")
    Call<ApiResponse<Void>> updateAccountInfo(@Body AccountInfoRequest request );


    @PUT("account/change-username")
    Call<ApiResponse<Void>> changeUserName(@Body AccountInfoRequest request );




}
