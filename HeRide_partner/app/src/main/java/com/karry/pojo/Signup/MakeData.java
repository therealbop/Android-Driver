package com.karry.pojo.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by embed on 3/2/18.
 */

public class MakeData implements Serializable {

    @SerializedName("makeName")
    @Expose
    private String makeName;

    @SerializedName("makeId")
    @Expose
    private String makeId;

    @SerializedName("selected")
    @Expose
    private boolean selected;

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public String getMakeId() {
        return makeId;
    }

    public void setMakeId(String makeId) {
        this.makeId = makeId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
