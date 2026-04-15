package com.ptithcm.apt.viewmodel.admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.models.bill.BillList;
import com.ptithcm.apt.repositoris.AdminBillRepository;

import java.util.List;

public class AdminBillViewModel extends ViewModel {
    private final AdminBillRepository repository;
    
    private final MutableLiveData<List<BillList>> _bills = new MutableLiveData<>();
    public LiveData<List<BillList>> bills = _bills;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    public AdminBillViewModel(AdminBillRepository repository) {
        this.repository = repository;
    }

    public void fetchBills(Integer month, Integer year, BillStatus status) {
        repository.getBills(month, year, status, 0, 50, _bills, _error, _isLoading);
    }
}
