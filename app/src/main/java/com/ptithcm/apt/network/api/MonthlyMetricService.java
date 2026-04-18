package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.bill.AdminBillDetail;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MonthlyMetricService {
    @GET("api/public/v1/admin/bills/{id}")
    Call<ApiResponse<AdminBillDetail>> getBillDetail(@Path("id") Long id);
}