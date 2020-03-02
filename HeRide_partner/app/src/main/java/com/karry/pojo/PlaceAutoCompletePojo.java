package com.karry.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by embed on 27/3/17.
 */

public class PlaceAutoCompletePojo
{
    @SerializedName("address")
    @Expose
    String address;

    @SerializedName("ref_key")
    @Expose
    String ref_key;

    @SerializedName("placeId")
    @Expose
    String placeId;

    @SerializedName("lat")
    @Expose
    String lat ;

    @SerializedName("lng")
    @Expose
    String lng;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRef_key() {
        return ref_key;
    }

    public void setRef_key(String ref_key) {
        this.ref_key = ref_key;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
