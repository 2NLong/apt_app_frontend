package com.ptithcm.apt.network.interceptor;

import androidx.annotation.NonNull;

import com.ptithcm.apt.utils.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class AuthInterceptor implements Interceptor {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private final SessionManager sessionManager;

    public AuthInterceptor(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        String path = original.url().encodedPath();

        // Bỏ qua login và refresh — không cần token
        if (path.contains("/auth/login") || path.contains("/auth/refresh-token")) {
            return chain.proceed(original);
        }

        String bearerToken = sessionManager.getBearerToken();
        if (bearerToken == null) {
            return chain.proceed(original);
        }

        Request authenticatedRequest = original.newBuilder()
                .header(HEADER_AUTHORIZATION, bearerToken)
                .build();

        return chain.proceed(authenticatedRequest);
    }
}
