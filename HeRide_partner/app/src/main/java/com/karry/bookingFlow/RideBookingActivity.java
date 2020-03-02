package com.karry.bookingFlow;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karry.app.cancelBooking.CancelReasonActivity;
import com.karry.app.mainActivity.MainActivity;
import com.karry.mqttChat.ChattingActivity;
import com.heride.partner.R;
import com.karry.pojo.bookingAssigned.CustVehicleDetails;
import com.karry.pojo.bookingAssigned.TowTrayService;
import com.karry.side_screens.home_fragment.invoice.InvoiceActivity;
import com.karry.network.NetworkErrorDialog;
import com.karry.pojo.bookingAssigned.BookingAssignedDataRideAppts;
import com.karry.service.LocationUpdateService;
import com.karry.telecall.utility.CallingApis;
import com.karry.utility.AppConstants;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;
import com.karry.utility.PicassoMarker;
import com.karry.utility.Slider;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.karry.utility.path_plot.LatLongBounds;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.karry.utility.LocationUtil.REQUEST_CHECK_SETTINGS;
import static com.karry.utility.VariableConstant.BOOKING_FLOW_OPEN;
import static com.karry.utility.VariableConstant.DATA;
import static com.karry.utility.VariableConstant.FORGROUND_LOCK;
import static com.karry.utility.VariableConstant.METERBOOKING_INVOICE_DATA;
import static com.karry.utility.VariableConstant.RIDE_BOOKING_DATA;
import static com.karry.utility.VariableConstant.TYPE;


/**
 * <h1>RideBookingActivity</h1>
 * <p>the Ride Booking Activity which handling the view of the Activity </p>
 */
