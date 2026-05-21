package com.ptithcm.apt.repositoris;

import androidx.lifecycle.MutableLiveData;
import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.enums.RentStatus;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.auth.response.PageResponse;
import com.ptithcm.apt.models.bill.response.UserBillApartmentResponse;
import com.ptithcm.apt.models.bill.response.UserBillDetailResponse;
import com.ptithcm.apt.models.bill.response.UserBillListResponse;
import com.ptithcm.apt.models.rentinvoice.response.UserRentInvoiceDetailResponse;
import com.ptithcm.apt.models.rentinvoice.response.UserRentInvoiceListResponse;
import com.ptithcm.apt.network.api.UserBillApiService;
import com.ptithcm.apt.utils.ErrorUtils;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBillRepository {
    private final UserBillApiService apiService;

    public UserBillRepository(UserBillApiService apiService) {
        this.apiService = apiService;
    }

    public void getMyBills(Integer month, Integer year, Long apartmentId, BillStatus status,
            MutableLiveData<List<UserBillListResponse>> data,
            MutableLiveData<String> error,
            MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.getMyBills(month, year, apartmentId, status, 0, 50).enqueue(new Callback<ApiResponse<PageResponse<UserBillListResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageResponse<UserBillListResponse>>> call, Response<ApiResponse<PageResponse<UserBillListResponse>>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getData().getContent());
                } else {
                    String msg = ErrorUtils.getErrorMessage(response, "Lỗi tải danh sách hóa đơn: " + response.code());
                    error.setValue(msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageResponse<UserBillListResponse>>> call, Throwable t) {
                loading.setValue(false);
                error.setValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    public void getMyApartments(MutableLiveData<List<UserBillApartmentResponse>> data, MutableLiveData<String> error) {
        apiService.getMyApartments().enqueue(new Callback<List<UserBillApartmentResponse>>() {
            @Override
            public void onResponse(Call<List<UserBillApartmentResponse>> call, Response<List<UserBillApartmentResponse>> response) {
                if (response.isSuccessful()) data.setValue(response.body());
                else {
                    String msg = ErrorUtils.getErrorMessage(response, "Không thể lấy danh sách căn hộ: " + response.code());
                    error.setValue(msg);
                }
            }
            @Override
            public void onFailure(Call<List<UserBillApartmentResponse>> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
    }

    public void getBillDetail(Long id, MutableLiveData<UserBillDetailResponse> data, MutableLiveData<String> error) {
        apiService.getBillDetail(id).enqueue(new Callback<ApiResponse<UserBillDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserBillDetailResponse>> call, Response<ApiResponse<UserBillDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getData());
                } else {
                    String msg = ErrorUtils.getErrorMessage(response, "Không thể tải chi tiết hóa đơn: " + response.code());
                    error.setValue(msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserBillDetailResponse>> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
    }

    public void getMyRentInvoices(Integer month, Integer year, Long apartmentId, BillStatus status,
            MutableLiveData<List<UserRentInvoiceListResponse>> data,
            MutableLiveData<String> error,
            MutableLiveData<Boolean> loading) {
        loading.setValue(true);

        String statusStr = (status != null) ? status.name() : null;

        apiService.getMyRentInvoices(month, year, apartmentId, statusStr, 0, 50)
                .enqueue(new Callback<ApiResponse<PageResponse<UserRentInvoiceListResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<PageResponse<UserRentInvoiceListResponse>>> call,
                            Response<ApiResponse<PageResponse<UserRentInvoiceListResponse>>> response) {
                        loading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            // Trả về danh sách content từ PageResponse
                            data.setValue(response.body().getData().getContent());
                        } else {
                            String msg = ErrorUtils.getErrorMessage(response, "Lỗi tải danh sách hóa đơn thuê: " + response.code());
                            error.setValue(msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<PageResponse<UserRentInvoiceListResponse>>> call, Throwable t) {
                        loading.setValue(false);
                        error.setValue("Lỗi kết nối: " + t.getMessage());
                    }
                });
    }

    public void getRentInvoiceDetail(int id,
            MutableLiveData<UserRentInvoiceDetailResponse> data,
            MutableLiveData<String> error) {
        apiService.getRentInvoiceDetail(id).enqueue(new Callback<ApiResponse<UserRentInvoiceDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserRentInvoiceDetailResponse>> call,
                    Response<ApiResponse<UserRentInvoiceDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Ánh xạ đúng dữ liệu từ trường data của ApiResponse
                    data.setValue(response.body().getData());
                } else {
                    String msg = ErrorUtils.getErrorMessage(response, "Không thể tải chi tiết hóa đơn thuê: " + response.code());
                    error.setValue(msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserRentInvoiceDetailResponse>> call, Throwable t) {
                error.setValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}