package com.ptithcm.apt.models.bill.request;

import com.ptithcm.apt.enums.BillStatus;

public class UpdateBillStatusRequest {
    private BillStatus status;

    public UpdateBillStatusRequest() {
    }

    public UpdateBillStatusRequest(BillStatus status) {
        this.status = status;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }
}
