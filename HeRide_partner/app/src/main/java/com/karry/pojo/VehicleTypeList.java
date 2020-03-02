package com.karry.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VehicleTypeList implements Serializable {

    @SerializedName("vehicleImgOn")
    @Expose
    private String vehicleImgOn;

    @SerializedName("vehicleTypeId")
    @Expose
    private String vehicleTypeId;

    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;

    @SerializedName("currencyAbbr")
    @Expose
    private String currencyAbbr;

    @SerializedName("vehicleTypeName")
    @Expose
    private String vehicleTypeName;

    @SerializedName("minimumFare")
    @Expose
    private String minimumFare;

    @SerializedName("mileagePrice")
    @Expose
    private String mileagePrice;

    @SerializedName("distanceMetrics")
    @Expose
    private String distanceMetrics;

    @SerializedName("isDefaultVehicleType")
    @Expose
    private boolean isDefaultVehicleType;


    public String getVehicleImgOn() {
        return vehicleImgOn;
    }

    public void setVehicleImgOn(String vehicleImgOn) {
        this.vehicleImgOn = vehicleImgOn;
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrencyAbbr() {
        return currencyAbbr;
    }

    public void setCurrencyAbbr(String currencyAbbr) {
        this.currencyAbbr = currencyAbbr;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getMinimumFare() {
        return minimumFare;
    }

    public void setMinimumFare(String minimumFare) {
        this.minimumFare = minimumFare;
    }

    public String getMileagePrice() {
        return mileagePrice;
    }

    public void setMileagePrice(String mileagePrice) {
        this.mileagePrice = mileagePrice;
    }

    public String getDistanceMetrics() {
        return distanceMetrics;
    }

    public void setDistanceMetrics(String distanceMetrics) {
        this.distanceMetrics = distanceMetrics;
    }

    public boolean isDefaultVehicleType() {
        return isDefaultVehicleType;
    }

    public void setDefaultVehicleType(boolean defaultVehicleType) {
        isDefaultVehicleType = defaultVehicleType;
    }
}
