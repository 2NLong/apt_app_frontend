package com.ptithcm.apt.network.interceptor;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ptithcm.apt.activities.AuthActivity;
import com.ptithcm.apt.models.auth.request.RefreshTokenRequest;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.auth.response.LoginResponse;
import com.ptithcm.apt.network.api.AuthApiService;
import com.ptithcm.apt.utils.SessionManager;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TokenAuthenticator implements Authenticator {

    private static final String BASE_URL = "http://10.0.2.2:8080/";

    private final SessionManager sessionManager;
    private final Context context;

    public TokenAuthenticator(Context context, SessionManager sessionManager) {
        this.context = context.getApplicationContext();
        this.sessionManager = sessionManager;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NonNull Response response) throws IOException {
        // Tránh vòng lặp vô hạn: nếu refresh cũng 401 thì dừng
        if (responseCount(response) >= 2) {
            onSessionExpired();
            return null;
        }

        String refreshToken = sessionManager.getRefreshToken();
        if (refreshToken == null) {
            onSessionExpired();
            return null;
        }

        // Gọi refresh API đồng bộ (KHÔNG dùng enqueue — đang ở background thread)
        AuthApiService authService = buildPublicRetrofit().create(AuthApiService.class);
        try {
            retrofit2.Response<ApiResponse<LoginResponse>> refreshResponse =
                    authService.refreshToken(new RefreshTokenRequest(refreshToken)).execute();

            if (refreshResponse.isSuccessful()
                    && refreshResponse.body() != null
                    && refreshResponse.body().getStatus() == 200) {

                LoginResponse newTokens = refreshResponse.body().getData();
                sessionManager.updateTokens(
                        newTokens.getAccessToken(),
                        newTokens.getRefreshToken()
                );

                // Retry request gốc với token mới
                return response.request().newBuilder()
                        .header("Authorization", "Bearer " + newTokens.getAccessToken())
                        .build();
            }
        } catch (Exception e) {
            // Network error hoặc parse error khi refresh
        }

        onSessionExpired();
        return null;
    }

    /**
     * Xoá session và đưa user về màn login.
     */
    private void onSessionExpired() {
        sessionManager.clearSession();
        Intent intent = new Intent(context, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    /**
     * Đếm số lần đã retry (tránh vòng lặp vô hạn).
     */
    private int responseCount(Response response) {
        int count = 1;
        while ((response = response.priorResponse()) != null) {
            count++;
        }
        return count;
    }

    /**
     * Retrofit riêng KHÔNG có AuthInterceptor để tránh đệ quy.
     */
    private Retrofit buildPublicRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
