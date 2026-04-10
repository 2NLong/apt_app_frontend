package com.ptithcm.apt.adapters.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.models.profile.ProfileApartmentResponse;

import java.util.List;

public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.ApartmentViewHolder> {

    private List<ProfileApartmentResponse> apartmentList;
    private OnItemClickListener listener;
    private boolean isExpanded = false;

    public interface OnItemClickListener {
        void onItemClick(ProfileApartmentResponse apartment);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ApartmentAdapter(List<ProfileApartmentResponse> apartmentList) {
        this.apartmentList = apartmentList;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ApartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_apartment, parent, false);
        return new ApartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApartmentViewHolder holder, int position) {
        ProfileApartmentResponse apartment = apartmentList.get(position);
        holder.iconApartment.setImageResource(R.drawable.ic_home);
        holder.tvApartmentName.setText(apartment.getRoomNumber() != null ? apartment.getRoomNumber() : "---");
        holder.tvApartmentStatus.setText(apartment.getRole() != null ? apartment.getRole() : "---");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(apartment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(apartmentList.size(), isExpanded ? Integer.MAX_VALUE : 3);
    }

    public static class ApartmentViewHolder extends RecyclerView.ViewHolder {
        ImageView iconApartment;
        TextView tvApartmentName;
        TextView tvApartmentStatus;

        public ApartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            iconApartment = itemView.findViewById(R.id.icon_apartment);
            tvApartmentName = itemView.findViewById(R.id.tv_apartment_name);
            tvApartmentStatus = itemView.findViewById(R.id.tv_apartment_status);
        }
    }
}