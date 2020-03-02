package com.karry.side_screens.profile.profile_model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfileData implements Serializable {

    @SerializedName("vehicleDetail")
    @Expose
    private ProfileVehicleDetail vehicleDetail;

    @SerializedName("driverDetail")
    @Expose
    private ProfileDriverDetail driverDetail;


    public ProfileVehicleDetail getVehicleDetail ()
    {
        return vehicleDetail;
    }

    public void setVehicleDetail (ProfileVehicleDetail vehicleDetail)
    {
        this.vehicleDetail = vehicleDetail;
    }

    public ProfileDriverDetail getDriverDetail ()
    {
        return driverDetail;
    }

    public void setDriverDetail (ProfileDriverDetail driverDetail)
    {
        this.driverDetail = driverDetail;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [vehicleDetail = "+vehicleDetail+", driverDetail = "+driverDetail+"]";
    }
}
