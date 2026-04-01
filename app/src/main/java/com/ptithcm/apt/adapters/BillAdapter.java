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
import com.ptithcm.apt.models.Bill;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    private List<Bill> list;

    public BillAdapter(List<Bill> list) {
        this.list = list;
    }

    public void updateList(List<Bill> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvApartment, tvDate, tvStatus;
        TextView tvElectric, tvWater, tvManagement, tvService;
        TextView tvTotal, tvDue;
        Button btnConfirm;
        ImageView imgIcon;

        public ViewHolder(View v) {
            super(v);

//            imgIcon = v.findViewById(R.id.imgIcon);

            tvApartment = v.findViewById(R.id.tvApartment);
            tvDate = v.findViewById(R.id.tvDate);
            tvStatus = v.findViewById(R.id.tvStatus);

            tvElectric = v.findViewById(R.id.tvElectric);
            tvWater = v.findViewById(R.id.tvWater);
            tvManagement = v.findViewById(R.id.tvManagement);
            tvService = v.findViewById(R.id.tvSanitation);

            tvTotal = v.findViewById(R.id.tvTotal);
//            tvDue = v.findViewById(R.id.tvDue);

            btnConfirm = v.findViewById(R.id.btnConfirm);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill, parent, false);
        return new ViewHolder(view);
    }

    private String format(double money) {
        return String.format("%,.0fđ", money);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int i) {
        Bill b = list.get(i);

        // HEADER
        h.tvApartment.setText("Căn hộ " + b.getApartmentId()); // hoặc A101
        h.tvDate.setText("Tháng " + b.getMonth() + "/" + b.getYear());

        // FEES
        h.tvElectric.setText(format(b.getElectricityFee()));
        h.tvWater.setText(format(b.getWaterFee()));
        h.tvManagement.setText(format(b.getManagementFee()));
        h.tvService.setText(format(b.getSanitationFee())); // dùng làm phí dịch vụ

        // TOTAL
        h.tvTotal.setText(format(b.getTotalAmount()));

        // DATE
//        h.tvDue.setText("Hạn: " + b.getDueDate());

        // STATUS
        if ("PAID".equals(b.getStatus())) {
            h.tvStatus.setText("Đã thanh toán");
            h.tvStatus.setTextColor(Color.parseColor("#4CAF50"));

            h.btnConfirm.setVisibility(View.GONE);

            if (b.getPaidAt() != null) {
                h.tvDue.setText("Đã trả: " + b.getPaidAt());
            }

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
        return list.size();
    }

    private String formatMoney(double amount) {
        return String.format("%,.0fđ", amount);
    }
}
