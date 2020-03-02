package com.karry.app.bookingRequest;

import java.io.Serializable;


public class BookingDataResponsePreference implements Serializable {

    private String wheelChairSupport;

    private String boosterSeatSupport;

    private String quietRide;

    private String gender;

    private String extraBagSupport;

    private String bikeCarrier;

    public String getWheelChairSupport() {
        return wheelChairSupport;
    }

    public void setWheelChairSupport(String wheelChairSupport) {
        this.wheelChairSupport = wheelChairSupport;
    }

    public String getBoosterSeatSupport() {
        return boosterSeatSupport;
    }

    public void setBoosterSeatSupport(String boosterSeatSupport) {
        this.boosterSeatSupport = boosterSeatSupport;
    }

    public String getQuietRide() {
        return quietRide;
    }

    public void setQuietRide(String quietRide) {
        this.quietRide = quietRide;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getExtraBagSupport() {
        return extraBagSupport;
    }

    public void setExtraBagSupport(String extraBagSupport) {
        this.extraBagSupport = extraBagSupport;
    }

    public String getBikeCarrier() {
        return bikeCarrier;
    }

    public void setBikeCarrier(String bikeCarrier) {
        this.bikeCarrier = bikeCarrier;
    }
}
