package com.karry.app.mainActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.karry.app.MyApplication;
import com.karry.app.adv.AdvertiseActivity;
import com.karry.app.bookingRequest.BookingPopUpActivity;
import com.karry.authentication.login.LoginActivity;
import com.karry.network.ConnectivityReceiver;
import com.karry.pojo.AdvertiseData;
import com.karry.side_screens.bankDetails.BankDetailsFragment;
import com.heride.partner.R;
import com.karry.side_screens.help_center.zendeskHelpIndex.ZendeskHelpIndex;
import com.karry.side_screens.history.HistoryFragment;
import com.karry.side_screens.home_fragment.HomeFragment;
import com.karry.side_screens.invite.InviteFragment;
import com.karry.network.NetworkErrorDialog;
import com.karry.side_screens.live_chat.LiveChatFragment;
import com.karry.side_screens.portal.PortalActivity;
import com.karry.side_screens.prefered_zone.PreferedZoneListActivity;
import com.karry.side_screens.profile.MyProfileFrag;
import com.karry.service.LocationUpdateService;
import com.karry.side_screens.support.SupportFragment;
import com.karry.utility.ActivityUtils;
import com.karry.utility.AppConstants;
import com.karry.utility.AppTypeFace;
import com.karry.utility.CircleImageView;
import com.karry.utility.CircleTransformation;
import com.karry.utility.DialogHelper;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.karry.side_screens.wallet.WalletFragment;
import com.livechatinc.inappchat.ChatWindowFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.karry.utility.VariableConstant.ADVERTISE_DETAILS;
import static com.karry.utility.VariableConstant.BOOKING_FLOW_OPEN;


