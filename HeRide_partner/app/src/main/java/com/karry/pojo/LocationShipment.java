package com.karry.pojo;

import java.io.Serializable;

/**
 * Created by ads on 17/05/17.
 */

public class LocationShipment implements Serializable {

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
