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
import retrofit2.http.Query;

public interface ServiceConfigApiService {
    @GET("api/v1/service-configs/admin-dashboard")
    Call<ApiResponse<List<AdminServiceConfigResponse>>> getAdminServiceConfigs();

    @POST("api/v1/service-configs/upcoming")
    Call<ApiResponse<Void>> updateServicePrice(@Body ServicePriceUpdateRequest request);

    @DELETE("api/v1/service-configs/{serviceCode}/upcoming")
    Call<ApiResponse<Void>> cancelUpdate(@Path("serviceCode") String serviceCode);

    @GET("api/v1/service-configs/active")
    Call<ApiResponse<List<AdminServiceConfigResponse>>> getServicePricesByDate(@Query("date") String date);
}
