package com.karry.side_screens.home_fragment;

import static com.heride.partner.BuildConfig.IS_DROP_MANDATE_METER_BOOKING;
import static com.karry.utility.LocationUtil.REQUEST_CHECK_SETTINGS;
import static com.karry.utility.VariableConstant.BOOKING_FLOW_OPEN;
import static com.karry.utility.VariableConstant.FORGROUND_LOCK;
import static com.karry.utility.VariableConstant.METERBOOKING_INVOICE_DATA;
import static com.karry.utility.VariableConstant.RIDE_BOOKING_DATA;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.karry.app.mainActivity.MainActivity;
import com.karry.app.meterBooking.DriverMeterActivity;
import com.karry.app.meterBooking.address.AddressSelectionActivity;
import com.karry.bookingFlow.RideBookingActivity;
import com.karry.dagger.ActivityScoped;
import com.karry.network.NetworkErrorDialog;
import com.heride.partner.BuildConfig;
import com.heride.partner.R;
import com.karry.pojo.bookingAssigned.BookingAssignedDataRideAppts;
import com.karry.pojo.bookingAssigned.BookingAssignedResponse;
import com.karry.service.LocationUpdateService;
import com.karry.side_screens.home_fragment.invoice.InvoiceActivity;
import com.karry.utility.AppConstants;
import com.karry.utility.AppTypeFace;
import com.karry.utility.BitmapCustomMarker;
import com.karry.utility.DialogHelper;
import com.karry.utility.PicassoMarker;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import dagger.android.support.DaggerFragment;
import javax.inject.Inject;
import pub.devrel.easypermissions.EasyPermissions;

