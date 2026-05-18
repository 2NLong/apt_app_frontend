package com.ptithcm.apt.adapters.rentinvoice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ptithcm.apt.R;
import com.ptithcm.apt.models.rentinvoice.response.UserRentInvoiceListResponse;
import java.text.DecimalFormat;
import java.util.List;

public class UserRentAdapter extends RecyclerView.Adapter<UserRentAdapter.RentViewHolder> {

    private List<UserRentInvoiceListResponse> rentList;
    private final OnRentItemClickListener listener;

    public interface OnRentItemClickListener {
        void onRentClick(UserRentInvoiceListResponse rent);
    }

    public UserRentAdapter(List<UserRentInvoiceListResponse> rentList, OnRentItemClickListener listener) {
        this.rentList = rentList;
        this.listener = listener;
    }

    public void updateList(List<UserRentInvoiceListResponse> newList) {
        this.rentList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_rent_invoice, parent, false);
        return new RentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentViewHolder holder, int position) {
        UserRentInvoiceListResponse rent = rentList.get(position);
        holder.bind(rent, listener);
    }

    @Override
    public int getItemCount() {
        return rentList != null ? rentList.size() : 0;
    }

    static class RentViewHolder extends RecyclerView.ViewHolder {
        TextView tvApartment, tvBillingDate, tvStatus, tvPrice, tvDueDate, tvRole, tvRentTenantName;
        DecimalFormat formatter = new DecimalFormat("#,###đ");

        public RentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvApartment = itemView.findViewById(R.id.tvRentApartmentName);
            tvBillingDate = itemView.findViewById(R.id.tvRentBillingDate);
            tvStatus = itemView.findViewById(R.id.tvRentStatus);
            tvPrice = itemView.findViewById(R.id.tvRentPrice);
            tvDueDate = itemView.findViewById(R.id.tvRentDueDate);
            tvRole = itemView.findViewById(R.id.tvRentRoleLabel);
            tvRentTenantName = itemView.findViewById(R.id.tvRentTenantName);
        }

        public void bind(UserRentInvoiceListResponse rent, OnRentItemClickListener listener) {
            tvApartment.setText("Căn hộ " + rent.getApartmentName());
            tvBillingDate.setText("Kỳ hạn tháng " + String.format("%02d", rent.getBillingMonth()) + "/" + rent.getBillingYear());
            tvPrice.setText(formatter.format(rent.getRentAmount()));

            // Xử lý hạn thanh toán (cắt chuỗi lấy phần ngày)
            if (rent.getDueDate() != null && rent.getDueDate().length() >= 10) {
                tvDueDate.setText("Hạn: " + rent.getDueDate().substring(0, 10));
            }

            // Xử lý vai trò
            String role = rent.getViewerRole(); // "OWNER" hoặc "TENANT"
            tvRole.setText("Vai trò: " + (role.equals("OWNER") ? "Chủ sở hữu" : "Khách thuê"));

            // Logic hiển thị tên người thuê nếu mình là OWNER
            if ("OWNER".equals(role) && rent.getTenantName() != null) {
                tvRentTenantName.setVisibility(View.VISIBLE);
                tvRentTenantName.setText("Khách thuê: " + rent.getTenantName());
            } else {
                tvRentTenantName.setVisibility(View.GONE);
            }

            // Xử lý trạng thái
            switch (rent.getStatus()) {
                case PAID:
                    tvStatus.setText("Đã thanh toán");
                    tvStatus.setTextColor(0xFF4CAF50); // Xanh lá
//                    tvStatus.setBackgroundResource(R.drawable.bg_button);
                    break;
                case UNPAID:
                    tvStatus.setText("Chưa thanh toán");
                    tvStatus.setTextColor(0xFFFF9800); // Cam
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_unpaid);
                    break;
                case LATE:
                    tvStatus.setText("Trễ hạn");
                    tvStatus.setTextColor(0xFFF44336); // Đỏ
//                    tvStatus.setBackgroundResource(R.drawable.bg_status_late);
                    break;
            }

            itemView.setOnClickListener(v -> listener.onRentClick(rent));
        }
    }
}