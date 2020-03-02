package com.karry.mqttChat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h>ChatData</h>
 * Created by Ali on 12/22/2017.
 */

public class ChatData implements Serializable
{

    @SerializedName("bid")
    @Expose
    private long bid;

    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("fromID")
    @Expose
    private String fromID;

    @SerializedName("targetId")
    @Expose
    private String targetId;

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("custProType")
    @Expose
    private int custProType;


    public int getCustProType() {
        return custProType;
    }

    public void setCustProType(int custProType) {
        this.custProType = custProType;
    }

    public long getBid() {
        return bid;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
