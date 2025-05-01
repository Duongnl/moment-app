package com.example.moment_app.api;

import com.example.moment_app.models.request.PhotoFilterRequest;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.PhotoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PhotoApiService {
    @POST("photo")
    Call<ApiResponse<List<PhotoResponse>>> getListPhotoFriends(@Body PhotoFilterRequest request);


}
