package com.ptithcm.apt.models.rentinvoice.request;

import com.ptithcm.apt.enums.RentStatus;

public class UpdateRentInvoiceStatusRequest {
    RentStatus status;

    public UpdateRentInvoiceStatusRequest() {
    }

    public UpdateRentInvoiceStatusRequest(RentStatus status) {
        this.status = status;
    }

    public RentStatus getStatus() {
        return status;
    }

    public void setStatus(RentStatus status) {
        this.status = status;
    }
}
