package com.ptithcm.apt.adapters.serviceconfig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.models.adminserviceconfig.ServiceConfigResponse;
import com.ptithcm.apt.utils.FormatUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HomeServiceConfigAdapter extends RecyclerView.Adapter<HomeServiceConfigAdapter.HomeServiceConfigViewHolder> {

    private final Context context;
    private final List<ServiceConfigResponse> items = new ArrayList<>();

    public HomeServiceConfigAdapter(Context context) {
        this.context = context;
    }

    public void submitList(List<ServiceConfigResponse> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeServiceConfigViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_service_config, parent, false);
        return new HomeServiceConfigViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeServiceConfigViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HomeServiceConfigViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageIcon;
        private final TextView textName;
        private final TextView textCategory;
        private final TextView textTagUpcoming;
        private final TextView textPrice;
        private final TextView textUnit;

        public HomeServiceConfigViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.image_service_icon);
            textName = itemView.findViewById(R.id.text_service_name);
            textCategory = itemView.findViewById(R.id.text_service_code);
            textTagUpcoming = itemView.findViewById(R.id.text_tag_upcoming);
            textPrice = itemView.findViewById(R.id.text_service_price);
            textUnit = itemView.findViewById(R.id.text_service_unit);
        }

        public void bind(ServiceConfigResponse config) {
            textName.setText(config.getServiceName());
            textUnit.setText(config.getUnit());
            textPrice.setText(FormatUtils.formatCurrency(config.getUnitPrice()).replace(" ₫", "").trim());

            // Map code and icons
            imageIcon.setImageResource(resolveIcon(config.getServiceCode()));
            
            String baseCode = config.getServiceCode() != null ? config.getServiceCode() : "";
            String dateStr = config.getEffectiveFrom();
            boolean isUpcoming = false;
            
            if (dateStr != null && !dateStr.isEmpty()) {
                try {
                    LocalDate effectiveDate = LocalDate.parse(dateStr);
                    isUpcoming = effectiveDate.isAfter(LocalDate.now());
                } catch (Exception ignored) {
                }
            }
            
            textTagUpcoming.setVisibility(isUpcoming ? View.VISIBLE : View.GONE);
            textCategory.setText(baseCode);
        }

        private int resolveIcon(String serviceCode) {
            if (serviceCode == null)
                return R.drawable.ic_metric;
            switch (serviceCode) {
                case "ELECTRICITY":
                    return R.drawable.ic_electric;
                case "WATER":
                    return R.drawable.ic_water;
                case "MANAGEMENT":
                    return R.drawable.ic_management;
                case "SANITATION":
                    return R.drawable.ic_sanitation;
                default:
                    return R.drawable.ic_metric;
            }
        }
    }
}
