package com.ptithcm.apt.repositoris;

import androidx.lifecycle.MutableLiveData;

import com.ptithcm.apt.models.adminserviceconfig.AdminServiceConfigResponse;
import com.ptithcm.apt.models.adminserviceconfig.ServiceConfigResponse;
import com.ptithcm.apt.models.adminserviceconfig.ServicePriceUpdateRequest;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.network.api.ServiceConfigApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceConfigRepository {

    private final ServiceConfigApiService apiService;

    public ServiceConfigRepository(ServiceConfigApiService apiService) {
        this.apiService = apiService;
    }

    public void getAdminDashboardServiceConfigs(
            MutableLiveData<List<AdminServiceConfigResponse>> result,
            MutableLiveData<String> errorMessage,
            MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);
        apiService.getAdminServiceConfigs()
                .enqueue(new Callback<ApiResponse<List<AdminServiceConfigResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<AdminServiceConfigResponse>>> call,
                            Response<ApiResponse<List<AdminServiceConfigResponse>>> response) {
                        isLoading.postValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<List<AdminServiceConfigResponse>> apiResponse = response.body();
                            if (apiResponse.getStatus() == 200) {
                                result.postValue(apiResponse.getData());
                            } else {
                                errorMessage.postValue(apiResponse.getMessage());
                            }
                        } else {
                            errorMessage.postValue("Lỗi khi lấy cấu hình dịch vụ: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<AdminServiceConfigResponse>>> call, Throwable t) {
                        isLoading.postValue(false);
                        errorMessage.postValue("Lỗi mạng: " + t.getLocalizedMessage());
                    }
                });
    }

    public void updateServicePrice(
            ServicePriceUpdateRequest request,
            MutableLiveData<Boolean> updateSuccess,
            MutableLiveData<String> errorMessage,
            MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);
        apiService.updateServicePrice(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        updateSuccess.postValue(true);
                    } else {
                        errorMessage.postValue(apiResponse.getMessage());
                    }
                } else {
                    errorMessage.postValue("Lỗi khi cập nhật giá: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi mạng: " + t.getLocalizedMessage());
            }
        });
    }

    public void cancelUpcomingUpdate(
            String serviceCode,
            MutableLiveData<Boolean> cancelSuccess,
            MutableLiveData<String> errorMessage,
            MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);
        apiService.cancelUpdate(serviceCode).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        cancelSuccess.postValue(true);
                    } else {
                        errorMessage.postValue(apiResponse.getMessage());
                    }
                } else {
                    errorMessage.postValue("Lỗi khi hủy lịch cập nhật: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi mạng: " + t.getLocalizedMessage());
            }
        });
    }

    public void getActiveServiceConfigs(
            String date,
            MutableLiveData<List<ServiceConfigResponse>> result,
            MutableLiveData<String> errorMessage,
            MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);
        apiService.getServicePricesByDate(date).enqueue(new Callback<ApiResponse<List<ServiceConfigResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ServiceConfigResponse>>> call, Response<ApiResponse<List<ServiceConfigResponse>>> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<ServiceConfigResponse>> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        result.postValue(apiResponse.getData());
                    } else {
                        errorMessage.postValue(apiResponse.getMessage());
                    }
                } else {
                    errorMessage.postValue("Lỗi khi lấy giá dịch vụ: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ServiceConfigResponse>>> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi mạng: " + t.getLocalizedMessage());
            }
        });
    }
}
