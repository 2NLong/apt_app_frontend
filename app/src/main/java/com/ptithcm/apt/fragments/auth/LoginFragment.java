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
import com.ptithcm.apt.activities.AdminActivity;
import com.ptithcm.apt.activities.MainActivity;
import com.ptithcm.apt.databinding.FragmentLoginBinding;
import com.ptithcm.apt.viewmodel.auth.LoginViewModel;
import com.ptithcm.apt.viewmodel.auth.LoginViewModelFactory;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;

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

        // Dùng Factory để inject Context
        LoginViewModelFactory factory = new LoginViewModelFactory(requireContext());
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

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
        // Kết quả đăng nhập thành công — session đã được lưu trong Repository
        loginViewModel.loginResult.observe(getViewLifecycleOwner(), loginResponse -> {
            if (loginResponse != null && loginResponse.getUser() != null) {
                String role = loginResponse.getUser().getRole();
                Intent intent;

                if ("ROLE_USER".equals(role)) {
                    intent = new Intent(requireContext(), MainActivity.class);
                } else {
                    intent = new Intent(requireContext(), AdminActivity.class);
                }

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
        binding = null;
    }
}