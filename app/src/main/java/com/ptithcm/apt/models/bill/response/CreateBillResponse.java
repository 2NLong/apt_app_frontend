package com.ptithcm.apt.models.bill.response;

import com.ptithcm.apt.enums.BillStatus;
import java.math.BigDecimal;

public class CreateBillResponse {
    private Long id;
    private Long apartmentId;
    private Integer billingMonth;
    private Integer billingYear;
    private BigDecimal totalAmount;
    private BillStatus status;
    private String createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }

}