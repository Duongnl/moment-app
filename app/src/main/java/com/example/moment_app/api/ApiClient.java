package com.example.moment_app.api;

import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();

                        // Lấy token từ SharedPreferences
                        SharedPreferences sharedPreferences = context.getSharedPreferences("MomentPrefs", Context.MODE_PRIVATE);
                        String token = sharedPreferences.getString("access_token", "");

                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Accept", "application/json");

                        if (token != null && !token.isEmpty()) {
                            requestBuilder.header("Authorization", "Bearer " + token);
                        }

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
//                    .baseUrl("https://be.moment.liber.vn/api/")
                    .baseUrl("http://192.168.1.5:8080/api/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
