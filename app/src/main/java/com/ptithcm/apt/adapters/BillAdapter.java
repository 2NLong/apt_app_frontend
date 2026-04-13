package com.ptithcm.apt.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.models.bill.BillList;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    private List<BillList> list;

    public BillAdapter(List<BillList> list) {
        this.list = list;
    }

    public void updateList(List<BillList> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvApartment, tvDate, tvStatus;
        TextView tvElectric, tvWater, tvManagement, tvService;
        TextView tvTotal;
        Button btnConfirm;

        public ViewHolder(View v) {
            super(v);
            tvApartment = v.findViewById(R.id.tvApartment);
            tvDate = v.findViewById(R.id.tvDate);
            tvStatus = v.findViewById(R.id.tvStatus);

            tvElectric = v.findViewById(R.id.tvElectric);
            tvWater = v.findViewById(R.id.tvWater);
            tvManagement = v.findViewById(R.id.tvManagement);
            tvService = v.findViewById(R.id.tvSanitation);

            tvTotal = v.findViewById(R.id.tvTotal);
            btnConfirm = v.findViewById(R.id.btnConfirm);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Chuyển đổi BigDecimal sang định dạng tiền tệ (VD: 1.000.000đ)
     */
    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "0đ";
        DecimalFormat formatter = new DecimalFormat("#,###đ");
        return formatter.format(amount);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int i) {
        BillList b = list.get(i);

        // HEADER
        h.tvApartment.setText("Căn hộ " + b.getApartmentName());
        h.tvDate.setText("Tháng " + b.getBillingMonth() + "/" + b.getBillingYear());

        // FEES - Sử dụng hàm formatMoney cho BigDecimal
        h.tvElectric.setText(formatMoney(b.getElectricityFee()));
        h.tvWater.setText(formatMoney(b.getWaterFee()));
        h.tvManagement.setText(formatMoney(b.getManagementFee()));
        h.tvService.setText(formatMoney(b.getSanitationFee()));

        // TOTAL
        h.tvTotal.setText(formatMoney(b.getTotalAmount()));

        // STATUS
        if (b.getStatus() == BillStatus.PAID) {
            h.tvStatus.setText("Đã thanh toán");
            h.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            h.btnConfirm.setVisibility(View.GONE);
        } else {
            h.tvStatus.setText("Chưa thanh toán");
            h.tvStatus.setTextColor(Color.parseColor("#FF9800"));
            h.btnConfirm.setVisibility(View.VISIBLE);
        }

        // CLICK CONFIRM
        h.btnConfirm.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Xác nhận bill ID: " + b.getId(), Toast.LENGTH_SHORT).show();
        });

        // CLICK ITEM
        h.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Xem chi tiết bill " + b.getId(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
