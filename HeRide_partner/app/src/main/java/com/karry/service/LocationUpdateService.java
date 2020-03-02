package com.karry.service;

import static com.karry.utility.VariableConstant.DEVICE_TYPE;
import static com.karry.utility.VariableConstant.RESPONSE_CODE_SUCCESS;
import static com.karry.utility.VariableConstant.RESPONSE_UNAUTHORIZED;

import android.Manifest;
import android.Manifest.permission;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import com.karry.app.MyApplication;
import com.karry.app.mainActivity.MainActivity;
import com.karry.authentication.login.LoginActivity;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.manager.booking.BookingManager;
import com.karry.manager.location.RxLocationObserver;
import com.karry.network.NetworkService;
import com.karry.network.NetworkStateHolder;
import com.heride.partner.R;
import com.karry.utility.AppConstants;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import dagger.android.DaggerService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

public class LocationUpdateService extends DaggerService {

  private static final String METER = "meters";
  private static final String KILOMETER = "Kilometers";
  private static final String NAUTICAL_MILES = "nauticalMiles";
  public double cumulativeDistance = 0.0;
  public double prevLat = 0.0, prevLng = 0.0;
  private int battery_level;

  private String TAG = LocationUpdateService.class.getSimpleName();

  private double prevLatTimer = 0.0, prevLongTimer = 0.0;
  private static int counter;
  private String strDouble;
  private Timer myTimer_publish;
  private TimerTask myTimerTask_publish;
  private Location prevLocation = null;
  private float bearing;
  private CouchDbHandler couchDBHandle;
  private int mqttCount = 0;

  @Inject
  PreferenceHelperDataSource preferenceHelperDataSource;
  @Inject
  NetworkService networkService;
  @Inject
  NetworkStateHolder networkStateHolder;
  @Inject
  Context mActivity;
  @Inject
  Gson gson;
  private int numberOfPointsReceived;

  private static LocationUpdateService locationUpdateService;

  public void setCumulativeDistance(double cumulativeDistance, double prevLat, double prevLng) {
    this.cumulativeDistance = cumulativeDistance;
    this.prevLat = prevLat;
    this.prevLng = prevLng;
    Utility.printLog("log1 distance update " + cumulativeDistance);
  }

  public static LocationUpdateService getLocationUpdateService() {
    return locationUpdateService;
  }

  Date startDate_speed_time = Calendar.getInstance().getTime();
  Date endDate_speed_time = Calendar.getInstance().getTime();

  BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {

      if (intent.getAction().equals("com.app.driverapp.internetStatus")) {
      } else {
        battery_level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
      }
    }
  };

  @Override
  public void onCreate() {
    super.onCreate();

    locationUpdateService = this;
    fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(this);
    initializeLocation();
    Utility.printLog(TAG + " service onCreate ");
    IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    iFilter.addAction("com.app.driverapp.internetStatus");
    registerReceiver(mBroadcastReceiver, iFilter);

    MyApplication controller = (MyApplication) getApplicationContext();
    couchDBHandle = controller.getDBHandler();

    if ((preferenceHelperDataSource.getBookingId() != null
        && !preferenceHelperDataSource.getBookingId().isEmpty())) {
      cumulativeDistance = preferenceHelperDataSource.getDistanceCalculated();
    }
    //subscribeLocationChange();
  }

  /**
   * <h2>subscribeLocationChange</h2>
   *
   * This method is used to subscribe for the location change
   */
 /* private void subscribeLocationChange() {
    RxLocationObserver.getInstance().getObservable().subscribe(new DisposableObserver<Location>() {
      @Override
      public void onNext(Location location) {
        Utility.printLog(TAG
            + "log1----> location oberved  "
            + location.getLatitude()
            + " "
            + location.getLongitude());
        numberOfPointsReceived++;

        if (((MyApplication) getApplication()).isMQTTConnected()) {
          ((MyApplication) getApplication()).publishMqttNew(
              "log1---->location received from google "
                  + System.currentTimeMillis()
                  + " "
                  + numberOfPointsReceived, preferenceHelperDataSource.getBookingId());


        }

        onNewLocation(location);
      }

      @Override
      public void onError(Throwable e) {
      }

      @Override
      public void onComplete() {
      }
    });
  }*/

  private void onNewLocation(Location location) {
    double currentLat = location.getLatitude();
    double currentLng = location.getLongitude();

    preferenceHelperDataSource.setCurrLatitude(String.valueOf(currentLat));
    preferenceHelperDataSource.setCurrLongitude(String.valueOf(currentLng));

    if (prevLocation == null) {
      prevLocation = location;
    } else {
      bearing = prevLocation.bearingTo(location);
    }

    if (preferenceHelperDataSource.getBookingStatus().matches(AppConstants.BookingStatus.Accept)
        || (preferenceHelperDataSource.getBookingStatus()
        .matches(AppConstants.BookingStatus.started)
        && !preferenceHelperDataSource.getServiceStatus()
        .matches(AppConstants.TowTrayService.Fixed))) {

      if (((MyApplication) getApplication()).isMQTTConnected()) {
        ((MyApplication) getApplication()).publishMqttNew(
            "log1--->on new location called " + currentLat + ", " + currentLng,
            preferenceHelperDataSource.getBookingId());
      }

      if (prevLat == 0.0 || prevLng == 0.0) {
        this.prevLat = currentLat;
        this.prevLng = currentLng;
        startDate_speed_time = Calendar.getInstance().getTime();
      } else {
        calculateDistance(currentLat, currentLng, prevLat, prevLng);
      }
    }
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  void initializeLocation(){
    try {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
          != PackageManager.PERMISSION_GRANTED
          && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
          != PackageManager.PERMISSION_GRANTED) {

        return;
      }

      locationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
          if (locationResult == null) {
            return;
          }
          else{
            for (Location location : locationResult.getLocations()) {

              RxLocationObserver.getInstance().publishData(location);
              // Update UI with location data
              Utility.printLog(TAG
                  + "log1----> location oberved  "
                  + location.getLatitude()
                  +"\n" + location.getLongitude()
                  +"\n"+location.getAccuracy()
                  +"\n"+location.getSpeed()
                  +"\n"+location.getProvider());
              numberOfPointsReceived++;


              if (((MyApplication) getApplication()).isMQTTConnected()) {
                ((MyApplication) getApplication()).publishMqttNew(
                    "log1---->location received from google "
                        +location.getSpeed()
                        + numberOfPointsReceived, preferenceHelperDataSource.getBookingId());
              }


              if(location.getSpeed()>0.0){
                onNewLocation(location);
              }
            }
          }
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
          super.onLocationAvailability(locationAvailability);
        }
      };

    /*  locationProvider.stopLocationUpdates();
      locationProvider.startLocation(this);*/

      startLocationUpdates();

    } catch (Exception e) {
      Utility.printLog("myservice : exception : ");
      e.printStackTrace();
      if (((MyApplication) getApplication()).isMQTTConnected()) {
        ((MyApplication) getApplication()).publishMqttNew("exception occured " + e,
            preferenceHelperDataSource.getBookingId());
      }
    }
  }

  private FusedLocationProviderClient fusedLocationProviderClient ;
  private LocationCallback locationCallback;

  private void startLocationUpdates() {
    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    LocationRequest locationRequest =
        LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    /*
    locationRequest.setMaxWaitTime(1000);
    */
    locationRequest.setFastestInterval(300);
    locationRequest.setInterval(500);
    locationRequest.setSmallestDisplacement(5);
    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback,
        null /* Looper */);
  }

  private void stopLocationUpdates() {
    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    locationCallback = null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    try {

      locationUpdateService = this;

      if (intent != null && intent.getAction() != null) {
        if (intent.getAction().equals(AppConstants.ACTION.STARTFOREGROUND_ACTION)) {
          Intent notificationIntent = new Intent(this, MainActivity.class);
          notificationIntent.setAction(AppConstants.ACTION.MAIN_ACTION);
          notificationIntent.setFlags(
              Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_RECEIVER_FOREGROUND);
          PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

          Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

          String CHANNEL_ID = getString(R.string.app_name_withoutSpace);
          ; // The id of the channel.
          CharSequence name = getString(R.string.app_name);

          Notification notification = new NotificationCompat.Builder(this).setContentTitle(
              getResources().getString(R.string.app_name))
              .setTicker("")
              .setContentText("Running...")
              .setSmallIcon(R.drawable.ic_launcher)
              .setChannelId(CHANNEL_ID)
              .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
              .setOngoing(true)
              .build();

          NotificationManager mNotificationManager =
              (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mNotificationManager.createNotificationChannel(mChannel);
          }

          startPublishingWithTimer();

          startForeground(AppConstants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
        } else if (intent.getAction().equals(AppConstants.ACTION.STOPFOREGROUND_ACTION)) {
          stopForeground(true);
          stopSelf();
        }
      } else {
        stopForeground(true);
        stopSelf();
      }
    } catch (Exception e) {
      e.printStackTrace();
      Utility.printLog("Crashed in forground service");
    }

    return START_STICKY;
  }

  /*protected synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
  }*/

  public void calculateDistance(double curLat, double curLong, double prevLat, double prevLng) {

    this.prevLat = curLat;
    this.prevLng = curLong;

    if ((preferenceHelperDataSource.getBookingId() != null
        && !preferenceHelperDataSource.getBookingId().isEmpty())) {

      // Akbar added this for distance issue
      if (prevLat != curLat && prevLng != curLong) {

        double dis = distance(prevLat, prevLng, curLat, curLong, METER);
        dis = Math.abs(dis);

        if (((MyApplication) getApplication()).isMQTTConnected()) {
          ((MyApplication) getApplication()).publishMqttNew(
              "log1--->successive points distance---" + dis,
              preferenceHelperDataSource.getBookingId());
        }

        if (dis < preferenceHelperDataSource.getDistanceForLatLongMax()) {

          couchDBHandle.updateDocument(curLat, curLong);
          cumulativeDistance = cumulativeDistance + dis;

          preferenceHelperDataSource.setDistanceCalculated(String.valueOf(cumulativeDistance));
          strDouble = String.format(Locale.US, "%.2f",
              (cumulativeDistance / preferenceHelperDataSource.getMileageMetricUnit()));
          preferenceHelperDataSource.setDistanceCalculatedShow(strDouble);

          // Log.d("log1--->", "cumulative distance---" + cumulativeDistance + " " + strDouble);

          if (((MyApplication) getApplication()).isMQTTConnected()) {
            ((MyApplication) getApplication()).publishMqttNew(
                "log1--->cumulative distance---" + cumulativeDistance + " " + dis,
                preferenceHelperDataSource.getBookingId());
          }
        } else if (dis >= preferenceHelperDataSource.getDistanceForLatLongMax()) {

          dis = 20;

          cumulativeDistance = cumulativeDistance + dis;

          preferenceHelperDataSource.setDistanceCalculated(String.valueOf(cumulativeDistance));
          strDouble = String.format(Locale.US, "%.2f",
              (cumulativeDistance / preferenceHelperDataSource.getMileageMetricUnit()));
          preferenceHelperDataSource.setDistanceCalculatedShow(strDouble);

          // Log.d("log1--->", "cumulative distance outlier point---" + cumulativeDistance + " " + strDouble);

          if (((MyApplication) getApplication()).isMQTTConnected()) {
            ((MyApplication) getApplication()).publishMqttNew(
                "log1--->cumulative distance outlier point---"
                    + cumulativeDistance
                    + " "
                    + strDouble, preferenceHelperDataSource.getBookingId());
          }
        }
      }
    }
  }

  public String getCumulativeDistance() {
    return String.valueOf(cumulativeDistance);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    stopLocationUpdates();
    fusedLocationProviderClient = null;
    if (myTimerTask_publish != null) {
      myTimerTask_publish.cancel();
      myTimer_publish.cancel();
    }

    if (mBroadcastReceiver != null) {
      unregisterReceiver(mBroadcastReceiver);
      mBroadcastReceiver = null;
    }
    Utility.printLog(TAG+" onDestroy");
  }

  private void startPublishingWithTimer() {
    if (myTimer_publish != null) {
      // Log.d(TAG, "Timer already started");
      return;
    }
    myTimer_publish = new Timer();

    myTimerTask_publish = new TimerTask() {
      @Override
      public void run() {
        Utility.printLog("network connection : "
            + networkStateHolder.isConnected()
            + "\n"
            + Utility.isNetworkAvailable(getApplicationContext()));

        if (Utility.isNetworkAvailable(getApplicationContext())) {
          // updateLocationLogs(); //Akbar commented to solve distance issue
          // if the mqtt is disconnected, and if the driver is online will check here and
          // reconnect after 6 sec.
          Utility.printLog(
              "mqtt connection iss : " + ((MyApplication) getApplication()).isMQTTConnected());
          if (!((MyApplication) getApplication()).isMQTTConnected()) {
            mqttCount++;
          } else {
            mqttCount = 0;
          }

          if (!((MyApplication) getApplication()).isMQTTConnected() && mqttCount > 6) {
            mqttCount = 0;
            if (preferenceHelperDataSource.getMqttTopic() != null) {
              ((MyApplication) getApplication()).unSubscribeMqtt(
                  preferenceHelperDataSource.getMqttTopic());
              Utility.printLog(
                  "testing unsubScribed Mqtt Topic :   " + VariableConstant.MQTT_TOPICS);
            }

            MyApplication.getInstance().connectMQTT();
          }

          if (prevLatTimer == 0.0 || prevLongTimer == 0.0) {
            prevLatTimer = preferenceHelperDataSource.getCurrLatitude();
            prevLongTimer = preferenceHelperDataSource.getCurrLongitude();
          }

          if (counter >= preferenceHelperDataSource.getApiIntervalWhenFree()) {
            counter = 0;
            Double distance_difference =
                distance(prevLatTimer, prevLongTimer, preferenceHelperDataSource.getCurrLatitude(),
                    preferenceHelperDataSource.getCurrLongitude(), METER);
            if (distance_difference >= preferenceHelperDataSource.getDistanceForLatLong()) {
              prevLatTimer = preferenceHelperDataSource.getCurrLatitude();
              prevLongTimer = preferenceHelperDataSource.getCurrLongitude();
              publishLocation(preferenceHelperDataSource.getCurrLatitude(),
                  preferenceHelperDataSource.getCurrLongitude(), distance_difference);
            } else {
              publishLocation(preferenceHelperDataSource.getCurrLatitude(),
                  preferenceHelperDataSource.getCurrLongitude(), distance_difference);
            }
          } else {
            counter++;
          }
        }
        // AKbar commented to solve distance issue
            /*else if (preferenceHelperDataSource.getBookingId() != null
                && !preferenceHelperDataSource.getBookingId().matches("")) {
              couchDBHandle.updateDocument(
                  preferenceHelperDataSource.getCurrLatitude(),
                  preferenceHelperDataSource.getCurrLongitude());
            }*/
      }
    };
    // Log.d(TAG, "myTimer_publish interval " + preferenceHelperDataSource.getApiIntervalWhenFree());
    myTimer_publish.schedule(myTimerTask_publish, 0, 1000);
  }

  public void publishLocation(double latitude, double longitude, double distance_difference) {

    if (preferenceHelperDataSource.isLoggedIn()) {

      String locationChk = "";
      LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      boolean gps_enabled = false;
      Intent gps_inIntent = new Intent("com.driver.gps");
      try {
        assert lm != null;
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
      } catch (Exception ignored) {
      }
      if (gps_enabled) {
        locationChk = "1";
        gps_inIntent.putExtra("action", "0");
      } else {
        locationChk = "0";
        gps_inIntent.putExtra("action", "1");
      }
      sendBroadcast(gps_inIntent);

      if (latitude != 0.00 && longitude != 0.00) {
        locationUpdateAPI(latitude, longitude, locationChk, distance_difference);
      }
    } else {
      Log.d(TAG, "MyService" + "isLogin  " + false);
    }
  }

  /**
   * <h1>API call for update location</h1>
   *
   * <p>for update the location
   *
   * @param latitude :current latitude
   * @param longitude: current Longitude
   * @param locationChk:gps check
   */
  private void locationUpdateAPI(Double latitude, Double longitude, String locationChk,
      double distance_difference) {

    endDate_speed_time = Calendar.getInstance().getTime();

    long diffInMs = endDate_speed_time.getTime() - startDate_speed_time.getTime();
    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
    startDate_speed_time = endDate_speed_time;
    double speed = distance_difference / diffInSec;

    if (speed < 1) speed = 0;

    if (preferenceHelperDataSource.getBookingId() != null
        && !preferenceHelperDataSource.getBookingId().matches("")
        && !preferenceHelperDataSource.getBookingStatus()
        .equals(AppConstants.BookingStatus.Completed)) {

      updateLocationMQTT(latitude, longitude, locationChk, speed);
    }

    Observable<Response<ResponseBody>> location = null;

    if (preferenceHelperDataSource.getBookingId() == null
        || preferenceHelperDataSource.getBookingId().matches("")) {
      location = networkService.location(preferenceHelperDataSource.getSessionToken(),
          VariableConstant.LANGUAGE, longitude, latitude, bearing + "",
          preferenceHelperDataSource.getAppVersion(), battery_level + "", locationChk + "",
          preferenceHelperDataSource.getPresenceTime(), Double.parseDouble("0"), null, null);


    }

    if (location != null) {

      Log.d(TAG, "locationUpdateAPI: ");
      location.observeOn(AndroidSchedulers.mainThread())
          .subscribeOn(Schedulers.io())
          .subscribe(new Observer<Response<ResponseBody>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Response<ResponseBody> value) {
              try {
                JSONObject jsonObject;

                switch (value.code()) {
                  case RESPONSE_CODE_SUCCESS:
                    String response = value.body().string();
                    jsonObject = new JSONObject(response);
                    FCM_Topics fcm_topics =
                        gson.fromJson(jsonObject.get("data").toString(), FCM_Topics.class);
                    preferenceHelperDataSource.setFcmTopic(fcm_topics);
                    subscribeFCM_Topics();

                    jsonObject = new JSONObject(jsonObject.get("data").toString());
                    int queuePosition =
                        Integer.parseInt(jsonObject.get("queuePosition").toString());
                    int queueLength = Integer.parseInt(jsonObject.get("queueLength").toString());
                    String godsviewTopic = jsonObject.getString("godsviewTopic");
                    String dispatcherTopic = jsonObject.getString("dispatcherTopic");
                    preferenceHelperDataSource.setBirdViewTopic(godsviewTopic);
                    preferenceHelperDataSource.setDispatcherTopic(dispatcherTopic);

                    BookingManager.queuePosition(queuePosition, queueLength);

                    break;
                  case RESPONSE_UNAUTHORIZED:
                    if (Utility.isMyServiceRunning(LocationUpdateService.class, mActivity)) {
                      Intent stopIntent = new Intent(mActivity, LocationUpdateService.class);
                      stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                      mActivity.startService(stopIntent);
                    }
                    VariableConstant.FCM_TOPIS = preferenceHelperDataSource.getFcmTopic();
                    VariableConstant.MQTT_TOPICS = preferenceHelperDataSource.getMqttTopic();
                    LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                    preferenceHelperDataSource.clearSharedPredf();
                    preferenceHelperDataSource.setLanguageSettings(languagesList);
                    jsonObject = new JSONObject(value.errorBody().string());
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("message", jsonObject.get("message").toString());
                    getApplicationContext().startActivity(intent);

                    break;

                  default:
                    if (preferenceHelperDataSource.getBookingId() != null
                        && !preferenceHelperDataSource.getBookingId().matches("")) {
                      couchDBHandle.updateDocument(preferenceHelperDataSource.getCurrLatitude(),
                          preferenceHelperDataSource.getCurrLongitude());
                    }
                    String err = value.errorBody().string();
                    jsonObject = new JSONObject(err);
                    break;
                }
              } catch (JSONException e) {
                Utility.printLog("location : Catch :" + e.getMessage());
              } catch (IOException e) {
                Utility.printLog("location : Catch :" + e.getMessage());
              }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
          });
    }
  }



  /**
   * <h1>subscribeFCM_Topics</h1>
   *
   * <p>subscribe the FCM topics which getting from the location update API response
   */
  private void subscribeFCM_Topics() {
    if (preferenceHelperDataSource.getFcmTopic().getFcmTopics().size() > 0) {
      for (int i = 0; i < preferenceHelperDataSource.getFcmTopic().getFcmTopics().size(); i++) {
        FirebaseMessaging.getInstance()
            .subscribeToTopic(
                "/topics/" + preferenceHelperDataSource.getFcmTopic().getFcmTopics().get(i));
      }
    }
  }

  private void updateLocationMQTT(Double latitude, Double longitude, String locationChk,
      double speed) {

    JSONObject reqObject = new JSONObject();
    JSONObject reqObjectForBirdView = new JSONObject();

    try {

      reqObject.put("status", Integer.parseInt(AppConstants.BookingStatus.MQTTStataus));
      reqObject.put("longitude", longitude);
      reqObject.put("latitude", latitude);
      reqObject.put("location_Heading", bearing + "");
      reqObject.put("driverId", preferenceHelperDataSource.getMid());
      reqObject.put("bookingId", preferenceHelperDataSource.getBookingId());
      reqObject.put("transit", Double.parseDouble("0"));
      reqObject.put("speed", speed);

      JSONObject location = new JSONObject();
      location.put("longitude", longitude);
      location.put("latitude", latitude);
      reqObjectForBirdView.put("driverId", preferenceHelperDataSource.getMid());
      reqObjectForBirdView.put("status", 100);
      reqObjectForBirdView.put("deviceType", DEVICE_TYPE);
      reqObjectForBirdView.put("lastActive", System.currentTimeMillis());
      reqObjectForBirdView.put("appversion", preferenceHelperDataSource.getAppVersion());
      reqObjectForBirdView.put("batteryPercentage", battery_level);
      reqObjectForBirdView.put("locationCheck", locationChk);
      reqObjectForBirdView.put("locationHeading", bearing);
      reqObjectForBirdView.put("location", location);

      if (((MyApplication) getApplication()).isMQTTConnected()) {
        ((MyApplication) getApplication()).publishMqtt(reqObject, reqObjectForBirdView);
      } else {
        if (preferenceHelperDataSource.getMqttTopic() != null) {
          ((MyApplication) getApplication()).unSubscribeMqtt(
              preferenceHelperDataSource.getMqttTopic());
          Utility.printLog(
              TAG + " testing unsubScribed Mqtt Topic :   " + VariableConstant.MQTT_TOPICS);
        }

        MyApplication.getInstance().connectMQTT();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static double distance(double lat1, double lng1, double lat2, double lng2, String unit) {
    double earthRadius = 3958.75; // miles (or 6371.0 kilometers)
    double dLat = Math.toRadians(lat2 - lat1);
    double dLng = Math.toRadians(lng2 - lng1);
    double sindLat = Math.sin(dLat / 2);
    double sindLng = Math.sin(dLng / 2);
    double a =
        Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(
            Math.toRadians(lat2));
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double dist = earthRadius * c;

    if (KILOMETER.equals(unit)) // Kilometer
    {
      dist = dist * 1.609344;
    } else if (NAUTICAL_MILES.equals(unit)) // Nautical Miles
    {
      dist = dist * 0.8684;
    } else if (METER.equals(unit)) // meter
    {
      dist = dist * 1609.344;
    }

    return dist;
  }
}
