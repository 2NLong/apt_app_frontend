package com.ptithcm.apt.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.graphics.drawable.GradientDrawable;

import com.ptithcm.apt.R;
import com.ptithcm.apt.utils.ToastUtils;
import com.ptithcm.apt.models.adminserviceconfig.AdminServiceConfigResponse;
import com.ptithcm.apt.viewmodel.adminserviceconfig.AdminServiceConfigViewModel;
import com.ptithcm.apt.viewmodel.adminserviceconfig.AdminServiceConfigViewModelFactory;

import java.text.NumberFormat;
import java.util.Locale;

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
                     if (config.getServiceCode() == null) continue;
                     
                     activeServicesCount++;
                     if (config.getUpcomingPrice() != null) {
                         pendingUpdatesCount++;
                     }
                     
                     switch(config.getServiceCode()) {
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

        viewModel.fetchServiceConfigs();
    }

    private void updatePriceData(View view, AdminServiceConfigResponse config) {
        View currentPriceLayout = view.findViewById(R.id.layout_current_price);
        View upcomingPriceLayout = view.findViewById(R.id.layout_upcoming_price);

        if (currentPriceLayout != null) {
            TextView textPrice = currentPriceLayout.findViewById(R.id.text_price);
            TextView textDate = currentPriceLayout.findViewById(R.id.text_date);
            
            Double price = config.getCurrentPrice();
            if (price != null) {
                textPrice.setText(formatCurrency(price));
            } else {
                textPrice.setText("--");
            }
            
            String date = config.getCurrentEffectiveFrom();
            if (date != null && !date.isEmpty()) {
                textDate.setText("Từ ngày " + formatDate(date));
            } else {
                textDate.setText("Chưa áp dụng");
            }
        }

        if (upcomingPriceLayout != null) {
            TextView textPrice = upcomingPriceLayout.findViewById(R.id.text_price);
            TextView textDate = upcomingPriceLayout.findViewById(R.id.text_date);
            View layoutDate = upcomingPriceLayout.findViewById(R.id.layout_date);
            TextView textNoRevision = upcomingPriceLayout.findViewById(R.id.text_no_revision);

            Double price = config.getUpcomingPrice();
            if (price != null) {
                textPrice.setVisibility(View.VISIBLE);
                textPrice.setText(formatCurrency(price));
                
                if (layoutDate != null) layoutDate.setVisibility(View.VISIBLE);
                
                String date = config.getUpcomingEffectiveFrom();
                if (date != null && !date.isEmpty()) {
                    textDate.setText("Từ ngày " + formatDate(date));
                } else {
                    textDate.setText("Chưa xác định");
                }
                
                if (textNoRevision != null) textNoRevision.setVisibility(View.GONE);
            } else {
                textPrice.setVisibility(View.GONE);
                if (layoutDate != null) layoutDate.setVisibility(View.GONE);
                if (textNoRevision != null) textNoRevision.setVisibility(View.VISIBLE);
            }
        }
    }

    private String formatCurrency(Double amount) {
        if (amount == null) return "0";
        return NumberFormat.getNumberInstance(Locale.US).format(Math.round(amount));
    }

    private String formatDate(String input) {
        if (input == null || input.isEmpty()) return "";
        String[] parts = input.split("-");
        if (parts.length == 3) {
            return parts[2] + "/" + parts[1] + "/" + parts[0];
        }
        return input;
    }

    /**
     * Thiết lập dữ liệu và giao diện cho từng mục dịch vụ.
     */
    private void setupServiceItem(View view, String title, String unit, int iconRes) {
        if (view == null) return;

        ((TextView) view.findViewById(R.id.text_service_title)).setText(title);
        ((TextView) view.findViewById(R.id.text_service_unit)).setText(unit);
        ((ImageView) view.findViewById(R.id.image_service_icon)).setImageResource(iconRes);

        // --- giá Hiện tại ---
        View currentPriceLayout = view.findViewById(R.id.layout_current_price);
        if (currentPriceLayout != null) {
            // Hiệu ứng nổi bật (elevation)
            currentPriceLayout.setElevation(4 * getResources().getDisplayMetrics().density);
            
            // Viền xanh lá liền mạch (Solid Green)
            GradientDrawable currentBg = (GradientDrawable) ContextCompat.getDrawable(requireContext(), R.drawable.bg_card_white).mutate();
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
