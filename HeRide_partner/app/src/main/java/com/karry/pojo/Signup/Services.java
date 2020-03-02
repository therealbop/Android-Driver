package com.karry.pojo.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class Services implements Serializable {


    @SerializedName("selected")
    @Expose
    private boolean selected;

    @SerializedName("isMeterBookingEnable")
    @Expose
    private boolean isMeterBookingEnable;

    @SerializedName("serviceId")
    @Expose
    private String serviceId;

    @SerializedName("serviceType")
    @Expose
    private String serviceType;

    @SerializedName("serviceName")
    @Expose
    private String serviceName;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isMeterBookingEnable() {
        return isMeterBookingEnable;
    }

    public void setMeterBookingEnable(boolean meterBookingEnable) {
        isMeterBookingEnable = meterBookingEnable;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
