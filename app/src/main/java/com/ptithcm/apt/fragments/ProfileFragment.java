package com.ptithcm.apt.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.profileuser.ApartmentAdapter;
import com.ptithcm.apt.adapters.profileuser.FamilyMemberAdapter;
import com.ptithcm.apt.models.profileuser.Apartment;
import com.ptithcm.apt.models.profileuser.FamilyMember;


import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView rvOwnedApartments;
    private RecyclerView rvFamilyMembers;
    private ApartmentAdapter apartmentAdapter;
    private FamilyMemberAdapter familyMemberAdapter;

    private List<Apartment> ownedApartments;
    private List<FamilyMember> familyMembers;

    // UI Elements
    private ImageView imgProfile;
    private TextView tvName;
    private TextView tvPhone;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Nạp giao diện cho Fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    // Viết toàn bộ logic ánh xạ và setup dữ liệu ở hàm này
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Ánh xạ UI Elements: Phải có chữ "view." ở đằng trước
        imgProfile = view.findViewById(R.id.img_profile);
        tvName = view.findViewById(R.id.tv_name);
        tvPhone = view.findViewById(R.id.tv_phone);
        rvOwnedApartments = view.findViewById(R.id.rv_owned_apartments);
        rvFamilyMembers = view.findViewById(R.id.rv_family_members);

        // 2. Khởi tạo danh sách dữ liệu
        ownedApartments = new ArrayList<>();
        familyMembers = new ArrayList<>();

        // Thêm dữ liệu giả lập
        ownedApartments.add(new Apartment("Căn hộ A-102", "Đang cho thuê", R.drawable.ic_home));
        ownedApartments.add(new Apartment("Căn hộ B-505", "Trống", R.drawable.ic_home));
        ownedApartments.add(new Apartment("Căn hộ B-505", "Trống", R.drawable.ic_home));


        familyMembers.add(new FamilyMember("Lê Thị Bình", "Vợ", R.drawable.ic_person));
        familyMembers.add(new FamilyMember("Nguyễn Văn Nam", "Con", R.drawable.ic_person));

        // 3. Setup RecyclerView
        apartmentAdapter = new ApartmentAdapter(ownedApartments);
        // LƯU Ý: Dùng requireContext() thay vì "this"
        rvOwnedApartments.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvOwnedApartments.setAdapter(apartmentAdapter);

        familyMemberAdapter = new FamilyMemberAdapter(familyMembers);
        // LƯU Ý: Dùng requireContext() thay vì "this"
        rvFamilyMembers.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvFamilyMembers.setAdapter(familyMemberAdapter);

        // Cập nhật thông tin người dùng
        // tvName.setText("Tên Người Dùng");
        // tvPhone.setText("Số Điện Thoại");
    }
}