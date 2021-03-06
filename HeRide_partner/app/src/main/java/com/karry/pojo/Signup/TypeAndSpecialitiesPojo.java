package com.karry.pojo.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 19/1/18.
 */

public class TypeAndSpecialitiesPojo implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private ArrayList<TypeAndSpecialitiesData> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<TypeAndSpecialitiesData> getData() {
        return data;
    }

    public void setData(ArrayList<TypeAndSpecialitiesData> data) {
        this.data = data;
    }
}
