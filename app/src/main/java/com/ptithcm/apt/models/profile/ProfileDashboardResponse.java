package com.ptithcm.apt.models.profile;

import java.util.List;

public class ProfileDashboardResponse {
    private ProfileInfoResponse personalInfo;
    private ProfileApartmentResponse livingApartment;
    private List<ProfileApartmentResponse> ownedApartments;
    private List<FamilyMemberResponse> familyMembers;

    public ProfileInfoResponse getPersonalInfo() { return personalInfo; }
    public ProfileApartmentResponse getLivingApartment() { return livingApartment; }
    public List<ProfileApartmentResponse> getOwnedApartments() { return ownedApartments; }
    public List<FamilyMemberResponse> getFamilyMembers() { return familyMembers; }
}
