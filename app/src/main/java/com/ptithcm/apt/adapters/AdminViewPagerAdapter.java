package com.ptithcm.apt.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ptithcm.apt.fragments.admin.AdminBillFragment;
import com.ptithcm.apt.fragments.admin.AdminHomeFragment;
import com.ptithcm.apt.fragments.admin.AdminServiceConfigFragment;
import com.ptithcm.apt.fragments.admin.AdminNotificationFragment;

public class AdminViewPagerAdapter extends FragmentStatePagerAdapter {
    public AdminViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public AdminViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AdminHomeFragment();
            case 1:
                return new AdminServiceConfigFragment();
            case 2:
                return new AdminBillFragment();
            case 3:
                return new AdminNotificationFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
