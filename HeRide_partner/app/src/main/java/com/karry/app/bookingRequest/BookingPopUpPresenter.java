package com.karry.app.bookingRequest;

import android.app.Activity;
import android.os.CountDownTimer;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.heride.partner.R;
import com.karry.service.LocationUpdateService;
import com.karry.side_screens.home_fragment.HomeFragment;
import com.karry.network.NetworkService;
import com.karry.utility.AppConstants;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;
import com.karry.utility.TimezoneMapper;
import com.karry.utility.Utility;
import com.karry.utility.path_plot.DownloadPathUrl;
import com.karry.utility.path_plot.GetGoogleDirectionsUrl;
import com.karry.utility.path_plot.LatLongBounds;
import com.karry.utility.path_plot.RotateKey;
import com.karry.utility.path_plot.RxRoutePathObserver;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TimeZone;

import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karry.app.bookingRequest.BookingPopUpActivity.booking_popup;
import static com.karry.utility.VariableConstant.BOOKING_FLOW_OPEN;
import static com.karry.utility.VariableConstant.RESPONSE_CODE_SUCCESS;


public class BookingPopUpPresenter implements BookingPopUpContract.BookingPopUpPresenter {

    @Inject BookingPopUpContract.BookingPoUpView bookingPoUpView;
    @Inject Activity context;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject NetworkService dispatcherService;
    @Inject Gson gson;
    private CountDownTimer countDownTimer;
    private String bookingId;
    private BookingDataResponse bookingDataResponse;
    private RxRoutePathObserver rxRoutePathObserver;
    private Disposable pathPlotDisposable;
    private BitmapDescriptor pickupIcon,dropIcon;
    private LatLng latLngOrigin,latLngPickUp,latLngPickUpOrigin,latLngDrop;
    private boolean isDropofPlot = false;
    private LatLongBounds pickUpLatLongBounds,dropLatLongBounds;
    private ArrayList<String> serviceList;

    @Inject
    public BookingPopUpPresenter(RxRoutePathObserver rxRoutePathObserver) {

        this.rxRoutePathObserver = rxRoutePathObserver;
        subscribeRoutePoints();

    }

    @Override
    public void getData() {
        String timeZoneString =  TimezoneMapper.latLngToTimezoneString(preferenceHelperDataSource.getCurrLatitude(),
                preferenceHelperDataSource.getCurrLongitude());
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);


        String data = context.getIntent().getStringExtra("BOOKING_DATA");
        bookingDataResponse  = gson.fromJson(data,BookingDataResponse.class);
        String pickupTime = Utility.getDateWithTimeZone(bookingDataResponse.getBookingDate(),timeZone);
        String[] gender = context.getResources().getStringArray(R.array.gender);


        String paymentType = bookingDataResponse.getPaymentType();
        if(bookingDataResponse.getPaymentType().contains("+")){
            paymentType = paymentType.replace("+",",");
            paymentType = paymentType.replace(" ","");
        }
        bookingPoUpView.setTextData(bookingDataResponse,pickupTime,paymentType);


        if(bookingDataResponse.isTowTruckBooking()){
            bookingPoUpView.setTowTray();

            // 1- fixed service, 2 - towing service, 3 - fixed and Towing
            if(bookingDataResponse.getTowTruckBookingService()!=null &&
                    bookingDataResponse.getTowTruckBookingService().matches(AppConstants.TowTrayService.Fixed) ){
                bookingPoUpView.disableDropLocation();
            }

            serviceList = new ArrayList<>();
            if (bookingDataResponse.getTowTruckServices().size()>0) {

                for(int i =0 ;i<bookingDataResponse.getTowTruckServices().size();i++){
                    serviceList.add(bookingDataResponse.getTowTruckServices().get(i).getTitle());
                }


            }

        }



        startTimer(bookingDataResponse.getDriverAcceptTime());
        bookingId = bookingDataResponse.getBookingId();

        //checking the boooking type is corporate, if true Change UI ti Corporate booking type
        if(bookingDataResponse.isCorporateBooking())
            bookingPoUpView.enableCorporateBook();
        else
            bookingPoUpView.disableCorporateBook();


        //for checking  the Booking is in Rental, if it is Rental showing the rental UI
        if(bookingDataResponse.isRental()){
            bookingPoUpView.enableRentalBooking();
        }

        //for checking HotelBooking and make changes
        if(bookingDataResponse.isPartnerBooking()){
            bookingPoUpView.enableHotelBooking();
        }

        if(bookingDataResponse.getPickupLat()!=null && bookingDataResponse.getPickupLong()!=null
                && bookingDataResponse.getPickupLong()!=0 &&bookingDataResponse.getPickupLat()!=0){
            latLngOrigin = new LatLng(preferenceHelperDataSource.getCurrLatitude(),preferenceHelperDataSource.getCurrLongitude());
            latLngPickUp = new LatLng(bookingDataResponse.getPickupLat(),bookingDataResponse.getPickupLong());
            int color = ContextCompat.getColor(context,R.color.green);

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
            Utility.printLog("the polyline url issss: "+pickupURL);
            pickupIcon = BitmapDescriptorFactory.fromResource(R.drawable.flag_pickup);

        }
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
                bookingPoUpView.googlePathPlot(latLongBounds);
                String distaceTime;

                if(latLngPickUp!=null) {
                    if(pickUpLatLongBounds==null)
                        pickUpLatLongBounds = latLongBounds;

                    distaceTime  = latLongBounds.getEstimatedDistanceTime().getDistance().concat(", ").concat(latLongBounds.getEstimatedDistanceTime().getDuration());
                    bookingPoUpView.setFlagPickupDrop(pickupIcon, latLngPickUp, distaceTime);
                    latLngPickUp=null;
                }


