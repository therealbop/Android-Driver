package com.karry.authentication.login;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.karry.app.MyApplication;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.authentication.login.model.LanguagesPojo;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.manager.mqtt_manager.MQTTManager;
import com.karry.network.NetworkStateHolder;
import com.karry.network.RxNetworkObserver;
import com.heride.partner.BuildConfig;
import com.heride.partner.R;
import com.karry.authentication.login.model.LoginData;
import com.karry.authentication.login.model.VehiclesDetails;
import com.karry.network.NetworkService;
import com.karry.pojo.VehicleTypes;
import com.karry.utility.DataParser;
import com.karry.utility.TextUtil;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.karry.utility.VariableConstant.LOGIN;
import static com.karry.utility.VariableConstant.PASSWORD;
import static com.karry.utility.VariableConstant.USER_NAME;
import static com.karry.utility.VariableConstant.VEH_NO;


/**
 * <h1>LoginPresenter</h1>
 * This class is used to communicate between login activity and model
 * @author 3Embed
 * @since on 30-Jan-18.
 */

public class LoginPresenter implements LoginContract.Presenter {

    @Inject  PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject  LoginContract.View view;
    @Inject  NetworkService networkService;
    @Inject  CompositeDisposable compositeDisposable;
    @Inject  Context mActivity;
    @Inject  MQTTManager mqttManager;
    @Inject  RxNetworkObserver rxNetworkObserver;
    @Inject  NetworkStateHolder networkStateHolder;
    @Inject Gson gson;
    @Inject @Named(LOGIN)
    ArrayList<LanguagesList> languagesLists = new ArrayList<>();
    private Disposable networkDisposable;
    private boolean networkAvailable = true;

    private String email = "", password = "", battery = "0";
    private SharedPreferences.Editor loginPrefsEditor;

    @Inject
    LoginPresenter()
    {
    }

