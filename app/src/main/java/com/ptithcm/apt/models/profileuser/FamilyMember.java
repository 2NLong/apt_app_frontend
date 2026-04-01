package com.ptithcm.apt.models.profileuser;

import java.io.Serializable;

public class FamilyMember implements Serializable {
    private Integer id;
    private Integer userId;
    private String fullName;
    private String citizenIdentity;
    private String phone;
    private String email;
    private String dob;

    private String role; // 'OWNER', 'TENANT', 'MEMBER'
    private Boolean isHead;
    private String contractStart;
    private String contractEnd;

    private String relation; // Quản lý xưng hô (Vợ, con...) cho hiển thị
    private int iconResId;

    public FamilyMember() {
    }

    // Constructor cũ
    public FamilyMember(String fullName, String relation, int iconResId) {
        this.fullName = fullName;
        this.relation = relation;
        this.iconResId = iconResId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return fullName;
    }

    public String getCitizenIdentity() {
        return citizenIdentity;
    }

    public void setCitizenIdentity(String citizenIdentity) {
        this.citizenIdentity = citizenIdentity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsHead() {
        return isHead;
    }

    public void setIsHead(Boolean head) {
        isHead = head;
    }

    public String getContractStart() {
        return contractStart;
    }

    public void setContractStart(String contractStart) {
        this.contractStart = contractStart;
    }

    public String getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(String contractEnd) {
        this.contractEnd = contractEnd;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
