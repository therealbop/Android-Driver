package com.karry.pojo.invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class BookingDetailsPojo implements Serializable{

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private BookingDetailsData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BookingDetailsData getData() {
        return data;
    }

    public void setData(BookingDetailsData data) {
        this.data = data;
    }
}