public class MainActivity extends DaggerAppCompatActivity
        implements MainActivityContract.MainActivityView, NavigationView.OnNavigationItemSelectedListener,
        EasyPermissions.PermissionCallbacks, ConnectivityReceiver.ConnectivityReceiverListener  {

    private static final String TAG = MainActivity.class.getName();
    private CircleImageView iv_profile;
    private TextView  tv_viewpof,tv_prof_name;
    private RelativeLayout rl_profile_view;
    private final static String NAV_ITEM_ID = "NAV_ITEM_ID";
    public static boolean mainActivity_opened = false;

    @BindView(R.id.tv_network) TextView tv_network;

    @BindView(R.id.iv_main_button) ImageView button_menu;
    @BindView(R.id.toolbar_main_menu) ImageView toolbarMenu;
    @BindView(R.id.tv_main_title) TextView tvTitle;
    @BindView(R.id.tv_main_title2) TextView tvTitle2;
    @BindView(R.id.tv_prof_edit) TextView tv_prof_edit;
    @BindView(R.id.rl_main_menu_layout) RelativeLayout menu_layout;
    @BindView(R.id.tv_main_version) TextView tvVersion ;
    @BindView(R.id.drawer_main_layout) DrawerLayout drawer ;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.navView_main) NavigationView navigationView;
    @BindView(R.id.tv_main_onOff_statas ) TextView  tv_main_onOff_statas;
    @BindView(R.id.swtch_toolbar_mode) SwitchCompat swtch_toolbar_mode;
    @BindString(R.string.version) String version;
    @BindString(R.string.loading) String loading;
    @BindString(R.string.offline) String offline;
    @BindString(R.string.online) String online;
    @BindString(R.string.plsAddStripeFirst) String plsAddStripeFirst;
    @BindString(R.string.bank_details) String bank_details;
    @BindString(R.string.wallet) String wallet;
    @BindString(R.string.invite) String invite;
    @BindString(R.string.support) String support;
    @BindString(R.string.support_content) String support_content;
    @BindString(R.string.history) String history;
    @BindString(R.string.add) String add;
    @BindString(R.string.profile) String profile;
    @BindString(R.string.app_name_withoutSpace) String appName;
    @BindString(R.string.location_permission_message) String location_permission_message;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.iv_portal) ImageView iv_portal;

    @BindString(R.string.live_chat) String live_chat;
    @BindString(R.string.LIVE_CHAT_LICENCE_NUMBER) String LIVE_CHAT_LICENCE_NUMBER;
    @Inject    LiveChatFragment liveChatFragment;

    private Fragment fragment;
    private AppBarLayout abarMain;
    private String currentVersion = "";
    private boolean doubleBackToExitPressedOnce = false;
    private Dialog mandatoryFieldPopup;


    @Inject AppTypeFace appTypeFace;
    @Inject MyProfileFrag myProfileFrag;
    @Inject InviteFragment inviteFragment;
    @Inject SupportFragment supportFragment;
    @Inject BankDetailsFragment bankDetailsFragment;
    @Inject WalletFragment walletFragment;
    @Inject HomeFragment homeFragment;
    @Inject HistoryFragment historyFragment;
    @Inject ActivityUtils activityUtils;
    @Inject MainActivityContract.MainActivityPresenter mainActivityPresenter;
    @Inject NetworkErrorDialog networkErrorDialog;
    @Inject DialogHelper dialogHelper;
    boolean  firstTime=true;
    private IntentFilter filter;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        ButterKnife.bind(this);
        try {
            currentVersion = MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        initVar();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mainActivityPresenter.getAppConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainActivity_opened = true;

        MyApplication.getInstance().setConnectivityListener(this);




        mainActivityPresenter.subscribeNetworkObserver();

        /*if(firstTime){
            firstTime=false;
            MyApplication.getInstance().connectMQTT();
        }*/

        //mainActivityPresenter.networkCheckOnresume();
        checkAndRequestPermissions();
        Utility.printLog(TAG + " onResume...");

        try {
            filter = new IntentFilter();
            filter.addAction("android.intent.action.MAIN."+appName);
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    mainActivityPresenter.getBroadCastReciever(intent);
                }
            };
            registerReceiver(receiver,filter);
            mainActivityPresenter.getBroadCastReciever(getIntent());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mainActivity_opened = false;
        mainActivityPresenter.clear();

        if(mandatoryFieldPopup!=null)
            if (mandatoryFieldPopup.isShowing())
                mandatoryFieldPopup.dismiss();
    }

    @Override
    public void networkNotAvailable() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mainActivity_opened && networkErrorDialog!=null && !networkErrorDialog.isShowing())
                    networkErrorDialog.show();
            }
        });

    }

    @Override
    public void networkAvailable() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(networkErrorDialog!=null && networkErrorDialog.isShowing()) {
                    networkErrorDialog.dismiss();
                    if(getFragmentRefreshListener()!=null){
                        getFragmentRefreshListener().onRefresh();
                    }
                }
            }
        });
    }

    @Override
    public void mandatoryUpdateDialog(boolean isMandatoryUpdateEnable) {
        mandatoryFieldPopup = DialogHelper.mandatoryUpdateDialog(this,isMandatoryUpdateEnable);
        if(mandatoryFieldPopup!=null && !mandatoryFieldPopup.isShowing()) {
            mandatoryFieldPopup.show();
        }
    }

    @Override
    public void onSuccesLogout() {
        stopUpdateLocation();
        Intent intentLogin = new Intent(this, LoginActivity.class);
        intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentLogin);
    }


    @Override
    public void openBookingPopUp(String data) {
        Intent intent = new Intent(this, BookingPopUpActivity.class);
        intent.putExtra("BOOKING_DATA", data);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * <h1>stopUpdateLocation</h1>
     * <p>if the forground service running need to stop</p>
     */
    private void stopUpdateLocation()
    {
        if(Utility.isMyServiceRunning(LocationUpdateService.class,this)){
            Intent stopIntent = new Intent(this, LocationUpdateService.class);
            stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
            startService(stopIntent);
        }

    }

    @OnCheckedChanged(R.id.swtch_toolbar_mode)
    public void onCheckedListner(boolean isChecked)
    {
        if(isChecked) {

            tv_main_onOff_statas.setText(online);
            tv_main_onOff_statas.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            tv_main_onOff_statas.setText(offline);
            tv_main_onOff_statas.setTextColor(getResources().getColor(R.color.red_light));

        }
    }

    /**
     * custom method to check and request for run time permissions
     * if not granted already
     */
    public void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (EasyPermissions.hasPermissions(this, perms)) {
                onPermissionGranted();
            } else {
                EasyPermissions.requestPermissions(this, location_permission_message,
                        VariableConstant.RC_LOCATION_STATE, perms);
            }

        } else {
            onPermissionGranted();
        }
    }

    /**
     * custom method to execute after run time permissions
     * granted or if it run time permission no required at all
     */
    public void onPermissionGranted() {
        if(homeFragment!=null && homeFragment.isAdded() && HomeFragment.homeFrag){
            homeFragment.onPermissionGranted();
        }
    }


    /**
     * predefined method to check run time permissions list call back
     *
     * @param requestCode   : to handle the corresponding request
     * @param permissions:  contains the list of requested permissions
     * @param grantResults: contains granted and un granted permissions result list
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * <h1>initVar</h1>
     * <p>initilize the Views</p>
     */
    private void initVar() {

        //Change 'YourConnectionChangedBroadcastReceiver'
        //to the class defined to handle the broadcast in your app
        registerReceiver(new ConnectivityReceiver(),
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        String dataObject = getIntent().getStringExtra(ADVERTISE_DETAILS);
        if(dataObject == null)
            dataObject = getIntent().getStringExtra("data");
        Utility.printLog(TAG+" advertise string "+dataObject);
        AdvertiseData advertiseData = new Gson().fromJson(dataObject, AdvertiseData.class);
        if(advertiseData!=null)
        {
            Intent intent = new Intent(this, AdvertiseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ADVERTISE_DETAILS,advertiseData);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        dialogHelper.getDialogCallbackHelper(new DialogHelper.DialogCallbackHelper() {

            @Override
            public void walletFragOpen() {
                new Handler().post(new Runnable() {
                    public void run() {
                        iv_portal.setVisibility(View.GONE);
                        BOOKING_FLOW_OPEN = false;
                        fragment = walletFragment;
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_main_container, fragment)
                                .commit();
                        /*activityUtils.addFragmentToActivity(walletFragment, R.id.frame_main_container);*/
                        tvTitle.setText(wallet);
                        tv_prof_edit.setVisibility(View.GONE);
                        tvTitle2.setVisibility(View.GONE);
                        tvTitle.setVisibility(View.VISIBLE);
                        tv_prof_edit.setVisibility(View.GONE);
                        menu_layout.setVisibility(View.GONE);
                        abarMain.setVisibility(View.VISIBLE);
                        button_menu.setImageResource(R.drawable.selector_hamburger_white);
                    }
                });
            }

            @Override
            public void changeLanguage(String langCode, String langName, int dir) {

            }

        });

        dialogHelper.getDialogLogoutCallbackHelper(new DialogHelper.DialogLogoutCallBackHelper() {
            @Override
            public void Logout() {
                mainActivityPresenter.logout();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_network.setTypeface(appTypeFace.getPro_narMedium());

        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(loading);
        pDialog.setCancelable(false);
        abarMain = findViewById(R.id.abl_main);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        tvVersion.setTypeface(appTypeFace.getPro_News());
        tvVersion.setText(version+ ": " + currentVersion);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {

                Menu menu = navigationView.getMenu();
                MenuItem menuItem = menu.getItem(2);
                menuItem.setTitle(mainActivityPresenter.getWalletAmount());

                rl_profile_view = drawerView.findViewById(R.id.rl_profile_view);
                rl_profile_view.setOnClickListener(view -> {
                    drawer.closeDrawer(GravityCompat.START);
                    activityUtils.addFragmentToActivity(myProfileFrag, R.id.frame_main_container);
                    tv_prof_edit.setVisibility(View.GONE);
                    tvTitle.setText(profile);
                    tvTitle.setVisibility(View.VISIBLE);
                    tvTitle2.setVisibility(View.GONE);
                    menu_layout.setVisibility(View.GONE);
                    abarMain.setVisibility(View.VISIBLE);
                    button_menu.setImageResource(R.drawable.selector_hamburger_white);
                });

                tv_viewpof = drawerView.findViewById(R.id.tv_viewpof);
                tv_prof_name = drawerView.findViewById(R.id.tv_prof_name);
                iv_profile = drawerView.findViewById(R.id.iv_cir_profile);

                tv_viewpof.setTypeface(appTypeFace.getClanaproNarrBook());
                tv_prof_name.setTypeface(appTypeFace.getPro_narMedium());
                mainActivityPresenter.getProfilePicImg();
                boolean zenDeskCheck = mainActivityPresenter.checkZenDesk();
                MenuItem menuItemZendesk = menu.getItem(8);
                menuItemZendesk.setVisible(zenDeskCheck);

            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }

        };

        drawer.addDrawerListener(toggle);

        fragment = homeFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_main_container, fragment)
                .commit();
        tv_prof_edit.setTypeface(appTypeFace.getPro_narMedium());

        tvTitle.setTypeface(appTypeFace.getPro_narMedium());
        tvTitle2.setTypeface(appTypeFace.getPro_narMedium());

        menu_layout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.transparent));
        abarMain.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Utility.BlueToast(this,getResources().getString(R.string.exit_double_back));
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                iv_portal.setVisibility(View.VISIBLE);
                activityUtils.addFragmentToActivity(homeFragment, R.id.frame_main_container);
                tvTitle.setVisibility(View.GONE);
                tvTitle2.setVisibility(View.GONE);
                tv_prof_edit.setVisibility(View.GONE);
                button_menu.setImageResource(R.drawable.selector_hamburger);
                menu_layout.setVisibility(View.VISIBLE);
                abarMain.setVisibility(View.GONE);
                menu_layout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.transparent));
                break;

            case R.id.nav_history:
                iv_portal.setVisibility(View.GONE);
                BOOKING_FLOW_OPEN  = false;
                activityUtils.addFragmentToActivity(historyFragment, R.id.frame_main_container);
                tv_prof_edit.setVisibility(View.GONE);
                tvTitle.setText(history);
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle2.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);
                break;

            case R.id.nav_support:
                iv_portal.setVisibility(View.GONE);
                BOOKING_FLOW_OPEN  = false;
                activityUtils.addFragmentToActivity(supportFragment, R.id.frame_main_container);
                tv_prof_edit.setVisibility(View.GONE);
                tvTitle.setText(support_content);
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle2.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);
                break;

            case R.id.nav_invite:
                iv_portal.setVisibility(View.GONE);
                BOOKING_FLOW_OPEN  = false;
                activityUtils.addFragmentToActivity(inviteFragment, R.id.frame_main_container);
                tvTitle.setText(invite);
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle2.setVisibility(View.GONE);
                tv_prof_edit.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);
                break;

            case R.id.nav_bank_details:
                iv_portal.setVisibility(View.GONE);
                BOOKING_FLOW_OPEN  = false;
                activityUtils.addFragmentToActivity(bankDetailsFragment, R.id.frame_main_container);
                tvTitle.setText(bank_details);
                tv_prof_edit.setText(add);
                tvTitle2.setVisibility(View.GONE);
                tvTitle.setVisibility(View.VISIBLE);
                tv_prof_edit.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);
                break;

            case R.id.nav_payment:
                iv_portal.setVisibility(View.GONE);
                BOOKING_FLOW_OPEN  = false;
                activityUtils.addFragmentToActivity(walletFragment, R.id.frame_main_container);
                tvTitle.setText(wallet);
                tv_prof_edit.setVisibility(View.GONE);
                tvTitle2.setVisibility(View.GONE);
                tvTitle.setVisibility(View.VISIBLE);
                tv_prof_edit.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);
                break;


            case R.id.nav_live_chat:
                iv_portal.setVisibility(View.GONE);
                BOOKING_FLOW_OPEN  = false;
                /*tvTitle.setText(live_chat);
                tv_prof_edit.setVisibility(View.GONE);
                tvTitle2.setVisibility(View.GONE);
                tvTitle.setVisibility(View.VISIBLE);
                tv_prof_edit.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);
                activityUtils.addFragmentToActivity(liveChatFragment, R.id.frame_main_container);
                mainActivityPresenter.getLiveChat();*/
                Intent intent = new Intent(this, com.livechatinc.inappchat.ChatWindowActivity.class);
                intent.putExtra(com.livechatinc.inappchat.ChatWindowActivity.KEY_GROUP_ID, appName);
                intent.putExtra(com.livechatinc.inappchat.ChatWindowActivity.KEY_LICENCE_NUMBER, LIVE_CHAT_LICENCE_NUMBER);
                startActivity(intent);
                break;

            case R.id.item_menu_nav_helpCenter:
                BOOKING_FLOW_OPEN  = false;
                startActivity(new Intent(this, ZendeskHelpIndex.class));
                break;


            case R.id.nav_portal:
                BOOKING_FLOW_OPEN  = false;
                startActivity(new Intent(this, PortalActivity.class));
                break;

            case R.id.nav_logout:
                BOOKING_FLOW_OPEN  = false;
                DialogHelper.customAlertDialogLogout( this);
                break;

        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @OnClick({R.id.iv_main_button,R.id.toolbar_main_menu,R.id.iv_portal})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_main_button:
            case R.id.toolbar_main_menu: {
                mainActivityPresenter.hideKeyboardAndClearFocus();
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
            break;
            case R.id.iv_portal:
                BOOKING_FLOW_OPEN  = false;
                startActivity(new Intent(this, PreferedZoneListActivity.class));
                break;



           /* case R.id.iv_portal:
                BOOKING_FLOW_OPEN  = false;
                startActivity(new Intent(this, PortalActivity.class));
                break;*/


            /*case R.id.rl_profile_view:
                drawer.closeDrawer(GravityCompat.START);
                activityUtils.addFragmentToActivity(myProfileFrag, R.id.frame_main_container);
                tv_prof_edit.setVisibility(View.GONE);
                tvTitle.setText(svg_profile);
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle2.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);
                break;*/

        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        onPermissionGranted();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else if (requestCode == VariableConstant.RC_LOCATION_STATE) {
            EasyPermissions.requestPermissions(MainActivity.this, location_permission_message
                    , VariableConstant.RC_LOCATION_STATE, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void setProfilePicImg(String profilePic, String name) {


        Picasso.get()
                .load(profilePic)
                .placeholder(R.drawable.signup_profile_default_image)
                .transform(new CircleTransformation())
                .into(iv_profile);
        tv_prof_name.setText(name);
    }

    /**********************************************************************************************/
    @Override
    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void showSoftKeyboard() {
        getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

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


    /**
     *for refresh the fragments after network check
     */
    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        mainActivityPresenter.checkForNetwork(isConnected);

    }

    public interface FragmentRefreshListener{
        void onRefresh();
    }

    @Override
    public void openLiveChat(String LIVE_CHAT_NAME) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_container, ChatWindowFragment.newInstance(LIVE_CHAT_LICENCE_NUMBER,
                        appName,LIVE_CHAT_NAME,""))
                .commit();
    }
}
