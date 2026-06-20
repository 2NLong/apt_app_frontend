package com.ptithcm.apt.models.notification;

import java.util.List;

public class NotificationResponse {
    private Long id;
    private String title;
    private String content;
    private String targetType;
    private String targetSummary;
    private List<String> roomNumbers;
    private Boolean isRead;
    private String createdAt;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTargetType() {
        return targetType;
    }

    public String getTargetSummary() {
        return targetSummary;
    }

    public List<String> getRoomNumbers() {
        return roomNumbers;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
