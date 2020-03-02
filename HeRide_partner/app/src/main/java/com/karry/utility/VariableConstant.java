package com.karry.utility;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.heride.partner.BuildConfig;
import com.karry.service.FCM_Topics;

import java.io.File;

/**
 * Created by embed on 17/6/16.
 */
public class VariableConstant {

    public static final String PARENT_FOLDER = "Karry";
    static final String PREF_NAME = "KarryPartner";
    public static final String APP_VERSION = BuildConfig.VERSION_NAME;
    public static final String DEVICE_MODEL = android.os.Build.MODEL;
    public static final String DEVICE_MAKER = android.os.Build.MANUFACTURER;
    public static final String DEVICE_OS_VERSION = String.valueOf(Build.VERSION.SDK_INT);
    public static final int DEVICE_TYPE = 1; //1-Android, 2-IOS, 3-Web
    static final int USER_TYPE = 2; //1 for customer 2 for driver
    //permission constants
    public static final int RC_LOCATION_STATE = 102;
    public static final String BUCKET_NAME = "karry";
    public static final String SIGNATURE_UPLOAD = "Drivers/signature";
    public static final String SIGNATURE_PIC_DIR = PARENT_FOLDER + "/sign";
    private static final String VEHICLE_DOCUMENTS = "Drivers/VehicleDocuments";
    public static final String PROFILE_PIC = "Drivers/ProfilePics";
    public static final String VEHICLE_PIC = "Drivers/vehicleImage";
    public static final String BANK_DOC = "Drivers/bankDocumets";
    public static final String DRIVING_LICENCE = VEHICLE_DOCUMENTS+"/Licence";
    public static final String VEHICLE_REGISTRATION = VEHICLE_DOCUMENTS+"/Registration";
    public static final String VEHICLE_INSURANCE = VEHICLE_DOCUMENTS+"/Insurance";
    public static final String POLICE_CLEARNCE = VEHICLE_DOCUMENTS+"/PoliceClearance";
    public static final String INSPECTION_REPORT = VEHICLE_DOCUMENTS+"/InspectionReport";
    public static final String GOODS_INSURANCE = VEHICLE_DOCUMENTS+"/GoodsInTransit";
    public static final String CHILDREN_CARD = VEHICLE_DOCUMENTS+"/WorkingWithChildrenCards";

    public static final int RESPONSE_CODE_SUCCESS = 200;
    public static final int RESPONSE_CODE_NO_STRIPE_ACCOUNT_FOUND = 400;
    public static final int RESPONSE_UNAUTHORIZED = 401;

    public static boolean IS_PROFILE_EDITED = false;

    public static boolean EDIT_PASSWORD_DIALOG = false;
    public static Uri newProfileImageUri;
    public static File newFile;
    public static boolean VECHICLESELECTED = false;
    public static String VECHICLEID = "";
    public static String VECHICLE_TYPE_ID = "";

    public static boolean IS_STRIPE_ADDED = false;
    public static boolean IS_POP_UP_OPEN = false;
    public static String PREVIOUS_BID = "";

    public static String lan  = "en";

    public static final int CAMERA_PIC = 11, GALLERY_PIC = 12, CROP_IMAGE=13;
    public static final int SELECT_AN_STATE = 111;
    public static final int SELECT_AN_YEAR = 112;
    public static final int SELECT_AN_COLOR = 113;
    public static final int SELECT_A_GENDER = 114;
    public static final int SELECT_PREFERED_ZONE_SELECT = 0;
    public static final int SELECT_PREFERED_ZONE_DELETE = 1;
    public static final String LANGUAGE="en";
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE=152;

    public static final String MB_DROP_LAT_LNG = "MB_DROP_LAT_LNG";
    public static final String MB_DROP_ADDRESS = "MB_DROP_ADDRESS";
    public static final String MB_DROP_PLACE_ID = "MB_DROP_PLACE_ID";
    public static final String FROM_MB = "FROM_METER_BOOKING";
    public static float BEARING = 0;
    public static Long TIMEDIFFERENCE = 0L;
    public static boolean isTwilioEnable = false;
    public static boolean isTowingStart = false;

    public static Bundle MeterBookingDropBundle = null;

    public static final String LOGIN_VEHICLE_LIST = "LOGIN_VEHICLE_LIST";
    public static final String LOGIN_VEHICLE_TYPE_LIST = "LOGIN_VEHICLE_TYPE_LIST";
    public static final String PREFERENCE_ZONE = "PREFERENCE_ZONE";


