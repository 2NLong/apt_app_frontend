package com.ptithcm.apt.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.apt.models.auth.request.LoginRequest;
import com.ptithcm.apt.models.auth.response.LoginResponse;
import com.ptithcm.apt.repositoris.AuthRepository;

public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<LoginResponse> _loginResult = new MutableLiveData<>();
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    // Expose read-only LiveData ra ngoài
    public final LiveData<LoginResponse> loginResult = _loginResult;
    public final LiveData<String> errorMessage = _errorMessage;
    public final LiveData<Boolean> isLoading = _isLoading;

    public LoginViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Validate input rồi gọi Repository để đăng nhập.
     */
    public void login(String username, String password) {
        if (username == null || username.isEmpty()) {
            _errorMessage.setValue("Vui lòng nhập tên đăng nhập");
            return;
        }
        if (password == null || password.isEmpty()) {
            _errorMessage.setValue("Vui lòng nhập mật khẩu");
            return;
        }
        if (password.length() < 6) {
            _errorMessage.setValue("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }

        authRepository.login(new LoginRequest(username, password),
                _loginResult, _errorMessage, _isLoading);
    }

    /**
     * Xoá thông báo lỗi (tránh hiển thị lại khi rotate screen).
     */
    public void clearError() {
        _errorMessage.setValue(null);
    }
}
