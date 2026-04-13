package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.adminserviceconfig.AdminServiceConfigResponse;
import com.ptithcm.apt.models.adminserviceconfig.ServicePriceUpdateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceConfigApiService {
    @GET("api/v1/service-configs/admin-dashboard")
    Call<ApiResponse<List<AdminServiceConfigResponse>>> getAdminDashboardServiceConfigs();

    @POST("api/v1/service-configs/update-price")
    Call<ApiResponse<Void>> updateServicePrice(@Body ServicePriceUpdateRequest request);

    @DELETE("api/v1/service-configs/cancel-update/{serviceCode}")
    Call<ApiResponse<Void>> cancelUpdate(@Path("serviceCode") String serviceCode);
}