@ActivityScoped
public class HomeFragment extends DaggerFragment
    implements HomeFragmentContract.HomeFragmentView, OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener {

  @BindView(R.id.swtch_toolbar_mode)
  SwitchCompat swtch_toolbar_mode;

  @BindView(R.id.tvMeterBooking)
  TextView tvMeterBooking;

  @BindView(R.id.ll_rideBooking_detail)
  LinearLayout ll_rideBooking_detail;

  @BindView(R.id.ll_services)
  LinearLayout ll_services;

  @BindView(R.id.rl_home_name)
  RelativeLayout rl_home_name;

  @BindView(R.id.tv_shipment)
  TextView tv_shipment;

  @BindView(R.id.tv_ride)
  TextView tv_ride;

  @BindView(R.id.tv_toolbar_title)
  TextView tv_toolbar_title;

  @BindView(R.id.iv_location_update)
  ImageView iv_location_update;

  @BindView(R.id.tv_network)
  TextView tv_network;

  @BindView(R.id.tv_toolbar_version)
  TextView tv_toolbar_version;

  @BindView(R.id.ll_network)
  LinearLayout ll_network;

  @BindView(R.id.rl_shipment)
  RelativeLayout rl_shipment;

  @BindView(R.id.rl_ride)
  RelativeLayout rl_ride;

  @BindView(R.id.view_shipment)
  View view_shipment;

  @BindView(R.id.view_ride)
  View view_ride;

  @BindView(R.id.iv_pickup_loc)
  ImageView iv_pickup_loc;

  @BindView(R.id.ll_address_layout)
  LinearLayout ll_address;

  @BindView(R.id.rv_deliveryAppts)
  RecyclerView rv_deliveryAppts;

  @BindView(R.id.tv_bid)
  TextView tv_bid;

  @BindView(R.id.tv_status)
  TextView tv_status;

  @BindView(R.id.tv_rider_name)
  TextView tv_rider_name;

  @BindView(R.id.tv_pickUp_head)
  TextView tv_pickUp_head;

  @BindView(R.id.tv_pickup_loc)
  TextView tv_pickup_loc;

  @BindView(R.id.tv_cashBalance)
  TextView tv_cashBalance;

  @BindView(R.id.tv_cashBalance_value)
  TextView tv_cashBalance_value;

  @BindView(R.id.tv_softLimit)
  TextView tv_softLimit;

  @BindView(R.id.tv_softLimit_value)
  TextView tv_softLimit_value;

  @BindView(R.id.tv_hardlimit)
  TextView tv_hardlimit;

  @BindView(R.id.tv_hardlimit_value)
  TextView tv_hardlimit_value;

  @BindView(R.id.ll_queue_position)
  LinearLayout ll_queue_position;

  @BindView(R.id.tv_queue_position)
  TextView tv_queue_position;

  @BindView(R.id.tv_queue_value)
  TextView tv_queue_value;

  @BindView(R.id.progressBar)
  ProgressBar progressBar;

  @BindString(R.string.ok)
  String ok;

  @BindString(R.string.message)
  String message;

  @BindString(R.string.online)
  String online;

  @BindString(R.string.offline)
  String offline;

  @BindString(R.string.location_permission_message)
  String location_permission_message;

  @BindString(R.string.pickup_location)
  String pickup_location;

  @BindString(R.string.drop_location)
  String drop_location;

  @BindString(R.string.bid)
  String bid;

  @BindDrawable(R.drawable.home_pickup_icon)
  Drawable home_pickup_icon;

  @BindDrawable(R.drawable.home_dropoff_icon)
  Drawable home_dropoff_icon;

  private GoogleMap googleMap;
  private MediaPlayer mp_Online, mp_Offline, notification;

  @Inject
  HomeFragmentContract.HomeFragmentPresenter homeFragmentPresenter;
  @Inject
  AppTypeFace appTypeFace;
  @Inject
  NetworkErrorDialog networkErrorDialog;
  @Inject
  DialogHelper dialogHelper;
  private Dialog walletAlertDialog;
  private PicassoMarker marker;

  private Unbinder unbinder;
  private BookingAssignedResponse bookingAssignedResponse;

  public static boolean homeFrag = false;

  @Inject
  public HomeFragment() {
  }

  @Override
  public void onResume() {
    super.onResume();

    rv_deliveryAppts.setVisibility(View.GONE);
    ll_rideBooking_detail.setVisibility(View.GONE);
    tvMeterBooking.setVisibility(View.GONE);

    setRideData();

    homeFrag = true;
    FORGROUND_LOCK = false;
    homeFragmentPresenter.checkBookingPopUp();
    swtch_toolbar_mode.setEnabled(false);
    homeFragmentPresenter.getBookingsAssigned();
    homeFragmentPresenter.checkRideCancel();
    homeFragmentPresenter.checkDropUpdate();
    startCurrLocation();

  }

  @Override
  public void setAddress(String address) {
    tv_pickup_loc.setText(address);
  }

  @Override
  public void setWalletData(String cashBalance, String softLimit, String hardLimit) {
    tv_cashBalance_value.setText(cashBalance);
    tv_softLimit_value.setText(softLimit);
    tv_hardlimit_value.setText(hardLimit);
  }

  @Override
  public void onPause() {
    super.onPause();
    FORGROUND_LOCK = true;
    homeFrag = false;
    homeFragmentPresenter.clearComposite();
    homeFragmentPresenter.disposeObservables();
    if (walletAlertDialog != null) if (walletAlertDialog.isShowing()) walletAlertDialog.dismiss();
  }

  /**
   * <h1>startCurrLocation</h1>
   *
   * This method is used to get the current location
   */
  private void startCurrLocation() {
    if (Build.VERSION.SDK_INT >= 23) {
      String[] perms = { Manifest.permission.ACCESS_FINE_LOCATION };
      if (EasyPermissions.hasPermissions(getContext(), perms)) {
        homeFragmentPresenter.getCurrentLocation();
      } else {
        EasyPermissions.requestPermissions(this, location_permission_message,
            VariableConstant.RC_LOCATION_STATE, perms);
      }
    } else {
      homeFragmentPresenter.getCurrentLocation();
    }
  }

  @Override
  public void callBookingAssigned() {
    homeFragmentPresenter.getBookingsAssigned();
  }

  @Override
  public void getBookingsAssignedSuccess(BookingAssignedResponse bookingAssignedResponse) {
    this.bookingAssignedResponse = bookingAssignedResponse;
  }

  @Override
  public void driverOnline() {
    swtch_toolbar_mode.setChecked(true);
    swtch_toolbar_mode.setEnabled(true);
    changeDriverStatus(Integer.parseInt(AppConstants.BookingStatus.Online));
  }

  @Override
  public void drierOffline() {
    swtch_toolbar_mode.setChecked(false);
    swtch_toolbar_mode.setEnabled(true);
    changeDriverStatus(Integer.parseInt(AppConstants.BookingStatus.Offline));
  }

  @Override
  public void driverBusy() {
    swtch_toolbar_mode.setEnabled(false);
    changeDriverStatus(Integer.parseInt(AppConstants.BookingStatus.busy));
  }

  @Override
  public void setCurrentLocation(LatLng latLng) {
    if (latLng != null) {
      googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
      googleMap.getUiSettings().setZoomControlsEnabled(false);
    }
  }

  @Override
  public void locationUpdated(Location location) {
    homeFragmentPresenter.getVehicleMoveBearing(location, googleMap.getProjection());
  }

  @Override
  public void setCarMove(LatLng latLng, float bearing) {
    if (marker != null) {
      marker.getmMarker().setPosition(latLng);
      marker.getmMarker().setAnchor(0.5f, 0.5f);
      marker.getmMarker().setRotation(VariableConstant.BEARING);
      marker.getmMarker().setFlat(true);
    }
  }

  @Override
  public void setCarMarker(LatLng latLng, double width, double height, String carUrl) {
    try {
      marker = new PicassoMarker(googleMap.addMarker(new MarkerOptions().position(latLng)));
      marker.getmMarker().setRotation(VariableConstant.BEARING);
      Picasso.get().load(carUrl).resize(60, 100).into(marker);
    } catch (IllegalArgumentException e) {
      Utility.printLog(" marker catch " + e);
      e.printStackTrace();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (marker != null) marker = null;
    homeFragmentPresenter.detachView();
    unbinder.unbind();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.activity_home, container, false);
    unbinder = ButterKnife.bind(this, rootView);
    homeFragmentPresenter.attachView(this);
    mp_Online = MediaPlayer.create(getContext(), R.raw.online);
    mp_Offline = MediaPlayer.create(getContext(), R.raw.offline);
    notification = MediaPlayer.create(getContext(), R.raw.notification);

    initializeView();
    initializeMap();
    ((MainActivity) getActivity()).setFragmentRefreshListener(this::onResume);
    return rootView;
  }

  /**
   * <h1>initializeView</h1>
   *
   * <p>which initilize the view, set type face and check which services are available
   */
  private void initializeView() {
    tv_toolbar_version.setText(BuildConfig.VERSION_NAME);
    tv_network.setTypeface(appTypeFace.getPro_narMedium());
    tv_toolbar_title.setTypeface(appTypeFace.getPro_narMedium());
    tvMeterBooking.setTypeface(appTypeFace.getPro_narMedium());
    tv_shipment.setTypeface(appTypeFace.getPro_narMedium());
    tv_ride.setTypeface(appTypeFace.getPro_narMedium());

    tv_bid.setTypeface(appTypeFace.getPro_News());
    tv_status.setTypeface(appTypeFace.getPro_narMedium());
    tv_rider_name.setTypeface(appTypeFace.getPro_narMedium());
    tv_pickUp_head.setTypeface(appTypeFace.getPro_News());
    tv_pickup_loc.setTypeface(appTypeFace.getPro_News());
    tv_cashBalance.setTypeface(appTypeFace.getPro_News());
    tv_cashBalance_value.setTypeface(appTypeFace.getPro_narMedium());
    tv_softLimit.setTypeface(appTypeFace.getPro_News());
    tv_softLimit_value.setTypeface(appTypeFace.getPro_narMedium());
    tv_hardlimit.setTypeface(appTypeFace.getPro_News());
    tv_hardlimit_value.setTypeface(appTypeFace.getPro_narMedium());
    tv_queue_position.setTypeface(appTypeFace.getPro_News());
    tv_queue_value.setTypeface(appTypeFace.getPro_narMedium());
    swtch_toolbar_mode.setEnabled(false);
    homeFragmentPresenter.getServiceView();
  }

  @Override
  public void setRideBookigView() {
    ll_services.setVisibility(View.GONE);
  }

  @Override
  public void setShipmentView() {
    ll_services.setVisibility(View.GONE);
  }

  @Override
  public void setShipmentRideBookingView() {
    /*ll_services.setVisibility(View.VISIBLE);
    tv_shipment.setSelected(false);
    view_shipment.setVisibility(View.GONE);
    view_ride.setVisibility(View.VISIBLE);
    tv_ride.setSelected(true);
    tvMeterBooking.setVisibility(View.GONE);*/

  }

  @Override
  public void setMeterBooking() {
    tvMeterBooking.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideMeterBooking() {
    tvMeterBooking.setVisibility(View.GONE);
  }

  @Override
  public void drawAreaZones(PolygonOptions polygonOptions) {
    com.google.android.gms.maps.model.Polygon polygon = googleMap.addPolygon(polygonOptions);
  }

  @Override
  public void addSergeInMap(final LatLng location, final String text) {

    if (googleMap != null) {

      BitmapCustomMarker bitmapCustomMarker =
          new BitmapCustomMarker(getContext(), text, appTypeFace);
      final MarkerOptions markerOptions = new MarkerOptions().position(location)
          .icon(BitmapDescriptorFactory.fromBitmap(bitmapCustomMarker.createBitmap()))
          .anchor(0.5f, 1);
      googleMap.addMarker(markerOptions);
    }
  }

  @Override
  public void setQueuePosition(String position) {
    tv_queue_value.setText(position);
    ll_queue_position.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideQueuePosition() {
    ll_queue_position.setVisibility(View.GONE);
  }

  /**
   * <h1>initializeMap</h1>
   *
   * <p>initialization of Map
   */
  private void initializeMap() {
    FragmentManager fragmentManager = getChildFragmentManager();
    Fragment fragment = fragmentManager.findFragmentById(R.id.map);
    SupportMapFragment supportmapfragment = (SupportMapFragment) fragment;
    supportmapfragment.getMapAsync(this);
  }

  @OnCheckedChanged(R.id.swtch_toolbar_mode)
  public void onCheckedListner(CompoundButton buttonView, boolean isChecked) {
    if (bookingAssignedResponse != null && !bookingAssignedResponse.getData()
        .getMasterStatus()
        .matches(AppConstants.BookingStatus.busy)) {
      if (isChecked) {
        homeFragmentPresenter.setDriverStatus(Integer.parseInt(AppConstants.BookingStatus.Online));
        mp_Online.start();
      } else {
        homeFragmentPresenter.setDriverStatus(Integer.parseInt(AppConstants.BookingStatus.Offline));
        mp_Offline.start();
      }
    }
  }

  @Override
  public void changeDriverStatus(int status) {

    switch (String.valueOf(status)) {
      case AppConstants.BookingStatus.Online:
        startUpdateLocation();
        tv_toolbar_title.setText(online);
        tv_toolbar_title.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
        break;
      case AppConstants.BookingStatus.Offline:
        stopUpdateLocation();
        tv_toolbar_title.setText(offline);
        tv_toolbar_title.setTextColor(getResources().getColor(R.color.red_light));
        hideQueuePosition();
        break;
      case AppConstants.BookingStatus.busy:
        startUpdateLocation();
        swtch_toolbar_mode.setChecked(false);
        tv_toolbar_title.setText(offline);
        tv_toolbar_title.setTextColor(getResources().getColor(R.color.colorPrimary));
        tvMeterBooking.setVisibility(View.GONE);
        break;
    }
  }

  @OnClick({
      R.id.map, R.id.tvMeterBooking, R.id.iv_location_update, R.id.ll_network,
      R.id.ll_rideBooking_detail, R.id.rl_shipment, R.id.rl_ride
  })
  public void onClick1(View view) {
    switch (view.getId()) {
      case R.id.map:
        break;

      case R.id.tvMeterBooking:
        if(IS_DROP_MANDATE_METER_BOOKING) {
          startActivity(new Intent(getActivity(),
              AddressSelectionActivity.class));
        } else{
          homeFragmentPresenter.startMeterBookingAPI();
        }
        break;

      case R.id.iv_location_update:
        if (googleMap != null) homeFragmentPresenter.checkCurrentLocation();
        break;

      case R.id.ll_rideBooking_detail:
        homeFragmentPresenter.getRideBookingData();
        break;

      case R.id.rl_shipment:
        setShipmentData();
        break;

      case R.id.rl_ride:
        setRideData();
        break;

      default:
        break;
    }
  }

  private void setShipmentData() {

    tv_shipment.setSelected(true);
    view_shipment.setVisibility(View.VISIBLE);
    view_ride.setVisibility(View.GONE);
    tv_ride.setSelected(false);
    tvMeterBooking.setVisibility(View.GONE);
    ll_rideBooking_detail.setVisibility(View.GONE);

    if (bookingAssignedResponse != null
        && bookingAssignedResponse.getData().getDeliveryAppts().size() > 0) {
      rv_deliveryAppts.setVisibility(View.VISIBLE);
    }
  }

  private void setRideData() {

    tv_shipment.setSelected(false);
    view_shipment.setVisibility(View.GONE);
    view_ride.setVisibility(View.VISIBLE);
    tv_ride.setSelected(true);
    if (bookingAssignedResponse != null) {
      homeFragmentPresenter.checkMeterBooking(bookingAssignedResponse.getData().getMasterStatus());
    }

    if (bookingAssignedResponse != null
        && bookingAssignedResponse.getData().getRideAppts().size() > 0) {
      ll_rideBooking_detail.setVisibility(View.VISIBLE);
    }

    rv_deliveryAppts.setVisibility(View.GONE);
  }

  @Override
  public void startRideBooking(String rideBookingData) {
    Intent rideIntent = new Intent(getActivity(), RideBookingActivity.class);
    rideIntent.putExtra(RIDE_BOOKING_DATA, rideBookingData);
    startActivity(rideIntent);
  }

  @Override
  public void moveCurrentLoc(Location location) {
    if (googleMap != null) {
      googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
          new LatLng(location.getLatitude(), location.getLongitude()), 16));
      googleMap.getUiSettings().setZoomControlsEnabled(false);
    }
  }

  @Override
  public void setInvoice(String bookingID) {
    Intent meterBookingIntent = new Intent(getActivity(), InvoiceActivity.class);
    meterBookingIntent.putExtra(METERBOOKING_INVOICE_DATA, bookingID);
    startActivity(meterBookingIntent);
    getActivity().finish();
  }

  @Override
  public void setOnTheWayText() {
    tv_pickUp_head.setText(pickup_location);
    iv_pickup_loc.setImageDrawable(home_pickup_icon);
  }

  @Override
  public void setArrivedText() {
    tv_pickUp_head.setText(pickup_location);
    iv_pickup_loc.setImageDrawable(home_pickup_icon);
  }

  @Override
  public void setStartTripText() {
    tv_pickUp_head.setText(drop_location);
    iv_pickup_loc.setImageDrawable(home_dropoff_icon);
  }

  @Override
  public void dropLocDisable() {
    tv_pickup_loc.setVisibility(View.GONE);
    ll_address.setVisibility(View.GONE);
    tv_pickUp_head.setVisibility(View.GONE);
  }

  @Override
  public void showDialog(String msg) {
    notification.start();
    DialogHelper.customAlertDialog(getActivity(), message, msg, ok);
  }

  /** ****************************************************************************************** */
  @Override
  public void bookingEnabled(BookingAssignedDataRideAppts bookingAssignedDataRideAppts,
      String masterStatus) {

    Gson gson = new Gson();
    String meterBookingData = gson.toJson(bookingAssignedDataRideAppts);
    Utility.printLog("the appoinment count is : " + meterBookingData);

    if (bookingAssignedDataRideAppts.isMeterBooking()) {
      Intent meterBookingIntent = new Intent(getActivity(), DriverMeterActivity.class);
      meterBookingIntent.putExtra("meterBookingData", meterBookingData);
      meterBookingIntent.setFlags(
          /*Intent.FLAG_ACTIVITY_CLEAR_TOP|*/ Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(meterBookingIntent);
      getActivity().overridePendingTransition(R.anim.activity_open_translate,
          R.anim.activity_close_scale);
    } else {
      ll_rideBooking_detail.setVisibility(View.VISIBLE);
      ll_rideBooking_detail.startAnimation(
          AnimationUtils.loadAnimation(getContext(), R.anim.bottem_slide_down));
      tv_bid.setText(bid.concat(bookingAssignedDataRideAppts.getBookingId()));
      tv_status.setText(bookingAssignedDataRideAppts.getStatusText());
      tv_rider_name.setText(bookingAssignedDataRideAppts.getCustomerName());
      homeFragmentPresenter.setStatusChangeText(bookingAssignedDataRideAppts.getStatus());

      if (BOOKING_FLOW_OPEN) {
        homeFragmentPresenter.getRideBookingData();
      }
    }
  }

  /** ****************************************************************************************** */
  @Override
  public void networkError(String message) {
  }

  @Override
  public void showProgress() {
    progressBar.setVisibility(View.VISIBLE);
    getActivity().getWindow()
        .setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
  }

  @Override
  public void meterBookingSuccess(BookingAssignedDataRideAppts bookingAssignedDataRideAppts) {
    new Utility().startMeterBookingActivity(bookingAssignedDataRideAppts,getActivity());
  }

  @Override
  public void hideProgress() {
    progressBar.setVisibility(View.GONE);
    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
  }

  @Override
  public void goToLogin(String errMsg) {
    DialogHelper.customAlertDialogSignupSuccess(getActivity(), message, errMsg, ok);
  }

  /** ****************************************************************************************** */
  @Override
  public void onCameraIdle() {
  }

  @Override
  public void onCameraMoveStarted(int i) {
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {

    this.googleMap = googleMap;

    if (this.googleMap == null) return;
    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(getActivity(),
        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return;
    }

    this.googleMap.setOnCameraIdleListener(this);
    this.googleMap.setOnCameraMoveStartedListener(this);
    this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
    this.googleMap.getUiSettings().setTiltGesturesEnabled(true);
    this.googleMap.setMyLocationEnabled(false);

    homeFragmentPresenter.findCurrentLocation();
    homeFragmentPresenter.checkCurrentLocation();

    homeFragmentPresenter.getAreaZone();
  }

  @Override
  public void moveGoogleMapToLocation(double newLatitude, double newLongitude) {
    if (googleMap == null) return;

    if (marker == null) {
      googleMap.moveCamera(
          CameraUpdateFactory.newLatLngZoom(new LatLng(newLatitude, newLongitude), 16));
      homeFragmentPresenter.addCarMarker();
    } else {
      this.googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
        @Override
        public void onCameraIdle() {
          double lat = googleMap.getCameraPosition().target.latitude;
          double lng = googleMap.getCameraPosition().target.longitude;
          double dis =
              LocationUpdateService.distance(newLatitude, newLongitude, lat, lng, "meters");
          if (dis < 50) {
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(new LatLng(newLatitude, newLongitude), 16));
            googleMap.getUiSettings().setZoomControlsEnabled(false);
          }
        }
      });

      marker.getmMarker().setPosition(new LatLng(newLatitude, newLongitude));
    }
  }

  /** ****************************************************************************************** */
  public void startUpdateLocation() {
    if (!Utility.isMyServiceRunning(LocationUpdateService.class, getActivity())) {
      Intent startIntent = new Intent(getActivity(), LocationUpdateService.class);
      startIntent.setAction(AppConstants.ACTION.STARTFOREGROUND_ACTION);
      getActivity().startService(startIntent);
    }
  }

  public void stopUpdateLocation() {
    if (Utility.isMyServiceRunning(LocationUpdateService.class, getActivity())) {
      /*if(startIntent!=null){
        try {
          getActivity().stopService(startIntent);
        }
        catch (Exception e){
          e.printStackTrace();
        }
      }*/
      Intent stopIntent = new Intent(getActivity(), LocationUpdateService.class);
      stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
      getActivity().startService(stopIntent);
    }
  }

  public void onPermissionGranted() {
    homeFragmentPresenter.getCurrentLocation();
  }

  @Override
  public void promptUserWithLocationAlert(Status status) {
    try {
      status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
    } catch (IntentSender.SendIntentException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void rechargeWallet(String cashBalance, String softLimit, String hardLimit) {
    if (walletAlertDialog != null && walletAlertDialog.isShowing()) walletAlertDialog.dismiss();

    walletAlertDialog =
        DialogHelper.walletLimitWarning(getActivity(), cashBalance, softLimit, hardLimit);
    if (walletAlertDialog != null && !walletAlertDialog.isShowing()) {
      walletAlertDialog.show();
    }
  }
}
