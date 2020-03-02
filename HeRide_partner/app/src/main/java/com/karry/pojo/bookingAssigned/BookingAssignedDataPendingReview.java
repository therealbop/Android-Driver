package com.karry.pojo.bookingAssigned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class BookingAssignedDataPendingReview implements Serializable {

    @SerializedName("customerName")
    @Expose
    private String customerName;

    @SerializedName("bookingId")
    @Expose
    private String bookingId;

    @SerializedName("bookingDate")
    @Expose
    private String bookingDate;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("statusText")
    @Expose
    private String statusText;

    @SerializedName("customerPhone")
    @Expose
    private String customerPhone;

    @SerializedName("customerEmail")
    @Expose
    private String customerEmail;

    @SerializedName("serviceType")
    @Expose
    private String serviceType;

    @SerializedName("pickupLatLong")
    @Expose
    private String pickupLatLong;

    @SerializedName("isMeterBooking")
    @Expose
    private String isMeterBooking;

    @SerializedName("dropLatLong")
    @Expose
    private String dropLatLong;

    @SerializedName("dropAddress")
    @Expose
    private String dropAddress;

    @SerializedName("pickupAddress")
    @Expose
    private String pickupAddress;




    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPickupLatLong() {
        return pickupLatLong;
    }

    public void setPickupLatLong(String pickupLatLong) {
        this.pickupLatLong = pickupLatLong;
    }

    public String getIsMeterBooking() {
        return isMeterBooking;
    }

    public void setIsMeterBooking(String isMeterBooking) {
        this.isMeterBooking = isMeterBooking;
    }

    public String getDropLatLong() {
        return dropLatLong;
    }

    public void setDropLatLong(String dropLatLong) {
        this.dropLatLong = dropLatLong;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }
}
