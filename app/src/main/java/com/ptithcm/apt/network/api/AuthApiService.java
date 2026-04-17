package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.auth.request.ChangePasswordRequest;
import com.ptithcm.apt.models.auth.request.LoginRequest;
import com.ptithcm.apt.models.auth.request.RefreshTokenRequest;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.auth.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface AuthApiService {

    @POST("api/v1/auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest loginRequest);

    @POST("api/v1/auth/refresh-token")
    Call<ApiResponse<LoginResponse>> refreshToken(@Body RefreshTokenRequest request);

    @POST("api/v1/auth/logout")
    Call<ApiResponse<Void>> logout();

    @PATCH("api/v1/auth/change-password")
    Call<ApiResponse<Void>> changePassword(@Body ChangePasswordRequest request);
}
