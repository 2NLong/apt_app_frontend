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
import com.ptithcm.apt.models.profileuser.FamilyMember;

public class FamilyMemberDetailsBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_MEMBER = "arg_member";

    public static FamilyMemberDetailsBottomSheet newInstance(FamilyMember member) {
        FamilyMemberDetailsBottomSheet fragment = new FamilyMemberDetailsBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEMBER, member);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_family_member_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView iconPerson = view.findViewById(R.id.dialog_icon_person);
        TextView tvFullName = view.findViewById(R.id.dialog_tv_full_name);
        TextView tvRoleTag = view.findViewById(R.id.dialog_tv_role_tag);
        TextView tvRelation = view.findViewById(R.id.dialog_tv_relation);
        TextView tvCitizenIdentity = view.findViewById(R.id.dialog_tv_citizen_identity);
        TextView tvPhone = view.findViewById(R.id.dialog_tv_phone);
        TextView tvEmail = view.findViewById(R.id.dialog_tv_email);
        TextView tvDob = view.findViewById(R.id.dialog_tv_dob);
        Button btnClose = view.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(v -> dismiss());

        if (getArguments() != null) {
            FamilyMember member = (FamilyMember) getArguments().getSerializable(ARG_MEMBER);
            if (member != null) {
                iconPerson.setImageResource(member.getIconResId() != 0 ? member.getIconResId() : R.drawable.ic_person);
                tvFullName.setText(member.getFullName());

                String roleText = member.getRole() != null ? member.getRole() : "Thành viên";
                if (Boolean.TRUE.equals(member.getIsHead())) {
                    roleText = "Chủ hộ";
                }
                tvRoleTag.setText(roleText);

                if (member.getRelation() != null && !member.getRelation().isEmpty()) {
                    tvRelation.setText("(" + member.getRelation() + ")");
                    tvRelation.setVisibility(View.VISIBLE);
                } else {
                    tvRelation.setVisibility(View.GONE);
                }

                tvCitizenIdentity.setText("CCCD/CMND: " + (member.getCitizenIdentity() != null ? member.getCitizenIdentity() : "---"));
                tvPhone.setText("SĐT: " + (member.getPhone() != null ? member.getPhone() : "---"));
                tvEmail.setText("Email: " + (member.getEmail() != null ? member.getEmail() : "---"));
                tvDob.setText("Ngày sinh: " + (member.getDob() != null ? member.getDob() : "---"));
            }
        }
    }
}
