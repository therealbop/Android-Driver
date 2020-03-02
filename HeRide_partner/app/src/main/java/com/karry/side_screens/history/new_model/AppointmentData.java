package com.karry.side_screens.history.new_model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.karry.pojo.invoice.BookingDetailsDataPayment;

import java.io.Serializable;

public class AppointmentData implements Serializable {


    @SerializedName("customerName")
    @Expose
    private String customerName;

    @SerializedName("bookingId")
    @Expose
    private String bookingId;

    @SerializedName("bookingDate")
    @Expose
    private String bookingDate;

    @SerializedName("currencySbl")
    @Expose
    private String currencySbl;

    @SerializedName("currencyAbbr")
    @Expose
    private String currencyAbbr;

    @SerializedName("dropTime")
    @Expose
    private String dropTime;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("areaZonePickupTitle")
    @Expose
    private String areaZonePickupTitle;

    @SerializedName("invoice")
    @Expose
    private HistoryInvoice invoice;

    @SerializedName("pickupTime")
    @Expose
    private String pickupTime;

    @SerializedName("statusText")
    @Expose
    private String statusText;

    @SerializedName("customerPhone")
    @Expose
    private String customerPhone;

    @SerializedName("dropAddress")
    @Expose
    private String dropAddress;

    @SerializedName("someOne")
    @Expose
    private SomeOneData someOne;

    @SerializedName("pickupAddress")
    @Expose
    private String pickupAddress;

    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;

    @SerializedName("isCorporateBooking")
    @Expose
    private boolean isCorporateBooking;

    @SerializedName("paymentMethod")
    @Expose
    private BookingDetailsDataPayment paymentMethod;

    @SerializedName("instituteName")
    @Expose
    private String instituteName;

    public String getCurrencyAbbr() {
        return currencyAbbr;
    }

    public void setCurrencyAbbr(String currencyAbbr) {
        this.currencyAbbr = currencyAbbr;
    }

    public String getCustomerName ()
    {
        return customerName;
    }

    public void setCustomerName (String customerName)
    {
        this.customerName = customerName;
    }

    public String getBookingId ()
    {
        return bookingId;
    }

    public void setBookingId (String bookingId)
    {
        this.bookingId = bookingId;
    }

    public String getBookingDate ()
    {
        return bookingDate;
    }

    public void setBookingDate (String bookingDate)
    {
        this.bookingDate = bookingDate;
    }

    public String getCurrencySbl ()
    {
        return currencySbl;
    }

    public void setCurrencySbl (String currencySbl)
    {
        this.currencySbl = currencySbl;
    }

    public String getDropTime ()
    {
        return dropTime;
    }

    public void setDropTime (String dropTime)
    {
        this.dropTime = dropTime;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getAreaZonePickupTitle ()
    {
        return areaZonePickupTitle;
    }

    public void setAreaZonePickupTitle (String areaZonePickupTitle)
    {
        this.areaZonePickupTitle = areaZonePickupTitle;
    }

    public HistoryInvoice getInvoice ()
    {
        return invoice;
    }

    public void setInvoice (HistoryInvoice invoice)
    {
        this.invoice = invoice;
    }

    public String getPickupTime ()
    {
        return pickupTime;
    }

    public void setPickupTime (String pickupTime)
    {
        this.pickupTime = pickupTime;
    }

    public String getStatusText ()
    {
        return statusText;
    }

    public void setStatusText (String statusText)
    {
        this.statusText = statusText;
    }

    public String getCustomerPhone ()
    {
        return customerPhone;
    }

    public void setCustomerPhone (String customerPhone)
    {
        this.customerPhone = customerPhone;
    }

    public String getDropAddress ()
    {
        return dropAddress;
    }

    public void setDropAddress (String dropAddress)
    {
        this.dropAddress = dropAddress;
    }

    public SomeOneData getSomeOne ()
    {
        return someOne;
    }

    public void setSomeOne (SomeOneData someOne)
    {
        this.someOne = someOne;
    }

    public String getPickupAddress ()
    {
        return pickupAddress;
    }

    public void setPickupAddress (String pickupAddress)
    {
        this.pickupAddress = pickupAddress;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isCorporateBooking() {
        return isCorporateBooking;
    }

    public void setCorporateBooking(boolean corporateBooking) {
        isCorporateBooking = corporateBooking;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public BookingDetailsDataPayment getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(BookingDetailsDataPayment paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
