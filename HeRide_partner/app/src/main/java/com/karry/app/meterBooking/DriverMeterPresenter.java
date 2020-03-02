package com.karry.app.meterBooking;


import static com.facebook.FacebookSdk.getApplicationContext;
import static com.karry.utility.VariableConstant.RESPONSE_UNAUTHORIZED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.karry.app.MyApplication;

import com.google.gson.Gson;
import com.karry.app.MyApplication;
import com.karry.service.CouchDbHandler;
import com.karry.service.LatLngBody;
import com.karry.service.LocationUpdateService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

import com.karry.authentication.login.model.LanguagesList;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.data.source.sqlite.SQLiteDataSource;
import com.karry.manager.location.LocationManagerCallback;
import com.karry.manager.location.RxLocationObserver;
import com.karry.network.NetworkService;
import com.karry.network.NetworkStateHolder;
import com.karry.network.RxNetworkObserver;
import com.karry.pojo.bookingAssigned.BookingAssignedDataRideAppts;
import com.karry.service.CouchDbHandler;
import com.karry.service.LatLngBody;
import com.karry.service.LocationUpdateService;
import com.karry.utility.DataParser;
import com.karry.utility.Scaler;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

/**
 * <h1>DriverMeterPresenter</h1>
 * <p>the class which implement the Driver Meter Activity</p>
 */
