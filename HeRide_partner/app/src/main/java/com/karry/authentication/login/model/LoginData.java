package com.karry.authentication.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginData implements Serializable
{
    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("countryCode")
    @Expose
    private String countryCode;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("mqttTopic")
    @Expose
    private String mqttTopic;

    @SerializedName("fcmTopic")
    @Expose
    private String fcmTopic;

    @SerializedName("profilePic")
    @Expose
    private String profilePic;

    @SerializedName("vehicles")
    @Expose
    private ArrayList<VehiclesDetails> vehicles;

    @SerializedName("mid")
    @Expose
    private String mid;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("requester_id")
    @Expose
    private String requester_id;

    @SerializedName("referralCode")
    @Expose
    private String referralCode;


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

    @SerializedName("googleMapKeyTopic")
    @Expose
    private String googleMapKeyTopic;

    @SerializedName("call")
    @Expose
    private LoginCallData call;

    public String getLastName ()
    {
        return lastName;
    }

    public void setLastName (String lastName)
    {
        this.lastName = lastName;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getCountryCode ()
    {
        return countryCode;
    }

    public void setCountryCode (String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getToken ()
    {
        return token;
    }

    public void setToken (String token)
    {
        this.token = token;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getMqttTopic ()
    {
        return mqttTopic;
    }

    public void setMqttTopic (String mqttTopic)
    {
        this.mqttTopic = mqttTopic;
    }

    public String getFcmTopic ()
    {
        return fcmTopic;
    }

    public void setFcmTopic (String fcmTopic)
    {
        this.fcmTopic = fcmTopic;
    }

    public String getProfilePic ()
    {
        return profilePic;
    }

    public void setProfilePic (String profilePic)
    {
        this.profilePic = profilePic;
    }

    public ArrayList<VehiclesDetails> getVehicles ()
    {
        return vehicles;
    }

    public void setVehicles (ArrayList<VehiclesDetails> vehicles)
    {
        this.vehicles = vehicles;
    }

    public String getMid ()
    {
        return mid;
    }

    public void setMid (String mid)
    {
        this.mid = mid;
    }

    public String getFirstName ()
    {
        return firstName;
    }

    public void setFirstName (String firstName)
    {
        this.firstName = firstName;
    }

    public String getRequester_id ()
    {
        return requester_id;
    }

    public void setRequester_id (String requester_id)
    {
        this.requester_id = requester_id;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
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

    public LoginCallData getCall() {
        return call;
    }

    public void setCall(LoginCallData call) {
        this.call = call;
    }

    public String getGoogleMapKeyTopic() {
        return googleMapKeyTopic;
    }

    public void setGoogleMapKeyTopic(String googleMapKeyTopic) {
        this.googleMapKeyTopic = googleMapKeyTopic;
    }
}