    public static String CANCEL_RIDE_REASON = "";
    public static boolean CANCEL_RIDE = false;
    public static boolean ADMIN_COLMPLETED = false;
    public static boolean UPDATE_DROP = false;
    public static boolean FORGROUND_LOCK = false;
    public static String CURRENCY = "";
    public static boolean APPISBACKGROUND = false;

    /**********************************************************************************************/
    public static final String DATA = "DATA";
    public static final String TYPE = "TYPE";
    public static final String TITLE = "TITLE";
    public static final String FROM = "FROM";
    public static final String URL = "URL";
    public static final String SIGNUP_DATA = "SIGNUP_DATA";
    public static final String COUNTRY_SELECT = "COUNTRY_SELECT";
    public static final String CURRENCY_SELECT = "CURRENCY_SELECT";

    public static final String GENDER = "GENDER";
    public static final String STATE = "STATE";
    public static final String VEHICLE_TYPE = "VEHICLE_TYPE";
    public static final String VEHICLE_SERVICE = "VEHICLE_SERVICE";
    public static final String VEHICLE_MAKE = "VEHICLE_MAKE";
    public static final String VEHICLE_MODEL = "VEHICLE_MODEL";
    public static final String VEHICLE_SPECIALITIES = "VEHICLE_SPECIALITIES";
    public static final String YEAR = "YEAR";
    public static final String COLOR = "COLOR";
    public static final String CITY = "CITY";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String MOBILE = "MOBILE";
    public static final String COUNTRY_CODE = "COUNTRY_CODE";
    public static final String USER_ID = "USER_ID";
    public static final String TRIGGER = "TRIGGER";

    public static final String LICENCE = "LICENCE";
    public static final String POLICE_CLEAR = "POLICE_CLEARENCE";
    public static final String CHILDREN_CARD_EXPIRY = "CHILDREN_CARD_EXPIRY";
    public static final String REGISTRATION = "REGISTRATION";
    public static final String INSURANCE = "INSURANCE";
    public static final String INSPECTION = "INSPECTION";
    public static final String GOODS_INSPECTION = "GOODS_INSPECTION";

    public static final String USER_NAME = "USER_NAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String VEH_NO = "VEH_NO";

    public static final String METERBOOKING_INVOICE_DATA = "METERBOOKING_INVOICE_DATA";
    public static final String RIDE_BOOKING_DATA = "RIDE_BOOKING_DATA";

    public static final String LAST_DIGITS = "last_digits";
    public static final String CARD_TOKEN = "card_token";
    public static final String CARD_BRAND = "card_brand";
    static int REQUEST_CODE = 1;
    public static final int REQUEST_CODE_PERMISSION_MULTIPLE = 127;


    public static final String GMT_TIME_FORMAT = "EEE MMM dd HH:mm:ss z yyyy";
    public static final String TIME_FORMAT_TIME_DISPLAY = "EEEE dd MMM yyyy h:mm a";
    public static final String TIME_FORMAT_TIME_DISPLAY1 = "MMM dd, yyyy h:mm a";

    public static boolean BOOKING_FLOW_OPEN = true;

    public static FCM_Topics FCM_TOPIS = null;
    public static String MQTT_TOPICS = null;
    public static final String LOGIN="login_activity";


    /*Permissions */
    public static final int PERMISSION_GRANTED = 40;
    public static final int PERMISSION_DENIED = 41;
    static final int PERMISSION_BLOCKED = 42;
    /*Twillio*/
    public static final String PHONE_NUMBER="phonenumber";
    public static final String PHONE_IMAGE_URL="ImageUrl";
    public static final String PHONE_TO="To";
    public static final String PHONE_FROM="From";

    public static final String ADVERTISE_DETAILS = "ADVERTISE_DETAILS";
    public static final String PRIVACY = "PRIVACY_POLICY";
    public static final String TERMS_CONDITION = "TERMS_CONDITION";
    public static final String SUPPORT = "SUPPORT";

    public static final String ADS="advertise_activity";
    public static final int VIEWED = 1;
    public static final int IGNORE = 2;

    public static final String STRIPE_DATA = "STRIPE_DATA";
    public static final String COUNTRY = "COUNTRY";

    public static final String SERVICELIST = "SERVICELIST";

    /*Google places API*/
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String OUT_JSON = "/json";

    public static final String INTENT_ACTION_CALL = "com.servicegenie.provider.call";
    public static boolean IS_IN_CALL = false;

    public static final String UNVERIFIED_BANK_DETAILS = "unverified bank details";
    public static final String STRIP_IMAGE = "strip image";

}
