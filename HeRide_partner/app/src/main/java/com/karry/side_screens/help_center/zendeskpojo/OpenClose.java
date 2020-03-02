package com.karry.side_screens.help_center.zendeskpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h>OpenClose</h>
 * Created by Ali on 12/30/2017.
 */

public class OpenClose implements Serializable
{
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("timeStamp")
    @Expose
    private long timeStamp;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("subject")
    @Expose
    private String subject;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("priority")
    @Expose
    private String priority;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("isFirst")
    @Expose
    private boolean isFirst = false;


    public OpenClose(int id, long timeStamp, String status, String subject, String type, String priority, String description) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.status = status;
        this.subject = subject;
        this.type = type;
        this.priority = priority;
        this.description = description;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
