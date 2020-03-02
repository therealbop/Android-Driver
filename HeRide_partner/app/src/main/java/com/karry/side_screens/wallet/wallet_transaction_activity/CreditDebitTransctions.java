package com.karry.side_screens.wallet.wallet_transaction_activity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreditDebitTransctions implements Serializable
{
    @SerializedName("txnType")
    @Expose
    private String txnType;

    @SerializedName("trigger")
    @Expose
    private String trigger;

    @SerializedName("openingBal")
    @Expose
    private String openingBal;

    @SerializedName("tripId")
    @Expose
    private String tripId;

    @SerializedName("paymentType")
    @Expose
    private String paymentType;

    @SerializedName("intiatedBy")
    @Expose
    private String intiatedBy;

    @SerializedName("txnId")
    @Expose
    private String txnId;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    @SerializedName("paymentTxnId")
    @Expose
    private String paymentTxnId;

    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("closingBal")
    @Expose
    private String closingBal;

    @SerializedName("currencyAbbr")
    @Expose
    private String currencyAbbr;



    public String getTxnType ()
    {
        return txnType;
    }

    public void setTxnType (String txnType)
    {
        this.txnType = txnType;
    }

    public String getTrigger ()
    {
        return trigger;
    }

    public void setTrigger (String trigger)
    {
        this.trigger = trigger;
    }

    public String getOpeningBal ()
    {
        return openingBal;
    }

    public void setOpeningBal (String openingBal)
    {
        this.openingBal = openingBal;
    }

    public String getTripId ()
    {
        return tripId;
    }

    public void setTripId (String tripId)
    {
        this.tripId = tripId;
    }

    public String getPaymentType ()
    {
        return paymentType;
    }

    public void setPaymentType (String paymentType)
    {
        this.paymentType = paymentType;
    }

    public String getIntiatedBy ()
    {
        return intiatedBy;
    }

    public void setIntiatedBy (String intiatedBy)
    {
        this.intiatedBy = intiatedBy;
    }

    public String getTxnId ()
    {
        return txnId;
    }

    public void setTxnId (String txnId)
    {
        this.txnId = txnId;
    }

    public String getCurrency ()
    {
        return currency;
    }

    public void setCurrency (String currency)
    {
        this.currency = currency;
    }

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getTimestamp ()
    {
        return timestamp;
    }

    public void setTimestamp (String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getPaymentTxnId ()
    {
        return paymentTxnId;
    }

    public void setPaymentTxnId (String paymentTxnId)
    {
        this.paymentTxnId = paymentTxnId;
    }

    public String getComment ()
    {
        return comment;
    }

    public void setComment (String comment)
    {
        this.comment = comment;
    }

    public String getClosingBal ()
    {
        return closingBal;
    }

    public void setClosingBal (String closingBal)
    {
        this.closingBal = closingBal;
    }

    public String getCurrencyAbbr() {
        return currencyAbbr;
    }

    public void setCurrencyAbbr(String currencyAbbr) {
        this.currencyAbbr = currencyAbbr;
    }
}
