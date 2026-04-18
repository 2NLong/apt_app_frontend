package com.ptithcm.apt.models.adminserviceconfig;

import java.math.BigDecimal;

public class ServiceConfigResponse {
    private String serviceCode;
    private String serviceName;
    private BigDecimal unitPrice;
    private String unit;
    private String effectiveFrom;
    public ServiceConfigResponse() {}

    public ServiceConfigResponse(String serviceCode, String serviceName, BigDecimal unitPrice, String unit, String effectiveFrom) {
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
        this.unitPrice = unitPrice;
        this.unit = unit;
        this.effectiveFrom = effectiveFrom;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

}

