package com.example.projectv2.api;

import com.example.projectv2.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("api/user/login")
    Call<User> login(@Body User user);

    @POST("api/user/register")
    Call<User> register(@Body User user);
} 