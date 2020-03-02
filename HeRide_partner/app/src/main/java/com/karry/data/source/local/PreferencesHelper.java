package com.karry.data.source.local;

import static com.karry.utility.VariableConstant.STRIP_IMAGE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.authentication.login.model.LoginData;
import com.karry.authentication.login.model.VehiclesDetails;
import com.karry.pojo.VehicleTypes;
import com.karry.service.FCM_Topics;
import com.karry.side_screens.wallet.changeCard.model.CardDetailsDataModel;
import com.karry.utility.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;



/**
 * Created by DELL on 21-12-2017.
 */




public class PreferencesHelper implements PreferenceHelperDataSource {

    private String PREF_NAME = "getdayrunner";
    private SharedPreferences sharedPreferences=null;
    private Gson gson;
    private SharedPreferences.Editor editor=null;
    @Inject Context context;


    private final String DRIVERAPIDISTANCEWHENFREE="driverapidistancewhenfree";
    private final String REG_ID = "push_token";
    private final String PUSH_TOPIC = "push_topic";
    private final String VEH_ID = "vehicle_id";
    private final String VEHICLES = "vehicles";
    private final String CHN = "driver_channel";
    private final String PRESENCE_CHN = "presence_channel";
    private final String LOGGED_IN = "login";
    private final String DEVICE_ID = "device_id";
    private final String DEVICE_TYPE = "device_type";
    private final String MID = "driver_id";
    private final String TOKEN = "token";
    private final String MY_NAME = "name";
    private final String PROFILE_PIC = "profile_pic";

    private static final String CURR_LAT = "curr_latitude";
    private static final String CURR_LONG = "curr_longitude";
    private static final String APP_VERSION="appVersion";
    private static final String IS_MANDATORY="mandatory";
    private static final String API_INTERVAL_WHEN_FREE="driverApiIntervalWhenFree";
    private static final String API_INTERVAL_WHEN_BUSY="driverApiIntervalWhenBusy";
    private static final String CALCULATE_DISTANCE_X_METER="meterBookingCalculateDistanceXMeters";
    private static final String CALCULATE_TIME_X_SECOND="meterBookingCalculateTimeXSeconds";
    private static final String DISTANCE_FOR_LAT_LONG="DistanceForLogingLatLongs";
    private static final String DistanceForLogingLatLongsMax="DistanceForLogingLatLongsMax";
    private static final String PRESENCE_TIME="presenceTime";
    private static final String LOGIN_DATA= "logindata";
    private static final String VEHICLE_DATA= "VEHICLE_DATA";
    private static final String REFERALCODE= "REFERALCODE";
    private static final String WALLET_BAL= "WALLET_BAL";
    private static final String IsHelpCenterEnable = "IsHelpCenterEnable";

    //    private static final String MID="mid";
    private static final String PHONE="phone";
    private static final String COUNTRY_CODE="code";
    private static final String FCM_TOPIC="fcmTopic";
    private static final String MQTT_TOPIC="mqttTopic";
    private static final String BIRDSVIEW_TOPIC="birdsViewTopic";
    private static final String DISPATCHER_TOPIC="dispatcherTopic";
    private static final String REQUSTER_ID="requester_id";
    private static final String PLATE_NUM="plateNo";
    private static final String GOODTYPE="goodTypes";
    private static final String TYPE_ID="typeId";
    private static final String VEHICLE_IMG_URL="vehicleImage";
    private static final String SERVICES="services";
    private static final String ID="id";
    private static final String VEHICLE_MODEL="vehicleModel";
    private static final String VEHICLE_MAKE="vehicleMake";
    private static final String VEHICLE_MAP_ICON="vehicleMapIcon";
    private static final String VEHICLE_TYPE="vehicleType";
    private static final String CURRENCY_SYMBOL="currency";
    private static final String MILEAGE_METRIC="currency";
    private static final String MeterBookingDropLat = "MeterBookingDropLat";
    private static final String MeterBookingDropLon = "MeterBookingDropLon";
    private static final String MileageMetricUnit = "mileageMetricUnit";
    private static final String DistanceCalculated = "DistanceCalculated";
    private static final String DistanceCalculatedShow = "DistanceCalculatedShow";
    private static final String BookingId = "BookingId";
    private static final String BookingStatus = "BookingStatus";
    private static final String ServiceStatus = "ServiceStatus";
    private static final String VEHICLE_CAR_ICON = "CarIcon";
    private static final String STRIPE_KEY = "STRIPE_KEY";
    private static final String DEFAULT_CARD = "DEFAULT_CARD";
    private static final String LANGUAGE_SETTINGS = "LANGUAGE_SETTINGS";
    private ArrayList<VehiclesDetails> WorkPlace_List;

