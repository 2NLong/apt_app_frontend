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
    private final MutableLiveData<List<ServiceConfigResponse>> _activeServiceConfigs = new MutableLiveData<>();
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> _selectedDate = new MutableLiveData<>(LocalDate.now().withDayOfMonth(1));

    public final LiveData<List<ServiceConfigResponse>> activeServiceConfigs = _activeServiceConfigs;
    public final LiveData<String> errorMessage = _errorMessage;
    public final LiveData<Boolean> isLoading = _isLoading;
    public final LiveData<LocalDate> selectedDate = _selectedDate;

    public HomeViewModel(ServiceConfigRepository repository) {
        this.repository = repository;
    }


    public void nextMonth() {
        LocalDate current = _selectedDate.getValue();
        if (current != null) {
            _selectedDate.setValue(current.plusMonths(1));
            refreshData();
        }
    }

    public void previousMonth() {
        LocalDate current = _selectedDate.getValue();
        if (current != null) {
            _selectedDate.setValue(current.minusMonths(1));
            refreshData();
        }
    }

    public void refreshData() {
        LocalDate current = _selectedDate.getValue();
        if (current != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateStr = current.withDayOfMonth(1).format(formatter);
            repository.getActiveServiceConfigs(dateStr, _activeServiceConfigs, _errorMessage, _isLoading);
        }
    }

}
