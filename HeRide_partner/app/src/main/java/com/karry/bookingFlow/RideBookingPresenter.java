package com.karry.bookingFlow;

import static com.karry.side_screens.home_fragment.HomeFragment.homeFrag;
import static com.karry.utility.VariableConstant.DATA;
import static com.karry.utility.VariableConstant.RESPONSE_CODE_SUCCESS;
import static com.karry.utility.VariableConstant.RESPONSE_UNAUTHORIZED;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.karry.app.MyApplication;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.bookingFlow.model.RideBookingCancel;
import com.karry.bookingFlow.model.RideBookingDropUpdate;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.data.source.sqlite.SQLiteDataSource;
import com.karry.manager.booking.RxDriverCancelledObserver;
import com.karry.manager.booking.RxDropLocationUpdateObserver;
import com.karry.manager.location.LocationManagerCallback;
import com.karry.manager.location.RxLocationObserver;
import com.karry.manager.mqtt_manager.MQTTManager;
import com.karry.network.NetworkService;
import com.karry.network.NetworkStateHolder;
import com.karry.network.RxNetworkObserver;
import com.heride.partner.R;
import com.karry.pojo.bookingAssigned.BookingAssignedDataRideAppts;
import com.karry.pojo.bookingAssigned.TowTrayService;
import com.karry.pojo.bookingAssigned.TowtrayServiceSelectData;
import com.karry.service.CouchDbHandler;
import com.karry.service.LatLngBody;
import com.karry.service.LocationUpdateService;
import com.karry.telecall.callingapi.CallingService;
import com.karry.utility.AppConstants;
import com.karry.utility.DataParser;
import com.karry.utility.Scaler;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.karry.utility.path_plot.DownloadPathUrl;
import com.karry.utility.path_plot.GetGoogleDirectionsUrl;
import com.karry.utility.path_plot.LatLongBounds;
import com.karry.utility.path_plot.RotateKey;
import com.karry.utility.path_plot.RxRoutePathObserver;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
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
 * <h1>RideBookingPresenter</h1>
 * <p>the implementation of the ride booking is doing in this class and inform to View for the changes</p>
 */
