package com.karry.telecall.callservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
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
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karry.app.MyApplication;
import com.karry.manager.mqtt_manager.MQTTManager;
import com.karry.manager.mqtt_manager.MqttEvents;
import com.karry.mqttChat.ChattingActivity;
import com.heride.partner.R;
import com.karry.telecall.utility.AppRTCAudioManager;
import com.karry.telecall.utility.AppRTCClient;
import com.karry.telecall.utility.LooperExecutor;
import com.karry.telecall.utility.PeerConnectionClient;
import com.karry.telecall.utility.TextDrawable;
import com.karry.telecall.utility.WebSocketRTCClient;
import com.karry.utility.CircleTransformation;
import com.karry.utility.VariableConstant;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RendererCommon;
import org.webrtc.SessionDescription;
import org.webrtc.StatsReport;
import org.webrtc.SurfaceViewRenderer;

import java.util.Locale;


public class VideoCallService extends Service implements AppRTCClient.SignalingEvents, PeerConnectionClient.PeerConnectionEvents,
        VideoCallEvents {


    public static final String EXTRA_ROOMID = "org.appspot.apprtc.ROOMID";
    public static final String EXTRA_CALL_ID = "org.appspot.apprtc.CALLID";
    public static final String EXTRA_LOOPBACK = "org.appspot.apprtc.LOOPBACK";
    public static final String EXTRA_VIDEO_CALL = "org.appspot.apprtc.VIDEO_CALL";
    public static final String EXTRA_VIDEO_WIDTH = "org.appspot.apprtc.VIDEO_WIDTH";
    public static final String EXTRA_VIDEO_HEIGHT = "org.appspot.apprtc.VIDEO_HEIGHT";
    public static final String EXTRA_VIDEO_FPS = "org.appspot.apprtc.VIDEO_FPS";
    public static final String EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED =
            "org.appsopt.apprtc.VIDEO_CAPTUREQUALITYSLIDER";
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
            "android.permission.RECORD_AUDIO", "android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.INTERNET"
    };
    // Peer connection statistics callback period in ms.
    private static final int STAT_CALLBACK_PERIOD = 1000;

   // public static Bus bus = MyApplication.getBus();
    public boolean isVideoAvailable = true;
    // Controls

    boolean disconnect;
    MediaPlayer mp;
    CountDownTimer timer;
    private PeerConnectionClient peerConnectionClient = null;
    private AppRTCClient appRtcClient;
    private AppRTCClient.SignalingParameters signalingParameters;
    private AppRTCAudioManager audioManager = null;
    private EglBase rootEglBase;
    private SurfaceViewRenderer localRender;
    private SurfaceViewRenderer remoteRender;

    private RendererCommon.ScalingType scalingType;

    private boolean commandLineRun;
    private int runTimeMs;

    private AppRTCClient.RoomConnectionParameters roomConnectionParameters;

    private boolean iceConnected;
    private boolean isError;
    private boolean callControlFragmentVisible = true;
    private long callStartedTimeMs = 0;


    private FrameLayout root;


    private Context mContext;

    private View videoCallView, callHeaderView;
    private WindowManager windowManager;


    private TextView callHeaderTv;

    private Intent intent;
    private WindowManager.LayoutParams params;
    private Handler handler;


    /**
     * For the fragment controls which are now moved into the UI
     */


    private ImageView mute, video;
    private TextView tvStopWatch, tvCallerName;


    private boolean isMute = false, isVideoShow = false;

    private ImageView callerImage;


    /**
     * Call control interface for container activity.
     */

    private String imageUrl = "";
    private ImageView initiateChat;


    private boolean isFrontCamera = false;
    private Chronometer stopWatchHeader;
    private long countUp, countUpHeader;
    private RelativeLayout parentRl;
    private SurfaceViewRenderer remoteRenderHeader;


