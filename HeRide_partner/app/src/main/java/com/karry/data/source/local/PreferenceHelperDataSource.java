package com.karry.data.source.local;

import com.karry.authentication.login.model.LanguagesList;
import com.karry.authentication.login.model.LoginData;
import com.karry.authentication.login.model.VehiclesDetails;
import com.karry.pojo.VehicleTypes;
import com.karry.service.FCM_Topics;
import com.karry.side_screens.wallet.changeCard.model.CardDetailsDataModel;

import java.util.List;

/**
 * Created by DELL on 21-12-2017.
 */

public interface PreferenceHelperDataSource {

    void setDeviceType();

    float getDeviceType();

    void setCalculateDistanceXMeter(String value);

    void setAppVersion(String version);

    void setIsMandatory(boolean isMandatory);

    boolean getIsMandatory();

    void setIsHelpCenterEnable(boolean isHelpCenterEnable);

    boolean getIsHelpCenterEnable();

    void setApiIntervalWhenFree(String apiIntervalWhenFree);

    void setApiIntervalWhenBusy(String whenBusy);

    void setCalculateTimeXSecond(String timeXSecond);

    void setDistanceForLatLong(String latLong);

    void setPresenceTime(String presenceTime);

    void setVehicleType(String vehicleType);

    String getVehicleType();

    void setVehicleMapIcon(String vehicleMapIcon);

    String getVehicleMapIcon();

    void setVehicleMake(String vehicleMake);

    String getVehicleMake();

    void setVehicleModele(String vehicleModele);

    String getVehicleModel();

    void setId(String id);

    String getId();

    void setServices(String services);

    String getServices();

    void setVehicleImgUrl(String imgUrl);

    String getVehicleImgUrl();

    void setTypeId(String typeId);

    String getTypeId();

    void setGoodtype(String goodtype);

    String getGoodtype();

    void setPlateNum(String plateNum);

    String getPlateNum();

    void setRequsterId(String requsterId);

    String getRequsterId();

    void setMqttTopic(String mqttTopic);

    String getMqttTopic();

    FCM_Topics getFcmTopic();

    void setFcmTopic(FCM_Topics fcmTopic);

    void setCountryCode(String countryCode);

    String getCountryCode();

    void setUserPhone(String phone);

    String getUserPhoneNumber();

    void setMid(String mid);

    String getMid();

    Double getDistanceForLatLong();

    String getApiIntervalWhenBusy();

    int getApiIntervalWhenFree();

    String getAppVersion();

    String getCalculateDistanceXMeter();

    String getCalculateTimeXSecond();



    String getPresenceTime();


    /**
     * <h2>setUserName</h2>
     * This method is used to set the language id
     *
     * @param value of the userName
     */
    void setLanguage(String value);

    /**
     * <h2>getLanguage</h2>
     * This method is used to get the language id
     */
    String getLanguage();

    /**
     * <h2>setPushTopic</h2>
     * This method is used to set the url
     *
     * @param pushTopic pushTopic
     */
    void setPushTopic(String pushTopic);

    /**
     * <h2>getPushTopic</h2>
     * This method is used to get the pushTopic
     */
    String getPushTopic();

    /**
     * <h2>setProfilePic</h2>
     * This method is used to set the url
     *
     * @param url url
     */
    void setProfilePic(String url);

    /**
     * <h2>getProfilePic</h2>
     * This method is used to get the url
     */
    String getProfilePic();

    /**
     * <h2>setMyName</h2>
     * This method is used to set the Driver Name
     *
     * @param name name
     */
    void setMyName(String name);

    /**
     * <h2>getMyName</h2>
     * This method is used to get the Driver Name
     */
    String getMyName();

    /**
     * <h2>setVehicles</h2>
     * This method is used to set the vehicles
     *
     * @param data data
     */
    void setVehicles(LoginData data);

    /**
     * <h2>getVehicles</h2>
     * This method is used to get the vehicles
     */
    LoginData getVehicles();

    /**
     * <h1>setSelectedVehicle</h1>
     * <p>store the selected vehicle data</p>
     * @param vehiclesDetail :
     */
    void setSelectedVehicle(VehiclesDetails vehiclesDetail);

    /**
     * <h2>getVehiclesDetail</h2>
     * <p>This method is used to get the vehicle data which stored</p>
     */
    VehiclesDetails getVehiclesDetail();

    /**
     * <h2>setPresenceChannel</h2>
     * This method is used to set the presence channel
     *
     * @param channel channel
     */
    void setPresenceChannel(String channel);

    /**
     * <h2>getPresenceChannel</h2>
     * This method is used to get the presence channel
     */
    String getPresenceChannel();

    /**
     * <h2>.setSessionToken</h2>
     * This method is used to set the token
     *
     * @param token ID
     */
    void setSessionToken(String token);

    /**
     * <h2>getSessionToken</h2>
     * This method is used to get the token
     */
    String getSessionToken();

    /**
     * <h2>.setDriverChannel</h2>
     * This method is used to set the Driver ID
     *
     * @param id ID
     */
    void setDriverID(String id);

