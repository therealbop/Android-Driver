package com.karry.manager.mqtt_manager;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.karry.app.MyApplication;
import com.karry.app.bookingRequest.BookingPopUpActivity;
import com.karry.app.mainActivity.MainActivity;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.manager.booking.BookingManager;
import com.karry.mqttChat.ChatData;
import com.karry.mqttChat.ChattingActivity;
import com.karry.network.NetworkStateHolder;
import com.karry.network.RxNetworkObserver;
import com.heride.partner.R;
import com.karry.telecall.UtilityVideoCall;
import com.karry.telecall.model.NewCallData;
import com.karry.telecall.model.NewCallMqttResponse;
import com.karry.telecall.utility.CallingApis;
import com.karry.telecall.utility.RxCallInfo;
import com.karry.utility.AcknowledgeHelper;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import static com.karry.mqttChat.ChattingActivity.CHAT_ACTIVITY;
import static com.karry.utility.VariableConstant.APPISBACKGROUND;
import static com.karry.utility.VariableConstant.FORGROUND_LOCK;
import static com.karry.utility.VariableConstant.IS_POP_UP_OPEN;
import static com.karry.utility.VariableConstant.PREVIOUS_BID;
import static com.karry.utility.VariableConstant.UPDATE_DROP;

/**
 * <h1>MQTTManager</h1>
 * This class is used to handle the MQTT data
 * @author 3Embed
 * @since on 21-12-2017.
 */
public class MQTTManager
{
    private static final String TAG = "MQTTManager";
    private IMqttActionListener mMQTTListener;
    private static MqttAndroidClient mqttAndroidClient;
    private static MqttConnectOptions mqttConnectOptions;
    private Context mContext;
    private BookingManager bookingManager;

    private AcknowledgeHelper acknowledgeHelper;
    PreferenceHelperDataSource helperDataSource;
    private static NetworkStateHolder networkStateHolder;
    private static RxNetworkObserver  rxNetworkObserver;
    private String chatUniqueID="";

