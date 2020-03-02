package com.karry.pojo.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 3/2/18.
 */

public class ModelDataPojo implements Serializable {

    @SerializedName("data")
    @Expose
    private ArrayList<ModelData> data;

    public ArrayList<ModelData> getData() {
        return data;
    }

    public void setData(ArrayList<ModelData> data) {
        this.data = data;
    }
}
