package com.ptithcm.apt.viewmodel.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.apt.network.api.ProfileApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            ProfileApiService apiService = RetrofitClient.getInstance().createService(ProfileApiService.class);
            return (T) new ProfileViewModel(apiService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
