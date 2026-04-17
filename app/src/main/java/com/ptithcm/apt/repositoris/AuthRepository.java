package com.ptithcm.apt.repositoris;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.ptithcm.apt.models.auth.request.LoginRequest;
import com.ptithcm.apt.models.auth.request.RefreshTokenRequest;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.auth.response.LoginResponse;
import com.ptithcm.apt.network.api.AuthApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;
import com.ptithcm.apt.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final AuthApiService authApiService;
    private final SessionManager sessionManager;

    public AuthRepository(AuthApiService authApiService, SessionManager sessionManager) {
        this.authApiService = authApiService;
        this.sessionManager = sessionManager;
    }

    /**
     * Gọi API đăng nhập, tự lưu session nếu thành công.
     *
     * @param loginRequest Thông tin username và password
     * @param loginResult  LiveData trả về LoginResponse khi thành công
     * @param errorMessage LiveData trả về thông báo lỗi khi thất bại
     * @param isLoading    LiveData để bật/tắt trạng thái loading
     */
    public void login(LoginRequest loginRequest,
            MutableLiveData<LoginResponse> loginResult,
            MutableLiveData<String> errorMessage,
            MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);

        authApiService.login(loginRequest).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call,
                    Response<ApiResponse<LoginResponse>> response) {
                isLoading.postValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<LoginResponse> apiResponse = response.body();

                    if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                        LoginResponse loginResponse = apiResponse.getData();

                        LoginResponse.UserInfo user = loginResponse.getUser();
                        sessionManager.saveSession(
                                loginResponse.getAccessToken(),
                                loginResponse.getRefreshToken(),
                                user != null ? user.getId() : null,
                                user != null ? user.getUsername() : null,
                                user != null ? user.getRole() : null);

                        loginResult.postValue(loginResponse);
                    } else {
                        errorMessage.postValue(
                                apiResponse.getMessage() != null
                                        ? apiResponse.getMessage()
                                        : "Đăng nhập thất bại");
                    }
                } else {
                    String msg;
                    switch (response.code()) {
                        case 401:
                            msg = "Sai tên đăng nhập hoặc mật khẩu";
                            break;
                        case 403:
                            msg = "Tài khoản bị khoá hoặc không có quyền";
                            break;
                        default:
                            msg = "Đăng nhập thất bại (Lỗi: " + response.code() + ")";
                    }
                    errorMessage.postValue(msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi mạng: " + t.getLocalizedMessage());
            }
        });
    }

    /**
     * Làm mới token thủ công.
     */
    public void refreshToken(String refreshToken,
            MutableLiveData<LoginResponse> refreshResult,
            MutableLiveData<String> errorMessage,
            MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);

        authApiService.refreshToken(new RefreshTokenRequest(refreshToken))
                .enqueue(new Callback<ApiResponse<LoginResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<LoginResponse>> call,
                            Response<ApiResponse<LoginResponse>> response) {
                        isLoading.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<LoginResponse> apiResponse = response.body();

                            if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                                LoginResponse loginResponse = apiResponse.getData();

                                // Cập nhật session mới
                                sessionManager.updateTokens(
                                        loginResponse.getAccessToken(),
                                        loginResponse.getRefreshToken());

                                refreshResult.postValue(loginResponse);
                            } else {
                                errorMessage.postValue(apiResponse.getMessage());
                            }
                        } else {
                            errorMessage.postValue("Làm mới token thất bại: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                        isLoading.postValue(false);
                        errorMessage.postValue("Lỗi mạng: " + t.getLocalizedMessage());
                    }
                });
    }

    /**
     * Đăng xuất: Xóa session và gọi API logout.
     */
    public void logout(MutableLiveData<Boolean> logoutResult, MutableLiveData<String> errorMessage) {
        sessionManager.clearSession();

        authApiService.logout().enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    logoutResult.postValue(true);
                } else {
                    // errorMessage.postValue("Đăng xuất từ server thất bại: " + response.code());
                    logoutResult.postValue(true);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                // errorMessage.postValue("Lỗi mạng khi đăng xuất: " + t.getLocalizedMessage());
                logoutResult.postValue(true);
            }
        });
    }

    public void changePassword(com.ptithcm.apt.models.auth.request.ChangePasswordRequest request,
            MutableLiveData<Boolean> changePasswordResult,
            MutableLiveData<String> errorMessage,
            MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);

        authApiService.changePassword(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                isLoading.postValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        changePasswordResult.postValue(true);
                    } else {
                        errorMessage.postValue(apiResponse.getMessage());
                    }
                } else {
                    String msg = "Đổi mật khẩu thất bại (Lỗi: " + response.code() + ")";
                    if (response.code() == 400) {
                        msg = "Mật khẩu cũ không chính xác hoặc dữ liệu không hợp lệ";
                    }
                    errorMessage.postValue(msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi mạng: " + t.getLocalizedMessage());
            }
        });
    }
}
