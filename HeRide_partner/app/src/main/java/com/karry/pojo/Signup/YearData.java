package com.karry.pojo.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 31/1/18.
 */

public class YearData implements Serializable {

    @SerializedName("data")
    @Expose
    private ArrayList<String> data;

    @SerializedName("year")
    @Expose
    private String year;

    @SerializedName("selected")
    @Expose
    private boolean selected;

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
