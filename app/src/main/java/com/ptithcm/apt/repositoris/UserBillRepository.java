package com.ptithcm.apt.repositoris;

import androidx.lifecycle.MutableLiveData;

import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.auth.response.PageResponse;
import com.ptithcm.apt.models.bill.response.BillListResponse;
import com.ptithcm.apt.network.api.UserBillApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBillRepository {
    private final UserBillApiService apiService;

    public UserBillRepository() {
        // Sử dụng createService để tự động đính kèm Token
        this.apiService = RetrofitClient.getInstance().createService(UserBillApiService.class);
    }

    public void getMyBills(Integer month, Integer year, String status,
            MutableLiveData<List<BillListResponse>> billsData,
            MutableLiveData<String> errorData,
            MutableLiveData<Boolean> isLoading) {
        isLoading.postValue(true);
        apiService.getMyBills(month, year, null, status, 0, 50)
                .enqueue(new Callback<ApiResponse<PageResponse<BillListResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<PageResponse<BillListResponse>>> call,
                            Response<ApiResponse<PageResponse<BillListResponse>>> response) {
                        isLoading.postValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            billsData.postValue(response.body().getData().getContent());
                        } else {
                            errorData.postValue("Không thể tải danh sách hóa đơn");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<PageResponse<BillListResponse>>> call, Throwable t) {
                        isLoading.postValue(false);
                        errorData.postValue(t.getMessage());
                    }
                });
    }
}
