package com.ptithcm.apt.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.AdminViewPagerAdapter;
import com.ptithcm.apt.utils.DialogUtils;
import com.ptithcm.apt.utils.SessionManager;
import com.ptithcm.apt.utils.ToastUtils;
import com.ptithcm.apt.viewmodel.auth.LoginViewModel;
import com.ptithcm.apt.viewmodel.auth.LoginViewModelFactory;

public class AdminActivity extends AppCompatActivity {

    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    LoginViewModel loginViewModel;
    SessionManager sessionManager;
    String currentRole;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        sessionManager = SessionManager.getInstance(this);
        currentRole = sessionManager.getRole();
        if (currentRole == null) currentRole = "ROLE_ADMIN"; // Cấu hình fallback an toàn

        LoginViewModelFactory factory = new LoginViewModelFactory(this);
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

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
        bottomNavigationView = findViewById(R.id.admin_menu_bottom_nav);

        // Bước 1: Khởi tạo ẩn/hiện cấu trúc phần tử menu tương ứng với từng quyền hạn chuyên môn
        setupBottomNavMenuVisibility();

        // Bước 2: Nạp cấu trúc danh sách Fragment động cho ViewPager tương thích theo Role
        AdminViewPagerAdapter adapter = new AdminViewPagerAdapter(getSupportFragmentManager(), currentRole);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        // Thiết lập tiêu điểm kích hoạt mặc định dựa trên màn hình chính của từng chức danh khi vào hệ thống
        if ("ROLE_ACCOUNTANT".equals(currentRole.toUpperCase())) {
            // Kế toán mặc định vào màn hình Hóa đơn (Bill)
            bottomNavigationView.setSelectedItemId(R.id.admin_nav_bill);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.admin_nav_home);
        }

        // Bước 3: Đồng bộ điều hướng khi người dùng tương tác click vào Menu (Bottom Nav -> ViewPager)
        bottomNavigationView.setOnItemSelectedListener(item -> {
            getSupportFragmentManager().popBackStack(null,
                    androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

            int id = item.getItemId();

            if (id == R.id.admin_nav_logout) {
                DialogUtils.showConfirmDialog(
                        this,
                        "Đăng xuất",
                        "Bạn có chắc chắn muốn đăng xuất không?",
                        () -> loginViewModel.logout());
                return false;
            }

            // Tính toán vị trí trang động tương thích theo Role để chuyển dịch ViewPager chính xác
            int targetPageIndex = getPageIndexByRole(id, currentRole);
            if (targetPageIndex != -1) {
                viewPager.setCurrentItem(targetPageIndex);
                return true;
            }

            return false;
        });

        // Bước 4: Đồng bộ trạng thái Menu khi người dùng thực hiện thao tác vuốt ngang (ViewPager -> Bottom Nav)
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                syncBottomNavSelectedByRole(position, currentRole);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setupBottomNavMenuVisibility() {
        switch (currentRole.toUpperCase()) {
            case "ROLE_STAFF":
                bottomNavigationView.getMenu().findItem(R.id.admin_nav_metric).setVisible(false);
                bottomNavigationView.getMenu().findItem(R.id.admin_nav_bill).setVisible(false);
                break;
            case "ROLE_ACCOUNTANT":
                bottomNavigationView.getMenu().findItem(R.id.admin_nav_home).setVisible(false);
                // Ẩn thêm menu Chỉ số đối với Kế toán
                bottomNavigationView.getMenu().findItem(R.id.admin_nav_metric).setVisible(false);
                break;
            case "ROLE_ADMIN":
            default:
                // Mặc định hiển thị đầy đủ thanh Menu chức năng đối với Quản trị viên
                break;
        }
    }

    private int getPageIndexByRole(int itemId, String role) {
        String r = role.toUpperCase();
        if ("ROLE_STAFF".equals(r)) {
            if (itemId == R.id.admin_nav_home) return 0;
            if (itemId == R.id.admin_nav_notification) return 1;
        }
        else if ("ROLE_ACCOUNTANT".equals(r)) {
            // Cập nhật lại chỉ mục cho Kế toán (Chỉ còn 2 trang)
            if (itemId == R.id.admin_nav_bill) return 0;
            if (itemId == R.id.admin_nav_notification) return 1;
        }
        else { // Trường hợp quyền hệ thống ROLE_ADMIN
            if (itemId == R.id.admin_nav_home) return 0;
            if (itemId == R.id.admin_nav_metric) return 1;
            if (itemId == R.id.admin_nav_bill) return 2;
            if (itemId == R.id.admin_nav_notification) return 3;
        }
        return -1;
    }

    private void syncBottomNavSelectedByRole(int position, String role) {
        String r = role.toUpperCase();
        if ("ROLE_STAFF".equals(r)) {
            switch (position) {
                case 0: bottomNavigationView.getMenu().findItem(R.id.admin_nav_home).setChecked(true); break;
                case 1: bottomNavigationView.getMenu().findItem(R.id.admin_nav_notification).setChecked(true); break;
            }
        }
        else if ("ROLE_ACCOUNTANT".equals(r)) {
            switch (position) {
                case 0: bottomNavigationView.getMenu().findItem(R.id.admin_nav_bill).setChecked(true); break;
                case 1: bottomNavigationView.getMenu().findItem(R.id.admin_nav_notification).setChecked(true); break;
            }
        }
        else { // Trường hợp quyền hệ thống ROLE_ADMIN
            switch (position) {
                case 0: bottomNavigationView.getMenu().findItem(R.id.admin_nav_home).setChecked(true); break;
                case 1: bottomNavigationView.getMenu().findItem(R.id.admin_nav_metric).setChecked(true); break;
                case 2: bottomNavigationView.getMenu().findItem(R.id.admin_nav_bill).setChecked(true); break;
                case 3: bottomNavigationView.getMenu().findItem(R.id.admin_nav_notification).setChecked(true); break;
            }
        }
    }
}