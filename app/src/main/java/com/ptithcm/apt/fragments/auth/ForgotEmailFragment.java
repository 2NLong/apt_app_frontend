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
 * Fragment xử lý yêu cầu quên mật khẩu thông qua Email.
 */
public class ForgotEmailFragment extends Fragment {

    private ForgotPasswordViewModel viewModel;

    public ForgotEmailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgotpassword_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ForgotPasswordViewModelFactory factory = new ForgotPasswordViewModelFactory(requireContext());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(ForgotPasswordViewModel.class);

        EditText etEmail = view.findViewById(R.id.et_email);
        Button btnSendResetOtp = view.findViewById(R.id.btn_send_reset_otp);
        TextView tvBackToLogin = view.findViewById(R.id.tv_back_to_login);

        // OBSERVE
        viewModel.forgotPasswordResult.observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                ToastUtils.showSuccessToast(requireContext(), "Mã OTP đã được gửi đến email của bạn!");
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_forgotEmailFragment_to_otpFragment);
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
                btnSendResetOtp.setEnabled(false);
                btnSendResetOtp.setText("Đang gửi...");
            } else {
                btnSendResetOtp.setEnabled(true);
                btnSendResetOtp.setText("Gửi mã OTP đặt lại");
            }
        });

        //
        btnSendResetOtp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            viewModel.forgotPassword(email);
        });

        tvBackToLogin.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.popBackStack();
        });
    }
}