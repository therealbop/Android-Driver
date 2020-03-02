package com.karry.twilio_call;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;


import com.heride.partner.R;
import com.karry.utility.AppPermissionsRunTime;
import com.karry.utility.CircleTransform;
import com.karry.utility.Utility;
import com.squareup.picasso.Picasso;
import com.twilio.client.Connection;
import com.twilio.client.ConnectionListener;
import com.twilio.client.Device;
import com.twilio.client.DeviceListener;
import com.twilio.client.PresenceEvent;
import com.twilio.client.Twilio;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.karry.utility.VariableConstant.PERMISSION_DENIED;
import static com.karry.utility.VariableConstant.PERMISSION_GRANTED;
import static com.karry.utility.VariableConstant.PHONE_FROM;
import static com.karry.utility.VariableConstant.PHONE_IMAGE_URL;
import static com.karry.utility.VariableConstant.PHONE_NUMBER;
import static com.karry.utility.VariableConstant.PHONE_TO;

/**
 * <h1>ClientActivity</h1>
 * used to open the twiilio calling activity
 */
public class ClientActivity extends DaggerAppCompatActivity implements DeviceListener, ConnectionListener
        , ClientActivityContract.ClientView
{
    private static final String TAG = "ClientActivity";
    @Inject ClientActivityContract.ClientPresenter clientPresenter;
    @Inject AppPermissionsRunTime appPermissionsRunTime;

    @BindView(R.id.callee_image_iv) ImageView callee_image_iv;
    @BindView(R.id.mute_action_fab) ImageView muteActionFab;
    @BindView(R.id.speaker_action_fab) ImageView speakerActionFab;
    @BindView(R.id.hangup_action_fab) ImageView hangupActionFab;
    @BindView(R.id.chronometer) Chronometer chronometer;
    @BindView(R.id.call_layout) View callView;
    @BindString(R.string.micro_phone_permission) String micro_phone_permission;
    @BindString(R.string.failed_to_init_call) String failed_to_init_call;
    @BindString(R.string.device_error) String device_error;
    @BindString(R.string.no_device) String no_device;
    @BindString(R.string.bad_gateway) String bad_gateway;

    private boolean muteMicrophone;
    private boolean speakerPhone;
    private String mob;
    private static final int MIC_PERMISSION_REQUEST_CODE = 1;
    private Device clientDevice;
    private Connection activeConnection;
    private Connection pendingConnection;
    private AudioManager audioManager;
    private int savedAudioMode = AudioManager.MODE_INVALID;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twilio_client);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null)
        {
            mob = intent.getStringExtra(PHONE_NUMBER);
            imageUrl = intent.getStringExtra(PHONE_IMAGE_URL);
        }

        if (imageUrl != null && !imageUrl.equals(""))
            Picasso.get().load(imageUrl).transform(new CircleTransform()).into(callee_image_iv);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (appPermissionsRunTime.checkIfPermissionNeeded())
        {
            if(appPermissionsRunTime.checkIfPermissionGrant(Manifest.permission.RECORD_AUDIO,this))
                initializeTwilioClientSDK();
            else
            {
                String[] strings = {Manifest.permission.RECORD_AUDIO};
                appPermissionsRunTime.requestForPermission(strings,this,MIC_PERMISSION_REQUEST_CODE);
            }
        }
        else
            initializeTwilioClientSDK();

        setCallAction();
    }


    @Override
    public void showToast()
    {
        Toast.makeText(this,bad_gateway,Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case MIC_PERMISSION_REQUEST_CODE:
                Utility.printLog(TAG+" onRequestPermissionsResult "+requestCode);
                int status =  appPermissionsRunTime.getPermissionStatusCall(this,
                        Manifest.permission.RECORD_AUDIO, true);
                Utility.printLog(TAG+" onRequestPermissionsResult "+status);
                switch (status)
                {
                    case PERMISSION_GRANTED:
                        initializeTwilioClientSDK();
                        break;

                    case PERMISSION_DENIED:
                        onBackPressed();
                        break;
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    /**
     * <h>Initialize Twilio</h>
     * <p>Initializing the Twilio Client SDK</p>
     */
    private void initializeTwilioClientSDK() {
        if (!Twilio.isInitialized()) {
            Twilio.initialize(getApplicationContext(), new Twilio.InitListener() {
                @Override
                public void onInitialized() {
                    Twilio.setLogLevel(Log.DEBUG);
                    clientPresenter.getCapabilityToken(mob);
                }

                @Override
                public void onError(Exception e)
                {
                    Utility.printLog(TAG+" onError "+e);
                    Toast.makeText(ClientActivity.this, failed_to_init_call, Toast.LENGTH_LONG).show();
                }
            });
        } else
            clientPresenter.getCapabilityToken(mob);
    }


    @Override
    public void createDevice(String capabilityToken) {
        try {
            if (clientDevice == null) {
                clientDevice = Twilio.createDevice(capabilityToken, this);
                Intent intent = new Intent(getApplicationContext(), ClientActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                clientDevice.setIncomingIntent(pendingIntent);
            } else {
                clientDevice.updateCapabilityToken(capabilityToken);
            }
            connect(mob);

        } catch (Exception e) {
            Toast.makeText(ClientActivity.this, device_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            Device device = intent.getParcelableExtra(Device.EXTRA_DEVICE);
            Connection incomingConnection = intent.getParcelableExtra(Device.EXTRA_CONNECTION);
            if (incomingConnection == null && device == null) {
                return;
            }
            intent.removeExtra(Device.EXTRA_DEVICE);
            intent.removeExtra(Device.EXTRA_CONNECTION);

            pendingConnection = incomingConnection;
            assert pendingConnection != null;
            pendingConnection.setConnectionListener(this);

        }
    }

    /*
     * Receive intent for incoming call from Twilio Client Service
     * Android will only call Activity.onNewIntent() if `android:launchMode` is set to `singleTop`.
     */
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /**
     * <h>Connect to Call</h>
     * <p>Create an outgoing connection</p>
     * @param contact contact number to make outgoing call
     */
    private void connect(String contact) {

        Map<String, String> params = new HashMap<>();
        params.put(PHONE_TO, contact);
        params.put(PHONE_FROM, "RideIn");
        if (clientDevice != null) {
            activeConnection = clientDevice.connect(params, this);
            setCallUI();
        } else {
            Toast.makeText(ClientActivity.this, no_device, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * <H>Disconnect the call</H>
     * <p>Disconnecting an active connection</p>
     */
    private void disconnect() {
        if (activeConnection != null) {
            activeConnection.disconnect();
            activeConnection = null;
        }
    }

    /**
     * <h>Set call action</h>
     * <P>The initial state when there is no active connection</P>
     */
    private void setCallAction() {
        hangupActionFab.setOnClickListener(hangupActionFabClickListener());
        muteActionFab.setOnClickListener(muteMicrophoneFabClickListener());
        speakerActionFab.setOnClickListener(toggleSpeakerPhoneFabClickListener());
    }


    /**
     * <h>Ui Reset method</h>
     * <p>this method is usig to Reset UI elements</p>
     */
    private void resetUI()
    {
        muteMicrophone = false;
        speakerPhone = false;
        muteActionFab.setImageDrawable(ContextCompat.getDrawable(ClientActivity.this, R.drawable.ic_mic_green_24px));
        speakerActionFab.setImageDrawable(ContextCompat.getDrawable(ClientActivity.this, R.drawable.ic_speaker_off_black_24dp));
        setAudioFocus(false);
        audioManager.setSpeakerphoneOn(speakerPhone);
        chronometer.stop();
    }

    /**
     * <h>Set Call Ui</h>
     * <P>The UI state when there is an active connection</P>
     */
    private void setCallUI() {
        hangupActionFab.setVisibility(View.VISIBLE);
        callView.setVisibility(View.VISIBLE);
        chronometer.setVisibility(View.VISIBLE);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private View.OnClickListener muteMicrophoneFabClickListener() {
        return v -> {
            muteMicrophone = !muteMicrophone;
            if (activeConnection != null) {
                activeConnection.setMuted(muteMicrophone);
            }

            if (muteMicrophone) {
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(ClientActivity.this, R.drawable.ic_mic_off_red_24px));
            } else {
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(ClientActivity.this, R.drawable.ic_mic_green_24px));
            }
        };
    }

    private View.OnClickListener toggleSpeakerPhoneFabClickListener() {
        return v ->
        {
            speakerPhone = !speakerPhone;
            setAudioFocus(true);
            audioManager.setSpeakerphoneOn(speakerPhone);
            if (speakerPhone) {
                speakerActionFab.setImageDrawable(ContextCompat.getDrawable(ClientActivity.this, R.drawable.ic_speaker_on_black_24dp));
            } else {
                speakerActionFab.setImageDrawable(ContextCompat.getDrawable(ClientActivity.this, R.drawable.ic_speaker_off_black_24dp));
            }
        };
    }

    private View.OnClickListener hangupActionFabClickListener() {
        return v ->
        {
            disconnect();
            resetUI();
            finish();
        };
    }

    @Override
    public void onStartListening(Device device) {

    }

    @Override
    public void onStopListening(Device device) {

    }

    @Override
    public void onStopListening(Device device, int errorCode, String error) {

    }

    @Override
    public boolean receivePresenceEvents(Device device) {

        return false;
    }

    @Override
    public void onPresenceChanged(Device device, PresenceEvent presenceEvent) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        clientPresenter.dispose();
    }

    @Override
    public void onConnecting(Connection connection) {

    }

    @Override
    public void onConnected(Connection connection) {

    }

    @Override
    public void onDisconnected(Connection connection) {
        if (connection == pendingConnection) {
            pendingConnection = null;
        } else if (activeConnection != null && connection != null) {
            if (activeConnection == connection) {
                activeConnection = null;
                Completable.complete()
                        .delay(10, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .doOnComplete(() -> {
                            resetUI();
                            finish();
                        })
                        .subscribe();
            }
        }
    }

    @Override
    public void onDisconnected(Connection connection, int errorCode, String error) {

        if (activeConnection != null && connection != null) {
            if (activeConnection == connection) {
                activeConnection = null;


                Completable.complete()
                        .delay(10, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .doOnComplete(() -> {
                            resetUI();
                            finish();
                        })
                        .subscribe();
            }
        }
    }

    /**
     * <h>Setting AudioFocus</h>
     * <p> this methos is using to set Audio Mode</p>
     * @param setFocus wheather to focus or not
     */
    private void setAudioFocus(boolean setFocus)
    {
        if (audioManager != null)
        {
            if (setFocus) {
                savedAudioMode = audioManager.getMode();
                audioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(savedAudioMode);
                audioManager.abandonAudioFocus(null);
            }
        }
    }
}
