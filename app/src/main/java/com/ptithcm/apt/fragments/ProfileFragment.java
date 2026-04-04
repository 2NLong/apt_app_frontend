package com.ptithcm.apt.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.activities.AuthActivity;
import com.ptithcm.apt.adapters.profile.ApartmentAdapter;
import com.ptithcm.apt.adapters.profile.FamilyMemberAdapter;
import com.ptithcm.apt.fragments.profile.ApartmentDetailsBottomSheet;
import com.ptithcm.apt.fragments.profile.FamilyMemberDetailsBottomSheet;
import com.ptithcm.apt.models.profileuser.Apartment;
import com.ptithcm.apt.models.profileuser.FamilyMember;
import com.ptithcm.apt.viewmodel.auth.LoginViewModel;
import com.ptithcm.apt.viewmodel.auth.LoginViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView rvOwnedApartments;
    private RecyclerView rvFamilyMembers;
    private ApartmentAdapter apartmentAdapter;
    private FamilyMemberAdapter familyMemberAdapter;
    private TextView tvShowMoreApartments, tvShowMoreFamilyMembers;
    private boolean isApartmentsExpanded = false;
    private boolean isFamilyExpanded = false;

    private List<Apartment> ownedApartments;
    private List<FamilyMember> familyMembers;

    private ImageView imgProfile;
    private TextView tvName;
    private TextView tvPhone;
    private Button btnLogout;

    private LoginViewModel loginViewModel;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoginViewModelFactory factory = new LoginViewModelFactory(requireContext());
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        imgProfile = view.findViewById(R.id.img_profile);
        tvName = view.findViewById(R.id.tv_name);
        tvPhone = view.findViewById(R.id.tv_phone);
        rvOwnedApartments = view.findViewById(R.id.rv_owned_apartments);
        rvFamilyMembers = view.findViewById(R.id.rv_family_members);
        tvShowMoreApartments = view.findViewById(R.id.tv_show_more_apartments);
        tvShowMoreFamilyMembers = view.findViewById(R.id.tv_show_more_family_members);
        btnLogout = view.findViewById(R.id.btn_logout);

        ownedApartments = new ArrayList<>();
        familyMembers = new ArrayList<>();

        ownedApartments.add(new Apartment("Căn hộ A-102", "Đang cho thuê", R.drawable.ic_home));
        ownedApartments.add(new Apartment("Căn hộ B-505", "Trống", R.drawable.ic_home));
        ownedApartments.add(new Apartment("Căn hộ B-505", "Trống", R.drawable.ic_home));

        familyMembers.add(new FamilyMember("Lê Thị Bình", "Vợ", R.drawable.ic_person));
        familyMembers.add(new FamilyMember("Nguyễn Văn Nam", "Con", R.drawable.ic_person));
        familyMembers.add(new FamilyMember("Trần Thị Hoa", "Mẹ", R.drawable.ic_person));
        familyMembers.add(new FamilyMember("Nguyễn Văn Bắc", "Em trai", R.drawable.ic_person));

        apartmentAdapter = new ApartmentAdapter(ownedApartments);
        apartmentAdapter.setOnItemClickListener(apartment -> {
            ApartmentDetailsBottomSheet bottomSheet = ApartmentDetailsBottomSheet.newInstance(apartment);
            bottomSheet.show(getChildFragmentManager(), "APT_DETAILS");
        });
        rvOwnedApartments.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvOwnedApartments.setAdapter(apartmentAdapter);

        if (ownedApartments.size() > 3) {
            tvShowMoreApartments.setVisibility(View.VISIBLE);
            tvShowMoreApartments.setOnClickListener(v -> {
                isApartmentsExpanded = !isApartmentsExpanded;
                apartmentAdapter.setExpanded(isApartmentsExpanded);
                tvShowMoreApartments
                        .setText(isApartmentsExpanded ? getString(R.string.collapse) : getString(R.string.see_more));
            });
        }

        familyMemberAdapter = new FamilyMemberAdapter(familyMembers);
        familyMemberAdapter.setOnItemClickListener(member -> {
            FamilyMemberDetailsBottomSheet bottomSheet = FamilyMemberDetailsBottomSheet.newInstance(member);
            bottomSheet.show(getChildFragmentManager(), "MEMBER_DETAILS");
        });
        rvFamilyMembers.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvFamilyMembers.setAdapter(familyMemberAdapter);

        if (familyMembers.size() > 3) {
            tvShowMoreFamilyMembers.setVisibility(View.VISIBLE);
            tvShowMoreFamilyMembers.setOnClickListener(v -> {
                isFamilyExpanded = !isFamilyExpanded;
                familyMemberAdapter.setExpanded(isFamilyExpanded);
                tvShowMoreFamilyMembers
                        .setText(isFamilyExpanded ? getString(R.string.collapse) : getString(R.string.see_more));
            });
        }

        // Đăng xuất
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> loginViewModel.logout());
        }

        loginViewModel.logoutResult.observe(getViewLifecycleOwner(), isLoggedOut -> {
            if (isLoggedOut != null && isLoggedOut) {
                Toast.makeText(requireContext(), "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(requireActivity(), AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        
//        loginViewModel.errorMessage.observe(getViewLifecycleOwner(), error -> {
//            if (error != null) {
//                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
//                loginViewModel.clearError();
//            }
//        });
    }
}
