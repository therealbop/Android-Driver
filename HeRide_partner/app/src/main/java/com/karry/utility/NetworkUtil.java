package com.karry.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            Utility.printLog("VVVVV Wifi enabled");
            status = "Wifi enabled,1";
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            Utility.printLog("VVVV Mobile data enabled");
            status = "Mobile data enabled,1";
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            Utility.printLog("VVVV Not connected to Internet");
            status = "Not connected to Internet,0";
        } else {
            Utility.printLog("VVVV Not connected to Internet");
            status = "Not connected to Internet,0";
        }
        return status;
    }
}
