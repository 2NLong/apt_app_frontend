package com.ptithcm.apt.viewmodel.adminserviceconfig;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.apt.network.api.ServiceConfigApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;
import com.ptithcm.apt.repositoris.ServiceConfigRepository;

public class AdminServiceConfigViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AdminServiceConfigViewModel.class)) {
            ServiceConfigApiService apiService = RetrofitClient.getInstance()
                    .createService(ServiceConfigApiService.class);
            ServiceConfigRepository repository = new ServiceConfigRepository(apiService);
            return (T) new AdminServiceConfigViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
