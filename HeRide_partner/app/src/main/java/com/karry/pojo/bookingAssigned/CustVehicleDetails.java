package com.karry.pojo.bookingAssigned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by embed on 3/1/19.
 */

public class CustVehicleDetails implements Serializable {


    @SerializedName("year")
    @Expose
    private String year;

    @SerializedName("make")
    @Expose
    private String make;

    @SerializedName("model")
    @Expose
    private String model;

    @SerializedName("color")
    @Expose
    private String color;


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
