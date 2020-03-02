package com.karry.pojo.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 19/1/18.
 */

public class MakeModelData implements Serializable {

    @SerializedName("selected")
    @Expose
    private boolean selected;

    @SerializedName("models")
    @Expose
    private ArrayList<Model> models;

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("count")
    @Expose
    private String count;

    @SerializedName("_id")
    @Expose
    private String _id;

    public ArrayList<Model> getModels() {
        return models;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setModels(ArrayList<Model> models) {
        this.models = models;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
