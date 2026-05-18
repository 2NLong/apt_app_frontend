package com.ptithcm.apt.repositoris;

import androidx.lifecycle.MutableLiveData;

import com.ptithcm.apt.models.auth.request.ChangePasswordRequest;
import com.ptithcm.apt.utils.ErrorUtils;
import com.ptithcm.apt.models.auth.request.ForgotPasswordRequest;
import com.ptithcm.apt.models.auth.request.LoginRequest;
import com.ptithcm.apt.models.auth.request.RefreshTokenRequest;
import com.ptithcm.apt.models.auth.request.ResetPasswordRequest;
import com.ptithcm.apt.models.auth.request.VerifyOtpRequest;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.auth.response.LoginResponse;
import com.ptithcm.apt.network.api.AuthApiService;
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
                                user != null ? user.getRole() : null,
                                user != null ? user.getResidentName() : null);

                        loginResult.postValue(loginResponse);
                    } else {
                        errorMessage.postValue(
                                apiResponse.getMessage() != null
                                        ? apiResponse.getMessage()
                                        : "Đăng nhập thất bại");
                    }
                } else {
                    String msg = ErrorUtils.getErrorMessage(response,
                            "Đăng nhập thất bại (Lỗi: " + response.code() + ")");
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
     * Gọi API đăng nhập bằng Google.
     */
    public void googleLogin(com.ptithcm.apt.models.auth.request.GoogleLoginRequest request,
            MutableLiveData<LoginResponse> loginResult,
            MutableLiveData<String> errorMessage,
            MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);

        authApiService.googleLogin(request).enqueue(new Callback<ApiResponse<LoginResponse>>() {
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
                                user != null ? user.getRole() : null,
                                user != null ? user.getResidentName() : null);

                        loginResult.postValue(loginResponse);
                    } else {
                        errorMessage.postValue(
                                apiResponse.getMessage() != null
                                        ? apiResponse.getMessage()
                                        : "Đăng nhập Google thất bại");
                    }
                } else {
                    String msg = ErrorUtils.getErrorMessage(response,
                            "Đăng nhập Google thất bại (Lỗi: " + response.code() + ")");
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

                                // Cập nhật session
                                LoginResponse.UserInfo user = loginResponse.getUser();
                                if (user != null) {
                                    sessionManager.saveSession(
                                            loginResponse.getAccessToken(),
                                            loginResponse.getRefreshToken(),
                                            user.getId(),
                                            user.getUsername(),
                                            user.getRole(),
                                            user.getResidentName());
                                } else {
                                    sessionManager.updateTokens(
                                            loginResponse.getAccessToken(),
                                            loginResponse.getRefreshToken());
                                }

                                refreshResult.postValue(loginResponse);
                            } else {
                                errorMessage.postValue(apiResponse.getMessage());
                            }
                        } else {
                            String msg = ErrorUtils.getErrorMessage(response,
                                    "Làm mới token thất bại (Lỗi: " + response.code() + ")");
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

    /**
     * Đổi mật khẩu (khi đã đăng nhập).
     */
    public void changePassword(ChangePasswordRequest request,
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
                    String msg = ErrorUtils.getErrorMessage(response,
                            "Đổi mật khẩu thất bại (Lỗi: " + response.code() + ")");
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

    /**
     * Gửi OTP đặt lại mật khẩu đến email người dùng.
     */
    public void forgotPassword(String email,
            MutableLiveData<Boolean> result,
            MutableLiveData<String> errorMessage,
            MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);

        authApiService.forgotPassword(new ForgotPasswordRequest(email))
                .enqueue(new Callback<ApiResponse<Void>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call,
                            Response<ApiResponse<Void>> response) {
                        isLoading.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<Void> apiResponse = response.body();
                            if (apiResponse.getStatus() == 200) {
                                result.postValue(true);
                            } else {
                                errorMessage.postValue(
                                        apiResponse.getMessage() != null
                                                ? apiResponse.getMessage()
                                                : "Gửi OTP thất bại");
                            }
                        } else {
                            String msg = ErrorUtils.getErrorMessage(response,
                                    "Gửi OTP thất bại (Lỗi: " + response.code() + ")");
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

    /**
     * Xác thực mã OTP — trả về resetToken nếu hợp lệ.
     */
    public void verifyOtp(String email, String otp,
            MutableLiveData<String> resetTokenResult,
            MutableLiveData<String> errorMessage,
            MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);

        authApiService.verifyOtp(new VerifyOtpRequest(email, otp))
                .enqueue(new Callback<ApiResponse<String>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<String>> call,
                            Response<ApiResponse<String>> response) {
                        isLoading.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<String> apiResponse = response.body();
                            if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                                resetTokenResult.postValue(apiResponse.getData());
                            } else {
                                errorMessage.postValue(
                                        apiResponse.getMessage() != null
                                                ? apiResponse.getMessage()
                                                : "Xác thực OTP thất bại");
                            }
                        } else {
                            String msg = ErrorUtils.getErrorMessage(response,
                                    "Xác thực OTP thất bại (Lỗi: " + response.code() + ")");
                            errorMessage.postValue(msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                        isLoading.postValue(false);
                        errorMessage.postValue("Lỗi mạng: " + t.getLocalizedMessage());
                    }
                });
    }

    /**
     * Đặt lại mật khẩu mới bằng resetToken đã được xác thực.
     */
    public void resetPassword(String resetToken, String newPassword,
            MutableLiveData<Boolean> result,
            MutableLiveData<String> errorMessage,
            MutableLiveData<Boolean> isLoading) {

        isLoading.postValue(true);

        authApiService.resetPassword(new ResetPasswordRequest(resetToken, newPassword))
                .enqueue(new Callback<ApiResponse<Void>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call,
                            Response<ApiResponse<Void>> response) {
                        isLoading.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<Void> apiResponse = response.body();
                            if (apiResponse.getStatus() == 200) {
                                result.postValue(true);
                            } else {
                                errorMessage.postValue(
                                        apiResponse.getMessage() != null
                                                ? apiResponse.getMessage()
                                                : "Đặt lại mật khẩu thất bại");
                            }
                        } else {
                            String msg = ErrorUtils.getErrorMessage(response,
                                    "Đặt lại mật khẩu thất bại (Lỗi: " + response.code() + ")");
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
