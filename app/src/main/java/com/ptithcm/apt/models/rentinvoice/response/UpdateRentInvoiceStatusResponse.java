package com.ptithcm.apt.models.rentinvoice.response;

import com.ptithcm.apt.enums.RentStatus;

public class UpdateRentInvoiceStatusResponse {
    private long id;
    private RentStatus status;

    public UpdateRentInvoiceStatusResponse() {
    }

    public UpdateRentInvoiceStatusResponse(long id, RentStatus status) {
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RentStatus getStatus() {
        return status;
    }

    public void setStatus(RentStatus status) {
        this.status = status;
    }
}
