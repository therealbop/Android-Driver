package com.karry.app.meterBooking;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.karry.app.cancelBooking.CancelReasonActivity;
import com.karry.app.meterBooking.address.AddressSelectionActivity;
import com.heride.partner.R;
import com.karry.side_screens.home_fragment.invoice.InvoiceActivity;
import com.karry.network.NetworkErrorDialog;
import com.karry.pojo.bookingAssigned.BookingAssignedDataRideAppts;
import com.karry.service.LocationUpdateService;
import com.karry.utility.AppConstants;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;
import com.karry.utility.PicassoMarker;
import com.karry.utility.Scaler;
import com.karry.utility.Slider;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.karry.utility.VariableConstant.FROM_MB;
import static com.karry.utility.VariableConstant.MB_DROP_ADDRESS;
import static com.karry.utility.VariableConstant.MB_DROP_LAT_LNG;
import static com.karry.utility.VariableConstant.MB_DROP_PLACE_ID;
import static com.karry.utility.VariableConstant.METERBOOKING_INVOICE_DATA;
import static com.karry.utility.VariableConstant.MeterBookingDropBundle;
import static com.karry.utility.VariableConstant.PLACE_AUTOCOMPLETE_REQUEST_CODE;

public class DriverMeterActivity extends DaggerAppCompatActivity implements DriverMeterContract.DriverMeterView
        ,OnMapReadyCallback, GoogleMap.OnCameraIdleListener,GoogleMap.OnCameraMoveStartedListener, EasyPermissions.PermissionCallbacks
{
    @Inject AppTypeFace appTypeFace;
    @Inject DriverMeterContract.DriverMeterPresenter driverMeterPresenter;
    @Inject NetworkErrorDialog networkErrorDialog;

    @BindView(R.id.ll_navigation) LinearLayout ll_navigation;
    @BindView(R.id.ll_meter_pass_detail) RelativeLayout ll_meter_pass_detail;
    @BindView(R.id.tv_meter_drop_loc) TextView tv_meter_drop_loc;
    @BindView(R.id.tv_meter_dest_address) TextView tv_meter_dest_address;
    @BindView(R.id.tv_booking_googlemap) TextView tv_booking_googlemap;
    @BindView(R.id.tv_meter_sheet_header) TextView tv_meter_sheet_header;
    @BindView(R.id.tv_cancel_booking) TextView tv_cancel_booking;
    @BindView(R.id.tv_meter_current_val) TextView tv_meter_current_val;
    @BindView(R.id.tv_driver_time) TextView tv_driver_time;
    @BindView(R.id.tv_meter_time_val) TextView tv_meter_time_val;
    @BindView(R.id.tv_meter_cost_val) TextView tv_meter_cost_val;
    @BindView(R.id.tv_meter_min_label) TextView tv_meter_min_label;
    @BindView(R.id.tv_meter_distance) TextView tv_meter_distance;
    @BindView(R.id.tv_meter_dist_val) TextView tv_meter_dist_val;
    @BindView(R.id.tv_meter_distance_cost) TextView tv_meter_distance_cost;
    @BindView(R.id.tv_meter_unit) TextView tv_meter_unit;
    @BindView(R.id.tv_meter_base_val) TextView tv_meter_base_val;
    @BindView(R.id.tv_meter_base_fare) TextView tv_meter_base_fare;
    @BindView(R.id.tv_meter_distance_fare) TextView tv_meter_distance_fare;
    @BindView(R.id.tv_meter_dist_fare_val) TextView tv_meter_dist_fare_val;
    @BindView(R.id.tv_meter_time_fare) TextView tv_meter_time_fare;
    @BindView(R.id.tv_meter_time_fare_val) TextView tv_meter_time_fare_val;
    @BindView(R.id.tv_meter_total_val) TextView tv_meter_total_val;
    @BindView(R.id.tv_meter_total) TextView tv_meter_total;
    @BindView(R.id.tv_meter_btnStatus) TextView tv_meter_btnStatus;
    @BindView(R.id.tv_meter_tBar_title) TextView tv_meter_tBar_title;
    @BindView(R.id.tv_meter_tBar_bid) TextView tv_meter_tBar_bid;
    @BindView(R.id.tv_meter_tBar_bid_val) TextView tv_meter_tBar_bid_val;
    @BindView(R.id.tv_meter_minimumfare) TextView tv_meter_minimumfare;
    @BindView(R.id.tv_meter_minimumfare_value) TextView tv_meter_minimumfare_value;
    @BindView(R.id.tv_pickup_head) TextView tv_pickup_head;
    @BindView(R.id.tv_drop_head) TextView tv_drop_head;
    @BindView(R.id.slider_booking_myseek) Slider seekBar;
    @BindView(R.id.rl_edit_drop) RelativeLayout rl_edit_drop;
    @BindView(R.id.iv_location_update) ImageView iv_location_update;
    @BindView(R.id.ll_calculation_top) LinearLayout ll_calculation_top;
    @BindView(R.id.ll_calculation_bottom) LinearLayout ll_calculation_bottom;
    @BindView(R.id.tv_network) TextView tv_network;
    @BindView(R.id.ll_network) LinearLayout ll_network;
    @BindView(R.id.iv_blArrow) ImageView iv_blArrow;
    @BindView(R.id.tv_after_time) TextView tv_after_time;
    @BindView(R.id.tv_after_dis) TextView tv_after_dis;
    @BindView(R.id.rl_calculated_fare) RelativeLayout rl_calculated_fare;
    @BindView(R.id.ll_minimumfare) LinearLayout ll_minimumfare;
    private BottomSheetBehavior mBottomSheetBehavior1;

    @BindView(R.id.ll_googlemap) LinearLayout ll_googlemap;
    @BindView(R.id.ll_booking_waze) LinearLayout ll_booking_waze;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindString(R.string.ok) String ok;
    @BindString(R.string.message) String message;
    @BindString(R.string.after) String after;
    @BindString(R.string.miniute) String min;
    private boolean doubleBackToExitPressedOnce = false;
    public static boolean driverMeterActivity_opened = false;

    private GoogleMap googleMap;
    private BookingAssignedDataRideAppts bookingAssignedData;
    private PicassoMarker marker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_meter);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        ButterKnife.bind(this);
        initilizeView();
        initializeMapFragment();

        View bottomSheet = findViewById(R.id.bottom_sheet1);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        Utility.printLog("the top layout height is : "+ll_calculation_top.getLayoutParams().height+" "+ll_calculation_top.getMeasuredHeight());


        double[] size = Scaler.getScalingFactor(this);
        int height = ll_calculation_top.getLayoutParams().height;
        mBottomSheetBehavior1.setPeekHeight((int) (size[0]*200));
        Utility.printLog("the height is : "+(int)(getResources().getDisplayMetrics().density*180));

        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_SETTLING);

        seekBar.setSliderProgressCallback(progress -> {
            if (progress > 65) {
                seekBar.setProgress(100);
                driverMeterPresenter.completeBooking(bookingAssignedData.getBookingId());
            }
        });
    }

    /**
     * <h1>startCurrLocation</h1>
     * This method is used to get the current location
     */
    private void startCurrLocation()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (EasyPermissions.hasPermissions(this, perms)) {
                driverMeterPresenter.getCurrentLocation();
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.location_permission_message),
                        VariableConstant.RC_LOCATION_STATE, perms);
            }

        } else {
            driverMeterPresenter.getCurrentLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        driverMeterActivity_opened = true;
        startUpdateLocation();
        driverMeterPresenter.enableRunTime(true);
        driverMeterPresenter.findBackgroundSeconds();
        driverMeterPresenter.subscribeNetworkObserver();
        /*driverMeterPresenter.networkCheckOnresume();*/
        startCurrLocation();
        if(MeterBookingDropBundle!=null)
        {
            Bundle bundle = MeterBookingDropBundle;
            String dropAddress = bundle.getString(MB_DROP_ADDRESS);
            String dropLatLng = bundle.getString(MB_DROP_LAT_LNG);
            String dropPlaceID = bundle.getString(MB_DROP_PLACE_ID);

            MeterBookingDropBundle = null;
            driverMeterPresenter.setDropLocationAPI(dropAddress, dropLatLng, dropPlaceID, bookingAssignedData.getBookingId());
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("test", "onDestroy: called");
        super.onDestroy();
        driverMeterPresenter.enableRunTime(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        driverMeterActivity_opened = false;
        driverMeterPresenter.enableRunTime(false);
        driverMeterPresenter.storeCurrentTime();
        driverMeterPresenter.disposeObservables();
        driverMeterPresenter.clearComposite();
    }

    /**
     * <h1>initilizeView</h1>
     * <p>initilaize the view view, set the type for text view</p>
     */
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void initilizeView()
    {
        tv_network.setTypeface(appTypeFace.getPro_narMedium());
        tv_meter_tBar_title.setTypeface(appTypeFace.getPro_narMedium());
        tv_meter_tBar_bid.setTypeface(appTypeFace.getPro_narMedium());
        tv_meter_tBar_bid_val.setTypeface(appTypeFace.getPro_narMedium());
        tv_meter_dest_address.setTypeface(appTypeFace.getPro_News());
        tv_cancel_booking.setTypeface(appTypeFace.getPro_News());
        tv_meter_minimumfare.setTypeface(appTypeFace.getPro_News());
        tv_meter_minimumfare_value.setTypeface(appTypeFace.getPro_News());
        tv_booking_googlemap.setTypeface(appTypeFace.getPro_News());
        tv_meter_sheet_header.setTypeface(appTypeFace.getPro_narMedium());
        tv_meter_current_val.setTypeface(appTypeFace.getClanaproNarrBook());
        tv_driver_time.setTypeface(appTypeFace.getPro_News());
        tv_meter_time_val.setTypeface(appTypeFace.getPro_narMedium());
        tv_meter_cost_val.setTypeface(appTypeFace.getPro_News());
        tv_meter_min_label.setTypeface(appTypeFace.getPro_News());
        tv_meter_distance.setTypeface(appTypeFace.getPro_News());
        tv_meter_dist_val.setTypeface(appTypeFace.getPro_narMedium());
        tv_meter_distance_cost.setTypeface(appTypeFace.getPro_News());
        tv_meter_unit.setTypeface(appTypeFace.getPro_News());
        tv_meter_base_val.setTypeface(appTypeFace.getPro_News());
        tv_meter_base_fare.setTypeface(appTypeFace.getPro_News());
        tv_meter_distance_fare.setTypeface(appTypeFace.getPro_News());
        tv_meter_dist_fare_val.setTypeface(appTypeFace.getPro_News());
        tv_meter_time_fare.setTypeface(appTypeFace.getPro_News());
        tv_meter_time_fare_val.setTypeface(appTypeFace.getPro_News());
        tv_meter_total_val.setTypeface(appTypeFace.getPro_narMedium());
        tv_meter_btnStatus.setTypeface(appTypeFace.getPro_narMedium());
        tv_meter_total.setTypeface(appTypeFace.getPro_narMedium());
        tv_meter_drop_loc.setTypeface(appTypeFace.getPro_News());
        tv_pickup_head.setTypeface(appTypeFace.getPro_narMedium());
        tv_drop_head.setTypeface(appTypeFace.getPro_narMedium());
        tv_after_time.setTypeface(appTypeFace.getPro_News());
        tv_after_dis.setTypeface(appTypeFace.getPro_News());

        String meterBookingData = getIntent().getStringExtra("meterBookingData");
        Gson gson=new Gson();
        bookingAssignedData  = gson.fromJson(meterBookingData,BookingAssignedDataRideAppts.class);
        Utility.printLog("the appoinment count is 11 : "+ bookingAssignedData.isMeterBooking());

        tv_meter_tBar_bid_val.setText(bookingAssignedData.getBookingId());
        tv_meter_drop_loc.setText(bookingAssignedData.getPickupAddress());
        tv_meter_cost_val.setText(bookingAssignedData.getCurrencySbl()+" "+String.format("%.2f",Float.parseFloat(bookingAssignedData.getTimeFee())));
        tv_meter_distance_cost.setText(bookingAssignedData.getCurrencySbl()+" "+String.format("%.2f",Float.parseFloat(bookingAssignedData.getMileagePrice())));
        tv_meter_base_val.setText(bookingAssignedData.getCurrencySbl()+" "+String.format("%.2f",Float.parseFloat(bookingAssignedData.getBaseFee())));
        tv_meter_minimumfare_value.setText(bookingAssignedData.getCurrencySbl()+" "+String.format("%.2f",Float.parseFloat(bookingAssignedData.getMinFee())));
        tv_meter_dist_fare_val.setText(bookingAssignedData.getCurrencySbl()+" "+"0.0");
        tv_meter_unit.setText("/"+bookingAssignedData.getMileageMetric());
        tv_after_time.setText(after.concat(" ").concat(bookingAssignedData.getTimeFeeXMinute()).concat(" ").concat(min));
        tv_after_dis.setText(after.concat(" ").concat(bookingAssignedData.getMileageAfterXMetric()).concat(" ").concat(bookingAssignedData.getMileageMetric()));

        @SuppressLint("DefaultLocale")
        String meterFare = bookingAssignedData.getCurrencySbl()+" "+String.format("%.2f",Float.parseFloat(bookingAssignedData.getMinFee()));
        String timeFare = bookingAssignedData.getCurrencySbl()+" "+0.00;
        fareSet(meterFare, timeFare, timeFare, meterFare);
        driverMeterPresenter.setBookingAssignedDataRideAppts(bookingAssignedData);
        driverMeterPresenter.getPreviousDistance();
        driverMeterPresenter.startTimer(bookingAssignedData.getElapsedSeconds());
    }

    /**
     * <h1>initializeMap</h1>
     * <p>This method is used to initialize google Map</p>
     */
    private void initializeMapFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frag_meter_map);
        SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
        supportmapfragment.getMapAsync(this);
    }
    /**********************************************************************************************/

    @OnClick({R.id.tv_meter_dest_address, R.id.rl_edit_drop, R.id.ll_googlemap,
            R.id.ll_booking_waze, R.id.iv_location_update, R.id.tv_cancel_booking , R.id.ll_network, R.id.iv_blArrow})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_meter_dest_address:
            case R.id.rl_edit_drop:
                Intent intent = new Intent(this, AddressSelectionActivity.class);
                intent.putExtra("FROM",FROM_MB);
                startActivity(intent);
                break;

            case R.id.ll_googlemap:
                driverMeterPresenter.startGoogleMap();
                break;

            case R.id.ll_booking_waze:
                driverMeterPresenter.startWaze();
                break;

            case R.id.iv_location_update:
                if(googleMap!=null)
                    driverMeterPresenter.checkCurrentLocation();
                break;

            case R.id.tv_cancel_booking:
                startActivity(new Intent(this, CancelReasonActivity.class));
                break;

            case R.id.iv_blArrow:
                if(mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

                }
                else {
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void setAddress(String Address) {
        tv_meter_dest_address.setText(Address);
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
    public void fareSet(String meterFare, String timeFare, String distanceFare, String totalFare) {
        tv_meter_current_val.setText(meterFare);
        tv_meter_time_fare_val.setText(timeFare);
        tv_meter_dist_fare_val.setText(distanceFare);
        tv_meter_total_val.setText(totalFare);
    }

    @Override
    public void showInvoice(DriverMeterInvoiceData driverMeterInvoiceData) {
        Gson gson = new Gson();
        String meterBookingInvoiceData = gson.toJson(driverMeterInvoiceData);
        Utility.printLog("the meterBookingInvoice Data is : "+ meterBookingInvoiceData);

        Intent meterBookingIntent=new Intent(this, InvoiceActivity.class);
        meterBookingIntent.putExtra(METERBOOKING_INVOICE_DATA,driverMeterInvoiceData.getBid());
        startActivity(meterBookingIntent);
        finish();

    }

    @Override
    public void navigationEnable(boolean enable) {
        if(enable)
            ll_navigation.setVisibility(View.VISIBLE);
        else
            ll_navigation.setVisibility(View.GONE);
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
    public void setCarMarker(LatLng latLng,double width,double height,
                             String carUrl) {
        try
        {
            marker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(latLng)));
            /*Picasso.get().load(carUrl)
                    .resize(Integer.parseInt(String.valueOf(width)),
                            Integer.parseInt(String.valueOf(height)))
                    .into(marker);*/
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
    public void locationUpdated(Location location) {

        if(googleMap!=null && googleMap.getProjection()!=null){
            driverMeterPresenter.getVehicleMoveBearing(location, googleMap.getProjection() );
        }

    }

    @Override
    public void setDistanceShow(Double distance) {

        Utility.printLog("distance show : "+distance + " "+distance);
        tv_meter_dist_val.setText(String.valueOf(distance));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PLACE_AUTOCOMPLETE_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK) {
                driverMeterPresenter.fetchAddress(data,bookingAssignedData.getBookingId());
            }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            driverMeterPresenter.enableRunTime(false);
            this.finishAffinity();
        }
        else
        {
            this.doubleBackToExitPressedOnce = true;
            Utility.BlueToast(this,getResources().getString(R.string.exit_double_back));
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }

    /**********************************************************************************************/
    @Override
    public void goToLogin(String errMsg) {
        DialogHelper.customAlertDialogSignupSuccess(this,message,errMsg,ok);
    }


    @Override
    public void apiFailure(String msg) {
        Utility.BlueToast(this,msg);
        seekBar.setProgress(0);
    }

    /**********************************************************************************************/
    @Override
    public void setRunningTimer(String time) {
        tv_meter_time_val.setText(time);
    }

    /**********************************************************************************************/
    @Override
    public void onCameraIdle() {
        double lat=googleMap.getCameraPosition().target.latitude;
        double lng=googleMap.getCameraPosition().target.longitude;

        Utility.printLog("the camera has changed position : "+lat+", "+lng);

        /*double dis = ForegroundService.distance(sessionManager.getDriverCurrentLat(),sessionManager.getDriverCurrentLng(),lat,lng,METER);
        if(dis<600){
            driverMeterPresenter.checkCurrentLocation();
        }*/


    }

    @Override
    public void onCameraMoveStarted(int i) {

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

        this.googleMap.setOnCameraIdleListener(this);
        this.googleMap.setOnCameraMoveStartedListener(this);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.googleMap.getUiSettings().setTiltGesturesEnabled(true);
        this.googleMap.setMyLocationEnabled(false);
        driverMeterPresenter.findCurrentLocation();

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
            driverMeterPresenter.addCarMarker();
        }else {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newLatitude, newLongitude), 16));
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            marker.getmMarker().setPosition(new LatLng(newLatitude, newLongitude));
        }

    }

    @Override
    public void networkNotAvailable() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (driverMeterActivity_opened && networkErrorDialog != null && !networkErrorDialog.isShowing())
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
    public void minimumFareGreatUI() {
        rl_calculated_fare.setVisibility(View.GONE);
        ll_minimumfare.setVisibility(View.VISIBLE);
    }

    @Override
    public void calculatedFareGreatUI() {
        rl_calculated_fare.setVisibility(View.VISIBLE);
        ll_minimumfare.setVisibility(View.GONE);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        driverMeterPresenter.getCurrentLocation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else if (requestCode == VariableConstant.RC_LOCATION_STATE) {
            EasyPermissions.requestPermissions(this, getString(R.string.location_permission_message)
                    , VariableConstant.RC_LOCATION_STATE, Manifest.permission.ACCESS_FINE_LOCATION);
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
}
