package com.karry.pojo.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by embed on 19/1/18.
 */

public class City implements Serializable {

    @SerializedName("selected")
    @Expose
    private boolean selected;

    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("city")
    @Expose
    private String city;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
