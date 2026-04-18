package com.ptithcm.apt.viewmodel.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.apt.models.adminserviceconfig.AdminServiceConfigResponse;
import com.ptithcm.apt.models.adminserviceconfig.ServiceConfigResponse;
import com.ptithcm.apt.repositoris.ServiceConfigRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeViewModel extends ViewModel {

    private final ServiceConfigRepository repository;
    private final MutableLiveData<List<ServiceConfigResponse>> activeServiceConfigs = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    
    private final MutableLiveData<Calendar> selectedDate = new MutableLiveData<>(Calendar.getInstance());

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
    
    public LiveData<Calendar> getSelectedDate() {
        return selectedDate;
    }

    public void nextMonth() {
        Calendar cal = selectedDate.getValue();
        if (cal != null) {
            Calendar next = (Calendar) cal.clone();
            next.add(Calendar.MONTH, 1);
            selectedDate.setValue(next);
            refreshData();
        }
    }

    public void previousMonth() {
        Calendar cal = selectedDate.getValue();
        if (cal != null) {
            Calendar prev = (Calendar) cal.clone();
            prev.add(Calendar.MONTH, -1);
            selectedDate.setValue(prev);
            refreshData();
        }
    }

    public void refreshData() {
        Calendar cal = selectedDate.getValue();
        if (cal != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01", Locale.getDefault());
            String dateStr = sdf.format(cal.getTime());
            repository.getActiveServiceConfigs(dateStr, activeServiceConfigs, errorMessage, isLoading);
        }
    }

}
