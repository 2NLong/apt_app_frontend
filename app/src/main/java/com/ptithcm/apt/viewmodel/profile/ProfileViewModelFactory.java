package com.ptithcm.apt.viewmodel.profile;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.apt.network.api.AuthApiService;
import com.ptithcm.apt.network.api.ProfileApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;
import com.ptithcm.apt.repositoris.AuthRepository;
import com.ptithcm.apt.repositoris.ProfileRepository;
import com.ptithcm.apt.utils.SessionManager;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public ProfileViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            ProfileApiService profileApiService = RetrofitClient.getInstance().createService(ProfileApiService.class);
            AuthApiService authApiService = RetrofitClient.getInstance().createService(AuthApiService.class);
            SessionManager sessionManager = SessionManager.getInstance(context);
            ProfileRepository profileRepository = new ProfileRepository(profileApiService);
            AuthRepository authRepository = new AuthRepository(authApiService, sessionManager);
            return (T) new ProfileViewModel(profileRepository, authRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}