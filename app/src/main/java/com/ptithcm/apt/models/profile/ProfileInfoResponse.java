package com.ptithcm.apt.models.profile;

public class ProfileInfoResponse {
    private Long residentId;
    private String fullName;
    private String citizenIdentity;
    private String phone;
    private String email;
    private String dob;

    public Long getResidentId() { return residentId; }
    public String getFullName() { return fullName; }
    public String getCitizenIdentity() { return citizenIdentity; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getDob() { return dob; }
}
