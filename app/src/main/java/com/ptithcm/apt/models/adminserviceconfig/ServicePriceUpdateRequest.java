package com.ptithcm.apt.models.adminserviceconfig;

import java.math.BigDecimal;

public class ServicePriceUpdateRequest {
    private String serviceCode;
    private BigDecimal newPrice;
    private String effectiveFrom;

    public ServicePriceUpdateRequest(String serviceCode, BigDecimal newPrice, String effectiveFrom) {
        this.serviceCode = serviceCode;
        this.newPrice = newPrice;
        this.effectiveFrom = effectiveFrom;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
}
