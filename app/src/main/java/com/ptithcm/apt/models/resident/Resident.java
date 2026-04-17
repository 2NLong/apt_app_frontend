package com.ptithcm.apt.models.resident;

public class Resident {
    private String fullName;
    private String dob;
    private String phone;
    private String citizenIdentity;
    private String email;


    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCitizenIdentity() { return citizenIdentity; }
    public void setCitizenIdentity(String citizenIdentity) { this.citizenIdentity = citizenIdentity; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}