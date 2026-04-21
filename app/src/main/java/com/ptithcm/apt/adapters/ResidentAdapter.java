package com.ptithcm.apt.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.models.resident.ResidentListResponse;

import java.util.List;

public class ResidentAdapter extends RecyclerView.Adapter<ResidentAdapter.ResidentViewHolder> {

    private List<ResidentListResponse> list;
    private final OnItemClickListener listener;

    // Interface để truyền sự kiện click ra bên ngoài Fragment
    public interface OnItemClickListener {
        void onItemClick(ResidentListResponse resident);
    }

    public ResidentAdapter(List<ResidentListResponse> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setData(List<ResidentListResponse> newData) {
        this.list.clear();
        this.list.addAll(newData);
        notifyDataSetChanged();
    }

    public void addData(List<ResidentListResponse> newData) {
        int startPosition = this.list.size();
        this.list.addAll(newData);
        notifyItemRangeInserted(startPosition, newData.size());
    }

    @NonNull
    @Override
    public ResidentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resident, parent, false);
        return new ResidentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResidentViewHolder holder, int position) {
        ResidentListResponse resident = list.get(position);

        holder.tvName.setText(resident.getFullName());
        holder.tvRoom.setText("Phòng: " + resident.getRoomNumber());

        holder.tvPhone.setText(resident.getPhone() != null && !resident.getPhone().isEmpty() ? "SĐT: " + resident.getPhone() : "SĐT: ---");
        holder.tvCccd.setText(resident.getCitizenIdentity() != null && !resident.getCitizenIdentity().isEmpty() ? "CCCD: " + resident.getCitizenIdentity() : "CCCD: ---");

        String role = resident.getRole();

        if ("OWNER".equals(role)) {
            holder.tvRole.setText("CHỦ SỞ HỮU");
            holder.tvRole.setTextColor(Color.parseColor("#D32F2F")); // Chữ Đỏ
            holder.tvRole.setBackgroundColor(Color.parseColor("#FCE4E4")); // Nền Hồng nhạt

        } else if ("TENANT".equals(role)) {
            holder.tvRole.setText("NGƯỜI THUÊ");
            holder.tvRole.setTextColor(Color.parseColor("#F57C00")); // Chữ Cam
            holder.tvRole.setBackgroundColor(Color.parseColor("#FFF3E0")); // Nền Cam nhạt

        } else {
            // Mặc định là MEMBER (Thành viên)
            holder.tvRole.setText("THÀNH VIÊN");
            holder.tvRole.setTextColor(Color.parseColor("#666666")); // Chữ Xám
            holder.tvRole.setBackgroundColor(Color.parseColor("#E0E0E0")); // Nền Xám nhạt
        }
        // Bắt sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(resident);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
    public static class ResidentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRole, tvRoom, tvPhone, tvCccd;

        public ResidentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_resident_name);
            tvRole = itemView.findViewById(R.id.tv_item_resident_role);
            tvRoom = itemView.findViewById(R.id.tv_item_resident_room);
            tvPhone = itemView.findViewById(R.id.tv_item_resident_phone);
            tvCccd = itemView.findViewById(R.id.tv_item_resident_cccd);
        }
    }
}