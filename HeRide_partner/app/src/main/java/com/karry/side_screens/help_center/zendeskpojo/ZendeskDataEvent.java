package com.karry.side_screens.help_center.zendeskpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h>ZendeskDataEvent</h>
 * Created by Ali on 1/2/2018.
 */

public class ZendeskDataEvent implements Serializable
{
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("author_id")
    @Expose
    private String author_id;

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("timeStamp")
    @Expose
    private long timeStamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
