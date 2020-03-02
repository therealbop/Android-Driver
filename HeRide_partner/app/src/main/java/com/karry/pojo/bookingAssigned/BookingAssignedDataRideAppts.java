package com.karry.pojo.bookingAssigned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.karry.app.bookingRequest.TowTruckServices;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>BookingAssigedDataRideAppts</h1>
 * <p>the Ride Appointment of Booking Assigned API Data </p>
 */

public class BookingAssignedDataRideAppts implements Serializable {

    @SerializedName("customerName")
    @Expose
    private String customerName;

    @SerializedName("mileageMetric")
    @Expose
    private String mileageMetric;

    @SerializedName("timeFee")
    @Expose
    private String timeFee;

    @SerializedName("currencySbl")
    @Expose
    private String currencySbl;

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

    @SerializedName("mileagePrice")
    @Expose
    private String mileagePrice;

    @SerializedName("meterBookingCreatedTimestamp")
    @Expose
    private String meterBookingCreatedTimestamp;

    @SerializedName("pickupLatLong")
    @Expose
    private String pickupLatLong;

    @SerializedName("isMeterBooking")
    @Expose
    private boolean isMeterBooking;

    @SerializedName("minFee")
    @Expose
    private String minFee;

    @SerializedName("elapsedSeconds")
    @Expose
    private String elapsedSeconds;

    @SerializedName("mileageMetricUnit")
    @Expose
    private String mileageMetricUnit;

    @SerializedName("baseFee")
    @Expose
    private String baseFee;

    @SerializedName("dropLatLong")
    @Expose
    private String dropLatLong;

    @SerializedName("dropAddress")
    @Expose
    private String dropAddress;

    @SerializedName("pickupAddress")
    @Expose
    private String pickupAddress;

    @SerializedName("vehicleMapIcon")
    @Expose
    private String vehicleMapIcon;

    @SerializedName("bookingStatus")
    @Expose
    private String bookingStatus;

    @SerializedName("mileageAfterXMetric")
    @Expose
    private String mileageAfterXMetric;

    @SerializedName("timeFeeXMinute")
    @Expose
    private String timeFeeXMinute;

    @SerializedName("journeyStart")
    @Expose
    private String journeyStart;

    @SerializedName("arrived")
    @Expose
    private String arrived;

    @SerializedName("customerId")
    @Expose
    private String customerId;

    @SerializedName("someOne")
    @Expose
    private SomeOne someOne;

    @SerializedName("arrivedRadius")
    @Expose
    private String arrivedRadius;

    @SerializedName("paymentType")
    @Expose
    private String paymentType;


    @SerializedName("towTruckBooking")
    @Expose
    private boolean towTruckBooking;

    @SerializedName("towTruckServices")
    @Expose
    private ArrayList<TowTruckServices> towTruckServices;

    @SerializedName("towTruckBookingService")
    @Expose
    private String towTruckBookingService;


    @SerializedName("towingVehicle")
    @Expose
    private boolean towingVehicle;


    @SerializedName("vehicleDetails")
    @Expose
    private CustVehicleDetails vehicleDetails;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMileageMetric() {
        return mileageMetric;
    }

    public void setMileageMetric(String mileageMetric) {
        this.mileageMetric = mileageMetric;
    }

    public String getTimeFee() {
        return timeFee;
    }

    public void setTimeFee(String timeFee) {
        this.timeFee = timeFee;
    }

    public String getCurrencySbl() {
        return currencySbl;
    }

    public void setCurrencySbl(String currencySbl) {
        this.currencySbl = currencySbl;
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

    public String getMileagePrice() {
        return mileagePrice;
    }

    public void setMileagePrice(String mileagePrice) {
        this.mileagePrice = mileagePrice;
    }

    public String getMeterBookingCreatedTimestamp() {
        return meterBookingCreatedTimestamp;
    }

    public void setMeterBookingCreatedTimestamp(String meterBookingCreatedTimestamp) {
        this.meterBookingCreatedTimestamp = meterBookingCreatedTimestamp;
    }

    public String getPickupLatLong() {
        return pickupLatLong;
    }

    public void setPickupLatLong(String pickupLatLong) {
        this.pickupLatLong = pickupLatLong;
    }

    public boolean isMeterBooking() {
        return isMeterBooking;
    }

    public void setMeterBooking(boolean meterBooking) {
        isMeterBooking = meterBooking;
    }

    public String getMinFee() {
        return minFee;
    }

    public void setMinFee(String minFee) {
        this.minFee = minFee;
    }

    public String getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(String elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public String getMileageMetricUnit() {
        return mileageMetricUnit;
    }

    public void setMileageMetricUnit(String mileageMetricUnit) {
        this.mileageMetricUnit = mileageMetricUnit;
    }

    public String getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(String baseFee) {
        this.baseFee = baseFee;
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

    public String getVehicleMapIcon() {
        return vehicleMapIcon;
    }

    public void setVehicleMapIcon(String vehicleMapIcon) {
        this.vehicleMapIcon = vehicleMapIcon;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getMileageAfterXMetric() {
        return mileageAfterXMetric;
    }

    public void setMileageAfterXMetric(String mileageAfterXMetric) {
        this.mileageAfterXMetric = mileageAfterXMetric;
    }

    public String getTimeFeeXMinute() {
        return timeFeeXMinute;
    }

    public void setTimeFeeXMinute(String timeFeeXMinute) {
        this.timeFeeXMinute = timeFeeXMinute;
    }

    public String getJourneyStart() {
        return journeyStart;
    }

    public void setJourneyStart(String journeyStart) {
        this.journeyStart = journeyStart;
    }

    public String getArrived() {
        return arrived;
    }

    public void setArrived(String arrived) {
        this.arrived = arrived;
    }

    public SomeOne getSomeOne() {
        return someOne;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setSomeOne(SomeOne someOne) {
        this.someOne = someOne;
    }

    public String getArrivedRadius() {
        return arrivedRadius;
    }

    public void setArrivedRadius(String arrivedRadius) {
        this.arrivedRadius = arrivedRadius;
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


    public boolean isTowingVehicle() {
        return towingVehicle;
    }

    public void setTowingVehicle(boolean towingVehicle) {
        this.towingVehicle = towingVehicle;
    }

    public CustVehicleDetails getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(CustVehicleDetails vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }
}
