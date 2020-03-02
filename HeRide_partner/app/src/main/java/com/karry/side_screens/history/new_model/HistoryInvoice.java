package com.karry.side_screens.history.new_model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.karry.pojo.invoice.BookingDetailsDataTowTruckServices;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoryInvoice implements Serializable
{
    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("timeFee")
    @Expose
    private String timeFee;

    @SerializedName("distanceCalc")
    @Expose
    private String distanceCalc;

    @SerializedName("isMinFeeApplied")
    @Expose
    private String isMinFeeApplied;

    @SerializedName("waitingFee")
    @Expose
    private String waitingFee;

    @SerializedName("paymentType")
    @Expose
    private String paymentType;

    @SerializedName("cardLastFourDigit")
    @Expose
    private String cardLastFourDigit;

    @SerializedName("cardType")
    @Expose
    private String cardType;

    @SerializedName("distanceFee")
    @Expose
    private String distanceFee;

    @SerializedName("tip")
    @Expose
    private String tip;

    @SerializedName("discount")
    @Expose
    private String discount;

    @SerializedName("extraFees")
    @Expose
    private ArrayList<HistoryExtraFees> extraFees;

    @SerializedName("subTotalCalc")
    @Expose
    private String subTotalCalc;

    @SerializedName("masEarning")
    @Expose
    private String masEarning;

    @SerializedName("appCom")
    @Expose
    private String appCom;

    @SerializedName("baseFee")
    @Expose
    private String baseFee;

    @SerializedName("timeCalc")
    @Expose
    private String timeCalc;

    @SerializedName("lastDue")
    @Expose
    private String lastDue;

    @SerializedName("minFee")
    @Expose
    private String minFee;

    @SerializedName("waitingTime")
    @Expose
    private String waitingTime;

    @SerializedName("pgCommission")
    @Expose
    private String pgCommission;

    @SerializedName("towTruckBooking")
    @Expose
    private boolean towTruckBooking;

    @SerializedName("towTruckBookingService")
    @Expose
    private String towTruckBookingService;

    @SerializedName("towTruckServices")
    @Expose
    private ArrayList<BookingDetailsDataTowTruckServices> towTruckServices;






    public String getIsMinFeeApplied() {
        return isMinFeeApplied;
    }

    public void setIsMinFeeApplied(String isMinFeeApplied) {
        this.isMinFeeApplied = isMinFeeApplied;
    }

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    public String getTimeFee ()
    {
        return timeFee;
    }

    public void setTimeFee (String timeFee)
    {
        this.timeFee = timeFee;
    }

    public String getDistanceCalc ()
    {
        return distanceCalc;
    }

    public void setDistanceCalc (String distanceCalc)
    {
        this.distanceCalc = distanceCalc;
    }

    public String getWaitingFee ()
    {
        return waitingFee;
    }

    public void setWaitingFee (String waitingFee)
    {
        this.waitingFee = waitingFee;
    }

    public String getPaymentType ()
    {
        return paymentType;
    }

    public void setPaymentType (String paymentType)
    {
        this.paymentType = paymentType;
    }

    public String getCardLastFourDigit ()
    {
        return cardLastFourDigit;
    }

    public void setCardLastFourDigit (String cardLastFourDigit)
    {
        this.cardLastFourDigit = cardLastFourDigit;
    }

    public String getCardType ()
    {
        return cardType;
    }

    public void setCardType (String cardType)
    {
        this.cardType = cardType;
    }

    public String getDistanceFee ()
    {
        return distanceFee;
    }

    public void setDistanceFee (String distanceFee)
    {
        this.distanceFee = distanceFee;
    }

    public String getTip ()
    {
        return tip;
    }

    public void setTip (String tip)
    {
        this.tip = tip;
    }

    public String getDiscount ()
    {
        return discount;
    }

    public void setDiscount (String discount)
    {
        this.discount = discount;
    }

    public ArrayList<HistoryExtraFees> getExtraFees ()
    {
        return extraFees;
    }

    public void setExtraFees (ArrayList<HistoryExtraFees> extraFees)
    {
        this.extraFees = extraFees;
    }

    public String getSubTotalCalc ()
    {
        return subTotalCalc;
    }

    public void setSubTotalCalc (String subTotalCalc)
    {
        this.subTotalCalc = subTotalCalc;
    }

    public String getMasEarning ()
    {
        return masEarning;
    }

    public void setMasEarning (String masEarning)
    {
        this.masEarning = masEarning;
    }

    public String getAppCom ()
    {
        return appCom;
    }

    public void setAppCom (String appCom)
    {
        this.appCom = appCom;
    }

    public String getBaseFee ()
    {
        return baseFee;
    }

    public void setBaseFee (String baseFee)
    {
        this.baseFee = baseFee;
    }

    public String getTimeCalc ()
    {
        return timeCalc;
    }

    public void setTimeCalc (String timeCalc)
    {
        this.timeCalc = timeCalc;
    }

    public String getLastDue() {
        return lastDue;
    }

    public void setLastDue(String lastDue) {
        this.lastDue = lastDue;
    }

    public String getMinFee() {
        return minFee;
    }

    public void setMinFee(String minFee) {
        this.minFee = minFee;
    }

    public String getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(String waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getPgCommission() {
        return pgCommission;
    }

    public void setPgCommission(String pgCommission) {
        this.pgCommission = pgCommission;
    }

    public boolean isTowTruckBooking() {
        return towTruckBooking;
    }

    public void setTowTruckBooking(boolean towTruckBooking) {
        this.towTruckBooking = towTruckBooking;
    }

    public String getTowTruckBookingService() {
        return towTruckBookingService;
    }

    public void setTowTruckBookingService(String towTruckBookingService) {
        this.towTruckBookingService = towTruckBookingService;
    }

    public ArrayList<BookingDetailsDataTowTruckServices> getTowTruckServices() {
        return towTruckServices;
    }

    public void setTowTruckServices(ArrayList<BookingDetailsDataTowTruckServices> towTruckServices) {
        this.towTruckServices = towTruckServices;
    }
}
