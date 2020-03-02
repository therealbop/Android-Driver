package com.karry.pojo.bookingAssigned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class SomeOne implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("isSomeOneElseBooking")
    @Expose
    private String isSomeOneElseBooking;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsSomeOneElseBooking() {
        return isSomeOneElseBooking;
    }

    public void setIsSomeOneElseBooking(String isSomeOneElseBooking) {
        this.isSomeOneElseBooking = isSomeOneElseBooking;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
