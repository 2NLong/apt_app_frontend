package com.ptithcm.apt.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.AdminViewPagerAdapter;
import com.ptithcm.apt.utils.DialogUtils;
import com.ptithcm.apt.utils.ToastUtils;
import com.ptithcm.apt.viewmodel.auth.LoginViewModel;
import com.ptithcm.apt.viewmodel.auth.LoginViewModelFactory;

public class AdminActivity extends AppCompatActivity {

    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    LoginViewModel loginViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        // Setup ViewModel
        LoginViewModelFactory factory = new LoginViewModelFactory(this);
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        // Observer đăng xuất
        loginViewModel.logoutResult.observe(this, isLoggedOut -> {
            if (Boolean.TRUE.equals(isLoggedOut)) {
                ToastUtils.showSuccessToast(this, "Đã đăng xuất thành công");
                Intent intent = new Intent(this, AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        viewPager = findViewById(R.id.admin_view_pager);

        AdminViewPagerAdapter adapter = new AdminViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        bottomNavigationView = findViewById(R.id.admin_menu_bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            getSupportFragmentManager().popBackStack(null,
                    androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

            int id = item.getItemId();

            if (id == R.id.admin_nav_home) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (id == R.id.admin_nav_metric) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (id == R.id.admin_nav_bill) {
                viewPager.setCurrentItem(2);
                return true;
            } else if (id == R.id.admin_nav_notification) {
                viewPager.setCurrentItem(3);
                return true;
            } else if (id == R.id.admin_nav_logout) {
                // Hiện confirm dialog
                DialogUtils.showConfirmDialog(
                        this,
                        "Đăng xuất",
                        "Bạn có chắc chắn muốn đăng xuất không?",
                        () -> loginViewModel.logout());
                return false;
            }

            return false;
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.admin_nav_home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.admin_nav_metric);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.admin_nav_bill);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.admin_nav_notification);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}