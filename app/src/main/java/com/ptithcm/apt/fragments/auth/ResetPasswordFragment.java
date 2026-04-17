package com.ptithcm.apt.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ptithcm.apt.R;

/**
 * Fragment xử lý đặt lại mật khẩu mới.
 */
public class ResetPasswordFragment extends Fragment {

    public ResetPasswordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgotpassword_reset, container, false);
    }
}