    private ArrayList<String> googleServerKeys = new ArrayList<>();
    private static final Type GOOGLE_SERVER_KEYS_TYPE = new TypeToken<List<String>>() {
    }.getType();
    private static final String GOOGLE_SERVER_KEYS = "GOOGLE_SERVER_KEYS";
    private static final String GOOGLE_KEY= "GOOGLE_KEY";
    private static final String paymentUrl= "paymentUrl";
    private static final String REQUESTER_ID = "requester_id";
    private final String EMAIL = "EMAIL";
    private static final String IS_CHAT_MODULE="isChatModuleEnable";

    /*MQTT*/
    private static final String mqttUserName= "mqttUserName";
    private static final String mqttPassword= "mqttPassword";
    private static final String mqttHost= "mqttHost";
    private static final String mqttPort= "mqttPort";

    private static final String googleMap_Key="googleMap_Key";
    private static final String GoogleMapKeyTopic="googleMapKeyTopic";
    private static final String CUSTOMER_NAME= "CUSTOMER_NAME";
    private static final String CUSTOMER_PIC= "CUSTOMER_PIC";
    private static final String AUTH_TOKEN= "AUTH_TOKEN";
    private static final String BATTERY_USAGE= "battery_usage";

    private static final String DISTANCEFORMAXRANGE = "DistanceForLogingLatLongsMax";


    public void getMileageMetric(String currencySymbol)
    {
        editor.putString(MILEAGE_METRIC,currencySymbol);
        editor.commit();
    }

    public String getMileageMetric()
    {
        return sharedPreferences.getString(MILEAGE_METRIC,"miles");
    }

    @Override
    public void setCarIcon(String url) {

        editor.putString(VEHICLE_CAR_ICON,url);
        editor.commit();
    }

    @Override
    public String getCarIcon() {
        return sharedPreferences.getString(VEHICLE_CAR_ICON,"");
    }


    public void setCurrencySymbol(String currencySymbol)
    {
        editor.putString(CURRENCY_SYMBOL,currencySymbol);
        editor.commit();
    }

    public String getCurrencySymbol()
    {
        return sharedPreferences.getString(CURRENCY_SYMBOL,"$");
    }
    public void setVehicleType(String vehicleType)
    {
        editor.putString(VEHICLE_TYPE,vehicleType);
        editor.commit();
    }

    public String getVehicleType()
    {
        return sharedPreferences.getString(VEHICLE_TYPE,"");
    }
    public void setVehicleMapIcon(String vehicleMapIcon)
    {
        editor.putString(VEHICLE_MAP_ICON,vehicleMapIcon);
        editor.commit();
    }

    public String getVehicleMapIcon()
    {
        return sharedPreferences.getString(VEHICLE_MAP_ICON,"");
    }

    public void setVehicleMake(String vehicleMake)
    {
        editor.putString(VEHICLE_MAKE,vehicleMake);
        editor.commit();
    }

    public String getVehicleMake()
    {
        return sharedPreferences.getString(VEHICLE_MAKE,"");
    }
    public void setVehicleModele(String vehicleModele)
    {
        editor.putString(VEHICLE_MODEL,vehicleModele);
        editor.commit();
    }

