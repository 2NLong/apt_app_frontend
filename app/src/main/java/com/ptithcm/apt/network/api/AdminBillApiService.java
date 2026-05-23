package com.ptithcm.apt.network.api;

import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.enums.RentStatus;
import com.ptithcm.apt.models.bill.request.UpdateBillStatusRequest;
import com.ptithcm.apt.models.bill.response.CreateBillResponse;
import com.ptithcm.apt.models.bill.response.UpdateBillStatusResponse;
import com.ptithcm.apt.models.rentinvoice.request.UpdateRentInvoiceStatusRequest;
import com.ptithcm.apt.models.rentinvoice.response.RentInvoiceDetailResponse;
import com.ptithcm.apt.models.rentinvoice.response.RentInvoiceListResponse;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.auth.response.PageResponse;
import com.ptithcm.apt.models.bill.response.AdminBillDetailResponse;
import com.ptithcm.apt.models.bill.response.BillApartmentResponse;
import com.ptithcm.apt.models.bill.response.BillListResponse;
import com.ptithcm.apt.models.bill.response.BillPreviousMonthlyMetricResponse;
import com.ptithcm.apt.models.bill.response.BillServiceConfigResponse;
import com.ptithcm.apt.models.bill.request.CreateBillRequest;
import com.ptithcm.apt.models.rentinvoice.response.UpdateRentInvoiceStatusResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminBillApiService {
    @GET("api/v1/bills")
    Call<ApiResponse<PageResponse<BillListResponse>>> getBillsByAdmin(
            @Query("month") Integer month,
            @Query("year") Integer year,
            @Query("apartmentId") Long apartmentId,
            @Query("status") BillStatus status,
            @Query("page") Integer page,
            @Query("size") Integer size);

    @GET("api/v1/bills/{id}")
    Call<ApiResponse<AdminBillDetailResponse>> getBillDetail(@Path("id") Long id);

    // Lấy danh sách căn hộ
    @GET("api/v1/apartments")
    Call<PageResponse<BillApartmentResponse>> getBillApartments(@Query("page") int page);

    // Lấy chỉ số cũ
    @GET("api/v1/monthly-metrics")
    Call<ApiResponse<BillPreviousMonthlyMetricResponse>> getPreviousMetrics(
            @Query("apartmentId") Long apartmentId
    );

    // Lấy bảng giá dịch vụ theo ngày
    @GET("api/v1/service-configs/active")
    Call<ApiResponse<List<BillServiceConfigResponse>>> getActiveConfigs(
            @Query("date") String date
    );

    @POST("api/v1/bills")
    Call<ApiResponse<CreateBillResponse>> createBill(@Body CreateBillRequest request);

    @GET("api/v1/rent-invoices")
    Call<ApiResponse<PageResponse<RentInvoiceListResponse>>> getRentInvoices(
            @Query("month") Integer month,
            @Query("year") Integer year,
            @Query("apartmentId") Long apartmentId,
            @Query("status") RentStatus status,
            @Query("page") Integer page,
            @Query("size") Integer size);

    @GET("api/v1/rent-invoices/{id}")
    Call<ApiResponse<RentInvoiceDetailResponse>> getRentInvoiceDetail(@Path("id") Long id);

    @PATCH("api/v1/bills/{id}/status")
    Call<ApiResponse<UpdateBillStatusResponse>> updateBillStatus(
            @Path("id") Long id,
            @Body UpdateBillStatusRequest request
    );

    @PATCH("api/v1/rent-invoices/{id}/status")
    Call<ApiResponse<UpdateRentInvoiceStatusResponse>> updateRentInvoiceStatus(
            @Path("id") Long rentInvoiceId,
            @Body UpdateRentInvoiceStatusRequest request
    );

}


