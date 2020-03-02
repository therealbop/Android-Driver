package com.karry.pojo.invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by embed on 11/12/18.
 */

public class BookingDetailsDataTowTruckServices implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("preferenceFor")
    @Expose
    private String preferenceFor;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("fee")
    @Expose
    private String fee;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreferenceFor() {
        return preferenceFor;
    }

    public void setPreferenceFor(String preferenceFor) {
        this.preferenceFor = preferenceFor;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}