package com.ptithcm.apt.viewmodel.adminserviceconfig;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.apt.models.adminserviceconfig.AdminServiceConfigResponse;
import com.ptithcm.apt.models.adminserviceconfig.ServicePriceUpdateRequest;
import com.ptithcm.apt.repositoris.ServiceConfigRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AdminServiceConfigViewModel extends ViewModel {

    private final ServiceConfigRepository repository;
    private final MutableLiveData<List<AdminServiceConfigResponse>> _serviceConfigsData = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _updateSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _cancelSuccess = new MutableLiveData<>();

    public final LiveData<List<AdminServiceConfigResponse>> serviceConfigsData = _serviceConfigsData;
    public final LiveData<String> error = _error;
    public final LiveData<Boolean> isLoading = _isLoading;
    public final LiveData<Boolean> updateSuccess = _updateSuccess;
    public final LiveData<Boolean> cancelSuccess = _cancelSuccess;

    public AdminServiceConfigViewModel(ServiceConfigRepository repository) {
        this.repository = repository;
    }

    public void fetchServiceConfigs() {
        repository.getAdminDashboardServiceConfigs(_serviceConfigsData, _error, _isLoading);
    }

    public void updateServicePrice(ServicePriceUpdateRequest request) {
        repository.updateServicePrice(request, _updateSuccess, _error, _isLoading);
    }

    public void cancelUpcomingUpdate(String serviceCode) {
        repository.cancelUpcomingUpdate(serviceCode, _cancelSuccess, _error, _isLoading);
    }

    public void validateAndUpdatePrice(ServicePriceUpdateRequest request, BigDecimal currentPrice) {
        // Kiểm tra ngày hiệu lực (Phải từ tháng sau trở đi)
        LocalDate today = LocalDate.now();
        LocalDate startOfNextMonth = today.withDayOfMonth(1).plusMonths(1);

        // yyyy-MM-01
        LocalDate effectiveDate = LocalDate.parse(request.getEffectiveFrom());

        if (effectiveDate.isBefore(startOfNextMonth)) {
            _error.setValue("Tháng áp dụng phải bắt đầu từ tháng " +
                    startOfNextMonth.getMonthValue() + "/" + startOfNextMonth.getYear() + " trở đi.");
            return;
        }

        // Kiểm tra đơn giá (Phải khác giá hiện tại)
        if (currentPrice != null && request.getNewPrice().compareTo(currentPrice) == 0) {
            _error.setValue("Dịch vụ đang được áp dụng mức giá này rồi.");
            return;
        }

        updateServicePrice(request);
    }

    public void resetUpdateStatus() {
        _updateSuccess.setValue(false);
    }

    public void resetCancelStatus() {
        _cancelSuccess.setValue(false);
    }
}
