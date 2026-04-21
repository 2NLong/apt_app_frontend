package com.ptithcm.apt.models.resident;

import java.util.List;

public class ResidentPageResponse {
    private List<ResidentListResponse> content;
    private int totalPages;
    private int number; // Đây là index của trang hiện tại (từ 0)

    public ResidentPageResponse() {
    }

    public List<ResidentListResponse> getContent() {
        return content;
    }

    public void setContent(List<ResidentListResponse> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}