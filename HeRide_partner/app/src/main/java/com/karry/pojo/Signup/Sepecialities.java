package com.karry.pojo.Signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by embed on 19/1/18.
 */

public class Sepecialities implements Serializable {

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("id")
    @Expose
    private String id;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
