package com.karry.pojo.bookingAssigned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>BookingAssignedDataDeliveryAppts</h1>
 * <p>the Delivery Appointment of Booking Assigned API Data </p>
 */

public class BookingAssignedDataDeliveryAppts implements Serializable {

    @SerializedName("vehicleMapIcon")
    @Expose
    private String vehicleMapIcon;

    @SerializedName("timeFee")
    @Expose
    private String timeFee;

    @SerializedName("currencySbl")
    @Expose
    private String currencySbl;

    @SerializedName("bookingId")
    @Expose
    private String bookingId;

    @SerializedName("timeFeeXMinute")
    @Expose
    private String timeFeeXMinute;

    @SerializedName("mileageAfterXMetric")
    @Expose
    private String mileageAfterXMetric;

    @SerializedName("areaZonePickupTitle")
    @Expose
    private String areaZonePickupTitle;

    @SerializedName("statusText")
    @Expose
    private String statusText;

    @SerializedName("customerEmail")
    @Expose
    private String customerEmail;

    @SerializedName("customerPhone")
    @Expose
    private String customerPhone;

    @SerializedName("pgCommission")
    @Expose
    private String pgCommission;

    @SerializedName("customerProfilePic")
    @Expose
    private String customerProfilePic;

    @SerializedName("pickupLatLong")
    @Expose
    private String pickupLatLong;

    @SerializedName("customerId")
    @Expose
    private String customerId;

    @SerializedName("mileageMetricUnit")
    @Expose
    private String mileageMetricUnit;

    @SerializedName("bookingDateTimestamp")
    @Expose
    private String bookingDateTimestamp;

    @SerializedName("baseFee")
    @Expose
    private String baseFee;

    @SerializedName("arrived")
    @Expose
    private String arrived;

    @SerializedName("someOne")
    @Expose
    private SomeOne someOne;

    @SerializedName("customerName")
    @Expose
    private String customerName;

    @SerializedName("mileageMetric")
    @Expose
    private String mileageMetric;

    @SerializedName("bookingDate")
    @Expose
    private String bookingDate;

    @SerializedName("journeyStart")
    @Expose
    private String journeyStart;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("paymentType")
    @Expose
    private String paymentType;

    @SerializedName("arrivedRadius")
    @Expose
    private String arrivedRadius;

    @SerializedName("serviceType")
    @Expose
    private String serviceType;

    @SerializedName("mileagePrice")
    @Expose
    private String mileagePrice;

    @SerializedName("isMeterBooking")
    @Expose
    private String isMeterBooking;

    @SerializedName("elapsedSeconds")
    @Expose
    private String elapsedSeconds;

    @SerializedName("minFee")
    @Expose
    private String minFee;

    @SerializedName("dropAddress")
    @Expose
    private String dropAddress;

    @SerializedName("dropLatLong")
    @Expose
    private String dropLatLong;

    @SerializedName("pickupAddress")
    @Expose
    private String pickupAddress;



    public String getVehicleMapIcon() {
        return vehicleMapIcon;
    }

    public void setVehicleMapIcon(String vehicleMapIcon) {
        this.vehicleMapIcon = vehicleMapIcon;
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

    public String getTimeFeeXMinute() {
        return timeFeeXMinute;
    }

    public void setTimeFeeXMinute(String timeFeeXMinute) {
        this.timeFeeXMinute = timeFeeXMinute;
    }

    public String getMileageAfterXMetric() {
        return mileageAfterXMetric;
    }

    public void setMileageAfterXMetric(String mileageAfterXMetric) {
        this.mileageAfterXMetric = mileageAfterXMetric;
    }

    public String getAreaZonePickupTitle() {
        return areaZonePickupTitle;
    }

    public void setAreaZonePickupTitle(String areaZonePickupTitle) {
        this.areaZonePickupTitle = areaZonePickupTitle;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerProfilePic() {
        return customerProfilePic;
    }

    public void setCustomerProfilePic(String customerProfilePic) {
        this.customerProfilePic = customerProfilePic;
    }

    public String getPickupLatLong() {
        return pickupLatLong;
    }

    public void setPickupLatLong(String pickupLatLong) {
        this.pickupLatLong = pickupLatLong;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMileageMetricUnit() {
        return mileageMetricUnit;
    }

    public void setMileageMetricUnit(String mileageMetricUnit) {
        this.mileageMetricUnit = mileageMetricUnit;
    }

    public String getBookingDateTimestamp() {
        return bookingDateTimestamp;
    }

    public void setBookingDateTimestamp(String bookingDateTimestamp) {
        this.bookingDateTimestamp = bookingDateTimestamp;
    }

    public String getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(String baseFee) {
        this.baseFee = baseFee;
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

    public void setSomeOne(SomeOne someOne) {
        this.someOne = someOne;
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

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getJourneyStart() {
        return journeyStart;
    }

    public void setJourneyStart(String journeyStart) {
        this.journeyStart = journeyStart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getArrivedRadius() {
        return arrivedRadius;
    }

    public void setArrivedRadius(String arrivedRadius) {
        this.arrivedRadius = arrivedRadius;
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

    public String getIsMeterBooking() {
        return isMeterBooking;
    }

    public void setIsMeterBooking(String isMeterBooking) {
        this.isMeterBooking = isMeterBooking;
    }

    public String getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(String elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public String getMinFee() {
        return minFee;
    }

    public void setMinFee(String minFee) {
        this.minFee = minFee;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }

    public String getDropLatLong() {
        return dropLatLong;
    }

    public void setDropLatLong(String dropLatLong) {
        this.dropLatLong = dropLatLong;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPgCommission() {
        return pgCommission;
    }

    public void setPgCommission(String pgCommission) {
        this.pgCommission = pgCommission;
    }
}
