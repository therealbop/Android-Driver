package com.karry.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * <h1>LatLngBody</h1>
 * <p>body of the latlng which used for LocationUpdate Service</p>
 */
public class LatLngBody implements Serializable {

    @Expose
    @SerializedName("latLong")
    private String[] latLong;

    public String[] getLatLong() {
        return latLong;
    }
}
