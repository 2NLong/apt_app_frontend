package com.ptithcm.apt;

import android.app.Application;

import com.ptithcm.apt.network.retrofit.RetrofitClient;

public class AptApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo RetrofitClient một lần duy nhất với Application context
        RetrofitClient.init(this);
    }
}
