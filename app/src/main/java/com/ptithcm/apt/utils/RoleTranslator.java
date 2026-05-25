package com.ptithcm.apt.utils;

public class RoleTranslator {
    public static String translateRole(String role) {
        if (role == null)
            return "---";
        switch (role.toUpperCase()) {
            case "ROLE_ADMIN":
                return "Quản trị viên";
            case "ROLE_ACCOUNTANT":
                return "Kế toán tòa nhà";
            case "ROLE_STAFF":
                return "Nhân viên vận hành";
            case "ROLE_USER":
                return "Cư dân";

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
