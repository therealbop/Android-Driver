package com.karry.pojo.TripsPojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 23/5/17.
 */

public class Appointments implements Serializable {

    private String customerName;

    private String dropLine1;

    private String status;

    private String vehicleImg;

    private String booking_time;

    private String customerPhone;

    private String customerEmail;

    private String extraNotes;

    private String drop_ltg;

    private String statusCode;

    private String addrLine1;

    private String drop_dt;

    private String DriverChn;

    private String apntDt;

    private String email;

    private String dorpzoneId;

    private String vehicleType;

    private String bid;

    private ArrayList<ShipmentDetails> shipemntDetails;

    private String pickup_ltg;

    private String apntDate;

    private String rating;

    private Invoice invoice;

    private String paymentType;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDropLine1() {
        return dropLine1;
    }

    public void setDropLine1(String dropLine1) {
        this.dropLine1 = dropLine1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVehicleImg() {
        return vehicleImg;
    }

    public void setVehicleImg(String vehicleImg) {
        this.vehicleImg = vehicleImg;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
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

    public String getExtraNotes() {
        return extraNotes;
    }

    public void setExtraNotes(String extraNotes) {
        this.extraNotes = extraNotes;
    }

    public String getDrop_ltg() {
        return drop_ltg;
    }

    public void setDrop_ltg(String drop_ltg) {
        this.drop_ltg = drop_ltg;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getAddrLine1() {
        return addrLine1;
    }

    public void setAddrLine1(String addrLine1) {
        this.addrLine1 = addrLine1;
    }

    public String getDrop_dt() {
        return drop_dt;
    }

    public void setDrop_dt(String drop_dt) {
        this.drop_dt = drop_dt;
    }

    public String getDriverChn() {
        return DriverChn;
    }

    public void setDriverChn(String driverChn) {
        DriverChn = driverChn;
    }

    public String getApntDt() {
        return apntDt;
    }

    public void setApntDt(String apntDt) {
        this.apntDt = apntDt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDorpzoneId() {
        return dorpzoneId;
    }

    public void setDorpzoneId(String dorpzoneId) {
        this.dorpzoneId = dorpzoneId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public ArrayList<ShipmentDetails> getShipemntDetails() {
        return shipemntDetails;
    }

    public void setShipemntDetails(ArrayList<ShipmentDetails> shipemntDetails) {
        this.shipemntDetails = shipemntDetails;
    }

    public String getPickup_ltg() {
        return pickup_ltg;
    }

    public void setPickup_ltg(String pickup_ltg) {
        this.pickup_ltg = pickup_ltg;
    }

    public String getApntDate() {
        return apntDate;
    }

    public void setApntDate(String apntDate) {
        this.apntDate = apntDate;
    }
}
