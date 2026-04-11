package com.ptithcm.apt.models.adminserviceconfig;

public class AdminServiceConfigResponse {
    private String serviceCode;
    private String serviceName;
    private String unit;
    private Double currentPrice;
    private String currentEffectiveFrom;
    private Double upcomingPrice;
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

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCurrentEffectiveFrom() {
        return currentEffectiveFrom;
    }

    public void setCurrentEffectiveFrom(String currentEffectiveFrom) {
        this.currentEffectiveFrom = currentEffectiveFrom;
    }

    public Double getUpcomingPrice() {
        return upcomingPrice;
    }

    public void setUpcomingPrice(Double upcomingPrice) {
        this.upcomingPrice = upcomingPrice;
    }

    public String getUpcomingEffectiveFrom() {
        return upcomingEffectiveFrom;
    }

    public void setUpcomingEffectiveFrom(String upcomingEffectiveFrom) {
        this.upcomingEffectiveFrom = upcomingEffectiveFrom;
    }
}
