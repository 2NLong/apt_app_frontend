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
import com.ptithcm.apt.utils.DialogUtils;
import com.ptithcm.apt.utils.ToastUtils;
import com.ptithcm.apt.viewmodel.auth.ForgotPasswordViewModel;
import com.ptithcm.apt.viewmodel.auth.ForgotPasswordViewModelFactory;

/**
 * Fragment xử lý đặt lại mật khẩu mới.
 */
public class ResetPasswordFragment extends Fragment {

    private ForgotPasswordViewModel viewModel;

    public ResetPasswordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgotpassword_reset, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ForgotPasswordViewModelFactory factory = new ForgotPasswordViewModelFactory(requireContext());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(ForgotPasswordViewModel.class);

        EditText etNewPassword = view.findViewById(R.id.et_new_password);
        EditText etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        Button btnResetPassword = view.findViewById(R.id.btn_reset_password);
        TextView tvBackToLogin = view.findViewById(R.id.tv_back_to_login);

        // OBSERVE
        viewModel.resetPasswordResult.observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                ToastUtils.showSuccessToast(requireContext(), "Đổi mật khẩu thành công! Vui lòng đăng nhập lại.");
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_resetPasswordFragment_to_loginFragment);
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
                DialogUtils.showLoadingDialog(requireContext(), "Đang đặt lại mật khẩu...");
            } else {
                DialogUtils.hideLoadingDialog();
            }
        });

        btnResetPassword.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (newPassword.isEmpty()) {
                ToastUtils.showErrorToast(requireContext(), "Vui lòng nhập mật khẩu mới");
                return;
            }
            if (newPassword.length() < 6) {
                ToastUtils.showErrorToast(requireContext(), "Mật khẩu phải có ít nhất 6 ký tự");
                return;
            }
            if (confirmPassword.isEmpty()) {
                ToastUtils.showErrorToast(requireContext(), "Vui lòng xác nhận mật khẩu mới");
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                ToastUtils.showErrorToast(requireContext(), "Mật khẩu xác nhận không khớp");
                return;
            }

            DialogUtils.showConfirmDialog(requireContext(),
                    "Đặt lại mật khẩu",
                    "Bạn có chắc chắn muốn đặt lại mật khẩu mới không?",
                    () -> viewModel.resetPassword(newPassword, confirmPassword)
            );
        });

        tvBackToLogin.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.popBackStack(R.id.loginFragment, false);
        });
    }
}