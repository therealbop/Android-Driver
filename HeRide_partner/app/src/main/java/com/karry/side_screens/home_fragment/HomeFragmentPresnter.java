package com.karry.side_screens.home_fragment;

import static com.karry.side_screens.home_fragment.HomeFragment.homeFrag;
import static com.karry.utility.VariableConstant.RESPONSE_CODE_SUCCESS;
import static com.karry.utility.VariableConstant.RESPONSE_UNAUTHORIZED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.location.Address;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import butterknife.BindColor;
import com.karry.app.bookingRequest.BookingPopUpActivity;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.authentication.login.model.VehiclesDetails;
import com.karry.bookingFlow.model.RideBookingCancel;
import com.karry.bookingFlow.model.RideBookingDropUpdate;
import com.karry.dagger.ActivityScoped;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.manager.booking.RxDriverCancelledObserver;
import com.karry.manager.booking.RxDriverQueuePosition;
import com.karry.manager.booking.RxDropLocationUpdateObserver;
import com.karry.manager.location.LocationManagerCallback;
import com.karry.manager.location.RxAddressObserver;
import com.karry.manager.location.RxLocationObserver;
import com.karry.manager.mqtt_manager.MQTTManager;
import com.karry.network.NetworkService;
import com.heride.partner.R;
import com.karry.pojo.AreaZone.AreaZone;
import com.karry.pojo.QueuePosition;
import com.karry.pojo.bookingAssigned.BookingAssignedDataRideAppts;
import com.karry.pojo.bookingAssigned.BookingAssignedResponse;
import com.karry.service.LocationUpdateService;
import com.karry.utility.AppConstants;
import com.karry.utility.DataParser;
import com.karry.utility.Scaler;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

