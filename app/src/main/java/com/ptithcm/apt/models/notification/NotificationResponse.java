package com.ptithcm.apt.models.notification;

public class NotificationResponse {
    private Long id;
    private String title;
    private String content;
    private String targetType;
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

    public Boolean getIsRead() {
        return isRead;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
