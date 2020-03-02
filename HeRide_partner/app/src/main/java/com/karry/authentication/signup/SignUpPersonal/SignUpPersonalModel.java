package com.karry.authentication.signup.SignUpPersonal;

/**
 * Created by embed on 18/1/18.
 */

class SignUpPersonalModel {

    private boolean fname=false;
    private boolean phone=false;
    private boolean email=false;
    private boolean password=false;
    private boolean gender=false;
    private boolean state=false;
    private boolean refCode=false;

    public boolean isImageUploaded() {
        return isImageUploaded;
    }

    public void setImageUploaded(boolean imageUploaded) {
        isImageUploaded = imageUploaded;
    }

    private boolean isImageUploaded;

    public boolean isFname() {
        return fname;
    }

    public void setFname(boolean fname) {
        this.fname = fname;
    }

    public boolean isPhone() {
        return phone;
    }

    public void setPhone(boolean phone) {
        this.phone = phone;
    }

    public boolean isEmail() {
        return email;
    }


    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isRefCode() {
        return refCode;
    }

    public void setRefCode(boolean refCode) {
        this.refCode = refCode;
    }
}
