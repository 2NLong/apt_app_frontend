package com.ptithcm.apt.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.models.apartment.Apartment;

import java.util.List;

public class ManagerApartmentAdapter extends RecyclerView.Adapter<ManagerApartmentAdapter.ApartmentViewHolder> {

    private List<Apartment> apartmentList;
    private OnItemClickListener listener; // ĐÃ SỬA: Bỏ cái đường dẫn dài ngoằng trỏ sai package đi

    // Interface dùng để bắt sự kiện click
    public interface OnItemClickListener {
        void onApartmentClick(Apartment apartment);
    }

    // Constructor chuẩn
    public ManagerApartmentAdapter(List<Apartment> apartmentList, OnItemClickListener listener) {
        this.apartmentList = apartmentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ApartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apartment, parent, false);
        return new ApartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApartmentViewHolder holder, int position) {
        Apartment apartment = apartmentList.get(position);

        // Gắn dữ liệu vào giao diện
        holder.tvName.setText("Phòng: " + apartment.getRoomNumber() + " (Tầng " + apartment.getFloor() + ")");
        holder.tvStatus.setText("Trạng thái: " + apartment.getStatus() + " | Diện tích: " + apartment.getArea() + "m²");

        // Bắt sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onApartmentClick(apartment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apartmentList != null ? apartmentList.size() : 0;
    }

    public void updateData(List<Apartment> newData) {
        this.apartmentList = newData;
        notifyDataSetChanged();
    }

    public static class ApartmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStatus;

        public ApartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_apartment_name);
            tvStatus = itemView.findViewById(R.id.tv_apartment_status);
        }
    }
}
