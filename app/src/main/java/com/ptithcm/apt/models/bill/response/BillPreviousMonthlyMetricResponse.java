package com.ptithcm.apt.models.bill.response;

import java.math.BigDecimal;

public class BillPreviousMonthlyMetricResponse {
    Long apartmentId;
    BigDecimal latestElectricity;
    BigDecimal latestWater;

    public BillPreviousMonthlyMetricResponse() {
    }

    public BillPreviousMonthlyMetricResponse(Long apartmentId, BigDecimal latestElectricity, BigDecimal latestWater) {
        this.apartmentId = apartmentId;
        this.latestElectricity = latestElectricity;
        this.latestWater = latestWater;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public BigDecimal getLatestWater() {
        return latestWater;
    }

    public void setLatestWater(BigDecimal latestWater) {
        this.latestWater = latestWater;
    }

    public BigDecimal getLatestElectricity() {
        return latestElectricity;
    }

    public void setLatestElectricity(BigDecimal latestElectricity) {
        this.latestElectricity = latestElectricity;
    }
}
