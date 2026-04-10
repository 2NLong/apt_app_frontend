package com.ptithcm.apt.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ptithcm.apt.R;
import com.ptithcm.apt.activities.AuthActivity;
import com.ptithcm.apt.adapters.profile.ApartmentAdapter;
import com.ptithcm.apt.adapters.profile.FamilyMemberAdapter;
import com.ptithcm.apt.fragments.profile.ApartmentDetailsBottomSheet;
import com.ptithcm.apt.fragments.profile.FamilyMemberDetailsBottomSheet;
import com.ptithcm.apt.models.profile.FamilyMemberResponse;
import com.ptithcm.apt.models.profile.ProfileApartmentResponse;
import com.ptithcm.apt.models.profile.ProfileInfoResponse;
import com.ptithcm.apt.viewmodel.auth.LoginViewModel;
import com.ptithcm.apt.viewmodel.auth.LoginViewModelFactory;
import com.ptithcm.apt.viewmodel.profile.ProfileViewModel;
import com.ptithcm.apt.viewmodel.profile.ProfileViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    // --- Header ---
    private TextView tvName, tvEmail;

    // --- Personal Info card ---
    private TextView tvDob, tvCitizenIdentity, tvPhone;

    // --- Current Apartment card ---
    private TextView tvStatusCurrent, tvApartmentNameCurrent, tvFloorCurrent, tvAreaCurrent;

    // --- Owned Apartments RecyclerView ---
    private RecyclerView rvOwnedApartments;
    private TextView tvShowMoreApartments;
    private ApartmentAdapter apartmentAdapter;
    private List<ProfileApartmentResponse> apartmentList;
    private boolean isApartmentsExpanded = false;

    // --- Family Members RecyclerView ---
    private RecyclerView rvFamilyMembers;
    private TextView tvShowMoreFamilyMembers;
    private FamilyMemberAdapter familyMemberAdapter;
    private List<FamilyMemberResponse> familyMemberList;
    private boolean isFamilyExpanded = false;

    // --- ViewModels ---
    private LoginViewModel loginViewModel;
    private ProfileViewModel profileViewModel;

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

        // ViewModels
        LoginViewModelFactory loginFactory = new LoginViewModelFactory(requireContext());
        loginViewModel = new ViewModelProvider(this, loginFactory).get(LoginViewModel.class);

        ProfileViewModelFactory profileFactory = new ProfileViewModelFactory();
        profileViewModel = new ViewModelProvider(this, profileFactory).get(ProfileViewModel.class);

        // BIND VIEW
        // Header
        View header = view.findViewById(R.id.layout_header);
        tvName = header.findViewById(R.id.tv_name);
        tvEmail = header.findViewById(R.id.tv_email);

        // Personal info card
        View personalInfo = view.findViewById(R.id.layout_personal_info);
        tvDob = personalInfo.findViewById(R.id.tv_dob);
        tvCitizenIdentity = personalInfo.findViewById(R.id.tv_citizen_identity);
        tvPhone = personalInfo.findViewById(R.id.tv_phone);

        // Current apartment card
        View currentApt = view.findViewById(R.id.layout_current_apartment);
        tvStatusCurrent = currentApt.findViewById(R.id.tv_status_current);
        tvApartmentNameCurrent = currentApt.findViewById(R.id.tv_apartment_name_current);
        tvFloorCurrent = currentApt.findViewById(R.id.tv_floor_current);
        tvAreaCurrent = currentApt.findViewById(R.id.tv_area_current);

        // Owned apartments RecyclerView
        rvOwnedApartments = view.findViewById(R.id.rv_owned_apartments);
        tvShowMoreApartments = view.findViewById(R.id.tv_show_more_apartments);

        // Family members RecyclerView
        rvFamilyMembers = view.findViewById(R.id.rv_family_members);
        tvShowMoreFamilyMembers = view.findViewById(R.id.tv_show_more_family_members);

        // Logout button
        View settings = view.findViewById(R.id.layout_settings);
        settings.findViewById(R.id.btn_logout).setOnClickListener(v -> loginViewModel.logout());

        // ADAPTERS
        apartmentList = new ArrayList<>();
        apartmentAdapter = new ApartmentAdapter(apartmentList);
        apartmentAdapter.setOnItemClickListener(apt -> {
            ApartmentDetailsBottomSheet.newInstance(apt)
                    .show(getChildFragmentManager(), "APT_DETAILS");
        });
        rvOwnedApartments.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvOwnedApartments.setAdapter(apartmentAdapter);

        familyMemberList = new ArrayList<>();
        familyMemberAdapter = new FamilyMemberAdapter(familyMemberList);
        familyMemberAdapter.setOnItemClickListener(member -> {
            FamilyMemberDetailsBottomSheet.newInstance(member)
                    .show(getChildFragmentManager(), "MEMBER_DETAILS");
        });
        rvFamilyMembers.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvFamilyMembers.setAdapter(familyMemberAdapter);

        // OBSERVERS
        loginViewModel.logoutResult.observe(getViewLifecycleOwner(), isLoggedOut -> {
            if (Boolean.TRUE.equals(isLoggedOut)) {
                Toast.makeText(requireContext(), "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireActivity(), AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        profileViewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        profileViewModel.profileData.observe(getViewLifecycleOwner(), dashboard -> {
            if (dashboard == null)
                return;

            // String jsonData = new Gson().toJson(dashboard);
            // Log.d("DEBUG_PROFILE", "Toàn bộ Profile Dashboard: " + jsonData);
            // --- Thông tin cá nhân ---
            ProfileInfoResponse info = dashboard.getPersonalInfo();
            if (info != null) {
                tvName.setText(info.getFullName() != null ? info.getFullName() : "");
                tvEmail.setText(info.getEmail() != null ? info.getEmail() : "");
                tvDob.setText(info.getDob() != null ? info.getDob() : "---");
                tvCitizenIdentity.setText(info.getCitizenIdentity() != null ? info.getCitizenIdentity() : "---");
                tvPhone.setText(info.getPhone() != null ? info.getPhone() : "---");
            }

            // --- Căn hộ đang ở ---
            ProfileApartmentResponse living = dashboard.getLivingApartment();
            if (living != null) {
                tvStatusCurrent.setText(living.getRole() != null ? living.getRole() : "");
                tvApartmentNameCurrent
                        .setText("Căn hộ " + (living.getRoomNumber() != null ? living.getRoomNumber() : "---"));
                tvFloorCurrent.setText("Tầng " + (living.getFloor() != null ? living.getFloor() : "---"));
                tvAreaCurrent.setText(living.getArea() != null ? living.getArea() + "m²" : "---");
                view.findViewById(R.id.layout_current_apartment).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.layout_current_apartment).setVisibility(View.GONE);
            }

            // --- Căn hộ sở hữu ---
            apartmentList.clear();
            if (dashboard.getOwnedApartments() != null) {
                apartmentList.addAll(dashboard.getOwnedApartments());
            }

            apartmentAdapter.notifyDataSetChanged();
            setupShowMore(tvShowMoreApartments, apartmentList.size(), apartmentAdapter,
                    () -> isApartmentsExpanded, v -> isApartmentsExpanded = v);

            // --- Thành viên gia đình ---
            familyMemberList.clear();
            if (dashboard.getFamilyMembers() != null) {
                familyMemberList.addAll(dashboard.getFamilyMembers());
            }
            familyMemberAdapter.notifyDataSetChanged();
            setupShowMore(tvShowMoreFamilyMembers, familyMemberList.size(), familyMemberAdapter,
                    () -> isFamilyExpanded, v -> isFamilyExpanded = v);
        });

        // Gọi API
        profileViewModel.fetchProfileDashboard();
    }

    /** trạng thái expanded */
    interface BooleanGetter {
        boolean get();
    }

    interface BooleanSetter {
        void set(boolean v);
    }

    private void setupShowMore(TextView tvShowMore, int total, Object adapter,
            BooleanGetter getter, BooleanSetter setter) {
        if (total > 3) {
            tvShowMore.setVisibility(View.VISIBLE);
            tvShowMore.setOnClickListener(v -> {
                boolean expanded = !getter.get();
                setter.set(expanded);
                if (adapter instanceof ApartmentAdapter) {
                    ((ApartmentAdapter) adapter).setExpanded(expanded);
                } else if (adapter instanceof FamilyMemberAdapter) {
                    ((FamilyMemberAdapter) adapter).setExpanded(expanded);
                }
                tvShowMore.setText(expanded ? R.string.collapse : R.string.see_more);
            });
        } else {
            tvShowMore.setVisibility(View.GONE);
        }
    }
}
