package com.ptithcm.apt.models.adminserviceconfig;

import java.math.BigDecimal;

public class AdminServiceConfigResponse {
    private String serviceCode;
    private String serviceName;
    private String unit;
    private BigDecimal currentPrice;
    private String currentEffectiveFrom;
    private BigDecimal upcomingPrice;
    private String upcomingEffectiveFrom;

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

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCurrentEffectiveFrom() {
        return currentEffectiveFrom;
    }

    public void setCurrentEffectiveFrom(String currentEffectiveFrom) {
        this.currentEffectiveFrom = currentEffectiveFrom;
    }

    public BigDecimal getUpcomingPrice() {
        return upcomingPrice;
    }

    public void setUpcomingPrice(BigDecimal upcomingPrice) {
        this.upcomingPrice = upcomingPrice;
    }

    public String getUpcomingEffectiveFrom() {
        return upcomingEffectiveFrom;
    }

    public void setUpcomingEffectiveFrom(String upcomingEffectiveFrom) {
        this.upcomingEffectiveFrom = upcomingEffectiveFrom;
    }
}