    public Double getDistanceForLatLongMax()
    {
        return Double.valueOf(sharedPreferences.getString(DistanceForLogingLatLongsMax,"500"));
    }

    public String getVehicleModel()
    {
        return sharedPreferences.getString(VEHICLE_MODEL,"");
    }
    public void setId(String id)
    {
        editor.putString(ID,id);
        editor.commit();
    }

    public String getId()
    {
        return sharedPreferences.getString(ID,"");
    }


    public void setServices(String services)
    {
        editor.putString(SERVICES,services);
        editor.commit();
    }

    public String getServices()
    {
        return sharedPreferences.getString(SERVICES,"");
    }
    public void setVehicleImgUrl(String imgUrl)
    {
        editor.putString(VEHICLE_IMG_URL,imgUrl);
        editor.commit();
    }

    public String getVehicleImgUrl()
    {
        return sharedPreferences.getString(VEHICLE_IMG_URL,"");
    }

    public void setTypeId(String typeId)
    {
        editor.putString(TYPE_ID,typeId);
        editor.commit();
    }
    public String getTypeId()
    {
        return sharedPreferences.getString(TYPE_ID,"");
    }
    public void setGoodtype(String goodtype)
    {
        editor.putString(GOODTYPE,goodtype);
        editor.commit();
    }

    public String getGoodtype()
    {
        return sharedPreferences.getString(GOODTYPE,"");
    }
    public void setPlateNum(String plateNum)
    {
        editor.putString(PLATE_NUM,plateNum);
        editor.commit();
    }

    public String getPlateNum()
    {
        return sharedPreferences.getString(PLATE_NUM,"");
    }
    public void setRequsterId(String requsterId)
    {
        editor.putString(REQUSTER_ID,requsterId);
        editor.commit();
    }
    public String getRequsterId()
    {
        return sharedPreferences.getString(REQUSTER_ID,"");
    }

    public void setMqttTopic(String mqttTopic)
    {
        editor.putString(MQTT_TOPIC,mqttTopic);
        editor.commit();
    }
    public String getMqttTopic()
    {
        return sharedPreferences.getString(MQTT_TOPIC,"");
    }

    @Override
    public FCM_Topics getFcmTopic() {
        Gson gson = new Gson();
        String jsonStr = sharedPreferences.getString(FCM_TOPIC, "");
        FCM_Topics fcm_topics = gson.fromJson(jsonStr, FCM_Topics.class);
        return fcm_topics;
    }

    @Override
    public void setFcmTopic(FCM_Topics fcmTopic) {

        Gson gson = new Gson();
        String jsonStr = gson.toJson(fcmTopic);
        editor.putString(FCM_TOPIC, jsonStr);
        editor.commit();
    }

    public void setCountryCode(String countryCode)
    {
        editor.putString(COUNTRY_CODE,countryCode);
        editor.commit();
    }

    public String getCountryCode()
    {
        return sharedPreferences.getString(COUNTRY_CODE,"");
    }
    public void setUserPhone(String phone)
    {
        editor.putString(PHONE,phone);
        editor.commit();
    }

    public String getUserPhoneNumber()
    {
        return sharedPreferences.getString(PHONE,"");
    }

    public void setMid(String mid)
    {
        editor.putString(MID,mid);
        editor.commit();
    }


    public String getMid()
    {
        return sharedPreferences.getString(MID, "0.0");
    }


    public Double getDistanceForLatLong()
    {
        return Double.valueOf(sharedPreferences.getString(DISTANCE_FOR_LAT_LONG,"5"));
    }

    public String getApiIntervalWhenBusy()
    {
        return sharedPreferences.getString(API_INTERVAL_WHEN_BUSY,"5");
    }

    public int getApiIntervalWhenFree()
    {
        return Integer.parseInt(sharedPreferences.getString(API_INTERVAL_WHEN_FREE,"5"));
    }

