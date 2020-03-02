package com.karry.telecall.utility;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.manager.mqtt_manager.MQTTManager;
import com.karry.manager.mqtt_manager.MqttEvents;
import com.karry.telecall.UtilityVideoCall;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import dagger.android.DaggerService;

/**
 * Created by Ali on 1/20/2018.
 */

public class OnMyService extends DaggerService
{
    //  SessionManager sharedPrefs;

    @Inject
    PreferenceHelperDataSource sharedPrefs;
    @Inject
    MQTTManager manager;
    @Inject
    public OnMyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //sharedPrefs = SessionManager.getInstance(this);
        Log.e("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ClearFromRecentService", "Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        String cid = sharedPrefs.getMid();

        Log.e("ClearFromRecentService", "END "+cid+" MQTTRESPO "+manager.isMQTTConnected());
        //Code here

        if(manager.isMQTTConnected())
        {
            if(!cid.equals(""))
            {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("status", 1);
                    manager.publish(MqttEvents.CallsAvailability.value + "/" + cid, obj, 0, true);//UserId
                    UtilityVideoCall.getInstance().setActiveOnACall(false, false);
                //    manager.subscribeToTopic(MqttEvents.Calls.value+"/"+sharedPrefs.getSID(),1);
                    //  mqttManager.subscribeToTopic(MqttEvents.Calls.value+"/5c177bf2f56745d4b143e1a6",1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
              //  manager.publish(MqttEvents.CallsAvailability.value+"/"+cid);

                manager.disconnect(sharedPrefs.getMqttTopic());
            }
        }

        stopSelf();
    }

}
