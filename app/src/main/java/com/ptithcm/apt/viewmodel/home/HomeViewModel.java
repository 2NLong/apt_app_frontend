package com.ptithcm.apt.viewmodel.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.apt.models.adminserviceconfig.AdminServiceConfigResponse;
import com.ptithcm.apt.models.adminserviceconfig.ServiceConfigResponse;
import com.ptithcm.apt.repositoris.ServiceConfigRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final ServiceConfigRepository repository;
    private final MutableLiveData<List<ServiceConfigResponse>> activeServiceConfigs = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    
    private final MutableLiveData<LocalDate> selectedDate = new MutableLiveData<>(LocalDate.now().withDayOfMonth(1));

    public HomeViewModel(ServiceConfigRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<ServiceConfigResponse>> getActiveServiceConfigs() {
        return activeServiceConfigs;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<LocalDate> getSelectedDate() {
        return selectedDate;
    }

    public void nextMonth() {
        LocalDate current = selectedDate.getValue();
        if (current != null) {
            selectedDate.setValue(current.plusMonths(1));
            refreshData();
        }
    }

    public void previousMonth() {
        LocalDate current = selectedDate.getValue();
        if (current != null) {
            selectedDate.setValue(current.minusMonths(1));
            refreshData();
        }
    }

    public void refreshData() {
        LocalDate current = selectedDate.getValue();
        if (current != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateStr = current.withDayOfMonth(1).format(formatter);
            repository.getActiveServiceConfigs(dateStr, activeServiceConfigs, errorMessage, isLoading);
        }
    }

}
