package com.karry.side_screens.profile.profile_model;



public class NewProfileData {

    private ProfileVehicleDetail vehicleDetail;

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
