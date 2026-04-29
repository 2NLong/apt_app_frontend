package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.bill.response.AdminBillDetailResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MonthlyMetricService {
    @GET("api/public/v1/admin/bills/{id}")
    Call<ApiResponse<AdminBillDetailResponse>> getBillDetail(@Path("id") Long id);
}