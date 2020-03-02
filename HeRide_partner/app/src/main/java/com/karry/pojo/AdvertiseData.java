package com.karry.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdvertiseData implements Serializable
{
    @SerializedName("title")
    @Expose
    private String title;


    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("knowMoreUrl")
    @Expose
    private String knowMoreUrl;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("messageId")
    @Expose
    private String messageId;

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getKnowMoreUrl() {
        return knowMoreUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setKnowMoreUrl(String knowMoreUrl) {
        this.knowMoreUrl = knowMoreUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