    public String getAppVersion()
    {
        return sharedPreferences.getString(APP_VERSION,"1");
    }

    public String getCalculateDistanceXMeter()
    {
        return sharedPreferences.getString(CALCULATE_DISTANCE_X_METER,"");
    }

    public String getCalculateTimeXSecond()
    {
        return sharedPreferences.getString(CALCULATE_TIME_X_SECOND,"");
    }

    public boolean getIsMandatory()
    {
        return sharedPreferences.getBoolean(IS_MANDATORY,false);
    }

    @Override
    public void setIsHelpCenterEnable(boolean isHelpCenterEnable) {
        editor.putBoolean(IsHelpCenterEnable,isHelpCenterEnable);
    }

    @Override
    public boolean getIsHelpCenterEnable() {
        return sharedPreferences.getBoolean(IsHelpCenterEnable,false);
    }

    public String getPresenceTime()
    {
        return sharedPreferences.getString(PRESENCE_TIME,"");
    }

    /**********************************************************************************************/

    public void setCalculateDistanceXMeter(String value)
    {
        editor.putString(CALCULATE_DISTANCE_X_METER,value);
        editor.commit();
    }

    public void setAppVersion(String version)
    {
        editor.putString(APP_VERSION,version);
        editor.commit();
    }

    public void setIsMandatory(boolean isMandatory)
    {
        editor.putBoolean(IS_MANDATORY,isMandatory);
        editor.commit();
    }
    public void setApiIntervalWhenFree(String apiIntervalWhenFree)
    {
        editor.putString(API_INTERVAL_WHEN_FREE,apiIntervalWhenFree);
        editor.commit();
    }

    public void setApiIntervalWhenBusy(String whenBusy)
    {
        editor.putString(API_INTERVAL_WHEN_BUSY,whenBusy);
        editor.commit();
    }

    public void setCalculateTimeXSecond(String timeXSecond)
    {
        editor.putString(CALCULATE_TIME_X_SECOND,timeXSecond);
        editor.commit();
    }

    public void setDistanceForLatLong(String latLong)
    {
        editor.putString(DISTANCE_FOR_LAT_LONG,latLong);
        editor.commit();
    }

    public void setPresenceTime(String presenceTime)
    {
        editor.putString(PRESENCE_TIME,presenceTime);
        editor.commit();
    }





    @Inject
    public PreferencesHelper(Context context) {

        int PRIVATE_MODE = 0;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        editor.commit();
        gson = new Gson();
    }


    @Override
    public void setLanguage(String value) {
        editor.putString("LANGUAGE_ID",value);
        editor.commit();
    }

    @Override
    public String getLanguage() {
        return sharedPreferences.getString("LANGUAGE_ID","en");
    }

    @Override
    public void setPushTopic(String pushTopic) {
        editor.putString(PUSH_TOPIC,pushTopic);
        editor.commit();
    }

    @Override
    public String getPushTopic() {
        return sharedPreferences.getString(PUSH_TOPIC,"");

    }

    @Override
    public void setProfilePic(String url) {
        editor.putString(PROFILE_PIC,url);
        editor.commit();
    }

    @Override
    public String getProfilePic() {
        return sharedPreferences.getString(PROFILE_PIC,null);
    }

    @Override
    public void setMyName(String name) {
        editor.putString(MY_NAME,name);
        editor.commit();
    }

    @Override
    public String getMyName() {
        return sharedPreferences.getString(MY_NAME,null);
    }

    @Override
    public void setVehicles(LoginData data) {

        Gson gson = new Gson();
        String jsonStr = gson.toJson(data);
        editor.putString(LOGIN_DATA, jsonStr);
        editor.commit();

    }

    @Override
    public LoginData getVehicles() {
        Gson gson = new Gson();
        String jsonStr = sharedPreferences.getString(LOGIN_DATA, "");
        LoginData loginData = gson.fromJson(jsonStr, LoginData.class);
        return loginData;
    }