    public void storeFcmToken()
    {
        preferenceHelperDataSource.setFCMRegistrationId(FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    public void checkIsAlreadyLogin() {
        if(!TextUtil.isEmpty(preferenceHelperDataSource.getUserName()))
        {
            view.setLoginCreds(preferenceHelperDataSource.getUserName(),preferenceHelperDataSource.getPassword());
        }
    }

    @Override
    public void getDeviceId() {
        preferenceHelperDataSource.setDeviceId(Utility.getDeviceId(mActivity));
    }

    @Override
    public void validateField(String emailPhone, String password, String vehNo) {

        boolean isValidEmail = TextUtil.isValidUser(emailPhone);

        if (isValidEmail && !TextUtil.isEmpty(password) /*&& !TextUtil.isEmpty(vehNo)*/)
        {
            view.enableLogIn();
        }
        else if(isValidEmail || !TextUtil.isEmpty(password) /*|| !TextUtil.isEmpty(vehNo)*/)
        {
            int count = 0;
            if(!isValidEmail)
                count++;
            else
                view.setDisableUserError();

            if(TextUtil.isEmpty(password))
                count++;
            else
                view.setDisablePasswordError();

           /* if(TextUtil.isEmpty(vehNo))
                count++;
            else
                view.setDisableVehPinError();*/

            Utility.printLog("the count is "+count);


            if(count==1)
                view.enableHalf();
            if (count==2)
                view.enableThirty();
        }
        else
        {
            view.disableLoginIn();
        }
    }

    @Override
    public void setSeekBarProgress(SeekBar seekBar, int progress)
    {
        ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, "progress", progress);
        animation.setDuration(500); // 0.5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @Override
    public void onClickLoginButton(String username, String password, String vehNo) {

        if(!TextUtil.isValidUser(username))
        {
            view.onUsernameError(mActivity.getString(R.string.invalid_email_or_phone));
        }
        else if(TextUtils.isEmpty(password))
        {
            view.onPasswordError(mActivity.getString(R.string.password_miss));
        }
       /* else if(TextUtils.isEmpty(vehNo))
        {
            view.onVehNoError(mActivity.getString(R.string.vehNo_miss));
        }*/
        else
        {
            signIn(username,password,vehNo);

        }
    }


    @Override
    public void currentLocation() {
        double[] latlong = Utility.getLocation(mActivity);
        preferenceHelperDataSource.setCurrLatitude(String.valueOf(latlong[0]));
        preferenceHelperDataSource.setCurrLongitude(String.valueOf(latlong[1]));
    }

    @Override
    public void checkMessage(Bundle data) {
        String errorMessage = data.getString("message");
        if(errorMessage!=null && !errorMessage.matches(""))
            view.onFailure(errorMessage, mActivity.getResources().getString(R.string.message));
    }

    @Override
    public void subscribeNetworkObserver() {

        Utility.printLog(" network not available.... "+networkStateHolder.isConnected());
        if(networkStateHolder.isConnected())
            view.networkAvailable();
        else
            view.networkNotAvailable();

        rxNetworkObserver.subscribeOn(Schedulers.io());
        rxNetworkObserver.observeOn(AndroidSchedulers.mainThread());
        networkDisposable = rxNetworkObserver.subscribeWith(new DisposableObserver<NetworkStateHolder>() {
            @Override
            public void onNext(NetworkStateHolder networkStateHolder) {
                networkAvailable = networkStateHolder.isConnected();
                Utility.printLog(" network not available "+networkStateHolder.isConnected());
                if(networkStateHolder.isConnected())
                    view.networkAvailable();
                else
                    view.networkNotAvailable();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                compositeDisposable.add(networkDisposable);
            }
        });
    }

    @Override
    public void networkCheckOnresume() {
        Utility.printLog(" network not available.... "+networkStateHolder.isConnected());
        if(networkStateHolder.isConnected())
            view.networkAvailable();
        else
            view.networkNotAvailable();
    }

    /**
     * <h1>mBroadcastReceiver</h1>
     * <p>for check the battery charge of device</p>
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            Utility.printLog("batery percentage is :  "+String.valueOf(level) + "%");
            battery  = String.valueOf(level);
        }
    };


    /**
     * <h1>signIn</h1>
     * <p>signin API call</p>
     * @param username : email or phone
     * @param password : password
     * @param vehNo : vehicle number
     */
    private void signIn(String username, String password, String vehNo){

        if(Utility.isNetworkAvailable(mActivity)) {
            view.startProgressBar();
            view.hideSoftKeyboard();
            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            iFilter.addAction("com.app.driverapp.internetStatus");
            mActivity.registerReceiver(mBroadcastReceiver, iFilter);

            Observable<Response<ResponseBody>> responseObservable = networkService.signIn(
                    preferenceHelperDataSource.getLanguage(),
                    username,
                    password,
                    /*vehNo,*/
                    VariableConstant.DEVICE_TYPE,
                    Utility.getDeviceId(mActivity),
                    VariableConstant.APP_VERSION,
                    VariableConstant.DEVICE_MAKER,
                    VariableConstant.DEVICE_MODEL,
                    VariableConstant.DEVICE_OS_VERSION,
                    Double.parseDouble(battery),
                    "0",
                    Utility.date());

            responseObservable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Response<ResponseBody>>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            view.stopProgressBar();

                            try {
                                switch (value.code()) {
                                    case VariableConstant.RESPONSE_CODE_SUCCESS:
                                        String response =value.body().string();
                                        Utility.printLog("MyResponseString" + response);
                                        JSONObject jsonObject = new JSONObject(response);
                                        response = jsonObject.getString("data");
                                        LoginData signInResponse = gson.fromJson(response, LoginData.class);
                                        setSignInData(signInResponse);
                                        loginPrefsEditor.putString(USER_NAME, username);
                                        loginPrefsEditor.putString(PASSWORD, password);
                                        loginPrefsEditor.putString(VEH_NO, vehNo);
                                        loginPrefsEditor.commit();
                                        break;

                                    default:

                                        JSONObject jsonObject1 = new JSONObject(value.errorBody().string());
                                        String err = jsonObject1.getString("message");
                                        Utility.printLog("MyResponseString error response is  : \n" + err);
                                        view.onFailure(err,mActivity.getResources().getString(R.string.message));
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Utility.printLog("LoginResponse : Catch :" + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.stopProgressBar();
                            Utility.printLog("LoginResponse : onError :" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else {
            view.onFailure(mActivity.getResources().getString(R.string.no_network), mActivity.getResources().getString(R.string.alert));
        }

    }


    /**
     * <h1>setSignInData</h1>
     * <p>the login success response handling, which store the response values</p>
     * @param data :response model class
     */
    private void setSignInData(LoginData data){

        preferenceHelperDataSource.setLoginResponseData(data);
        preferenceHelperDataSource.setIsLogin(true);
        preferenceHelperDataSource.setDriverID(data.getMid());
        preferenceHelperDataSource.setSessionToken(data.getToken());
        preferenceHelperDataSource.setRefereralCode(data.getReferralCode());
        preferenceHelperDataSource.setMyName(data.getFirstName()+" "+data.getLastName());
        preferenceHelperDataSource.setMid(data.getMid());
        preferenceHelperDataSource.setUserPhone(data.getPhone());
        preferenceHelperDataSource.setCountryCode(data.getCountryCode());
        /*preferenceHelperDataSource.setFcmTopic(data.getFcmTopic());*/
        preferenceHelperDataSource.setMqttTopic(data.getMqttTopic());
        preferenceHelperDataSource.setProfilePic(data.getProfilePic());
        preferenceHelperDataSource.setGmail(data.getEmail());
        preferenceHelperDataSource.setRequesterId(data.getRequester_id());
        preferenceHelperDataSource.setDriverMail(data.getEmail());

        preferenceHelperDataSource.setMqttHost(data.getMqttHost());
        preferenceHelperDataSource.setMqttPassword(data.getMqttPassword());
        preferenceHelperDataSource.setMqttUserName(data.getMqttUserName());
        preferenceHelperDataSource.setMqttPort(data.getMqttPort());

        if(data.getCall()!=null && data.getCall().getAuthToken()!=null)
            preferenceHelperDataSource.setAuthTokenCall(data.getCall().getAuthToken());
        preferenceHelperDataSource.setGoogleMapKeyTopic(data.getGoogleMapKeyTopic());


        if(MyApplication.getInstance().isMQTTConnected()){
            mqttManager.subscribeToTopic(preferenceHelperDataSource.getMqttTopic());
            mqttManager.subscribeToTopic(preferenceHelperDataSource.getGoogleMapKeyTopic());

        }else {
            MyApplication.getInstance().connectMQTT();
        }

        try {
            ArrayList<VehiclesDetails> vehicleList;
            if(data.getVehicles()!=null && data.getVehicles().size()>0 && data.getVehicles().get(0).getServices()!=null){
                // ((MyApplication)mActivity.getApplicationContext()).connectMQTT();
                vehicleList= data.getVehicles();
                preferenceHelperDataSource.setVehicles(data);
                preferenceHelperDataSource.setCarIcon(vehicleList.get(0).getVehicleMapIcon());
                preferenceHelperDataSource.setSelectedVehicle(vehicleList.get(0));
                view.onSuccessLogin(vehicleList);
            }else {
                view.onFailure(mActivity.getResources().getString(R.string.services_invalid), mActivity.getResources().getString(R.string.message));
            }
        }catch (Exception e){
            view.onFailure(mActivity.getResources().getString(R.string.services_invalid), mActivity.getResources().getString(R.string.message));

        }

    }



    @Override
    public void onDestoryView() {
        compositeDisposable.clear();
    }

    @Override
    public void getUsernamePass() {

        view.setLanguage(preferenceHelperDataSource.getLanguageSettings().getName(),false);
        SharedPreferences loginPreferences = mActivity.getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        view.setUserNamePass(loginPreferences.getString(USER_NAME, ""),
                loginPreferences.getString(PASSWORD, ""),
                loginPreferences.getString(VEH_NO, ""));

    }

    @Override
    public void checkLoginResponse() {
        if (preferenceHelperDataSource.getSigninData() != null &&
                !"".equals(preferenceHelperDataSource.getSigninData())){

            if(preferenceHelperDataSource.getVehicles()!=null &&
                    preferenceHelperDataSource.getVehicles().getVehicles()!=null) {
                ArrayList<VehiclesDetails> vehicleList;
                vehicleList = preferenceHelperDataSource.getVehicles().getVehicles();//data.getVehicles();
                view.onSuccessLogin(vehicleList);
            }
        }

        if (preferenceHelperDataSource.getVehicleTypes() != null && !"".equals(preferenceHelperDataSource.getVehicleTypes())){

            VehicleTypes vehicleTypes = preferenceHelperDataSource.getVehicleTypes();
            view.gotoVehicleTypeActivity(vehicleTypes);
        }
    }

    @Override
    public void clear() {
        compositeDisposable.clear();
    }

    @Override
    public void unSubScribeFCMTopics() {
        if(VariableConstant.FCM_TOPIS!=null && VariableConstant.FCM_TOPIS.getFcmTopics().size()>0){
            for(int i = 0;i<VariableConstant.FCM_TOPIS.getFcmTopics().size();i++){
                Utility.printLog("unsubScribed FCM TOPIC :  "+VariableConstant.FCM_TOPIS.getFcmTopics().get(i));
                FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/"+VariableConstant.FCM_TOPIS.getFcmTopics().get(i));
            }
            VariableConstant.FCM_TOPIS = null;
        }

        if(VariableConstant.MQTT_TOPICS!=null && mqttManager.isMQTTConnected()){
            mqttManager.unSubscribeToTopic(VariableConstant.MQTT_TOPICS);
            Utility.printLog("testing unsubScribed Mqtt Topic :   "+VariableConstant.MQTT_TOPICS);
            VariableConstant.MQTT_TOPICS = null;
        }
    }

    @Override
    public void getLanguages() {
        if(Utility.isNetworkAvailable(mActivity)) {
            view.startProgressBar();
            view.hideSoftKeyboard();

            Observable<Response<ResponseBody>> getLanguages = networkService.getLanguages();

            getLanguages.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Response<ResponseBody>>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            view.stopProgressBar();

                            try {
                                switch (value.code()) {
                                    case VariableConstant.RESPONSE_CODE_SUCCESS:

                                        LanguagesPojo languagesListModel = gson.fromJson(value.body().string(),LanguagesPojo.class);
                                        languagesLists.clear();
                                        languagesLists.addAll(languagesListModel.getData());
                                        boolean isLanguage = false;
                                        for(LanguagesList languagesList : languagesLists)
                                        {
                                            if(preferenceHelperDataSource.getLanguageSettings().getCode().equals(languagesList.getCode()))
                                            {
                                                isLanguage = true;
                                                view.setLanguageDialog(languagesLists.indexOf(languagesList));
                                                break;
                                            }
                                        }
                                        if(!isLanguage)
                                            view.setLanguageDialog(-1);


                                        /*Utility.printLog("getLanguages : " +resp);
                                        LanguagesPojo languagesPojo = gson.fromJson(resp, LanguagesPojo.class);
                                        if(languagesPojo.getData().size()>0){
                                            view.setLanguageDialog(languagesPojo.getData());
                                        }*/
                                        break;

                                    default:
                                        view.onFailure(DataParser.fetchErrorMessage(value), mActivity.getResources().getString(R.string.message));
                                        break;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Utility.printLog("getLanguages : Catch :" + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.stopProgressBar();
                            Utility.printLog("getLanguages : onError :" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else {
            view.onFailure(mActivity.getResources().getString(R.string.no_network), mActivity.getResources().getString(R.string.alert));
        }

    }

    @Override
    public void languageChanged(String langCode, String langName, int dir) {
        preferenceHelperDataSource.setLanguage(langCode);
        preferenceHelperDataSource.setLanguageSettings(new LanguagesList(langCode,langName, dir));
        view.setLanguage(langName,true);
        VariableConstant.lan = langCode;
    }

    @Override
    public void setWebUrl(String from) {
        String url = BuildConfig.SIGNUP_URL.concat(preferenceHelperDataSource.getLanguage()).concat("_termsAndConditions.php");
        view.openWebView(url);
    }


}
