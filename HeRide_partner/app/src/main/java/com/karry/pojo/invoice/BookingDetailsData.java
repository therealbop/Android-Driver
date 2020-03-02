package com.karry.pojo.invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 23/2/18.
 */

public class BookingDetailsData implements Serializable {

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

    @SerializedName("invoice")
    @Expose
    private BookingDetailsDataInvoice invoice;

    @SerializedName("paymentType")
    @Expose
    private String paymentType;

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

    @SerializedName("dropLatLong")
    @Expose
    private String dropLatLong;

    @SerializedName("dropAddress")
    @Expose
    private String dropAddress;

    @SerializedName("pickupAddress")
    @Expose
    private String pickupAddress;

    @SerializedName("currencySbl")
    @Expose
    private String currencySbl;

    @SerializedName("currencyAbbr")
    @Expose
    private String currencyAbbr;

    @SerializedName("pickupTime")
    @Expose
    private String pickupTime;

    @SerializedName("dropTime")
    @Expose
    private String dropTime;

    @SerializedName("pickupTimeStamp")
    @Expose
    private String pickupTimeStamp;

    @SerializedName("dropTimeStamp")
    @Expose
    private String dropTimeStamp;

    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;

    @SerializedName("isCorporateBooking")
    @Expose
    private boolean isCorporateBooking;

    @SerializedName("instituteName")
    @Expose
    private String instituteName;

    @SerializedName("travelAgentLogo")
    @Expose
    private String travelAgentLogo;

    @SerializedName("travelAgentName")
    @Expose
    private String travelAgentName;

    @SerializedName("isTravelAgentBooking")
    @Expose
    private boolean isTravelAgentBooking;

    @SerializedName("paymentMethod")
    @Expose
    private BookingDetailsDataPayment paymentMethod;


    @SerializedName("towTruckBooking")
    @Expose
    private boolean towTruckBooking;

    @SerializedName("towTruckBookingService")
    @Expose
    private String towTruckBookingService;

    @SerializedName("towTruckServices")
    @Expose
    private ArrayList<BookingDetailsDataTowTruckServices> towTruckServices;


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

    public BookingDetailsDataInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(BookingDetailsDataInvoice invoice) {
        this.invoice = invoice;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
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

    public String getCurrencySbl() {
        return currencySbl;
    }

    public void setCurrencySbl(String currencySbl) {
        this.currencySbl = currencySbl;
    }

    public String getCurrencyAbbr() {
        return currencyAbbr;
    }

    public void setCurrencyAbbr(String currencyAbbr) {
        this.currencyAbbr = currencyAbbr;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getDropTime() {
        return dropTime;
    }

    public void setDropTime(String dropTime) {
        this.dropTime = dropTime;
    }

    public String getPickupTimeStamp() {
        return pickupTimeStamp;
    }

    public void setPickupTimeStamp(String pickupTimeStamp) {
        this.pickupTimeStamp = pickupTimeStamp;
    }

    public String getDropTimeStamp() {
        return dropTimeStamp;
    }

    public void setDropTimeStamp(String dropTimeStamp) {
        this.dropTimeStamp = dropTimeStamp;
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

    public boolean isTowTruckBooking() {
        return towTruckBooking;
    }

    public void setTowTruckBooking(boolean towTruckBooking) {
        this.towTruckBooking = towTruckBooking;
    }

    public String getTowTruckBookingService() {
        return towTruckBookingService;
    }

    public void setTowTruckBookingService(String towTruckBookingService) {
        this.towTruckBookingService = towTruckBookingService;
    }

    public ArrayList<BookingDetailsDataTowTruckServices> getTowTruckServices() {
        return towTruckServices;
    }

    public void setTowTruckServices(ArrayList<BookingDetailsDataTowTruckServices> towTruckServices) {
        this.towTruckServices = towTruckServices;
    }

    public String getTravelAgentName() {
        return travelAgentName;
    }

    public void setTravelAgentName(String travelAgentName) {
        this.travelAgentName = travelAgentName;
    }

    public boolean isTravelAgentBooking() {
        return isTravelAgentBooking;
    }

    public void setTravelAgentBooking(boolean travelAgentBooking) {
        isTravelAgentBooking = travelAgentBooking;
    }

    public String getTravelAgentLogo() {
        return travelAgentLogo;
    }

    public void setTravelAgentLogo(String travelAgentLogo) {
        this.travelAgentLogo = travelAgentLogo;
    }
}
