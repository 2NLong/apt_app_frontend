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
import java.math.BigDecimal;

import com.ptithcm.apt.utils.FormatUtils;
import com.ptithcm.apt.utils.RoleTranslator;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
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
                tvRoomNumber.setText("Căn hộ " + (apt.getRoomNumber() != null ? apt.getRoomNumber() : "---"));

                // Trạng thái hiển thị từ vai trò (Role)
                tvStatus.setText(RoleTranslator.translateRole(apt.getRole()));

                tvFloor.setText("Tầng: " + (apt.getFloor() != null ? apt.getFloor() : "---"));
                tvArea.setText("Diện tích: " + (apt.getArea() != null ? apt.getArea() + "m²" : "---"));

                String roleText = "Vai trò: " + RoleTranslator.translateRole(apt.getRole());
                if (Boolean.TRUE.equals(apt.getIsHead())) {
                    roleText += " (Chủ hộ)";
                }
                tvRole.setText(roleText);

                if (apt.getContractStart() != null || apt.getContractEnd() != null) {
                    String start = apt.getContractStart() != null ? FormatUtils.formatDate(apt.getContractStart())
                            : "...";
                    String end = apt.getContractEnd() != null ? FormatUtils.formatDate(apt.getContractEnd()) : "...";
                    tvContractPeriod.setText("Hợp đồng: " + start + " - " + end);
                    tvContractPeriod.setVisibility(View.VISIBLE);
                } else {
                    tvContractPeriod.setVisibility(View.GONE);
                }

                boolean isOwner = "OWNER".equalsIgnoreCase(apt.getRole());

                if (!isOwner && apt.getRentalPrice() != null) {
                    tvRentalPrice.setText("Giá thuê: " + FormatUtils.formatCurrency(apt.getRentalPrice()));
                    tvRentalPrice.setVisibility(View.VISIBLE);
                } else {
                    tvRentalPrice.setVisibility(View.GONE);
                }

                if (!isOwner && apt.getDepositAmount() != null) {
                    tvDepositAmount.setText("Tiền cọc: " + FormatUtils.formatCurrency(apt.getDepositAmount()));
                    tvDepositAmount.setVisibility(View.VISIBLE);
                } else {
                    tvDepositAmount.setVisibility(View.GONE);
                }
            }
        }
    }
}
