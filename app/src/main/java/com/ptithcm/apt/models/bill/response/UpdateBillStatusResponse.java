package com.ptithcm.apt.models.bill.response;

import com.ptithcm.apt.enums.BillStatus;

public class UpdateBillStatusResponse {
    private Long id;
    private BillStatus status;

    public UpdateBillStatusResponse() {
    }

    public UpdateBillStatusResponse(Long id, BillStatus status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }
}
