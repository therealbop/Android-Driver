
package com.karry.app.meterBooking.address;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.karry.utility.VariableConstant.MeterBookingDropBundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.karry.adapter.PlaceAutoCompleteAdapter;
import com.karry.adapter.RecentAddressAdapter;
import com.karry.network.NetworkErrorDialog;
import com.heride.partner.R;
import com.karry.pojo.PlaceAutoCompletePojo;
import com.karry.pojo.bookingAssigned.BookingAssignedDataRideAppts;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;
import com.karry.utility.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import dagger.android.support.DaggerAppCompatActivity;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 *
 *
 * <h1>AddDropLocation Activity</h1>
 *
 * This class is used to provide the AddDropLocation screen, where we can search or select our
 * address.
 *
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class AddressSelectionActivity extends DaggerAppCompatActivity
    implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        AddressSelectContract.View {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.tv_title)
  TextView tv_title;

  @BindString(R.string.setDropLocation)
  String title;

  @Inject AppTypeFace appTypeface;
  @Inject AddressSelectContract.Presenter presenter;

  @Inject NetworkErrorDialog networkErrorDialog;
  private ArrayList<PlaceAutoCompletePojo> placeAutoCompletePojos;
  private ArrayList<PlaceAutoCompletePojo> recentAddressList = new ArrayList<>();
  public static boolean addressSelectActivity_opened = false;
  long last_text_edit = 0;
  long delay = 1000; // 1 seconds after user stops typing

  private double placeAPI_Count = 0;

  private String TAG = "AddressSelectionActivity";
  private GoogleApiClient mGoogleApiClient;
  @Inject PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
  private RecentAddressAdapter recentAddressAdapter;
  Handler handler = new Handler();
  private String constraint;

  private Runnable input_finish_checker =
      new Runnable() {
        public void run() {
          if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
            if (iv_select_address_clear.getVisibility() != VISIBLE)
              iv_select_address_clear.setVisibility(VISIBLE);
            presenter.toggleFavAddressField(false);
            filterAddress();
          }
        }
      };

  @Override
  public void filterAddress() {
    if (!constraint.equals("") && mGoogleApiClient.isConnected())
      placeAutoCompleteAdapter.getFilter().filter(constraint);
  }

  @Override
  public double getcount() {
    return placeAPI_Count;
  }

  @BindView(R.id.et_select_address_search)
  EditText et_select_address_search;

  @BindView(R.id.iv_select_address_clear)
  ImageView iv_select_address_clear;

  @BindView(R.id.tv_select_address_map)
  TextView tv_select_address_map;

  @BindView(R.id.tv_select_address_auto_title)
  TextView tv_select_address_auto_title;

  @BindView(R.id.tv_select_address_recent_title)
  TextView tv_select_address_recent_title;

  @BindView(R.id.cv_select_address_auto)
  CardView cv_select_address_auto;

  @BindView(R.id.cv_select_address_recent)
  CardView cv_select_address_recent;

  @BindView(R.id.rv_select_address_auto_list)
  RecyclerView rv_select_address_auto_list;

  @BindView(R.id.rv_select_address_recent_list)
  RecyclerView rv_select_address_recent_list;

  @BindView(R.id.iv_select_address_type)
  ImageView iv_select_address_type;

  @BindView(R.id.progressBar)
  ProgressBar progressBar;

  @BindString(R.string.ok)
  String ok;

  @BindString(R.string.message)
  String message;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_address_selection);
    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    ButterKnife.bind(this);
    setTypeface();
    recentAddressAdapter = new RecentAddressAdapter(this, presenter);
    presenter.getRecentAddress();
    presenter.setActionBar();

    mGoogleApiClient =
        new GoogleApiClient.Builder(this)
            .enableAutoManage(this, 0 /* clientId */, this)
            .addApi(Places.GEO_DATA_API)
            .build();

    initViews();
  }

  @Override
  protected void onResume() {
    super.onResume();
    addressSelectActivity_opened = true;
    presenter.subscribeNetworkObserver();
    presenter.networkCheckOnresume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    addressSelectActivity_opened = false;
  }

  @Override
  public void networkNotAvailable() {

    if (addressSelectActivity_opened
        && networkErrorDialog != null
        && !networkErrorDialog.isShowing()) networkErrorDialog.show();
  }

  @Override
  public void networkAvailable() {

    if (networkErrorDialog != null && networkErrorDialog.isShowing()) networkErrorDialog.dismiss();
  }

  /**
   *
   *
   * <h2>setTypeface</h2>
   *
   * This method is used to set the type face for the views
   */
  private void setTypeface() {
    et_select_address_search.setTypeface(appTypeface.getPro_News());
    tv_select_address_map.setTypeface(appTypeface.getPro_News());
    tv_select_address_auto_title.setTypeface(appTypeface.getPro_News());
    tv_select_address_recent_title.setTypeface(appTypeface.getPro_News());
  }

  @Override
  public void onStart() {
    mGoogleApiClient.connect();
    super.onStart();
  }

  /**
   * <h>initViews</h>
   *
   * <p>This method initialize the all UI elements of our layout.
   */
  private void initViews() {
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    placeAutoCompletePojos = new ArrayList<>();
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    rv_select_address_auto_list.setLayoutManager(linearLayoutManager);
    placeAutoCompleteAdapter.setData(placeAutoCompletePojos);
    placeAutoCompleteAdapter.notifyDataSetChanged();

    LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
    rv_select_address_recent_list.setLayoutManager(linearLayoutManager1);
    rv_select_address_recent_list.setAdapter(recentAddressAdapter);

    placeAutoCompleteAdapter.getAddressData(
        new PlaceAutoCompleteAdapter.AddressSelected() {
          @Override
          public void addressData(PlaceAutoCompletePojo placeAutoCompletePojo) {
            showProgress();
            presenter.fidLatLng(placeAutoCompletePojo, true);
            rv_select_address_auto_list.setVisibility(GONE);
            et_select_address_search.setText(placeAutoCompletePojo.getAddress());
          }

          @Override
          public void hideRecentList(int size) {
            cv_select_address_auto.setVisibility(size > 0 ? VISIBLE : GONE);
            cv_select_address_recent.setVisibility(size > 0 ? GONE : VISIBLE);
          }
        });

    recentAddressAdapter.getAddressData(
        placeAutoCompletePojo -> {
          showProgress();
          presenter.fidLatLng(placeAutoCompletePojo, false);
          rv_select_address_auto_list.setVisibility(GONE);
        });

    ProgressDialog dialog = new ProgressDialog(this);
    dialog.setCancelable(true);
    dialog.setMessage(getResources().getString(R.string.pleaseWait));
  }

  /** ******************************************************************************************* */
  @Override
  public void initActionBar() {

    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowTitleEnabled(false);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white_btn);
    }
    tv_title.setTypeface(appTypeface.getPro_narMedium());
    presenter.setActionBarTitle();
  }

  @Override
  public void setTitle() {
    tv_title.setText(title);
  }

  @Override
  public void addressFetchSuccess(ArrayList<PlaceAutoCompletePojo> placeAutoCompletePojos) {
    this.recentAddressList = placeAutoCompletePojos;
    cv_select_address_recent.setVisibility(placeAutoCompletePojos.size() > 0 ? VISIBLE : GONE);
    recentAddressAdapter.setData(placeAutoCompletePojos);
    recentAddressAdapter.notifyDataSetChanged();
  }

  @Override
  public void latLngFetchSuccess(Bundle bundle) {
    if (bundle != null) MeterBookingDropBundle = bundle;
    finish();
    overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
  }

  /** ******************************************************************************************* */
  @Override
  public void networkError(String message) {}

  @Override
  public void showProgress() {
    progressBar.setVisibility(VISIBLE);
    getWindow()
        .setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
  }

  @Override
  public void hideProgress() {
    progressBar.setVisibility(GONE);
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
    super.onBackPressed();
    overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
  }
  /** ******************************************************************************************* */
  @OnTextChanged(R.id.et_select_address_search)
  public void onTextChanged(CharSequence charSequence, int count) {
    handler.removeCallbacks(input_finish_checker);
    Utility.printLog(TAG + "count: " + charSequence.toString().length());
    if (charSequence.toString().length() == 0) {
      cv_select_address_auto.setVisibility(GONE);
      cv_select_address_recent.setVisibility(recentAddressList.size() > 0 ? VISIBLE : GONE);
    }
  }

  @OnTextChanged(R.id.et_select_address_search)
  public void afterTextChanged(final Editable s) {
    // avoid triggering event when text is empty
    if (s.length() > 0) {
      /*rv_select_address_auto_list.setVisibility(View.VISIBLE);*/
      constraint = s.toString();
      last_text_edit = System.currentTimeMillis();
      handler.postDelayed(input_finish_checker, delay);
    } else {
      placeAutoCompleteAdapter.setData(null);
      placeAutoCompleteAdapter.notifyDataSetChanged();
      /*rv_select_address_auto_list.setVisibility(View.GONE);*/
    }
    /*presenter.toggleFavAddressField(true);*/
  }

  @Override
  public void onConnected(Bundle bundle) {}

  @Override
  public void onConnectionSuspended(int i) {}

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

  @Override
  public void onStop() {
    mGoogleApiClient.disconnect();
    super.onStop();
  }

  @OnClick({R.id.iv_select_address_clear})
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.iv_select_address_clear:
        et_select_address_search.setText("");
        /*presenter.toggleFavAddressField(true);*/
        break;
    }
  }

  @Override
  public void setTitleForDropAddress() {
    iv_select_address_type.setImageDrawable(
        getResources().getDrawable(R.drawable.shape_box_square_red));
  }

  @Override
  public void showFavAddressListUI() {
    if (cv_select_address_auto.getVisibility() == VISIBLE)
      cv_select_address_auto.setVisibility(GONE);
    iv_select_address_clear.setVisibility(GONE);
  }

  @Override
  public void hideFavAddressListUI() {
    iv_select_address_clear.setVisibility(VISIBLE);
    rv_select_address_auto_list.setAdapter(placeAutoCompleteAdapter);
    placeAPI_Count++;
  }

  @Override
  public void goToLogin(String errMsg) {
    DialogHelper.customAlertDialogSignupSuccess(this, message, errMsg, ok);
  }

  @Override
  public void meterBookingSuccess(BookingAssignedDataRideAppts bookingAssignedDataRideAppts) {
    new Utility().startMeterBookingActivity(bookingAssignedDataRideAppts,this);
  }

  @Override
  public void hideSoftKeyboard() {
    View view = this.getCurrentFocus();
    if (view != null) {
      InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }
}
