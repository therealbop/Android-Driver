package com.karry.authentication.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>SignUpData</h1>
 * <p>the data store which entered and validated dor SignUp</p>
 */

public class SignUpData implements Serializable {

    //Personal SignUp
    @SerializedName("fname")
    @Expose
    private String fname;

    @SerializedName("lname")
    @Expose
    private String lname;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("passwod")
    @Expose
    private String passwod;

    @SerializedName("referalCode")
    @Expose
    private String referalCode;

    @SerializedName("countryCode")
    @Expose
    private String countryCode;

    @SerializedName("profilePic")
    @Expose
    private String profilePic;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("gender")
    @Expose
    private String gender;

    //Vehicle SignUp
    @SerializedName("plateNo")
    @Expose
    private String plateNo;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("service")
    @Expose
    private String service;

    @SerializedName("make")
    @Expose
    private String make;

    @SerializedName("model")
    @Expose
    private String model;

    @SerializedName("year")
    @Expose
    private String year;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("vehiclePic")
    @Expose
    private String vehiclePic;

    @SerializedName("wheelChairSupport")
    @Expose
    private String wheelChairSupport;

    @SerializedName("boosterSeatSupport")
    @Expose
    private String boosterSeatSupport;

    @SerializedName("extraBagSupport")
    @Expose
    private String extraBagSupport;

    @SerializedName("bikeCarrierSupport")
    @Expose
    private String bikeCarrierSupport;

    //Document SignUp
    @SerializedName("licenceDoc")
    @Expose
    private String licenceDoc;

    @SerializedName("licenceDateExp")
    @Expose
    private String licenceDateExp;

    @SerializedName("vehiclereg")
    @Expose
    private String vehiclereg;

    @SerializedName("vehicleregDateExp")
    @Expose
    private String vehicleregDateExp;

    @SerializedName("vehicleInsurance")
    @Expose
    private String vehicleInsurance;

    @SerializedName("vehicleInsuranceDateExp")
    @Expose
    private String vehicleInsuranceDateExp;

    @SerializedName("policeClearence")
    @Expose
    private String policeClearence;

    @SerializedName("policeClearenceDate")
    @Expose
    private String policeClearenceDate;

    @SerializedName("inspectionReport")
    @Expose
    private String inspectionReport;

    @SerializedName("inspectionReportDate")
    @Expose
    private String inspectionReportDate;

    @SerializedName("goodsInsurance")
    @Expose
    private String goodsInsurance;

    @SerializedName("goodsInsuranceDateExp")
    @Expose
    private String goodsInsuranceDateExp;

    @SerializedName("childrenCard")
    @Expose
    private String childrenCard;

    @SerializedName("childrenCardDateExp")
    @Expose
    private String childrenCardDateExp;

    @SerializedName("bookingPreference")
    @Expose
    private String bookingPreference;

    @SerializedName("driverBookingPreference")
    @Expose
    private String driverBookingPreference;

    @SerializedName("vehicleBookingPreference")
    @Expose
    private String vehicleBookingPreference;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswod() {
        return passwod;
    }

    public void setPasswod(String passwod) {
        this.passwod = passwod;
    }

    public String getReferalCode() {
        return referalCode;
    }

    public void setReferalCode(String referalCode) {
        this.referalCode = referalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVehiclePic() {
        return vehiclePic;
    }

    public void setVehiclePic(String vehiclePic) {
        this.vehiclePic = vehiclePic;
    }

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

    public String getExtraBagSupport() {
        return extraBagSupport;
    }

    public void setExtraBagSupport(String extraBagSupport) {
        this.extraBagSupport = extraBagSupport;
    }

    public String getLicenceDoc() {
        return licenceDoc;
    }

    public void setLicenceDoc(String licenceDoc) {
        this.licenceDoc = licenceDoc;
    }

    public String getVehiclereg() {
        return vehiclereg;
    }

    public void setVehiclereg(String vehiclereg) {
        this.vehiclereg = vehiclereg;
    }

    public String getVehicleInsurance() {
        return vehicleInsurance;
    }

    public void setVehicleInsurance(String vehicleInsurance) {
        this.vehicleInsurance = vehicleInsurance;
    }

    public String getPoliceClearence() {
        return policeClearence;
    }

    public void setPoliceClearence(String policeClearence) {
        this.policeClearence = policeClearence;
    }

    public String getInspectionReport() {
        return inspectionReport;
    }

    public void setInspectionReport(String inspectionReport) {
        this.inspectionReport = inspectionReport;
    }

    public String getGoodsInsurance() {
        return goodsInsurance;
    }

    public void setGoodsInsurance(String goodsInsurance) {
        this.goodsInsurance = goodsInsurance;
    }

    public String getChildrenCard() {
        return childrenCard;
    }

    public void setChildrenCard(String childrenCard) {
        this.childrenCard = childrenCard;
    }

    public String getLicenceDateExp() {
        return licenceDateExp;
    }

    public void setLicenceDateExp(String licenceDateExp) {
        this.licenceDateExp = licenceDateExp;
    }

    public String getVehicleregDateExp() {
        return vehicleregDateExp;
    }

    public void setVehicleregDateExp(String vehicleregDateExp) {
        this.vehicleregDateExp = vehicleregDateExp;
    }

    public String getVehicleInsuranceDateExp() {
        return vehicleInsuranceDateExp;
    }

    public void setVehicleInsuranceDateExp(String vehicleInsuranceDateExp) {
        this.vehicleInsuranceDateExp = vehicleInsuranceDateExp;
    }

    public String getPoliceClearenceDate() {
        return policeClearenceDate;
    }

    public void setPoliceClearenceDate(String policeClearenceDate) {
        this.policeClearenceDate = policeClearenceDate;
    }

    public String getInspectionReportDate() {
        return inspectionReportDate;
    }

    public void setInspectionReportDate(String inspectionReportDate) {
        this.inspectionReportDate = inspectionReportDate;
    }

    public String getGoodsInsuranceDateExp() {
        return goodsInsuranceDateExp;
    }

    public void setGoodsInsuranceDateExp(String goodsInsuranceDateExp) {
        this.goodsInsuranceDateExp = goodsInsuranceDateExp;
    }

    public String getChildrenCardDateExp() {
        return childrenCardDateExp;
    }

    public void setChildrenCardDateExp(String childrenCardDateExp) {
        this.childrenCardDateExp = childrenCardDateExp;
    }

    public String getBikeCarrierSupport() {
        return bikeCarrierSupport;
    }

    public void setBikeCarrierSupport(String bikeCarrierSupport) {
        this.bikeCarrierSupport = bikeCarrierSupport;
    }

    public String getBookingPreference() {
        return bookingPreference;
    }

    public void setBookingPreference(String bookingPreference) {
        this.bookingPreference = bookingPreference;
    }

    public String getDriverBookingPreference() {
        return driverBookingPreference;
    }

    public void setDriverBookingPreference(String driverBookingPreference) {
        this.driverBookingPreference = driverBookingPreference;
    }

    public String getVehicleBookingPreference() {
        return vehicleBookingPreference;
    }

    public void setVehicleBookingPreference(String vehicleBookingPreference) {
        this.vehicleBookingPreference = vehicleBookingPreference;
    }
}
