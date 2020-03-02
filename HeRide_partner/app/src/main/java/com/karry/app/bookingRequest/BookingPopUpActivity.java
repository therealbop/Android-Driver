package com.karry.app.bookingRequest;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.karry.app.mainActivity.MainActivity;
import com.heride.partner.R;
import com.karry.utility.AppConstants;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;
import com.karry.utility.PicassoMarker;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.karry.utility.path_plot.LatLongBounds;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karry.utility.VariableConstant.IS_POP_UP_OPEN;


/**************************************************************************************************/
public class BookingPopUpActivity extends DaggerAppCompatActivity implements
        BookingPopUpContract.BookingPoUpView, OnMapReadyCallback
{
    public static MediaPlayer mediaPlayer;
    private ProgressDialog mDialog;

    @BindView(R.id.circular_progress_bar) ProgressBar circular_progress_bar;
    @BindView(R.id.tv_popup_pickup) TextView  tv_popup_pickup;
    @BindView(R.id.tvBID) TextView  tvBID;
    @BindView(R.id.tv_bookingType_head) TextView tv_bookingType_head ;
    @BindView(R.id.tv_vehicleType_head) TextView tv_vehicleType_head;
    @BindView(R.id.tv_serviceType_head) TextView tv_serviceType_head;
    @BindView(R.id.tv_BookingType) TextView tv_BookingType;
    @BindView(R.id.tv_vehicleType) TextView tv_vehicleType;
    @BindView(R.id.tv_serviceType) TextView tv_serviceType;
    @BindView(R.id.btnReject) Button btnReject;
    @BindView(R.id.tv_popup_drop) TextView tv_popup_drop;
    @BindView(R.id.tv_lefttoaccept) TextView tv_lefttoaccept;
    @BindView(R.id.tv_deliveryfee) TextView tv_deliveryfee;
    @BindView(R.id.tv_popup_cur) TextView tv_popup_cur;
    @BindView(R.id.tv_pickuptime) TextView tv_pickuptime;
    @BindView(R.id.tv_popup_droploc) TextView tv_popup_droploc;
    @BindView(R.id.tv_specialPickup) TextView tv_specialPickup;
    @BindView(R.id.tv_popup_pickuploc) TextView tv_popup_pickuploc;
    @BindView(R.id.tv_droptime) TextView tv_droptime;
    @BindView(R.id.tv_timer) TextView tv_timer;
    @BindView(R.id.tv_delivery_charge) TextView tv_delivery_charge;
    @BindView(R.id.tv_noOfSeat_head) TextView tv_noOfSeat_head;
    @BindView(R.id.tv_noOfSeat) TextView tv_noOfSeat;
    @BindView(R.id.rl_booking_popup) RelativeLayout rl_booking_popup;
    @BindView(R.id.rl_droplocation)  RelativeLayout rl_dropLocation;
    @BindView(R.id.rl_pickup)  RelativeLayout rl_pickup;
    @BindView(R.id.tvCorporateBook)  TextView tvCorporateBook;
    @BindView(R.id.tv_preference)  TextView tv_preference;
    @BindView(R.id.ll_preference)  LinearLayout ll_preference;
    @BindView(R.id.tv_distance_head)  TextView tv_distance_head;
    @BindView(R.id.tv_time_head)  TextView tv_time_head;
    @BindView(R.id.tv_Reject)  TextView tv_Reject;
    @BindView(R.id.ll_reject)  LinearLayout ll_reject;
    @BindView(R.id.tv_amount)TextView tv_amount;
    @BindView(R.id.tv_distance) TextView tv_distance;
    @BindView(R.id.tv_time) TextView tv_time;
    @BindView(R.id.ll_distance) LinearLayout ll_distance;
    @BindView(R.id.iv_location_update) ImageView iv_location_update;
    @BindView(R.id.ll_toplayer_head) LinearLayout ll_toplayer_head;
    @BindView(R.id.ll_value) LinearLayout ll_value;

    @BindView(R.id.view_seat1) View view_seat1;
    @BindView(R.id.view_seat2) View view_seat2;
    @BindView(R.id.ll_service) LinearLayout ll_service;
    @BindView(R.id.rl_service_select)  RelativeLayout rl_service_select;
    @BindView(R.id.btnServices) Button btnServices;
    @BindView(R.id.tv_service) TextView tv_service;

    @Inject DialogHelper dialogHelper;

    @Inject  BookingPopUpContract.BookingPopUpPresenter bookingPopUpPresenter;
    @Inject  AppTypeFace appTypeFace;
    @BindString(R.string.app_name) String app_name;
    /*@BindString(R.string.) String app_name;*/
    public static boolean booking_popup = false;

    private GoogleMap googleMap;
    private Polyline confirmPathPlotLine;
    private LatLngBounds latLngBounds;
    private PicassoMarker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_pop_up_map);
        ButterKnife.bind(this);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initializeViews();
        initializeMapFragment();
        bookingPopUpPresenter.getData();

        dialogHelper.getBookingAccept(new DialogHelper.BookingAccept() {
            @Override
            public void accepted() {
                bookingPopUpPresenter.updateApptRequest(AppConstants.BookingStatus.Accept);
            }

            @Override
            public void rejected() {
                bookingPopUpPresenter.updateApptRequest(AppConstants.BookingStatus.Reject);
            }
        });
    }

    /**
     * <h1>initializeMap</h1>
     * <p>This method is used to initialize google Map</p>
     */
    private void initializeMapFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frag_map);
        SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
        supportmapfragment.getMapAsync(BookingPopUpActivity.this);
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
        bookingPopUpPresenter.findCurrentLocation();
    }

    /**
     * <h1>initializeViews</h1>
     * <p>initialize the view and set the data</p>
     */
    private void initializeViews() {
        IS_POP_UP_OPEN = true;
        tv_popup_pickup.setTypeface(appTypeFace.getPro_News());
        tvBID.setTypeface(appTypeFace.getPro_narMedium());
        tv_bookingType_head.setTypeface(appTypeFace.getPro_News());
        tv_vehicleType_head.setTypeface(appTypeFace.getPro_News());
        tv_serviceType_head.setTypeface(appTypeFace.getPro_News());
        tv_noOfSeat_head.setTypeface(appTypeFace.getPro_News());
        tv_BookingType.setTypeface(appTypeFace.getPro_narMedium());
        tv_vehicleType.setTypeface(appTypeFace.getPro_narMedium());
        tv_noOfSeat.setTypeface(appTypeFace.getPro_narMedium());
        tv_serviceType.setTypeface(appTypeFace.getPro_narMedium());
        btnReject.setTypeface(appTypeFace.getPro_News());
        tv_popup_drop.setTypeface(appTypeFace.getPro_News());
        tv_lefttoaccept.setTypeface(appTypeFace.getPro_News());
        tv_deliveryfee.setTypeface(appTypeFace.getPro_News());
        tv_popup_cur.setTypeface(appTypeFace.getPro_narMedium());
        tv_specialPickup.setTypeface(appTypeFace.getPro_News());
        tv_popup_pickuploc.setTypeface(appTypeFace.getPro_News());
        tv_pickuptime.setTypeface(appTypeFace.getPro_News());
        tv_popup_droploc.setTypeface(appTypeFace.getPro_News());
        tv_droptime.setTypeface(appTypeFace.getPro_News());
        tv_preference.setTypeface(appTypeFace.getPro_News());
        tv_timer.setTypeface(appTypeFace.getPro_narMedium());
        tv_delivery_charge.setTypeface(appTypeFace.getPro_narMedium());
        tvCorporateBook.setTypeface(appTypeFace.getPro_narMedium());
        tv_distance_head.setTypeface(appTypeFace.getPro_News());
        tv_time_head.setTypeface(appTypeFace.getPro_News());
        tv_distance.setTypeface(appTypeFace.getPro_News());
        tv_time.setTypeface(appTypeFace.getPro_News());
        tv_amount.setTypeface(appTypeFace.getPro_News());
        tv_Reject.setTypeface(appTypeFace.getPro_News());
        tv_service.setTypeface(appTypeFace.getPro_News());

        mediaPlayer = MediaPlayer.create(this, R.raw.popupsound);
        mediaPlayer.setLooping(true);
        mDialog = new ProgressDialog(BookingPopUpActivity.this);
    }

    @Override
    public void setTextData(BookingDataResponse bookingDataResponse,String pickupTime,String paymentType) {
        tv_amount.setText(bookingDataResponse.getAmount());
        tv_distance.setText(bookingDataResponse.getDistanceCalc());
        tv_time.setText(bookingDataResponse.getEstimateTime());
        tv_BookingType.setText(bookingDataResponse.getBookingTypeText());
        tv_vehicleType.setText(bookingDataResponse.getTypeName());
        tv_serviceType.setText(bookingDataResponse.getServiceTypeText());
        String bookingId = getResources().getString(R.string.bid).concat(bookingDataResponse.getBookingId().concat(" (").concat(paymentType).concat(")"));
        tvBID.setText(bookingId);
        tv_popup_pickuploc.setText(bookingDataResponse.getPickupAddress());
        tv_pickuptime.setText(Utility.convertUTCToServerFormat(bookingDataResponse.getTimeStamp(), VariableConstant.TIME_FORMAT_TIME_DISPLAY) );

        tv_noOfSeat.setText(bookingDataResponse.getNumOfPassanger());
        if(bookingDataResponse.getDropAddress()==null || bookingDataResponse.getDropAddress().matches("")) {
            rl_dropLocation.setVisibility(View.GONE);
            ll_distance.setVisibility(View.GONE);
            tv_amount.setVisibility(View.INVISIBLE);
        }
        else
            tv_popup_droploc.setText(bookingDataResponse.getDropAddress());

        if(bookingDataResponse.getAreaZonePickupTitle() != null && !bookingDataResponse.getAreaZonePickupTitle().replace(" ","").matches("")) {
            tv_specialPickup.setVisibility(View.VISIBLE);
            tv_specialPickup.setText(bookingDataResponse.getAreaZonePickupTitle());
        }


    }


    @Override
    public void startMusicPlayer() {
        if(!mediaPlayer.isPlaying())
            mediaPlayer.start();
    }

    @Override
    public void enableCorporateBook() {
        tvCorporateBook.setVisibility(View.GONE);
    }

    @Override
    public void disableCorporateBook() {
        tvCorporateBook.setVisibility(View.GONE);
    }

    @Override
    public void hidePreferences() {
        ll_preference.setVisibility(View.GONE);
    }

    @Override
    public void googlePathPlot(LatLongBounds latLongBounds) {
        try {
            confirmPathPlotLine = googleMap.addPolyline(latLongBounds.getPolylineOptions());
            LatLng southWestLocation = new LatLng(Double.parseDouble(latLongBounds.getSouthwest().getLat()),
                    Double.parseDouble(latLongBounds.getSouthwest().getLng()));
            LatLng northEastLocation = new LatLng(Double.parseDouble(latLongBounds.getNortheast().getLat()),
                    Double.parseDouble(latLongBounds.getNortheast().getLng()));
            latLngBounds = new LatLngBounds(southWestLocation, northEastLocation);
            // Obtain a movement description object
            // offset from edges of the map in pixels
            int padding = 220;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding);
            // Move the map
            googleMap.moveCamera(cu);
        }catch (Exception e){
            Utility.printLog("exception : in map");
        }
    }

    @Override
    public void moveGoogleMapToLocation(double newLatitude, double newLongitude,String carUrl) {


        if(googleMap!=null){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newLatitude, newLongitude), 16));
            googleMap.getUiSettings().setZoomControlsEnabled(false);

            try
            {
                marker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(new LatLng(newLatitude,newLongitude))));
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

    }

    @Override
    public void setFlagPickupDrop(BitmapDescriptor icon, LatLng point, String distaceTime) {
        if(googleMap!=null) {
            Marker marker  =googleMap.addMarker(new MarkerOptions().position(point)
                    .icon(icon).title(distaceTime));
            marker.showInfoWindow();
        }
    }

    @Override
    public void setFlagDrop(BitmapDescriptor icon, LatLng point/*, String distaceTime*/) {
        if(googleMap!=null) {
            googleMap.addMarker(new MarkerOptions().position(point)
                    .icon(icon));


        }
    }

    @Override
    public void setTowTray() {
        view_seat1.setVisibility(View.GONE);
        view_seat2.setVisibility(View.GONE);
        tv_noOfSeat_head.setVisibility(View.GONE);
        tv_noOfSeat.setVisibility(View.GONE);
        ll_toplayer_head.setWeightSum(3);
        ll_value.setWeightSum(3);
        ll_service.setVisibility(View.VISIBLE);
        rl_service_select.setVisibility(View.VISIBLE);


    }

    @Override
    public void disableDropLocation() {
        rl_dropLocation.setVisibility(View.GONE);
    }

    @Override
    public void enableRentalBooking() {
        rl_dropLocation.setVisibility(View.GONE);
        /*tv_popup_drop.setText(getResources());*/
    }

    @Override
    public void enableHotelBooking() {

    }


    @OnClick({R.id.rl_booking_popup, R.id.btnReject, R.id.iv_location_update, R.id.ll_reject, R.id.rl_service_select})
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.rl_booking_popup:
                disableClick();
                Utility.printLog("click click");
                bookingPopUpPresenter.updateApptRequest(AppConstants.BookingStatus.Accept);
                break;

            case R.id.btnReject:
            case R.id.ll_reject:
                disableClick();
                bookingPopUpPresenter.updateApptRequest(AppConstants.BookingStatus.Reject);
                break;

            case  R.id.iv_location_update :
                bookingPopUpPresenter.reboundMap();
                break;

            case  R.id.rl_service_select :
                bookingPopUpPresenter.setServiceListDialog(this,appTypeFace);

                break;
        }
    }

    /**
     * <h1>disableClick</h1>
     * <p>once click happens need to disable the click option</p>
     */
    void disableClick(){
        rl_booking_popup.setEnabled(false);
        btnReject.setEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        booking_popup = false;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        booking_popup = true;

    }


    @Override
    public void onSuccess() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }

    @Override
    public void onError(String error) {
        if(error.isEmpty())
            Toast.makeText(BookingPopUpActivity.this, getString(R.string.smthWentWrong), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(BookingPopUpActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressbar() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.setMessage(getResources().getString(R.string.loading));
            mDialog.setCancelable(false);
            mDialog.show();
        }
    }

    @Override
    public void dismissProgressbar() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog.cancel();
        }
    }

    @Override
    public void onTimerChanged(int progress,String time) {
        tv_timer.setText(time);
        circular_progress_bar.setProgress(progress);
    }

    @Override
    public void onFinish() {
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        IS_POP_UP_OPEN=false;
        finish();
    }



}