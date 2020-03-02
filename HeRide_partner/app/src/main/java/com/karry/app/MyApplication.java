package com.karry.app;

import android.content.Context;
import android.content.Intent;
import androidx.multidex.MultiDex;

import com.karry.authentication.login.model.LanguagesList;
import com.karry.dagger.AppComponent;
import com.karry.dagger.DaggerAppComponent;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.manager.mqtt_manager.MQTTManager;
import com.karry.network.ConnectivityReceiver;
import com.karry.network.NetworkStateHolder;
import com.karry.network.RxNetworkObserver;
import com.karry.service.CouchDbHandler;
import com.karry.telecall.utility.CallHelper;
import com.karry.utility.Utility;
import com.facebook.FacebookSdk;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;


public class MyApplication extends DaggerApplication {

    private static final String TAG = "MyApplication";
    CouchDbHandler couchDBHandler;
    @Inject
    NetworkStateHolder networkStateHolder;
    @Inject
    RxNetworkObserver rxNetworkObserver;
    @Inject
    MQTTManager mqttManager;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    Intent networkService;
    private CallHelper callHelper;
    private static MyApplication mInstance;

    @Override
    public void onLowMemory() {
        System.gc();
        super.onLowMemory();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FacebookSdk.sdkInitialize(this);
        couchDBHandler = new CouchDbHandler(MyApplication.this);


        Utility.printLog(TAG + " preferenceHelperDataSource.getLanguageSettings() " +
                preferenceHelperDataSource.getLanguageSettings());
        if (preferenceHelperDataSource.getLanguageSettings() == null)
            preferenceHelperDataSource.setLanguageSettings(new LanguagesList("en", "English", 0));
        else
            Utility.changeLanguageConfig(preferenceHelperDataSource.getLanguageSettings().getCode(), this);


        //        connectMQTT();
        /*startNetworkService();*/

    }

    /*
     *Starting the network service for the internet checking. */
    /*private void startNetworkService()
    {
        networkService = new Intent(this,NetworkCheckerService.class);
        Utility.printLog(" SERVICE on startNetworkService ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            this.startForegroundService(networkService);
        else
            startService(networkService);

    }*/
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

    public CouchDbHandler getDBHandler() {
        return couchDBHandler;
    }


    public void reconnect() {
        networkStateHolder.setConnected(true);
        rxNetworkObserver.publishData(networkStateHolder);
    }

    public void connectMQTT() {

        if (preferenceHelperDataSource.isLoggedIn()) {
            mqttManager.createMQttConnection(preferenceHelperDataSource.getDriverID() + Utility.getDeviceId(this));

        }

    }

    public CallHelper getCallHelper() {
        if (callHelper == null) {
            callHelper = new CallHelper(preferenceHelperDataSource);
        }
        return callHelper;
    }

    public MQTTManager getMqtt() {
        return mqttManager;
    }

    public void publishMqtt(JSONObject reqObject, JSONObject backendObject) {
        mqttManager.publish(reqObject, backendObject);
    }

    public void publishMqttNew(String reqObject, String bookingId) {
        mqttManager.publishTest(reqObject,bookingId);
    }

    public boolean isMQTTConnected() {
        return mqttManager.isMQTTConnected();
    }

    public void unSubscribeMqtt(String topic) {
        mqttManager.unSubscribeToTopic(topic);
    }

}

