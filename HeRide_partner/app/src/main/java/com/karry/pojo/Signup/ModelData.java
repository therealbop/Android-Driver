package com.karry.pojo.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by embed on 3/2/18.
 */

public class ModelData implements Serializable {

    @SerializedName("selected")
    @Expose
    private boolean selected;

    @SerializedName("modelName")
    @Expose
    private String modelName;

    @SerializedName("modelId")
    @Expose
    private String modelId;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
}
