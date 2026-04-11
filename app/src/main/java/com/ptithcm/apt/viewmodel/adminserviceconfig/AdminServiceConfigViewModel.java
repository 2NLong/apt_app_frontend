package com.ptithcm.apt.viewmodel.adminserviceconfig;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.adminserviceconfig.AdminServiceConfigResponse;
import com.ptithcm.apt.network.api.ServiceConfigApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminServiceConfigViewModel extends ViewModel {

    private final ServiceConfigApiService serviceConfigApiService;
    private final MutableLiveData<List<AdminServiceConfigResponse>> _serviceConfigsData = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    public final LiveData<List<AdminServiceConfigResponse>> serviceConfigsData = _serviceConfigsData;
    public final LiveData<String> error = _error;
    public final LiveData<Boolean> isLoading = _isLoading;

    public AdminServiceConfigViewModel(ServiceConfigApiService serviceConfigApiService) {
        this.serviceConfigApiService = serviceConfigApiService;
    }

    public void fetchServiceConfigs() {
        _isLoading.setValue(true);
        serviceConfigApiService.getAdminDashboardServiceConfigs().enqueue(new Callback<ApiResponse<List<AdminServiceConfigResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<AdminServiceConfigResponse>>> call,
                    Response<ApiResponse<List<AdminServiceConfigResponse>>> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        _serviceConfigsData.setValue(response.body().getData());
                    } else {
                        _error.setValue(response.body().getMessage());
                    }
                } else {
                    _error.setValue("Lỗi khi lấy cấu hình dịch vụ: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<AdminServiceConfigResponse>>> call, Throwable t) {
                _isLoading.setValue(false);
                _error.setValue("Lỗi mạng: " + t.getLocalizedMessage());
            }
        });
    }
}
