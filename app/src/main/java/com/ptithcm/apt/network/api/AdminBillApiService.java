package com.ptithcm.apt.network.api;

import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.auth.response.PageResponse;
import com.ptithcm.apt.models.bill.AdminBillDetail;
import com.ptithcm.apt.models.bill.BillList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminBillApiService {
    @GET("api/public/v1/admin/bills")
    Call<ApiResponse<PageResponse<BillList>>> getBillsByAdmin(
            @Query("month") Integer month,
            @Query("year") Integer year,
            @Query("apartmentId") Long apartmentId,
            @Query("status") BillStatus status,
            @Query("page") Integer page,
            @Query("size") Integer size);

    @GET("api/public/v1/admin/bills/{id}")
    Call<ApiResponse<AdminBillDetail>> getBillDetail(@Path("id") Long id);

}
