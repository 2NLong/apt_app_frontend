package com.ptithcm.apt.models.profile;

import java.io.Serializable;

public class FamilyMemberResponse implements Serializable {
    private Long residentId;
    private String fullName;
    private String phone;
    private String dob;
    private String role;
    private Boolean isHead;

    public Long getResidentId() { return residentId; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getDob() { return dob; }
    public String getRole() { return role; }
    public Boolean getIsHead() { return isHead; }
}