    @Inject
    public MQTTManager(Context context,
                       AcknowledgeHelper acknowledgeHelper,
                       PreferenceHelperDataSource dDataSource, BookingManager bookingManager, NetworkStateHolder networkStateHolder,RxNetworkObserver rxNetworkObserver)
    {
        mContext = context;
        this.acknowledgeHelper=acknowledgeHelper;
        this.helperDataSource=dDataSource;
        this.bookingManager=bookingManager;
        this.networkStateHolder = networkStateHolder;
        this.rxNetworkObserver = rxNetworkObserver;

        mMQTTListener = new IMqttActionListener()
        {
            @Override
            public void onSuccess(IMqttToken asyncActionToken)
            {
                if(mqttAndroidClient!=null && mqttAndroidClient.isConnected()) {

                    subscribeToTopic(helperDataSource.getMqttTopic());
                    subscribeToTopic(helperDataSource.getMid());

                    JSONObject obj = new JSONObject();

                    try {
                        obj.put("status", 1);

                        publish(MqttEvents.CallsAvailability.value + "/" + helperDataSource.getMid(), obj, 0, true);//UserId
                        //subscribeToTopic(MqttEvents.Calls.value + "/" + helperDataSource.getDriverID());
                        UtilityVideoCall.getInstance().setActiveOnACall(false, false);
                        networkStateHolder.setConnected(true);
                        Utility.printLog(TAG + " TEST MQTT" +" connected " + mqttAndroidClient.getClientId());
                        rxNetworkObserver.publishData(networkStateHolder);
                        Utility.printLog(TAG + " onSuccessPhone: myqtt client " + asyncActionToken.isComplete());

                        makeAvilabilityOnOff(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    /*subscribeToTopic(helperDataSource.getMqttTopic());
                    networkStateHolder.setConnected(true);
                    Utility.printLog(TAG + " TEST MQTT" +" connected " + mqttAndroidClient.getClientId());
                    rxNetworkObserver.publishData(networkStateHolder);
                    Utility.printLog(TAG + " onSuccessPhone: myqtt client " + asyncActionToken.isComplete());*/
                }
            }
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                exception.printStackTrace();

                new Handler().postDelayed(() -> {

                    if(mqttAndroidClient!=null && !mqttAndroidClient.isConnected())
                    {
                        networkStateHolder.setConnected(false);
                        Utility.printLog(TAG+ "onFailure: myqtt client "+asyncActionToken.isComplete()
                                +" "+exception.getMessage());
                        rxNetworkObserver.publishData(networkStateHolder);
                    }
                }, 2000);

            }

        };
    }

    /**
     * <h2>subscribeToTopic</h2>
     * This method is used to subscribe to the mqtt topic
     */
    public void subscribeToTopic(String mqttTopic)
    {
        try
        {
            if (mqttAndroidClient != null)
                mqttAndroidClient.subscribe(mqttTopic, 1);
        }
        catch (MqttException e)
        {
            Utility.printLog(TAG+" MqttException "+e);
            e.printStackTrace();
        }
    }

    /**
     * <h2>unSubscribeToTopic</h2>
     * This method is used to unSubscribe to topic already subscribed
     * @param topic Topic name from which to  unSubscribe
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    public void unSubscribeToTopic(String topic)
    {
        try
        {
            if (mqttAndroidClient != null)
                mqttAndroidClient.unsubscribe(topic);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * <h2>isMQTTConnected</h2>
     * This method is used to check whether MQTT is connected
     * @return boolean value whether MQTT is connected
     */
    public boolean isMQTTConnected()
    {
        return mqttAndroidClient != null && mqttAndroidClient.isConnected();
    }

    /**
     * <h2>createMQttConnection</h2>
     * This method is used to create the connection with MQTT
     * @param clientId customer ID to connect MQTT
     */
    @SuppressWarnings("unchecked")
    public void createMQttConnection(String clientId)
    {
        Utility.printLog(TAG+" createMQttConnection: "+clientId);
        String serverUri = "tcp://" +helperDataSource.getMqttHost()+ ":" +
                helperDataSource.getMqttPort();
        mqttAndroidClient = new MqttAndroidClient(mContext, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Utility.printLog(TAG+" TEST MQTT not connect "+cause);
/*
                new Handler().postDelayed(() -> {
                    if(mqttAndroidClient!=null && !mqttAndroidClient.isConnected())
                    {
                        networkStateHolder.setConnected(false);
                        Utility.printLog(TAG+ "onFailure: myqtt client ");
                        rxNetworkObserver.publishData(networkStateHolder);
                    }
                }, 2000);*/
            }
            @Override
            public void messageArrived(String topic, final MqttMessage message) throws Exception
            {

                try {

                    final JSONObject jsonObject = new JSONObject(new String(message.getPayload()));
                    Utility.printLog("mqtt message : " + jsonObject.toString());
                    Utility.printLog("mqtt message status : " );


                    if (jsonObject.has("data") &&
                            topic.equals(helperDataSource.getMid())) {
                        handleNewCall(new String(message.getPayload()));

                    } else if (jsonObject.has("data") && topic.equals(MqttEvents.Call.value + "/" + UtilityVideoCall.getInstance().getActiveCallId())) {
                        handleActiveCall(new String(message.getPayload()));

                    }

                    else  if(jsonObject.getString("status")!=null) {
                        switch (jsonObject.getString("status")) {
                            case "1":
                                //if got Booking Request
                                String bookingID = jsonObject.getString("bookingId");
                                ;

                                Utility.printLog("bookinig id is : " + IS_POP_UP_OPEN + " " + bookingID + " " + PREVIOUS_BID);
                                if (!PREVIOUS_BID.matches(bookingID)) {
                                    PREVIOUS_BID = bookingID;
                                    acknowledgeHelper.bookingAckApi(bookingID, new AcknowledgeHelper.AcknowledgementCallback() {
                                        @Override
                                        public void callback(String bid) {
                                            Utility.printLog("bookinig id is :::: " + IS_POP_UP_OPEN + " " + bookingID + " " + PREVIOUS_BID);

                                            if (!IS_POP_UP_OPEN) {
                                                IS_POP_UP_OPEN = true;
                                                Intent intent = new Intent(mContext, BookingPopUpActivity.class);
                                                intent.putExtra("BOOKING_DATA", jsonObject.toString());
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                mContext.startActivity(intent);
                                            }
                                        }
                                    });
                                }
                                break;

                            case "3":
                                //cancel popup
                                if (IS_POP_UP_OPEN) {
                                    IS_POP_UP_OPEN = false;
                                    String cancel_reason = jsonObject.getString("msg");
                                    Utility.printLog("mqtt message : " + cancel_reason);
                                    VariableConstant.CANCEL_RIDE_REASON = cancel_reason;
                                    if (BookingPopUpActivity.mediaPlayer != null && BookingPopUpActivity.mediaPlayer.isPlaying())
                                        BookingPopUpActivity.mediaPlayer.stop();
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    mContext.startActivity(intent);
                                }

                                break;

                            case "2":
                                //update Drop Location
                                Utility.printLog("app is background:::::");
                                if (isApplicationSentToBackground() || FORGROUND_LOCK) {
                                    UPDATE_DROP = true;
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    mContext.startActivity(intent);
                                } else {
                                    bookingManager.handleBookingsStatus(new String(message.getPayload()));
                                }
                                break;
                            case "4":
                            case "20":
                                //cancel acepted Booking
                                VariableConstant.CANCEL_RIDE = true;
                                VariableConstant.CANCEL_RIDE_REASON = jsonObject.getString("msg");
                                if (isApplicationSentToBackground() || FORGROUND_LOCK) {
                                    VariableConstant.CANCEL_RIDE = true;
                                    VariableConstant.CANCEL_RIDE_REASON = jsonObject.getString("msg");
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    mContext.startActivity(intent);
                                } else {
                                    bookingManager.handleBookingsStatus(new String(message.getPayload()));
                                }
                                break;

                            case "5":
                            case "12":
                                VariableConstant.CANCEL_RIDE = true;
                                VariableConstant.ADMIN_COLMPLETED = true;
                                VariableConstant.CANCEL_RIDE_REASON = jsonObject.getString("statusText");
                                if (isApplicationSentToBackground() || FORGROUND_LOCK) {
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    mContext.startActivity(intent);
                                } else {
                                    bookingManager.handleBookingsStatus(new String(message.getPayload()));
                                }
                                break;

                            case "16":
                                JSONObject object = new JSONObject(new String(message.getPayload()));
                                //JSONObject data = object.getJSONObject("data");
                                Gson gson = new Gson();
                                ChatData rideChat = gson.fromJson(object.toString(), ChatData.class);

                                if (chatUniqueID.matches("") || (!chatUniqueID.matches("") && !chatUniqueID.matches(rideChat.getTimestamp()))) {
                                    chatUniqueID = rideChat.getTimestamp();

                                    if (CHAT_ACTIVITY) {
                                        bookingManager.handleBookingsStatus(new String(message.getPayload()));
                                    } else {
                                        setNotificationForChat(new String(message.getPayload()));
                                    }
                                }
                                break;

                            case "17":
                                String key = jsonObject.getString("googleMapKey");
                                Utility.printLog("rotated key is : " + key);
                                helperDataSource.setGoogleServerKey(key);
                                break;
                        }
                    }

                }catch (Exception e){
                    Utility.printLog(TAG+" "+e.getMessage());
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Utility.printLog(TAG+" deliveryComplete: "+token);
            }
        });

        byte[] payload = helperDataSource.getMid().getBytes();

        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setWill(MqttEvents.WillTopic.value,payload,1,false);
        mqttConnectOptions.setUserName(helperDataSource.getMqttUserName()/*MQTT_USERNAME*/);
        mqttConnectOptions.setPassword(/*MQTT_PASSWORD*/helperDataSource.getMqttPassword().toCharArray());
        connectMQTTClient(mContext);
    }

