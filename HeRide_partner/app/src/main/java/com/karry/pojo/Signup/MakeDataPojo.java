package com.karry.pojo.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 3/2/18.
 */

public class MakeDataPojo  implements Serializable{

    @SerializedName("data")
    @Expose
    private ArrayList<MakeData> data;

    public ArrayList<MakeData> getData() {
        return data;
    }

    public void setData(ArrayList<MakeData> data) {
        this.data = data;
    }
}
