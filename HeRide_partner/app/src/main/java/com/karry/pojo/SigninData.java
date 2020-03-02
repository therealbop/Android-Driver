package com.karry.pojo;

import java.io.Serializable;
import java.util.ArrayList;


public class SigninData implements Serializable{


    private String lastName;

    private String phone;

    private String countryCode;

    private String code;

    private String token;

    private String email;

    private String mqttTopic;

    private String fcmTopic;

    private String profilePic;

    private ArrayList<SigninDriverVehicle> vehicles;

    private String mid;

    private String firstName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMqttTopic() {
        return mqttTopic;
    }

    public void setMqttTopic(String mqttTopic) {
        this.mqttTopic = mqttTopic;
    }

    public String getFcmTopic() {
        return fcmTopic;
    }

    public void setFcmTopic(String fcmTopic) {
        this.fcmTopic = fcmTopic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public ArrayList<SigninDriverVehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<SigninDriverVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
