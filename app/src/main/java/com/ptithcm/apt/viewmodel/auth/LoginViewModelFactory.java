package com.ptithcm.apt.viewmodel.auth;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.apt.network.api.AuthApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;
import com.ptithcm.apt.repositoris.AuthRepository;
import com.ptithcm.apt.utils.SessionManager;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public LoginViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            // Khởi tạo các phụ thuộc tại đây (Manual Dependency Injection)
            SessionManager sessionManager = SessionManager.getInstance(context);
            AuthApiService authApiService = RetrofitClient.getInstance().createPublicService(AuthApiService.class);
            AuthRepository authRepository = new AuthRepository(authApiService, sessionManager);

            return (T) new LoginViewModel(authRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