    /**
     * <h2>getDriverID</h2>
     * This method is used to get the Driver ID
     */
    String getDriverID();

    /**
     * <h2>.setDriverChannel</h2>
     * This method is used to set the channel name
     *
     * @param chn channel
     */
    void setDriverChannel(String chn);

    /**
     * <h2>getDriverChannel</h2>
     * This method is used to get the channel
     */
    String getDriverChannel();

    /**
     * <h2>.setVehicleId</h2>
     * This method is used to set the vehicle Id
     *
     * @param id vehicle Id
     */
    void setVehicleId(String id);

    /**
     * <h2>getVehicleId</h2>
     * This method is used to get the vehicle Id
     */
    String getVehicleId();

    /**
     * <h2>setDeviceId</h2>
     * This method is used to set the Device ID
     *
     * @param deviceId Device ID
     */
    void setDeviceId(String deviceId);

    /**
     * <h2>getDeviceId</h2>
     * This method is used to get the Device ID
     */
    String getDeviceId();

    /**
     * <h2>.setFCMRegistrationId</h2>
     * This method is used to set the FCM push token
     *
     * @param regId FCM push token
     */
    void setFCMRegistrationId(String regId);

    /**
     * <h2>getFCMRegistrationId</h2>
     * This method is used to get the FCM push token
     */
    String getFCMRegistrationId();

    /**
     * <h2>setIsLogin</h2>
     * This method is used to set if the user is logged in
     *
     * @param isLogin true if logged in else false
     */
    void setIsLogin(boolean isLogin);

    /**
     * <h2>isLoggedIn</h2>
     * This method is used to get if the user is logged in
     *
     * @return true if logged in else false
     */
    boolean isLoggedIn();

    /**
     * <h2>setUserName</h2>
     * This method is used to set the user name
     *
     * @param value of the userName
     */
    void setUserName(String value);

    /**
     * <h2>getUserName</h2>
     * This method is used to get the UserName
     */
    String getUserName();

    /**
     * <h2>setPassword</h2>
     * This method is used to set the user password
     *
     * @param value password
     */
    void setPassword(String value);

    /**
     * <h2>getPassword</h2>
     * This method is used to get the password of the user
     */
    String getPassword();

    /**
     * <h2>setGmail</h2>
     * This method is used to store user gmail
     */

    void setGmail(String gmail);

    /**
     * <h2>getGmail</h2>
     * This method is used to get the gmail of the user
     */
    String getGmail();

    /**
     * <h2>clearSharedPredf</h2>
     * This method is used to clear the shared prefernce
     */


    void setLoginResponseData(LoginData data);

    LoginData getSigninData();

    void setVehicleTypeData(VehicleTypes data);

    VehicleTypes getVehicleTypes();

    /**********************************************************************************************/
    boolean getIsDriverLoggedIn();

    void setIsDriverLoggedIn(boolean isLoggedIn);


    void clearSharedPredf();

    /**
     * setCurrLatitude     * This method is used to set the curr latitude
     */
    void setCurrLatitude(String latitude);

    /**
     * getCurrLatitude     * This method is used to get the curr latitude
     */
    Double getCurrLatitude();

    /**
     * setCurrLongitude     * This method is used to set the curr longitude
     */
    void setCurrLongitude(String longitude);

    /**
     * getCurrLongitude     * This method is used to get the curr longitude
     */
    Double getCurrLongitude();


    public String getCurrencySymbol();

    void setCurrencySymbol(String currencySymbol);

    void getMileageMetric(String currencySymbol);

    String getMileageMetric();


    /**
     * <h2>setCarIcon</h2>
     * This method is used to set the url of car icon
     *
     * @param url url
     */
    void setCarIcon(String url);

    /**
     * <h2>getCarIcon</h2>
     * This method is used to get the url of Car icon
     */
    String getCarIcon();


    /**********************************************************************************************/

    /**
     * setDriverMeterDropLatitude     * This method is used to set the Drop latitude of Driver Meter
     */
    void setDriverMeterDropLatitude(String latitude);

    /**
     * getDriverMeterDropLatitude     * This method is used to get the Drop latitude of Driver Meter
     */
    Double getDriverMeterDropLatitude();

    /**
     * setDriverMeterDropLongitude     * This method is used to set the Drop Longitude of Driver Meter
     */
    void setDriverMeterDropLongitude(String Longitude);

    /**
     * getDriverMeterDropLongitude     * This method is used to get the Drop Longitude of Driver Meter
     */
    Double getDriverMeterDropLongitude();

    /**
     * setDriverMeterDropAddress     * This method is used to set the Drop Address of Driver Meter
     */
    void setDriverMeterDropAddress(String DropAddress);

    /**
     * getDriverMeterDropAddress     * This method is used to get the Drop Address of Driver Meter
     */
    String getDriverMeterDropAddress();

    /**
     * setMileageMetricUnit     * This method is used to set the MileageMetricUnit
     */
    void setMileageMetricUnit(String mileageMetricUnit);

