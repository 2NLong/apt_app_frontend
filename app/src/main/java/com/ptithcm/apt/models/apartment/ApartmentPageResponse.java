package com.ptithcm.apt.models.apartment;

import java.util.List;

public class ApartmentPageResponse {
    private List<Apartment> content;
    private int totalPages;
    private int number;

    public List<Apartment> getContent() { return content; }
    public int getTotalPages() { return totalPages; }
    public int getNumber() { return number; }
}