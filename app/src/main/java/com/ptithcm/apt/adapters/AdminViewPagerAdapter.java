package com.ptithcm.apt.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ptithcm.apt.fragments.admin.AdminBillFragment;
import com.ptithcm.apt.fragments.admin.AdminHomeFragment;
import com.ptithcm.apt.fragments.admin.AdminServiceConfigFragment;
import com.ptithcm.apt.fragments.admin.AdminNotificationFragment;

import java.util.ArrayList;
import java.util.List;

public class AdminViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();

    public AdminViewPagerAdapter(@NonNull FragmentManager fm, String role) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        if (role == null) role = "";

        switch (role.toUpperCase()) {
            case "ROLE_STAFF":
                fragmentList.add(new AdminHomeFragment());
                fragmentList.add(new AdminNotificationFragment());
                break;

            case "ROLE_ACCOUNTANT":
                fragmentList.add(new AdminBillFragment());
                fragmentList.add(new AdminNotificationFragment());
                break;

            case "ROLE_ADMIN":
            default:
                fragmentList.add(new AdminHomeFragment());
                fragmentList.add(new AdminServiceConfigFragment());
                fragmentList.add(new AdminBillFragment());
                fragmentList.add(new AdminNotificationFragment());
                break;
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}