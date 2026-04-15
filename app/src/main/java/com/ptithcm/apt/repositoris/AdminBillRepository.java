package com.ptithcm.apt.repositoris;

import androidx.lifecycle.MutableLiveData;

import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.auth.response.PageResponse;
import com.ptithcm.apt.models.bill.BillList;
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

    public void getBills(Integer month, Integer year, BillStatus status, Integer page, Integer size,
                         MutableLiveData<List<BillList>> billsData,
                         MutableLiveData<String> errorMessage,
                         MutableLiveData<Boolean> isLoading) {
        
        isLoading.postValue(true);
        apiService.getBillsByAdmin(month, year, null, status, page, size)
                .enqueue(new Callback<ApiResponse<PageResponse<BillList>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageResponse<BillList>>> call, Response<ApiResponse<PageResponse<BillList>>> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PageResponse<BillList>> apiResponse = response.body();
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
            public void onFailure(Call<ApiResponse<PageResponse<BillList>>> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi mạng: " + t.getLocalizedMessage());
            }
        });
    }
}
