package com.karry.pojo.Signup;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PreferenceData implements Serializable{


    @SerializedName("vehiclePreferences")
    @Expose
    private ArrayList<PreferencesList> vehiclePreferences;

    @SerializedName("driverPreferencesArr")
    @Expose
    private ArrayList<PreferencesList> driverPreferencesArr;

    public ArrayList<PreferencesList> getVehiclePreferences() {
        return vehiclePreferences;
    }

    public void setVehiclePreferences(ArrayList<PreferencesList> vehiclePreferences) {
        this.vehiclePreferences = vehiclePreferences;
    }

    public ArrayList<PreferencesList> getDriverPreferencesArr() {
        return driverPreferencesArr;
    }

    public void setDriverPreferencesArr(ArrayList<PreferencesList> driverPreferencesArr) {
        this.driverPreferencesArr = driverPreferencesArr;
    }
}
