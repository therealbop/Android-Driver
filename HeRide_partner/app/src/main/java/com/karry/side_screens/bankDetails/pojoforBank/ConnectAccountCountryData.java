package com.karry.side_screens.bankDetails.pojoforBank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 30/11/18.
 */

public class ConnectAccountCountryData implements Serializable {

    @SerializedName("countryList")
    @Expose
    private ArrayList<ConnectAccountCountryList> countryList;

    public ArrayList<ConnectAccountCountryList> getCountryList() {
        return countryList;
    }

    public void setCountryList(ArrayList<ConnectAccountCountryList> countryList) {
        this.countryList = countryList;
    }
}
