package com.ptithcm.apt.fragments.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.utils.ToastUtils;
import com.ptithcm.apt.viewmodel.auth.ForgotPasswordViewModel;
import com.ptithcm.apt.viewmodel.auth.ForgotPasswordViewModelFactory;

/**
 * Fragment xử lý xác thực mã OTP.
 */
public class OtpFragment extends Fragment {

    private ForgotPasswordViewModel viewModel;

    public OtpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgotpassword_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ForgotPasswordViewModelFactory factory = new ForgotPasswordViewModelFactory(requireContext());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(ForgotPasswordViewModel.class);

        TextView tvEmailDisplay = view.findViewById(R.id.tv_email_display);
        EditText etOtp = view.findViewById(R.id.et_otp);
        Button btnVerifyOtp = view.findViewById(R.id.btn_verify_otp);
        TextView tvResendOtp = view.findViewById(R.id.tv_resend_otp);
        TextView tvBackToLogin = view.findViewById(R.id.tv_back_to_login);

        // Hiển thị email đã nhập
        String email = viewModel.getPendingEmail();
        if (email != null && !email.isEmpty()) {
            tvEmailDisplay.setText("Một mã gồm 6 chữ số đã được gửi tới: " + email);
        }

        // OBSERVE
        viewModel.resetToken.observe(getViewLifecycleOwner(), token -> {
            if (token != null && !token.isEmpty()) {
                ToastUtils.showSuccessToast(requireContext(), "Xác thực OTP thành công!");
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_otpFragment_to_resetPasswordFragment);
            }
        });

        viewModel.errorMessage.observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                ToastUtils.showErrorToast(requireContext(), error);
                viewModel.clearError();
            }
        });

        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                btnVerifyOtp.setEnabled(false);
                btnVerifyOtp.setText("Đang xác thực...");
            } else {
                btnVerifyOtp.setEnabled(true);
                btnVerifyOtp.setText("Xác minh OTP");
            }
        });

        btnVerifyOtp.setOnClickListener(v -> {
            String otp = etOtp.getText().toString().trim();
            viewModel.verifyOtp(otp);
        });

        tvResendOtp.setOnClickListener(v -> viewModel.resendOtp());

        tvBackToLogin.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.popBackStack(R.id.loginFragment, false);
        });
    }
}