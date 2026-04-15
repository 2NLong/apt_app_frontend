package com.ptithcm.apt.adapters.serviceconfig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;

import com.ptithcm.apt.R;
import com.ptithcm.apt.models.adminserviceconfig.AdminServiceConfigResponse;
import com.ptithcm.apt.utils.FormatUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdminServiceConfigAdapter extends RecyclerView.Adapter<AdminServiceConfigAdapter.ServiceConfigViewHolder> {

    public interface OnEditClickListener {
        void onEditClick(AdminServiceConfigResponse config);
    }

    private final Context context;
    private final List<AdminServiceConfigResponse> items = new ArrayList<>();
    private OnEditClickListener editClickListener;

    public AdminServiceConfigAdapter(Context context) {
        this.context = context;
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }

    public void submitList(List<AdminServiceConfigResponse> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceConfigViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_serviceconfig, parent, false);
        return new ServiceConfigViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceConfigViewHolder holder, int position) {
        AdminServiceConfigResponse config = items.get(position);
        holder.bind(config);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ServiceConfigViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageServiceIcon;
        private final TextView textServiceTitle;
        private final TextView textServiceUnit;
        private final View layoutCurrentPrice;
        private final View layoutUpcomingPrice;
        private final View buttonEdit;

        ServiceConfigViewHolder(@NonNull View itemView) {
            super(itemView);
            imageServiceIcon = itemView.findViewById(R.id.image_service_icon);
            textServiceTitle = itemView.findViewById(R.id.text_service_title);
            textServiceUnit = itemView.findViewById(R.id.text_service_unit);
            layoutCurrentPrice = itemView.findViewById(R.id.layout_current_price);
            layoutUpcomingPrice = itemView.findViewById(R.id.layout_upcoming_price);
            buttonEdit = itemView.findViewById(R.id.button_edit);
        }

        void bind(AdminServiceConfigResponse config) {
            // Tên và đơn vị
            textServiceTitle.setText(config.getServiceName() != null ? config.getServiceName() : "");
            textServiceUnit.setText(config.getUnit() != null ? config.getUnit() : "");

            // Icon
            imageServiceIcon.setImageResource(resolveIcon(config.getServiceCode()));

            // Style thẻ giá hiện tại
            if (layoutCurrentPrice != null) {
                layoutCurrentPrice.setElevation(4 * context.getResources().getDisplayMetrics().density);
                GradientDrawable currentBg = (GradientDrawable) ContextCompat
                        .getDrawable(context, R.drawable.bg_card_white).mutate();
                int strokeWidth = (int) (2 * context.getResources().getDisplayMetrics().density);
                currentBg.setStroke(strokeWidth, ContextCompat.getColor(context, R.color.indicator_current));
                layoutCurrentPrice.setBackground(currentBg);

                TextView label = layoutCurrentPrice.findViewById(R.id.text_label);
                if (label != null) {
                    label.setText("GIÁ HIỆN TẠI");
                    label.setTextColor(ContextCompat.getColor(context, R.color.indicator_current));
                    label.setAlpha(1.0f);
                }
            }

            // Style thẻ giá sắp tới
            if (layoutUpcomingPrice != null) {
                layoutUpcomingPrice.setElevation(0);
                layoutUpcomingPrice.setAlpha(0.8f);
                layoutUpcomingPrice.setBackgroundResource(R.drawable.bg_card_dashed);

                TextView label = layoutUpcomingPrice.findViewById(R.id.text_label);
                if (label != null) {
                    label.setText("GIÁ SẮP TỚI");
                    label.setTextColor(ContextCompat.getColor(context, R.color.indicator_upcoming));
                }
            }

            // giá hiện tại
            updatePriceCard(layoutCurrentPrice, config.getCurrentPrice(), config.getCurrentEffectiveFrom(), true);

            // giá sắp tới
            updatePriceCard(layoutUpcomingPrice, config.getUpcomingPrice(), config.getUpcomingEffectiveFrom(), false);

            // Nút chỉnh sửa
            if (buttonEdit != null && editClickListener != null) {
                buttonEdit.setOnClickListener(v -> editClickListener.onEditClick(config));
            }
        }

        private void updatePriceCard(View priceLayout, BigDecimal price, String dateStr, boolean isCurrent) {
            if (priceLayout == null) return;

            TextView textPrice = priceLayout.findViewById(R.id.text_price);
            TextView textDate = priceLayout.findViewById(R.id.text_date);
            View layoutDate = priceLayout.findViewById(R.id.layout_date);
            TextView textNoRevision = priceLayout.findViewById(R.id.text_no_revision);

            if (isCurrent) {
                // Thẻ giá hiện tại
                if (textPrice != null) {
                    textPrice.setVisibility(View.VISIBLE);
                    textPrice.setText(price != null ? FormatUtils.formatCurrency(price) : "--");
                }
                if (textDate != null) {
                    textDate.setText((dateStr != null && !dateStr.isEmpty())
                            ? "Từ ngày " + FormatUtils.formatDate(dateStr)
                            : "Chưa áp dụng");
                }
                if (layoutDate != null) layoutDate.setVisibility(View.VISIBLE);
                if (textNoRevision != null) textNoRevision.setVisibility(View.GONE);
            } else {
                // Thẻ giá sắp tới
                if (price != null) {
                    if (textPrice != null) {
                        textPrice.setVisibility(View.VISIBLE);
                        textPrice.setText(FormatUtils.formatCurrency(price));
                    }
                    if (layoutDate != null) layoutDate.setVisibility(View.VISIBLE);
                    if (textDate != null) {
                        textDate.setText((dateStr != null && !dateStr.isEmpty())
                                ? "Từ ngày " + FormatUtils.formatDate(dateStr)
                                : "Chưa xác định");
                    }
                    if (textNoRevision != null) textNoRevision.setVisibility(View.GONE);
                } else {
                    if (textPrice != null) textPrice.setVisibility(View.GONE);
                    if (layoutDate != null) layoutDate.setVisibility(View.GONE);
                    if (textNoRevision != null) textNoRevision.setVisibility(View.VISIBLE);
                }
            }
        }

        private int resolveIcon(String serviceCode) {
            if (serviceCode == null) return R.drawable.ic_metric;
            switch (serviceCode) {
                case "ELECTRICITY": return R.drawable.ic_electric;
                case "WATER":       return R.drawable.ic_water;
                case "MANAGEMENT":  return R.drawable.ic_management;
                case "SANITATION":  return R.drawable.ic_sanitation;
                default:            return R.drawable.ic_metric;
            }
        }
    }
}
