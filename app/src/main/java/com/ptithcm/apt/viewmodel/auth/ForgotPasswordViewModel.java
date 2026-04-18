package com.ptithcm.apt.viewmodel.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.apt.repositoris.AuthRepository;

public class ForgotPasswordViewModel extends ViewModel {

    private final AuthRepository authRepository;

    // --- Quên mật khẩu (gửi OTP) ---
    private final MutableLiveData<Boolean> _forgotPasswordResult = new MutableLiveData<>();
    public final LiveData<Boolean> forgotPasswordResult = _forgotPasswordResult;

    // --- Xác thực OTP — trả về resetToken ---
    private final MutableLiveData<String> _resetToken = new MutableLiveData<>();
    public final LiveData<String> resetToken = _resetToken;

    // --- Đặt lại mật khẩu ---
    private final MutableLiveData<Boolean> _resetPasswordResult = new MutableLiveData<>();
    public final LiveData<Boolean> resetPasswordResult = _resetPasswordResult;

    // --- Dùng chung ---
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public final LiveData<String> errorMessage = _errorMessage;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public final LiveData<Boolean> isLoading = _isLoading;

    // Email được nhập
    private String pendingEmail;

    public ForgotPasswordViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Gửi OTP đến email.
     */
    public void forgotPassword(String email) {
        if (email == null || email.trim().isEmpty()) {
            _errorMessage.setValue("Vui lòng nhập email");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            _errorMessage.setValue("Email không đúng định dạng");
            return;
        }
        pendingEmail = email.trim();
        authRepository.forgotPassword(pendingEmail, _forgotPasswordResult, _errorMessage, _isLoading);
    }

    /**
     * Xác thực OTP.
     */
    public void verifyOtp(String otp) {
        if (pendingEmail == null || pendingEmail.isEmpty()) {
            _errorMessage.setValue("Không tìm thấy email liên kết. Vui lòng thực hiện lại từ đầu.");
            return;
        }
        if (otp == null || otp.trim().isEmpty()) {
            _errorMessage.setValue("Vui lòng nhập mã OTP");
            return;
        }
        if (otp.trim().length() != 6) {
            _errorMessage.setValue("Mã OTP phải có đúng 6 chữ số");
            return;
        }
        authRepository.verifyOtp(pendingEmail, otp.trim(), _resetToken, _errorMessage, _isLoading);
    }

    /**
     * Đặt lại mật khẩu.
     */
    public void resetPassword(String newPassword, String confirmPassword) {
        String token = _resetToken.getValue();
        if (token == null || token.isEmpty()) {
            _errorMessage.setValue("Token không hợp lệ. Vui lòng thực hiện lại từ đầu.");
            return;
        }
        if (newPassword == null || newPassword.isEmpty()) {
            _errorMessage.setValue("Vui lòng nhập mật khẩu mới");
            return;
        }
        if (newPassword.length() < 6) {
            _errorMessage.setValue("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            _errorMessage.setValue("Mật khẩu xác nhận không khớp");
            return;
        }
        authRepository.resetPassword(token, newPassword, _resetPasswordResult, _errorMessage, _isLoading);
    }

    /**
     * Gửi lại OTP (dùng lại email đã lưu).
     */
    public void resendOtp() {
        if (pendingEmail == null || pendingEmail.isEmpty()) {
            _errorMessage.setValue("Không tìm thấy email. Vui lòng quay lại và nhập lại.");
            return;
        }
        authRepository.forgotPassword(pendingEmail, _forgotPasswordResult, _errorMessage, _isLoading);
    }

    public String getPendingEmail() {
        return pendingEmail;
    }

    public void clearError() {
        _errorMessage.setValue(null);
    }
}