    @Override
    public void setSelectedVehicle(VehiclesDetails vehiclesDetail) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(vehiclesDetail);
        editor.putString(VEHICLE_DATA, jsonStr);
        editor.commit();
    }

    @Override
    public VehiclesDetails getVehiclesDetail() {
        Gson gson = new Gson();
        String jsonStr = sharedPreferences.getString(VEHICLE_DATA, "");
        VehiclesDetails vehiclesDetail = gson.fromJson(jsonStr, VehiclesDetails.class);
        return vehiclesDetail;
    }


    @Override
    public void setPresenceChannel(String channel) {
        editor.putString(PRESENCE_CHN,channel);
        editor.commit();
    }

    @Override
    public String getPresenceChannel() {
        return sharedPreferences.getString(PRESENCE_CHN,null);
    }

    @Override
    public void setSessionToken(String token) {
        editor.putString(TOKEN,token);
        editor.commit();
    }

    @Override
    public String getSessionToken() {
        return sharedPreferences.getString(TOKEN,null);
    }

    @Override
    public void setDriverID(String id) {
        editor.putString(MID,id);
        editor.commit();
    }

    @Override
    public String getDriverID() {
        return sharedPreferences.getString(MID,null);
    }

    @Override
    public void setDriverChannel(String chn) {
        editor.putString(CHN,chn);
        editor.commit();
    }

    @Override
    public String getDriverChannel() {
        return sharedPreferences.getString(CHN,null);
    }

    @Override
    public void setVehicleId(String id) {
        editor.putString(VEH_ID,id);
        editor.commit();
    }

    @Override
    public String getVehicleId() {
        return sharedPreferences.getString(VEH_ID,null);
    }

    @Override
    public void setDeviceId(String deviceId) {
        editor.putString(DEVICE_ID,deviceId);
        editor.commit();
    }

    @Override
    public String getDeviceId() {
        return sharedPreferences.getString(DEVICE_ID,null);
    }

    @Override
    public void setDeviceType() {
        editor.putFloat(DEVICE_TYPE,1);
        editor.commit();
    }

    @Override
    public float getDeviceType() {
        return sharedPreferences.getFloat(DEVICE_TYPE, (float) 0.0);
    }

    @Override
    public void setFCMRegistrationId(String regId) {
        editor.putString(REG_ID,regId);
        editor.commit();
    }

    @Override
    public String getFCMRegistrationId() {
        return sharedPreferences.getString(REG_ID,"");
    }

    @Override
    public void setIsLogin(boolean isLogin) {
        editor.putBoolean(LOGGED_IN,isLogin);
        editor.commit();    }

    @Override
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(LOGGED_IN,false);
    }

    @Override
    public void setUserName(String value) {
        editor.putString("USER_NAME",value);
        editor.commit();
    }

    @Override
    public String getUserName() {
        return sharedPreferences.getString("USER_NAME","");
    }

    @Override
    public void setPassword(String value) {
        editor.putString("PASSWORD",value);
        editor.commit();
    }

    @Override
    public String getPassword() {
        return sharedPreferences.getString("PASSWORD","");
    }

    @Override
    public void setGmail(String gmail) {
        editor.putString("GMAIL",gmail);
        editor.commit();
    }

    @Override
    public String getGmail() {
        return sharedPreferences.getString("GMAIL","");
    }

    @Override
    public void setLoginResponseData(LoginData data) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(data);
        editor.putString("LoginDetail", jsonStr);
        editor.apply();
    }

    @Override
    public LoginData getSigninData() {
        Gson gson = new Gson();
        String jsonStr = sharedPreferences.getString("LoginDetail", "");
        LoginData loginResponseDetails = gson.fromJson(jsonStr, LoginData.class);
        return loginResponseDetails;
    }

    @Override
    public void setVehicleTypeData(VehicleTypes data) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(data);
        editor.putString("VehicleTypeData", jsonStr);
        editor.apply();
    }

    @Override
    public VehicleTypes getVehicleTypes() {
        Gson gson = new Gson();
        String jsonStr = sharedPreferences.getString("VehicleTypeData", "");
        VehicleTypes vehicleTypes = gson.fromJson(jsonStr, VehicleTypes.class);
        return vehicleTypes;
    }


    @Override
    public boolean getIsDriverLoggedIn() {
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false);
    }

    @Override
    public void setIsDriverLoggedIn(boolean isLoggedIn) {
        editor.putBoolean("IS_LOGGED_IN", isLoggedIn);
        editor.commit();
    }

    @Override
    public void clearSharedPredf() {
        editor.clear();
        editor.apply();
    }
    @Override
    public void setCurrLatitude(String latitude) {
        editor.putString(CURR_LAT, latitude);
        editor.commit();
    }

    @Override
    public Double getCurrLatitude()
    {
        return Double.valueOf(sharedPreferences.getString(CURR_LAT, "0.0"));
    }

    @Override
    public void setCurrLongitude(String longitude)
    {
        editor.putString(CURR_LONG, longitude);
        editor.commit();
    }

    @Override
    public Double getCurrLongitude()
    {
        return Double.valueOf(sharedPreferences.getString(CURR_LONG, "0.0"));
    }

    /*********************************************************************************************/
    @Override
    public void setDriverMeterDropLatitude(String latitude) {
        editor.putString(MeterBookingDropLat, latitude);
        editor.commit();
    }

    @Override
    public Double getDriverMeterDropLatitude() {
        return Double.valueOf(sharedPreferences.getString(MeterBookingDropLat, "0.0"));
    }

    @Override
    public void setDriverMeterDropLongitude(String Longitude) {
        editor.putString(MeterBookingDropLon, Longitude);
        editor.commit();
    }

    @Override
    public Double getDriverMeterDropLongitude() {
        return Double.valueOf(sharedPreferences.getString(MeterBookingDropLon, "0.0"));
    }

    @Override
    public void setDriverMeterDropAddress(String DropAddress) {
        editor.putString("DropAddress",DropAddress);
        editor.commit();
    }

    @Override
    public String getDriverMeterDropAddress() {
        return sharedPreferences.getString("DropAddress","");
    }

    @Override
    public void setMileageMetricUnit(String mileageMetricUnit) {
        editor.putString(MileageMetricUnit, mileageMetricUnit);
        editor.commit();
    }

    @Override
    public Double getMileageMetricUnit() {
        return Double.valueOf(sharedPreferences.getString(MileageMetricUnit, "1000.0"));
    }

    @Override
    public void setDistanceCalculated(String distance) {
        editor.putString(DistanceCalculated, distance);
        editor.commit();
    }

    @Override
    public Double getDistanceCalculated() {
        return Double.valueOf(sharedPreferences.getString(DistanceCalculated, "0.0"));
    }

    @Override
    public void setDistanceCalculatedShow(String distance) {
        editor.putString(DistanceCalculatedShow, distance);
        editor.commit();
    }

    @Override
    public Double getDistanceCalculatedShow() {

        String distance = sharedPreferences.getString(DistanceCalculatedShow, "0.0");
        Utility.printLog("the distance str is 11" + distance);
        if(distance.contains(",")){
            distance = distance.replace(",",".");
            Utility.printLog("the distance str is 22" + distance);
        }

        return Double.valueOf(String.format(Locale.US,distance));
    }

    @Override
    public void setBookingId(String bid) {
        editor.putString(BookingId,bid);
        editor.commit();
    }

    @Override
    public String getBookingId() {
        return sharedPreferences.getString(BookingId,"");
    }

    @Override
    public void setBookingStatus(String bookingStatus) {
        editor.putString(BookingStatus,bookingStatus);
        editor.commit();
    }

    @Override
    public String getBookingStatus() {
        return sharedPreferences.getString(BookingStatus,"");
    }


    @Override
    public String getServiceStatus() {
        return sharedPreferences.getString(ServiceStatus,"-1");
    }

    @Override
    public void setServiceStatus(String serviceStatus) {
        editor.putString(ServiceStatus,serviceStatus);
        editor.commit();
    }

    @Override
    public void setStripeKey(String stripeKey) {
        editor.putString(STRIPE_KEY, stripeKey);
        editor.commit();
    }

    @Override
    public String getStripeKey() {
        return sharedPreferences.getString(STRIPE_KEY, "");
    }


    @Override
    public CardDetailsDataModel getDefaultCardDetails()
    {
        String jsonString = sharedPreferences.getString(DEFAULT_CARD, "");
        return new Gson().fromJson(jsonString, CardDetailsDataModel.class);
    }
    @Override
    public void setDefaultCardDetails(CardDetailsDataModel defaultCardDetails)
    {
        if (defaultCardDetails != null) {
            String jsonString = new Gson().toJson(defaultCardDetails);
            editor.putString(DEFAULT_CARD, jsonString);
            editor.commit();
        }
        else
        {
            editor.putString(DEFAULT_CARD, null);
            editor.commit();
        }
    }

    @Override
    public void setRefereralCode(String refereralCode) {
        editor.putString(REFERALCODE,refereralCode);
        editor.commit();
    }

    @Override
    public String getRefereralCode() {
        return sharedPreferences.getString(REFERALCODE,"");
    }

    @Override
    public void setWalletBal(String balance) {
        editor.putString(WALLET_BAL,balance);
        editor.commit();
    }

    @Override
    public String getWalletBal() {
        return sharedPreferences.getString(WALLET_BAL,"0");
    }

    @Override
    public LanguagesList getLanguageSettings() {
        String jsonString = sharedPreferences.getString(LANGUAGE_SETTINGS, "");
        return new Gson().fromJson(jsonString, LanguagesList.class);
    }

    @Override
    public void setLanguageSettings(LanguagesList languageSettings) {
        if (languageSettings != null)
        {
            String jsonString = new Gson().toJson(languageSettings);
            editor.putString(LANGUAGE_SETTINGS, jsonString);
            editor.commit();
        }
        else
        {
            editor.putString(LANGUAGE_SETTINGS, "");
            editor.commit();
        }

    }

    @Override
    public void setGoogleServerKeys(List<String> distanceMatrixKeys)
    {
        this.googleServerKeys = new ArrayList<>(distanceMatrixKeys);
        String jsonString = gson.toJson(this.googleServerKeys);
        editor.putString(GOOGLE_SERVER_KEYS, jsonString);
        editor.commit();
    }

    @Override
    public ArrayList<String> getGoogleServerKeys()
    {
        String jsonString;
        if (googleServerKeys == null) {
            googleServerKeys = new ArrayList<>();
        }
        jsonString = sharedPreferences.getString(GOOGLE_SERVER_KEYS, "");
        googleServerKeys = new Gson().fromJson(jsonString, GOOGLE_SERVER_KEYS_TYPE);
        return googleServerKeys;
    }

    @Override
    public void setGoogleServerKey(String googleKey) {
        editor.putString(GOOGLE_KEY,googleKey);
        editor.commit();
    }

    @Override
    public String getGoogleServerKey() {
        return sharedPreferences.getString(GOOGLE_KEY,"");
    }

    @Override
    public void setRequesterId(String requesterId) {
        editor.putString(REQUESTER_ID, requesterId);
        editor.commit();
    }

    @Override
    public String getRequesterId() {
        return sharedPreferences.getString(REQUESTER_ID, "");
    }


    @Override
    public void setDriverMail(String email) {
        editor.putString(EMAIL,email);
        editor.commit();
    }

    @Override
    public String getDriverMail() {
        return sharedPreferences.getString(EMAIL,null);
    }



    public void setIsChatModuleEnable(boolean isChatModuleEnable)
    {
        editor.putBoolean(IS_CHAT_MODULE,isChatModuleEnable);
        editor.commit();
    }

    public boolean getIsChatModuleEnable()
    {
        return sharedPreferences.getBoolean(IS_CHAT_MODULE,false);
    }


    @Override
    public void setMqttUserName(String userName) {
        editor.putString(mqttUserName,userName);
        editor.commit();
    }

    @Override
    public String getMqttUserName() {
        return sharedPreferences.getString(mqttUserName,"");
    }

    @Override
    public void setMqttPassword(String password) {
        editor.putString(mqttPassword,password);
        editor.commit();
    }

    @Override
    public String getMqttPassword() {
        return sharedPreferences.getString(mqttPassword,"");
    }

    @Override
    public String getMqttHost() {
        return sharedPreferences.getString(mqttHost,"");
    }

    @Override
    public void setMqttHost(String host) {
        editor.putString(mqttHost,host);
        editor.commit();
    }

    @Override
    public String getMqttPort() {
        return sharedPreferences.getString(mqttPort,"");
    }

    @Override
    public void setMqttPort(String port) {
        editor.putString(mqttPort,port);
        editor.commit();
    }


    @Override
    public void setAuthTokenCall(String authTokenCall) {
        editor.putString(AUTH_TOKEN, authTokenCall);
        editor.commit();
    }

    @Override
    public String getAuthTokenCall() {
        return sharedPreferences.getString(AUTH_TOKEN, "");
    }


    @Override
    public void setCustomerName(String customerName) {
        editor.putString(CUSTOMER_NAME,customerName);
        editor.commit();
    }

    @Override
    public String getCustomerName() {
        return sharedPreferences.getString(CUSTOMER_NAME,"");
    }

    @Override
    public void setCustomerPic(String customerPic) {
        editor.putString(CUSTOMER_PIC,customerPic);
        editor.commit();
    }

    @Override
    public String getCustomerPic() {
        return sharedPreferences.getString(CUSTOMER_PIC,"");
    }


    @Override
    public void setGoogleMapKey(String googleMapKey) {
        editor.putString(googleMap_Key,googleMapKey);
        editor.commit();
    }

    @Override
    public String getGoogleMapKey() {
        return sharedPreferences.getString(googleMap_Key,"");
    }

    @Override
    public void setGoogleMapKeyTopic(String googleMapKeyTopic) {
        editor.putString(GoogleMapKeyTopic,googleMapKeyTopic);
        editor.commit();
    }

    @Override
    public String getGoogleMapKeyTopic() {
        return sharedPreferences.getString(GoogleMapKeyTopic,"");
    }
    public void setBirdViewTopic(String mqttTopic) {
        editor.putString(BIRDSVIEW_TOPIC, mqttTopic);
        editor.commit();
    }

    public String getBirdViewTopic() {
        return sharedPreferences.getString(BIRDSVIEW_TOPIC, "");
    }

    public void setDispatcherTopic(String mqttTopic) {
        editor.putString(DISPATCHER_TOPIC, mqttTopic);
        editor.commit();
    }

    public String getDispatcherTopic() {
        return sharedPreferences.getString(DISPATCHER_TOPIC, "");
    }

    @Override
    public void setDistanceForLatLongMax(String latLong) {
        editor.putString(DISTANCEFORMAXRANGE, latLong);
        editor.commit();
    }

    @Override
    public void setDriverApiDistanceWhenFree(String driverApiDistanceWhenFree) {
        editor.putString(DRIVERAPIDISTANCEWHENFREE, driverApiDistanceWhenFree);
        editor.commit();
    }

    @Override
    public Double getDriverApiDistanceWhenFree() {
        return Double.valueOf(sharedPreferences.getString(DRIVERAPIDISTANCEWHENFREE,"50"));
    }

    @Override
    public void setStripImage(String imageUrl) {
        editor.putString(STRIP_IMAGE,imageUrl);
        editor.commit();
    }

    @Override
    public String getStripImage() {
        return sharedPreferences.getString(STRIP_IMAGE,"");
    }
}
