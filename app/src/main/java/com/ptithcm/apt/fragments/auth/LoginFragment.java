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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.app.Activity;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.ptithcm.apt.BuildConfig;
import com.ptithcm.apt.R;
import com.ptithcm.apt.utils.DialogUtils;
import com.ptithcm.apt.utils.ToastUtils;
import com.ptithcm.apt.activities.AdminActivity;
import com.ptithcm.apt.activities.MainActivity;
import com.ptithcm.apt.viewmodel.auth.LoginViewModel;
import com.ptithcm.apt.viewmodel.auth.LoginViewModelFactory;

public class LoginFragment extends Fragment {

    private Button btnSignIn;
    private Button btnGoogleLogin;
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvForgotPassword;
    private LoginViewModel loginViewModel;
    private GoogleSignInClient mGoogleSignInClient;

    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    String idToken = account.getIdToken();
                    if (idToken != null) {
                        loginViewModel.googleLogin(idToken);
                    } else {
                        ToastUtils.showErrorToast(requireContext(), "Lỗi: Không nhận được ID Token từ Google");
                    }
                } catch (ApiException e) {
                    Log.w("GoogleSignIn", "Google sign in failed. Status code: " + e.getStatusCode(), e);
                    ToastUtils.showErrorToast(requireContext(),
                            "Đăng nhập Google thất bại! Mã lỗi: " + e.getStatusCode());
                }
            });

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
        btnGoogleLogin = view.findViewById(R.id.btn_google_login);
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        tvForgotPassword = view.findViewById(R.id.tv_forgot_password);

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        // Dùng Factory để inject Context
        LoginViewModelFactory factory = new LoginViewModelFactory(requireContext());
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        observeViewModel();

        btnSignIn.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            loginViewModel.login(username, password);
        });

        btnGoogleLogin.setOnClickListener(v -> {
            mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity(), task -> {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                googleSignInLauncher.launch(signInIntent);
            });
        });

        tvForgotPassword.setOnClickListener(v -> {
            com.ptithcm.apt.viewmodel.auth.ForgotPasswordViewModelFactory forgotFactory = new com.ptithcm.apt.viewmodel.auth.ForgotPasswordViewModelFactory(
                    requireContext());
            com.ptithcm.apt.viewmodel.auth.ForgotPasswordViewModel forgotViewModel = new ViewModelProvider(
                    requireActivity(), forgotFactory).get(com.ptithcm.apt.viewmodel.auth.ForgotPasswordViewModel.class);
            forgotViewModel.clearAll();

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
                DialogUtils.showLoadingDialog(requireContext(), "Đang đăng nhập...");
            } else {
                DialogUtils.hideLoadingDialog();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}