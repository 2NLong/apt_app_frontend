package com.ptithcm.apt.models.bill.response;

public class BillApartmentResponse {
    private Long id;
    private String roomNumber;
    private Double area;

    public BillApartmentResponse() {
    }

    public BillApartmentResponse(Double area, String roomNumber, Long id) {
        this.area = area;
        this.roomNumber = roomNumber;
        this.id = id;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Override
    public String toString() {
        return roomNumber != null ? roomNumber : "";
    }


}