    /**
     * getMileageMetricUnit     * This method is used to get the MileageMetricUnit
     */
    Double getMileageMetricUnit();

    /**
     * setDistanceCalculated     * This method is used to set the DistanceCalculated
     */
    void setDistanceCalculated(String distance);

    /**
     * getDistanceCalculated     * This method is used to get the DistanceCalculated
     */
    Double getDistanceCalculated();

    /**
     * setDistanceCalculatedShow     * This method is used to set the DistanceCalculated
     */
    void setDistanceCalculatedShow(String distance);

    /**
     * getDistanceCalculatedShow     * This method is used to get the DistanceCalculated
     */
    Double getDistanceCalculatedShow();

    /**
     * setBookingId     * This method is used to set the Booking id
     */
    void setBookingId(String bid);

    /**
     * getBookingId     * This method is used to get the Booking ID
     */
    String getBookingId();

    /**
     * setMasterStatus     * This method is used to set the Master Status
     */
    void setBookingStatus(String bookingStatus);

    /**
     * getServiceStatus     * This method is used to get the towtray Status
     */
    String getServiceStatus();

    /**
     * setServiceStatus     * This method is used to set the towtray Status
     */
    void setServiceStatus(String serviceStatus);

    /**
     * getMasterStatus     * This method is used to get the Master Status
     */
    String getBookingStatus();



    /**
     * setStripeKey
     * This method is used to setList the stripeKey for Stripe payment
     * @param stripeKey stripeKey for Stripe payment
     */
    void setStripeKey(String stripeKey);


    /**
     * getStripeKey
     * This method is used to get the the stripeKey for Stripe payment
     */
    String getStripeKey();



    /**
     * <h2>getDefaultCardDetails</h2>
     * used to get the default card details
     * @return returns the default card details
     */
    CardDetailsDataModel getDefaultCardDetails();

    /**
     * <h2>setDefaultCardDetails</h2>
     * used to setList the default card details
     * @param defaultCardDetails default card details model
     */
    void setDefaultCardDetails(CardDetailsDataModel defaultCardDetails);

    /**
     * <h1>setRefereralCode</h1>
     * @param refereralCode :
     */
    void setRefereralCode(String refereralCode);

    /**
     * <h1>getRefereralCode</h1>
     * @return
     */
    String getRefereralCode();

    /**
     * <h1>setWalletBal</h1>
     * @param balance :
     */
    void setWalletBal(String balance);

    /**
     * <h1>getWalletBal</h1>
     * @return : balance
     */
    String getWalletBal();

    /**
    * getLanguageSettings      method is used to get language details
    * @return  language data
    */
    LanguagesList getLanguageSettings();

     /**
      *  setLanguageSettings      method is used to set language details
      *  @param languageSettings   language data
     **/
     void setLanguageSettings(LanguagesList languageSettings);

    List<String> getGoogleServerKeys();

    void setGoogleServerKeys(List<String> googleServerKeys);

    void setGoogleServerKey(String googleKey);

    String getGoogleServerKey();

    /**
     * <h2>setRequesterId</h2>
     * used to set the requesterId for zen desk
     * @param requesterId  sets requesterId for zen desk
     */
    void setRequesterId(String requesterId);
    /**
     * <h2>getRequesterId</h2>
     * used to get the RequesterId
     * @return returns the RequesterId
     */
    String getRequesterId();

    /**
     * <h2>setDriverMail</h2>
     */
    void setDriverMail(String email);

    /**
     * <h2>getDriverMail</h2>
     */
    String getDriverMail();


    void setIsChatModuleEnable(boolean isChatModuleEnable);

    boolean getIsChatModuleEnable();


    //mqtt dynamic data
    void setMqttUserName(String userName);
    String getMqttUserName();

    void setMqttPassword(String password);
    String getMqttPassword();

    String getMqttHost();
    void setMqttHost(String host);

    String getMqttPort();
    void setMqttPort(String port);


    //InApp Calling
    /**
     * <h2>setAuthTokenCall</h2>
     * method is used to set authToken
     * @param authTokenCall to set authToken
     */
    void setAuthTokenCall(String authTokenCall);

    /**
     * <h2>getAuthTokenCall</h2>
     * method is used to get AuthToken
     * @return  String AuthToken
     */
    String getAuthTokenCall();

    void setCustomerName(String customerName);

    String getCustomerName();

    void setCustomerPic(String customerPic);

    String getCustomerPic();

    void setGoogleMapKey(String googleMapKey);

    String getGoogleMapKey();

    void setGoogleMapKeyTopic(String googleMapKeyTopic);

    String getGoogleMapKeyTopic();

    void setBirdViewTopic(String mqttTopic);

    String getBirdViewTopic();

    void setDispatcherTopic(String mqttTopic);

    String getDispatcherTopic();

    Double getDistanceForLatLongMax();

    void setDistanceForLatLongMax(String latLong);

    void setDriverApiDistanceWhenFree(String driverApiDistanceWhenFree);

    Double getDriverApiDistanceWhenFree();

  void setStripImage(String imageUrl);
  String getStripImage();
}
