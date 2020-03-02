package com.karry.authentication.signup.SignUpVehicle;

/**
 * Created by embed on 19/1/18.
 */

class SignupVehModel {

    private boolean checkPlate;

    private boolean checkCity;

    private boolean checkType;

    private boolean checkService;

    private boolean checkMake;

    private boolean checkModel;

    private boolean checkYear;

    private boolean checkColor;

    private boolean checkvehicleImage;


    public boolean isCheckPlate() {
        return checkPlate;
    }

    public void setCheckPlate(boolean checkPlate) {
        this.checkPlate = checkPlate;
    }


    public boolean isCheckCity() {
        return checkCity;
    }

    public void setCheckCity(boolean checkCity) {
        this.checkCity = checkCity;
    }

    public boolean isCheckType() {
        return checkType;
    }

    public boolean isCheckvehicleImage() {
        return checkvehicleImage;
    }

    public void setCheckvehicleImage(boolean checkvehicleImage) {
        this.checkvehicleImage = checkvehicleImage;
    }

    public void setCheckType(boolean checkType) {
        this.checkType = checkType;
    }

    public boolean isCheckService() {
        return checkService;
    }

    public void setCheckService(boolean checkService) {
        this.checkService = checkService;
    }

    public boolean isCheckMake() {
        return checkMake;
    }

    public void setCheckMake(boolean checkMake) {
        this.checkMake = checkMake;
    }

    public boolean isCheckModel() {
        return checkModel;
    }

    public void setCheckModel(boolean checkModel) {
        this.checkModel = checkModel;
    }

    public boolean isCheckYear() {
        return checkYear;
    }

    public void setCheckYear(boolean checkYear) {
        this.checkYear = checkYear;
    }

    public boolean isCheckColor() {
        return checkColor;
    }

    public void setCheckColor(boolean checkColor) {
        this.checkColor = checkColor;
    }
}
