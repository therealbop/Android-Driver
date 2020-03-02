package com.karry.utility;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by embed on 18/5/16.
 */
public class SessionManager {


    private static SessionManager sessionManager;
    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences.Editor editor = null;
    public final String TIME_WHILE_PAUSED = "time_while_paused";
    public final String TIME_ELAPSED = "time_elapsed";
    public final String IS_TIMER_STARTED = "timer_started";
    private final String LOGGED_IN = "login";
    private final String PUSH_TOKEN = "push_token";
    private final String DEVICE_ID = "device_id";
    private final String SESSION_TOKEN = "session_token";
    private final String PUSH_TOPIC = "push_topic";
    private final String PUBLISH_KEY = "publish_key";
    private final String SUBSCRIBE_KEY = "subscribe_key";
    private final String SERVER_CHANNEL = "server_chn";
    private final String PRESENCE_CHANNEL = "presence_chn";
    private final String VEHICLE = "vehicle";
    private final String VEHICLE_IMAGE = "vehicle_image";
    private final String DRIVER_STATUS = "driver_status";
    private final String DRIVER_UUID = "driver_uuid";
    private final String DRIVER_MID = "driver_mid";
    private final String DRIVER_CHANNEL = "driver_channel";
    private final String TYPE_ID = "type_id";
    private final String DRIVER_BEGIN_JOURNEY = "isBeginJourney";
    private final String DISTANCE_IN_DOUBLE = "distance_double";
    private final String ROUTE_ARRAY = "route_array";
    private final String APPOINTMENT_STATUS = "appointment_status";
    private final String BID = "b_id";
    private final String LATITUDE = "lat";
    private final String LONGITUDE = "lng";
    private final String MIN_RADIOUS_PUBLISH = "min_Rad_publish";
    private final String INTERVAL = "interval";
    private final String BOOKING_COMPLETED = "booking_completed";
    private final String PickUpRouteArray = "pickup_array";
    private final String ArrivedRouteArray = "arrived_array";
    private final String MinDistForRouteArray = "min_dist_array";
    private final String DISTANCE = "distance";
    private final String BOOKING_CHANNEL = "booking_channel";
    private final String SID = "s_id";
    private final String LAST_BOOKING_ID = "last_booking_id";
    private final String PASSWORD = "password";
    private final String FIRSTNAME = "firstname";
    private final String PROFILE_PIC = "profile_picture";
    private final String REFERRAL = "referral_code";
    private final String currencySymbol = "currencySymbol";
    private final String veh_id = "veh_id";
    private final String mileage_metric = "mileage_metric";
    private final String presence_interval = "presence_interval";
    private final String DISTANCE_CONVERSION_UNIT = "distance_conversion";

    /***********************************************************************************************/

    public SessionManager(Context context) {
        sharedPreferences = getSharedPref(context);
        editor = getEditor();
    }

    /**********************************************************************************************/


