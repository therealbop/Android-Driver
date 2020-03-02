package com.karry.telecall.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewCallMqttResponse implements Serializable
{

    @SerializedName("data")
    @Expose
    private NewCallData data;

    public NewCallData getData() {
        return data;
    }

    public void setData(NewCallData data) {
        this.data = data;
    }

}