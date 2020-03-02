package com.karry.app.meterBooking.address;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>DropLocationGoogleModel</h1>
 * used to hold the drop address model
 * @author  embed
 * @since on 27/3/17.
 */
public class DropLocationGoogleModel implements Serializable
{
    @SerializedName("result")
    @Expose
    private ResultGoogleAddressModel result;
    @SerializedName("status")
    @Expose
    private String status;

    public ResultGoogleAddressModel getResult ()
    {
        return result;
    }
    public void setResult (ResultGoogleAddressModel result) { this.result = result; }
    public String getStatus () { return status; }
    public void setStatus (String status) { this.status = status; }

    @Override
    public String toString()
    {
        return "ClassPojo [result = "+result+", status = "+status+"]";
    }
}
