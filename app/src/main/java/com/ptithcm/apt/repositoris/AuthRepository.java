package com.ptithcm.apt.repositoris;

import androidx.lifecycle.MutableLiveData;

import com.ptithcm.apt.models.auth.request.LoginRequest;
import com.ptithcm.apt.models.auth.response.LoginResponse;
import com.ptithcm.apt.network.api.AuthApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final AuthApiService authApiService;

    public AuthRepository() {
        authApiService = RetrofitClient.getInstance().createService(AuthApiService.class);
    }

    /**
     * Gọi API đăng nhập.
     *
     * @param loginRequest  Thông tin username và password
     * @param loginResult   LiveData trả về LoginResponse khi thành công
     * @param errorMessage  LiveData trả về thông báo lỗi khi thất bại
     * @param isLoading     LiveData để bật/tắt trạng thái loading
     */
    public void login(LoginRequest loginRequest,
                      MutableLiveData<LoginResponse> loginResult,
                      MutableLiveData<String> errorMessage,
                      MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);

        authApiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    loginResult.postValue(response.body());
                } else {
                    // Xử lý lỗi HTTP (401, 403, v.v.)
                    String msg;
                    if (response.code() == 401) {
                        msg = "Sai tên đăng nhập hoặc mật khẩu";
                    } else if (response.code() == 403) {
                        msg = "Tài khoản không có quyền truy cập";
                    } else {
                        msg = "Đăng nhập thất bại (lỗi " + response.code() + ")";
                    }
                    errorMessage.postValue(msg);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
}
