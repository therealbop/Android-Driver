package com.karry.side_screens.wallet.wallet_transaction_activity.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransctionsItem implements Serializable {

    @SerializedName("currencyAbbr")
    private int currencyAbbr;

    @SerializedName("amount")
    private Double amount;

    @SerializedName("paymentTypeText")
    private String paymentTypeText;

    @SerializedName("currencySymbol")
    private String currencySymbol;

    @SerializedName("txnType")
    private String txnType;

    @SerializedName("comment")
    private String comment;

    @SerializedName("tripId")
    private String tripId;

    @SerializedName("trigger")
    private String trigger;

    @SerializedName("txnDate")
    private int txnDate;

    @SerializedName("txnId")
    private String txnId;

    public void setCurrencyAbbr(int currencyAbbr) {
        this.currencyAbbr = currencyAbbr;
    }

    public int getCurrencyAbbr() {
        return currencyAbbr;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setPaymentTypeText(String paymentTypeText) {
        this.paymentTypeText = paymentTypeText;
    }

    public String getPaymentTypeText() {
        return paymentTypeText;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTxnDate(int txnDate) {
        this.txnDate = txnDate;
    }

    public int getTxnDate() {
        return txnDate;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getTxnId() {
        return txnId;
    }

    @Override
    public String toString() {
        return "TransctionsItem{" + "currencyAbbr = '" + currencyAbbr + '\'' + ",amount = '" + amount + '\'' + ",paymentTypeText = '" + paymentTypeText + '\'' + ",currencySymbol = '" + currencySymbol + '\'' + ",txnType = '" + txnType + '\'' + ",comment = '" + comment + '\'' + ",tripId = '" + tripId + '\'' + ",trigger = '" + trigger + '\'' + ",txnDate = '" + txnDate + '\'' + ",txnId = '" + txnId + '\'' + "}";
    }
}