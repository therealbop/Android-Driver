package com.karry.telecall.callservice;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.manager.mqtt_manager.MQTTManager;
import com.karry.manager.mqtt_manager.MqttEvents;
import com.heride.partner.R;
import com.karry.telecall.UtilityVideoCall;
import com.karry.telecall.audiocall.AudioCallContract;
import com.karry.telecall.model.CallActions;
import com.karry.telecall.utility.AppRTCAudioManager;
import com.karry.telecall.utility.AppRTCClient;
import com.karry.telecall.utility.LooperExecutor;
import com.karry.telecall.utility.OnMyService;
import com.karry.telecall.utility.PeerConnectionClient;
import com.karry.telecall.utility.RxCallInfo;
import com.karry.telecall.utility.TextDrawable;
import com.karry.telecall.utility.WebSocketRTCClient;
import com.karry.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;
import org.webrtc.StatsReport;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.karry.telecall.callservice.VideoCallService.EXTRA_CALL_ID;


/**
 * Created by moda on 16/08/17.
 */

public class AudioCallService extends DaggerService implements AudioCallContract.View, AppRTCClient.SignalingEvents,
        PeerConnectionClient.PeerConnectionEvents,
        AudioCallEvents {


    @Inject AudioCallContract.Presenter presenter;

    public static final String EXTRA_ROOMID = "org.appspot.apprtc.ROOMID";
    public static final String EXTRA_LOOPBACK = "org.appspot.apprtc.LOOPBACK";
    public static final String EXTRA_VIDEO_CALL = "org.appspot.apprtc.VIDEO_CALL";
    public static final String EXTRA_VIDEO_WIDTH = "org.appspot.apprtc.VIDEO_WIDTH";
    public static final String EXTRA_VIDEO_HEIGHT = "org.appspot.apprtc.VIDEO_HEIGHT";
    public static final String EXTRA_VIDEO_FPS = "org.appspot.apprtc.VIDEO_FPS";

    public static final String EXTRA_VIDEO_BITRATE = "org.appspot.apprtc.VIDEO_BITRATE";
    public static final String EXTRA_VIDEOCODEC = "org.appspot.apprtc.VIDEOCODEC";
    public static final String EXTRA_HWCODEC_ENABLED = "org.appspot.apprtc.HWCODEC";
    public static final String EXTRA_CAPTURETOTEXTURE_ENABLED = "org.appspot.apprtc.CAPTURETOTEXTURE";
    public static final String EXTRA_AUDIO_BITRATE = "org.appspot.apprtc.AUDIO_BITRATE";
    public static final String EXTRA_AUDIOCODEC = "org.appspot.apprtc.AUDIOCODEC";
    public static final String EXTRA_NOAUDIOPROCESSING_ENABLED =
            "org.appspot.apprtc.NOAUDIOPROCESSING";
    public static final String EXTRA_AECDUMP_ENABLED = "org.appspot.apprtc.AECDUMP";
    public static final String EXTRA_OPENSLES_ENABLED = "org.appspot.apprtc.OPENSLES";
    public static final String EXTRA_DISPLAY_HUD = "org.appspot.apprtc.DISPLAY_HUD";
    public static final String EXTRA_TRACING = "org.appspot.apprtc.TRACING";
    public static final String EXTRA_CMDLINE = "org.appspot.apprtc.CMDLINE";
    public static final String EXTRA_RUNTIME = "org.appspot.apprtc.RUNTIME";
    private static final String TAG = "CallRTCClient";

    // List of mandatory application permissions.
    private static final String[] MANDATORY_PERMISSIONS = {
            "android.permission.MODIFY_AUDIO_SETTINGS", "android.permission.RECORD_AUDIO",
            "android.permission.INTERNET"
    };

    // Peer connection statistics callback period in ms.
    private static final int STAT_CALLBACK_PERIOD = 1000;

    private PeerConnectionClient peerConnectionClient = null;
    private AppRTCClient appRtcClient;
    private AppRTCClient.SignalingParameters signalingParameters;
    private AppRTCAudioManager audioManager = null;
    private EglBase rootEglBase;

    private AppRTCClient.RoomConnectionParameters roomConnectionParameters;


    private long callStartedTimeMs = 0;


    //  private static Bus bus = AppController.getBus();


    private boolean disconnect = false;

    /* Media Player Class to enable ring sound */ MediaPlayer mp;

    private CountDownTimer timer;
    private View audioCallView, callHeaderView;

    private Context mContext;


    private Intent intent;
    private Handler handler;


    /**
     * Previously the fragment content to be accessible in service now
     */
    private long countUp, countUpHeader;

    private Chronometer stopWatchHeader;


    private ImageView mute, speaker;
    private TextView tvStopWatch, tvCallerName;


    private boolean isMute = false, isSpeaker = false;

    private ImageView callerImage;


    /**
     * Call control interface for container activity.
     */

    private String imageUrl = "";
//    private ImageView initiateChat;


    private String callDuration, callDurationHeader;


    /**
     * Legacy code although not used as of now
     */
    private boolean isError;
    private boolean iceConnected;
    private boolean commandLineRun;
    private int runTimeMs;
    private WindowManager windowManager;


    private TextView callHeaderTv;
    private ImageView callHeaderIv;

    private WindowManager.LayoutParams params;
   /* PowerManager mgr;
    PowerManager.WakeLock wakeLock;*/


    /****************** i am doing *******************/

    @Inject
    MQTTManager mqttManager;
    @Inject
    PreferenceHelperDataSource manager;
   /* @Inject
    AlertProgress alertProgress;*/

    /************************************************/

    @Override
    public void onCreate() {
        super.onCreate();

        if(!mqttManager.isMQTTConnected())
            mqttManager.createMQttConnection(Utility.getDeviceId(getApplicationContext())+"_"+
                    manager.getMid());

        startService(new Intent(this, OnMyService.class));

        mContext = this;
        audioCallView = LayoutInflater.from(this).inflate(R.layout.audio_call_service, null);
        //  bus.register(this);

       /* mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();*/


        // Set window styles for fullscreen-window size. Needs to be done before
        // adding content.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            audioCallView
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {


            audioCallView
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                    );
        }

        tvStopWatch = audioCallView.findViewById(R.id.tvStopWatch);
        //Add the view to the window.
        addMainLayout();


        handler = new Handler(Looper.getMainLooper());



        iceConnected = false;
        signalingParameters = null;


        rootEglBase = EglBase.create();

        // Check for mandatory permissions.
        for (String permission : MANDATORY_PERMISSIONS) {
            if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                logAndToast("Permission " + permission + " is not granted");


                stopSelf();
                return;
            }
        }

        initializeRxJava();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.intent = intent;

        if (intent != null) {
            if (!intent.getExtras().getBoolean("isIncomingCall", true)) {
                mqttManager.subscribeToTopic(MqttEvents.Call.value + "/" + intent.getExtras().getString(EXTRA_CALL_ID));
                mp = MediaPlayer.create(this, R.raw.calling);
                mp.setLooping(true);
                mp.start();
            } else {
                tvStopWatch.setText(getString(R.string.connecting));
            }
            startCallProcedure();

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {


        this.intent = intent;
        return null;
    }


    @Override
    public void onDestroy() {


        disconnect();
        //  bus.unregister(this);

        rootEglBase.release();

        if (windowManager == null) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        }
        try {
            if (UtilityVideoCall.getInstance().isCallMinimized()) {

                if (callHeaderView != null) {
                    windowManager.removeView(callHeaderView);
                }
            } else {
                if (audioCallView != null) {


                    /*
                     * For clearing of the full screen UI mode
                     */
                    audioCallView
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                    windowManager.removeView(audioCallView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    // OnCallEvents interface implementation.
    @Override
    public void onCallHangUp(int val, boolean received) {

        if (!disconnect) {
            disconnect = true;
            /* Send Reject through call event and call callEnd API */


            /*
             * MQtt
             */
            /*
             * Timeout from the sender side or call canceled from sender side
             */

            if (intent != null) {

                mqttManager.unSubscribeToTopic(MqttEvents.Call.value + "/" + intent.getExtras().getString(EXTRA_CALL_ID));
            }
        }
        disconnect();
    }

    @Override
    public void onMute() {
        isMute = !isMute;
        if (audioManager != null)
            audioManager.setMicrophoneMute(isMute);
    }

    @Override
    public void onSpeaker() {

        isSpeaker = !isSpeaker;
        if (audioManager != null)
            audioManager.setSpeakerphoneOn(isSpeaker);
    }


    private void startCall() {
        if (appRtcClient == null) {

            return;
        }
        callStartedTimeMs = System.currentTimeMillis();

        // Start room connection.
        // logAndToast(getString(R.string.connecting_to, roomConnectionParameters.roomUrl));
        appRtcClient.connectToRoom(roomConnectionParameters);

        // Create and audio manager that will take care of audio routing,
        // audio modes, audio device enumeration etc.
        audioManager = AppRTCAudioManager.create(false, this, new Runnable() {
            // This method will be called each time the audio state (number and
            // type of devices) has been changed.
            @Override
            public void run() {
                onAudioManagerChangedState();
            }
        });
        // Store existing audio settings and change audio mode to
        // MODE_IN_COMMUNICATION for best possible VoIP performance.

        audioManager.init();

        /*
         * To set speaker as default for the audio call
         */
        if (audioManager != null){
            audioManager.setSpeakerphoneOn(false);
            isSpeaker = false;
        }
    }

    // Should be called from UI thread
    private void callConnected() {
        //   final long delta = System.currentTimeMillis() - callStartedTimeMs;

        if (peerConnectionClient == null || isError) {

            return;
        }
        // Update video view.
        // Enable statistics callback.
        peerConnectionClient.enableStatsEvents(true, STAT_CALLBACK_PERIOD);

        try {
            if (mp != null)
                mp.stop();
        } catch (Exception e) {

        }


        timer.cancel();

        /* Start the stop watch */
//        AudioCallFragment.startStopWatch();

        startStopWatch();
    }

    private void onAudioManagerChangedState() {
        // TODO(henrika): disable video if AppRTCAudioManager.AudioDevice.EARPIECE
        // is active.
    }

    // Disconnect from remote resources, dispose of local resources, and exit.
    private void disconnect() {
        stopSelf();

        try {


            /*
             * To make myself available for receiving the new call
             */


            JSONObject obj = new JSONObject();
            obj.put("status", 1);

            mqttManager.publish(MqttEvents.CallsAvailability.value + "/" + manager.getMid(), obj, 0, true);
            UtilityVideoCall.getInstance().setActiveOnACall(false, false);


            timer.cancel();

            if (appRtcClient != null) {
                appRtcClient.disconnectFromRoom();
                appRtcClient = null;
            }
            if (peerConnectionClient != null) {
                peerConnectionClient.close();
                peerConnectionClient = null;
            }
            if (audioManager != null) {
                audioManager.close();
                audioManager = null;
            }


            try {
                if (mp != null)
                    mp.stop();

            } catch (Exception e) {

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void logAndToast(String msg) {


    }


    // -----Implementation of AppRTCClient.AppRTCSignalingEvents ---------------
    // All callbacks are invoked from websocket signaling looper thread and
    // are routed to UI thread.
    private void onConnectedToRoomInternal(final AppRTCClient.SignalingParameters params) {
        // final long delta = System.currentTimeMillis() - callStartedTimeMs;

        signalingParameters = params;
        // logAndToast("Creating peer connection, delay=" + delta + "ms");
      /*  peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), null, null,
                signalingParameters);*/

        peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), null, null, null,
                signalingParameters);


        if (signalingParameters.initiator) {
            //    logAndToast("Creating OFFER...");
            // Create offer. Offer SDP will be sent to answering client in
            // PeerConnectionEvents.onLocalDescription event.
            peerConnectionClient.createOffer();
        } else {
            if (params.offerSdp != null) {
                peerConnectionClient.setRemoteDescription(params.offerSdp);
                //   logAndToast("Creating ANSWER...");
                // Create answer. Answer SDP will be sent to offering client in
                // PeerConnectionEvents.onLocalDescription event.
                peerConnectionClient.createAnswer();
            }
            if (params.iceCandidates != null) {
                // Add remote ICE candidates from room.
                for (IceCandidate iceCandidate : params.iceCandidates) {
                    peerConnectionClient.addRemoteIceCandidate(iceCandidate);
                }
            }
        }
    }

    @Override
    public void onConnectedToRoom(final AppRTCClient.SignalingParameters params) {


        handler.post(new Runnable() {

            @Override
            public void run() {
                onConnectedToRoomInternal(params);
            }
        });
    }

    @Override
    public void onRemoteDescription(final SessionDescription sdp) {
        //     final long delta = System.currentTimeMillis() - callStartedTimeMs;


        handler.post(new Runnable() {
            @Override
            public void run() {
                if (peerConnectionClient == null) {

                    return;
                }
                //   logAndToast("Received remote " + sdp.type + ", delay=" + delta + "ms");
                peerConnectionClient.setRemoteDescription(sdp);
                if (!signalingParameters.initiator) {
                    //     logAndToast("Creating ANSWER...");
                    // Create answer. Answer SDP will be sent to offering client in
                    // PeerConnectionEvents.onLocalDescription event.
                    peerConnectionClient.createAnswer();
                }
            }
        });
    }

    @Override
    public void onRemoteIceCandidate(final IceCandidate candidate) {


        handler.post(new Runnable() {
            @Override
            public void run() {
                if (peerConnectionClient == null) {

                    return;
                }
                peerConnectionClient.addRemoteIceCandidate(candidate);
            }
        });
    }

    @Override
    public void onChannelClose() {


        handler.post(new Runnable() {
            @Override
            public void run() {
                //  logAndToast("Remote end hung up; dropping PeerConnection");
                disconnect();
            }
        });
    }

    @Override
    public void onChannelError(final String description) {
//          reportError(description);
    }

    // -----Implementation of PeerConnectionClient.PeerConnectionEvents.---------
    // Send local peer connection SDP and ICE candidates to remote party.
    // All callbacks are invoked from peer connection client looper thread and
    // are routed to UI thread.
    @Override
    public void onLocalDescription(final SessionDescription sdp) {
        //    final long delta = System.currentTimeMillis() - callStartedTimeMs;


        handler.post(new Runnable() {
            @Override
            public void run() {
                if (appRtcClient != null) {
                    //     logAndToast("Sending " + sdp.type + ", delay=" + delta + "ms");
                    if (signalingParameters.initiator) {
                        appRtcClient.sendOfferSdp(sdp);
                    } else {
                        appRtcClient.sendAnswerSdp(sdp);
                    }
                }
            }
        });
    }

    @Override
    public void onIceCandidate(final IceCandidate candidate) {


        handler.post(new Runnable() {
            @Override
            public void run() {
                if (appRtcClient != null) {
                    appRtcClient.sendLocalIceCandidate(candidate);
                }
            }
        });
    }

    @Override
    public void onIceConnected() {
        //  final long delta = System.currentTimeMillis() - callStartedTimeMs;


        handler.post(new Runnable() {
            @Override
            public void run() {
                //    logAndToast("ICE connected, delay=" + delta + "ms");
                iceConnected = true;
                callConnected();
            }
        });
    }

    @Override
    public void onIceDisconnected() {


        handler.post(new Runnable() {
            @Override
            public void run() {
                //   logAndToast("ICE disconnected");
                iceConnected = false;
                disconnect();
            }
        });
    }

    @Override
    public void onPeerConnectionClosed() {
    }

    @Override
    public void onPeerConnectionStatsReady(final StatsReport[] reports) {
    }

    @Override
    public void onPeerConnectionError(final String description) {
        //reportError(description);
    }


    public void initializeRxJava() {

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String objects) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(objects);
                    onMessage(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        RxCallInfo.getInstance().subscribe(observer);

    }

    private void onMessage(JSONObject object) {



        try {

            if(object.getString("action").equals(CallActions.NOT_ANSWER_OR_LEFT.value)){
                onRejectSuccess();
                UtilityVideoCall.getInstance().setActiveOnACall(false, true);
            }
            else if(object.getString("action").equals(CallActions.JOIN_ON_CALL.value)){
                tvStopWatch.setText(getString(R.string.connecting));
                UtilityVideoCall.getInstance().setActiveOnACall(true, true);
            }
            else if(object.getString("action").equals(CallActions.CALL_ENDED.value)){
                onRejectSuccess();
                UtilityVideoCall.getInstance().setActiveOnACall(false, true);
            }

           /* if (object.getString("eventName").equals(MqttEvents.CallsAvailability.value)) {

                if (intent != null) {


                    Bundle extras = intent.getExtras();

                    if (object.getInt("status") == 0) {
                        *//*
             * Receiver is busy
             *//*


                        if (root != null) {

                            //   alertProgress.alertinfo(this,extras.getString("callerName") + " " + getString(R.string.busy));
                           *//* Snackbar snackbar = Snackbar.make(root, extras.getString("callerName") + " " + getString(R.string.busy), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);*//*
                            Toast.makeText(getApplicationContext(),extras.getString("callerName") + " " + getString(R.string.busy),Toast.LENGTH_SHORT).show();
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                stopSelf();
                            }
                        }, 2000);


                    } else {
                        *//*
             * I put myself as busy and make the call request to the receiver
             *//*


                        try {

                            JSONObject obj = new JSONObject();
                            obj.put("status", 0);


                            mqttManager.publish(MqttEvents.CallsAvailability.value + "/" + manager.getSid(), obj, 0, true);
                            UtilityVideoCall.getInstance().setActiveOnACall(true, true);

                            obj = new JSONObject();

                            obj.put("callerId", manager.getSid());
                            obj.put("callId", extras.getString(EXTRA_ROOMID));
                            obj.put("bookingId", extras.getString("BookingID"));
                            obj.put("callType", "0");
                            obj.put("callerName", manager.getUsername());
                            obj.put("callerImage", manager.getImageUrl());
                            obj.put("callerIdentifier", extras.getString("callerIdentifier"));

                            *//*obj.put("callerId", AppController.getInstance().getUserId());
                            obj.put("callId", extras.getString(EXTRA_ROOMID));
                            obj.put("callType", "0");
                            obj.put("callerName", AppController.getInstance().getUserName());
                            obj.put("callerImage", AppController.getInstance().getUserImageUrl());
                            obj.put("callerIdentifier", AppController.getInstance().getUserIdentifier());*//*

                            obj.put("type", 0);
                            obj.put("userType", 1);
                            *//*
             * CalleeId althought not required but can be used in future on server so using it
             *
             *
             * CalleeId contains the receiverUid,to whom the call has been made
             *
             * *//*



             *//*
                         * Type-0---call initiate request,on receiving the call initiate request,receiver will set his status as busy,so nobody else can call him
                         *
                         * /


                        obj.put("type", 0);
/*
 * Not making any message of call signalling as being persistent intentionally
 *//*

                            Log.d(TAG, "onMessageAudio: "+obj+" callerId "+extras.getString("callerId"));
                                                                                                                     //mqttManager.publish(MqttEvents.Calls.value + "/" + extras.getString("callerId"), obj, 0, false);


                            UtilityVideoCall.getInstance().setActiveCallId(extras.getString(EXTRA_ROOMID));
                            UtilityVideoCall.getInstance().setActiveCallerId(extras.getString("callerId"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }


            } *//*else if (object.getString("eventName").equals(MqttEvents.Calls.value )) {//+ "/" + manager.getSid())

                if (intent != null) {
                    switch (object.getInt("type")) {

                        case 1:
                            tvStopWatch.setText(getString(R.string.connecting));
                            break;
                        case 2:
                            onCallHangUp(2, true);
                            break;
                        case 3:


                            if (root != null) {

                                //   alertProgress.alertinfo(this,getString(R.string.NoAnswer) + " " + intent.getExtras().getString("callerName"));
                               *//**//* Snackbar snackbar = Snackbar.make(root, getString(R.string.NoAnswer) + " " + intent.getExtras().getString("callerName"), Snackbar.LENGTH_SHORT);

                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);*//**//*
                                Toast.makeText(getApplicationContext(),getString(R.string.NoAnswer) + " " + intent.getExtras().getString("callerName"),Toast.LENGTH_SHORT).show();

                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {


                                    stopSelf();
                                }
                            }, 2000);
                            break;

                        case 7:


                            onCallHangUp(7, true);


                    }
                }
            }*//* else if (object.getString("eventName").equals("turnOnScreen")) {

                turnOnScreen();


            } else if (object.getString("eventName").equals("turnOffScreen")) {


                turnOffScreen();

            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startCallProcedure() {

        // Get Intent parameters.


        if (intent != null) {


            final Intent intent = this.intent;


            Uri roomUri = intent.getData();
            if (roomUri == null) {
                //  logAndToast(getString(R.string.missing_url));


                stopSelf();
                return;
            }

            String roomId = intent.getStringExtra(EXTRA_ROOMID);

            Log.d(TAG, "startCallProcedure: roomId"+ roomId);
            if (roomId == null || roomId.length() == 0) {
                //  logAndToast(getString(R.string.missing_url));


                stopSelf();
                return;
            }


            boolean loopback = intent.getBooleanExtra(EXTRA_LOOPBACK, false);
            boolean tracing = intent.getBooleanExtra(EXTRA_TRACING, false);
            PeerConnectionClient.PeerConnectionParameters peerConnectionParameters = new PeerConnectionClient.PeerConnectionParameters(
                    intent.getBooleanExtra(EXTRA_VIDEO_CALL, true), loopback, tracing,
                    intent.getIntExtra(EXTRA_VIDEO_WIDTH, 0), intent.getIntExtra(EXTRA_VIDEO_HEIGHT, 0),
                    intent.getIntExtra(EXTRA_VIDEO_FPS, 0), intent.getIntExtra(EXTRA_VIDEO_BITRATE, 0),
                    intent.getStringExtra(EXTRA_VIDEOCODEC),
                    intent.getBooleanExtra(EXTRA_HWCODEC_ENABLED, true),
                    intent.getBooleanExtra(EXTRA_CAPTURETOTEXTURE_ENABLED, false),
                    intent.getIntExtra(EXTRA_AUDIO_BITRATE, 0), intent.getStringExtra(EXTRA_AUDIOCODEC),
                    intent.getBooleanExtra(EXTRA_NOAUDIOPROCESSING_ENABLED, false),
                    intent.getBooleanExtra(EXTRA_AECDUMP_ENABLED, false),
                    intent.getBooleanExtra(EXTRA_OPENSLES_ENABLED, false));
            commandLineRun = intent.getBooleanExtra(EXTRA_CMDLINE, false);
            runTimeMs = intent.getIntExtra(EXTRA_RUNTIME, 0);

            // Create connection client and connection parameters.
            appRtcClient = new WebSocketRTCClient(this, new LooperExecutor());
            roomConnectionParameters =
                    new AppRTCClient.RoomConnectionParameters(roomUri.toString(),
                            roomId, loopback);

            // Send intent arguments to fragments.


            showCallControlsUi();


            startCall();


            peerConnectionClient = PeerConnectionClient.getInstance();
            if (loopback) {
                PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
                options.networkIgnoreMask = 0;
                peerConnectionClient.setPeerConnectionFactoryOptions(options);
            }

            /*
             * Since service has context in itself
             */
            peerConnectionClient.createPeerConnectionFactory(mContext, peerConnectionParameters,
                    this);


//            mp = MediaPlayer.create(this, R.raw.calling);
//            mp.setLooping(true);
//            mp.start();
            AudioManager audioManager1 = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager1.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);


            /* Implement the count down timer */
            timer = new CountDownTimer(60000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    /* Perform the click of cancel button here */

                    Toast.makeText(mContext, getResources().getString(R.string.Timeout), Toast.LENGTH_LONG).show();

                    onCallHangUp(7, false);
                }
            };
            timer.start();
        }
    }


    public void startStopWatch() {

        setCallDuration();
        try {

            Chronometer stopWatch = audioCallView.findViewById(R.id.chrono);


            stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer arg0) {


//                    countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;
//
//                    callDuration = String.format(Locale.US, "%02d:%02d:%02d", countUp / 3600, countUp / 60, countUp % 60);
//
//
//                    tvStopWatch.setText(callDuration);


                }
            });


            setupCallHeaderDuration();


            stopWatch.setBase(SystemClock.elapsedRealtime());
            stopWatch.start();

            /*
             * UNCOMMENT THIS LINE IF HAVE TO START AUDIO CALL WITH SPEAKER BEING SET TO LOUDSPEAKER
             */
            //speaker.performClick();

//            initiateChat.setVisibility(View.VISIBLE);


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void setCallDuration() {

        CountDownTimer cTimer = new CountDownTimer(3600000, 1000) {

            public void onTick(long millisUntilFinished) {
                long milliSec = (3600000 - millisUntilFinished) / 1000;

                long sec = milliSec % 60;
                long min = milliSec / 60;
                if (min < 10) {
                    if (sec < 10)
                        tvStopWatch.setText("00:0" + min + ":0" + sec);
                    else
                        tvStopWatch.setText("00:0" + min + ":" + sec);

                } else {
                    if (sec < 10)
                        tvStopWatch.setText("00:" + min + ":0" + sec);
                    else
                        tvStopWatch.setText("00:" + min + ":" + sec);

                }


            }

            @Override
            public void onFinish() {

            }
        };

        cTimer.start();

        setupCallHeaderDuration();

        /*
         * UNCOMMENT THIS LINE IF HAVE TO START AUDIO CALL WITH SPEAKER BEING SET TO LOUDSPEAKER
         */
        //speaker.performClick();

//        initiateChat.setVisibility(View.VISIBLE);

    }

    @SuppressWarnings("TryWithIdenticalCatches")

    private void showCallControlsUi() {
        /*
         * Instead of adding the fragment in the container,we now just update the visibility of the view
         *
         */

        callHeaderView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        callHeaderIv = callHeaderView.findViewById(R.id.image_iv);
        callHeaderTv = callHeaderView.findViewById(R.id.duration);
        stopWatchHeader = callHeaderView.findViewById(R.id.chrono);


        callerImage = audioCallView.findViewById(R.id.thumbnail);


        /*
         * For initiating of the chat
         */

//        initiateChat = (ImageView) audioCallView.findViewById(R.id.initiateChat);


        // Create UI controls.
        ImageView cancelButton = audioCallView.findViewById(R.id.diconnect_btn);
        mute = audioCallView.findViewById(R.id.mute);
        speaker = audioCallView.findViewById(R.id.speaker);

//        mute.getDrawable().clearColorFilter();
//        speaker.getDrawable().clearColorFilter();
//        speaker.getDrawable().setColorFilter(0xFF000000, PorterDuff.Mode.SRC_ATOP);
//        mute.getDrawable().setColorFilter(0xFF000000, PorterDuff.Mode.SRC_ATOP);

        tvCallerName = audioCallView.findViewById(R.id.tvCallerName);


        final String caller_id = intent.getStringExtra("callerId");


        // Map<String, Object> contactInfo = AppController.getInstance().getDbController().getContactInfoFromUid(UtilityVideoCall.getInstance().getContactsDocId(), caller_id);

//        if (contactInfo != null) {
//
//
//            tvCallerName.setText((String) contactInfo.get("contactName"));
//
//
//            imageUrl = (String) contactInfo.get("contactPicUrl");
//
//            if (imageUrl == null || imageUrl.isEmpty()) {
//
//
//                callerImage.setImageDrawable(TextDrawable.builder()
//
//
//                        .beginConfig()
//                        .textColor(Color.WHITE)
//                        .useFont(Typeface.DEFAULT)
//                        .fontSize(124 * (int) getResources().getDisplayMetrics().density) /* size in px */
//                        .bold()
//                        .toUpperCase()
//                        .endConfig()
//
//                        .buildRect(((String) contactInfo.get("contactName")).trim().charAt(0) + "", Color.parseColor("#FFCCBC")));
//
//                      //  .buildRect(((String) contactInfo.get("contactName")).trim().charAt(0) + "", Color.parseColor(AppController.getInstance().getColorCode(5))));
//
//                /*
//                 * For header when call is minimized
//                 */
//                callHeaderIv.setImageDrawable(TextDrawable.builder()
//
//
//                        .beginConfig()
//                        .textColor(Color.WHITE)
//                        .useFont(Typeface.DEFAULT)
//                        .fontSize(36 * (int) getResources().getDisplayMetrics().density) /* size in px */
//                        .bold()
//                        .toUpperCase()
//                        .endConfig()
//
//                        .buildRect(((String) contactInfo.get("contactName")).trim().charAt(0) + "", Color.parseColor("#FFCCBC")));
//
//                       // .buildRect(((String) contactInfo.get("contactName")).trim().charAt(0) + "", Color.parseColor(AppController.getInstance().getColorCode(5))));
//
//
//            } else {
//
//                Glide.with(this)
//                        .load(imageUrl)
//                        //.apply(Utility.createGlideOption(this))
//                        .into(callerImage);
//                try {
//
//                     /* For header when call is minimized
//                     */
//
//                    Glide.with(this)
//                            .load(imageUrl)
//                            .apply(Utility.createGlideOption(this))
//                            .into(callHeaderIv);
//
//
//
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        } else {


        /*
         * If userId doesn't exists in contact
         */


        if (intent != null) {
            Bundle extras = intent.getExtras();
            tvCallerName.setText(extras.getString("callerIdentifier"));

            imageUrl = extras.getString("callerImage");

            if (imageUrl == null || imageUrl.isEmpty()) {

                try {

                    callerImage.setImageDrawable(TextDrawable.builder()

                            .beginConfig()
                            .textColor(Color.WHITE)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(124 * (int) getResources().getDisplayMetrics().density) /* size in px */
                            .bold()
                            .toUpperCase()
                            .endConfig()


                            .buildRect((extras.getString("callerIdentifier").trim()).charAt(0) + "", Color.parseColor(UtilityVideoCall.getInstance().getColorCode(5))));

                    /*
                     * For header when call is minimized
                     */
                    callHeaderIv.setImageDrawable(TextDrawable.builder()


                            .beginConfig()
                            .textColor(Color.WHITE)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(36 * (int) getResources().getDisplayMetrics().density) /* size in px */
                            .bold()
                            .toUpperCase()
                            .endConfig()


                            .buildRect((extras.getString("callerIdentifier").trim()).charAt(0) + "", Color.parseColor(UtilityVideoCall.getInstance().getColorCode(5))));

                } catch (Exception e) {

                }
            } else {

                try {

                    Glide.with(this)
                            .load(imageUrl)
                            //.apply(Utility.createGlideOption(this))
                            .into(callerImage);


                    /*
                     * For header when call is minimized
                     */

                    Glide.with(this)
                            .load(imageUrl)
                            .apply(Utility.createGlideOption(this))
                            .into(callHeaderIv);



                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }

        // }
        // Add buttons click events.
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onCallHangUp(2, false);
                if(intent != null) {
                    presenter.endCall(intent.getExtras().getString(EXTRA_CALL_ID),"call");
                }
                else{
                    onCallHangUp(2, false);
                }
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMute();
                mute.setSelected(isMute);
            }
        });

        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSpeaker();
                speaker.setSelected(isSpeaker);

            }
        });


        final WindowManager.LayoutParams params;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);


        }


        // final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;


        callHeaderView.setOnTouchListener(new View.OnTouchListener()

        {
            private int lastAction;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {


                    case MotionEvent.ACTION_DOWN:


                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //As we implemented on touch listener with ACTION_MOVE,
                        //we have to check if the previous action was ACTION_DOWN
                        //to identify if the user clicked the view or not.


                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            //Open the chat conversation click.


                            //close the service and remove the chat heads


                            windowManager.removeView(callHeaderView);
                            addMainLayout();
                            UtilityVideoCall.getInstance().setCallMinimized(false);
                        }
                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.


                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);


                        if ((int) (event.getRawX() - initialTouchX) == 0 && (int) (event.getRawY() - initialTouchY) == 0) {
                            lastAction = 0;
                        } else {


                            lastAction = event.getAction();
                        }

                        try {
                            //Update the layout with new X & Y coordinate
                            windowManager.updateViewLayout(callHeaderView, params);
                        } catch (NullPointerException e) {


                            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                            windowManager.updateViewLayout(callHeaderView, params);
                        }
                        return true;
                }
                return false;
            }
        });
        /*
         *To initiate chat while on the call
         *
         */
