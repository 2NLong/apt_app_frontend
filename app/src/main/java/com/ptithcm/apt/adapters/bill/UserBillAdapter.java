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
    private OnBillClickListener listener;

    // Định dạng hiển thị: 28/04/2026
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    // Định dạng parse từ API: 2026-04-28...
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

    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "0đ";
        DecimalFormat formatter = new DecimalFormat("#,###đ");
        return formatter.format(amount);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_bill, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserBillListResponse bill = list.get(position);

        // 1. Mapping các thông tin cơ bản (Giữ nguyên)
        holder.tvApartment.setText("Căn hộ " + bill.getApartmentName());
        holder.tvDate.setText("Kỳ hóa đơn: Tháng " + bill.getBillingMonth() + "/" + bill.getBillingYear());
        holder.tvTotal.setText(formatMoney(bill.getTotalAmount()));
        // ... set các loại phí điện, nước ...

        // 2. LOGIC HIỂN THỊ THÔNG TIN CHỦ HỘ / NGƯỜI THUÊ (THEO YÊU CẦU MỚI)
        String role = bill.getViewerRole(); // "HEAD" hoặc "OWNER"
        String tenant = bill.getTenantName();

        if ("HEAD".equals(role)) {
            // TRƯỜNG HỢP 1: Bạn đang ở (HEAD)
            // Không hiện vai trò, không hiện tên người thuê vì chính là mình
            holder.tvRole.setVisibility(View.GONE);
            holder.tvTenant.setVisibility(View.GONE);
        }
        else if ("OWNER".equals(role)) {
            // Bạn là chủ sở hữu, hiện vai trò để phân biệt với căn đang ở
            holder.tvRole.setVisibility(View.VISIBLE);
            holder.tvRole.setText("Vai trò: Chủ sở hữu");

            if (tenant != null && !tenant.isEmpty()) {
                // TRƯỜNG HỢP 2: Căn đang cho thuê
                holder.tvTenant.setVisibility(View.VISIBLE);
                holder.tvTenant.setText("Khách thuê: " + tenant);
            } else {
                // TRƯỜNG HỢP 3: Căn đang trống
                holder.tvTenant.setVisibility(View.VISIBLE);
                holder.tvTenant.setText("Trạng thái: Đang trống");
                holder.tvTenant.setTextColor(Color.GRAY);
            }
        }

        // 4. Xử lý DueDate (Hạn thanh toán)
        String rawDueDate = bill.getDueDate(); // Đã cập nhật trong model
        if (rawDueDate != null && rawDueDate.length() >= 10 && bill.getStatus() != BillStatus.PAID) {
            try {
                String datePart = rawDueDate.substring(0, 10);
                Date date = apiFormat.parse(datePart);
                if (date != null) {
                    holder.tvDue.setVisibility(View.VISIBLE);
                    holder.tvDue.setText("Hạn: " + displayFormat.format(date));
                }
            } catch (Exception e) {
                holder.tvDue.setText("Hạn: " + rawDueDate);
            }
        } else {
            // Nếu đã thanh toán (PAID) thì ẩn hạn đóng đi cho gọn
            holder.tvDue.setVisibility(View.GONE);
        }

        // 5. Xử lý Trạng thái & Màu sắc
        if (bill.getStatus() == BillStatus.PAID) {
            holder.tvStatus.setText("ĐÃ THANH TOÁN");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
        }
        else if (bill.getStatus() == BillStatus.LATE) {
            holder.tvStatus.setText("QUÁ HẠN");
            holder.tvStatus.setTextColor(Color.parseColor("#F44336"));
            holder.tvDue.setTextColor(Color.parseColor("#F44336")); // Chuyển ngày hạn sang màu đỏ cảnh báo
        }
        else { // UNPAID
            holder.tvStatus.setText("CHƯA THANH TOÁN");
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800"));
            holder.tvDue.setTextColor(Color.parseColor("#757575")); // Màu xám bình thường
        }

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