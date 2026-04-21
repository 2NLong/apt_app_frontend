package com.ptithcm.apt.models.bill;

import java.math.BigDecimal;

public class BillServiceConfig {
    private String serviceCode;
    private String serviceName;
    private BigDecimal unitPrice;
    private String unit;

    public BillServiceConfig() {
    }

    public BillServiceConfig(String serviceCode, String serviceName, String unit, BigDecimal unitPrice) {
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
        this.unit = unit;
        this.unitPrice = unitPrice;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
