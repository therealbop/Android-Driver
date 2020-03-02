package com.karry.app.mainActivity;


import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.manager.mqtt_manager.MQTTManager;
import com.heride.partner.R;
import com.karry.network.NetworkStateHolder;
import com.karry.network.RxNetworkObserver;
import com.karry.network.NetworkService;
import com.karry.utility.DataParser;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karry.utility.VariableConstant.IS_POP_UP_OPEN;
import static com.karry.utility.VariableConstant.RESPONSE_CODE_SUCCESS;

public class MainActivityPresenter implements MainActivityContract.MainActivityPresenter {

    @Inject MainActivityContract.MainActivityView mainActivityView;
    @Inject PreferenceHelperDataSource preferencesHelper;
    @Inject NetworkService networkService;
    @Inject Gson gson;
    @Inject RxNetworkObserver rxNetworkObserver;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject CompositeDisposable compositeDisposable;
    @Inject Context context;
    private Disposable networkDisposable;
    @Inject
    MQTTManager mqttManager;


    @Inject
    MainActivityPresenter() {
    }

    @Override
    public void getProfilePicImg() {
        mainActivityView.setProfilePicImg(preferencesHelper.getProfilePic(), preferencesHelper.getMyName());
    }

    @Override
    public void getAppConfig()
    {
        Observable<Response<ResponseBody>> request = networkService.getAppConfig(preferencesHelper.getSessionToken(), preferencesHelper.getLanguage(), 1);
        request.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        Utility.printLog("MyResponseFromConfig"+value.code());
                        switch (value.code()) {
                            case 200:
                                String response=DataParser.fetchDataString(value);
                                Utility.printLog("MyResponseFromConfig1"+response);
                                DriverConfigDetailModel driverConfigDetailModel=gson.fromJson(response,DriverConfigDetailModel.class);
                                handleResponse(driverConfigDetailModel);

                                if(driverConfigDetailModel.isTwillioCallingEnable())
                                    VariableConstant.isTwilioEnable = true;
                                else
                                    VariableConstant.isTwilioEnable=false;
                                break;

                            default:
                                break;
                        }
                    }
                    @Override
                    public void onError(Throwable e)
                    {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * <h1>handleResponse</h1>
     * <p>for handling the Driver Configuration, store the values for use</p>
     * @param configDetail : Model class with API response
     */
    private void handleResponse(DriverConfigDetailModel configDetail)
    {
        preferencesHelper.setGoogleServerKeys(configDetail.getDriverGoogleMapKeys());
        /*if(preferencesHelper.getGoogleServerKeys().size()>0)
            preferencesHelper.setGoogleServerKey(preferencesHelper.getGoogleServerKeys().get(0));*/

        preferencesHelper.setDriverApiDistanceWhenFree(configDetail.getDriverApiDistanceWhenFree());
        preferencesHelper.setApiIntervalWhenBusy(configDetail.getDriverApiIntervalWhenBusy());
        if(configDetail.getDriverApiIntervalWhenFree()!=null && !configDetail.getDriverApiIntervalWhenFree().equals(""))
            preferencesHelper.setApiIntervalWhenFree(configDetail.getDriverApiIntervalWhenFree());
        preferencesHelper.setAppVersion(configDetail.getAppVersion());
        preferencesHelper.setDistanceForLatLongMax(configDetail.getDistanceForLogingLatLongsMax());
        preferencesHelper.setDistanceForLatLong(configDetail.getDistanceForLogingLatLongs());
        preferencesHelper.setCalculateDistanceXMeter(configDetail.getMeterBookingCalculateDistanceXMeters());
        preferencesHelper.setCalculateTimeXSecond(configDetail.getMeterBookingCalculateTimeXSeconds());
        preferencesHelper.setIsMandatory(configDetail.getMandatory());
        preferencesHelper.setPresenceTime(configDetail.getPresenceTime());
        preferencesHelper.setStripeKey(configDetail.getStripePublishKey());
        preferencesHelper.setIsHelpCenterEnable(configDetail.isHelpCenterEnable());
        preferencesHelper.setIsChatModuleEnable(configDetail.isChatModuleEnable());
        preferencesHelper.setGoogleMapKey(configDetail.getGoogleMapKey());
        preferencesHelper.setGoogleServerKey(configDetail.getGoogleMapKey());



        boolean mqttChange  = false;
        if(!preferencesHelper.getMqttPort().matches(configDetail.getMqttPort())){
            preferencesHelper.setMqttPort(configDetail.getMqttPort());
            mqttChange = true;
        }


        if(!preferencesHelper.getMqttUserName().matches(configDetail.getMqttUserName())) {
            preferencesHelper.setMqttUserName(configDetail.getMqttUserName());
            mqttChange = true;
        }

        if(!preferencesHelper.getMqttHost().matches(configDetail.getMqttHost())) {
            preferencesHelper.setMqttHost(configDetail.getMqttHost());
            mqttChange = true;
        }

        if(!preferencesHelper.getMqttPassword().matches(configDetail.getMqttPassword())) {
            preferencesHelper.setMqttPassword(configDetail.getMqttPassword());
            mqttChange = true;
        }

        if(mqttChange){
            mqttManager.createMQttConnection(preferencesHelper.getDriverID()+Utility.getDeviceId(context));

        }

        Utility.printLog("mandatory update : "+Utility.currentVersion(context).compareToIgnoreCase(configDetail.getAppVersion()));
        if(Utility.currentVersion(context).compareToIgnoreCase(configDetail.getAppVersion())<0)
            mainActivityView.mandatoryUpdateDialog(configDetail.isMandatoryUpdateEnable());

    }

    @Override
    public void hideKeyboardAndClearFocus() {
        mainActivityView.hideSoftKeyboard();
    }

    @Override
    public void showKeyboard() {
        mainActivityView.showSoftKeyboard();
    }

    @Override
    public void subscribeNetworkObserver()
    {

        checkForNetwork(networkStateHolder.isConnected());


/*
        rxNetworkObserver.subscribeOn(Schedulers.io());
        rxNetworkObserver.observeOn(AndroidSchedulers.mainThread());
        networkDisposable = rxNetworkObserver.subscribeWith(new DisposableObserver<NetworkStateHolder>() {
            @Override
            public void onNext(NetworkStateHolder networkStateHolder) {
                Utility.printLog(" network not available "+networkStateHolder.isConnected());
                if(networkStateHolder.isConnected())
                    mainActivityView.networkAvailable();
                else
                    mainActivityView.networkNotAvailable();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                compositeDisposable.add(networkDisposable);
            }
        });*/
    }

    @Override
    public void clear() {
        compositeDisposable.clear();
    }

    @Override
    public String getWalletAmount() {
        String wallet = context.getResources().getString(R.string.wallet);
        if(!preferencesHelper.getWalletBal().matches("0"))
            wallet = wallet.concat(" ").concat(preferencesHelper.getWalletBal());
        return wallet;
    }

    @Override
    public void logout() {
        logoutApi();
    }

    @Override
    public void networkCheckOnresume() {
        if(Utility.isNetworkAvailable(context))
            mainActivityView.networkAvailable();
        else
            mainActivityView.networkNotAvailable();
    }

    @Override
    public void getBroadCastReciever(Intent intent) {
        String data = intent.getStringExtra("DATA");
        Utility.printLog("the booking data is : "+data);
        if(data!=null)
            bookingAckAPI(data);
    }

    @Override
    public void preferenceZoneClick() {

    }

    @Override
    public void getLiveChat() {
        mainActivityView.openLiveChat(preferencesHelper.getMyName());
    }

    @Override
    public boolean checkZenDesk() {
       return preferencesHelper.getIsHelpCenterEnable();
    }

    @Override
    public void checkForNetwork(boolean isConnected) {
        networkStateHolder.setConnected(isConnected);
        rxNetworkObserver.publishData(networkStateHolder);
        /*if(!isConnected && !IS_APP_BACKGROUND && !NETWORK_SCREEN_OPEN)
            mainActivityView.openNetworkScreen();*/


        if(isConnected)
            mainActivityView.networkAvailable();
        else
            mainActivityView.networkNotAvailable();
    }


    /**
     * <h1>logoutApi</h1>
     * <p>API call for logout</p>
     */
    private void logoutApi()
    {
        mainActivityView.showProgress();
        Observable<Response<ResponseBody>> logoutApi=networkService.logout(preferencesHelper.getLanguage(),preferencesHelper.getSessionToken());
        logoutApi.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {


                        try {
                            switch (value.code())
                            {
                                case RESPONSE_CODE_SUCCESS:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    mainActivityView.onSuccesLogout();
                                    break;

                                default:
                                    Utility.BlueToast(context,DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            Utility.printLog("logout : Catch :"+e.getMessage());
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        mainActivityView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        mainActivityView.hideProgress();
                    }
                });
    }

    /**
     * <h1>bookingAckAPI</h1>
     * <p>the api call for the booking conform</p>
     * @param data
     */
    private void bookingAckAPI(String data){
        try {
            if(data!=null)
            {
                JSONObject jsonObject = new JSONObject(data);
                String bid = jsonObject.getString("bookingId");

                Observable<Response<ResponseBody>> bookingACK = networkService.bookingAck(preferencesHelper.getSessionToken(), preferencesHelper.getLanguage(), bid);
                bookingACK.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Response<ResponseBody>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(Response<ResponseBody> value) {


                                try {
                                    switch (value.code()) {
                                        case RESPONSE_CODE_SUCCESS:
                                            Utility.printLog("values " + value.code());
                                            if (!IS_POP_UP_OPEN) {
                                                IS_POP_UP_OPEN = true;
                                                mainActivityView.openBookingPopUp(data);
                                            }
                                            break;

                                        default:
//                                        Utility.BlueToast(context,DataParser.fetchErrorMessage(value));
                                            break;
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Utility.printLog("bookingAckAPI : Catch :" + e.getMessage());
                                }
                            }


                            @Override
                            public void onError(Throwable e) {
                                Utility.printLog("bookingAckAPI : error :" + e.getMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}