                if(latLngDrop!=null) {
                    if(dropLatLongBounds==null)
                        dropLatLongBounds = latLongBounds;

                    //distaceTime  = latLongBounds.getEstimatedDistanceTime().getDistance().concat(", ").concat(latLongBounds.getEstimatedDistanceTime().getDuration());
                    bookingPoUpView.setFlagDrop(dropIcon, latLngDrop/*, distaceTime*/);
                }


                if( !isDropofPlot &&bookingDataResponse.getDropLat()!=null && bookingDataResponse.getDropLong()!=null
                        && bookingDataResponse.getDropLat()!=0 &&bookingDataResponse.getDropLong()!=0){
                    isDropofPlot = true;
                    latLngPickUpOrigin = new LatLng(bookingDataResponse.getPickupLat(),bookingDataResponse.getPickupLong());
                    latLngDrop = new LatLng(bookingDataResponse.getDropLat(),bookingDataResponse.getDropLong());
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
                    String pickupURL = GetGoogleDirectionsUrl.getDirectionsUrl(latLngPickUpOrigin,
                            latLngDrop,preferenceHelperDataSource.getGoogleServerKey());
                    downloadPathUrl.execute(pickupURL);
                    Utility.printLog("the polyline url issss: "+pickupURL);
                    dropIcon = BitmapDescriptorFactory.fromResource(R.drawable.flag_drop);
                }
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
    public void reboundMap() {
        /*if(pickUpLatLongBounds!=null)
            bookingPoUpView.googlePathPlot(pickUpLatLongBounds);*/

        if(dropLatLongBounds!=null)
            bookingPoUpView.googlePathPlot(dropLatLongBounds);
    }

    @Override
    public void setServiceListDialog(Activity mActivity, AppTypeFace appTypeFace) {
        DialogHelper.showServices(mActivity,appTypeFace,serviceList);
    }

    @Override
    public void updateApptRequest(String status) {
        respondToRequest(status);
    }


    @Override
    public void findCurrentLocation() {
        if(!preferenceHelperDataSource.getCurrLatitude().equals("0.0") && !preferenceHelperDataSource.getCurrLongitude().equals("0.0") ) {
            double lat = preferenceHelperDataSource.getCurrLatitude();
            double lang = preferenceHelperDataSource.getCurrLongitude();
            bookingPoUpView.moveGoogleMapToLocation(lat,lang,preferenceHelperDataSource.getVehiclesDetail().getVehicleMapIcon());
        }
    }

    /**
     * <h1>startTimer</h1>
     * <p>count time timer for the popup show</p>
     * @param countTime : cout time from the response
     */
    private void startTimer(String countTime)
    {
        bookingPoUpView.startMusicPlayer();

        final long finalTime = Long.parseLong(countTime);
        countDownTimer = new CountDownTimer(finalTime * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);

                int res = (int) (((float) seconds / finalTime) * 100);
                if(seconds<=99){
                    bookingPoUpView.onTimerChanged(res,String.format("%02d", seconds));
                }else {
                    bookingPoUpView.onTimerChanged(res,String.format("%03d", seconds));
                }
            }
            public void onFinish() {
                bookingPoUpView.onTimerChanged(0,"00");
                cancelCoutdownTimer();
            }
        }.start();

    }


    /**
     * <h1>respondToRequest</h1>
     * <p>api call for accept and reject the booking</p>
     * @param status : accept/reject
     */
    private void respondToRequest(String status){
        if(bookingPoUpView!=null){
            bookingPoUpView.showProgressbar();
        }
        Observable<Response<ResponseBody>> respondToRequest=dispatcherService.respondToRequest(
                preferenceHelperDataSource.getSessionToken(),
                preferenceHelperDataSource.getLanguage(),
                bookingId,
                Integer.parseInt(status),
                preferenceHelperDataSource.getCurrLatitude(),
                preferenceHelperDataSource.getCurrLongitude());
        respondToRequest.observeOn(AndroidSchedulers.mainThread())
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

                                    LocationUpdateService.getLocationUpdateService().setCumulativeDistance( 0.0, 0.0, 0.0);
                                    preferenceHelperDataSource.setDistanceCalculated("0.0");
                                    preferenceHelperDataSource.setDistanceCalculatedShow("0.0");


                                    jsonObject = new JSONObject(value.body().string());
                                    Utility.printLog("the respondToRequest success response : "+jsonObject.toString());
                                    BOOKING_FLOW_OPEN = true;
                                    if(!HomeFragment.homeFrag)
                                        bookingPoUpView.onSuccess();
                                    break;

                                default:
                                    jsonObject = new JSONObject(value.errorBody().string());
                                    bookingPoUpView.onSuccess();
                                    break;
                            }
                            cancelCoutdownTimer();
                            Utility.printLog("respondToRequest : "+jsonObject.toString());

                        }
                        catch (Exception e)
                        {
                            Utility.printLog("respondToRequest : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(bookingPoUpView!=null && booking_popup){
                            bookingPoUpView.dismissProgressbar();
                        }
                        cancelCoutdownTimer();
                    }

                    @Override
                    public void onComplete() {
                        if(bookingPoUpView!=null && booking_popup){
                            bookingPoUpView.dismissProgressbar();
                        }
                    }
                });
    }

    /**
     * <h1>cancelCoutdownTimer</h1>
     * <p>for close the timer</p>
     */
    private void cancelCoutdownTimer(){
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        bookingPoUpView.onFinish();
    }

}
