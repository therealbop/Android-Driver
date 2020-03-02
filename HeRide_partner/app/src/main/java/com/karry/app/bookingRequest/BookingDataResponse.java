package com.karry.app.bookingRequest;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class BookingDataResponse implements Serializable {

    @SerializedName("distanceCalc")
    @Expose
    private String distanceCalc;

    @SerializedName("bookingDate")
    @Expose
    private String bookingDate;

    @SerializedName("bookingId")
    @Expose
    private String bookingId;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("serviceType")
    @Expose
    private String serviceType;

    @SerializedName("estimateTime")
    @Expose
    private String estimateTime;

    @SerializedName("receivers")
    @Expose
    private BookingDataResponseRec receivers;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("driverAcceptTime")
    @Expose
    private String driverAcceptTime;

    @SerializedName("typeName")
    @Expose
    private String typeName;

    @SerializedName("timeForDriverAck")
    @Expose
    private String timeForDriverAck;

    @SerializedName("serviceTypeText")
    @Expose
    private String serviceTypeText;

    @SerializedName("bookingTypeText")
    @Expose
    private String bookingTypeText;

    @SerializedName("dropAddress")
    @Expose
    private String dropAddress;

    @SerializedName("pickupAddress")
    @Expose
    private String pickupAddress;

    @SerializedName("numOfPassanger")
    @Expose
    private String numOfPassanger;

    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;

    @SerializedName("isCorporateBooking")
    @Expose
    private boolean isCorporateBooking;

    @SerializedName("instituteName")
    @Expose
    private String instituteName;

    @SerializedName("paymentType")
    @Expose
    private String paymentType;

    @SerializedName("pickupLat")
    @Expose
    private Double pickupLat;

    @SerializedName("pickupLong")
    @Expose
    private Double pickupLong;

    @SerializedName("dropLat")
    @Expose
    private Double dropLat;

    @SerializedName("dropLong")
    @Expose
    private Double dropLong;

    @SerializedName("areaZonePickupTitle")
    @Expose
    private String areaZonePickupTitle;

    @SerializedName("towTruckBooking")
    @Expose
    private boolean towTruckBooking;

    @SerializedName("towTruckServices")
    @Expose
    private ArrayList<TowTruckServices> towTruckServices;

    @SerializedName("towTruckBookingService")
    @Expose
    private String towTruckBookingService;


    @SerializedName("isRental")
    @Expose
    private boolean isRental;

    @SerializedName("isPartnerBooking")
    @Expose
    private boolean isPartnerBooking;

    @SerializedName("isTravelAgentBooking")
    @Expose
    private boolean isTravelAgentBooking;


    public String getAreaZonePickupTitle() {
        return areaZonePickupTitle;
    }

    public void setAreaZonePickupTitle(String areaZonePickupTitle) {
        this.areaZonePickupTitle = areaZonePickupTitle;
    }

    private BookingDataResponsePreference preference;

    public String getDistanceCalc() {
        return distanceCalc;
    }

    public void setDistanceCalc(String distanceCalc) {
        this.distanceCalc = distanceCalc;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public BookingDataResponseRec getReceivers() {
        return receivers;
    }

    public void setReceivers(BookingDataResponseRec receivers) {
        this.receivers = receivers;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDriverAcceptTime() {
        return driverAcceptTime;
    }

    public void setDriverAcceptTime(String driverAcceptTime) {
        this.driverAcceptTime = driverAcceptTime;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTimeForDriverAck() {
        return timeForDriverAck;
    }

    public void setTimeForDriverAck(String timeForDriverAck) {
        this.timeForDriverAck = timeForDriverAck;
    }

    public String getServiceTypeText() {
        return serviceTypeText;
    }

    public void setServiceTypeText(String serviceTypeText) {
        this.serviceTypeText = serviceTypeText;
    }

    public String getBookingTypeText() {
        return bookingTypeText;
    }

    public void setBookingTypeText(String bookingTypeText) {
        this.bookingTypeText = bookingTypeText;
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

    public String getNumOfPassanger() {
        return numOfPassanger;
    }

    public void setNumOfPassanger(String numOfPassanger) {
        this.numOfPassanger = numOfPassanger;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BookingDataResponsePreference getPreference() {
        return preference;
    }

    public void setPreference(BookingDataResponsePreference preference) {
        this.preference = preference;
    }

    public String getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(String estimateTime) {
        this.estimateTime = estimateTime;
    }


    public Double getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(Double pickupLat) {
        this.pickupLat = pickupLat;
    }

    public Double getPickupLong() {
        return pickupLong;
    }

    public void setPickupLong(Double pickupLong) {
        this.pickupLong = pickupLong;
    }

    public Double getDropLat() {
        return dropLat;
    }

    public void setDropLat(Double dropLat) {
        this.dropLat = dropLat;
    }

    public Double getDropLong() {
        return dropLong;
    }

    public void setDropLong(Double dropLong) {
        this.dropLong = dropLong;
    }

    public boolean isTowTruckBooking() {
        return towTruckBooking;
    }

    public void setTowTruckBooking(boolean towTruckBooking) {
        this.towTruckBooking = towTruckBooking;
    }

    public ArrayList<TowTruckServices> getTowTruckServices() {
        return towTruckServices;
    }

    public void setTowTruckServices(ArrayList<TowTruckServices> towTruckServices) {
        this.towTruckServices = towTruckServices;
    }

    public String getTowTruckBookingService() {
        return towTruckBookingService;
    }

    public void setTowTruckBookingService(String towTruckBookingService) {
        this.towTruckBookingService = towTruckBookingService;
    }

    public boolean isRental() {
        return isRental;
    }

    public void setRental(boolean rental) {
        isRental = rental;
    }

    public boolean isPartnerBooking() {
        return isPartnerBooking;
    }

    public void setPartnerBooking(boolean partnerBooking) {
        isPartnerBooking = partnerBooking;
    }

    public boolean isTravelAgentBooking() {
        return isTravelAgentBooking;
    }

    public void setTravelAgentBooking(boolean travelAgentBooking) {
        isTravelAgentBooking = travelAgentBooking;
    }
}
