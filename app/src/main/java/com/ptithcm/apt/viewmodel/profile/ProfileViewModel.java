package com.ptithcm.apt.viewmodel.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ptithcm.apt.models.profile.ProfileDashboardResponse;
import com.ptithcm.apt.repositoris.ProfileRepository;

public class ProfileViewModel extends ViewModel {

    private final ProfileRepository profileRepository;
    private final MutableLiveData<ProfileDashboardResponse> _profileData = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    public final LiveData<ProfileDashboardResponse> profileData = _profileData;
    public final LiveData<String> error = _error;
    public final LiveData<Boolean> isLoading = _isLoading;

    public ProfileViewModel(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public void fetchProfileDashboard() {
        profileRepository.getProfileDashboard(_profileData, _error, _isLoading);
    }
}
