package com.ptithcm.apt.fragments.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ptithcm.apt.R;

/**
 * Fragment xử lý yêu cầu quên mật khẩu thông qua Email.
 */
public class ForgotEmailFragment extends Fragment {

    public ForgotEmailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgotpassword_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnSendResetOtp = view.findViewById(R.id.btn_send_reset_otp);
        TextView tvBackToLogin = view.findViewById(R.id.tv_back_to_login);

        btnSendResetOtp.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_forgotEmailFragment_to_otpFragment);
        });

        tvBackToLogin.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.popBackStack();
        });
    }
}