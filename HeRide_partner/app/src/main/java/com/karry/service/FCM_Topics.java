package com.karry.service;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class FCM_Topics  implements Serializable{

    @SerializedName("fcmTopics")
    @Expose
    private ArrayList<String> fcmTopics;

    public ArrayList<String> getFcmTopics() {
        return fcmTopics;
    }

    public void setFcmTopics(ArrayList<String> fcmTopics) {
        this.fcmTopics = fcmTopics;
    }
}
