package com.karry.side_screens.bankDetails.pojoforBank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by embed on 30/11/18.
 */

public class ConnectAccountCountryList implements Serializable {

    @SerializedName("countryCode")
    @Expose
    private String countryCode;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("selected")
    @Expose
    private boolean selected;


    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
