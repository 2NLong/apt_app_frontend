package com.ptithcm.apt.repositoris;

import androidx.lifecycle.MutableLiveData;

import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.profile.ProfileDashboardResponse;
import com.ptithcm.apt.network.api.ProfileApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    private final ProfileApiService profileApiService;

    public ProfileRepository(ProfileApiService profileApiService) {
        this.profileApiService = profileApiService;
    }

    /**
     * @param profileData  LiveData trả về ProfileDashboardResponse
     * @param errorMessage LiveData trả về thông báo lỗi
     * @param isLoading    LiveData để bật/tắt trạng thái loading
     */
    public void getProfileDashboard(
            MutableLiveData<ProfileDashboardResponse> profileData,
            MutableLiveData<String> errorMessage,
            MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);

        profileApiService.getProfileDashboard().enqueue(new Callback<ApiResponse<ProfileDashboardResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ProfileDashboardResponse>> call,
                    Response<ApiResponse<ProfileDashboardResponse>> response) {
                isLoading.postValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ProfileDashboardResponse> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        profileData.postValue(apiResponse.getData());
                    } else {
                        errorMessage.postValue(apiResponse.getMessage());
                    }
                } else {
                    errorMessage.postValue("Lỗi khi lấy thông tin cá nhân: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProfileDashboardResponse>> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi mạng: " + t.getLocalizedMessage());
            }
        });
    }
}
