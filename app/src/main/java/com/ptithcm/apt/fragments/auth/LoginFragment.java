package com.ptithcm.apt.fragments.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ptithcm.apt.R;
import com.ptithcm.apt.utils.ToastUtils;
import com.ptithcm.apt.activities.AdminActivity;
import com.ptithcm.apt.activities.MainActivity;
import com.ptithcm.apt.viewmodel.auth.LoginViewModel;
import com.ptithcm.apt.viewmodel.auth.LoginViewModelFactory;

public class LoginFragment extends Fragment {

    private Button btnSignIn;
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvForgotPassword;
    private LoginViewModel loginViewModel;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSignIn = view.findViewById(R.id.btn_sign_in);
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        tvForgotPassword = view.findViewById(R.id.tv_forgot_password);

        // Dùng Factory để inject Context
        LoginViewModelFactory factory = new LoginViewModelFactory(requireContext());
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        observeViewModel();

        btnSignIn.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            loginViewModel.login(username, password);
        });

        tvForgotPassword.setOnClickListener(v -> {
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

                ToastUtils.showSuccessToast(requireContext(), "Đăng nhập thành công!");

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        // Thông báo lỗi
        loginViewModel.errorMessage.observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                ToastUtils.showErrorToast(requireContext(), error);
                loginViewModel.clearError();
            }
        });

        // Trạng thái loading
        loginViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                btnSignIn.setEnabled(false);
                btnSignIn.setText("Đang đăng nhập...");
            } else {
                btnSignIn.setEnabled(true);
                btnSignIn.setText("Đăng nhập");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}