public class RideBookingPresenter implements RideBookingContract.RideBookingPresenter,
        LocationManagerCallback.CallBacks
{
  private static final String TAG = RideBookingPresenter.class.getSimpleName();
    @Inject
    Gson gson;
    @Inject NetworkService networkService;
    @Inject SQLiteDataSource addressDataSource;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject RideBookingContract.RideBookingView rideBookingView;
    @Inject Context context;
    @Inject CompositeDisposable compositeDisposable;
    @Inject RxNetworkObserver rxNetworkObserver;
    @Inject NetworkStateHolder networkStateHolder;
    private RxLocationObserver rxLocationObserver;
    private BookingAssignedDataRideAppts bookingAssignedDataRideAppts;
    private Double navigationDropLat = 0.0;
    private Double navigationDropLng = 0.0;
    private static int timeinsec = 0;
    private Handler handler;
    private boolean runTimer = true;
    private Date pauseTime;
    private int pauseTimeinSec = 0;
    private Location mCurrentLoc,mPreviousLoc;
    private BitmapDescriptor icon;
    private BitmapDescriptor prevIcon;
    private LatLng iconSetLatLng;
    private LatLng prevIconSetLatLng;
    private boolean networkAvailable = true;
    @Inject MQTTManager mqttManager;
    private RxDriverCancelledObserver rxDriverCancelledObserver;
    private RxDropLocationUpdateObserver rxDropLocationUpdateObserver;

    private RxRoutePathObserver rxRoutePathObserver;
    private BitmapDescriptor pickupIcon;
    private Disposable pathPlotDisposable;
    private LatLng latLngOrigin,latLngPickUp,latLngPickUpOrigin,latLngDrop;
    private ArrayList<TowtrayServiceSelectData> towtrayServiceSelectData = new ArrayList<>();
    private CouchDbHandler couchDBHandle;

    @Inject
    CallingService callingService;

    @Inject
    RideBookingPresenter(RxLocationObserver rxLocationObserver,
                         RxDriverCancelledObserver rxDriverCancelledObserver,
                         RxDropLocationUpdateObserver rxDropLocationUpdateObserver, RxRoutePathObserver rxRoutePathObserver)
    {
        this.rxLocationObserver = rxLocationObserver;
        this.rxDriverCancelledObserver = rxDriverCancelledObserver;
        this.rxDropLocationUpdateObserver = rxDropLocationUpdateObserver;
        subscribeLocationChange();
        subscribeDriverCancelDetails();
        subscribeDropLocation();

        this.rxRoutePathObserver = rxRoutePathObserver;
        subscribeRoutePoints();
        MyApplication controller = (MyApplication) getApplicationContext();
        couchDBHandle = controller.getDBHandler();
  }


    /**
     * <h2>subscribeRoutePoints</h2>
     * This method is used to subscribe to the path points from origin to destination
     */
    private void subscribeRoutePoints()
    {
        rxRoutePathObserver.subscribeOn(Schedulers.io());
        rxRoutePathObserver.observeOn(AndroidSchedulers.mainThread());
        pathPlotDisposable = rxRoutePathObserver.subscribeWith( new DisposableObserver<LatLongBounds>()
        {
            @Override
            public void onNext(LatLongBounds latLongBounds)
            {
                Utility.printLog("path plot route "+latLongBounds.getNortheast()+"\n"+latLongBounds.getEstimatedDistanceTime().getDistance());
                rideBookingView.googlePathPlot(latLongBounds);

            }


            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog("path plot route  "+e);
            }

            @Override
            public void onComplete()
            {

            }
        });
    }

    @Override
    public boolean isTowtruck() {
        return bookingAssignedDataRideAppts.isTowTruckBooking();
    }

    @Override
    public void getRideBookingData(String rideBookingData) {
        Utility.printLog("the rideBookingData is : "+ rideBookingData);
        bookingAssignedDataRideAppts  = gson.fromJson(rideBookingData,BookingAssignedDataRideAppts.class);

        String paymentType = bookingAssignedDataRideAppts.getPaymentType();
        if(bookingAssignedDataRideAppts.getPaymentType().contains("+")){
            paymentType = paymentType.replace("+",",");
            paymentType = paymentType.replace(" ","");
        }

        rideBookingView.setText(bookingAssignedDataRideAppts, preferenceHelperDataSource.getMyName(),paymentType);
        if(bookingAssignedDataRideAppts.getSomeOne().getIsSomeOneElseBooking().matches("1")){
            rideBookingView.setBookigForSomeOne();
        }

        String timeStamp = "0";
        long startDate = 0;
        long endDate = 0;

        switch (bookingAssignedDataRideAppts.getStatus()){
            case AppConstants.BookingStatus.Arrived:
                timeStamp = Utility.convertUTCToServerFormat(bookingAssignedDataRideAppts.getArrived(),VariableConstant.GMT_TIME_FORMAT);
                break;

            case AppConstants.BookingStatus.started:
                timeStamp = Utility.convertUTCToServerFormat(bookingAssignedDataRideAppts.getJourneyStart(),VariableConstant.GMT_TIME_FORMAT);
                break;

        }
        try {
            startDate = Date.parse(timeStamp);
            Utility.printLog("testing startDate :  "+startDate);
            endDate = Date.parse(String.valueOf(Calendar.getInstance().getTime()));
            Utility.printLog("testing endDate :  "+endDate);
            long different = endDate - startDate;
            pauseTimeinSec = (int) (different*0.001);
            pauseTimeinSec-=VariableConstant.TIMEDIFFERENCE;
            System.out.println("testing total seconds..... : " + pauseTimeinSec);

    } catch (Exception e) {
      Utility.printLog("Exception in time cal ");
    }
    setStatusChangeText(bookingAssignedDataRideAppts.getStatus(), String.valueOf(pauseTimeinSec),preferenceHelperDataSource.getBookingId());
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
              / preferenceHelperDataSource.getMileageMetricUnit();

            Utility.printLog("the distance of previous : "+String.format("%.2f", distance));
            String dis = String.format("%.2f",distance);
            rideBookingView.setDistanceShow(dis.concat(" ").concat(bookingAssignedDataRideAppts.getMileageMetric()));
            preferenceHelperDataSource.setDistanceCalculatedShow(dis);
        }
    }


    @Override
    public void setActionBar() {
        rideBookingView.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        rideBookingView.setTitle();
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
                networkAvailable = networkStateHolder.isConnected();
                Utility.printLog(""+" network not avalable "+value.isConnected());
                try {
                    if(value.isConnected())
                        rideBookingView.networkAvailable();
                    else
                        rideBookingView.networkNotAvailable();
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
            rideBookingView.networkAvailable();
        else
            rideBookingView.networkNotAvailable();
    }

    @Override
    public void onTowTryService(Intent data) {
        if (towtrayServiceSelectData != null) {
            towtrayServiceSelectData.clear();
            towtrayServiceSelectData = new ArrayList<>();
        }

        towtrayServiceSelectData = (ArrayList<TowtrayServiceSelectData>) data.getSerializableExtra(DATA);
        for (TowtrayServiceSelectData towtrayServiceSelectData : towtrayServiceSelectData) {
            if (towtrayServiceSelectData.isSelected()) {
                Utility.printLog("selected serices are :  "+towtrayServiceSelectData.getTitle());
            }

        }
    }



    /**
     * <h2>subscribeLocationChange</h2>
     * <p>This method is used to subscribe for the location change</p>
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
                  preferenceHelperDataSource.setCurrLatitude(String.valueOf(location.getLatitude()));
                  preferenceHelperDataSource.setCurrLongitude(String.valueOf(location.getLongitude()));
                  findCurrentLocation();
                  rideBookingView.locationUpdated(location);

                  rideBookingView.setDistanceShow(String.valueOf(preferenceHelperDataSource.getDistanceCalculatedShow()).concat(" ").concat(bookingAssignedDataRideAppts.getMileageMetric()));

                }

                @Override
                public void onError(Throwable e) {}

                @Override
                public void onComplete() {}
              });
    }

    /**
     * <h2>subscribeDriverDetails</h2>
     * This method is used to subscribe to the driver details published
     */
    private void subscribeDriverCancelDetails()
    {
        Observer<RideBookingCancel> observer = new Observer<RideBookingCancel>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
            }

            @Override
            public void onNext(RideBookingCancel rideBookingCancel)
            {
                Utility.printLog(" cancel driver details observed  "+rideBookingCancel.getStatus());
                /*VariableConstant.CANCEL_RIDE_REASON  = rideBookingCancel.getMsg();*/
                VariableConstant.CANCEL_RIDE = true;
                rideBookingView.bookingCancelledPas();

            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(" cancel driver details onError  "+e);
            }

            @Override
            public void onComplete()
            {}
        };
        rxDriverCancelledObserver.subscribeOn(Schedulers.io());
        rxDriverCancelledObserver.observeOn(AndroidSchedulers.mainThread());
        rxDriverCancelledObserver.subscribe(observer);
    }

    /**
     * <h2>subscribeDropLocation</h2>
     * This method is used to subscribe drop location published
     */
    private void subscribeDropLocation()
    {
        Observer<RideBookingDropUpdate> observer = new Observer<RideBookingDropUpdate>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {

            }

            @Override
            public void onNext(RideBookingDropUpdate rideBookingDropUpdate)
            {
                Utility.printLog(" drop location details observed  "+bookingAssignedDataRideAppts.getDropAddress()+"\n"+
                        bookingAssignedDataRideAppts.getDropLatLong());

                bookingAssignedDataRideAppts.setDropAddress(rideBookingDropUpdate.getDropAddress());
                icon = BitmapDescriptorFactory.fromResource(R.drawable.flag_pickup);
                iconSetLatLng = new LatLng(navigationDropLat,navigationDropLng);
                bookingAssignedDataRideAppts.setDropLatLong(rideBookingDropUpdate.getDropLatitude().concat(",").concat(rideBookingDropUpdate.getDropLongitude()));
                Utility.printLog(" drop location details observed  "+bookingAssignedDataRideAppts.getDropAddress()+"\n"+
                        bookingAssignedDataRideAppts.getDropLatLong());

                rideBookingView.showDropLocUpdated();
                if(bookingAssignedDataRideAppts.getStatus().matches(AppConstants.BookingStatus.started)) {
                    rideBookingView.setFlagPickupDrop(icon,iconSetLatLng);
                    rideBookingView.showDropLocAddress(rideBookingDropUpdate.getDropAddress());
                    navigationDropLat = Double.parseDouble(rideBookingDropUpdate.getDropLatitude());
                    navigationDropLng = Double.parseDouble(rideBookingDropUpdate.getDropLongitude());


                    icon = BitmapDescriptorFactory.fromResource(R.drawable.flag_drop);
                    if(navigationDropLng==0 && navigationDropLat==0 && bookingAssignedDataRideAppts.getDropAddress().matches("")){
                        rideBookingView.dropLocDisable();
                    }else {
                        iconSetLatLng = new LatLng(navigationDropLat,navigationDropLng);
                        if(icon != null)
                            rideBookingView.setFlagPickupDrop(icon,iconSetLatLng);
                    }

                    if(navigationDropLat!=0 && navigationDropLng!=0){
                        latLngOrigin = new LatLng(preferenceHelperDataSource.getCurrLatitude(),preferenceHelperDataSource.getCurrLongitude());
                        latLngPickUp = new LatLng(navigationDropLat,navigationDropLng);
                        int color = ContextCompat.getColor(context,R.color.red);



                        DownloadPathUrl downloadPathUrl =  new DownloadPathUrl(rxRoutePathObserver, color,
                                preferenceHelperDataSource, new RotateKey() {
                            @Override
                            public void rotateKey() {
                                DownloadPathUrl downloadPathUrl =  new DownloadPathUrl(rxRoutePathObserver, color, preferenceHelperDataSource,this);
                                String pickupURL = GetGoogleDirectionsUrl.getDirectionsUrl(latLngOrigin,latLngPickUp,preferenceHelperDataSource.getGoogleServerKey());
                                downloadPathUrl.execute(pickupURL);
                            }
                        });
                        String pickupURL = GetGoogleDirectionsUrl.getDirectionsUrl(latLngOrigin,latLngPickUp,preferenceHelperDataSource.getGoogleServerKey());
                        downloadPathUrl.execute(pickupURL);
                    }

                }

            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(" cancel driver details onError  "+e);
            }

            @Override
            public void onComplete()
            {}
        };
        rxDropLocationUpdateObserver.subscribeOn(Schedulers.io());
        rxDropLocationUpdateObserver.observeOn(AndroidSchedulers.mainThread());
        rxDropLocationUpdateObserver.subscribe(observer);
    }

    @Override
    public void clearComposite() {
        compositeDisposable.clear();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getCurrentLocation()
    {
    }

    @Override
    public void disposeObservables() {
    }

    @Override
    public void addCarMarker() {
        double[] size = Scaler.getScalingFactor(context);
        int height = (int) ((65) * size[0]);
        int width = (int) ((65) * size[1]);
        LatLng latLng = new LatLng(preferenceHelperDataSource.getCurrLatitude(),
                preferenceHelperDataSource.getCurrLongitude());
        rideBookingView.setCarMarker(latLng, width, height, bookingAssignedDataRideAppts.getVehicleMapIcon());


        if(iconSetLatLng!=null && icon!=null && (prevIcon==null || prevIcon!=icon) && (prevIconSetLatLng==null || prevIconSetLatLng!=iconSetLatLng)) {
            prevIconSetLatLng = iconSetLatLng;
            prevIcon = icon;
            rideBookingView.setFlagPickupDrop(icon, iconSetLatLng);
        }
    }

    @Override
    public void onUpdateLocation(Location location) {

    }

    @Override
    public void locationMsg(String error) {

    }

    @Override
    public void onLocationServiceDisabled(Status status) {
        rideBookingView.promptUserWithLocationAlert(status);
    }

    @Override
    public void findCurrentLocation()
    {
        Utility.printLog("findCurrentLocation"+preferenceHelperDataSource.getCurrLatitude());
        if(!preferenceHelperDataSource.getCurrLatitude().equals("0.0") && !preferenceHelperDataSource.getCurrLongitude().equals("0.0") ) {
            double lat = preferenceHelperDataSource.getCurrLatitude();
            double lang = preferenceHelperDataSource.getCurrLongitude();
            rideBookingView.moveGoogleMapToLocation(lat,lang);
        }
    }

    @Override
    public void checkCurrentLocation() {
        LatLng latLng = new LatLng(preferenceHelperDataSource.getCurrLatitude(),preferenceHelperDataSource.getCurrLongitude());
        rideBookingView.setCurrentLocation(latLng);
    }

    @Override
    public void updateBookingStatusRide() {

        Utility.printLog("the booking status is : "+preferenceHelperDataSource.getBookingStatus());
        switch (preferenceHelperDataSource.getBookingStatus()){

            case AppConstants.BookingStatus.OnTheWay:
                if (preferenceHelperDataSource.getCurrLatitude() != 0.00 && preferenceHelperDataSource.getCurrLatitude() != 0.00){
                    List<String> pickupLatLng;
                    pickupLatLng = Arrays.asList( bookingAssignedDataRideAppts.getPickupLatLong().split(","));
                    double radiusDistance = LocationUpdateService.distance(preferenceHelperDataSource.getCurrLatitude(),
                            preferenceHelperDataSource.getCurrLongitude(),
                            Double.parseDouble(pickupLatLng.get(0)),Double.parseDouble(pickupLatLng.get(1)), "meters");

                    Utility.printLog("radiusDistance is : "+preferenceHelperDataSource.getCurrLatitude()+"  , getArrivedRadius : "+preferenceHelperDataSource.getCurrLongitude());
                    Utility.printLog("radiusDistance is : "+radiusDistance+"  , getArrivedRadius : "+bookingAssignedDataRideAppts.getArrivedRadius());
                    if(radiusDistance<Double.parseDouble(bookingAssignedDataRideAppts.getArrivedRadius())){
                        updateBookingStatusAPI(AppConstants.getAptStatus_Arrived.value);
                    }else {
                        String errMsg = context.getResources().getString(R.string.youAreNot).concat(" ".concat(bookingAssignedDataRideAppts.getArrivedRadius()).concat("mtr ").concat(context.getResources().getString(R.string.rangePick)));
                        rideBookingView.apiFailure(errMsg);
                    }

                }else {
                    subscribeLocationChange();
                    updateBookingStatusRide();
                }

                /*updateBookingStatusAPI(AppConstants.BookingStatus.Arrived);*/
                break;

            case AppConstants.BookingStatus.Arrived:
                updateBookingStatusAPI(AppConstants.BookingStatus.started);
                break;

            case AppConstants.BookingStatus.started:
                if(bookingAssignedDataRideAppts.isTowTruckBooking())
                {
                    getTowTruckServices();
                }else
                    updateBookingStatusAPI(AppConstants.BookingStatus.Completed);
                break;
        }
    }

    @Override
    public void startGoogleMap() {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                preferenceHelperDataSource.getCurrLatitude(), preferenceHelperDataSource.getCurrLongitude(),
                navigationDropLat,navigationDropLng);
        rideBookingView.connectGoogleMap(uri);
    }

    @Override
    public void startWaze() {
        String uri = "waze://?ll=" + navigationDropLat + "," + navigationDropLng + "&navigate=yes";
        rideBookingView .connectWaze(uri);
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
                    rideBookingView.setRunningTimer(getDurationString(timeinsec));
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


    @Override
    public void enableRunTime(boolean enable) {
        runTimer = enable;
    }

    @Override
    public void findBackgroundSeconds() {
        if(pauseTime!=null)
            DateDifference(pauseTime, Calendar.getInstance().getTime());
    }

    @Override
    public void storeCurrentTime() {
        pauseTime  = Calendar.getInstance().getTime();
        Utility.printLog("the current time is : "+pauseTime);
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
        System.out.println("total seconds.....bookingAssignedDataRideAppts.getStatus()   : " + bookingAssignedDataRideAppts.getStatus());
        timeinsec+=pauseTimeinSec;

        if(runTimer && (bookingAssignedDataRideAppts.getStatus().matches(String.valueOf(AppConstants.BookingStatus.Arrived)) ||
                bookingAssignedDataRideAppts.getStatus().matches(String.valueOf(AppConstants.BookingStatus.started)) ))
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
  /**
   *
   *
   * <h1>updateBookingStatusAPI</h1>
   *
   * <p>the API for update the booking status
   */
  private void updateBookingStatusAPI(String status) {
    if (preferenceHelperDataSource.getCurrLatitude() != 0.00
        && preferenceHelperDataSource.getCurrLatitude() != 0.00) {
      rideBookingView.showProgress();

      if (status.equals(AppConstants.BookingStatus.Completed)) {
        updateLocationLogs(status);
      } else {
        updateBookingStatusAPi(status);
      }
    } else {
      subscribeLocationChange();
      updateBookingStatusAPI(status);
    }
  }

  private void updateBookingStatusAPi(String status) {
    Observable<Response<ResponseBody>> bookingStatusRide =
        networkService.bookingStatusRide(
            preferenceHelperDataSource.getSessionToken(),
            preferenceHelperDataSource.getLanguage(),
            preferenceHelperDataSource.getBookingId(),
            Integer.parseInt(status),
            String.valueOf(preferenceHelperDataSource.getCurrLatitude()),
            String.valueOf(preferenceHelperDataSource.getCurrLongitude()),
            String.valueOf(Double.parseDouble(LocationUpdateService.getLocationUpdateService()
                .getCumulativeDistance())),
            Utility.date());

            bookingStatusRide.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {

                try {
                  switch (value.code()) {
                    case RESPONSE_CODE_SUCCESS:
                      String bookingId = preferenceHelperDataSource.getBookingId();
                      if (status.equals(AppConstants.BookingStatus.Completed)) {
                        preferenceHelperDataSource.setBookingId(null);
                      }
                      String resp = value.body().string();
                      Utility.printLog("bookingStatusRide success response is  : \n" + resp);
                      preferenceHelperDataSource.setBookingStatus(status);
                      setStatusChangeText(status, "0",bookingId);
                      break;

                                    case RESPONSE_UNAUTHORIZED:
                                        String err_ = value.errorBody().string();
                                        Utility.printLog("bookingStatusRide error response is  : \n" + err_);
                                        VariableConstant.FCM_TOPIS = preferenceHelperDataSource.getFcmTopic();
                                        VariableConstant.MQTT_TOPICS = preferenceHelperDataSource.getMqttTopic();
                                        LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                        preferenceHelperDataSource.clearSharedPredf();
                                        preferenceHelperDataSource.setLanguageSettings(languagesList);
                                        rideBookingView.goToLogin(DataParser.fetchErrorMessage(value));
                                        break;

                                    default:
                                        String err="";
                                        err = DataParser.fetchErrorMessage(value);
                                        if(err.matches(""))
                                            err = value.errorBody().string();
                                        Utility.printLog("bookingStatusRide error response is  : \n" + err);
                                        rideBookingView.apiFailure(err);
                                        break;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

              @Override
              public void onError(Throwable e) {
                rideBookingView.hideProgress();
              }

              @Override
              public void onComplete() {
                rideBookingView.hideProgress();
              }
            });
  }

  private void updateLocationLogs(String status) {

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
              preferenceHelperDataSource.getSessionToken(), VariableConstant.LANGUAGE, latLngBody);

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
                      couchDBHandle.deleteDocument();
                      updateBookingStatusAPi(status);
                    } else {
                      jsonObject = new JSONObject(value.errorBody().string());
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

  /**
   *
   *
   * <h1>setStatusChangeText</h1>
   *
   * <p>which check the status match and set the view
   *
   * @param status :ride booking status
   */
  private void setStatusChangeText(String status, String time,String bookingId) {
    bookingAssignedDataRideAppts.setStatus(String.valueOf(status));

        if(bookingAssignedDataRideAppts.isTowTruckBooking()){
            rideBookingView.setCustVehSet(bookingAssignedDataRideAppts.getVehicleDetails());
        }

        List<String> latLng;
        switch (status){

            case AppConstants.BookingStatus.OnTheWay:
                latLng = Arrays.asList( bookingAssignedDataRideAppts.getPickupLatLong().split(","));
                navigationDropLat = Double.parseDouble(latLng.get(0));
                navigationDropLng = Double.parseDouble(latLng.get(1));
                icon = BitmapDescriptorFactory.fromResource(R.drawable.flag_pickup);
                iconSetLatLng = new LatLng(navigationDropLat,navigationDropLng);
                if(icon != null)
                    rideBookingView.setFlagPickupDrop(icon,iconSetLatLng);
                rideBookingView.setOnTheWayText();

                if(navigationDropLat!=0 && navigationDropLng!=0){
                    latLngOrigin = new LatLng(preferenceHelperDataSource.getCurrLatitude(),preferenceHelperDataSource.getCurrLongitude());
                    latLngPickUp = new LatLng(navigationDropLat,navigationDropLng);
                    int color = ContextCompat.getColor(context,R.color.green);

                    DownloadPathUrl downloadPathUrl =  new DownloadPathUrl(rxRoutePathObserver, color, preferenceHelperDataSource,
                            new RotateKey() {
                                @Override
                                public void rotateKey() {
                                    DownloadPathUrl downloadPathUrl =  new DownloadPathUrl(rxRoutePathObserver, color, preferenceHelperDataSource,this);
                                    String pickupURL = GetGoogleDirectionsUrl.getDirectionsUrl(latLngOrigin,latLngPickUp,preferenceHelperDataSource.getGoogleServerKey());
                                    downloadPathUrl.execute(pickupURL);
                                }
                            });
                    String pickupURL = GetGoogleDirectionsUrl.getDirectionsUrl(latLngOrigin,latLngPickUp,preferenceHelperDataSource.getGoogleServerKey());
                    downloadPathUrl.execute(pickupURL);

                }
                break;

            case AppConstants.BookingStatus.Arrived:
                if(time.matches("0")){
                    LocationUpdateService.getLocationUpdateService().setCumulativeDistance(0.0, 0.0, 0.0);
                    preferenceHelperDataSource.setDistanceCalculated("0.0");
                    preferenceHelperDataSource.setDistanceCalculatedShow("0.0");
                }
                startTimer(time);
                latLng = Arrays.asList( bookingAssignedDataRideAppts.getPickupLatLong().split(","));
                navigationDropLat = Double.parseDouble(latLng.get(0));
                navigationDropLng = Double.parseDouble(latLng.get(1));
                icon = BitmapDescriptorFactory.fromResource(R.drawable.flag_pickup);
                iconSetLatLng = new LatLng(navigationDropLat,navigationDropLng);
                if(icon != null)
                    rideBookingView.setFlagPickupDrop(icon,iconSetLatLng);
                iconSetLatLng = new LatLng(navigationDropLat,navigationDropLng);
                rideBookingView.setArrivedText();

                //if is towtray booking
                if(bookingAssignedDataRideAppts.isTowTruckBooking())
                    rideBookingView.setArrivedTextTowtray();
                break;

            case AppConstants.BookingStatus.started:

                rideBookingView.setStartTripText();

                //if is towtray booking
                if(bookingAssignedDataRideAppts.isTowTruckBooking()) {
                    rideBookingView.setStartTripTextTowtray();
                    switch (bookingAssignedDataRideAppts.getTowTruckBookingService()){
                        case AppConstants.TowTrayService.Fixed:
                            if(bookingAssignedDataRideAppts.isTowingVehicle())
                                rideBookingView.showTowing();
                            break;

                        default:
                            if(!runTimer) {
                                runTimer = true;
                                startTimer(time);
                            }else
                                timeinsec = Integer.parseInt(time);

                            rideBookingView.hideTowing();

                            break;
                    }
                }else {

                    if(!runTimer) {
                        runTimer = true;
                        startTimer(time);
                    }else
                        timeinsec = Integer.parseInt(time);
                }

                latLng = Arrays.asList( bookingAssignedDataRideAppts.getDropLatLong().split(","));
                navigationDropLat = Double.parseDouble(latLng.get(0));
                navigationDropLng = Double.parseDouble(latLng.get(1));
                icon = BitmapDescriptorFactory.fromResource(R.drawable.flag_drop);
                if(navigationDropLng==0 && navigationDropLat==0 && bookingAssignedDataRideAppts.getDropAddress().matches("")){
                    rideBookingView.dropLocDisable();
                }else {
                    iconSetLatLng = new LatLng(navigationDropLat,navigationDropLng);
                    if(icon != null)
                        rideBookingView.setFlagPickupDrop(icon,iconSetLatLng);
                }

                if(navigationDropLat!=0 && navigationDropLng!=0){
                    latLngOrigin = new LatLng(preferenceHelperDataSource.getCurrLatitude(),preferenceHelperDataSource.getCurrLongitude());
                    latLngPickUp = new LatLng(navigationDropLat,navigationDropLng);
                    int color = ContextCompat.getColor(context,R.color.red);

                    DownloadPathUrl downloadPathUrl =  new DownloadPathUrl(rxRoutePathObserver, color,
                            preferenceHelperDataSource, new RotateKey() {
                        @Override
                        public void rotateKey() {
                            DownloadPathUrl downloadPathUrl =  new DownloadPathUrl(rxRoutePathObserver, color, preferenceHelperDataSource,this);
                            String pickupURL = GetGoogleDirectionsUrl.getDirectionsUrl(latLngOrigin,latLngPickUp,preferenceHelperDataSource.getGoogleServerKey());
                            downloadPathUrl.execute(pickupURL);
                        }
                    });
                    String pickupURL = GetGoogleDirectionsUrl.getDirectionsUrl(latLngOrigin,latLngPickUp,preferenceHelperDataSource.getGoogleServerKey());
                    downloadPathUrl.execute(pickupURL);
                }

                break;

            case AppConstants.BookingStatus.Completed:
                rideBookingView.startInvoice(bookingId);
                break;
        }

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
                    rideBookingView.setCarMove(new LatLng(lat,lng),bearing);
                }

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 20);
                }

            }
        });

        mPreviousLoc=mCurrentLoc;
        rideBookingView.moveGoogleMapToLocation(location.getLatitude(),location.getLongitude());

    }




    /**********************************************************************************************/

    /**
     * <h1>getTowTruckServices</h1>
     * <p>api call for get the services for Selection</p>
     */

    private void getTowTruckServices() {

        rideBookingView.showProgress();
        Observable<Response<ResponseBody>> getBookingsAssigned = networkService.getTowTruckServices
                (preferenceHelperDataSource.getLanguage(), preferenceHelperDataSource.getSessionToken());
        getBookingsAssigned.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            switch (value.code()) {
                                case RESPONSE_CODE_SUCCESS:

                                    String resp  = value.body().string();
                                    Utility.printLog("the service resp is : "+resp);
                                    TowTrayService towTrayService = gson.fromJson(resp, TowTrayService.class);
                                    rideBookingView.setServiceSelection(towTrayService);

                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferenceHelperDataSource.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferenceHelperDataSource.getMqttTopic();
                                    LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                    preferenceHelperDataSource.clearSharedPredf();
                                    preferenceHelperDataSource.setLanguageSettings(languagesList);
                                    rideBookingView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    Utility.BlueToast(context,DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(rideBookingView!=null && homeFrag)
                            rideBookingView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(rideBookingView!=null && homeFrag)
                            rideBookingView.hideProgress();
                    }
                });
    }


    @Override
    public void postTowTruckServicesAPI(String serviceID) {
        rideBookingView.showProgress();
        Observable<Response<ResponseBody>> postTowTruckServices = networkService.postTowTruckServices
                (preferenceHelperDataSource.getSessionToken(),
                        preferenceHelperDataSource.getLanguage(),
                        preferenceHelperDataSource.getBookingId(),
                        serviceID);
        postTowTruckServices.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            switch (value.code()) {
                                case RESPONSE_CODE_SUCCESS:

                                    String resp  = value.body().string();
                                    Utility.printLog("postTowTruckServices : "+resp);
                                    updateBookingStatusAPI(AppConstants.BookingStatus.Completed);

                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferenceHelperDataSource.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferenceHelperDataSource.getMqttTopic();
                                    LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                    preferenceHelperDataSource.clearSharedPredf();
                                    preferenceHelperDataSource.setLanguageSettings(languagesList);
                                    rideBookingView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    JSONObject jsonObject=new JSONObject(value.errorBody().string());
                                    Utility.BlueToast(context, jsonObject.getString("message"));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(rideBookingView!=null)
                            rideBookingView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        /*if(rideBookingView!=null)
                            rideBookingView.hideProgress();*/
                    }
                });
    }

    @Override
    public void startTowingApi() {
        rideBookingView.showProgress();
        Observable<Response<ResponseBody>> postUpdateTowing = networkService.postUpdateTowing
                (preferenceHelperDataSource.getSessionToken(),
                        preferenceHelperDataSource.getLanguage(),
                        preferenceHelperDataSource.getBookingId());
        postUpdateTowing.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            switch (value.code()) {
                                case RESPONSE_CODE_SUCCESS:

                                    String resp  = value.body().string();
                                    Utility.printLog("postUpdateTowing : "+resp);
                                    bookingAssignedDataRideAppts.setTowTruckBookingService(AppConstants.TowTrayService.FixedAndTowing);

                                    //start timer
                                    setStatusChangeText(AppConstants.BookingStatus.started,"0",preferenceHelperDataSource.getBookingId());

                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferenceHelperDataSource.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferenceHelperDataSource.getMqttTopic();
                                    LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                    preferenceHelperDataSource.clearSharedPredf();
                                    preferenceHelperDataSource.setLanguageSettings(languagesList);
                                    rideBookingView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    JSONObject jsonObject=new JSONObject(value.errorBody().string());
                                    Utility.BlueToast(context, jsonObject.getString("message"));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(rideBookingView!=null )
                            rideBookingView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(rideBookingView!=null)
                            rideBookingView.hideProgress();
                    }
                });
    }


    @Override
    public void checkChatEnable() {
        if(preferenceHelperDataSource.getIsChatModuleEnable()){
            rideBookingView.enableChat();
        }
    }




    @Override
    public void callInitialize(String audio, String customerId)
    {
        Utility.printLog("the callinit input values: "+preferenceHelperDataSource.getAuthTokenCall()+"\n"+
                preferenceHelperDataSource.getLanguageSettings().getCode()+"\n"+
                audio,preferenceHelperDataSource.getBookingId()+"\n"+
                customerId);
        Observable<Response<ResponseBody>> request =
                callingService.initCall(preferenceHelperDataSource.getAuthTokenCall(),
                        preferenceHelperDataSource.getLanguageSettings().getCode(),
                        audio,preferenceHelperDataSource.getBookingId(),
                        customerId);
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        try {

                            String resp = value.body().string();
                            Utility.printLog("the call resp : "+resp);
                            JSONObject object = new JSONObject(resp);
                            JSONObject callData = object.getJSONObject("data");
                            // Log.d(TAG, "onNext: initCall Response"+response);
                            //PostCallResponseModel postCallResponse = gson.fromJson(response, PostCallResponseModel.class);
                            String callId= callData.getString("callId");
                            if(!TextUtils.isEmpty(callId)){
                                if(mqttManager.isMQTTConnected()){
                                    if(audio.equals("audio"))
                                        rideBookingView.launchCallsScreen(callId,"0");
                                    else
                                        rideBookingView.launchCallsScreen(callId,"1");
                                }
                                else{
                                    Log.d("", "onNext: can not subscribe to callId mqtt is not connected" );
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Utility.printLog(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Utility.printLog("onComplete"+"Done");
                    }
                });
    }

    /**
     * <h1>Set the pickup and Drop of location marker</h1>
     */
    @Override
    public String getCustomerId() {
        return bookingAssignedDataRideAppts.getCustomerId();
    }

    @Override
    public String getName() {
        return bookingAssignedDataRideAppts.getCustomerName();
    }

    @Override
    public String getProfilePic() {
        return "";
    }

    @Override
    public String getPhone() {
        return bookingAssignedDataRideAppts.getCustomerPhone();
    }
}
