package com.karry.pojo.AreaZone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class AreaZoneDataZones implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("surgePrice")
    @Expose
    private String surgePrice;

    @SerializedName("paths")
    @Expose
    private ArrayList<AreaZoneDataZonesPath> paths;

    @SerializedName("isPrefredZone")
    @Expose
    private boolean isPrefredZone;

    @SerializedName("position")
    @Expose
    private int position;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<AreaZoneDataZonesPath> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<AreaZoneDataZonesPath> paths) {
        this.paths = paths;
    }

    public String getSurgePrice() {
        return surgePrice;
    }

    public void setSurgePrice(String surgePrice) {
        this.surgePrice = surgePrice;
    }

    public boolean isPrefredZone() {
        return isPrefredZone;
    }

    public void setPrefredZone(boolean prefredZone) {
        isPrefredZone = prefredZone;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
