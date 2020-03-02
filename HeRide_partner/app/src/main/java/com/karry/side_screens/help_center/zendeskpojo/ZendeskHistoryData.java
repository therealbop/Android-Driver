package com.karry.side_screens.help_center.zendeskpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h>ZendeskHistoryData</h>
 * Created by Ali on 1/2/2018.
 */

public class ZendeskHistoryData implements Serializable
{

    @SerializedName("ticket_id")
    @Expose
    private int ticket_id;

    @SerializedName("timeStamp")
    @Expose
    private long timeStamp;

    @SerializedName("subject")
    @Expose
    private String subject;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("priority")
    @Expose
    private String priority;

    @SerializedName("events")
    @Expose
    private ArrayList<ZendeskDataEvent>events;

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
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

    public ArrayList<ZendeskDataEvent> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<ZendeskDataEvent> events) {
        this.events = events;
    }



}