//    private boolean cameraNotHidden = true;

    PowerManager mgr;

    private BroadcastReceiver receiver;

    MQTTManager mqttManager = MyApplication.getInstance().getMqtt();

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        if(mqttManager == null)
        {
            mqttManager = MyApplication.getInstance().getMqtt();
        }

        videoCallView = LayoutInflater.from(this).inflate(R.layout.video_call_service, null);
      //  bus.register(this);

        mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            videoCallView
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            videoCallView
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);

        }
        // Set window styles for fullscreen-window size. Needs to be done before
        // adding content.

        tvStopWatch = (TextView) videoCallView.findViewById(R.id.tvStopWatch);
        /*
         * For showing own camera view while making a call
         */


        addMainLayout();

        handler = new Handler(Looper.getMainLooper());
        root = (FrameLayout) videoCallView.findViewById(R.id.root);
        /*
         * Mqtt
         */


        /*
         * To check if the call screen has been opened for the incoming call or the outgoing call
         *
         */
        /*
         * For the outgoing call have to check for the
         */


        iceConnected = false;
        signalingParameters = null;
        scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;

        // Create UI controls.
        localRender = (SurfaceViewRenderer) videoCallView.findViewById(R.id.local_video_view);
        localRender.setMirror(true);
        remoteRender = (SurfaceViewRenderer) videoCallView.findViewById(R.id.remote_video_view);
        remoteRender.setMirror(true);

        // Show/hide call control fragment on view click.
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCallControlFragmentVisibility();
            }
        };

        try {
            localRender.setOnClickListener(listener);
            remoteRender.setOnClickListener(listener);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        // Create video renderers.
        rootEglBase = EglBase.create();
        localRender.init(rootEglBase.getEglBaseContext(), null);
        remoteRender.init(rootEglBase.getEglBaseContext(), null);
        localRender.setZOrderMediaOverlay(true);
        localRender.requestLayout();
//        remoteRender.requestLayout();


        // Check for mandatory permissions.
        for (String permission : MANDATORY_PERMISSIONS) {
            if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                logAndToast("Permission " + permission + " is not granted");
                stopSelf();

                return;
            }


        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(VariableConstant.INTENT_ACTION_CALL);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction() !=null && intent.getAction().equals(VariableConstant.INTENT_ACTION_CALL))
                {
                    try {
                        getMessage(new JSONObject(intent.getStringExtra("value")));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        registerReceiver(receiver, filter);

        VariableConstant.IS_IN_CALL = true;
    }

    @Override
    public IBinder onBind(Intent intent) {


        this.intent = intent;
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.intent = intent;

        if(mqttManager == null)
        {
            mqttManager = MyApplication.getInstance().getMqtt();
        }

        if (intent != null) {

            if (!intent.getExtras().getBoolean("isIncomingCall", true)) {


                mqttManager.subscribeToTopic(MqttEvents.CallsAvailability.value + "/" + intent.getExtras().getString("callerId"));


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
    public void onDestroy() {

        VariableConstant.IS_IN_CALL = false;
        disconnect();

        if(receiver != null)
        {
            unregisterReceiver(receiver);
        }


        if (windowManager == null) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        }
        try {

            if (MyApplication.getInstance().getCallHelper().isCallMinimized()) {

                if (callHeaderView != null) {
                    windowManager.removeView(callHeaderView);
                }
            } else {
                if (videoCallView != null) {


                    /*
                     * For clearing of the full screen UI mode
                     */
                    videoCallView
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                    windowManager.removeView(videoCallView);
                }
            }


            rootEglBase.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // hideCameraView(false);
        super.onDestroy();
    }

    // OnCallEvents interface implementation.
    @Override
    public void onCallHangUp(int val, boolean received) {

        if (!disconnect) {
            disconnect = true;
            /* Send Reject through call event and call callEnd API */

            /*
             * To make myself available for receiving the new call
             */

            /*
             * Timeout from the sender side
             */

            if (intent != null) {
                try {


                    if (!received) {

                        JSONObject obj = new JSONObject();

                        obj.put("callId", intent.getExtras().getString(EXTRA_ROOMID));
                        obj.put("userId",MyApplication.getInstance().getCallHelper().getUserId());// userId
                        obj.put("type", val);


                        //mqttManager.publish(MqttEvents.Calls.value + "/" + intent.getExtras().getString("callerId"), obj, 0, false);
                        Log.d(TAG, "Calling : " +obj);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        disconnect();
    }

    @Override
    public void onCameraSwitch() {
        if (peerConnectionClient != null) {
            if (!isFrontCamera) {
                localRender.setMirror(false);
            } else {
                localRender.setMirror(true);
            }
            peerConnectionClient.switchCamera();
        }
    }

    @Override
    public void onMute() {
        isMute = !isMute;
        audioManager.setSpeakerphoneOn(isMute);
        audioManager.setMicrophoneMute(isMute);
    }

    @Override
    public void onVideoShow() {
        video();
    }

    public void video() {

        Log.d("log1","1");
        isVideoAvailable = !isVideoAvailable;
        if (isVideoAvailable) {

            if (localRender != null) {

                localRender.setBackgroundColor(Color.parseColor("#00000000"));
            }

            if (intent != null) {
                try {
                    JSONObject obj = new JSONObject();

                    obj.put("callId", intent.getExtras().getString(EXTRA_ROOMID));
                    obj.put("userId", MyApplication.getInstance().getCallHelper().getUserId());//userId
                    obj.put("type", 5);


                    //mqttManager.publish(MqttEvents.Calls.value + "/" + intent.getExtras().getString("callerId"), obj, 0, false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } else {


            if (localRender != null) {
                localRender.setBackgroundColor(Color.parseColor("#000000"));
            }


            if (intent != null) {
                try {

                    JSONObject obj = new JSONObject();
                    obj.put("callId", intent.getExtras().getString(EXTRA_ROOMID));
                    obj.put("userId", MyApplication.getInstance().getCallHelper().getUserId());
                    obj.put("type", 6);


                   // mqttManager.publish(MqttEvents.Calls.value + "/" + intent.getExtras().getString("callerId"), obj, 0, false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Helper functions.
    private void toggleCallControlFragmentVisibility() {


        if (!iceConnected) {
            return;
        }

        callControlFragmentVisible = !callControlFragmentVisible;

        try {
            if (callControlFragmentVisible) {
                parentRl.setVisibility(View.VISIBLE);
            } else {
                parentRl.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    private void startCall() {
        if (appRtcClient == null) {
            Log.e(TAG, "AppRTC client is not allocated for a call.");
            return;
        }
        callStartedTimeMs = System.currentTimeMillis();

        // Start room connection.
        //  logAndToast(getString(R.string.connecting_to, roomConnectionParameters.roomUrl));
        appRtcClient.connectToRoom(roomConnectionParameters);

        // Create and audio manager that will take care of audio routing,
        // audio modes, audio device enumeration etc.

        audioManager = AppRTCAudioManager.create(true, this, new Runnable() {
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

    }

    // Should be called from UI thread
    private void callConnected() {


//        final long delta = System.currentTimeMillis() - callStartedTimeMs;
//        Log.i(TAG, "Call connected: delay=" + delta + "ms");
        if (peerConnectionClient == null || isError) {
            Log.w(TAG, "Call is connected in closed or error state");
            return;
        }


        // Update video view.

        remoteRender.requestLayout();

        /*
         * Have been put after intentionally to avoid momentary flick on updating renderer state
         */


        remoteRender.setVisibility(View.VISIBLE);


        /*
         * To hide the controls
         */

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callControlFragmentVisible = false;
                parentRl.setVisibility(View.GONE);
            }
        }, 2000);


        // Enable statistics callback.
        peerConnectionClient.enableStatsEvents(true, STAT_CALLBACK_PERIOD);
        /* Start the stop watch */

        /*
         * By default video call is put on loudspeaker,unless speaker is moved near the ears.
         */
        audioManager.setSpeakerphoneOn(true);

        timer.cancel();
        /* Stop the calling sound */

        try {
            if (mp != null)
                mp.stop();
        } catch (Exception e) {

        }

        startStopWatch();

        // hideCameraView(true);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) localRender.getLayoutParams();

        float density = mContext.getResources().getDisplayMetrics().density;
        params.height = (int) ((density) * 150);

        params.width = (int) ((density) * 150);

        int margin = (int) density * 10;
        params.setMargins(margin, margin, margin, margin);
        localRender.setLayoutParams(params);

    }

    private void onAudioManagerChangedState() {
        // TODO(henrika): disable video if AppRTCAudioManager.AudioDevice.EARPIECE
        // is active.
    }

    // Disconnect from remote resources, dispose of local resources, and exit.
    private void disconnect() {
        try {



            /*
             * To make myself available for receiving the new call
             */


            JSONObject obj = new JSONObject();
            obj.put("status", 1);

            mqttManager.publish(MqttEvents.CallsAvailability.value + "/" + MyApplication.getInstance().getCallHelper().getUserId(), obj, 0, true);
            MyApplication.getInstance().getCallHelper().setActiveOnACall(false, false);


            try {
                if (mp != null)
                    mp.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            timer.cancel();

            if (appRtcClient != null) {
                appRtcClient.disconnectFromRoom();
                appRtcClient = null;
            }
            if (peerConnectionClient != null) {
                peerConnectionClient.close();
                peerConnectionClient = null;
            }
            if (localRender != null) {
                localRender.release();
                localRender = null;
            }
            if (remoteRender != null) {
                remoteRender.release();
                remoteRender = null;
            }


            if (remoteRenderHeader != null) {
                remoteRenderHeader.release();
                remoteRenderHeader = null;
            }

            if (audioManager != null) {
                audioManager.close();
                audioManager = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        stopSelf();
    }

    // Log |msg| and Toast about it.
    private void logAndToast(String msg) {

    }


    // -----Implementation of AppRTCClient.AppRTCSignalingEvents ---------------
    // All callbacks are invoked from websocket signaling looper thread and
    // are routed to UI thread.
    private void onConnectedToRoomInternal(final AppRTCClient.SignalingParameters params) {
        final long delta = System.currentTimeMillis() - callStartedTimeMs;
Log.d("log1","connected to room internal");
        signalingParameters = params;
        //   logAndToast("Creating peer connection, delay=" + delta + "ms");


        peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), localRender,
                remoteRender, remoteRenderHeader, signalingParameters);

        if (signalingParameters.initiator) {
            //logAndToast("Creating OFFER...");
            // Create offer. Offer SDP will be sent to answering client in
            // PeerConnectionEvents.onLocalDescription event.
            peerConnectionClient.createOffer();
        } else {
            if (params.offerSdp != null) {
                peerConnectionClient.setRemoteDescription(params.offerSdp);
                // logAndToast("Creating ANSWER...");
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
        Log.d("log1","connected to room");

        handler.post(new Runnable() {


            @Override
            public void run() {

                onConnectedToRoomInternal(params);
            }
        });
    }

    @Override
    public void onRemoteDescription(final SessionDescription sdp) {
//        final long delta = System.currentTimeMillis() - callStartedTimeMs;

        Log.d("log1","remote description");
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (peerConnectionClient == null) {
                    Log.e(TAG, "Received remote SDP for non-initilized peer connection.");
                    return;
                }
                //  logAndToast("Received remote " + sdp.type + ", delay=" + delta + "ms");
                peerConnectionClient.setRemoteDescription(sdp);
                if (!signalingParameters.initiator) {
                    //   logAndToast("Creating ANSWER...");
                    // Create answer. Answer SDP will be sent to offering client in
                    // PeerConnectionEvents.onLocalDescription event.
                    peerConnectionClient.createAnswer();
                }
            }
        });
    }

    @Override
    public void onRemoteIceCandidate(final IceCandidate candidate) {

        Log.d("log1","remote ice candidate");
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (peerConnectionClient == null) {
                    Log.e(TAG, "Received ICE candidate for non-initilized peer connection.");
                    return;
                }
                peerConnectionClient.addRemoteIceCandidate(candidate);
            }
        });
    }

    @Override
    public void onChannelClose() {

        Log.d("log1","channel close");
        handler.post(new Runnable() {
            @Override
            public void run() {
                //   logAndToast("Remote end hung up; dropping PeerConnection");


                disconnect();


            }
        });
    }

    @Override
    public void onChannelError(final String description) {
        //reportError(description);

        Log.d("log1","channel error"+description);
    }

    // -----Implementation of PeerConnectionClient.PeerConnectionEvents.---------
    // Send local peer connection SDP and ICE candidates to remote party.
    // All callbacks are invoked from peer connection client looper thread and
    // are routed to UI thread.
    @Override
    public void onLocalDescription(final SessionDescription sdp) {
//        final long delta = System.currentTimeMillis() - callStartedTimeMs;

        Log.d("log1","on local description");
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (appRtcClient != null) {
                    //   logAndToast("Sending " + sdp.type + ", delay=" + delta + "ms");
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

        Log.d("log1","on ice candidate");

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

        Log.d("log1","on ice connected");
        // final long delta = System.currentTimeMillis() - callStartedTimeMs;
        handler.post(new Runnable() {
            @Override
            public void run() {
                //      logAndToast("ICE connected, delay=" + delta + "ms");
                iceConnected = true;
                callConnected();
            }
        });
    }

    @Override
    public void onIceDisconnected() {
        Log.d("log1","on ice disconnected");
        handler.post(new Runnable() {
            @Override
            public void run() {
                // logAndToast("ICE disconnected");
                iceConnected = false;


                disconnect();
            }
        });
    }

    @Override
    public void onPeerConnectionClosed() {
        Log.d("log1","peer connection closed");
    }

    @Override
    public void onPeerConnectionStatsReady(final StatsReport[] reports) {
    }

    @Override
    public void onPeerConnectionError(final String description) {
        //reportError(description);
        Log.d("log1","peer connection error");
    }


    public void getMessage(JSONObject object) {
        try {

            if (object.getString("eventName").equals(MqttEvents.CallsAvailability.value)) {

                if (intent != null) {
                    Bundle extras = intent.getExtras();

                    if (object.getInt("status") == 0) {
                        /*
                         * Receiver is busy
                         */
                        if (root != null) {

                          /*  Snackbar snackbar = Snackbar.make(root, extras.getString("callerName") + " " + getString(R.string.busy), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);*/

                          Toast.makeText(getApplicationContext(),extras.getString("callerName") + " " + getString(R.string.busy), Toast.LENGTH_SHORT).show();

                        }


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                stopSelf();
                            }
                        }, 2000);

                    } else {


                        try {
                            /*
                             * I put myself as busy and make the call request to the receiver
                             */
                            JSONObject obj = new JSONObject();
                            obj.put("status", 0);


                            mqttManager.publish(MqttEvents.CallsAvailability.value + "/" + MyApplication.getInstance().getCallHelper().getUserId(), obj, 0, true);
                            MyApplication.getInstance().getCallHelper().setActiveOnACall(true, true);

                            obj = new JSONObject();
                            obj.put("callerId", MyApplication.getInstance().getCallHelper().getUserId());
                            obj.put("callId", extras.getString(EXTRA_ROOMID));
                            obj.put("callType", "1");
                            obj.put("callerName", MyApplication.getInstance().getCallHelper().getUserName());
                            obj.put("callerImage", MyApplication.getInstance().getCallHelper().getUserImageUrl());
                            obj.put("callerIdentifier", MyApplication.getInstance().getCallHelper().getUserIdentifier());
                            obj.put("bookingId", MyApplication.getInstance().getCallHelper().getBookingId());
                            obj.put("type", 0);
                            obj.put("userType ", 2);
                            obj.put("bookingId", MyApplication.getInstance().getCallHelper().getBookingId());



                            /*
                             * CalleeId although not required but can be used in future on server so using it
                             *
                             *
                             * CalleeId contains the receiverUid,to whom the call has been made
                             *
                             * */



                        /*
                         * Type-0---call initiate request,on receiving the call initiate request,receiver will set his status as busy,so nobody else can call him
                         *
                         * /


                        obj.put("type", 0);
/*
 * Not making any message of call signalling as being persistent intentionally
 */

                           // mqttManager.publish(MqttEvents.Calls.value + "/" + extras.getString("callerId"), obj, 0, false);

                            MyApplication.getInstance().getCallHelper().setActiveCallId(extras.getString(EXTRA_ROOMID));
                            MyApplication.getInstance().getCallHelper().setActiveCallerId(extras.getString("callerId"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (object.getString("eventName").equals(MqttEvents.Call.value + "/" + MyApplication.getInstance().getCallHelper().getUserId())) {


                switch (object.getInt("type")) {

                    case 1:


                        tvStopWatch.setText(getString(R.string.connecting));
                        break;


                    case 2:
                        onCallHangUp(2, true);
                        break;
                    case 3:


                        if (root != null && intent != null) {


                          /*  Snackbar snackbar = Snackbar.make(root, getString(R.string.NoAnswer) + " " + intent.getExtras().getString("callerName"), Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);*/

                            Toast.makeText(getApplicationContext(),getString(R.string.NoAnswer) + " " + intent.getExtras().getString("callerName"), Toast.LENGTH_SHORT).show();
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                stopSelf();
                            }
                        }, 2000);
                        break;
                    case 5:
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                remoteRender.setBackgroundColor(Color.parseColor("#00000000"));
                            }
                        });
                        break;
                    case 6:
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                remoteRender.setBackgroundColor(Color.parseColor("#4e4e4e"));


                            }
                        });
                        break;
                    case 7:


                        onCallHangUp(7, true);


                }
            }
            else  if(object.getString("eventName").equals("appCrashed")){


                stopSelf();
                System.exit(1);
            }


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
                //    logAndToast(getString(R.string.missing_url));
                Log.e(TAG, "Didn't get any URL in intent!");

                return;
            }
            String roomId = intent.getStringExtra(EXTRA_ROOMID);
            if (roomId == null || roomId.length() == 0) {
//                logAndToast(getString(R.string.missing_url));
//                Log.e(TAG, "Incorrect room ID in intent!");

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
                    new AppRTCClient.RoomConnectionParameters(roomUri.toString(), roomId, loopback);




            // Send intent arguments to fragments.

            // Activate call and HUD fragments and start the call.


            showCallControlsUi();
            startCall();


            peerConnectionClient = PeerConnectionClient.getInstance();
            if (loopback) {
                PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
                options.networkIgnoreMask = 0;
                peerConnectionClient.setPeerConnectionFactoryOptions(options);
            }
            peerConnectionClient.createPeerConnectionFactory(mContext, peerConnectionParameters,
                    this);
            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.


//                mp = MediaPlayer.create(this, R.raw.calling);
//                mp.setLooping(true);
//                mp.start();
//

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
            //      WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,


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


        windowManager.addView(videoCallView, params);

    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void showCallControlsUi() {


        callHeaderView = LayoutInflater.from(this).inflate(R.layout.video_call_floating_widget, null);
        remoteRenderHeader = (SurfaceViewRenderer) callHeaderView.findViewById(R.id.remoteVideoHeader);

        remoteRenderHeader.init(rootEglBase.getEglBaseContext(), null);


        callHeaderTv = (TextView) callHeaderView.findViewById(R.id.duration);
        stopWatchHeader = (Chronometer) callHeaderView.findViewById(R.id.chrono);
        parentRl = (RelativeLayout) videoCallView.findViewById(R.id.container_rl);
        parentRl.setVisibility(View.VISIBLE);


        initiateChat = (ImageView) videoCallView.findViewById(R.id.initiateChat);
        // Create UI controls.
        final ImageButton camera = (ImageButton) videoCallView.findViewById(R.id.camera);
        ImageView cancelCall = (ImageButton) videoCallView.findViewById(R.id.cancelCall);
        mute = (ImageButton) videoCallView.findViewById(R.id.mute);
        video = (ImageButton) videoCallView.findViewById(R.id.video);
        camera.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);


        tvCallerName = (TextView) videoCallView.findViewById(R.id.tvCallerName);

        callerImage = (ImageView) videoCallView.findViewById(R.id.userImage);


        final String caller_id = intent.getStringExtra("callerId");


      //  Map<String, Object> contactInfo = MyApplication.getInstance().getDbController().getContactInfoFromUid(MyApplication.getInstance().getContactsDocId(), caller_id);

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
//                try {
//
//                    callerImage.setImageDrawable(TextDrawable.builder()
//
//
//                            .beginConfig()
//                            .textColor(Color.WHITE)
//                            .useFont(Typeface.DEFAULT)
//                            .fontSize(24 * (int) getResources().getDisplayMetrics().density) /* size in px */
//                            .bold()
//                            .toUpperCase()
//                            .endConfig()
//
//
//                            .buildRound(((String) contactInfo.get("contactName")).trim().charAt(0) + "", Color.parseColor(UtilityVideoCall.getInstance().getColorCode(5))));
//                } catch (Exception e) {
//                }
//
//            } else {
//
//                try {
//                    Glide.with(this)
//                            .load(imageUrl)
//                            .apply(Utility.createGlideOption(this))
//                            .into(callerImage);
//
//
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//
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
                                .fontSize(24 * (int) getResources().getDisplayMetrics().density) /* size in px */
                                .bold()
                                .toUpperCase()
                                .endConfig()


                                .buildRound((extras.getString("callerIdentifier").trim()).charAt(0) + "", Color.parseColor(MyApplication.getInstance().getCallHelper().getColorCode(5))));

                    } catch (Exception ignored) {

                    }
                } else {

                    try {

                        Picasso.get()
                                .load(imageUrl)
                                .placeholder(R.drawable.svg_profile)
                                .transform(new CircleTransformation())
                                .into(callerImage);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                }
            }
       // }


        // Add buttons click events.
        cancelCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCallHangUp(2, false);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCameraSwitch();
                isFrontCamera = !isFrontCamera;

                camera.setSelected(isFrontCamera);


            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMute();
                mute.setSelected(isMute);
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onVideoShow();
                isVideoShow = !isVideoShow;
                video.setSelected(isVideoShow);
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
                            MyApplication.getInstance().getCallHelper().setCallMinimized(false);
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
         *To open the chatscreen activity with the given user
         *
         */

        initiateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windowManager.removeView(videoCallView);
                windowManager.addView(callHeaderView, params);
                /*
                 * To open the chats fragment,we add to the container
                 */
               /* String docId = MyApplication.findDocumentIdOfReceiver(caller_id, Utilities.tsInGmt(), tvCallerName.getText().toString(),
                        imageUrl, "", false, intent.getExtras().getString("callerIdentifier"), "", false);*/


                MyApplication.getInstance().getCallHelper().setCallMinimized(true);

                MyApplication.getInstance().getCallHelper().setFirstTimeAfterCallMinimized(true);

                Intent chatIntent = new Intent(mContext,ChattingActivity.class);
                startActivity(chatIntent);
            }
        });


    }

    public void startStopWatch() {

        setCallDuration();
        try {


            Chronometer stopWatch = (Chronometer) videoCallView.findViewById(R.id.chrono);


            stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer arg0) {


//                    countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;
//
//
//                    tvStopWatch.setText(String.format(Locale.US, "%02d:%02d:%02d", countUp / 3600, countUp / 60, countUp % 60));


                }
            });

            setupCallHeaderDuration();


            stopWatch.setBase(SystemClock.elapsedRealtime());

            stopWatch.start();

            initiateChat.setVisibility(View.VISIBLE);
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


    }


    private void setupCallHeaderDuration() {

        stopWatchHeader.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUpHeader = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;


                try {
                    callHeaderTv.setText(String.format(Locale.US, "%02d:%02d:%02d", countUpHeader / 3600, countUpHeader / 60, countUpHeader % 60));


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });


        stopWatchHeader.setBase(SystemClock.elapsedRealtime());
        stopWatchHeader.start();
    }


//    private void hideCameraView(boolean callConnected) {
//
//
//        if (cameraNotHidden) {
//
//            cameraNotHidden = false;
//            try {
//                JSONObject obj = new JSONObject();
//                obj.put("eventName", "hideCameraView");
//                bus.post(obj);
//
//            } catch (JSONException e) {
//            }
//
//        }
//
//        if (callConnected) {
//
////            if (peerConnectionClient != null) {
////                peerConnectionClient.addLocalVideoTrack();
////
////
////                localRender.requestLayout();
////                localRender.setVisibility(View.VISIBLE);
////            }
//
//
//
//
//        }
//    }
}