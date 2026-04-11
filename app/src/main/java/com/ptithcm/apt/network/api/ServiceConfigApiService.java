package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.adminserviceconfig.AdminServiceConfigResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceConfigApiService {
    @GET("api/v1/service-configs/admin-dashboard")
    Call<ApiResponse<List<AdminServiceConfigResponse>>> getAdminDashboardServiceConfigs();
}
