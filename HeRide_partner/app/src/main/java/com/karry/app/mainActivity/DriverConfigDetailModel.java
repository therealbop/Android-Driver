package com.karry.app.mainActivity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DriverConfigDetailModel implements Serializable {

    @SerializedName("driverApiDistanceWhenFree")
    @Expose
    private String driverApiDistanceWhenFree;


    @SerializedName("driverApiIntervalWhenBusy")
    @Expose
    private String driverApiIntervalWhenBusy;


    @SerializedName("meterBookingCalculateTimeXSeconds")
    @Expose
    private String meterBookingCalculateTimeXSeconds;

    @SerializedName("DistanceForLogingLatLongs")
    @Expose
    private String DistanceForLogingLatLongs;

    @SerializedName("appVersion")
    @Expose
    private String appVersion;


    @SerializedName("DistanceForLogingLatLongsMax")
    @Expose
    private String DistanceForLogingLatLongsMax;

    @SerializedName("presenceTime")
    @Expose
    private String presenceTime;

    @SerializedName("mandatory")
    @Expose
    private boolean mandatory;

    @SerializedName("isHelpCenterEnable")
    @Expose
    private boolean isHelpCenterEnable;

    @SerializedName("isChatModuleEnable")
    @Expose
    private boolean isChatModuleEnable;

    @SerializedName("meterBookingCalculateDistanceXMeters")
    @Expose
    private String meterBookingCalculateDistanceXMeters;

    @SerializedName("pushTopics")
    @Expose
    private String[] pushTopics;

    public String getDistanceForLogingLatLongsMax() {
        return DistanceForLogingLatLongsMax;
    }

    @SerializedName("driverApiIntervalWhenFree")
    @Expose
    private String driverApiIntervalWhenFree;

    @SerializedName("stripePublishKey")
    @Expose
    private String stripePublishKey;

    @SerializedName("isMandatoryUpdateEnable")
    @Expose
    private boolean isMandatoryUpdateEnable;

    @SerializedName("isTwillioCallingEnable")
    @Expose
    private boolean isTwillioCallingEnable;

    @SerializedName("driverGoogleMapKeys")
    @Expose
    private ArrayList<String> driverGoogleMapKeys;

    @SerializedName("mqttUserName")
    @Expose
    private String mqttUserName;

    @SerializedName("mqttPassword")
    @Expose
    private String mqttPassword;

    @SerializedName("mqttHost")
    @Expose
    private String mqttHost;

    @SerializedName("mqttPort")
    @Expose
    private String mqttPort;

    @SerializedName("googleMapKey")
    @Expose
    private String googleMapKey;


    public ArrayList<String> getDriverGoogleMapKeys() {
        return driverGoogleMapKeys;
    }

    public void setDriverGoogleMapKeys(ArrayList<String> driverGoogleMapKeys) {
        this.driverGoogleMapKeys = driverGoogleMapKeys;
    }

    public boolean isTwillioCallingEnable() {
        return isTwillioCallingEnable;
    }

    public void setTwillioCallingEnable(boolean twillioCallingEnable) {
        isTwillioCallingEnable = twillioCallingEnable;
    }

    public String getStripePublishKey() {
        return stripePublishKey;
    }

    public void setStripePublishKey(String stripePublishKey) {
        this.stripePublishKey = stripePublishKey;
    }

    public String getDriverApiIntervalWhenBusy() {
        return driverApiIntervalWhenBusy;
    }

    public void setDriverApiIntervalWhenBusy(String driverApiIntervalWhenBusy) {
        this.driverApiIntervalWhenBusy = driverApiIntervalWhenBusy;
    }

    public String getMeterBookingCalculateTimeXSeconds() {
        return meterBookingCalculateTimeXSeconds;
    }

    public void setMeterBookingCalculateTimeXSeconds(String meterBookingCalculateTimeXSeconds) {
        this.meterBookingCalculateTimeXSeconds = meterBookingCalculateTimeXSeconds;
    }

    public String getDistanceForLogingLatLongs() {
        return DistanceForLogingLatLongs;
    }

    public void setDistanceForLogingLatLongs(String DistanceForLogingLatLongs) {
        this.DistanceForLogingLatLongs = DistanceForLogingLatLongs;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPresenceTime() {
        return presenceTime;
    }

    public void setPresenceTime(String presenceTime) {
        this.presenceTime = presenceTime;
    }

    public boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getMeterBookingCalculateDistanceXMeters() {
        return meterBookingCalculateDistanceXMeters;
    }

    public void setMeterBookingCalculateDistanceXMeters(String meterBookingCalculateDistanceXMeters) {
        this.meterBookingCalculateDistanceXMeters = meterBookingCalculateDistanceXMeters;
    }

    public String[] getPushTopics() {
        return pushTopics;
    }

    public void setPushTopics(String[] pushTopics) {
        this.pushTopics = pushTopics;
    }

    public String getDriverApiIntervalWhenFree() {
        return driverApiIntervalWhenFree;
    }

    public void setDriverApiIntervalWhenFree(String driverApiIntervalWhenFree) {
        this.driverApiIntervalWhenFree = driverApiIntervalWhenFree;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isMandatoryUpdateEnable() {
        return isMandatoryUpdateEnable;
    }

    public void setMandatoryUpdateEnable(boolean mandatoryUpdateEnable) {
        isMandatoryUpdateEnable = mandatoryUpdateEnable;
    }

    public boolean isHelpCenterEnable() {
        return isHelpCenterEnable;
    }

    public void setHelpCenterEnable(boolean helpCenterEnable) {
        isHelpCenterEnable = helpCenterEnable;
    }

    public boolean isChatModuleEnable() {
        return isChatModuleEnable;
    }

    public void setChatModuleEnable(boolean chatModuleEnable) {
        isChatModuleEnable = chatModuleEnable;
    }

    public String getMqttUserName() {
        return mqttUserName;
    }

    public void setMqttUserName(String mqttUserName) {
        this.mqttUserName = mqttUserName;
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword;
    }

    public String getMqttHost() {
        return mqttHost;
    }

    public void setMqttHost(String mqttHost) {
        this.mqttHost = mqttHost;
    }

    public String getMqttPort() {
        return mqttPort;
    }

    public void setMqttPort(String mqttPort) {
        this.mqttPort = mqttPort;
    }

    public String getGoogleMapKey() {
        return googleMapKey;
    }

    public void setGoogleMapKey(String googleMapKey) {
        this.googleMapKey = googleMapKey;
    }

    public String getDriverApiDistanceWhenFree() {
        return driverApiDistanceWhenFree;
    }
}
