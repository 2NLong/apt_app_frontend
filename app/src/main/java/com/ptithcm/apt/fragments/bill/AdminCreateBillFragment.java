package com.ptithcm.apt.fragments.bill;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.ptithcm.apt.models.bill.response.BillApartmentResponse;
import com.ptithcm.apt.models.bill.response.BillServiceConfigResponse;
import com.ptithcm.apt.models.bill.request.CreateBillRequest;
import com.ptithcm.apt.utils.ToastUtils;
import com.ptithcm.apt.viewmodel.admin.AdminBillViewModel;
import com.ptithcm.apt.viewmodel.admin.AdminBillViewModelFactory; // Đảm bảo bạn có Factory này

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    private BigDecimal priceElec = BigDecimal.ZERO, priceWater = BigDecimal.ZERO;
    private BigDecimal priceMng = BigDecimal.ZERO, priceSani = BigDecimal.ZERO;
    private Double currentArea = 0.0;

    private TextView tvElectricFee, tvWaterFee, tvTotalAmount;

    private TextView tvDisplayArea, tvUnitPriceElec, tvUnitPriceWater, tvUnitPriceMng, tvUnitPriceSani;

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

        // 1. Lấy ngày hiện tại để truyền vào API (Định dạng yyyy-MM-dd)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());

        viewModel.fetchServiceConfigs(today);

        // 2. Lấy tháng/năm để hiển thị lên EditText và lưu biến tạm
        Calendar cal = Calendar.getInstance();
        selectedMonth = cal.get(Calendar.MONTH) + 1;
        selectedYear = cal.get(Calendar.YEAR);

        etBillingDate.setText(String.format("Tháng %02d/%d", selectedMonth, selectedYear));


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

        tvElectricFee = view.findViewById(R.id.tvElectricFee);
        tvWaterFee = view.findViewById(R.id.tvWaterFee);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);

        tvDisplayArea = view.findViewById(R.id.tvDisplayArea);
        tvUnitPriceElec = view.findViewById(R.id.tvUnitPriceElec);
        tvUnitPriceWater = view.findViewById(R.id.tvUnitPriceWater);
        tvUnitPriceMng = view.findViewById(R.id.tvUnitPriceMng);
        tvUnitPriceSani = view.findViewById(R.id.tvUnitPriceSani);

        TextWatcher calculatorWatcher = new TextWatcher() {
            @Override public void afterTextChanged(Editable s) { calculateAllFees(); }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };

        etElectricNew.addTextChangedListener(calculatorWatcher);
        etWaterNew.addTextChangedListener(calculatorWatcher);
    }

    private void calculateAllFees() {
        try {
            // 1. Tiền điện = (Mới - Cũ) * 3000
            BigDecimal oldE = new BigDecimal(tvElectricOld.getText().toString());
            String newEStr = etElectricNew.getText().toString();
            BigDecimal feeE = BigDecimal.ZERO;
            if (!newEStr.isEmpty()) {
                BigDecimal usageE = new BigDecimal(newEStr).subtract(oldE);
                if (usageE.signum() > 0) feeE = usageE.multiply(priceElec);
            }
            tvElectricFee.setText(formatVND(feeE));

            // 2. Tiền nước = (Mới - Cũ) * 15000
            BigDecimal oldW = new BigDecimal(tvWaterOld.getText().toString());
            String newWStr = etWaterNew.getText().toString();
            BigDecimal feeW = BigDecimal.ZERO;
            if (!newWStr.isEmpty()) {
                BigDecimal usageW = new BigDecimal(newWStr).subtract(oldW);
                if (usageW.signum() > 0) feeW = usageW.multiply(priceWater);
            }
            tvWaterFee.setText(formatVND(feeW));

            // 3. Phí quản lý = Diện tích * 10000
            BigDecimal feeMng = BigDecimal.valueOf(currentArea).multiply(priceMng);
            tvManagementFee.setText(formatVND(feeMng));

            // 4. Phí vệ sinh = 50000 (Cố định)
            tvSanitationFee.setText(formatVND(priceSani));

            // 5. TỔNG CỘNG
            BigDecimal total = feeE.add(feeW).add(feeMng).add(priceSani);
            tvTotalAmount.setText(formatVND(total));

        } catch (Exception e) {
            Log.e("CALC_ERROR", "Lỗi tính toán: " + e.getMessage());
        }
    }

    private String formatVND(BigDecimal amount) {
        return String.format("%,.0f VNĐ", amount);
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
            BillApartmentResponse selected = (BillApartmentResponse) parent.getItemAtPosition(position);
            selectedApartmentId = selected.getId();
            currentArea = selected.getArea();

            tvDisplayArea.setText(currentArea + " m2");


            viewModel.fetchPreviousMetrics(selectedApartmentId);
            calculateAllFees();
        });

        btnCreateBill.setOnClickListener(v -> {
            if (selectedApartmentId == null) {
                ToastUtils.showErrorToast(requireContext(), "Vui lòng chọn căn hộ");
                return;
            }

            String elecStr = etElectricNew.getText().toString();
            String waterStr = etWaterNew.getText().toString();

            if (elecStr.isEmpty() || waterStr.isEmpty()) {
                ToastUtils.showErrorToast(requireContext(), "Vui lòng nhập đầy đủ chỉ số mới!");
                return;
            }


            BigDecimal oldE = new BigDecimal(tvElectricOld.getText().toString());
            BigDecimal newE = new BigDecimal(etElectricNew.getText().toString());
            if (newE.compareTo(oldE) < 0) {
                ToastUtils.showErrorToast(requireContext(), "Số điện mới không được nhỏ hơn số cũ!");
                return;
            }

            // 2. Tạo đối tượng Request
            BigDecimal elecNew = new BigDecimal(elecStr);
            BigDecimal waterNew = new BigDecimal(waterStr);

            CreateBillRequest request = new CreateBillRequest(
                    selectedApartmentId,
                    selectedMonth,
                    selectedYear,
                    elecNew,
                    waterNew
            );
            viewModel.createBill(request);
        });
    }

    private void observeViewModel() {
        // Quan sát danh sách căn hộ đổ vào Spinner
        viewModel.billApartments.observe(getViewLifecycleOwner(), apartments -> {
            if (apartments != null) {
                ArrayAdapter<BillApartmentResponse> adapter = new ArrayAdapter<>(
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

        viewModel.serviceConfigs.observe(getViewLifecycleOwner(), configs -> {
            if (configs != null) {
                for (BillServiceConfigResponse c : configs) {
                    switch (c.getServiceCode()) {
                        case "ELECTRICITY":
                            priceElec = c.getUnitPrice();
                            break;
                        case "WATER":
                            priceWater = c.getUnitPrice();
                            break;
                        case "MANAGEMENT":
                            priceMng = c.getUnitPrice();
                            break;
                        case "SANITATION":
                            priceSani = c.getUnitPrice();
                            break;
                    }
                }
                calculateAllFees();
            }
        });

        viewModel.serviceConfigs.observe(getViewLifecycleOwner(), configs -> {
            if (configs != null) {
                for (BillServiceConfigResponse c : configs) {
                    String priceStr = String.format("%,.0fđ", c.getUnitPrice());
                    switch (c.getServiceCode()) {
                        case "ELECTRICITY":
                            priceElec = c.getUnitPrice();
                            tvUnitPriceElec.setText("Điện: " + priceStr + "/kWh");
                            break;
                        case "WATER":
                            priceWater = c.getUnitPrice();
                            tvUnitPriceWater.setText("Nước: " + priceStr + "/m3");
                            break;
                        case "MANAGEMENT":
                            priceMng = c.getUnitPrice();
                            tvUnitPriceMng.setText("Quản lý: " + priceStr + "/m2");
                            break;
                        case "SANITATION":
                            priceSani = c.getUnitPrice();
                            tvUnitPriceSani.setText("Vệ sinh: " + priceStr + "/tháng");
                            break;
                    }
                }
                calculateAllFees();
            }
        });

        viewModel.isCreateSuccess.observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                // 1. Hiển thị Toast thành công bằng Util của bạn
                ToastUtils.showSuccessToast(requireContext(), "Tạo hóa đơn thành công!");

                // 2. Chuyển ngược về fragment danh sách
                // onBackPressed sẽ lấy fragment từ BackStack ra (màn hình danh sách cũ)
                requireActivity().getOnBackPressedDispatcher().onBackPressed();

                // 3. Reset lại trạng thái để tránh bị gọi lại khi quay lại fragment này
                // (Tùy chọn: nếu ViewModel dùng chung, bạn nên có hàm resetSuccessState)
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