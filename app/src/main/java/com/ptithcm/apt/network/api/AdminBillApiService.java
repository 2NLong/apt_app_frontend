package com.ptithcm.apt.network.api;

import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.enums.RentStatus;
import com.ptithcm.apt.models.auth.rentinvoice.RentInvoiceDetail;
import com.ptithcm.apt.models.auth.rentinvoice.RentInvoiceList;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.auth.response.PageResponse;
import com.ptithcm.apt.models.bill.AdminBillDetail;
import com.ptithcm.apt.models.bill.BillApartment;
import com.ptithcm.apt.models.bill.BillList;
import com.ptithcm.apt.models.bill.BillPreviousMonthlyMetric;
import com.ptithcm.apt.models.bill.BillServiceConfig;
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
    Call<ApiResponse<PageResponse<BillList>>> getBillsByAdmin(
            @Query("month") Integer month,
            @Query("year") Integer year,
            @Query("apartmentId") Long apartmentId,
            @Query("status") BillStatus status,
            @Query("page") Integer page,
            @Query("size") Integer size);

    @GET("api/public/v1/admin/bills/{id}")
    Call<ApiResponse<AdminBillDetail>> getBillDetail(@Path("id") Long id);

    // Lấy danh sách căn hộ
    @GET("api/v1/admin/apartments")
    Call<PageResponse<BillApartment>> getBillApartments(@Query("page") int page);

    // Lấy chỉ số cũ
    @GET("api/public/v1/admin/monthly-metrics")
    Call<ApiResponse<BillPreviousMonthlyMetric>> getPreviousMetrics(
            @Query("apartmentId") Long apartmentId
    );

    // Lấy bảng giá dịch vụ theo ngày
    @GET("api/v1/service-configs/active")
    Call<ApiResponse<List<BillServiceConfig>>> getActiveConfigs(
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

}


