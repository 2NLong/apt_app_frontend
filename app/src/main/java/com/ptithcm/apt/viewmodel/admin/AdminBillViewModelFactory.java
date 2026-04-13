package com.ptithcm.apt.viewmodel.admin;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.apt.network.api.AdminBillApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;
import com.ptithcm.apt.repositoris.AdminBillRepository;

public class AdminBillViewModelFactory implements ViewModelProvider.Factory {

    public AdminBillViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AdminBillViewModel.class)) {
            // Admin API thường yêu cầu Token, dùng createAuthenticatedService
            AdminBillApiService apiService = RetrofitClient.getInstance().createService(AdminBillApiService.class);
            AdminBillRepository repository = new AdminBillRepository(apiService);

            return (T) new AdminBillViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
