package com.ptithcm.apt.models.resident;

public class MemberRequest {
    private String fullName;
    private String dob;
    private String phone;
    private String citizenIdentity;
    private String email;
    public MemberRequest(String fullName, String dob, String phone, String citizenIdentity, String email) {
        this.fullName = fullName;
        this.dob = dob;
        this.phone = phone;
        this.citizenIdentity = citizenIdentity;
        this.email = email;
    }
}