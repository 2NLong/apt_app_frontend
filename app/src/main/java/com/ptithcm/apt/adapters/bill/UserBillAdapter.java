package com.ptithcm.apt.adapters.bill;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.models.bill.response.UserBillListResponse;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserBillAdapter extends RecyclerView.Adapter<UserBillAdapter.ViewHolder> {

    private List<UserBillListResponse> list;
    private final OnBillClickListener listener;
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public interface OnBillClickListener {
        void onItemClick(UserBillListResponse bill);
    }

    public UserBillAdapter(List<UserBillListResponse> list, OnBillClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void updateList(List<UserBillListResponse> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_bill, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserBillListResponse bill = list.get(position);

        bindBasicInfo(holder, bill);
        bindFees(holder, bill);
        bindUserRole(holder, bill);
        bindStatusAndDueDate(holder, bill);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(bill);
        });
    }

    // 1. Hiển thị thông tin cơ bản: Tên căn hộ, kỳ hóa đơn, tổng tiền
    private void bindBasicInfo(ViewHolder holder, UserBillListResponse bill) {
        holder.tvApartment.setText("Căn hộ " + bill.getApartmentName());
        holder.tvDate.setText("Kỳ hóa đơn: Tháng " + bill.getBillingMonth() + "/" + bill.getBillingYear());
        holder.tvTotal.setText(formatMoney(bill.getTotalAmount()));
    }

    // 2. Hiển thị chi tiết các loại phí (Sửa lỗi 0đ)
    private void bindFees(ViewHolder holder, UserBillListResponse bill) {
        holder.tvElectric.setText(formatMoney(bill.getElectricityFee()));
        holder.tvWater.setText(formatMoney(bill.getWaterFee()));
        holder.tvManagement.setText(formatMoney(bill.getManagementFee()));
        holder.tvSanitation.setText(formatMoney(bill.getSanitationFee()));
    }

    // 3. Logic hiển thị vai trò và tên khách thuê
    private void bindUserRole(ViewHolder holder, UserBillListResponse bill) {
        String role = bill.getViewerRole();
        String tenantName = bill.getTenantName();

        if ("OWNER".equals(role) && tenantName != null && !tenantName.trim().isEmpty()) {
            holder.tvRole.setVisibility(View.VISIBLE);
            holder.tvRole.setText("Vai trò: Chủ sở hữu");

            holder.tvTenant.setVisibility(View.VISIBLE);
            holder.tvTenant.setText("Khách thuê: " + tenantName);
            holder.tvTenant.setTextColor(Color.parseColor("#616161"));
        } else {
            holder.tvRole.setVisibility(View.GONE);
            holder.tvTenant.setVisibility(View.GONE);
        }
    }

    // 4. Xử lý Trạng thái & Hạn thanh toán
    private void bindStatusAndDueDate(ViewHolder holder, UserBillListResponse bill) {
        BillStatus status = bill.getStatus();

        // Cấu hình trạng thái
        if (status == BillStatus.PAID) {
            holder.tvStatus.setText("ĐÃ THANH TOÁN");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            holder.tvDue.setVisibility(View.GONE);
        } else {
            holder.tvStatus.setText(status == BillStatus.LATE ? "QUÁ HẠN" : "CHƯA THANH TOÁN");
            holder.tvStatus.setTextColor(status == BillStatus.LATE ? Color.parseColor("#F44336") : Color.parseColor("#FF9800"));

            // Hiển thị ngày hạn
            setupDueDate(holder, bill.getDueDate(), status == BillStatus.LATE);
        }
    }

    private void setupDueDate(ViewHolder holder, String rawDueDate, boolean isLate) {
        if (rawDueDate != null && rawDueDate.length() >= 10) {
            try {
                Date date = apiFormat.parse(rawDueDate.substring(0, 10));
                if (date != null) {
                    holder.tvDue.setVisibility(View.VISIBLE);
                    holder.tvDue.setText("Hạn: " + displayFormat.format(date));
                    holder.tvDue.setTextColor(isLate ? Color.parseColor("#F44336") : Color.parseColor("#757575"));
                    return;
                }
            } catch (Exception ignored) {}
        }
        holder.tvDue.setVisibility(View.GONE);
    }

    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "0đ";
        return new DecimalFormat("#,###đ").format(amount);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvApartment, tvDate, tvStatus, tvTotal;
        TextView tvElectric, tvWater, tvManagement, tvSanitation;
        TextView tvDue, tvRole, tvTenant;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvApartment = v.findViewById(R.id.tvApartmentName);
            tvDate = v.findViewById(R.id.tvBillingDate);
            tvStatus = v.findViewById(R.id.tvStatus);
            tvTotal = v.findViewById(R.id.tvTotalAmount);
            tvElectric = v.findViewById(R.id.tvElectric);
            tvWater = v.findViewById(R.id.tvWater);
            tvManagement = v.findViewById(R.id.tvManagement);
            tvSanitation = v.findViewById(R.id.tvSanitation);
            tvDue = v.findViewById(R.id.tvDueDate);
            tvRole = v.findViewById(R.id.tvRoleLabel);
            tvTenant = v.findViewById(R.id.tvTenantName);
        }
    }
}