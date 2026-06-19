package com.ptithcm.apt.models.notification;

import java.util.List;

public class CreateNotificationRequest {
    private final String title;
    private final String content;
    private final String targetType;
    private final List<Long> apartmentIds;

    public CreateNotificationRequest(String title, String content, String targetType, List<Long> apartmentIds) {
        this.title = title;
        this.content = content;
        this.targetType = targetType;
        this.apartmentIds = apartmentIds;
    }
}
