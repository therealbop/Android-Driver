package com.karry.authentication.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginCallData implements Serializable {

    @SerializedName("willTopic")
    @Expose
    private String willTopic;

    @SerializedName("authToken")
    @Expose
    private String authToken;

    public String getWillTopic() {
        return willTopic;
    }

    public void setWillTopic(String willTopic) {
        this.willTopic = willTopic;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
