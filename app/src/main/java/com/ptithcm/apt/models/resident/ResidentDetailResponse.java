package com.ptithcm.apt.models.resident;

public class ResidentDetailResponse {
    private Long id;
    private String fullName;
    private String dob;
    private String phone;
    private String citizenIdentity;
    private String email;

    // Thêm các trường này để hiển thị UI và phục vụ nút Chuyển đi
    private Long apartmentId;
    private String roomNumber;
    private String role;
    private Boolean isHead;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Long getApartmentId() { return apartmentId; }
    public void setApartmentId(Long apartmentId) { this.apartmentId = apartmentId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getIsHead() { return isHead; }
    public void setIsHead(Boolean isHead) { this.isHead = isHead; }
}