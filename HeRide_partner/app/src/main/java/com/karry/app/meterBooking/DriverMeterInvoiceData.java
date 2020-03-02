package com.karry.app.meterBooking;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DriverMeterInvoiceData implements Serializable{

    @SerializedName("bid")
    @Expose
    private String bid;

    @SerializedName("minFare")
    @Expose
    private String minFare;

    @SerializedName("tripOnTime")
    @Expose
    private String tripOnTime;

    @SerializedName("baseFare")
    @Expose
    private String baseFare;

    @SerializedName("distanceFare")
    @Expose
    private Float distanceFare;

    @SerializedName("timeFare")
    @Expose
    private Float timeFare;

    @SerializedName("totalFare")
    @Expose
    private Float totalFare;

    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;



    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getMinFare() {
        return minFare;
    }

    public void setMinFare(String minFare) {
        this.minFare = minFare;
    }

    public String getTripOnTime() {
        return tripOnTime;
    }

    public void setTripOnTime(String tripOnTime) {
        this.tripOnTime = tripOnTime;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public Float getDistanceFare() {
        return distanceFare;
    }

    public void setDistanceFare(Float distanceFare) {
        this.distanceFare = distanceFare;
    }

    public Float getTimeFare() {
        return timeFare;
    }

    public void setTimeFare(Float timeFare) {
        this.timeFare = timeFare;
    }

    public Float getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(Float totalFare) {
        this.totalFare = totalFare;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
