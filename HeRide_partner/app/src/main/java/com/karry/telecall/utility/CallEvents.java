package com.karry.telecall.utility;


public enum CallEvents {

    TurnOnScreen("turnOnScreen"),

    TurnOffScreen("turnOffScreen"),

    CancelCall("cancelCall");

    public String value;

    CallEvents(String value) {
        this.value = value;
    }


}