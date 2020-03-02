package com.karry.utility;

/**
 * Created by ads on 18/05/17.
 */

public enum AppConstants {

    getAptStatus_OnTheWay("6"),
    getAptStatus_Arrived("7"),
    getAptStatus_LoadedAndDelivery("8"),
    getAptStatus_reached_drop_loc("9"),
    getAptStatus_Unloaded("16"),
    getAptStatus_Completed("10");

    public String value;

    AppConstants(String value) {
        this.value = value;
    }

    public interface ACTION
    {
        String MAIN_ACTION = "com.app.foregroundservice.action.main";
        String STARTFOREGROUND_ACTION = "com.app.service.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.app.service.action.stopforeground";
        String PUSH_ACTION = "com.app.firebase.action";
    }

    public interface NOTIFICATION_ID
    {
        int FOREGROUND_SERVICE = 108;
    }

    public interface BookingStatus
    {
        String Accept="6";
        String Reject="3";
        String JourneyStarted="12";
        String ReachedAtLocation="13";
        String MQTTStataus = "14";
        String Done="15";

        String Online="3";
        String Offline="4";
        String busy = "5";
        String OnTheWay="6";
        String Arrived="7";
        String InActive="8";
        String started="9";
        String Completed="12";
    }

    public interface TowTrayService{

        String Fixed = "1";
        String Towing = "2";
        String FixedAndTowing = "3";
    }
}