    public static SharedPreferences getSharedPref(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(VariableConstant.PREF_NAME, Context.MODE_MULTI_PROCESS);

        }
        return sharedPreferences;
    }
    /**********************************************************************************************/
    /**********************************************************************************************/

    private static SharedPreferences.Editor getEditor() {
        if (editor == null) {
            editor = sharedPreferences.edit();
            editor.commit();
        }
        return editor;
    }

    /**********************************************************************************************/

    public static SessionManager getSessionManager(Context context) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(context);

        }
        return sessionManager;
    }

    /**********************************************************************************************/

    public void clearSharedPredf() {
        editor.clear();
        editor.apply();
    }

    /**********************************************************************************************/

    public boolean isLogin() {
        return sharedPreferences.getBoolean(LOGGED_IN, false);
    }

    public void setIsLogin(boolean value) {
        editor.putBoolean(LOGGED_IN, value);
        editor.commit();
    }

    public String getPushToken() {
        return sharedPreferences.getString(PUSH_TOKEN, null);
    }

    public void setPushToken(String pushToken) {
        editor.putString(PUSH_TOKEN, pushToken);
        editor.commit();
    }

    public String getDeviceId() {
        return sharedPreferences.getString(DEVICE_ID, null);
    }

    public void setDeviceId(String deviceId) {
        editor.putString(DEVICE_ID, deviceId);
        editor.commit();
    }

    public String getSessionToken() {
        return sharedPreferences.getString(SESSION_TOKEN, null);
    }

    public void setSessionToken(String sessionToken) {
        editor.putString(SESSION_TOKEN, sessionToken);
        editor.commit();
    }

    public String getPushTopic() {
        return sharedPreferences.getString(PUSH_TOPIC, "");
    }

    public void setPushTopic(String sessionToken) {
        editor.putString(PUSH_TOPIC, sessionToken);
        editor.commit();
    }

    public String getPublishKey() {
        return sharedPreferences.getString(PUBLISH_KEY, null);
    }

    public void setPublishKey(String publishKey) {
        editor.putString(PUBLISH_KEY, publishKey);
        editor.commit();
    }

    public String getSubscribeKey() {
        return sharedPreferences.getString(SUBSCRIBE_KEY, null);
    }

    public void setSubscribeKey(String subscribeKey) {
        editor.putString(SUBSCRIBE_KEY, subscribeKey);
        editor.commit();
    }

    public String getServerChannel() {
        return sharedPreferences.getString(SERVER_CHANNEL, null);
    }

    public void setServerChannel(String serverChannel) {
        editor.putString(SERVER_CHANNEL, serverChannel);
        editor.commit();
    }

    public String getPresenceChannel() {
        return sharedPreferences.getString(PRESENCE_CHANNEL, null);
    }

    public void setPresenceChannel(String presenceChannel) {
        editor.putString(PRESENCE_CHANNEL, presenceChannel);
        editor.commit();
    }

    public String getVehicle() {
        return sharedPreferences.getString(VEHICLE, null);
    }

    public void setVehicle(String vehicle) {
        editor.putString(VEHICLE, vehicle);
        editor.commit();
    }

    public String getVehicleImage() {
        return sharedPreferences.getString(VEHICLE_IMAGE, null);
    }

    public void setVehicleImage(String vehicle) {
        editor.putString(VEHICLE_IMAGE, vehicle);
        editor.commit();
    }

    public int getDriverStatus() {
        return sharedPreferences.getInt(DRIVER_STATUS, 0);
    }

    public void setDriverStatus(int driverStatus) {
        editor.putInt(DRIVER_STATUS, driverStatus);
        editor.commit();
    }

    public String getDriverUuid() {
        return sharedPreferences.getString(DRIVER_UUID, null);
    }

    public void setDriverUuid(String driverUuid) {
        editor.putString(DRIVER_UUID, driverUuid);
        editor.commit();
    }

    public String getMid() {
        return sharedPreferences.getString(DRIVER_MID, null);
    }

    public void setMid(String driverUuid) {
        editor.putString(DRIVER_MID, driverUuid);
        editor.commit();
    }

    public String getDriverChannel() {
        return sharedPreferences.getString(DRIVER_CHANNEL, null);
    }

    public void setDriverChannel(String driverChannel) {
        editor.putString(DRIVER_CHANNEL, driverChannel);
    }

    public String getTypeId() {
        return sharedPreferences.getString(TYPE_ID, null);
    }

    public void setTypeId(String typeId) {
        editor.putString(TYPE_ID, typeId);
        editor.commit();
    }

    /**********************************************************************************************/
    public boolean getBeginJourney() {
        return sharedPreferences.getBoolean(DRIVER_BEGIN_JOURNEY, false);
    }

    public void setBeginJourney(boolean isAccept) {
        editor.putBoolean(DRIVER_BEGIN_JOURNEY, isAccept);
        editor.commit();
    }

    public double getDistanceInDouble() {
        String distancestr = sharedPreferences.getString(DISTANCE_IN_DOUBLE, "0.0");
        double distanceDouble = 0.0;
        try {
            distanceDouble = Double.parseDouble(distancestr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return distanceDouble;
    }

    public void setDistanceInDouble(String distance) {
        editor.putString(DISTANCE_IN_DOUBLE, distance);
        editor.commit();
    }

    public String getRouteArray() {
        return sharedPreferences.getString(ROUTE_ARRAY, "");
    }

    public void setRouteArray(String route) {
        editor.putString(ROUTE_ARRAY, route);
        editor.commit();
    }

    public int getAppointmentStatus() {
        return sharedPreferences.getInt(APPOINTMENT_STATUS, 4);
    }

    public void setAppointmentStatus(int value) {
        editor.putInt(APPOINTMENT_STATUS, value);
        editor.apply();
    }

    public String getBid() {
        return sharedPreferences.getString(BID, "");
    }

    public void setBid(String bid) {
        editor.putString(BID, bid);
        editor.apply();
    }

    public double getDriverCurrentLat() {
        String driverCurrentLatStr = sharedPreferences.getString(LATITUDE, "0.0");
        double driverCurrentLat = 0.0;
        try {
            driverCurrentLat = Double.parseDouble(driverCurrentLatStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return driverCurrentLat;
    }

    // to store and retrieve current Latitude
    public void setDriverCurrentLat(String latitude) {
        editor.putString(LATITUDE, latitude);
        editor.commit();
    }

    public double getDriverCurrentLng() {
        String driverCurrentLongiStr = sharedPreferences.getString(LONGITUDE, "0.0");
        double driverCurrentLongi = 0.0;
        try {
            driverCurrentLongi = Double.parseDouble(driverCurrentLongiStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return driverCurrentLongi;
    }

    /**********************************************************************************************/
    // to store and retrieve current Longitude
    public void setDriverCurrentLng(String latitude) {
        editor.putString(LONGITUDE, latitude);
        editor.commit();
    }

    public long getMinRadiusForPubnubPublish() {
        return sharedPreferences.getLong(MIN_RADIOUS_PUBLISH, 2);
    }

    public void setMinRadiusForPubnubPublish(long radius) {
        editor.putLong(MIN_RADIOUS_PUBLISH, radius);
        editor.commit();
    }

    public float getPubnubScheduleInterval() {
        if (sharedPreferences.getFloat(INTERVAL, 5000.0f) != 0.0) {
            return sharedPreferences.getFloat(INTERVAL, 5000.0f);
        } else {
            return 5000.0f;
        }

    }

    public void setPubnubScheduleInterval(float interval) {
        editor.putFloat(INTERVAL, interval);
        editor.commit();
    }

    public boolean isBookingCompleted() {
        return sharedPreferences.getBoolean(BOOKING_COMPLETED, false);
    }

    public void setBookingCompleted(boolean bookingCompleted) {
        editor.putBoolean(BOOKING_COMPLETED, bookingCompleted);
        editor.commit();
    }

    public String getPickUpRouteArray() {
        return sharedPreferences.getString(PickUpRouteArray, "");
    }

    public void setPickUpRouteArray(String route) {
        editor.putString(PickUpRouteArray, route);
        editor.commit();
    }

    public String getArrivedRouteArray() {
        return sharedPreferences.getString(ArrivedRouteArray, "");
    }

    public void setArrivedRouteArray(String route) {
        editor.putString(ArrivedRouteArray, route);
        editor.commit();
    }

    public long getMinDistForRouteArray() {
        return sharedPreferences.getLong(MinDistForRouteArray, 20);
    }

    public void setMinDistForRouteArray(long dist) {
        editor.putLong(MinDistForRouteArray, dist);
        editor.commit();
    }

    public String getBookingChannel() {
        return sharedPreferences.getString(BOOKING_CHANNEL, null);
    }

    public void setBookingChannel(String bookingChannel) {
        editor.putString(BOOKING_CHANNEL, bookingChannel);
        editor.commit();
    }

    public String getSID() {
        return sharedPreferences.getString(SID, null);
    }

    public void setSID(String sid) {
        editor.putString(SID, sid);
        editor.commit();
    }

    public String getDistance() {
        return sharedPreferences.getString(DISTANCE, null);
    }

    public void setDistance(String distance) {
        editor.putString(DISTANCE, distance);
        editor.commit();
    }

    public long getLastBookingId() {
        return sharedPreferences.getLong(LAST_BOOKING_ID, 0);
    }

    public void setLastBookingId(long lastBookingId) {
        editor.putLong(LAST_BOOKING_ID, lastBookingId);
        editor.commit();
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, null);
    }

    public void setPassword(String password) {
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public String getMyName() {
        return sharedPreferences.getString(FIRSTNAME, null);
    }

    public void setMyName(String name) {
        editor.putString(FIRSTNAME, name);
        editor.commit();
    }

    public String getProfilePic() {
        return sharedPreferences.getString(PROFILE_PIC, null);
    }

    public void setProfilePic(String url) {
        editor.putString(PROFILE_PIC, url);
        editor.commit();
    }

    public String getReferralCode() {
        return sharedPreferences.getString(REFERRAL, null);
    }

    public void setReferralCode(String url) {
        editor.putString(REFERRAL, url);
        editor.commit();
    }

    public String getcurrencySymbol() {
        return sharedPreferences.getString(currencySymbol, "$");
    }

    public void setcurrencySymbol(String currencySymbol1) {
        editor.putString(currencySymbol, currencySymbol1);
        editor.commit();
    }

    public String getvehid() {
        return sharedPreferences.getString(veh_id, "");
    }

    public void setvehid(String veh_id1) {
        editor.putString(veh_id, veh_id1);
        editor.commit();
    }

    public String getDocumentID() {
        return sharedPreferences.getString("COUCH_DAYRUNNER", "");
    }

    public void setDocumentID(String id) {
        editor.putString("COUCH_DAYRUNNER", id);
        editor.commit();
    }

    public String getDocumentIDLatLng() {
        return sharedPreferences.getString("COUCH_DAYRUNNER_LATLNG", "");
    }

    public void setDocumentIDLatLng(String id) {
        editor.putString("COUCH_DAYRUNNER_LATLNG", id);
        editor.commit();
    }

    public String getmileage_metric() {
        return sharedPreferences.getString(mileage_metric, "Km");
    }

    public void setmileage_metric(String mileage_metric1) {
        editor.putString(mileage_metric, mileage_metric1);
        editor.commit();
    }

    public int getpresence_interval() {
        return sharedPreferences.getInt(presence_interval, 60);
    }

    public void setpresence_interval(int presence_interval1) {
        editor.putInt(presence_interval, presence_interval1);
        editor.commit();
    }

    public String getDISTANCE_CONVERSION_UNIT() {
        return sharedPreferences.getString(DISTANCE_CONVERSION_UNIT, "0.001");
    }

    public void setDISTANCE_CONVERSION_UNIT(String DISTANCE_CONVERSION_UNIT1) {
        editor.putString(DISTANCE_CONVERSION_UNIT, DISTANCE_CONVERSION_UNIT1);
        editor.commit();
    }

    public String getBookings() {
        return sharedPreferences.getString("BOOKINGS", "");
    }

    public void setBookings(String bookings) {
        editor.putString("BOOKINGS", bookings);
        editor.commit();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public long getTimeWhilePaused() {
        return sharedPreferences.getLong(TIME_WHILE_PAUSED, 0);
    }

    public void setTimeWhilePaused(long time) {
        editor.putLong(TIME_WHILE_PAUSED, time);
        editor.commit();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    public long getElapsedTime() {
        return sharedPreferences.getLong(TIME_ELAPSED, 0);
    }

    public void setElapsedTime(long time) {
        editor.putLong(TIME_ELAPSED, time);
        editor.commit();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean isTimerStarted() {

        return sharedPreferences.getBoolean(IS_TIMER_STARTED, false);
    }

    public void setTimerStarted(boolean started) {
        editor.putBoolean(IS_TIMER_STARTED, started);
        editor.commit();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * This method is used to store the currencySymbol
     * @param currencySymbol, containing currencySymbol.
     */
    public void setCurrencySymbol(String currencySymbol)
    {
        editor.putString("currencySymbol", currencySymbol);
        editor.commit();
    }

    /**
     * This method is used to getting the currencySymbol.
     * @return, returns currencySymbol.
     */
    public String getCurrencySymbol()
    {
        return sharedPreferences.getString("currencySymbol", "");
    }

    /**
     * This method is used to getting the LastCard Number.
     * @return, returns LastCard Number.
     */
    public String getLastCardNumber()
    {
        return sharedPreferences.getString("LastCardNumber", "");
    }

    /**
     * This method is used to store the LastCard Number.
     * @param LastCardNumber, containing lastCard Number.
     */
    public void setLastCardNumber(String LastCardNumber)
    {
        editor.putString("LastCardNumber", LastCardNumber);
        editor.commit();
    }

    /**
     * This method is used to store the CardType.
     * @param CardType, containing CardType, debit/ credit card type.
     */
    public void setCardType(String CardType)
    {
        editor.putString("CardType", CardType);
        editor.commit();
    }
    /**
     * This method is used to getting the LastCard Image.
     * @return, returns CardType menase it is credit/ debit card..
     */
    public String getCardType()
    {
        return sharedPreferences.getString("CardType", "");
    }

    /**
     * This method is used to store the LastCard Image.
     * @param LastCardImage, containing lastCard Image.
     */
    public void setLastCardImage(String LastCardImage)
    {
        editor.putString("LastCardImage", LastCardImage);
        editor.commit();
    }
    /**
     * This method is used to getting the LastCard Image.
     * @return, returns LastCard Image.
     */
    public String getLastCardImage()
    {
        return sharedPreferences.getString("LastCardImage", "");
    }

   /* public void setWalletSettings(WalletDataPojo walletDataPojo)
    {
        if(walletDataPojo != null)
        {
            String jsonString =  new Gson().toJson(walletDataPojo);
            //Log.d("SessionManager", "setDriverRatingData jsonString: "+jsonString);
            editor.putString("WALLET_SETTINGS", jsonString);
            editor.commit();
        }
    }

    public WalletDataPojo getWalletSettings()
    {
        String jsonString =  sharedPreferences.getString("WALLET_SETTINGS", "");
        //Log.d("SessionManager", "setDriverRatingData jsonString: "+jsonString);
        WalletDataPojo walletDataPojo = new Gson().fromJson(jsonString, WalletDataPojo.class);
        return walletDataPojo;
    }*/

    public String getImageUrl(){
        return sharedPreferences.getString("imageUrl","");
    }
    public void setImageUrl(String got_ImageUrl){
        editor.putString("imageUrl",got_ImageUrl);
        editor.commit();
    }

    /**
     * This method is used to getting the LastCard ID.
     * @return, returns LastCard ID.
     */
    public String getLastCard()
    {
        return sharedPreferences.getString("LastCard", "");
    }

    /**
     * This method is used to store the LastCard id.
     * @param LastCard, containing lastCard id.
     */
    public void setLastCard(String LastCard)
    {
        editor.putString("LastCard", LastCard);
        editor.commit();
    }

    public void setPushTopics(ArrayList<String> pushTopics)
    {

        editor.putString("PUSH_TOPICS", String.valueOf(pushTopics));
        editor.commit();
    }

    public ArrayList<String> getPushTopics()
    {
        String pushTopics = sharedPreferences.getString("PUSH_TOPICS","");

        if(pushTopics != null && !pushTopics.isEmpty())
        {
            pushTopics = pushTopics.substring(1,pushTopics.length()-1);
            pushTopics = pushTopics.replaceAll(" ","");
            ArrayList<String> pushTopicsAL = new ArrayList<String>(Arrays.asList(pushTopics.split(",")));
            return pushTopicsAL;
        }
        else
        {
            return new ArrayList<String>();
        }
    }

    /**
     * This method is used to store the stripeKey
     * @param stripeKey, containing stripeKey.
     */
    public void setStripeKey(String stripeKey)
    {
        editor.putString("stripeKey", stripeKey);
        editor.commit();
    }

    /**
     * This method is used to getting the stripeKey.
     * @return, returns stripeKey.
     */
    public String getStripeKey()
    {
        return sharedPreferences.getString("stripeKey", "");
    }
    /**
     * needed to get transaction history... getting value in login and signUp response
     * This method is used for getting the sid.
     * @return LaterTime.
     */
    public String getSid()
    {
        return sharedPreferences.getString("SID", "");
    }

    /**
     * This method is used for setting the sid retrieved from login and signup api.
     * @param sid Sid.
     */
    public void setSid(String sid)
    {
        editor.putString("SID", sid);
        editor.commit();
    }
    ///////////////////////
    public String getWalletAmount()
    {
        return sharedPreferences.getString("WalletAmount", "");
    }

    public void setWalletAmount(String amt)
    {
        editor.putString("WalletAmount", amt);
        editor.commit();
    }
    public String getSoftLimit()
    {
        return sharedPreferences.getString("SoftLimit", "");
    }

    public void setSoftLimit(String amt)
    {
        editor.putString("SoftLimit", amt);
        editor.commit();
    }

    public String getHardLimit()
    {
        return sharedPreferences.getString("HardLimit", "");
    }

    public void setHardLimit(String amt)
    {
        editor.putString("HardLimit", amt);
        editor.commit();
    }
    public boolean isWalletEnabled() {
        return sharedPreferences.getBoolean("WalletEnabled", false);
    }

    public void setWalletEnabled(boolean value) {
        editor.putBoolean("WalletEnabled", value);
        editor.commit();
    }
}
