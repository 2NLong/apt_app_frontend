package com.ptithcm.apt.viewmodel.bill;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.apt.repositoris.UserBillRepository;

public class UserBillViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserBillViewModel.class)) {
            return (T) new UserBillViewModel(new UserBillRepository());
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
