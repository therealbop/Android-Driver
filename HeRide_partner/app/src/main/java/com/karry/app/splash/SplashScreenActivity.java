package com.karry.app.splash;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.PowerManager;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.karry.app.MyApplication;
import com.karry.app.mainActivity.MainActivity;
import com.karry.authentication.login.LoginActivity;
import com.karry.network.NetworkErrorDialog;
import com.heride.partner.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import io.fabric.sdk.android.Fabric;

import static com.karry.utility.VariableConstant.ADVERTISE_DETAILS;

public class SplashScreenActivity extends DaggerAppCompatActivity implements
        SplashScreenContract.SplashScreenView {
    private static final int IGNORE_BATTERY_OPTIMIZATION_REQUEST = 78;
    boolean firstTime=true;
    private String advertiseData;

    @Inject SplashScreenContract.SplashScreenPresenter splashScreenPresenter;
    @Inject NetworkErrorDialog networkErrorDialog;
    public static boolean splashActivity_opened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        advertiseData = getIntent().getStringExtra("data");
       // openBatteryOptimizeDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        splashActivity_opened = true;
//        splashScreenPresenter.subscribeNetworkObserver();
//        if(firstTime){firstTime=false;
//            MyApplication.getInstance().connectMQTT();
//
//
//            Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
//            splashScreenPresenter.checkConfiguration();
//        }

        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        splashScreenPresenter.checkConfiguration();
    }

    @Override
    protected void onPause() {
        super.onPause();
        splashActivity_opened = false;
        splashScreenPresenter.clear();
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        intent.putExtra(ADVERTISE_DETAILS,advertiseData);
        startActivity(intent);
        finish();
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        hideNetworkCheckPopup();
    }

    @Override
    public void networkNotAvailable() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(splashActivity_opened && networkErrorDialog!=null && !networkErrorDialog.isShowing())
                    networkErrorDialog.show();
            }
        });

    }

    @Override
    public void networkAvailable() {
        hideNetworkCheckPopup();
    }

    private void  hideNetworkCheckPopup(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(networkErrorDialog!=null && networkErrorDialog.isShowing()) {
                    networkErrorDialog.dismiss();

                }
            }
        });
    }
}
