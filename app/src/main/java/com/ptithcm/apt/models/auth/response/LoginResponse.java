package com.ptithcm.apt.models.auth.response;


public class LoginResponse {

    private String accessToken;

    private String refreshToken;

    private UserInfo user;

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public UserInfo getUser() { return user; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public void setUser(UserInfo user) { this.user = user; }

    public static class UserInfo {
        private Long id;

        private String username;

        private String role;

        private String residentName;

        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getRole() { return role; }
        public String getResidentName() { return residentName; }

        public void setId(Long id) { this.id = id; }
        public void setUsername(String username) { this.username = username; }
        public void setRole(String role) { this.role = role; }
        public void setResidentName(String residentName) { this.residentName = residentName; }
    }
}
