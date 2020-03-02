package com.karry.side_screens.profile.profile_model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.karry.pojo.Signup.PreferencesList;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfileVehicleDetail implements Serializable
{

    @SerializedName("typeName")
    @Expose
    private String typeName;

    @SerializedName("colour")
    @Expose
    private String colour;

    @SerializedName("planName")
    @Expose
    private String planName;

    @SerializedName("year")
    @Expose
    private String year;

    @SerializedName("serviceName")
    @Expose
    private String serviceName;

    @SerializedName("make")
    @Expose
    private String make;

    @SerializedName("model")
    @Expose
    private String model;

    @SerializedName("plateNo")
    @Expose
    private String plateNo;

    @SerializedName("vehiclePreferencesArr")
    @Expose
    private ArrayList<PreferencesList> vehiclePreferencesArr;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTypeName ()
    {
        return typeName;
    }

    public void setTypeName (String typeName)
    {
        this.typeName = typeName;
    }

    public String getColour ()
    {
        return colour;
    }

    public void setColour (String colour)
    {
        this.colour = colour;
    }

    public String getPlanName ()
    {
        return planName;
    }

    public void setPlanName (String planName)
    {
        this.planName = planName;
    }

    public String getYear ()
    {
        return year;
    }

    public void setYear (String year)
    {
        this.year = year;
    }

    public String getMake ()
    {
        return make;
    }

    public void setMake (String make)
    {
        this.make = make;
    }

    public String getPlateNo ()
    {
        return plateNo;
    }

    public void setPlateNo (String plateNo)
    {
        this.plateNo = plateNo;
    }

    public ArrayList<PreferencesList> getVehiclePreferencesArr() {
        return vehiclePreferencesArr;
    }

    public void setVehiclePreferencesArr(ArrayList<PreferencesList> vehiclePreferencesArr) {
        this.vehiclePreferencesArr = vehiclePreferencesArr;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
