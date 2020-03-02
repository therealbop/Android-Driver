package com.karry.pojo.bookingAssigned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>BookingAssignedData</h1>
 * <p>the data of bookingAssigned API response</p>
 */

public class BookingAssignedData  implements Serializable{

    @SerializedName("gmtTimestamp")
    @Expose
    private String gmtTimestamp;

    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;

    @SerializedName("currencyAbbr")
    @Expose
    private String currencyAbbr;

    @SerializedName("walletBalance")
    @Expose
    private String walletBalance;

    @SerializedName("walletHardLimit")
    @Expose
    private String walletHardLimit;

    @SerializedName("walletSoftLimit")
    @Expose
    private String walletSoftLimit;

    @SerializedName("MasterStatus")
    @Expose
    private String MasterStatus;

    @SerializedName("godsviewTopic")
    @Expose
    private String godsviewTopic;

    @SerializedName("dispatcherTopic")
    @Expose
    private String dispatcherTopic;

    @SerializedName("isMeterBookingEnable")
    @Expose
    private boolean isMeterBookingEnable;

    @SerializedName("rideAppts")
    @Expose
    private ArrayList<BookingAssignedDataRideAppts> rideAppts;

    @SerializedName("rideApptsPendingReview")
    @Expose
    private ArrayList<BookingAssignedDataPendingReview> rideApptsPendingReview;

    @SerializedName("deliveryAppts")
    @Expose
    private ArrayList<BookingAssignedDataDeliveryAppts> deliveryAppts;


    public String getGodsviewTopic() {
        return godsviewTopic;
    }

    public void setGodsviewTopic(String godsviewTopic) {
        this.godsviewTopic = godsviewTopic;
    }

    public String getDispatcherTopic() {
        return dispatcherTopic;
    }

    public void setDispatcherTopic(String dispatcherTopic) {
        this.dispatcherTopic = dispatcherTopic;
    }

    public String getGmtTimestamp() {
        return gmtTimestamp;
    }

    public void setGmtTimestamp(String gmtTimestamp) {
        this.gmtTimestamp = gmtTimestamp;
    }

    public ArrayList<BookingAssignedDataRideAppts> getRideAppts() {
        return rideAppts;
    }

    public void setRideAppts(ArrayList<BookingAssignedDataRideAppts> rideAppts) {
        this.rideAppts = rideAppts;
    }

    public String getMasterStatus() {
        return MasterStatus;
    }

    public void setMasterStatus(String masterStatus) {
        MasterStatus = masterStatus;
    }

    public ArrayList<BookingAssignedDataPendingReview> getRideApptsPendingReview() {
        return rideApptsPendingReview;
    }

    public void setRideApptsPendingReview(ArrayList<BookingAssignedDataPendingReview> rideApptsPendingReview) {
        this.rideApptsPendingReview = rideApptsPendingReview;
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

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getWalletHardLimit() {
        return walletHardLimit;
    }

    public void setWalletHardLimit(String walletHardLimit) {
        this.walletHardLimit = walletHardLimit;
    }

    public String getWalletSoftLimit() {
        return walletSoftLimit;
    }

    public void setWalletSoftLimit(String walletSoftLimit) {
        this.walletSoftLimit = walletSoftLimit;
    }

    public boolean isMeterBookingEnable() {
        return isMeterBookingEnable;
    }

    public void setMeterBookingEnable(boolean meterBookingEnable) {
        isMeterBookingEnable = meterBookingEnable;
    }

    public ArrayList<BookingAssignedDataDeliveryAppts> getDeliveryAppts() {
        return deliveryAppts;
    }

    public void setDeliveryAppts(ArrayList<BookingAssignedDataDeliveryAppts> deliveryAppts) {
        this.deliveryAppts = deliveryAppts;
    }
}
