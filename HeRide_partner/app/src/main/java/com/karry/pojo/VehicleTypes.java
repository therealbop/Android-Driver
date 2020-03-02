package com.karry.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class VehicleTypes implements Serializable{


    @SerializedName("vehicleTyps")
    @Expose
    private ArrayList<VehicleTypeList> vehicleTyps;


    public ArrayList<VehicleTypeList> getVehicleTyps() {
        return vehicleTyps;
    }

    public void setVehicleTyps(ArrayList<VehicleTypeList> vehicleTyps) {
        this.vehicleTyps = vehicleTyps;
    }
}
