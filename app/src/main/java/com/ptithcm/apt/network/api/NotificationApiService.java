package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.notification.CreateNotificationRequest;
import com.ptithcm.apt.models.notification.NotificationResponse;
import com.ptithcm.apt.models.notification.NotificationTargetResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.POST;

public interface NotificationApiService {
    @GET("api/v1/notifications")
    Call<ApiResponse<List<NotificationResponse>>> getAllNotifications();

    @GET("api/v1/notifications/targets")
    Call<ApiResponse<List<NotificationTargetResponse>>> getNotificationTargets();

    @GET("api/v1/notifications/my")
    Call<ApiResponse<List<NotificationResponse>>> getMyNotifications();

    @POST("api/v1/notifications")
    Call<ApiResponse<NotificationResponse>> createNotification(@Body CreateNotificationRequest request);

    @PATCH("api/v1/notifications/my/read-all")
    Call<ApiResponse<Void>> markMyNotificationsAsRead();

    @PATCH("api/v1/notifications/my/{id}/read")
    Call<ApiResponse<Void>> markMyNotificationAsRead(@Path("id") Long id);
}
