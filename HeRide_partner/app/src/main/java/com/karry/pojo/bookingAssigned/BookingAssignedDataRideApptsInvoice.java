package com.karry.pojo.bookingAssigned;

import java.io.Serializable;

/**
 * Created by embed on 21/2/18.
 */

public class BookingAssignedDataRideApptsInvoice implements Serializable {

    private String total;

    private String timeFee;

    private String distanceCalc;

    private String watingFee;

    private String baseFee;

    private String tip;

    private String timeCalc;

    private String distanceFee;

    private String discount;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTimeFee() {
        return timeFee;
    }

    public void setTimeFee(String timeFee) {
        this.timeFee = timeFee;
    }

    public String getDistanceCalc() {
        return distanceCalc;
    }

    public void setDistanceCalc(String distanceCalc) {
        this.distanceCalc = distanceCalc;
    }

    public String getWatingFee() {
        return watingFee;
    }

    public void setWatingFee(String watingFee) {
        this.watingFee = watingFee;
    }

    public String getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(String baseFee) {
        this.baseFee = baseFee;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTimeCalc() {
        return timeCalc;
    }

    public void setTimeCalc(String timeCalc) {
        this.timeCalc = timeCalc;
    }

    public String getDistanceFee() {
        return distanceFee;
    }

    public void setDistanceFee(String distanceFee) {
        this.distanceFee = distanceFee;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
