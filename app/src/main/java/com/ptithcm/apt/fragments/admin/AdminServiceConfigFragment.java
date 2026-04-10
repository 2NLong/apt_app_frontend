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
import android.graphics.drawable.GradientDrawable;

import com.ptithcm.apt.R;

public class AdminServiceConfigFragment extends Fragment {

    public AdminServiceConfigFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_serviceconfig, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Điện
        View electricity = view.findViewById(R.id.include_electricity);
        setupServiceItem(electricity, "Điện", "VND/kWh", R.drawable.ic_electric);

        // Nước
        View water = view.findViewById(R.id.include_water);
        setupServiceItem(water, "Nước", "VND/m³", R.drawable.ic_water);

        // Phí quản lý
        View management = view.findViewById(R.id.include_management);
        setupServiceItem(management, "Phí quản lý", "VND/Tháng", R.drawable.ic_management);

        // Vệ sinh
        View sanitation = view.findViewById(R.id.include_sanitation);
        setupServiceItem(sanitation, "Vệ sinh", "VND/Tháng", R.drawable.ic_sanitation);
    }

    /**
     * Thiết lập dữ liệu và giao diện cho từng mục dịch vụ.
     */
    private void setupServiceItem(View view, String title, String unit, int iconRes) {
        if (view == null) return;

        ((TextView) view.findViewById(R.id.text_service_title)).setText(title);
        ((TextView) view.findViewById(R.id.text_service_unit)).setText(unit);
        ((ImageView) view.findViewById(R.id.image_service_icon)).setImageResource(iconRes);

        // --- Thiết lập Thẻ giá Hiện tại ---
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

        // --- Thiết lập Thẻ giá Sắp tới ---
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
