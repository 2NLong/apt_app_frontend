package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.auth.request.LoginRequest;
import com.ptithcm.apt.models.auth.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {

    @POST("api/v1/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
