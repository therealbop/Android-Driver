package com.karry.pojo.bookingAssigned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 10/12/18.
 */

public class TowTrayService implements Serializable {

    @SerializedName("data")
    @Expose
    ArrayList<TowtrayServiceSelectData> data;

    public ArrayList<TowtrayServiceSelectData> getData() {
        return data;
    }

    public void setData(ArrayList<TowtrayServiceSelectData> data) {
        this.data = data;
    }
}
