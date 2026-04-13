package com.ptithcm.apt.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.AdminViewPagerAdapter;
import com.ptithcm.apt.adapters.ViewPagerAdapter;

public class AdminActivity extends AppCompatActivity {

    ViewPager viewPager;

    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        viewPager = findViewById(R.id.admin_view_pager);

        AdminViewPagerAdapter adapter = new AdminViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        bottomNavigationView = findViewById(R.id.admin_menu_bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            switch (item.getItemId()) {
                case R.id.admin_nav_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.admin_nav_metric:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.admin_nav_bill:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.admin_nav_notification:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
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
