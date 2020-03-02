package com.karry.telecall.utility;

import com.karry.data.source.local.PreferenceHelperDataSource;

import java.util.ArrayList;
import java.util.Random;

public class CallHelper {
    private boolean callMinimized = false;
    private boolean firstTimeAfterCallMinimized = false;
    private String activeCallId, activeCallerId;
    private boolean activeOnACall = false;

    private ArrayList<String> colors;

    private PreferenceHelperDataSource preferenceHelperDataSource;;

    public CallHelper(PreferenceHelperDataSource preferenceHelperDataSource) {
        this.preferenceHelperDataSource = preferenceHelperDataSource;
        setBackgroundColorArray();
    }

    public boolean isCallMinimized() {
        return callMinimized;
    }

    public void setCallMinimized(boolean callMinimized) {
        this.callMinimized = callMinimized;
    }

    public boolean isActiveOnACall() {
        return activeOnACall;
    }

    public void setActiveOnACall(boolean activeOnACall, boolean notCallCut) {
        this.activeOnACall = activeOnACall;
        if (!activeOnACall && notCallCut) {
            this.callMinimized = false;
        }
    }

    public boolean isFirstTimeAfterCallMinimized() {
        return firstTimeAfterCallMinimized;
    }

    public void setFirstTimeAfterCallMinimized(boolean firstTimeAfterCallMinimized) {
        this.firstTimeAfterCallMinimized = firstTimeAfterCallMinimized;
    }

    public void setActiveCallId(String activeCallId) {
        this.activeCallId = activeCallId;
    }

    public void setActiveCallerId(String activeCallerId) {
        this.activeCallerId = activeCallerId;
    }

    public String getUserId()
    {
        return preferenceHelperDataSource.getDriverID();
    }

    public String getUserName() {
        return preferenceHelperDataSource.getUserName();
    }

    public String getUserImageUrl() {
        return preferenceHelperDataSource.getProfilePic();
    }

    public String getUserIdentifier() {
        return preferenceHelperDataSource.getUserPhoneNumber();
    }

    public String getContactName() {
        return preferenceHelperDataSource.getCustomerName();
    }

    public String getContactUrl() {
        return preferenceHelperDataSource.getCustomerPic();
    }

    public String randomString() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }


        sb.append("PnPLabs3Embed");
        return sb.toString();
    }

    public String getBookingId()
    {
        return preferenceHelperDataSource.getBookingId();
    }

    private void setBackgroundColorArray() {

        colors = new ArrayList<>();

        colors.add("#FFCDD2");
        colors.add("#D1C4E9");
        colors.add("#B3E5FC");
        colors.add("#C8E6C9");
        colors.add("#FFF9C4");
        colors.add("#FFCCBC");
        colors.add("#CFD8DC");
        colors.add("#F8BBD0");
        colors.add("#C5CAE9");
        colors.add("#B2EBF2");
        colors.add("#DCEDC8");
        colors.add("#FFECB3");
        colors.add("#D7CCC8");
        colors.add("#F5F5F5");
        colors.add("#FFE0B2");
        colors.add("#F0F4C3");
        colors.add("#B2DFDB");
        colors.add("#BBDEFB");
        colors.add("#E1BEE7");
    }

    public String getColorCode(int position) {
        return colors.get(position);
    }

}
