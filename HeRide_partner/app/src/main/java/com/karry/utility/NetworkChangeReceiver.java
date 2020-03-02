package com.karry.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.app.NotificationCompat;

import com.karry.app.mainActivity.MainActivity;
import com.heride.partner.R;


public class NetworkChangeReceiver extends BroadcastReceiver {
    public static MyNetworkChangeListner myNetworkChangeListner ;
    private Notification notification;
    private SessionManager sessionManager;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Utility.printLog("PK" + " onRecieve  " + intent.getAction());
        String status = NetworkUtil.getConnectivityStatusString(context);

        String[] networkStatus = status.split(",");
        sessionManager = new SessionManager(context);
        //Toast.makeText(context, networkStatus[0], Toast.LENGTH_LONG).show();

        Intent homeIntent = new Intent("com.app.driverapp.internetStatus");
        homeIntent.putExtra("STATUS", networkStatus[1]);
        context.sendBroadcast(homeIntent);
        Utility.printLog("PK Network Status" + status);
        if (myNetworkChangeListner != null) {
            myNetworkChangeListner.onNetworkStateChanges(status);
        }

        if (!"1".equals(networkStatus[1])) {
//            sendNotification(context, networkStatus[1]);
        }
    }

    public void setMyNetworkChangeListner(MyNetworkChangeListner myNetworkChangeListner) {
        NetworkChangeReceiver.myNetworkChangeListner = myNetworkChangeListner;
    }

    private void sendNotification(Context context, String staus) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String title = context.getString(R.string.app_name);

        Intent notificationIntent;
        notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_launcher);

        //Assign inbox style notification
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Alert");
        bigText.setBigContentTitle(title);
        //bigText.setSummaryText("Alert");

        //build notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentIntent(intent)
                        .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                        .setPriority(NotificationCompat.PRIORITY_MAX) //must give priority to High, Max which will considered as heads-up notification)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setContentText("")
                        .setLargeIcon(icon1)
                        .setStyle(bigText);


        if ("1".equals(staus)) {
            mBuilder.setContentText("Internet connected.");
            /*notification = new Notification(icon, "Internet connected", when);
            notification.setLatestEventInfo(context, title, "Internet connected", intent);*/
        } else {
            mBuilder.setContentText("No network connection found.");
            /*notification = new Notification(icon, "No network connection found.", when);
            notification.setLatestEventInfo(context, title, "No network connection found.", intent);*/
        }


        // Gets an instance of the NotificationManager service
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0 /* ID of notification */, mBuilder.build());

    }

}
