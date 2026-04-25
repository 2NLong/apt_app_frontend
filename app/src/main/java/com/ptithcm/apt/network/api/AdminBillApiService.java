package com.ptithcm.apt.network.api;

import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.enums.RentStatus;
import com.ptithcm.apt.models.bill.request.UpdateBillStatusRequest;
import com.ptithcm.apt.models.bill.response.UpdateBillStatusResponse;
import com.ptithcm.apt.models.rentinvoice.RentInvoiceDetail;
import com.ptithcm.apt.models.rentinvoice.RentInvoiceList;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.auth.response.PageResponse;
import com.ptithcm.apt.models.bill.response.AdminBillDetailResponse;
import com.ptithcm.apt.models.bill.response.BillApartmentResponse;
import com.ptithcm.apt.models.bill.response.BillListResponse;
import com.ptithcm.apt.models.bill.response.BillPreviousMonthlyMetricResponse;
import com.ptithcm.apt.models.bill.response.BillServiceConfigResponse;
import com.ptithcm.apt.models.bill.request.CreateBillRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminBillApiService {
    @GET("api/public/v1/admin/bills")
    Call<ApiResponse<PageResponse<BillListResponse>>> getBillsByAdmin(
            @Query("month") Integer month,
            @Query("year") Integer year,
            @Query("apartmentId") Long apartmentId,
            @Query("status") BillStatus status,
            @Query("page") Integer page,
            @Query("size") Integer size);

    @GET("api/public/v1/admin/bills/{id}")
    Call<ApiResponse<AdminBillDetailResponse>> getBillDetail(@Path("id") Long id);

    // Lấy danh sách căn hộ
    @GET("api/v1/admin/apartments")
    Call<PageResponse<BillApartmentResponse>> getBillApartments(@Query("page") int page);

    // Lấy chỉ số cũ
    @GET("api/public/v1/admin/monthly-metrics")
    Call<ApiResponse<BillPreviousMonthlyMetricResponse>> getPreviousMetrics(
            @Query("apartmentId") Long apartmentId
    );

    // Lấy bảng giá dịch vụ theo ngày
    @GET("api/v1/service-configs/active")
    Call<ApiResponse<List<BillServiceConfigResponse>>> getActiveConfigs(
            @Query("date") String date
    );

    @POST("api/public/v1/admin/bills")
    Call<ApiResponse<Void>> createBill(@Body CreateBillRequest request);

    @GET("api/public/v1/admin/rent-invoices")
    Call<ApiResponse<PageResponse<RentInvoiceList>>> getRentInvoices(
            @Query("month") Integer month,
            @Query("year") Integer year,
            @Query("apartmentId") Long apartmentId,
            @Query("status") RentStatus status,
            @Query("page") Integer page,
            @Query("size") Integer size);

    @GET("api/public/v1/admin/rent-invoices/{id}")
    Call<ApiResponse<RentInvoiceDetail>> getRentInvoiceDetail(@Path("id") Long id);

    @POST("api/public/v1/admin/bills/{billId}/update-status")
    Call<ApiResponse<UpdateBillStatusResponse>> updateBillStatus(
            @Path("billId") Long billId,
            @Body UpdateBillStatusRequest request
    );

}


