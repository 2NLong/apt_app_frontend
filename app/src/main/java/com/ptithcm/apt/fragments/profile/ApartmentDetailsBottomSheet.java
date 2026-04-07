package com.ptithcm.apt.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ptithcm.apt.R;
import com.ptithcm.apt.models.profile.ProfileApartmentResponse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class ApartmentDetailsBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_APARTMENT = "arg_apartment";

    public static ApartmentDetailsBottomSheet newInstance(ProfileApartmentResponse apartment) {
        ApartmentDetailsBottomSheet fragment = new ApartmentDetailsBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_APARTMENT, (Serializable) apartment);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_apartment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView iconApartment = view.findViewById(R.id.dialog_icon_apartment);
        TextView tvRoomNumber = view.findViewById(R.id.dialog_tv_room_number);
        TextView tvStatus = view.findViewById(R.id.dialog_tv_status);
        TextView tvFloor = view.findViewById(R.id.dialog_tv_floor);
        TextView tvArea = view.findViewById(R.id.dialog_tv_area);
        TextView tvRole = view.findViewById(R.id.dialog_tv_role);
        TextView tvContractPeriod = view.findViewById(R.id.dialog_tv_contract_period);
        TextView tvRentalPrice = view.findViewById(R.id.dialog_tv_rental_price);
        TextView tvDepositAmount = view.findViewById(R.id.dialog_tv_deposit_amount);
        Button btnClose = view.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(v -> dismiss());

        if (getArguments() != null) {
            ProfileApartmentResponse apt = (ProfileApartmentResponse) getArguments().getSerializable(ARG_APARTMENT);
            if (apt != null) {
                iconApartment.setImageResource(R.drawable.ic_home);
                tvRoomNumber.setText("Can ho " + (apt.getRoomNumber() != null ? apt.getRoomNumber() : "---"));

                // Status hien thi tu role
                tvStatus.setText(apt.getRole() != null ? apt.getRole() : "---");

                tvFloor.setText("Tang: " + (apt.getFloor() != null ? apt.getFloor() : "---"));
                tvArea.setText("Dien tich: " + (apt.getArea() != null ? apt.getArea() + "m2" : "---"));

                String roleText = "Vai tro: " + (apt.getRole() != null ? apt.getRole() : "---");
                if (Boolean.TRUE.equals(apt.getIsHead())) {
                    roleText += " (Chu ho)";
                }
                tvRole.setText(roleText);

                if (apt.getContractStart() != null || apt.getContractEnd() != null) {
                    String start = apt.getContractStart() != null ? apt.getContractStart() : "...";
                    String end = apt.getContractEnd() != null ? apt.getContractEnd() : "...";
                    tvContractPeriod.setText("Hop dong: " + start + " - " + end);
                    tvContractPeriod.setVisibility(View.VISIBLE);
                } else {
                    tvContractPeriod.setVisibility(View.GONE);
                }

                if (apt.getRentalPrice() != null) {
                    tvRentalPrice.setText("Giá thuê: " + formatCurrency(apt.getRentalPrice()));
                    tvRentalPrice.setVisibility(View.VISIBLE);
                } else {
                    tvRentalPrice.setVisibility(View.GONE);
                }

                if (apt.getDepositAmount() != null) {
                    tvDepositAmount.setText("Tiền cọc: " + formatCurrency(apt.getDepositAmount()));
                    tvDepositAmount.setVisibility(View.VISIBLE);
                } else {
                    tvDepositAmount.setVisibility(View.GONE);
                }
            }
        }
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "---";
        try {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
            return formatter.format(amount);
        } catch (Exception e) {
            return amount.toString() + " VNĐ";
        }
    }
}
