package com.ptithcm.apt.viewmodel.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.apt.repositoris.ServiceConfigRepository;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final ServiceConfigRepository repository;

    public HomeViewModelFactory(ServiceConfigRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
