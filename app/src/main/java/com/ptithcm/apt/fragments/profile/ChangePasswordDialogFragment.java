package com.ptithcm.apt.fragments.profile;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.apt.R;
import com.ptithcm.apt.utils.DialogUtils;
import com.ptithcm.apt.viewmodel.profile.ProfileViewModel;
import com.ptithcm.apt.viewmodel.profile.ProfileViewModelFactory;

public class ChangePasswordDialogFragment extends DialogFragment {

    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnCancel, btnConfirm;

    private ProfileViewModel profileViewModel;

    public static ChangePasswordDialogFragment newInstance() {
        return new ChangePasswordDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProfileViewModelFactory factory = new ProfileViewModelFactory(requireContext());
        profileViewModel = new ViewModelProvider(requireParentFragment(), factory).get(ProfileViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_change_password, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        bindViews(view);
        setupObservers();
        setupListeners();

        return view;
    }

    private void bindViews(View view) {
        etOldPassword = view.findViewById(R.id.et_old_password);
        etNewPassword = view.findViewById(R.id.et_new_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);

        btnCancel = view.findViewById(R.id.btn_cancel);
        btnConfirm = view.findViewById(R.id.btn_confirm);
    }

    private void setupObservers() {
        profileViewModel.oldPasswordError.observe(getViewLifecycleOwner(), error -> {
            etOldPassword.setError(error);
            if (error != null)
                etOldPassword.requestFocus();
        });

        profileViewModel.newPasswordError.observe(getViewLifecycleOwner(), error -> {
            etNewPassword.setError(error);
            if (error != null)
                etNewPassword.requestFocus();
        });

        profileViewModel.confirmPasswordError.observe(getViewLifecycleOwner(), error -> {
            etConfirmPassword.setError(error);
            if (error != null)
                etConfirmPassword.requestFocus();
        });

        profileViewModel.changePasswordSuccess.observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                dismiss();
            }
        });

        profileViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (Boolean.TRUE.equals(isLoading)) {
                DialogUtils.showLoadingDialog(requireContext(), "Đang đổi mật khẩu...");
            } else {
                DialogUtils.hideLoadingDialog();
            }
        });
    }

    private void setupListeners() {
        btnCancel.setOnClickListener(v -> {
            profileViewModel.resetErrors();
            dismiss();
        });

        btnConfirm.setOnClickListener(v -> {
            String oldPass = etOldPassword.getText().toString().trim();
            String newPass = etNewPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            boolean isValid = profileViewModel.validateOnly(oldPass, newPass, confirmPass);

            if (isValid) {
                DialogUtils.showConfirmDialog(
                        requireContext(),
                        "Đổi mật khẩu",
                        "Bạn có chắc chắn muốn đổi mật khẩu không?",
                        () -> profileViewModel.changePassword(oldPass, newPass));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
