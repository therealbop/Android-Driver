package com.karry.pojo.AreaZone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AreaZone implements Serializable{

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private AreaZoneData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AreaZoneData getData() {
        return data;
    }

    public void setData(AreaZoneData data) {
        this.data = data;
    }
}
