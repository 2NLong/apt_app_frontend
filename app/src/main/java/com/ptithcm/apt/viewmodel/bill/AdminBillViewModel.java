package com.ptithcm.apt.viewmodel.bill;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.enums.RentStatus;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.bill.response.CreateBillResponse;
import com.ptithcm.apt.models.rentinvoice.response.RentInvoiceDetailResponse;
import com.ptithcm.apt.models.rentinvoice.response.RentInvoiceListResponse;
import com.ptithcm.apt.models.bill.response.AdminBillDetailResponse;
import com.ptithcm.apt.models.bill.response.BillApartmentResponse;
import com.ptithcm.apt.models.bill.response.BillListResponse;
import com.ptithcm.apt.models.bill.response.BillPreviousMonthlyMetricResponse;
import com.ptithcm.apt.models.bill.response.BillServiceConfigResponse;
import com.ptithcm.apt.models.bill.request.CreateBillRequest;
import com.ptithcm.apt.repositoris.AdminBillRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminBillViewModel extends ViewModel {
    private final AdminBillRepository repository;

    // LiveData cho Danh sách (AdminBillFragment)
    private final MutableLiveData<List<BillListResponse>> _bills = new MutableLiveData<>();
    public LiveData<List<BillListResponse>> bills = _bills;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    // LiveData cho Chi tiết (AdminBillDetailFragment)
    private final MutableLiveData<AdminBillDetailResponse> _billDetail = new MutableLiveData<>();
    public LiveData<AdminBillDetailResponse> billDetail = _billDetail;


    private final MutableLiveData<List<BillApartmentResponse>> _billApartments = new MutableLiveData<>();
    public LiveData<List<BillApartmentResponse>> billApartments = _billApartments;

    private final MutableLiveData<BillPreviousMonthlyMetricResponse> _previousMetric = new MutableLiveData<>();
    public LiveData<BillPreviousMonthlyMetricResponse> previousMetric = _previousMetric;

    // Hàm gọi
    public void fetchApartmentsForBill() {
        repository.getApartmentsForBill(_billApartments, _error);
    }

    public void fetchPreviousMetrics(Long apartmentId) {
        repository.getPreviousMetrics(apartmentId, _previousMetric, _error);
    }

    private final MutableLiveData<List<BillServiceConfigResponse>> _serviceConfigs = new MutableLiveData<>();
    public LiveData<List<BillServiceConfigResponse>> serviceConfigs = _serviceConfigs;

    public void fetchServiceConfigs(String date) {
        repository.getServiceConfigs(date, _serviceConfigs);
    }

    private final MutableLiveData<Boolean> _isCreateSuccess = new MutableLiveData<>();
    public LiveData<Boolean> isCreateSuccess = _isCreateSuccess;

    // Sửa lại hàm này trong file AdminBillViewModel.java của bạn
    public void createBill(CreateBillRequest request) {
        // ViewModel chỉ việc "ra lệnh" cho Repository làm việc
        // và truyền các biến LiveData có sẵn của mình vào để Repository cập nhật
        repository.createBill(request, _isLoading, _isCreateSuccess, _error);
    }

    private final MutableLiveData<List<RentInvoiceListResponse>> _rentInvoices = new MutableLiveData<>();
    public LiveData<List<RentInvoiceListResponse>> rentInvoices = _rentInvoices;

    public void fetchRentInvoices(Integer month, Integer year, Long apartmentId, BillStatus status) {
        RentStatus rentStatus = RentStatus.valueOf(status.name());

        repository.getRentInvoices(month, year, apartmentId, rentStatus, 0, 50,
                _rentInvoices, _error, _isLoading);
    }

    private final MutableLiveData<RentInvoiceDetailResponse> _rentDetail = new MutableLiveData<>();
    public LiveData<RentInvoiceDetailResponse> rentDetail = _rentDetail;

    public void fetchRentDetail(Long id) {
        repository.getRentInvoiceDetail(id, _rentDetail, _error);
    }

    private final MutableLiveData<Boolean> _updateStatusSuccess = new MutableLiveData<>();
    public LiveData<Boolean> updateStatusSuccess = _updateStatusSuccess;

    public void approveBill(Long billId) {
        // Mặc định set là PAID như bạn yêu cầu
        _updateStatusSuccess.setValue(false);
        repository.updateBillStatus(billId, BillStatus.PAID, _updateStatusSuccess, _error);
    }

    private final MutableLiveData<Boolean> _updateRentSuccess = new MutableLiveData<>();
    public LiveData<Boolean> updateRentSuccess = _updateRentSuccess;

    public void approveRentInvoice(Long rentId) {
        // Duyệt mặc định là PAID (Enum RentStatus)
        _updateRentSuccess.setValue(false);
        repository.updateRentStatus(rentId, com.ptithcm.apt.enums.RentStatus.PAID, _updateRentSuccess, _error);
    }

    public AdminBillViewModel(AdminBillRepository repository) {
        this.repository = repository;
    }

    public void fetchBills(Integer month, Integer year, Long apartmentId, BillStatus status) {
        repository.getBills(month, year, apartmentId, status, 0, 50, _bills, _error, _isLoading);
    }

    public void fetchBillDetail(Long id) {
        // Sử dụng phương thức getBillDetail mà chúng ta đã viết thêm trong Repository
        repository.getBillDetail(id, _billDetail, _error, _isLoading);
    }
}