public class RideBookingActivity extends DaggerAppCompatActivity implements
        RideBookingContract.RideBookingView, OnMapReadyCallback,EasyPermissions.PermissionCallbacks
{

    private GoogleMap googleMap;
    private BookingAssignedDataRideAppts bookingData;
    private PicassoMarker marker;
    private MediaPlayer notification;
    private String driver_name = "";
    private MarkerOptions markerOptions;
    private Marker flagMarker;
    public static boolean rideBookingActivity_opened = false;
    @BindString(R.string.ok) String ok;
    @BindString(R.string.message) String message;
    @BindString(R.string.dropUpdateMsg) String dropUpdateMsg;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @BindView(R.id.ll_rideBooking_detail) LinearLayout ll_rideBooking_detail;
    @BindView(R.id.tv_bid) TextView tv_bid;
    @BindView(R.id.tv_status) TextView tv_status;
    @BindView(R.id.tv_rider_name) TextView tv_rider_name;
    @BindView(R.id.tv_status_head) TextView tv_status_head;
    @BindView(R.id.tv_address) TextView tv_address;
    @BindView(R.id.iv_cancel)  ImageView iv_cancel;
    @BindView(R.id.iv_call) ImageView iv_call;
    @BindView(R.id.iv_someOne_call) ImageView iv_someOne_call;
    @BindView(R.id.iv_pickup_loc) ImageView iv_pickup_loc;

    @BindView(R.id.tv_booking_btnStatus) TextView tv_booking_btnStatus;
    @BindView(R.id.ll_navigation) LinearLayout ll_navigation;
    @BindView(R.id.ll_address) LinearLayout ll_address;
    @BindView(R.id.rl_calculateDisTime)  RelativeLayout rl_calculateDisTime;
    @BindView(R.id.ll_distance)  LinearLayout ll_distance;
    @BindView(R.id.ll_waitingTime)  LinearLayout ll_waitingTime;
    @BindView(R.id.ll_timer)  LinearLayout ll_timer;
    @BindView(R.id.tv_distance_value)  TextView tv_distance_value;
    @BindView(R.id.tv_distance_head)  TextView tv_distance_head;
    @BindView(R.id.tv_waitingTime_value)  TextView tv_waitingTime_value;
    @BindView(R.id.tv_waitingTime_head)  TextView tv_waitingTime_head;
    @BindView(R.id.tv_rideTimer_value)  TextView tv_rideTimer_value;
    @BindView(R.id.tv_rideTimer_head)  TextView tv_rideTimer_head;
    @BindView(R.id.rl_bookForSomeOne)  RelativeLayout rl_bookForSomeOne;
    @BindView(R.id.tv_bookForSomeOne)  TextView tv_bookForSomeOne;
    @BindView(R.id.tv_someOneName)  TextView tv_someOneName;
    @BindView(R.id.view_address_divider)  View view_address_divider;
    @BindView(R.id.iv_location_update) ImageView iv_location_update;
    @BindView(R.id.iv_chat) ImageView iv_chat;
    @BindView(R.id.rl_calculateArrive) RelativeLayout rl_calculateArrive;
    @BindView(R.id.tv_arriveDistance_value) TextView tv_arriveDistance_value;
    @BindView(R.id.tv_arriveDistance_head) TextView tv_arriveDistance_head;
    @BindView(R.id.rl_towing) RelativeLayout rl_towing;
    @BindView(R.id.iv_towing) ImageView iv_towing;

    @BindView(R.id.ll_cust_veh) LinearLayout ll_cust_veh;
    @BindView(R.id.tv_make) TextView tv_make;
    @BindView(R.id.tv_make_value) TextView tv_make_value;
    @BindView(R.id.tv_model) TextView tv_model;
    @BindView(R.id.tv_model_value) TextView tv_model_value;
    @BindView(R.id.tv_year) TextView tv_year;
    @BindView(R.id.tv_year_value) TextView tv_year_value;
    @BindView(R.id.tv_color) TextView tv_color;
    @BindView(R.id.tv_color_value) TextView tv_color_value;

    @BindView(R.id.slider_booking_myseek) Slider seekBar;

    @Inject RideBookingContract.RideBookingPresenter presenter;
    @Inject AppTypeFace appTypeFace;
    @Inject DialogHelper dialogHelper;
    @Inject NetworkErrorDialog networkErrorDialog;

    @BindString(R.string.bid) String bid;
    @BindString(R.string.rideBooking) String title;
    @BindString(R.string.job_booking) String towing_tittle;
    @BindString(R.string.pickup_location) String pickup_location;
    @BindString(R.string.drop_location) String drop_location;
    @BindString(R.string.ontheway) String ontheway;
    @BindString(R.string.arrivedToPick) String arrivedToPick;
    @BindString(R.string.startTrip) String startTrip;
    @BindString(R.string.startJob) String startJob;
    @BindString(R.string.completeJob) String completeJob;
    @BindString(R.string.completeTrip) String completeTrip;
    @BindString(R.string.towingStartmsg) String towingStartmsg;
    @BindString(R.string.location_permission_message) String location_permission_message;
    @BindDrawable(R.drawable.back_white_btn) Drawable back_white_btn;
    @BindDrawable(R.drawable.home_dropoff_icon) Drawable home_dropoff_icon;


    private static final String[] CALL_PERMISSIONS = {
            "android.permission.RECORD_AUDIO", "android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.INTERNET"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_flow);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        ButterKnife.bind(this);
        initilizeView();
        presenter.setActionBar();
        initializeMapFragment();
    }


    @Override
    public void setText(BookingAssignedDataRideAppts bookingData,String driver_name, String payment) {
        this.driver_name = driver_name;
        this.bookingData = bookingData;
        tv_bid.setText(bid.concat(bookingData.getBookingId()).concat(" (").concat(payment).concat(")"));
        tv_rider_name.setText(bookingData.getCustomerName());
    }

    @Override
    public void setBookigForSomeOne() {
        rl_bookForSomeOne.setVisibility(View.VISIBLE);
        tv_someOneName.setText(bookingData.getSomeOne().getName());
    }

    @Override
    public void setOnTheWayText() {
        seekBar.setProgress(0);
        tv_status_head.setText(pickup_location);
        tv_address.setText(bookingData.getPickupAddress());
        tv_booking_btnStatus.setText(arrivedToPick);
        tv_status.setText(ontheway);

        rl_calculateDisTime.setVisibility(View.GONE);
        rl_towing.setVisibility(View.GONE);
        ll_timer.setVisibility(View.GONE);
        ll_waitingTime.setVisibility(View.GONE);
        rl_calculateArrive.setVisibility(View.VISIBLE);
    }

    @Override
    public void setArrivedText() {
        seekBar.setProgress(0);
        tv_status_head.setText(pickup_location);
        tv_address.setText(bookingData.getPickupAddress());
        tv_booking_btnStatus.setText(startTrip);
        if(presenter.isTowtruck())
            tv_booking_btnStatus.setText(startJob);
        tv_status.setText(arrivedToPick);

        rl_calculateDisTime.setVisibility(View.VISIBLE);
        rl_towing.setVisibility(View.GONE);
        ll_distance.setVisibility(View.GONE);
        ll_timer.setVisibility(View.GONE);
        ll_waitingTime.setVisibility(View.VISIBLE);
        rl_calculateArrive.setVisibility(View.GONE);
    }

    @Override
    public void setArrivedTextTowtray() {
        tv_booking_btnStatus.setText(startJob);
    }

    @Override
    public void setStartTripText() {
        seekBar.setProgress(0);
        tv_status_head.setText(drop_location);
        tv_address.setText(bookingData.getDropAddress());
        tv_booking_btnStatus.setText(completeTrip);
        if(presenter.isTowtruck())
            tv_booking_btnStatus.setText(completeJob);
        tv_status.setText(startTrip);
        if(presenter.isTowtruck())
            tv_status.setText(startJob);
        iv_cancel.setVisibility(View.GONE);
        iv_pickup_loc.setImageDrawable(home_dropoff_icon);

        rl_calculateDisTime.setVisibility(View.VISIBLE);
        /*rl_towing.setVisibility(View.VISIBLE);*/
        ll_distance.setVisibility(View.VISIBLE);
        ll_timer.setVisibility(View.VISIBLE);
        ll_waitingTime.setVisibility(View.GONE);
        rl_calculateArrive.setVisibility(View.GONE);
    }

    @Override
    public void setStartTripTextTowtray() {
        tv_booking_btnStatus.setText(completeJob);
        tv_status.setText(startJob);
    }

    @Override
    public void setRunningTimer(String time) {
        tv_waitingTime_value.setText(time);
        tv_rideTimer_value.setText(time);
    }

    @Override
    public void startInvoice(String bid) {
        Intent meterBookingIntent=new Intent(this, InvoiceActivity.class);
        meterBookingIntent.putExtra(METERBOOKING_INVOICE_DATA,bid);
        startActivity(meterBookingIntent);
        finish();
    }

    /**
     * <h1>initilizeView</h1>
     * <p>initialize the views</p>
     */
    public void initilizeView()
    {
        BOOKING_FLOW_OPEN = false;
        tv_rider_name.setTypeface(appTypeFace.getPro_narMedium());
        tv_bid.setTypeface(appTypeFace.getPro_News());
        tv_status.setTypeface(appTypeFace.getPro_narMedium());
        tv_status_head.setTypeface(appTypeFace.getPro_News());
        tv_address.setTypeface(appTypeFace.getPro_News());
        tv_someOneName.setTypeface(appTypeFace.getPro_News());

        tv_distance_value.setTypeface(appTypeFace.getPro_News());
        tv_distance_head.setTypeface(appTypeFace.getPro_News());
        tv_waitingTime_head.setTypeface(appTypeFace.getPro_News());
        tv_rideTimer_head.setTypeface(appTypeFace.getPro_News());
        tv_distance_value.setTypeface(appTypeFace.getPro_narMedium());
        tv_waitingTime_value.setTypeface(appTypeFace.getPro_narMedium());
        tv_arriveDistance_head.setTypeface(appTypeFace.getPro_narMedium());
        tv_arriveDistance_value.setTypeface(appTypeFace.getPro_narMedium());
        tv_rideTimer_value.setTypeface(appTypeFace.getPro_narMedium());
        tv_booking_btnStatus.setTypeface(appTypeFace.getPro_narMedium());
        tv_bookForSomeOne.setTypeface(appTypeFace.getPro_narMedium());

        tv_make.setTypeface(appTypeFace.getPro_News());
        tv_make_value.setTypeface(appTypeFace.getPro_News());
        tv_model.setTypeface(appTypeFace.getPro_News());
        tv_model_value.setTypeface(appTypeFace.getPro_News());
        tv_year.setTypeface(appTypeFace.getPro_News());
        tv_year_value.setTypeface(appTypeFace.getPro_News());
        tv_color.setTypeface(appTypeFace.getPro_News());
        tv_color_value.setTypeface(appTypeFace.getPro_News());

        /*iv_chat.setVisibility(View.VISIBLE);*/
        presenter.checkChatEnable();

        notification = MediaPlayer.create(this, R.raw.notification);
        presenter.enableRunTime(false);
        presenter.getRideBookingData(getIntent().getStringExtra(RIDE_BOOKING_DATA));
        presenter.getPreviousDistance();

        seekBar.setSliderProgressCallback(progress -> {
            if (progress > 65) {
                seekBar.setProgress(100);
                presenter.updateBookingStatusRide();
            }
        });


        dialogHelper.getTypeCall(new DialogHelper.TypeCall() {
            @Override
            public void inAppCall() {
                showDialogForCall();
            }
        });

        dialogHelper.getServiceSelected(new DialogHelper.ServiceSelected() {
            @Override
            public void serviceSelected(String seviceID) {
                seekBar.setProgress(0);
                presenter.postTowTruckServicesAPI(seviceID);

            }

            @Override
            public void canceled() {
                seekBar.setProgress(0);
                if(progressBar!=null)
                    hideProgress();
            }

            @Override
            public void startTowingService() {
                presenter.startTowingApi();
            }
        });
    }

    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(back_white_btn);
        }
        tv_title.setTypeface(appTypeFace.getPro_narMedium());
        presenter.setActionBarTitle();
    }

    @Override
    public void setTitle() {
        tv_title.setText(title);
    }

    /**
     * <h1>startCurrLocation</h1>
     * <p>This method is used to get the current location</p>
     */
    private void startCurrLocation()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (EasyPermissions.hasPermissions(this, perms)) {
                presenter.getCurrentLocation();
            } else {
                EasyPermissions.requestPermissions(this, location_permission_message,
                        VariableConstant.RC_LOCATION_STATE, perms);
            }

        } else {
            presenter.getCurrentLocation();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        rideBookingActivity_opened = true;
        FORGROUND_LOCK = false;
        startUpdateLocation();
        presenter.enableRunTime(true);
        presenter.findBackgroundSeconds();
        presenter.subscribeNetworkObserver();
        /*presenter.networkCheckOnresume();*/
        startCurrLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        rideBookingActivity_opened = true;
        FORGROUND_LOCK = true;
        presenter.storeCurrentTime();
        presenter.enableRunTime(false);
        presenter.disposeObservables();
        presenter.clearComposite();
    }

    /**
     * <h1>initializeMap</h1>
     * <p>This method is used to initialize google Map</p>
     */
    private void initializeMapFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frag_booking_map);
        SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
        supportmapfragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (this.googleMap == null)
            return;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                !=  PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.googleMap.getUiSettings().setTiltGesturesEnabled(true);
        this.googleMap.setMyLocationEnabled(false);
        presenter.findCurrentLocation();
    }

    @Override
    public void moveGoogleMapToLocation(double newLatitude, double newLongitude)
    {
        if(googleMap == null)
            return;
        /*CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(newLatitude, newLongitude)).zoom(16.00f).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
        if(marker==null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newLatitude, newLongitude), 16));
            presenter.addCarMarker();
        }
        else
        {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newLatitude, newLongitude), 16));
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            marker.getmMarker().setPosition(new LatLng(newLatitude, newLongitude));
        }
    }


    @Override
    public void goToLogin(String errMsg) {
        DialogHelper.customAlertDialogSignupSuccess(this,message,errMsg,ok);
    }

    @Override
    public void apiFailure(String msg) {
        seekBar.setProgress(0);
        DialogHelper.customAlertDialog(this,message,msg, ok);
        /*Utility.BlueToast(this,msg);*/

    }

    @Override
    public void setCarMarker(LatLng latLng,int width, int height,
                             String carUrl) {
        try
        {
            marker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(latLng)));
            marker.getmMarker().setRotation(VariableConstant.BEARING);
            Picasso.get().load(carUrl)
                    .resize(60,
                            100)
                    .into(marker);
        }
        catch (IllegalArgumentException e)
        {
            Utility.printLog(" marker catch "+e);
            e.printStackTrace();
        }
    }

    @Override
    public void setCurrentLocation(LatLng latLng) {
        if(latLng!=null)
        {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            googleMap.getUiSettings().setZoomControlsEnabled(false);
        }
    }

    @Override
    public void connectGoogleMap(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    public void connectWaze(String uri) {
        try
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent intent =
                    new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
            startActivity(intent);
        }
    }

    @Override
    public void dropLocDisable() {
        ll_navigation.setVisibility(View.GONE);
        ll_address.setVisibility(View.GONE);
        tv_status_head.setVisibility(View.GONE);
        view_address_divider.setVisibility(View.GONE);
    }

    @Override
    public void locationUpdated(Location location) {
        if(googleMap!=null && googleMap.getProjection()!=null)
            presenter.getVehicleMoveBearing(location, googleMap.getProjection());
    }

    @Override
    public void setDistanceShow(String distance) {
        tv_distance_value.setText(distance);
        tv_arriveDistance_value.setText(distance);
    }


    @Override
    public void setCarMove(LatLng latLng, float bearing) {
        if(marker!=null)
        {
            marker.getmMarker().setPosition(latLng);
            marker.getmMarker().setAnchor(0.5f, 0.5f);
            marker.getmMarker().setRotation(VariableConstant.BEARING);
            marker.getmMarker().setFlat(true);
        }
    }

    @Override
    public void bookingCancelledPas() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        finish();
    }

    @Override
    public void showDropLocUpdated() {
        notification.start();
        DialogHelper.customAlertDialog(this,message,dropUpdateMsg, ok);

        ll_navigation.setVisibility(View.VISIBLE);
        ll_address.setVisibility(View.VISIBLE);
        tv_status_head.setVisibility(View.VISIBLE);
        view_address_divider.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDropLocAddress(String dropAddress) {
        tv_address.setText(dropAddress);
    }

    @Override
    public void networkNotAvailable() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rideBookingActivity_opened && networkErrorDialog != null && !networkErrorDialog.isShowing())
                    networkErrorDialog.show();
            }
        });
    }

    @Override
    public void networkAvailable() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (networkErrorDialog != null && networkErrorDialog.isShowing())
                    networkErrorDialog.dismiss();
            }
        });

    }

    @Override
    public void setFlagPickupDrop(BitmapDescriptor icon, LatLng point) {
        if(googleMap!=null) {

            googleMap.clear();
            presenter.addCarMarker();

            markerOptions = new MarkerOptions();
            markerOptions.icon(icon);
            markerOptions.position(point);
            flagMarker = googleMap.addMarker(markerOptions);
        }
    }

    @Override
    public void googlePathPlot(LatLongBounds latLongBounds) {
        try {
            googleMap.addPolyline(latLongBounds.getPolylineOptions());
        }catch (Exception e){
            Utility.printLog("exception : in map");
        }
    }

    @Override
    public void setServiceSelection(TowTrayService towtrayServiceSelectData) {
        DialogHelper.serviceSelectionForCompleteBooking(this,appTypeFace,towtrayServiceSelectData);

    }

    @Override
    public void showTowing() {
        rl_towing.setVisibility(View.VISIBLE);
        rl_calculateDisTime.setVisibility(View.GONE);
    }

    @Override
    public void hideTowing() {
        rl_towing.setVisibility(View.GONE);
        rl_calculateDisTime.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCustVehSet(CustVehicleDetails custVehSet) {
        tv_title.setText(towing_tittle);
        ll_cust_veh.setVisibility(View.VISIBLE);
        tv_make_value.setText(custVehSet.getMake());
        tv_model_value.setText(custVehSet.getModel());
        tv_year_value.setText(custVehSet.getYear());
        tv_color_value.setText(custVehSet.getColor());
    }


    @OnClick({R.id.iv_cancel, R.id.iv_call,
            R.id.iv_someOne_call, R.id.ll_googlemap,
            R.id.ll_booking_waze, R.id.iv_location_update,
            R.id.iv_chat,R.id.iv_towing})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.iv_cancel:
                startActivity(new Intent(this, CancelReasonActivity.class));
                break;

            case R.id.iv_call:
                VariableConstant.isTowingStart = false;
                DialogHelper.customAlertDialogCall(this, bookingData.getCustomerPhone(),appTypeFace,VariableConstant.isTwilioEnable,VariableConstant.isTowingStart);
                break;

            case R.id.iv_someOne_call:
                VariableConstant.isTowingStart = false;
                DialogHelper.customAlertDialogCall(this, bookingData.getSomeOne().getMobile(),appTypeFace,VariableConstant.isTwilioEnable,VariableConstant.isTowingStart);
                break;

            case R.id.iv_towing:
                VariableConstant.isTowingStart = true;
                DialogHelper.customAlertDialogCall(this,towingStartmsg,appTypeFace,false,VariableConstant.isTowingStart);
                break;

            case R.id.ll_googlemap:
                presenter.startGoogleMap();
                break;

            case R.id.ll_booking_waze:
                presenter.startWaze();
                break;

            case R.id.iv_location_update:
                if(googleMap!=null)
                    presenter.checkCurrentLocation();
                break;

            case R.id.iv_chat:
                Intent  intent = new Intent(this, ChattingActivity.class);
                intent.putExtra("BID",bookingData.getBookingId());
                intent.putExtra("DRIVER_NAME",bookingData.getCustomerName());
                intent.putExtra("CUST_ID",bookingData.getCustomerId());
                startActivity(intent);

                break;
        }
    }

    /**********************************************************************************************/
    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /**********************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void promptUserWithLocationAlert(Status status) {
        try
        {
            status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
        }
        catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h1>startUpdateLocation</h1>
     * <p>check the LocationUpdate service is running or not, if not running start the service</p>
     */
    public void startUpdateLocation()
    {
        if(!Utility.isMyServiceRunning(LocationUpdateService.class,this)){
            Intent startIntent = new Intent(this, LocationUpdateService.class);
            startIntent.setAction(AppConstants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null)
            presenter.onTowTryService(data);

    }

    public void sendResult(ArrayList list, String type) {
        Intent intent = new Intent();
        intent.putExtra(TYPE, type);
        intent.putExtra(DATA, list);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    @Override
    public void enableChat() {
        iv_chat.setVisibility(View.VISIBLE);
    }

    @Override
    public void launchCallsScreen(String callId, String callType) {
        CallingApis.initiateCall(this, presenter.getCustomerId(),
                presenter.getName(),
                presenter.getProfilePic(),
                callType,
                presenter.getName(),
                callId,
                bookingData.getBookingId(),
                false);
    }


    private void showDialogForCall()
    {
        openCall();
    }


    private void openCall() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (!Settings.System.canWrite(this) || !Settings.canDrawOverlays(this)) {
                if (!Settings.System.canWrite(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
                //If the draw over permission is not available open the settings screen
                //to grant the permission.

                if (!Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            }
            else
            {
                if(isPermissionGranted())
                {
                    presenter.callInitialize("audio", presenter.getCustomerId());

                }
            }
        }
        else
        {
            if(isPermissionGranted())
            {
                presenter.callInitialize("audio", presenter.getCustomerId());

            }
        }
    }

    private boolean isPermissionGranted() {
        if(EasyPermissions.hasPermissions(this, CALL_PERMISSIONS))
        {
            return true;
        }
        else
        {
            EasyPermissions.requestPermissions(this,getString(R.string.call_permission_message),
                    104, CALL_PERMISSIONS);
            return false;
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

}
