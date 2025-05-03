package com.example.moment_app.api;

import com.example.moment_app.models.request.AccountInfoRequest;
import com.example.moment_app.models.request.ChangePasswordRequest;
import com.example.moment_app.models.request.FriendFilterRequest;
import com.example.moment_app.models.request.FriendInviteRequest;
import com.example.moment_app.models.request.RegisterRequest;
import com.example.moment_app.models.response.AccountResponse;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.AuthenticationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface AccountApiService {

    @POST("account/register")
    Call<ApiResponse<AuthenticationResponse>> register(@Body RegisterRequest request );


    @PUT("account/setting")
    Call<ApiResponse<Void>> updateAccountInfo(@Body AccountInfoRequest request );


    @PUT("account/change-username")
    Call<ApiResponse<Void>> changeUserName(@Body AccountInfoRequest request );


    @PUT("account/change-password")
    Call<ApiResponse<Void>> changePassword(@Body ChangePasswordRequest request );

    @GET("account/search")
    Call<ApiResponse<List<AccountResponse>>> searchFriend(@Query("s") String keyword);


    @POST("account/friend/add")
    Call <ApiResponse<AccountResponse>> addFriend(@Body FriendInviteRequest friendInviteRequest);

    @PUT("account/friend/status")
    Call <ApiResponse<AccountResponse>> changeFriendStatus(@Body FriendInviteRequest friendInviteRequest);

    @POST("account/friend")
    Call<ApiResponse<List<AccountResponse>>>  getFriends(@Body FriendFilterRequest friendFilterRequest);

    @POST("account/friend/received")
    Call<ApiResponse<List<AccountResponse>>>  getFriendsReceived(@Body FriendFilterRequest friendFilterRequest);

    @POST("account/friend/sent")
    Call<ApiResponse<List<AccountResponse>>>  getFriendsSent(@Body FriendFilterRequest friendFilterRequest);









}
