package com.ptithcm.apt.utils;

public class RoleTranslator {
    public static String translateRole(String role) {
        if (role == null)
            return "---";
        switch (role.toUpperCase()) {
            case "OWNER":
                return "Chủ sở hữu";
            case "TENANT":
                return "Đang thuê";
            case "MEMBER":
                return "Thành viên";
            default:
                return role;
        }
    }
}
