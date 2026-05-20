package com.ptithcm.apt.adapters.rentinvoice;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.models.rentinvoice.response.RentInvoiceListResponse;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdminRentAdapter extends RecyclerView.Adapter<AdminRentAdapter.ViewHolder> {
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy",
            Locale.getDefault());
    private final SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd",
            Locale.getDefault());
    private List<RentInvoiceListResponse> list;
    private OnRentActionListener listener;

    public AdminRentAdapter(List<RentInvoiceListResponse> list, OnRentActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void updateList(List<RentInvoiceListResponse> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_rent_invoice,
                parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RentInvoiceListResponse bill = list.get(position);
        holder.tvApartment.setText("Căn hộ " + bill.getApartmentName());
        holder.tvDate.setText("Kỳ thuê: Tháng " + bill.getBillingMonth() + "/" + bill.getBillingYear());

        DecimalFormat formatter = new DecimalFormat("#,###đ");
        holder.tvRentAmount.setText(formatter.format(bill.getRentAmount()));
        holder.tvTotal.setText(formatter.format(bill.getRentAmount()));


        if (bill.getStatus().name().equals("PAID")) {
            holder.tvStatus.setText("ĐÃ THANH TOÁN");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            holder.btnConfirm.setVisibility(View.GONE);
        } else if (bill.getStatus().name().equals("LATE")) {
            holder.tvStatus.setText("QUÁ HẠN");
            holder.tvStatus.setTextColor(Color.parseColor("#F44336"));
            holder.btnConfirm.setVisibility(View.VISIBLE);
            holder.btnConfirm.setText("Duyệt");
        } else {
            holder.tvStatus.setText("CHƯA THANH TOÁN");
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800"));
            holder.btnConfirm.setVisibility(View.VISIBLE);
            holder.btnConfirm.setText("Duyệt");
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(bill));
        holder.btnConfirm.setOnClickListener(v -> {
            if (listener != null) listener.onApprove(bill);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface OnRentActionListener {
        void onItemClick(RentInvoiceListResponse bill);

        void onApprove(RentInvoiceListResponse bill);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvApartment, tvDate, tvStatus, tvRentAmount, tvTotal, tvDue;
        Button btnConfirm;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvApartment = v.findViewById(R.id.tvApartment);
            tvDate = v.findViewById(R.id.tvDate);
            tvStatus = v.findViewById(R.id.tvStatus);
            tvRentAmount = v.findViewById(R.id.tvRentAmount);
            tvTotal = v.findViewById(R.id.tvTotal);
            tvDue = v.findViewById(R.id.tvDue);
            btnConfirm = v.findViewById(R.id.btnConfirm);
        }
    }
}
