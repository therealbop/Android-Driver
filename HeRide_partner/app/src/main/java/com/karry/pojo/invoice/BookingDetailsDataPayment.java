package com.karry.pojo.invoice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookingDetailsDataPayment implements Serializable{

    @SerializedName("cashCollected")
    @Expose
    private String cashCollected;

    @SerializedName("cardDeduct")
    @Expose
    private String cardDeduct;

    @SerializedName("walletTransaction")
    @Expose
    private String walletTransaction;

    @SerializedName("isCorporateBooking")
    @Expose
    private boolean isCorporateBooking;

    public String getCashCollected() {
        return cashCollected;
    }

    public void setCashCollected(String cashCollected) {
        this.cashCollected = cashCollected;
    }

    public String getCardDeduct() {
        return cardDeduct;
    }

    public void setCardDeduct(String cardDeduct) {
        this.cardDeduct = cardDeduct;
    }

    public String getWalletTransaction() {
        return walletTransaction;
    }

    public void setWalletTransaction(String walletTransaction) {
        this.walletTransaction = walletTransaction;
    }

    public boolean isCorporateBooking() {
        return isCorporateBooking;
    }

    public void setCorporateBooking(boolean corporateBooking) {
        isCorporateBooking = corporateBooking;
    }
}
