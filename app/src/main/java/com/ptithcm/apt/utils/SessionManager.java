package com.ptithcm.apt.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SessionManager {

    private static final String PREF_NAME = "apt_secure_session";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_RESIDENT_NAME = "resident_name";

    private static volatile SessionManager instance;
    private final SharedPreferences prefs;

    private SessionManager(Context context) {
        Context appContext = context.getApplicationContext();
        SharedPreferences encryptedPrefs;
        try {
            MasterKey masterKey = new MasterKey.Builder(appContext)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            encryptedPrefs = EncryptedSharedPreferences.create(
                    appContext,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException | IOException e) {
            // Fallback sang SharedPreferences thường nếu thiết bị không hỗ trợ (hoặc lỗi
            // cấu hình)
            encryptedPrefs = appContext.getSharedPreferences(PREF_NAME + "_fallback", Context.MODE_PRIVATE);
        }
        prefs = encryptedPrefs;
    }

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * Lưu toàn bộ session sau khi đăng nhập thành công.
     */
    public void saveSession(String accessToken, String refreshToken,
            Long userId, String username, String role, String residentName) {
        prefs.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .putString(KEY_USERNAME, username)
                .putString(KEY_ROLE, role)
                .putLong(KEY_USER_ID, userId != null ? userId : -1L)
                .putString(KEY_RESIDENT_NAME, residentName)
                .apply();
    }

    /**
     * Chỉ cập nhật access + refresh token (dùng khi refresh token thành công).
     */
    public void updateTokens(String accessToken, String refreshToken) {
        prefs.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .apply();
    }

    /**
     * Xoá toàn bộ session khi đăng xuất hoặc refresh token thất bại.
     */
    public void clearSession() {
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return getRefreshToken() != null;
    }

    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return prefs.getString(KEY_REFRESH_TOKEN, null);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    public String getRole() {
        return prefs.getString(KEY_ROLE, null);
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, -1L);
    }

    public String getResidentName() {
        return prefs.getString(KEY_RESIDENT_NAME, null);
    }

    /**
     * Trả về header Authorization để đính kèm vào request.
     */
    public String getBearerToken() {
        String token = getAccessToken();
        return token != null ? "Bearer " + token : null;
    }
}
