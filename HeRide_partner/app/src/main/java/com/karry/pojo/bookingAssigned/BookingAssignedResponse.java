package com.karry.pojo.bookingAssigned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>BookingAssignedResponse</h1>
 * <p>the response of bookingsAssigned API in Home Fragment </p>
 */

public class BookingAssignedResponse implements Serializable{

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private BookingAssignedData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BookingAssignedData getData() {
        return data;
    }

    public void setData(BookingAssignedData data) {
        this.data = data;
    }
}
