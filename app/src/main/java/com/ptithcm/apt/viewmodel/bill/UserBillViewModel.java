package com.ptithcm.apt.viewmodel.bill;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.enums.RentStatus;
import com.ptithcm.apt.models.bill.response.UserBillApartmentResponse;
import com.ptithcm.apt.models.bill.response.UserBillListResponse;
import com.ptithcm.apt.models.rentinvoice.response.UserRentInvoiceListResponse;
import com.ptithcm.apt.repositoris.UserBillRepository;

import java.util.List;

public class UserBillViewModel extends ViewModel {
    private final UserBillRepository repository;
    private final MutableLiveData<List<UserBillListResponse>> _bills = new MutableLiveData<>();
    public LiveData<List<UserBillListResponse>> bills = _bills;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<String> error = new MutableLiveData<>();

    private final MutableLiveData<List<UserBillApartmentResponse>> _myApartments = new MutableLiveData<>();
    public LiveData<List<UserBillApartmentResponse>> myApartments = _myApartments;
    public void fetchMyApartments() {
        repository.getMyApartments(_myApartments, error);
    }

    private final MutableLiveData<List<UserRentInvoiceListResponse>> _rentInvoices = new MutableLiveData<>();
    public LiveData<List<UserRentInvoiceListResponse>> rentInvoices = _rentInvoices;

    public void fetchMyRentInvoices(Integer month, Integer year, Long apartmentId, BillStatus status) {
        repository.getMyRentInvoices(month, year, apartmentId, status, _rentInvoices, error, isLoading);
    }

    public UserBillViewModel(UserBillRepository repository) { this.repository = repository; }

    public void fetchMyBills(Integer month, Integer year, Long apartmentId, BillStatus status) {
        repository.getMyBills(month, year, apartmentId, status, _bills, error, isLoading);
    }
}
