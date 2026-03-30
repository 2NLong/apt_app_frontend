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

    /** Retrofit có auth (dùng cho mọi API cần token). */
    private final Retrofit retrofit;

    private RetrofitClient(Context context) {
        SessionManager sessionManager = new SessionManager(context);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new AuthInterceptor(sessionManager))          // gắn Bearer token
                .authenticator(new TokenAuthenticator(context, sessionManager)) // refresh khi 401
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Gọi một lần duy nhất trong Application.onCreate() hoặc trước khi dùng.
     */
    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context.getApplicationContext());
        }
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            throw new IllegalStateException("RetrofitClient chưa được khởi tạo. Gọi RetrofitClient.init(context) trước.");
        }
        return instance;
    }

    public <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
