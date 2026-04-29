package com.ptithcm.apt.adapters.bill;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.models.bill.response.BillListResponse;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminBillAdapter extends RecyclerView.Adapter<AdminBillAdapter.ViewHolder> {

    private List<BillListResponse> list;
    private OnBillActionListener listener;
    
    // Định dạng mong muốn hiển thị
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    // Định dạng để parse chuỗi từ API (chỉ lấy phần ngày yyyy-MM-dd)
    private final SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public interface OnBillActionListener {
        void onApprove(BillListResponse bill);
        void onItemClick(BillListResponse bill);
    }

    public AdminBillAdapter(List<BillListResponse> list, OnBillActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void updateList(List<BillListResponse> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill, parent, false);
        return new ViewHolder(view);
    }

    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "0đ";
        DecimalFormat formatter = new DecimalFormat("#,###đ");
        return formatter.format(amount);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BillListResponse bill = list.get(position);

        holder.tvApartment.setText("Căn hộ " + bill.getApartmentName());
        holder.tvDate.setText("Tháng " + bill.getBillingMonth() + "/" + bill.getBillingYear());

        holder.tvElectric.setText(formatMoney(bill.getElectricityFee()));
        holder.tvWater.setText(formatMoney(bill.getWaterFee()));
        holder.tvManagement.setText(formatMoney(bill.getManagementFee()));
        holder.tvSanitation.setText(formatMoney(bill.getSanitationFee()));
        holder.tvTotal.setText(formatMoney(bill.getTotalAmount()));

        String rawDate = bill.getDueDate();
        if (rawDate != null && rawDate.length() >= 10) {
            try {
                // Cắt lấy 10 ký tự đầu (yyyy-MM-dd)
                String datePart = rawDate.substring(0, 10);
                Date date = apiFormat.parse(datePart);
                if (date != null) {
                    holder.tvDue.setText("Hạn: " + displayFormat.format(date));
                }
            } catch (Exception e) {
                holder.tvDue.setText("Hạn: " + rawDate);
            }
        } else {
            holder.tvDue.setText("Hạn: --/--/----");
        }

        if (bill.getStatus() == BillStatus.PAID) {
            holder.tvStatus.setText("ĐÃ THANH TOÁN");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            holder.btnConfirm.setVisibility(View.GONE);
        }
        else if (bill.getStatus() == BillStatus.LATE) {
            holder.tvStatus.setText("QUÁ HẠN");
            holder.tvStatus.setTextColor(Color.parseColor("#F44336"));
            holder.btnConfirm.setVisibility(View.VISIBLE);
            holder.btnConfirm.setText("Duyệt");
        }
        else { // Mặc định là UNPAID (Chưa thanh toán)
            holder.tvStatus.setText("CHƯA THANH TOÁN");
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800"));
            holder.btnConfirm.setVisibility(View.VISIBLE);
            holder.btnConfirm.setText("Duyệt");
        }

        holder.btnConfirm.setOnClickListener(v -> {
            if (listener != null) listener.onApprove(bill);
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(bill);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvApartment, tvDate, tvStatus, tvTotal;
        TextView tvElectric, tvWater, tvManagement, tvSanitation;
        TextView tvDue;
        Button btnConfirm;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvApartment = v.findViewById(R.id.tvApartment);
            tvDate = v.findViewById(R.id.tvDate);
            tvStatus = v.findViewById(R.id.tvStatus);
            tvTotal = v.findViewById(R.id.tvTotal);
            tvElectric = v.findViewById(R.id.tvElectric);
            tvWater = v.findViewById(R.id.tvWater);
            tvManagement = v.findViewById(R.id.tvManagement);
            tvSanitation = v.findViewById(R.id.tvSanitation);
            tvDue = v.findViewById(R.id.tvDue);
            btnConfirm = v.findViewById(R.id.btnConfirm);
        }
    }
}
