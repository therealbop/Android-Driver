package com.karry.pojo.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 19/1/18.
 */

public class TypeAndSpecialitiesData implements Serializable {

    @SerializedName("selected")
    @Expose
    private boolean selected;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("services")
    @Expose
    private ArrayList<Services> services;

    @SerializedName("sepecialities")
    @Expose
    private ArrayList<Sepecialities> sepecialities;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<Services> getServices() {
        return services;
    }

    public void setServices(ArrayList<Services> services) {
        this.services = services;
    }

    public ArrayList<Sepecialities> getSepecialities() {
        return sepecialities;
    }

    public void setSepecialities(ArrayList<Sepecialities> sepecialities) {
        this.sepecialities = sepecialities;
    }
}
