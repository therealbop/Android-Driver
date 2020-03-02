package com.karry.pojo.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by embed on 19/1/18.
 */

public class Model implements Serializable {

    @SerializedName("selected")
    @Expose
    private boolean selected;

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("Makeid")
    @Expose
    private String Makeid;

    @SerializedName("_id")
    @Expose
    private String _id;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMakeid() {
        return Makeid;
    }

    public void setMakeid(String makeid) {
        Makeid = makeid;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
