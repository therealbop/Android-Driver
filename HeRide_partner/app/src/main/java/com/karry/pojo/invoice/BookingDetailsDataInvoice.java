package com.karry.pojo.invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 23/2/18.
 */

public class BookingDetailsDataInvoice implements Serializable {

    @SerializedName("minFee")
    @Expose
    private String minFee;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("timeFee")
    @Expose
    private String timeFee;

    @SerializedName("distanceCalc")
    @Expose
    private String distanceCalc;

    @SerializedName("subTotalCalc")
    @Expose
    private String subTotalCalc;

    @SerializedName("watingFee")
    @Expose
    private String watingFee;

    @SerializedName("baseFee")
    @Expose
    private String baseFee;

    @SerializedName("tip")
    @Expose
    private String tip;

    @SerializedName("timeCalc")
    @Expose
    private String timeCalc;

    @SerializedName("distanceFee")
    @Expose
    private String distanceFee;

    @SerializedName("discount")
    @Expose
    private String discount;

    @SerializedName("isMinFeeApplied")
    @Expose
    private String isMinFeeApplied;

    @SerializedName("waitingFee")
    @Expose
    private String waitingFee;

    @SerializedName("isMeterBooking")
    @Expose
    private String isMeterBooking;

    @SerializedName("paymentType")
    @Expose
    private String paymentType;

    @SerializedName("lastDue")
    @Expose
    private String lastDue;

    @SerializedName("waitingTime")
    @Expose
    private String waitingTime;

    @SerializedName("extraFees")
    @Expose
    private ArrayList<BookingDetailsDataInvoiceExtra> extraFees;




    public String getMinFee() {
        return minFee;
    }

    public void setMinFee(String minFee) {
        this.minFee = minFee;
    }

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

    public String getSubTotalCalc() {
        return subTotalCalc;
    }

    public void setSubTotalCalc(String subTotalCalc) {
        this.subTotalCalc = subTotalCalc;
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

    public ArrayList<BookingDetailsDataInvoiceExtra> getExtraFees() {
        return extraFees;
    }

    public void setExtraFees(ArrayList<BookingDetailsDataInvoiceExtra> extraFees) {
        this.extraFees = extraFees;
    }

    public String getIsMinFeeApplied() {
        return isMinFeeApplied;
    }

    public void setIsMinFeeApplied(String isMinFeeApplied) {
        this.isMinFeeApplied = isMinFeeApplied;
    }

    public String getWaitingFee() {
        return waitingFee;
    }

    public void setWaitingFee(String waitingFee) {
        this.waitingFee = waitingFee;
    }

    public String getIsMeterBooking() {
        return isMeterBooking;
    }

    public void setIsMeterBooking(String isMeterBooking) {
        this.isMeterBooking = isMeterBooking;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getLastDue() {
        return lastDue;
    }

    public void setLastDue(String lastDue) {
        this.lastDue = lastDue;
    }

    public String getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(String waitingTime) {
        this.waitingTime = waitingTime;
    }

}