//        initiateChat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                windowManager.removeView(audioCallView);
//                windowManager.addView(callHeaderView, params);
//
//
//                /*
//                 * To open the chats fragment,we add to the container
//                 */
//                /*String docId = AppController.findDocumentIdOfReceiver(caller_id, Utilities.tsInGmt(),
//                        tvCallerName.getText().toString(), imageUrl, "", false,
//                        intent.getExtras().getString("callerIdentifier"), "", false);*/
//
//
//                UtilityVideoCall.getInstance().setCallMinimized(true);
//                UtilityVideoCall.getInstance().setFirstTimeAfterCallMinimized(true);
//
//                if (UtilityVideoCall.getInstance().getActiveActivitiesCount() == 0) {
//
//
//                    try {
//                        Intent intent = new Intent(mContext, ChattingActivity.class);
//
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        /*
//                        intent.putExtra("receiverUid", caller_id);
//                        intent.putExtra("receiverName", tvCallerName.getText().toString());
//                    //    intent.putExtra("documentId", docId);
//                        intent.putExtra("receiverImage", imageUrl);
//                        intent.putExtra("colorCode", UtilityVideoCall.getInstance().getColorCode(5));*/
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                } else {
//                    JSONObject obj = new JSONObject();
//                    try {
//                        obj.put("eventName", "callMinimized");
//
//
//                        obj.put("receiverUid", caller_id);
//                        obj.put("receiverName", tvCallerName.getText().toString());
//                        // obj.put("documentId", docId);
//                        obj.put("receiverImage", imageUrl);
//                        obj.put("colorCode", UtilityVideoCall.getInstance().getColorCode(5));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    //  bus.post(obj);
//                }
//
//            }
//        });
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public void turnOnScreen() {

        try {
            if (!UtilityVideoCall.getInstance().isCallMinimized()) {

                final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                params.screenBrightness = 1;

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    params.type = WindowManager.LayoutParams.TYPE_PHONE;
                } else {
                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

                }

                WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


                mWindowManager.updateViewLayout(audioCallView, params);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public void turnOffScreen() {


        try {

            if (!UtilityVideoCall.getInstance().isCallMinimized()) {
                final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

                params.screenBrightness = 0;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    params.type = WindowManager.LayoutParams.TYPE_PHONE;
                } else {
                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

                }
                WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


                mWindowManager.updateViewLayout(audioCallView, params);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void addMainLayout() {

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        if (params == null) {
            params = new WindowManager.LayoutParams(


                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            //WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,


           /* if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                params.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

            }*/
        }
        //Add the view to the window

        if (windowManager == null) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        }
        windowManager.addView(audioCallView, params);
    }

    private void setupCallHeaderDuration() {

        stopWatchHeader.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUpHeader = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;

                callDurationHeader = String.format(Locale.US, "%02d:%02d:%02d", countUpHeader / 3600, countUpHeader / 60, countUpHeader % 60);


                try {
                    callHeaderTv.setText(callDurationHeader);


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });


        stopWatchHeader.setBase(SystemClock.elapsedRealtime());
        stopWatchHeader.start();
    }

    @Override
    public void onAnswerSuccess() {

    }

    @Override
    public void onRejectSuccess() {
        onCallHangUp(2,false);
    }
}
