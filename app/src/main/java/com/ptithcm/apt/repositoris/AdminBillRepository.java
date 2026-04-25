package com.ptithcm.apt.repositoris;

import androidx.lifecycle.MutableLiveData;

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
import com.ptithcm.apt.network.api.AdminBillApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminBillRepository {
    private final AdminBillApiService apiService;

    public AdminBillRepository(AdminBillApiService apiService) {
        this.apiService = apiService;
    }

    public void getBills(Integer month, Integer year,Long apartmentId, BillStatus status, Integer page, Integer size,
                         MutableLiveData<List<BillListResponse>> billsData,
                         MutableLiveData<String> errorMessage,
                         MutableLiveData<Boolean> isLoading) {
        
        isLoading.postValue(true);
        apiService.getBillsByAdmin(month, year, apartmentId, status, page, size)
                .enqueue(new Callback<ApiResponse<PageResponse<BillListResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageResponse<BillListResponse>>> call, Response<ApiResponse<PageResponse<BillListResponse>>> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PageResponse<BillListResponse>> apiResponse = response.body();
                    if (apiResponse.getData() != null) {
                        billsData.postValue(apiResponse.getData().getContent());
                    } else {
                        errorMessage.postValue("Không có dữ liệu");
                    }
                } else {
                    errorMessage.postValue("Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageResponse<BillListResponse>>> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi mạng: " + t.getLocalizedMessage());
            }
        });
    }

    public void getBillDetail(Long id,
                              MutableLiveData<AdminBillDetailResponse> detailData,
                              MutableLiveData<String> errorData,
                              MutableLiveData<Boolean> loadingData) {

        loadingData.setValue(true);

        apiService.getBillDetail(id).enqueue(new Callback<ApiResponse<AdminBillDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AdminBillDetailResponse>> call, Response<ApiResponse<AdminBillDetailResponse>> response) {
                loadingData.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    // Kiểm tra status từ API (0 là thành công theo JSON bạn gửi)
                    if (response.body().getStatus() == 200) {
                        detailData.setValue(response.body().getData());
                    } else {
                        errorData.setValue(response.body().getMessage());
                    }
                } else {
                    errorData.setValue("Không thể lấy thông tin chi tiết: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AdminBillDetailResponse>> call, Throwable t) {
                loadingData.setValue(false);
                errorData.setValue("Lỗi kết nối server: " + t.getMessage());
            }
        });
    }

    public void getApartmentsForBill(MutableLiveData<List<BillApartmentResponse>> data, MutableLiveData<String> error) {
        apiService.getBillApartments(0).enqueue(new Callback<PageResponse<BillApartmentResponse>>() {
            @Override
            public void onResponse(Call<PageResponse<BillApartmentResponse>> call, Response<PageResponse<BillApartmentResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getContent());
                } else {
                    error.setValue("Không thể lấy danh sách căn hộ");
                }
            }
            @Override
            public void onFailure(Call<PageResponse<BillApartmentResponse>> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
    }

    public void getPreviousMetrics(Long apartmentId, MutableLiveData<BillPreviousMonthlyMetricResponse> data, MutableLiveData<String> error) {
        apiService.getPreviousMetrics(apartmentId).enqueue(new Callback<ApiResponse<BillPreviousMonthlyMetricResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<BillPreviousMonthlyMetricResponse>> call, Response<ApiResponse<BillPreviousMonthlyMetricResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getData());
                } else {
                    error.setValue("Chưa có chỉ số cũ cho căn hộ này");
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<BillPreviousMonthlyMetricResponse>> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
    }

    public void getServiceConfigs(String date, MutableLiveData<List<BillServiceConfigResponse>> data) {
        apiService.getActiveConfigs(date).enqueue(new Callback<ApiResponse<List<BillServiceConfigResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BillServiceConfigResponse>>> call, Response<ApiResponse<List<BillServiceConfigResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getData());
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<BillServiceConfigResponse>>> call, Throwable t) {}
        });
    }

    public void createBill(CreateBillRequest request, MutableLiveData<Boolean> isSuccess, MutableLiveData<String> error) {
        apiService.createBill(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    isSuccess.setValue(true);
                } else {
                    error.setValue("Lỗi khi tạo hóa đơn: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
    }

    // AdminBillRepository.java
    public void getRentInvoices(Integer month, Integer year, Long apartmentId, RentStatus status, Integer page, Integer size,
                                MutableLiveData<List<RentInvoiceList>> rentData,
                                MutableLiveData<String> errorMessage,
                                MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);
        apiService.getRentInvoices(month, year, apartmentId, status, page, size)
                .enqueue(new Callback<ApiResponse<PageResponse<RentInvoiceList>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<PageResponse<RentInvoiceList>>> call, Response<ApiResponse<PageResponse<RentInvoiceList>>> response) {
                        isLoading.postValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null) {
                                rentData.postValue(response.body().getData().getContent());
                            } else {
                                errorMessage.postValue("Không có dữ liệu tiền thuê");
                            }
                        } else {
                            errorMessage.postValue("Lỗi: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<PageResponse<RentInvoiceList>>> call, Throwable t) {
                        isLoading.postValue(false);
                        errorMessage.postValue("Lỗi kết nối: " + t.getMessage());
                    }
                });
    }

    public void getRentInvoiceDetail(Long id, MutableLiveData<RentInvoiceDetail> detailData, MutableLiveData<String> error) {
        apiService.getRentInvoiceDetail(id).enqueue(new Callback<ApiResponse<RentInvoiceDetail>>() {
            @Override
            public void onResponse(Call<ApiResponse<RentInvoiceDetail>> call, Response<ApiResponse<RentInvoiceDetail>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    detailData.postValue(response.body().getData());
                } else {
                    error.postValue("Không thể lấy chi tiết hóa đơn thuê");
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<RentInvoiceDetail>> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }

    public void updateBillStatus(Long billId, BillStatus status,
                                 MutableLiveData<Boolean> isSuccess,
                                 MutableLiveData<String> error) {
        UpdateBillStatusRequest req = new UpdateBillStatusRequest(status);

        apiService.updateBillStatus(billId, req).enqueue(new Callback<ApiResponse<UpdateBillStatusResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UpdateBillStatusResponse>> call, Response<ApiResponse<UpdateBillStatusResponse>> response) {
                if (response.isSuccessful()) {
                    isSuccess.postValue(true);
                } else {
                    error.postValue("Lỗi hệ thống: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UpdateBillStatusResponse>> call, Throwable t) {
                error.postValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
