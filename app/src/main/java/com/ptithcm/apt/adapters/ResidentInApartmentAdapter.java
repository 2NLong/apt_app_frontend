package com.ptithcm.apt.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.models.resident.ResidentListResponse;

import java.util.List;

public class ResidentInApartmentAdapter extends RecyclerView.Adapter<ResidentInApartmentAdapter.ViewHolder> {


    private List<ResidentListResponse> residentList;

    public ResidentInApartmentAdapter(List<ResidentListResponse> residentList) {
        this.residentList = residentList;
    }

    public void updateData(List<ResidentListResponse> newData) {
        this.residentList = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resident_in_apartment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResidentListResponse resident = residentList.get(position);

        holder.tvName.setText(resident.getFullName());
        holder.tvCccd.setText("CCCD: " + (resident.getCitizenIdentity() != null ? resident.getCitizenIdentity() : "Chưa cập nhật"));
        holder.tvPhone.setText("SĐT: " + (resident.getPhone() != null ? resident.getPhone() : "Chưa cập nhật"));

        String roleRaw = resident.getRole();
        String displayRole = "Thành viên";

        if (roleRaw != null) {
            switch (roleRaw.toUpperCase()) {
                case "OWNER":
                    displayRole = "Chủ Sở Hữu";
                    break;
                case "TENANT":
                    displayRole = "Người Thuê";
                    break;
                case "MEMBER":
                    displayRole = "Thành viên";
                    break;
                default:
                    displayRole = "Thành viên";
                    break;
            }
        }

        holder.tvRole.setText(displayRole);
    }

    @Override
    public int getItemCount() {
        return residentList != null ? residentList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvRole;
        TextView tvCccd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_resident_name);
            tvPhone = itemView.findViewById(R.id.tv_resident_phone);
            tvCccd = itemView.findViewById(R.id.tv_resident_cccd);
            tvRole = itemView.findViewById(R.id.tv_resident_role);
        }
    }
}