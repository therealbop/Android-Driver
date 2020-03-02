package com.karry.side_screens.history.new_model;





import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HistoryExtraFees implements Serializable
{
    @SerializedName("fee")
    @Expose
    private String fee;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("location")
    @Expose
    private HistoryLocation location;

    public String getFee ()
    {
        return fee;
    }

    public void setFee (String fee)
    {
        this.fee = fee;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public HistoryLocation getLocation ()
    {
        return location;
    }

    public void setLocation (HistoryLocation location)
    {
        this.location = location;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [fee = "+fee+", title = "+title+", location = "+location+"]";
    }
}
