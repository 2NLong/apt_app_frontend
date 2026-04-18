package com.ptithcm.apt.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.serviceconfig.AdminServiceConfigAdapter;
import com.ptithcm.apt.models.adminserviceconfig.AdminServiceConfigResponse;
import com.ptithcm.apt.utils.ToastUtils;
import com.ptithcm.apt.viewmodel.adminserviceconfig.AdminServiceConfigViewModel;
import com.ptithcm.apt.viewmodel.adminserviceconfig.AdminServiceConfigViewModelFactory;

import java.math.BigDecimal;

public class AdminServiceConfigFragment extends Fragment {

    private AdminServiceConfigViewModel viewModel;
    private AdminServiceConfigAdapter adapter;

    public AdminServiceConfigFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, new AdminServiceConfigViewModelFactory())
                .get(AdminServiceConfigViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_serviceconfig, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup RecyclerView
        RecyclerView rvServiceConfigs = view.findViewById(R.id.rvServiceConfigs);
        adapter = new AdminServiceConfigAdapter(requireContext());
        adapter.setOnEditClickListener(this::showEditDialog);
        rvServiceConfigs.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvServiceConfigs.setAdapter(adapter);

        TextView tvActiveServicesCount = view.findViewById(R.id.tvActiveServicesCount);
        TextView tvPendingUpdatesCount = view.findViewById(R.id.tvPendingUpdatesCount);

        viewModel.serviceConfigsData.observe(getViewLifecycleOwner(), configs -> {
            if (configs != null) {
                // Cập nhật danh sách
                adapter.submitList(configs);

                int activeCount = 0;
                int pendingCount = 0;
                for (AdminServiceConfigResponse config : configs) {
                    if (config.getServiceCode() == null) continue;
                    activeCount++;
                    if (config.getUpcomingPrice() != null) pendingCount++;
                }

                if (tvActiveServicesCount != null)
                    tvActiveServicesCount.setText(String.valueOf(activeCount));
                if (tvPendingUpdatesCount != null)
                    tvPendingUpdatesCount.setText(String.valueOf(pendingCount));
            }
        });

        viewModel.error.observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                ToastUtils.showErrorToast(requireContext(), errorMsg);
            }
        });

        viewModel.updateSuccess.observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                ToastUtils.showSuccessToast(requireContext(), "Lên lịch cập nhật giá thành công!");
                viewModel.fetchServiceConfigs();
                viewModel.resetUpdateStatus();
            }
        });

        viewModel.cancelSuccess.observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                ToastUtils.showSuccessToast(requireContext(), "Hủy lịch cập nhật thành công!");
                viewModel.fetchServiceConfigs();
                viewModel.resetCancelStatus();
            }
        });

        viewModel.fetchServiceConfigs();
    }

    private android.app.Dialog currentDialog;

    private void showEditDialog(AdminServiceConfigResponse config) {
        currentDialog = new android.app.Dialog(requireContext());
        currentDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        currentDialog.setContentView(R.layout.dialog_edit_serviceconfig);

        if (currentDialog.getWindow() != null) {
            currentDialog.getWindow().setBackgroundDrawable(
                    new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
            currentDialog.getWindow().setLayout(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        TextView tvServiceType = currentDialog.findViewById(R.id.tv_service_type);
        if (tvServiceType != null) {
            tvServiceType.setText((config.getServiceName() != null ? config.getServiceName().toUpperCase() : ""));
        }

        android.widget.EditText etPrice = currentDialog.findViewById(R.id.et_new_unit_price);
        if (etPrice != null) {
            BigDecimal upcomingPrice = config.getUpcomingPrice();
            if (upcomingPrice != null) {
                // Hiển thị số nguyên
                etPrice.setText(upcomingPrice.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString());
            } else {
                // Nếu chưa có giá sắp tới, lấy giá hiện tại làm gợi ý
                BigDecimal currentPrice = config.getCurrentPrice();
                if (currentPrice != null) {
                    etPrice.setText(currentPrice.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString());
                } else {
                    etPrice.setText("");
                }
            }
        }

        TextView tvCurrency = currentDialog.findViewById(R.id.tv_currency);
        if (tvCurrency != null && config.getUnit() != null) {
            tvCurrency.setText(config.getUnit());
        }

        android.widget.NumberPicker npMonth = currentDialog.findViewById(R.id.np_month);
        android.widget.NumberPicker npYear = currentDialog.findViewById(R.id.np_year);

        if (npMonth != null && npYear != null) {
            npMonth.setMinValue(0);
            npMonth.setMaxValue(11);
            String[] months = new String[12];
            for (int i = 0; i < 12; i++) {
                months[i] = "Tháng " + (i + 1);
            }
            npMonth.setDisplayedValues(months);

            java.util.Calendar cal = java.util.Calendar.getInstance();
            int currentYear = cal.get(java.util.Calendar.YEAR);
            int currentMonth = cal.get(java.util.Calendar.MONTH);

            // Parser ngày sắp tới
            String upcomingDateStr = config.getUpcomingEffectiveFrom();
            if (upcomingDateStr != null && !upcomingDateStr.isEmpty()) {
                String[] parts = upcomingDateStr.split("-");
                if (parts.length >= 2) {
                    try {
                        currentYear = Integer.parseInt(parts[0]);
                        currentMonth = Integer.parseInt(parts[1]) - 1;
                    } catch (NumberFormatException ignored) {
                    }
                }
            } else {
                // Nếu chưa có giá sắp tới, mặc định là tháng sau
                cal.add(java.util.Calendar.MONTH, 1);
                currentYear = cal.get(java.util.Calendar.YEAR);
                currentMonth = cal.get(java.util.Calendar.MONTH);
            }

            npYear.setMinValue(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
            npYear.setMaxValue(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) + 10);

            npMonth.setValue(currentMonth);
            npYear.setValue(currentYear);
        }

        View btnClose = currentDialog.findViewById(R.id.iv_close_icon);
        View tvCancel = currentDialog.findViewById(R.id.tv_cancel);
        View btnSave = currentDialog.findViewById(R.id.btn_schedule_change);
        View tvCancelSchedule = currentDialog.findViewById(R.id.tv_cancel_schedule);

        // Hiển thị nút "Hủy đặt lịch" nếu đang có lịch cập nhật sắp tới
        if (tvCancelSchedule != null) {
            if (config.getUpcomingPrice() != null) {
                tvCancelSchedule.setVisibility(View.VISIBLE);
                tvCancelSchedule.setOnClickListener(v -> {
                    viewModel.cancelUpcomingUpdate(config.getServiceCode());
                    currentDialog.dismiss();
                });
            } else {
                tvCancelSchedule.setVisibility(View.GONE);
            }
        }

        if (btnClose != null)
            btnClose.setOnClickListener(v -> currentDialog.dismiss());
        if (tvCancel != null)
            tvCancel.setOnClickListener(v -> currentDialog.dismiss());
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                int selectedMonth = npMonth != null ? npMonth.getValue() + 1 : 1;
                int selectedYear = npYear != null ? npYear.getValue() : 2024;
                String priceStr = etPrice != null ? etPrice.getText().toString() : "";

                if (priceStr.isEmpty()) {
                    ToastUtils.showErrorToast(requireContext(), "Vui lòng nhập đơn giá mới");
                    return;
                }

                try {
                    BigDecimal newPrice = new BigDecimal(priceStr);
                    // Format date as yyyy-MM-01
                    String effectiveDate = String.format("%04d-%02d-01", selectedYear, selectedMonth);

                    com.ptithcm.apt.models.adminserviceconfig.ServicePriceUpdateRequest request = new com.ptithcm.apt.models.adminserviceconfig.ServicePriceUpdateRequest(
                            config.getServiceCode(),
                            newPrice,
                            effectiveDate);

                    viewModel.validateAndUpdatePrice(request, config.getCurrentPrice());
                    currentDialog.dismiss();
                } catch (NumberFormatException e) {
                    ToastUtils.showErrorToast(requireContext(), "Giá không hợp lệ");
                }
            });
        }

        currentDialog.show();
    }
}
