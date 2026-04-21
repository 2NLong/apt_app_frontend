package com.ptithcm.apt.utils;

import com.google.gson.Gson;
import com.ptithcm.apt.models.auth.response.ApiResponse;

import retrofit2.Response;

public class ErrorUtils {

    private static final Gson gson = new Gson();

    public static String getErrorMessage(Response<?> response, String defaultMessage) {
        if (response != null && response.errorBody() != null) {
            try {
                String errorBodyStr = response.errorBody().string();
                ApiResponse<?> apiResponse = gson.fromJson(errorBodyStr, ApiResponse.class);
                if (apiResponse != null && apiResponse.getMessage() != null && !apiResponse.getMessage().isEmpty()) {
                    return apiResponse.getMessage();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultMessage;
    }
}
