package com.karry.pojo.AreaZone;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AreaZoneDataZonesPath implements Serializable{

    @SerializedName("lng")
    @Expose
    private String lng;

    @SerializedName("lat")
    @Expose
    private String lat;


    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
