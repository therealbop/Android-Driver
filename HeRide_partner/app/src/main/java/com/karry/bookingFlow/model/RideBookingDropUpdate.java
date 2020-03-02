package com.karry.bookingFlow.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RideBookingDropUpdate implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("bookingId")
    @Expose
    private String bookingId;

    @SerializedName("dropAddress")
    @Expose
    private String dropAddress;

    @SerializedName("dropLatitude")
    @Expose
    private String dropLatitude;

    @SerializedName("dropLongitude")
    @Expose
    private String dropLongitude;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDropLatitude() {
        return dropLatitude;
    }

    public void setDropLatitude(String dropLatitude) {
        this.dropLatitude = dropLatitude;
    }

    public String getDropLongitude() {
        return dropLongitude;
    }

    public void setDropLongitude(String dropLongitude) {
        this.dropLongitude = dropLongitude;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }
}
