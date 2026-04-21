package com.ptithcm.apt.models.resident;

public class UpdateResidentRequest {
    private String fullName;
    private String dob; // Vẫn phải truyền lên, dù giao diện không cho sửa để tránh bị null
    private String phone;
    private String citizenIdentity;
    private String email;

    public UpdateResidentRequest(String fullName, String dob, String phone, String citizenIdentity, String email) {
        this.fullName = fullName;
        this.dob = dob;
        this.phone = phone;
        this.citizenIdentity = citizenIdentity;
        this.email = email;
    }

    // Bạn có thể generate Getters và Setters ở đây nếu cẩn thận
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
