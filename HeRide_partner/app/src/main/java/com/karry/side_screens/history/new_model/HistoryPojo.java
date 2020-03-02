package com.karry.side_screens.history.new_model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HistoryPojo implements Serializable
{
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private HistoryData data;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public HistoryData getData ()
    {
        return data;
    }

    public void setData (HistoryData data)
    {
        this.data = data;
    }

}
