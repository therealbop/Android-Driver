package com.karry.pojo.AreaZone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class AreaZoneData implements Serializable{

    @SerializedName("areaZones")
    @Expose
    private ArrayList<AreaZoneDataZones>  areaZones;

    public ArrayList<AreaZoneDataZones> getAreaZones() {
        return areaZones;
    }

    public void setAreaZones(ArrayList<AreaZoneDataZones> areaZones) {
        this.areaZones = areaZones;
    }
}