    /**
     * <h2>connectMQTTClient</h2>
     * This method is used to connect to MQTT client
     */
    private void connectMQTTClient(Context mContext)
    {
        try
        {
            mqttAndroidClient.connect(mqttConnectOptions, mContext, mMQTTListener);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * <h2>disconnect</h2>
     * This method is used To disconnect the MQtt client
     */
    public void disconnect(String mqttTopic)
    {
        try
        {
            if (mqttAndroidClient != null)
            {
                unSubscribeToTopic(mqttTopic);
                mqttAndroidClient.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h1>publish</h1>
     * @param jsonObject : body to publish
     */
    public void publish(JSONObject jsonObject,JSONObject backendObject){
        Utility.printLog("mqtt message status : " );
        try {
            Utility.printLog("topic is :   "+helperDataSource.getMqttTopic()+"\n"+jsonObject.toString());
            mqttAndroidClient.publish(helperDataSource.getMqttTopic(),jsonObject.toString().getBytes(),2,false);
            mqttAndroidClient.publish(helperDataSource.getBirdViewTopic(),backendObject.toString().getBytes(),2,false);
            mqttAndroidClient.publish(helperDataSource.getDispatcherTopic(),backendObject.toString().getBytes(),2,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishTest(String testString, String bookingId) {

        try {
            //Log.d(TAG, " publishTest: " + "Booking Id:  " + bookingId + " " + testString);
            mqttAndroidClient.publish("/TESTAKBAR/HONOUR/", testString.getBytes(), 2, false);
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void publish(String topicName, JSONObject obj, int qos, boolean retained) {

        try {
            mqttAndroidClient.publish(topicName, obj.toString().getBytes(), qos, retained);
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * <h1>isApplicationSentToBackground</h1>
     * <p>check whether the app is background or not</p>
     * @return :boolean value true: app is background  ,false: not in background
     */
    private boolean isApplicationSentToBackground()
    {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty())
        {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName()))
            {
                APPISBACKGROUND = true;
                return true;
            }
        }
        APPISBACKGROUND = false;
        return false;
    }


    private void setNotificationForChat(String chatData){

        try {
            JSONObject object = new JSONObject(chatData);
//            JSONObject data = object.getJSONObject("data");

            Gson gson = new Gson();
            ChatData rideChat = gson.fromJson(object.toString(),ChatData.class);


            Intent notificationIntent;
            notificationIntent = new Intent(mContext, ChattingActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            String str2 = String.valueOf(System.currentTimeMillis()).substring(9);
            Intent intent = new Intent(mContext, ChattingActivity.class);
            intent.putExtra("BID",String.valueOf(rideChat.getBid()));
            intent.putExtra("DRIVER_NAME",rideChat.getName());
            intent.putExtra("CUST_ID",String.valueOf(rideChat.getFromID()));


            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, Integer.parseInt(str2), intent, PendingIntent.FLAG_ONE_SHOT);

            String CHANNEL_ID = mContext.getString(R.string.app_name_withoutSpace);;// The id of the channel.
            CharSequence name = mContext.getString(R.string.app_name);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_launcher))
                    .setContentTitle(mContext.getResources().getString(R.string.message))
                    .setContentText(rideChat.getContent())
                    .setTicker(rideChat.getContent())
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setPriority(Notification.PRIORITY_HIGH);

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(rideChat.getFromID(), Integer.parseInt(str2), notificationBuilder.build());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                notificationManager.createNotificationChannel(mChannel);
            }


        } catch (Exception e) {

            Log.d(TAG, "setNotificationForChat: "+" exception");

            e.printStackTrace();
        }

    }



    public static void noInternetAvailableFromBegining(){
            if(mqttAndroidClient!=null && !mqttAndroidClient.isConnected())
            {
                try{
                networkStateHolder.setConnected(false);
                Utility.printLog(TAG+ "onFailure: myqtt client ");
                rxNetworkObserver.publishData(networkStateHolder);
                }catch(NullPointerException e){e.printStackTrace();}
            }
    }

    private void handleNewCall(String callDataResponse)
    {
        try
        {
            Gson gson = new Gson();
            NewCallMqttResponse newCallMqttResponse  = gson.fromJson(callDataResponse,
                    NewCallMqttResponse.class);
            NewCallData callData = newCallMqttResponse.getData();
            CallingApis.OpenIncomingCallScreen(callData,mContext);
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }

    private void handleActiveCall(String callDataResponse)
    {
        try
        {
            //ActiveCallResponse activeCallResponse = gson.fromJson(callDataResponse,ActiveCallResponse.class);
            //ActiveCallData activeCallData = activeCallResponse.getData();

            //Publish to the rx
            //action type
    /*
    2 call not Answer of left from a call
    3 join on call
    4 call ended.
    */
            String dataRes = new JSONObject(callDataResponse).getJSONObject("data").toString();
            RxCallInfo.getInstance().emitData(dataRes);
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }


    private void makeAvilabilityOnOff(int status)
    {
        JSONObject tempObj = new JSONObject();
        try {
            tempObj.put("status", status);
            publish(MqttEvents.CallsAvailability.value + "/" + MyApplication.getInstance().getCallHelper().getUserId(), tempObj, 0, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}