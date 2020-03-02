package com.karry.app.bookingRequest;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookingDataResponseRec implements Serializable{

    @SerializedName("loadType")
    @Expose
    private String loadType;

    @SerializedName("approxFare")
    @Expose
    private String approxFare;

    @SerializedName("approxDistance")
    @Expose
    private String approxDistance;

    @SerializedName("goodType")
    @Expose
    private String goodType;

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

    public String getApproxFare() {
        return approxFare;
    }

    public void setApproxFare(String approxFare) {
        this.approxFare = approxFare;
    }

    public String getApproxDistance() {
        return approxDistance;
    }

    public void setApproxDistance(String approxDistance) {
        this.approxDistance = approxDistance;
    }

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }
}
