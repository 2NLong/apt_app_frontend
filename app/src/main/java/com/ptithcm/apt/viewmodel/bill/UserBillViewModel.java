package com.ptithcm.apt.viewmodel.bill;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.apt.models.bill.response.BillListResponse;
import com.ptithcm.apt.repositoris.UserBillRepository;

import java.util.List;

public class UserBillViewModel extends ViewModel {
    private final UserBillRepository repository;

    private final MutableLiveData<List<BillListResponse>> _bills = new MutableLiveData<>();
    public LiveData<List<BillListResponse>> bills = _bills;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    public UserBillViewModel(UserBillRepository repository) {
        this.repository = repository;
    }

    public void fetchMyBills(Integer month, Integer year, String status) {
        repository.getMyBills(month, year, status, _bills, _error, _isLoading);
    }
}
