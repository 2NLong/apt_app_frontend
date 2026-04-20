package com.ptithcm.apt.viewmodel.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.apt.models.auth.request.ChangePasswordRequest;
import com.ptithcm.apt.models.profile.ProfileDashboardResponse;
import com.ptithcm.apt.repositoris.AuthRepository;
import com.ptithcm.apt.repositoris.ProfileRepository;

public class ProfileViewModel extends ViewModel {

    private final ProfileRepository profileRepository;
    private final AuthRepository authRepository;

    private final MutableLiveData<ProfileDashboardResponse> _profileData = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _changePasswordSuccess = new MutableLiveData<>();

    private final MutableLiveData<String> _oldPasswordError = new MutableLiveData<>();
    private final MutableLiveData<String> _newPasswordError = new MutableLiveData<>();
    private final MutableLiveData<String> _confirmPasswordError = new MutableLiveData<>();

    public LiveData<ProfileDashboardResponse> profileData = _profileData;
    public LiveData<String> error = _error;
    public LiveData<Boolean> isLoading = _isLoading;
    public LiveData<Boolean> changePasswordSuccess = _changePasswordSuccess;

    public LiveData<String> oldPasswordError = _oldPasswordError;
    public LiveData<String> newPasswordError = _newPasswordError;
    public LiveData<String> confirmPasswordError = _confirmPasswordError;

    public ProfileViewModel(ProfileRepository profileRepository, AuthRepository authRepository) {
        this.profileRepository = profileRepository;
        this.authRepository = authRepository;
    }

    public void fetchProfileDashboard() {
        profileRepository.getProfileDashboard(_profileData, _error, _isLoading);
    }

    public void validateAndChangePassword(String oldPass, String newPass, String confirmPass) {
        boolean isValid = true;

        if (oldPass == null || oldPass.trim().isEmpty()) {
            _oldPasswordError.setValue("Mật khẩu cũ không được để trống");
            isValid = false;
        } else {
            _oldPasswordError.setValue(null);
        }

        if (newPass == null || newPass.trim().isEmpty()) {
            _newPasswordError.setValue("Mật khẩu mới không được để trống");
            isValid = false;
        } else if (newPass.length() < 6) {
            _newPasswordError.setValue("Mật khẩu phải có ít nhất 6 ký tự");
            isValid = false;
        } else {
            _newPasswordError.setValue(null);
        }

        if (confirmPass == null || confirmPass.trim().isEmpty()) {
            _confirmPasswordError.setValue("Vui lòng xác nhận mật khẩu mới");
            isValid = false;
        } else if (!confirmPass.equals(newPass)) {
            _confirmPasswordError.setValue("Mật khẩu xác nhận không khớp");
            isValid = false;
        } else {
            _confirmPasswordError.setValue(null);
        }

        if (isValid) {
            changePassword(oldPass, newPass);
        }
    }

    public void changePassword(String oldPassword, String newPassword) {
        ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);
        authRepository.changePassword(request, _changePasswordSuccess, _error, _isLoading);
    }

    public void resetChangePasswordStatus() {
        _changePasswordSuccess.setValue(null);
        resetErrors();
    }

    public void resetErrors() {
        _oldPasswordError.setValue(null);
        _newPasswordError.setValue(null);
        _confirmPasswordError.setValue(null);
    }
}