package com.karry.pojo.TripsPojo;

import java.io.Serializable;

/**
 * Created by Admin on 7/3/2017.
 */

public class TotalEarning implements Serializable {
    private String amt;

    private String date;

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
