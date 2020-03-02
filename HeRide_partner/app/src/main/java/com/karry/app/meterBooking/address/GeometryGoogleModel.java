package com.karry.app.meterBooking.address;

import com.karry.app.bookingRequest.eta_model.Location;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>GeometryGoogleModel</h1>
 * used to hold geometry data model
 * @author embed on 27/3/17.
 */
public class GeometryGoogleModel implements Serializable
{
    @SerializedName("location")
    @Expose
    private Location location;

    public Location getLocation ()
    {
        return location;
    }
    public void setLocation (Location location)
    {
        this.location = location;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [viewport = "+", location = "+location+"]";
    }
}
