package com.karry.telecall.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewCallData implements Serializable {

    @SerializedName("action")
    @Expose
    private Integer action;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("userImage")
    @Expose
    private String userImage;

    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("callId")
    @Expose
    private String callId;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("room")
    @Expose
    private String room;

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

}
