package com.karry.pojo.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>PreferencesList</h1>
 * <p>data of the preferences</p>
 */

public class PreferencesList implements Serializable  {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("fees")
    @Expose
    private String fees;

    @SerializedName("selected")
    @Expose
    private boolean selected;

    @SerializedName("preferenceFor")
    @Expose
    private int preferenceFor;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public int getPreferenceFor() {
        return preferenceFor;
    }

    public void setPreferenceFor(int preferenceFor) {
        this.preferenceFor = preferenceFor;
    }
}
