package com.ptithcm.apt.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.graphics.drawable.GradientDrawable;

import com.ptithcm.apt.R;
import com.ptithcm.apt.utils.FormatUtils;
import com.ptithcm.apt.utils.ToastUtils;
import com.ptithcm.apt.models.adminserviceconfig.AdminServiceConfigResponse;
import com.ptithcm.apt.viewmodel.adminserviceconfig.AdminServiceConfigViewModel;
import com.ptithcm.apt.viewmodel.adminserviceconfig.AdminServiceConfigViewModelFactory;

import java.math.BigDecimal;

public class AdminServiceConfigFragment extends Fragment {

    private AdminServiceConfigViewModel viewModel;

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

        // Các view dịch vụ
        View electricity = view.findViewById(R.id.include_electricity);
        View water = view.findViewById(R.id.include_water);
        View management = view.findViewById(R.id.include_management);
        View sanitation = view.findViewById(R.id.include_sanitation);

        TextView tvActiveServicesCount = view.findViewById(R.id.tvActiveServicesCount);
        TextView tvPendingUpdatesCount = view.findViewById(R.id.tvPendingUpdatesCount);

        viewModel.serviceConfigsData.observe(getViewLifecycleOwner(), configs -> {
            if (configs != null) {
                int activeServicesCount = 0;
                int pendingUpdatesCount = 0;
                for (AdminServiceConfigResponse config : configs) {
                    View serviceView = null;
                    int iconRes = 0;
                    if (config.getServiceCode() == null)
                        continue;

                    activeServicesCount++;
                    if (config.getUpcomingPrice() != null) {
                        pendingUpdatesCount++;
                    }

                    switch (config.getServiceCode()) {
                        case "MANAGEMENT":
                            serviceView = management;
                            iconRes = R.drawable.ic_management;
                            break;
                        case "WATER":
                            serviceView = water;
                            iconRes = R.drawable.ic_water;
                            break;
                        case "ELECTRICITY":
                            serviceView = electricity;
                            iconRes = R.drawable.ic_electric;
                            break;
                        case "SANITATION":
                            serviceView = sanitation;
                            iconRes = R.drawable.ic_sanitation;
                            break;
                    }
                    if (serviceView != null) {
                        setupServiceItem(serviceView, config.getServiceName(), config.getUnit(), iconRes);
                        updatePriceData(serviceView, config);
                        // Bắt sự kiện click vào nút Đặt lịch thay đổi
                        View btnEdit = serviceView.findViewById(R.id.button_edit);
                        if (btnEdit != null) {
                            btnEdit.setOnClickListener(v -> showEditDialog(config));
                        }
                    }
                }

                if (tvActiveServicesCount != null) {
                    tvActiveServicesCount.setText(String.valueOf(activeServicesCount));
                }
                if (tvPendingUpdatesCount != null) {
                    tvPendingUpdatesCount.setText(String.valueOf(pendingUpdatesCount));
                }
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
                // Hiển thị số nguyên cho ô nhập liệu
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

            // Parser ngày sắp tới (nếu có)
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

    private void updatePriceData(View view, AdminServiceConfigResponse config) {
        View currentPriceLayout = view.findViewById(R.id.layout_current_price);
        View upcomingPriceLayout = view.findViewById(R.id.layout_upcoming_price);

        if (currentPriceLayout != null) {
            TextView textPrice = currentPriceLayout.findViewById(R.id.text_price);
            TextView textDate = currentPriceLayout.findViewById(R.id.text_date);

            BigDecimal price = config.getCurrentPrice();
            if (price != null) {
                textPrice.setText(FormatUtils.formatCurrency(price));
            } else {
                textPrice.setText("--");
            }

            String date = config.getCurrentEffectiveFrom();
            if (date != null && !date.isEmpty()) {
                textDate.setText("Từ ngày " + FormatUtils.formatDate(date));
            } else {
                textDate.setText("Chưa áp dụng");
            }
        }

        if (upcomingPriceLayout != null) {
            TextView textPrice = upcomingPriceLayout.findViewById(R.id.text_price);
            TextView textDate = upcomingPriceLayout.findViewById(R.id.text_date);
            View layoutDate = upcomingPriceLayout.findViewById(R.id.layout_date);
            TextView textNoRevision = upcomingPriceLayout.findViewById(R.id.text_no_revision);

            BigDecimal price = config.getUpcomingPrice();
            if (price != null) {
                textPrice.setVisibility(View.VISIBLE);
                textPrice.setText(FormatUtils.formatCurrency(price));

                if (layoutDate != null)
                    layoutDate.setVisibility(View.VISIBLE);

                String date = config.getUpcomingEffectiveFrom();
                if (date != null && !date.isEmpty()) {
                    textDate.setText("Từ ngày " + FormatUtils.formatDate(date));
                } else {
                    textDate.setText("Chưa xác định");
                }

                if (textNoRevision != null)
                    textNoRevision.setVisibility(View.GONE);
            } else {
                textPrice.setVisibility(View.GONE);
                if (layoutDate != null)
                    layoutDate.setVisibility(View.GONE);
                if (textNoRevision != null)
                    textNoRevision.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Thiết lập dữ liệu và giao diện cho từng mục dịch vụ.
     */
    private void setupServiceItem(View view, String title, String unit, int iconRes) {
        if (view == null)
            return;

        ((TextView) view.findViewById(R.id.text_service_title)).setText(title);
        ((TextView) view.findViewById(R.id.text_service_unit)).setText(unit);
        ((ImageView) view.findViewById(R.id.image_service_icon)).setImageResource(iconRes);

        // --- giá Hiện tại ---
        View currentPriceLayout = view.findViewById(R.id.layout_current_price);
        if (currentPriceLayout != null) {
            // Hiệu ứng nổi bật (elevation)
            currentPriceLayout.setElevation(4 * getResources().getDisplayMetrics().density);

            // Viền xanh lá liền mạch (Solid Green)
            GradientDrawable currentBg = (GradientDrawable) ContextCompat
                    .getDrawable(requireContext(), R.drawable.bg_card_white).mutate();
            int strokeWidth = (int) (2 * getResources().getDisplayMetrics().density);
            currentBg.setStroke(strokeWidth, ContextCompat.getColor(requireContext(), R.color.indicator_current));
            currentPriceLayout.setBackground(currentBg);

            // Tùy chỉnh Nhãn (Label)
            TextView label = currentPriceLayout.findViewById(R.id.text_label);
            label.setText("GIÁ HIỆN TẠI");
            label.setTextColor(ContextCompat.getColor(requireContext(), R.color.indicator_current));
            label.setAlpha(1.0f);
        }

        // --- giá Sắp tới ---
        View upcomingPriceLayout = view.findViewById(R.id.layout_upcoming_price);
        if (upcomingPriceLayout != null) {
            // Không đổ bóng và làm mờ nhẹ
            upcomingPriceLayout.setElevation(0);
            upcomingPriceLayout.setAlpha(0.8f);

            // Viền xám đứt đoạn (Dashed Gray)
            upcomingPriceLayout.setBackgroundResource(R.drawable.bg_card_dashed);

            // Tùy chỉnh Nhãn (Label)
            TextView label = upcomingPriceLayout.findViewById(R.id.text_label);
            label.setText("GIÁ SẮP TỚI");
            label.setTextColor(ContextCompat.getColor(requireContext(), R.color.indicator_upcoming));
        }
    }
}
