package com.ptithcm.apt.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        bottomNavigationView = findViewById(R.id.menu_bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.nav_bills:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.nav_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.nav_profile:
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
                        bottomNavigationView.setSelectedItemId(R.id.nav_home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.nav_bills);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.nav_notifications);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}