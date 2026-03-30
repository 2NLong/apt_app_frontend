package com.ptithcm.apt.network.retrofit;

import android.content.Context;

import com.ptithcm.apt.network.interceptor.AuthInterceptor;
import com.ptithcm.apt.network.interceptor.TokenAuthenticator;
import com.ptithcm.apt.utils.SessionManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/";

    private static RetrofitClient instance;

    private final Retrofit authenticatedRetrofit;
    private final Retrofit publicRetrofit;

    private RetrofitClient(Context context) {
        SessionManager sessionManager = SessionManager.getInstance(context);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Client cho các API công khai (Login, Refresh Token)
        OkHttpClient publicClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        publicRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(publicClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Client cho các API cần xác thực
        OkHttpClient authenticatedClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new AuthInterceptor(sessionManager)) // gắn Bearer token
                .authenticator(new TokenAuthenticator(context, sessionManager)) // refresh khi 401
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        authenticatedRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(authenticatedClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context.getApplicationContext());
        }
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    "RetrofitClient chưa được khởi tạo. Gọi RetrofitClient.init(context) trước.");
        }
        return instance;
    }

    /**
     * Tạo service cho các API CẦN xác thực (gửi kèm token).
     */
    public <T> T createService(Class<T> serviceClass) {
        return authenticatedRetrofit.create(serviceClass);
    }

    /**
     * Tạo service cho các API CÔNG KHAI (không gửi kèm token).
     */
    public <T> T createPublicService(Class<T> serviceClass) {
        return publicRetrofit.create(serviceClass);
    }
}
