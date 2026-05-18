package com.ptithcm.apt.viewmodel.bill;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.apt.network.api.UserBillApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;
import com.ptithcm.apt.repositoris.UserBillRepository;

public class UserBillViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserBillViewModel.class)) {
            UserBillApiService service = RetrofitClient.getInstance().createService(UserBillApiService.class);

            return (T) new UserBillViewModel(new UserBillRepository(service));
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
