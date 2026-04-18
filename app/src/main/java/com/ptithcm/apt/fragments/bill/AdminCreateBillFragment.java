package com.ptithcm.apt.fragments.bill;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.apt.R;
import com.ptithcm.apt.models.bill.BillApartment;
import com.ptithcm.apt.viewmodel.admin.AdminBillViewModel;
import com.ptithcm.apt.viewmodel.admin.AdminBillViewModelFactory; // Đảm bảo bạn có Factory này

import java.util.Calendar;

public class AdminCreateBillFragment extends Fragment {

    private AdminBillViewModel viewModel;

    // Views
    private ImageView btnBack;
    private AutoCompleteTextView spinnerApartment;
    private TextInputEditText etBillingDate, etElectricNew, etWaterNew;
    private TextView tvElectricOld, tvWaterOld, tvManagementFee, tvSanitationFee;
    private Button btnCreateBill;

    // Data biến tạm
    private int selectedMonth, selectedYear;
    private Long selectedApartmentId;

    public AdminCreateBillFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_create_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupViewModel();
        setupListeners();
        observeViewModel();

        // Load danh sách căn hộ ngay khi vào màn hình
        viewModel.fetchApartmentsForBill();
    }

    private void initViews(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        spinnerApartment = view.findViewById(R.id.spinnerApartment);
        etBillingDate = view.findViewById(R.id.etBillingDate);
        etElectricNew = view.findViewById(R.id.etElectricNew);
        etWaterNew = view.findViewById(R.id.etWaterNew);
        tvElectricOld = view.findViewById(R.id.tvElectricOld);
        tvWaterOld = view.findViewById(R.id.tvWaterOld);
        tvManagementFee = view.findViewById(R.id.tvManagementFee);
        tvSanitationFee = view.findViewById(R.id.tvSanitationFee);
        btnCreateBill = view.findViewById(R.id.btnCreateBill);
    }

    private void setupViewModel() {
        // Khởi tạo ViewModel (Sử dụng Factory nếu Repository cần truyền vào)
        AdminBillViewModelFactory factory = new AdminBillViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(AdminBillViewModel.class);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        // Mở Dialog chọn tháng năm
        etBillingDate.setOnClickListener(v -> showMonthYearDialog());

        // Chọn căn hộ từ Spinner
        spinnerApartment.setOnItemClickListener((parent, v, position, id) -> {
            BillApartment selected = (BillApartment) parent.getItemAtPosition(position);
            selectedApartmentId = selected.getId();

            // Lấy chỉ số cũ của căn hộ này
            viewModel.fetchPreviousMetrics(selectedApartmentId);
        });

        // Nút lưu hóa đơn
        btnCreateBill.setOnClickListener(v -> {
            // Logic gửi API tạo hóa đơn sẽ viết ở đây
            Toast.makeText(getContext(), "Đang xử lý lưu hóa đơn...", Toast.LENGTH_SHORT).show();
        });
    }

    private void observeViewModel() {
        // Quan sát danh sách căn hộ đổ vào Spinner
        viewModel.billApartments.observe(getViewLifecycleOwner(), apartments -> {
            if (apartments != null) {
                ArrayAdapter<BillApartment> adapter = new ArrayAdapter<>(
                        requireContext(), android.R.layout.simple_dropdown_item_1line, apartments);
                spinnerApartment.setAdapter(adapter);
            }
        });

        // Quan sát chỉ số cũ khi chọn căn hộ
        viewModel.previousMetric.observe(getViewLifecycleOwner(), metric -> {
            if (metric != null) {
                String electricity = (metric.getLatestElectricity() != null)
                        ? metric.getLatestElectricity().toString() : "0.0";
                String water = (metric.getLatestWater() != null)
                        ? metric.getLatestWater().toString() : "0.0";

                tvElectricOld.setText(electricity);
                tvWaterOld.setText(water);

                // Điền sẵn vào ô nhập mới
                etElectricNew.setText(electricity);
                etWaterNew.setText(water);
            }
        });

        // Lắng nghe lỗi
        viewModel.error.observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showMonthYearDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_month_year_picker, null);
        NumberPicker pickerMonth = view.findViewById(R.id.pickerMonth);
        NumberPicker pickerYear = view.findViewById(R.id.pickerYear);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        Calendar cal = Calendar.getInstance();
        pickerMonth.setMinValue(1);
        pickerMonth.setMaxValue(12);
        pickerMonth.setValue(cal.get(Calendar.MONTH) + 1);

        pickerYear.setMinValue(2024);
        pickerYear.setMaxValue(2030);
        pickerYear.setValue(cal.get(Calendar.YEAR));

        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(view).create();

        btnConfirm.setOnClickListener(v -> {
            selectedMonth = pickerMonth.getValue();
            selectedYear = pickerYear.getValue();
            etBillingDate.setText(String.format("Tháng %02d/%d", selectedMonth, selectedYear));
            dialog.dismiss();
        });

        dialog.show();
    }
}