package com.ptithcm.apt.models.complaint;

public class CreateComplaintRequest {
    private final Long apartmentId;
    private final String category;
    private final String title;
    private final String content;

    public CreateComplaintRequest(Long apartmentId, String category, String title, String content) {
        this.apartmentId = apartmentId;
        this.category = category;
        this.title = title;
        this.content = content;
    }
}
