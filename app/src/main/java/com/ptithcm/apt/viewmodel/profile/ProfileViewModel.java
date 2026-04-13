package com.ptithcm.apt.viewmodel.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.profile.ProfileDashboardResponse;
import com.ptithcm.apt.network.api.ProfileApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {

    private final ProfileApiService profileApiService;
    private final MutableLiveData<ProfileDashboardResponse> _profileData = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    public final LiveData<ProfileDashboardResponse> profileData = _profileData;
    public final LiveData<String> error = _error;
    public final LiveData<Boolean> isLoading = _isLoading;

    public ProfileViewModel(ProfileApiService profileApiService) {
        this.profileApiService = profileApiService;
    }

    public void fetchProfileDashboard() {
        _isLoading.setValue(true);
        profileApiService.getProfileDashboard().enqueue(new Callback<ApiResponse<ProfileDashboardResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ProfileDashboardResponse>> call,
                    Response<ApiResponse<ProfileDashboardResponse>> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        _profileData.setValue(response.body().getData());
                    } else {
                        _error.setValue(response.body().getMessage());
                    }
                } else {
                    _error.setValue("Lỗi khi lấy thông tin cá nhân: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProfileDashboardResponse>> call, Throwable t) {
                _isLoading.setValue(false);
                _error.setValue("Lỗi mạng: " + t.getLocalizedMessage());
            }
        });
    }
}