@ActivityScoped
public class HomeFragmentPresnter implements HomeFragmentContract.HomeFragmentPresenter,
        LocationManagerCallback.CallBacks
{
    private static final String TAG = HomeFragmentPresnter.class.getSimpleName();
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferencesHelper;
    @Inject RxAddressObserver rxAddressObserver;
    @Inject Context context;
    @Inject CompositeDisposable compositeDisposable;
    @Inject MQTTManager mqttManager;

    @BindColor(R.color.zoneBorder) int zoneBorder;
    private double prevLat =  0.0, prevLng =  0.0;

    @Nullable private HomeFragmentContract.HomeFragmentView homeFragmentView;

    private RxLocationObserver rxLocationObserver;
    private Location mCurrentLoc,mPreviousLoc;
    private boolean locationzero = false;
    private boolean areaZone = false;
    private BookingAssignedDataRideAppts rideAppts;
    private VehiclesDetails vehiclesDetail;
    private ArrayList<List<LatLng>> Polygon = new ArrayList<>();
    private RxDriverCancelledObserver rxDriverCancelledObserver;
    private RxDropLocationUpdateObserver rxDropLocationUpdateObserver;
    private RxDriverQueuePosition rxDriverQueuePosition;


    @Inject HomeFragmentPresnter(RxLocationObserver rxLocationObserver,
                                 RxDriverCancelledObserver rxDriverCancelledObserver,
                                 RxDropLocationUpdateObserver rxDropLocationUpdateObserver,
                                 RxDriverQueuePosition rxDriverQueuePosition)
    {
        this.rxLocationObserver = rxLocationObserver;
        this.rxDriverCancelledObserver = rxDriverCancelledObserver;
        this.rxDropLocationUpdateObserver = rxDropLocationUpdateObserver;
        this.rxDriverQueuePosition = rxDriverQueuePosition;
        subscribeDriverCancelDetails();
        subscribeDropLocation();
        subscribeDriverQueue();
        subscribeLocationChange(null);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getCurrentLocation()
    {
        //locationProvider.startLocation(this);
    }

    @Override
    public void disposeObservables() {
        //locationProvider.stopLocationUpdates();
    }

    @Override
    public void handleResultFromIntents(String latitude, String longitude, String address) {
        Utility.printLog("the selected Address iss: "+address);
    }

    /**
     * <h2>subscribeLocationChange</h2>
     * This method is used to subscribe for the location change
     */
    private void subscribeLocationChange(BookingAssignedResponse bookingAssignedResponse)
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
                        preferencesHelper.setCurrLatitude(String.valueOf(location.getLatitude()));
                        preferencesHelper.setCurrLongitude(String.valueOf(location.getLongitude()));
                        if (locationzero) getBookingDistance(bookingAssignedResponse);
                        if (areaZone) getAreaZone();
                        findCurrentLocation();
                        try {
                            homeFragmentView.locationUpdated(location);
                        } catch (Exception e) {
                            Utility.printLog(
                                "Location Exception is  : " + Arrays.toString(e.getStackTrace()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
    }

    @Override
    public void addCarMarker() {
        double[] size = Scaler.getScalingFactor(context);
        double height = (65) * size[0];
        double width = (65) * size[1];
        LatLng latLng = new LatLng(preferencesHelper.getCurrLatitude(),
                preferencesHelper.getCurrLongitude());
        assert homeFragmentView != null;
        homeFragmentView.setCarMarker(latLng, width, height, preferencesHelper.getVehiclesDetail().getVehicleMapIcon());
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
                Utility.printLog(" cancel driver details observed :: "+rideBookingCancel.getStatus());
                /*VariableConstant.CANCEL_RIDE_REASON  = rideBookingCancel.getMsg();*/
                VariableConstant.CANCEL_RIDE = true;
                homeFragmentView.onResume();
            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(" cancel driver details onError :: "+e);
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
                homeFragmentView.onResume();
                homeFragmentView.showDialog(context.getResources().getString(R.string.dropUpdateMsg));

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


    /**
     * <h2>subscribeDropLocation</h2>
     * This method is used to subscribe drop location published
     */
    private void subscribeDriverQueue()
    {
        Observer<QueuePosition> observer = new Observer<QueuePosition>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {

            }

            @Override
            public void onNext(QueuePosition queuePosition)
            {
                if(homeFrag) {
                    Utility.printLog("the queue position : "+queuePosition.getQueueLength());
                    if (queuePosition.getQueuePosition() > 0) {
                        String position = " ".concat(String.valueOf(queuePosition.getQueuePosition()).concat("/").concat(String.valueOf(queuePosition.getQueueLength())));
                        homeFragmentView.setQueuePosition(position);
                    } else
                        homeFragmentView.hideQueuePosition();
                }

            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(" queue onError  "+e);
            }

            @Override
            public void onComplete()
            {}
        };
        rxDriverQueuePosition.subscribeOn(Schedulers.io());
        rxDriverQueuePosition.observeOn(AndroidSchedulers.mainThread());
        rxDriverQueuePosition.subscribe(observer);
    }


    @Override
    public void clearComposite() {
        compositeDisposable.clear();
    }

    @Override
    public void getRideBookingData() {

        if(rideAppts!=null) {
            Gson gson = new Gson();
            String rideBookingData = gson.toJson(rideAppts);
            Utility.printLog("the appoinment count is : " + rideBookingData);
            homeFragmentView.startRideBooking(rideBookingData);
        }
    }

    @Override
    public void getAreaZone() {
        homeFragmentView.showProgress();

        if(preferencesHelper.getCurrLatitude()!=0.00 && preferencesHelper.getCurrLatitude()!=0.00){
            areaZone = false;
            Observable<Response<ResponseBody>> getAreaZone = networkService.getAreaZone(
                    preferencesHelper.getSessionToken(),
                    preferencesHelper.getLanguage(),
                    String.valueOf(preferencesHelper.getCurrLatitude()),
                    String.valueOf(preferencesHelper.getCurrLongitude()));

            getAreaZone.subscribeOn(Schedulers.newThread())
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
                                        String resp=value.body().string();
                                        Utility.printLog("getAreaZone success response is  : \n" +resp);
                                        Gson gson = new Gson();
                                        AreaZone areaZone = gson.fromJson(resp,AreaZone.class);
                                        Utility.printLog("getAreaZone success response is  : \n" +areaZone.getData().getAreaZones().size());

                                        if(!Polygon.isEmpty())
                                        {
                                            Polygon.clear();
                                        }
                                        for(int i=0;i<areaZone.getData().getAreaZones().size();i++)
                                        {
                                            List<LatLng> polygonPath  = new ArrayList<>();
                                            Double lat_total=0.0;
                                            Double lng_total=0.0;

                                            for(int j=0;j<areaZone.getData().getAreaZones().get(i).getPaths().size();j++)
                                            {
                                                polygonPath.add(new LatLng(Double.parseDouble(areaZone.getData().getAreaZones().get(i).getPaths().get(j).getLat()),
                                                        Double.parseDouble(areaZone.getData().getAreaZones().get(i).getPaths().get(j).getLng())));

                                                lat_total+=Double.parseDouble(areaZone.getData().getAreaZones().get(i).getPaths().get(j).getLat());
                                                lng_total+=Double.parseDouble(areaZone.getData().getAreaZones().get(i).getPaths().get(j).getLng());

                                            }
                                            Polygon.add(polygonPath);
                                            countPolygonPoints(Polygon.get(i),areaZone.getData().getAreaZones().get(i).getSurgePrice());

                                            Double lat_avg = lat_total/areaZone.getData().getAreaZones().get(i).getPaths().size();
                                            Double lnt_avg = lng_total/areaZone.getData().getAreaZones().get(i).getPaths().size();
                                            if(!areaZone.getData().getAreaZones().get(i).getSurgePrice().matches("1"))
                                                homeFragmentView.addSergeInMap(new LatLng(lat_avg,lnt_avg),areaZone.getData().getAreaZones().get(i).getSurgePrice().concat("x"));
                                        }
                                        break;

                                    case RESPONSE_UNAUTHORIZED:
                                        VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                        VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                        LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                        preferencesHelper.clearSharedPredf();
                                        preferencesHelper.setLanguageSettings(languagesList);
                                        homeFragmentView.goToLogin(DataParser.fetchErrorMessage(value));
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
                            if(homeFragmentView!=null && homeFrag)
                                homeFragmentView.hideProgress();
                        }

                        @Override
                        public void onComplete() {
                            if(homeFragmentView!=null && homeFrag)
                                homeFragmentView.hideProgress();
                        }
                    });
        }else {
            areaZone = true;
            subscribeLocationChange(null);
        }
    }

    /**
     * <h1>countPolygonPoints</h1>
     * @param arrayPoints :latlng array for polygon
     */
    private void countPolygonPoints(List<LatLng> arrayPoints,String serge)
    {

        Utility.printLog("the zone latlng points are : "+arrayPoints);
        if (arrayPoints.size() >= 3)
        {
            PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.addAll(arrayPoints);
            polygonOptions.strokeColor(context.getResources().getColor(R.color.zoneBorder));
            polygonOptions.strokeWidth(5);

            float sergeValue =Float.parseFloat(serge);

            if(sergeValue==1)
                polygonOptions.fillColor(context.getResources().getColor(R.color.transparent));
            else if(sergeValue>1 && sergeValue<=1.5)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_35));
            else if(sergeValue>1.5 && sergeValue<=2)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_40));
            else if(sergeValue>2 && sergeValue<=2.5)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_45));
            else if(sergeValue>2.5 && sergeValue<=3)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_50));
            else if(sergeValue>3 && sergeValue<=3.5)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_55));
            else if(sergeValue>3.5 && sergeValue<=4)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_60));
            else if(sergeValue>4 && sergeValue<=4.5)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_65));
            else if(sergeValue>4.5 && sergeValue<=5)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_70));
            else if(sergeValue>5)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_75));


            homeFragmentView.drawAreaZones(polygonOptions);
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

        Utility.printLog("the bearing is startPoint : "+startPoint+"\t endpoint : "+endpoint);

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

                try {

                    LatLng  latLng = new LatLng(lat,lng);

                    if(!startPoint.equals(endpoint)) {
                        VariableConstant.BEARING = bearing;
                        assert homeFragmentView != null;
                        homeFragmentView.setCarMove(latLng, bearing);
                    }
                }catch (Exception e){
                    Utility.printLog(""+ Arrays.toString(e.getStackTrace()));
                }


                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 20);
                }

            }
        });

        mPreviousLoc=mCurrentLoc;
        assert homeFragmentView != null;
        homeFragmentView.moveGoogleMapToLocation(location.getLatitude(),location.getLongitude());

    }

    @Override
    public void getServiceView() {
        vehiclesDetail  = preferencesHelper.getVehiclesDetail();



        if(vehiclesDetail!=null && vehiclesDetail.getServices()!=null && vehiclesDetail.getServices().size()>0)



            switch (vehiclesDetail.getServices().size()){
                case 1:
                    switch (vehiclesDetail.getServices().get(0).getServiceType()){

                        case "1":
                            break;

                        case "2":
                            if(vehiclesDetail.getServices().get(0).isMeterBookingEnable())
                                homeFragmentView.setMeterBooking();
                            break;
                    }
                    break;

                case 2:
                    homeFragmentView.setShipmentRideBookingView();
                    break;
            }
        else {
            VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
            VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
            LanguagesList languagesList = preferencesHelper.getLanguageSettings();
            preferencesHelper.clearSharedPredf();
            preferencesHelper.setLanguageSettings(languagesList);
            homeFragmentView.goToLogin(context.getResources().getString(R.string.services_invalid));
        }



    }

    @Override
    public void checkMeterBooking(String masterStatus) {
        int serviceCount = 0;

        do {
            if(vehiclesDetail.getServices().get(serviceCount).getServiceType().matches("2") &&
                    vehiclesDetail.getServices().get(serviceCount).isMeterBookingEnable() && !masterStatus.matches(AppConstants.BookingStatus.busy)){
                homeFragmentView.setMeterBooking();
            }
            serviceCount++;

        }while(serviceCount < vehiclesDetail.getServices().size());
    }

    @Override
    public void attachView(Object view) {
        homeFragmentView  = (HomeFragmentContract.HomeFragmentView) view;
    }

    @Override
    public void findCurrentLocation() {
        if(preferencesHelper.getCurrLongitude()!=0.00 && preferencesHelper.getCurrLatitude()!=0.00) {
            try {
                homeFragmentView.moveGoogleMapToLocation(preferencesHelper.getCurrLatitude(),preferencesHelper.getCurrLongitude());
            }catch (Exception e){

            }
        }
    }

    @Override
    public void checkCurrentLocation() {
        LatLng latLng = new LatLng(preferencesHelper.getCurrLatitude(),preferencesHelper.getCurrLongitude());
        homeFragmentView.setCurrentLocation(latLng);
    }

    @Override
    public void detachView() {
        homeFragmentView = null;
    }


    @Override
    public void setDriverStatus(int status)
    {
        homeFragmentView.showProgress();
        Utility.printLog("Driver Status Update status : "+status);
        Observable<Response<ResponseBody>> request = networkService.setDriverStatus
                (preferencesHelper.getSessionToken(),
                        preferencesHelper.getLanguage(),
                        status,
                        String.valueOf(preferencesHelper.getCurrLatitude()),
                        String.valueOf(preferencesHelper.getCurrLongitude()));
        request.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        Utility.printLog("Driver Status Update status : "+value.code());

                        switch (value.code()) {
                            case RESPONSE_CODE_SUCCESS:
                                try {
                                    Utility.printLog("MyResponseFromStat"+ value.body().string());
                                    homeFragmentView.changeDriverStatus(status);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case RESPONSE_UNAUTHORIZED:
                                VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                preferencesHelper.clearSharedPredf();
                                preferencesHelper.setLanguageSettings(languagesList);
                                homeFragmentView.goToLogin(DataParser.fetchErrorMessage(value));
                                break;

                            default:
                                switch (String.valueOf(status)){
                                    case AppConstants.BookingStatus.Online:
                                        homeFragmentView.changeDriverStatus(Integer.parseInt(AppConstants.BookingStatus.Offline));
                                        break;
                                    case AppConstants.BookingStatus.Offline:
                                        homeFragmentView.changeDriverStatus(Integer.parseInt(AppConstants.BookingStatus.Online));

                                        break;
                                }
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(homeFragmentView!=null && homeFrag)
                            homeFragmentView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(homeFragmentView!=null && homeFrag)
                            homeFragmentView.hideProgress();
                    }
                });
    }

    /**********************************************************************************************/
    @Override
    public void getBookingsAssigned() {

        homeFragmentView.showProgress();
        Observable<Response<ResponseBody>> getBookingsAssigned = networkService.getBookingsAssigned
                (preferencesHelper.getSessionToken(), preferencesHelper.getLanguage());
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
                                    String resp=value.body().string();
                                    Utility.printLog("getBookingsAssigned success response is  :1 \n" +resp);
                                    bookingAssignedResponseHandler(resp);
                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    homeFragmentView.goToLogin(DataParser.fetchErrorMessage(value));
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
                        if(homeFragmentView!=null && homeFrag)
                            homeFragmentView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(homeFragmentView!=null && homeFrag)
                            homeFragmentView.hideProgress();
                    }
                });
    }


    /**
     * <h1>bookingAssignedResponseHandler</h1>
     * <p>the response handling of BookingAssignedAPI</p>
     * @param response : api response
     */
    private void bookingAssignedResponseHandler(String response){

        Gson gson=new Gson();
        BookingAssignedResponse bookingAssignedResponse  = gson.fromJson(response,BookingAssignedResponse.class);
        homeFragmentView.getBookingsAssignedSuccess(bookingAssignedResponse);
        preferencesHelper.setDispatcherTopic(bookingAssignedResponse.getData().getDispatcherTopic());
        preferencesHelper.setBirdViewTopic(bookingAssignedResponse.getData().getGodsviewTopic());
        VariableConstant.TIMEDIFFERENCE = (System.currentTimeMillis()/1000) - Long.parseLong(bookingAssignedResponse.getData().getGmtTimestamp());
        Utility.printLog("the time differnce is : "+VariableConstant.TIMEDIFFERENCE);

        String cashBalance = "0", softLimit = "0",hardLimit = "0";
        String currency_symbol = bookingAssignedResponse.getData().getCurrencySymbol();
        if(bookingAssignedResponse.getData().getCurrencyAbbr().matches("1")){
            preferencesHelper.setWalletBal("( ".concat(currency_symbol.concat(" ").concat(bookingAssignedResponse.getData().getWalletBalance())).concat(" )"));
            cashBalance = currency_symbol.concat(" ").concat(bookingAssignedResponse.getData().getWalletBalance());
            softLimit = currency_symbol.concat(" ").concat(bookingAssignedResponse.getData().getWalletSoftLimit());
            hardLimit = currency_symbol.concat(" ").concat(bookingAssignedResponse.getData().getWalletHardLimit());
        }
        else {
            preferencesHelper.setWalletBal("( ".concat(bookingAssignedResponse.getData().getWalletBalance().concat(" ").concat(currency_symbol)).concat(" )"));
            cashBalance = bookingAssignedResponse.getData().getWalletBalance().concat(" ").concat(currency_symbol);
            softLimit = bookingAssignedResponse.getData().getWalletSoftLimit().concat(" ").concat(currency_symbol);
            hardLimit = bookingAssignedResponse.getData().getWalletHardLimit().concat(" ").concat(currency_symbol);
        }
        homeFragmentView.setWalletData(cashBalance,softLimit,hardLimit);

        if(bookingAssignedResponse.getData().getRideAppts().size()>0 && bookingAssignedResponse.getData().getRideAppts().get(0).getStatus()!=null){
            preferencesHelper.setBookingStatus(bookingAssignedResponse.getData().getRideAppts().get(0).getStatus());
            rideAppts = bookingAssignedResponse.getData().getRideAppts().get(0);
        }

        if(bookingAssignedResponse.getData().getRideAppts().size()>0 &&
                bookingAssignedResponse.getData().getRideAppts().get(0).isTowTruckBooking()){
            preferencesHelper.setServiceStatus(bookingAssignedResponse.getData().getRideAppts().get(0).getTowTruckBookingService());

        }else
        {
            preferencesHelper.setServiceStatus("-1");
        }



        switch (bookingAssignedResponse.getData().getMasterStatus()){
            case AppConstants.BookingStatus.Online:
            case AppConstants.BookingStatus.InActive:
                homeFragmentView.driverOnline();
                break;
            case AppConstants.BookingStatus.Offline:
                homeFragmentView.drierOffline();
                homeFragmentView.hideQueuePosition();
                break;
            case AppConstants.BookingStatus.busy:
                homeFragmentView.driverBusy();
                break;

        }

      if(bookingAssignedResponse.getData().getRideAppts()!=null &&
          bookingAssignedResponse.getData().getRideAppts().size()!=0 ){

        getBookingDistance(bookingAssignedResponse);
      }
      else {

        setCumulativeDistance();
        preferencesHelper.setDistanceCalculated("0.0");
        preferencesHelper.setDistanceCalculatedShow("0.0");
        preferencesHelper.setBookingId("");
      }


      if(bookingAssignedResponse.getData().getRideAppts().size()==0  &&
                bookingAssignedResponse.getData().getRideApptsPendingReview().size()>0)
            homeFragmentView.setInvoice(bookingAssignedResponse.getData().getRideApptsPendingReview().get(0).getBookingId());

        if(Double.parseDouble(bookingAssignedResponse.getData().getWalletBalance())<=Double.parseDouble(bookingAssignedResponse.getData().getWalletSoftLimit()))
        {
            homeFragmentView.rechargeWallet(bookingAssignedResponse.getData().getWalletBalance(),
                    bookingAssignedResponse.getData().getWalletSoftLimit(),
                    bookingAssignedResponse.getData().getWalletHardLimit());
        }

        Utility.printLog("the meterbooking is : "+bookingAssignedResponse.getData().isMeterBookingEnable());
        if(bookingAssignedResponse.getData().isMeterBookingEnable())
            homeFragmentView.setMeterBooking();
        else
            homeFragmentView.hideMeterBooking();

        if(bookingAssignedResponse.getData().getRideAppts().size()>0) {
            homeFragmentView.hideMeterBooking();
            homeFragmentView.hideQueuePosition();
        }
    }

    private void setCumulativeDistance() {

        if (LocationUpdateService.getLocationUpdateService() != null) {
            LocationUpdateService.getLocationUpdateService().setCumulativeDistance(0.0, 0.0, 0.0);

            if (timerTask != null) {

                try {
                    timerTask.cancel();
                    timer.cancel();
                    timer.purge();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {

            if (timerTask == null) {

                timerTask =
                    new TimerTask() {
                        @Override
                        public void run() {

                            setCumulativeDistance();

                        }
                    };
            }
            if (timer == null) {

                timer = new Timer();
                timer.schedule(timerTask, 0, 200);

            }
        }
    }
    private TimerTask timerTask;
    private Timer timer;

    /**********************************************************************************************/
    private void getBookingDistance(BookingAssignedResponse bookingAssignedResponse){
        homeFragmentView.showProgress();

        if(preferencesHelper.getCurrLatitude()!=0.00 && preferencesHelper.getCurrLatitude()!=0.00){
            locationzero = false;
            Observable<Response<ResponseBody>> getBookingDistance = networkService.getBookingDistance
                    (preferencesHelper.getSessionToken(),
                            preferencesHelper.getLanguage(),
                            bookingAssignedResponse.getData().getRideAppts().get(0).getBookingId(),
                            String.valueOf(preferencesHelper.getCurrLatitude()),
                            String.valueOf(preferencesHelper.getCurrLongitude()));

            getBookingDistance.subscribeOn(Schedulers.newThread())
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
                                        String resp=value.body().string();
                                        Utility.printLog("getBookingDistance success response is  : \n" +resp);
                                        JSONObject jsonObject = new JSONObject(resp);
                                        resp  = jsonObject.get("data").toString();
                                        Utility.printLog("getBookingDistance success response is  : \n" +resp);
                                        jsonObject = new JSONObject(resp);
                                        resp = jsonObject.get("totalDistance").toString();
                                        Utility.printLog("getBookingDistance success response is  : \n" +resp);

                                        if(bookingAssignedResponse.getData().getRideAppts().get(0).getStatus().matches(AppConstants.BookingStatus.Completed)){
                                            LocationUpdateService.getLocationUpdateService().setCumulativeDistance(Double.parseDouble(resp), 0.0, 0.0);                                            Utility.printLog("Myservice meter distance Homee" + resp);
                                            preferencesHelper.setDistanceCalculated(resp);
                                        }


                                        Utility.printLog("getBookingDistance success response is  :: \n" +preferencesHelper.getDistanceCalculated());

                                        if(!preferencesHelper.getBookingId().matches(bookingAssignedResponse.getData().getRideAppts().get(0).getBookingId())){
                                            preferencesHelper.setBookingId(bookingAssignedResponse.getData().getRideAppts().get(0).getBookingId());
                                        }
                                        Utility.printLog("the appoinment count is : "+bookingAssignedResponse.getData().getRideAppts().size());
                                        preferencesHelper.setMileageMetricUnit(bookingAssignedResponse.getData().getRideAppts().get(0).getMileageMetricUnit());
                                        homeFragmentView.bookingEnabled(bookingAssignedResponse.getData().getRideAppts().get(0),
                                                bookingAssignedResponse.getData().getMasterStatus());
                                        break;

                                    case RESPONSE_UNAUTHORIZED:
                                        VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                        VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                        LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                        preferencesHelper.clearSharedPredf();
                                        preferencesHelper.setLanguageSettings(languagesList);
                                        homeFragmentView.goToLogin(DataParser.fetchErrorMessage(value));
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
                            if(homeFragmentView!=null && homeFrag)
                                homeFragmentView.hideProgress();
                        }

                        @Override
                        public void onComplete() {
                            if(homeFragmentView!=null && homeFrag)
                                homeFragmentView.hideProgress();
                        }
                    });
        }else {
            locationzero = true;
            subscribeLocationChange(bookingAssignedResponse);
        }


    }

    /**********************************************************************************************/
    private void subscribeAddressChange(String dropAddress, String dropLatLng, String dropPlaceID)
    {
        assert homeFragmentView != null;
        homeFragmentView.showProgress();
        Observer<Address> observer = new Observer<Address>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {

            }
            @Override
            public void onNext(Address pickupAddress)
            {
                Utility.printLog(" onNext address observed  "+pickupAddress.toString());
                bookingAssignedAPI(pickupAddress, dropAddress, dropLatLng, dropPlaceID);
                String full_address = DataParser.getStringAddress(pickupAddress);
                Utility.printLog( " onNext getAddressFromLocation full_address: " + full_address);

            }
            @Override
            public void onError(Throwable e)
            {
                homeFragmentView.hideProgress();
                /*bookingAssignedAPI(null);*/
                e.printStackTrace();
                Utility.printLog(" onError address observed  "+e);
            }
            @Override
            public void onComplete()
            {
                homeFragmentView.hideProgress();
            }
        };
        rxAddressObserver.subscribeOn(Schedulers.io());
        rxAddressObserver.observeOn(AndroidSchedulers.mainThread());
        rxAddressObserver.subscribe(observer);
    }

    /**********************************************************************************************/
    private void bookingAssignedAPI(Address PickUpAddress, String dropAddress, String dropLatLng, String dropPlaceID){

        Utility.printLog("lat lng is :  "+String.valueOf(preferencesHelper.getCurrLatitude())+","+
                String.valueOf(preferencesHelper.getCurrLongitude()));

        dropLatLng = dropLatLng.substring(10,dropLatLng.length()-1);
        String[] dropLatLong =  dropLatLng.split(",");

        Utility.printLog("the bundle values are : "+dropLatLong[0]);
        Utility.printLog("the bundle values are : "+dropLatLong[1]);

        String add;
        if(PickUpAddress!=null)
        {
            add = DataParser.getStringAddress(PickUpAddress);
        }
        else {
            add = "3embed soft private ltd.";
        }

        homeFragmentView.showProgress();

        Observable<Response<ResponseBody>> startMeterBooking = networkService.startMeterBooking
                (preferencesHelper.getSessionToken(),
                        preferencesHelper.getLanguage(),
                        Utility.date(),
                        null,
                        null,
                        add,
                        null,
                        null,
                        null,
                        null,
                        null,
                        String.valueOf(preferencesHelper.getCurrLongitude()),
                        String.valueOf(preferencesHelper.getCurrLatitude()),
                        dropPlaceID,
                        null,
                        dropAddress,
                        null,
                        null,
                        null,
                        null,
                        null,
                        dropLatLong[0],
                        dropLatLong[1]);
        startMeterBooking.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        Utility.printLog("getBookingsAssigned response is  : "+value);
                        try {
                            switch (value.code()) {
                                case RESPONSE_CODE_SUCCESS:
                                    String resp=value.body().string();
                                    Utility.printLog("getBookingsAssigned success response is  : \n"+resp);
                                    homeFragmentView.callBookingAssigned();
                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    homeFragmentView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    Utility.printLog("getBookingsAssigned error response is  : \n"+value.errorBody().string());
                                    break;
                            }



                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        if(homeFragmentView!=null && homeFrag)
                            homeFragmentView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(homeFragmentView!=null && homeFrag)
                            homeFragmentView.hideProgress();
                    }
                });

    }

    @Override
    public void onUpdateLocation(Location location) {
        Utility.printLog("the latlng is : "+location.getLatitude());
    }

    @Override
    public void locationMsg(String error) {

    }

    @Override
    public void onLocationServiceDisabled(Status status) {
        homeFragmentView.promptUserWithLocationAlert(status);
    }


    @Override
    public void setStatusChangeText(String status){

        switch (status){
            case AppConstants.BookingStatus.OnTheWay:
                homeFragmentView.setOnTheWayText();
                homeFragmentView.setAddress(rideAppts.getPickupAddress());
                break;

            case AppConstants.BookingStatus.Arrived:
                homeFragmentView.setArrivedText();
                homeFragmentView.setAddress(rideAppts.getPickupAddress());
                break;

            case AppConstants.BookingStatus.started:
                homeFragmentView.setStartTripText();
                homeFragmentView.setAddress(rideAppts.getDropAddress());
                if(rideAppts.getDropAddress().matches("")){
                    homeFragmentView.dropLocDisable();
                }
                break;
        }
    }

    @Override
    public void checkRideCancel() {
        /*if(VariableConstant.CANCEL_RIDE){
            VariableConstant.CANCEL_RIDE = false;
            homeFragmentView.showDialog(VariableConstant.CANCEL_RIDE_REASON);
            VariableConstant.CANCEL_RIDE_REASON="";
        }*/

        if(VariableConstant.CANCEL_RIDE){
            VariableConstant.CANCEL_RIDE = false;


          /*  if(VariableConstant.ADMIN_COLMPLETED){
                Utility.BlueToast(context, VariableConstant.CANCEL_RIDE_REASON);
                VariableConstant.ADMIN_COLMPLETED = false;
            }else {
                homeFragmentView.showDialog(VariableConstant.CANCEL_RIDE_REASON);
            }*/
            if(VariableConstant.ADMIN_COLMPLETED){
                homeFragmentView.showDialog(VariableConstant.CANCEL_RIDE_REASON);
                VariableConstant.ADMIN_COLMPLETED = false;
            }else {
                homeFragmentView.showDialog(VariableConstant.CANCEL_RIDE_REASON);
            }
        }
    }

    @Override
    public void checkDropUpdate() {
        if(VariableConstant.UPDATE_DROP){
            VariableConstant.UPDATE_DROP = false;
            homeFragmentView.showDialog(context.getResources().getString(R.string.dropUpdateMsg));
        }
    }

    @Override
    public void checkBookingPopUp() {
        /*mqttManager.unSubscribeToTopic(preferencesHelper.getMqttTopic());
        mqttManager.subscribeToTopic(preferencesHelper.getMqttTopic());*/

        if(BookingPopUpActivity.mediaPlayer!=null && BookingPopUpActivity.mediaPlayer.isPlaying())
            BookingPopUpActivity.mediaPlayer.stop();
    }
    /**
     *
     *
     * <h1>startMeterBookingAPI</h1>
     *
     * <p>start the meterBooking
     *
     */
    public void startMeterBookingAPI() {
        Address pickUpAddress = null;
        try {
            List<Address> addresses = new Utility().getPickupAddress(context,preferencesHelper);
            pickUpAddress = addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String add;
        if (pickUpAddress != null) {
            add = DataParser.getStringAddress(pickUpAddress);
        } else {
            add = "3embed soft private ltd.";
        }

        homeFragmentView.showProgress();
        Observable<Response<ResponseBody>> startMeterBooking =
            networkService.startMeterBooking(
                preferencesHelper.getSessionToken(),
                preferencesHelper.getLanguage(),
                Utility.date(),
                null,
                null,
                add,
                null,
                null,
                null,
                null,
                null,
                String.valueOf(preferencesHelper.getCurrLongitude()),
                String.valueOf(preferencesHelper.getCurrLatitude()),
                "",
                null,
                "",
                null,
                null,
                null,
                null,
                null,
                "",
                "");
        startMeterBooking
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        try {
                            switch (value.code()) {
                                case VariableConstant.RESPONSE_CODE_SUCCESS:
                                    String resp = value.body().string();
                                    Utility.printLog("startMeterBooking success response is  : \n" + resp);

                                    JSONObject jsonObject = new JSONObject(resp);
                                    Gson gson = new Gson();
                                    BookingAssignedDataRideAppts bookingAssignedDataRideAppts =
                                        gson.fromJson(
                                            jsonObject.getJSONObject("data").toString(),
                                            BookingAssignedDataRideAppts.class);
                                    homeFragmentView.meterBookingSuccess(bookingAssignedDataRideAppts);

                                    if (bookingAssignedDataRideAppts.getBookingStatus() != null) {
                                        preferencesHelper.setBookingStatus(
                                            bookingAssignedDataRideAppts.getBookingStatus());
                                    }

                                    if (!preferencesHelper
                                        .getBookingId()
                                        .matches(bookingAssignedDataRideAppts.getBookingId())) {

                                        LocationUpdateService.getLocationUpdateService().setCumulativeDistance( 0.0,0.0,0.0);
                                        preferencesHelper.setDistanceCalculated("0.0");
                                        preferencesHelper.setDistanceCalculatedShow("0.0");
                                        preferencesHelper.setBookingId(bookingAssignedDataRideAppts.getBookingId());
                                    }

                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    homeFragmentView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    Utility.BlueToast(context, DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        homeFragmentView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        homeFragmentView.hideProgress();
                    }
                });
    }
}
