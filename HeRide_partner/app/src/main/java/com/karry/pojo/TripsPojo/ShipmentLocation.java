package com.karry.pojo.TripsPojo;

import java.io.Serializable;

/**
 * Created by embed on 23/5/17.
 */

public class ShipmentLocation implements Serializable {

    private String log;

    private String lat;

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
