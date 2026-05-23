package com.ptithcm.apt.network.api;

import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.auth.response.PageResponse;
import com.ptithcm.apt.models.bill.response.UserBillApartmentResponse;
import com.ptithcm.apt.models.bill.response.UserBillDetailResponse;
import com.ptithcm.apt.models.bill.response.UserBillListResponse;
import com.ptithcm.apt.models.rentinvoice.response.UserRentInvoiceDetailResponse;
import com.ptithcm.apt.models.rentinvoice.response.UserRentInvoiceListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserBillApiService {
    @GET("api/v1/bills/me")
    Call<ApiResponse<PageResponse<UserBillListResponse>>> getMyBills(
            @Query("month") Integer month,
            @Query("year") Integer year,
            @Query("apartmentId") Long apartmentId,
            @Query("status") BillStatus status,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("api/v1/residents/me")
    Call<List<UserBillApartmentResponse>> getMyApartments();

    @GET("api/v1/bills/me/{id}")
    Call<ApiResponse<UserBillDetailResponse>> getBillDetail(@Path("id") Long id);

    @GET("api/v1/rent-invoices/me")
    Call<ApiResponse<PageResponse<UserRentInvoiceListResponse>>> getMyRentInvoices(
            @Query("month") Integer month,
            @Query("year") Integer year,
            @Query("apartmentId") Long apartmentId,
            @Query("status") String status,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("api/v1/rent-invoices/me/{id}")
    Call<ApiResponse<UserRentInvoiceDetailResponse>> getRentInvoiceDetail(@Path("id") int id);
}