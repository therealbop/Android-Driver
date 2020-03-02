package com.karry.pojo.invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class BookingDetailsDataInvoiceExtra implements Serializable {

    @SerializedName("fee")
    @Expose
    private String fee;

    @SerializedName("title")
    @Expose
    private String title;

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