class DriverMeterPresenter implements  DriverMeterContract.DriverMeterPresenter,
        LocationManagerCallback.CallBacks {
  private static final String TAG = DriverMeterPresenter.class.getSimpleName();

    @Inject DriverMeterContract.DriverMeterView driverMeterView;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferencesHelper;
    @Inject Context context;
    @Inject RxNetworkObserver rxNetworkObserver;
    @Inject NetworkStateHolder networkStateHolder;
    private RxLocationObserver rxLocationObserver;
    @Inject CompositeDisposable compositeDisposable;
    @Inject SQLiteDataSource addressDataSource;
    private boolean networkAvailable = true;

    private static int timeinsec = 0;
    private Handler handler;
    private boolean runTimer = true;
    private BookingAssignedDataRideAppts bookingAssignedDataRideAppts;
    private float estimatedTimeFare = (float) 0;
    private float estimatedDistanceFare = (float) 0;
    private int oldTotalMin = 0;
    private float totalFare = (float) 0;
    private float systemFare = (float) 0;
    private String currecySymbol = "";
    private Double dropLat = 0.0;
    private Double dropLng = 0.0;
    private Date pauseTime;
    private int pauseTimeinSec = 0;

    private Location mCurrentLoc,mPreviousLoc;
    private CouchDbHandler couchDBHandle;


    @Inject
    DriverMeterPresenter(RxLocationObserver rxLocationObserver) {
        this.rxLocationObserver = rxLocationObserver;
        subscribeLocationChange();
        MyApplication controller = (MyApplication) getApplicationContext();
        couchDBHandle = controller.getDBHandler();
    }

    @Override
    public void setBookingAssignedDataRideAppts(BookingAssignedDataRideAppts bookingAssignedDataRideAppts) {
        this.bookingAssignedDataRideAppts = bookingAssignedDataRideAppts;
        currecySymbol = bookingAssignedDataRideAppts.getCurrencySbl()+" ";

        try {
            driverMeterView.setAddress(bookingAssignedDataRideAppts.getDropAddress());

            List<String> dropLatLng = Arrays.asList(bookingAssignedDataRideAppts.getDropLatLong().split(","));
            dropLat = Double.parseDouble(dropLatLng.get(0));
            dropLng = Double.parseDouble(dropLatLng.get(1));
        }
        catch (Exception e){
            Utility.printLog(""+ Arrays.toString(e.getStackTrace()));
        }

        if(dropLat == 0.0 || dropLng == 0.0)
            driverMeterView.navigationEnable(false);
        else
            driverMeterView.navigationEnable(true);

        driverMeterView.setDistanceShow(preferencesHelper.getDistanceCalculatedShow());
        setFare();
    }

    @Override
    public void completeBooking(String bid) {
        completeMeterBookingAPI(bid);
    }


  private void completeMeterBookingAPI(String bid) {
    driverMeterView.showProgress();

    JSONArray list = couchDBHandle.retriveDocument();
    JSONObject joLatLngBody = new JSONObject();
    try {
      Utility.printLog(TAG + " put location in doc try ");
      joLatLngBody.put("latLong", list);
    } catch (JSONException e) {
      Utility.printLog(TAG + " put location in doc catch " + e.getLocalizedMessage());
      e.printStackTrace();
    }
    Gson gson = new Gson();
    LatLngBody latLngBody = gson.fromJson(joLatLngBody.toString(), LatLngBody.class);
    Utility.printLog(TAG + "retriveDocument body:  " + joLatLngBody.toString());

    if (latLngBody != null && latLngBody.getLatLong() != null) {
      Utility.printLog(
          TAG
              + "retriveDocument body:  "
              + latLngBody.getLatLong().length
              + "\n"
              + joLatLngBody.toString());
      Observable<Response<ResponseBody>> locationLogs =
          networkService.locationLogs(
              preferencesHelper.getSessionToken(), VariableConstant.LANGUAGE, latLngBody);

      locationLogs
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeOn(Schedulers.io())
          .subscribe(
              new Observer<Response<ResponseBody>>() {
                @Override
                public void onSubscribe(Disposable d) {}

                @Override
                public void onNext(Response<ResponseBody> value) {
                  try {
                    JSONObject jsonObject;
                    if (value.code() == 200) {
                      String response = value.body().string();
                      jsonObject = new JSONObject(response);
                      preferencesHelper.setBookingId(null);
                      couchDBHandle.deleteDocument();
                      Observable<Response<ResponseBody>> completeMeterBooking =
                          networkService.completeMeterBooking(
                              preferencesHelper.getSessionToken(),
                              preferencesHelper.getLanguage(),
                              bid,
                              String.valueOf(preferencesHelper.getCurrLatitude()),
                              String.valueOf(preferencesHelper.getCurrLongitude()),
                              String.valueOf(timeinsec),
                              String.valueOf(estimatedTimeFare),
                              LocationUpdateService.getLocationUpdateService()
                                  .getCumulativeDistance(),
                              String.valueOf(estimatedDistanceFare),
                              String.valueOf(systemFare));

        completeMeterBooking.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            switch (value.code()) {
                                case VariableConstant.RESPONSE_CODE_SUCCESS:
                                    String resp=value.body().string();
                                    Utility.printLog("completeMeterBooking success response is  : \n" +resp);

                                    DriverMeterInvoiceData driverMeterInvoiceData  = new DriverMeterInvoiceData();
                                    driverMeterInvoiceData.setBid(bookingAssignedDataRideAppts.getBookingId());
                                    driverMeterInvoiceData.setMinFare(bookingAssignedDataRideAppts.getMinFee());
                                    driverMeterInvoiceData.setTripOnTime(bookingAssignedDataRideAppts.getBookingDate());
                                    driverMeterInvoiceData.setBaseFare(bookingAssignedDataRideAppts.getBaseFee());
                                    driverMeterInvoiceData.setDistanceFare(estimatedDistanceFare);
                                    driverMeterInvoiceData.setTimeFare(estimatedTimeFare);
                                    driverMeterInvoiceData.setTotalFare(systemFare);
                                    driverMeterInvoiceData.setCurrencySymbol(bookingAssignedDataRideAppts.getCurrencySbl());

                                    driverMeterView.showInvoice(driverMeterInvoiceData);

                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    driverMeterView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    String err=value.errorBody().string();
                                    Utility.printLog("completeMeterBooking error response is  : \n" +err);
                                    driverMeterView.apiFailure(DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        driverMeterView.hideProgress();
                    }

                                @Override
                                public void onComplete() {
                                  driverMeterView.hideProgress();
                                }
                              });
                    } else {
                      jsonObject = new JSONObject(value.errorBody().string());
                      //
                      // view.setError(value.code(),jsonObject.getString("message"));
                    }

                    Utility.printLog(TAG + " locationLogs : " + jsonObject.toString());

                  } catch (Exception e) {
                    Utility.printLog(TAG + " locationLogs : Catch :" + e.getMessage());
                  }
                }

                @Override
                public void onError(Throwable e) {}

                @Override
                public void onComplete() {}
              });
    }
  }


  @Override
    public void startTimer(String seconds) {

        timeinsec  = Integer.parseInt(seconds);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utility.printLog("the timer running .." + runTimer);
                if(runTimer) {
                    timeinsec = timeinsec + 1;
                    Utility.printLog("the timer running .." + timeinsec);
                    handler.postDelayed(this, 1000);
                    driverMeterView.setRunningTimer(getDurationString(timeinsec));

                    int fareTimeInMin = getTotalMin(timeinsec)-Integer.parseInt(bookingAssignedDataRideAppts.getTimeFeeXMinute());
                    Utility.printLog("time fare is fareTimeInMin : " + fareTimeInMin);
                    if(fareTimeInMin>0) {
                        int totalMin = getTotalMin(timeinsec-Integer.parseInt(bookingAssignedDataRideAppts.getTimeFeeXMinute()));
                        float timeFare = fareTimeInMin * Float.parseFloat(bookingAssignedDataRideAppts.getTimeFee());
                        Utility.printLog("time fare is timeFare : " + timeFare);
                        if (fareTimeInMin != oldTotalMin) {
                            oldTotalMin = fareTimeInMin;
                            estimatedTimeFare = timeFare;
                            Utility.printLog("time fare is estimatedTimeFare : " + estimatedTimeFare);
                            getTimeFare();

                        }
                    }
                }

            }
        },0);
    }

    /**
     * <h1>getDurationString</h1>
     * <p>format of timer set</p>
     * @param seconds : the input in seconds for convert the time format
     * @return :time format
     */
    private String getDurationString(int seconds)
    {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    /**
     * <h1>twoDigitString</h1>
     * <p>number format for keep 2 digit</p>
     * @param number : number
     * @return : 2 digit number
     */
    private String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }
        return String.valueOf(number);
    }

    /**
     * <h1>getTotalMin</h1>
     * <p>for get the second in minute</p>
     * @param sec : second
     * @return : return the minute
     */
    private int getTotalMin(int sec){
        return sec/60;
    }

    @Override
    public void findCurrentLocation() {
        if(preferencesHelper.getCurrLongitude()!=0.00 && preferencesHelper.getCurrLatitude()!=0.00)
            driverMeterView.moveGoogleMapToLocation(preferencesHelper.getCurrLatitude(),preferencesHelper.getCurrLongitude());
    }


    @Override
    public void fetchAddress(Intent data, String bid) {
        Place place = PlaceAutocomplete.getPlace(context, data);
        String fullName = (String) place.getAddress();
        Utility.printLog("the selected address is : "+fullName);
    }


    @Override
    public void startGoogleMap() {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                preferencesHelper.getCurrLatitude(), preferencesHelper.getCurrLongitude(),
                dropLng,dropLat);
        driverMeterView.connectGoogleMap(uri);
    }

    @Override
    public void startWaze() {
        String uri = "waze://?ll=" + dropLat + "," + dropLng + "&navigate=yes";
        driverMeterView.connectWaze(uri);
    }

    @Override
    public void checkCurrentLocation() {
        LatLng latLng = new LatLng(preferencesHelper.getCurrLatitude(),preferencesHelper.getCurrLongitude());
        driverMeterView.setCurrentLocation(latLng);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getCurrentLocation()
    {
      //  locationProvider.startLocation(this);
    }

    @Override
    public void disposeObservables() {
       // locationProvider.stopLocationUpdates();
    }

    @Override
    public void addCarMarker() {
        double[] size = Scaler.getScalingFactor(context);
        double height = (65) * size[0];
        double width = (65) * size[1];
        LatLng latLng = new LatLng(preferencesHelper.getCurrLatitude(),
                preferencesHelper.getCurrLongitude());
        driverMeterView.setCarMarker(latLng, width, height, bookingAssignedDataRideAppts.getVehicleMapIcon());
    }

    @Override
    public void getVehicleMoveBearing(Location location, Projection projection) {

        mCurrentLoc=location;

        if(mPreviousLoc==null)
        {
            mPreviousLoc=location;
        }
        final float bearing = mPreviousLoc.bearingTo(mCurrentLoc);
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Point startPoint = projection.toScreenLocation(new LatLng(mPreviousLoc.getLatitude(),mPreviousLoc.getLongitude()));
        Point endpoint = projection.toScreenLocation(new LatLng(location.getLatitude(),location.getLongitude()));
        final LatLng startLatLng = projection.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * mCurrentLoc.getLongitude() + (1 - t)
                        * startLatLng.longitude;
                double lat = t * mCurrentLoc.getLatitude() + (1 - t)
                        * startLatLng.latitude;

                if(!startPoint.equals(endpoint)) {
                    VariableConstant.BEARING = bearing;
                    driverMeterView.setCarMove(new LatLng(lat,lng),bearing);
                }

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 20);
                }

            }
        });

        mPreviousLoc=mCurrentLoc;
        driverMeterView.moveGoogleMapToLocation(location.getLatitude(),location.getLongitude());

    }


    private void getTimeFare(){
        setFare();
    }

    /**
     * <h1>getDistanceFare</h1>
     * <p>calculate the distance fare</p>
     */
    private void getDistanceFare(){
        try {
            Double distanceShow = preferencesHelper.getDistanceCalculatedShow()-Double.parseDouble(bookingAssignedDataRideAppts.getMileageAfterXMetric());
            Utility.printLog("the distance in integer : distance : " + preferencesHelper.getDistanceCalculatedShow()+", "+distanceShow);
            if(distanceShow>0) {
                Double distanceFare = distanceShow * Float.parseFloat(bookingAssignedDataRideAppts.getMileagePrice());
                Utility.printLog("the distance in integer : distanceFare : " + distanceFare);
                estimatedDistanceFare = Float.parseFloat(String.valueOf(distanceFare));
                setFare();
            }

        }catch (Exception e){
            Utility.printLog("catch : "+e);
        }
    }

    /**
     * <h1>setFare</h1>
     * <p>set the UI depends on the change fare values</p>
     */
    private void setFare(){
        totalFare = estimatedTimeFare+estimatedDistanceFare+Float.parseFloat(bookingAssignedDataRideAppts.getBaseFee());

        float minFare = Float.parseFloat(bookingAssignedDataRideAppts.getMinFee());
        if(totalFare>minFare) {
            driverMeterView.calculatedFareGreatUI();
            driverMeterView.fareSet(
                    currecySymbol + String.format("%.2f", totalFare),
                    currecySymbol + String.format("%.2f", estimatedTimeFare),
                    currecySymbol + String.format("%.2f", estimatedDistanceFare),
                    currecySymbol + String.format("%.2f", totalFare)
            );
            systemFare = totalFare;

        }

        else {
            driverMeterView.minimumFareGreatUI();
            driverMeterView.fareSet(
                    currecySymbol + String.format("%.2f", minFare),
                    currecySymbol + String.format("%.2f", estimatedTimeFare),
                    currecySymbol + String.format("%.2f", estimatedDistanceFare),
                    currecySymbol + String.format("%.2f", minFare)
            );

            systemFare = minFare;
        }
    }





    @Override
    public void setDropLocationAPI(String dropAddress, String dropLatLng, String dropPlaceID, String bid) {

   /* }
    private void setDropLocationAPI(Place place, String bid){*/

        dropLatLng = dropLatLng.substring(10,dropLatLng.length()-1);
        String[] dropLatLong =  dropLatLng.split(",");

        Utility.printLog("the bundle values are : "+dropLatLong[0]);
        Utility.printLog("the bundle values are : "+dropLatLong[1]);

        driverMeterView.showProgress();
        Observable<Response<ResponseBody>> setDropLoc = networkService.setDropLocation
                (preferencesHelper.getSessionToken(),
                        preferencesHelper.getLanguage(),
                        bid,
                        dropPlaceID,
                        null,
                        dropAddress,
                        null,
                        null,
                        null,
                        null,
                        null,
                        dropLatLong[1],
                        dropLatLong[0]);

        setDropLoc.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            switch (value.code()) {
                                case VariableConstant.RESPONSE_CODE_SUCCESS:
                                    String resp=value.body().string();
                                    Utility.printLog("setDropLocationAPI success response is  : \n" +resp);
                                    dropLat  = Double.parseDouble(dropLatLong[0]);
                                    dropLng = Double.parseDouble(dropLatLong[1]);
                                    driverMeterView.navigationEnable(true);
                                    driverMeterView.setAddress(dropAddress);
                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    driverMeterView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    String err=value.errorBody().string();
                                    Utility.printLog("setDropLocationAPI error response is  : \n" +err);
                                    driverMeterView.apiFailure(DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        driverMeterView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        driverMeterView.hideProgress();
                    }
                });
    }



    @Override
    public void onUpdateLocation(Location location) {

    }

    @Override
    public void locationMsg(String error) {

    }

    /**
     * <h2>subscribeLocationChange</h2>
     * This method is used to subscribe for the location change
     */
    private void subscribeLocationChange()
    {
      RxLocationObserver.getInstance()
          .getObservable()
          .subscribe(
              new DisposableObserver<Location>() {
                @Override
                public void onNext(Location location) {
                  Utility.printLog(
                      TAG
                          + " onNext location oberved  "
                          + location.getLatitude()
                          + " "
                          + location.getLongitude());
                  driverMeterView.locationUpdated(location);
                  driverMeterView.setDistanceShow(preferencesHelper.getDistanceCalculatedShow());
                  getDistanceFare();
                }

                @Override
                public void onError(Throwable e) {}

                @Override
                public void onComplete() {}
              });
    }

    @Override
    public void onLocationServiceDisabled(Status status) {

    }

    @Override
    public void subscribeNetworkObserver()
    {
        Observer<NetworkStateHolder> observer = new Observer<NetworkStateHolder>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
                compositeDisposable.add(d);
            }
            @Override
            public void onNext(NetworkStateHolder value)
            {
                Utility.printLog(""+" network not avalable "+value.isConnected());
                networkAvailable = networkStateHolder.isConnected();
                try {
                    if(value.isConnected())
                        driverMeterView.networkAvailable();
                    else
                        driverMeterView.networkNotAvailable();
                }catch (Exception e){
                    Utility.printLog(""+ Arrays.toString(e.getStackTrace()));
                }

            }
            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
            }
            @Override
            public void onComplete()
            {}
        };
        rxNetworkObserver.subscribeOn(Schedulers.io());
        rxNetworkObserver.observeOn(AndroidSchedulers.mainThread());
        rxNetworkObserver.subscribe(observer);
    }

    @Override
    public void networkCheckOnresume() {
        if(networkAvailable)
            driverMeterView.networkAvailable();
        else
            driverMeterView.networkNotAvailable();
    }


    @Override
    public void clearComposite() {
        compositeDisposable.clear();
    }

    @Override
    public void getPreviousDistance() {
      if (LocationUpdateService.getLocationUpdateService().getCumulativeDistance() != null
          && Double.parseDouble(
          LocationUpdateService.getLocationUpdateService().getCumulativeDistance())
          != 0.0) {
        Double distance =
            Double.parseDouble(
                LocationUpdateService.getLocationUpdateService().getCumulativeDistance())
                / preferencesHelper.getMileageMetricUnit();

            String dis = String.format("%.2f",distance);
            driverMeterView.setDistanceShow(Double.parseDouble(dis));
            preferencesHelper.setDistanceCalculatedShow(dis);
        }
    }

    @Override
    public void storeCurrentTime() {
        pauseTime  = Calendar.getInstance().getTime();
        Utility.printLog("the current time is : "+pauseTime);
    }

    @Override
    public void enableRunTime(boolean enable) {
        runTimer = enable;
    }

    @Override
    public void findBackgroundSeconds() {
        if(pauseTime!=null)
            DateDifference(pauseTime,Calendar.getInstance().getTime());
    }


    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    private void DateDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        pauseTimeinSec = (int) (different*0.001);
        System.out.println("total seconds..... : " + pauseTimeinSec);
        timeinsec+=pauseTimeinSec;
        startTimer(String.valueOf(timeinsec));

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
    }


}
