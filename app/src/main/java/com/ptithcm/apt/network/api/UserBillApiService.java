package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.auth.response.PageResponse;
import com.ptithcm.apt.models.bill.response.BillListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserBillApiService {
    @GET("api/public/v1/bills/my-bills")
    Call<ApiResponse<PageResponse<BillListResponse>>> getMyBills(
            @Query("month") Integer month,
            @Query("year") Integer year,
            @Query("apartmentId") Long apartmentId,
            @Query("status") String status,
            @Query("page") int page,
            @Query("size") int size
    );
}
