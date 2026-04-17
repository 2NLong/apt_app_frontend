package com.ptithcm.apt.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
}