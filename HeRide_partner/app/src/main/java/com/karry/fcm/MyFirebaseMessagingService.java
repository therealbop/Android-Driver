package com.karry.fcm;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.karry.app.adv.AdvertiseActivity;
import com.karry.app.mainActivity.MainActivity;
import com.karry.app.splash.SplashScreenActivity;
import com.heride.partner.R;
import com.karry.service.LocationUpdateService;
import com.karry.pojo.AdvertiseData;
import com.karry.utility.AppConstants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.karry.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.karry.utility.VariableConstant.ADVERTISE_DETAILS;
import static com.karry.utility.VariableConstant.IS_POP_UP_OPEN;
import static com.karry.utility.VariableConstant.PREVIOUS_BID;

/**
 * Created by embed on 16/9/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    JSONObject received;
    private String message,bid;
    private int action=-1;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            try {
                received = new JSONObject(remoteMessage.getData());

                if(received.has("a")){
                    action = received.getInt("a");
                }
                if(received.has("action")){
                    action = received.getInt("action");
                }
                if(received.has("msg")){
                    message = received.getString("msg");
                }
                if(received.has("message")){
                    message = received.getString("message");
                }
                if(received.has("body")){
                    message = received.getString("body");
                }
                if(received.has("bid")){
                    bid = received.getString("bid");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(action == 1 && !IS_POP_UP_OPEN)
            {
                try {
                    received=new JSONObject(received.getString("data"));
                    if(isApplicationSentToBackground(this) || !MainActivity.mainActivity_opened)
                    {
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("DATA",received.toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }else {

                        Utility.printLog(TAG+" data response "+received.toString());
                        double bookingID = received.getDouble("bookingId");;

                        Utility.printLog("bookinig id is : "+IS_POP_UP_OPEN+" "+bookingID+" "+PREVIOUS_BID);

                        if(!PREVIOUS_BID.matches(bookingID+"")) {
                            PREVIOUS_BID = bookingID+"";
                            Intent i = new Intent("android.intent.action.MAIN."+getResources().getString(R.string.app_name_withoutSpace));
                            i.putExtra("DATA",received.toString());
                            sendBroadcast(i);

                        }

                    }
                }catch (Exception e){

                    Log.d("catshthe",e.toString());
                }
            }

            if(action==3){

                    Bundle bundle=new Bundle();
                    bundle.putString("action",action+"");
                    bundle.putString("message",message);
                    bundle.putString("bid",bid);

                    Intent homeIntent=new Intent(AppConstants.ACTION.PUSH_ACTION);
                    homeIntent.putExtras(bundle);
                    sendBroadcast(homeIntent);

            }

            if("506".equals(action))
            {
                    Intent stopIntent = new Intent(MyFirebaseMessagingService.this, LocationUpdateService.class);
                    stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                    startService(stopIntent);

                    Intent startIntent = new Intent(MyFirebaseMessagingService.this, LocationUpdateService.class);
                    startIntent.setAction(AppConstants.ACTION.STARTFOREGROUND_ACTION);
                    startService(startIntent);

            }

            if(action == 111 || action == 112) {

                try {
                    received=new JSONObject(received.getString("data"));
                    AdvertiseData advertiseDataModel = new Gson().fromJson(received.toString(), AdvertiseData.class);

                    /*notificationIntent = new Intent(this, MainActivity.class);
                    taskStackBuilder.addParentStack(MainActivity.class);
                    taskStackBuilder.addNextIntent(notificationIntent);*/

                    Intent intent = new Intent(this, AdvertiseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ADVERTISE_DETAILS, advertiseDataModel);
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }





//        sendNotification(message, received);
    }


    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, JSONObject received) {

        Intent intent;
        intent = new Intent(MyFirebaseMessagingService.this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(messageBody);
        bigText.setBigContentTitle(getString(R.string.app_name));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
//                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(messageBody))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setLargeIcon(bitmap)
                .setStyle(bigText);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    public static boolean isApplicationSentToBackground(final Context context)
    {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty())
        {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName()))
            {
                return true;
            }
        }
        return false;
    }
}