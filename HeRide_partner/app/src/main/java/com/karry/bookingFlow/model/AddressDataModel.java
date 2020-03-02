package com.karry.bookingFlow.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>AddressDataModel</h1>
 * This class is used to hold the address data
 * @author embed
 * @since on 17/3/17.
 */

public class AddressDataModel implements Serializable {


    @SerializedName("addressId")
    @Expose
    private int addressId;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("lat")
    @Expose
    private String lat;

    @SerializedName("lng")
    @Expose
    private String lng;

    @SerializedName("paceId")
    @Expose
    private String paceId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("isToAddAsFav")
    @Expose
    private boolean isToAddAsFav = false;

    @SerializedName("isItAFavAdrs")
    @Expose
    private  boolean isItAFavAdrs =false;


    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPaceId() {
        return paceId;
    }

    public void setPaceId(String paceId) {
        this.paceId = paceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isToAddAsFav() {
        return isToAddAsFav;
    }

    public void setToAddAsFav(boolean toAddAsFav) {
        isToAddAsFav = toAddAsFav;
    }

    public boolean isItAFavAdrs() {
        return isItAFavAdrs;
    }

    public void setItAFavAdrs(boolean itAFavAdrs) {
        isItAFavAdrs = itAFavAdrs;
    }
}
