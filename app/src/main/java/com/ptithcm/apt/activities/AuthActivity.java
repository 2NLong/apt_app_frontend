package com.ptithcm.apt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.ptithcm.apt.R;
import com.ptithcm.apt.utils.SessionManager;
import com.ptithcm.apt.viewmodel.auth.LoginViewModel;
import com.ptithcm.apt.viewmodel.auth.LoginViewModelFactory;

public class AuthActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private ProgressBar progressBar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        progressBar = findViewById(R.id.progress_bar);
        sessionManager = SessionManager.getInstance(this);

        LoginViewModelFactory factory = new LoginViewModelFactory(this);
        viewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        observeViewModel();

        // Kiểm tra Auto-Login
        String refreshToken = sessionManager.getRefreshToken();
        if (refreshToken != null) {
            viewModel.checkSession(refreshToken);
        } else {
            setupNavigation();
        }
    }

    private void observeViewModel() {
        viewModel.isRefreshing.observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.refreshResult.observe(this, response -> {
            if (response != null) {
                navigateToMain();
            }
        });

        viewModel.refreshError.observe(this, error -> {
            if (error != null) {
                sessionManager.clearSession();
                setupNavigation();
            }
        });
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
        }
    }

    private void navigateToMain() {
        String role = sessionManager.getRole();
        Intent intent;

        if (role == null) {
            setupNavigation();
            return;
        }

        switch (role.toUpperCase()) {
            case "ROLE_ADMIN":
            case "ROLE_ACCOUNTANT":
            case "ROLE_STAFF":
                intent = new Intent(this, AdminActivity.class);
                break;
            case "ROLE_USER":
            default:
                intent = new Intent(this, MainActivity.class);
                break;
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}