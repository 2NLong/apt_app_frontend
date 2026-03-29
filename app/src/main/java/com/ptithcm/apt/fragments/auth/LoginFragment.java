package com.ptithcm.apt.fragments.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ptithcm.apt.R;
import com.ptithcm.apt.activities.MainActivity;
import com.ptithcm.apt.databinding.FragmentLoginBinding;
import com.ptithcm.apt.utils.SessionManager;
import com.ptithcm.apt.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;
    private SessionManager sessionManager;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        sessionManager = new SessionManager(requireContext());

        // Observe các LiveData từ ViewModel
        observeViewModel();

        binding.btnSignIn.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            loginViewModel.login(username, password);
        });

        binding.tvForgotPassword.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_loginFragment_to_forgotEmailFragment);
        });
    }

    private void observeViewModel() {
        // Kết quả đăng nhập thành công
        loginViewModel.loginResult.observe(getViewLifecycleOwner(), loginResponse -> {
            if (loginResponse != null) {
                // Lưu token và thông tin user vào SharedPreferences
                sessionManager.saveSession(
                        loginResponse.getAccessToken(),
                        loginResponse.getRefreshToken(),
                        loginResponse.getUser() != null ? loginResponse.getUser().getId() : null,
                        loginResponse.getUser() != null ? loginResponse.getUser().getUsername() : null,
                        loginResponse.getUser() != null ? loginResponse.getUser().getRole() : null
                );

                // Chuyển sang MainActivity
                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        // Thông báo lỗi
        loginViewModel.errorMessage.observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
                loginViewModel.clearError();
            }
        });

        // Trạng thái loading
        loginViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                binding.btnSignIn.setEnabled(false);
                binding.btnSignIn.setText("Đang đăng nhập...");
            } else {
                binding.btnSignIn.setEnabled(true);
                binding.btnSignIn.setText("Đăng nhập");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Tránh memory leak
    }
}