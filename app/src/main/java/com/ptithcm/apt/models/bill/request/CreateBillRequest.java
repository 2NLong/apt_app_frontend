package com.ptithcm.apt.models.bill.request;

import java.math.BigDecimal;

public class CreateBillRequest {
    private Long apartmentId;
    private Integer month;
    private Integer year;
    private BigDecimal electricityService;
    private BigDecimal waterService;

    public CreateBillRequest(Long apartmentId, Integer month, Integer year, BigDecimal electricityService, BigDecimal waterService) {
        this.apartmentId = apartmentId;
        this.month = month;
        this.year = year;
        this.electricityService = electricityService;
        this.waterService = waterService;
    }
}
