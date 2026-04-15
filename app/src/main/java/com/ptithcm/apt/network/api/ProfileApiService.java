package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.profile.ProfileDashboardResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProfileApiService {
    @GET("api/v1/profile")
    Call<ApiResponse<ProfileDashboardResponse>> getProfileDashboard();
}
