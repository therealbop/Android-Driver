package com.karry.authentication.login.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LanguagesPojo implements Serializable{


    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private ArrayList<LanguagesList> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<LanguagesList> getData() {
        return data;
    }

    public void setData(ArrayList<LanguagesList> data) {
        this.data = data;
    }
}
