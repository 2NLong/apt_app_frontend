package com.ptithcm.apt.adapters.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.models.profileuser.Apartment;

import java.util.List;

public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.ApartmentViewHolder> {

    private List<Apartment> apartmentList;
    private OnItemClickListener listener;
    private boolean isExpanded = false;

    public interface OnItemClickListener {
        void onItemClick(Apartment apartment);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ApartmentAdapter(List<Apartment> apartmentList) {
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
        Apartment apartment = apartmentList.get(position);
        holder.iconApartment.setImageResource(apartment.getIconResId());
        holder.tvApartmentName.setText(apartment.getName());
        holder.tvApartmentStatus.setText(apartment.getStatus());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(apartment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(apartmentList.size(), isExpanded ? 10 : 3);
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