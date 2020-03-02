package com.karry.telecall.utility;

/**
 * Created by moda on 04/05/17.
 */

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.karry.app.MyApplication;
import com.heride.partner.R;
import com.karry.telecall.UtilityVideoCall;
import com.karry.telecall.callservice.AudioCallService;
import com.karry.telecall.callservice.VideoCallService;
import com.karry.telecall.model.NewCallData;
import com.karry.utility.VariableConstant;

import org.json.JSONException;
import org.json.JSONObject;


public class CallingApis {

    /* Variables for Audio Video User */
    private static String keyprefVideoCallEnabled;
    private static String keyprefResolution;
    private static String keyprefFps;
    private static String keyprefCaptureQualitySlider;
    private static String keyprefVideoBitrateType;
    private static String keyprefVideoBitrateValue;
    private static String keyprefVideoCodec;
    private static String keyprefAudioBitrateType;
    private static String keyprefAudioBitrateValue;
    private static String keyprefAudioCodec;
    private static String keyprefHwCodecAcceleration;
    private static String keyprefCaptureToTexture;
    private static String keyprefNoAudioProcessingPipeline;
    private static String keyprefAecDump;
    private static String keyprefOpenSLES;
    private static String keyprefDisplayHud;
    private static String keyprefTracing;
    private static String keyprefRoomServerUrl;
    private static String keyprefRoom;
    private static String keyprefRoomList;

    private static final int CONNECTION_REQUEST = 1;

    /* Socket Object to send data*/
//    private static Socket socket = MyApplication.getConnection();

//
//    /* This API is used to check in case we have any live call available */
//    public static void CheckForLiveCall(Context context) {
//        JSONObject obj = new JSONObject();
//        SharedPreferences m_prefs =
//                context.getSharedPreferences("global_settings", Context.MODE_PRIVATE);
//        /* Get the mobile number of the user from shared preferences */
//        String mUserId = m_prefs.getString("userId", "");
//
////        if (!MyApplication.getInstance().getSessionManager().getUSER_ID().contentEquals("")) {
////            try {
////
////                obj.put("from", MyApplication.getInstance().getSessionManager().getUSER_ID());
////
////
////                socket.emit("getCallStatus", obj);
////
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////        }
//    }

    /* This API is used to open the incoming call screen */


    /*
     *
     *MQtt
     *
     */


    /**
     * Have to open the incoming call screen
     */

//
//  public static void OpenIncomingCallScreen(String args, Context context) {


    public static void OpenIncomingCallScreen(NewCallData newCallData, Context context) {
        try {
            SharedPreferences m_prefs =
                    context.getSharedPreferences("global_settings", Context.MODE_PRIVATE);
            /* Get the mobile number of the user from shared preferences */
            String previousCallId = m_prefs.getString("call_id", "");

            assert previousCallId != null;
            if (!previousCallId.contentEquals(newCallData.getCallId())) {

                UtilityVideoCall.getInstance().setActiveCallId(newCallData.getCallId());
                UtilityVideoCall.getInstance().setActiveCallerId(newCallData.getUserId());
                /*
                 * Type 0
                 */
                Intent incomingScreen = new Intent("com.karry.telecall.incommingcalls.IncomingCallScreen");
                incomingScreen.putExtra("callerImage", newCallData.getUserImage());
                incomingScreen.putExtra("callerName", newCallData.getUserName());
                incomingScreen.putExtra("BookingId", newCallData.getRoom());
                incomingScreen.putExtra("callerId", newCallData.getUserId());
                incomingScreen.putExtra("callId", newCallData.getCallId());
                incomingScreen.putExtra("roomId", newCallData.getRoom());
                Log.d("OpenIncoming", "OpenIncomingCallScreen: roomID: "+newCallData.getRoom());
                if(newCallData.getType().equals("audio"))
                    incomingScreen.putExtra("callType", "0");//audio call
                else
                    incomingScreen.putExtra("callType", "1");//video call

                incomingScreen.putExtra("callerIdentifier", newCallData.getUserName());


                incomingScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(context, (int) System.currentTimeMillis(), incomingScreen,
                                0);
                pendingIntent.send();

                /* Save this call id in shared preferences */
                SharedPreferences.Editor editor = m_prefs.edit();
                editor.putString("call_id", newCallData.getCallId());
                editor.apply();
            }
//                } else {
//            / Register an event to Audio Call or Video call to signial that this call is no longer live /
//                    bus.register("Cancel Call");
//                }
            //}
        } catch (Exception e) {
            //   bus.register("Cancel Call"); i am doing
            e.printStackTrace();
        }
    }


    public static void OpenIncomingCallScreen(JSONObject data, Context context) {
        // JSONObject data = null;
        try {
            //     data = new JSONObject(args);
            SharedPreferences m_prefs =
                    context.getSharedPreferences("global_settings", Context.MODE_PRIVATE);
        /* Get the mobile number of the user from shared preferences */
            String previousCallId = m_prefs.getString("call_id", "");


//            if (data != null) {

            //  if (data.getString("err").contentEquals("0")) {
            /* Success Message */

            if (!previousCallId.contentEquals(data.getString("callId"))) {


//                        Intent incomingScreen = new Intent("com.example.moda.mqttchat.Calls.IncomingCallScreen");
//                        incomingScreen.putExtra("call_from", data.getString("call_from"));
//                        incomingScreen.putExtra("call_id", data.getString("call_id"));
//                        incomingScreen.putExtra("callType", data.getString("callType"));

                MyApplication.getInstance().getCallHelper().setActiveCallId(data.getString("callId"));
                MyApplication.getInstance().getCallHelper().setActiveCallerId(data.getString("callerId"));

                /*
                 * Type 0
                 */


                Intent incomingScreen = new Intent("com.karry.telecall.incommingcalls.IncomingCallScreen");
                incomingScreen.putExtra("callerImage", data.getString("callerImage"));
                incomingScreen.putExtra("callerName", data.getString("callerName"));
                incomingScreen.putExtra("BookingId", data.getString("bookingId"));
                incomingScreen.putExtra("callerId", data.getString("callerId"));
                incomingScreen.putExtra("callId", data.getString("callId"));
                incomingScreen.putExtra("callType", data.getString("callType"));

                incomingScreen.putExtra("callerIdentifier", data.getString("callerIdentifier"));


                incomingScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(context, (int) System.currentTimeMillis(), incomingScreen,
                                0);
                pendingIntent.send();

            /* Save this call id in shared preferences */
                SharedPreferences.Editor editor = m_prefs.edit();
                editor.putString("call_id", data.getString("callId"));
                editor.apply();

                Log.d("Calling", "Calling received: 4"+data.getString("callType"));
            }
//                } else {
//            /* Register an event to Audio Call or Video call to signial that this call is no longer live */
//                    bus.register("Cancel Call");
//                }
            //}
        } catch (Exception e) {
            e.printStackTrace();

            Intent intent = new Intent(VariableConstant.INTENT_ACTION_CALL);
            JSONObject obj = new JSONObject();
            try {
                obj.put("eventName", CallEvents.CancelCall);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            intent.putExtra("value", obj.toString());
            context.sendBroadcast(intent);
        }
    }

//    public static void sendCallEvent(Context context, String status, String to, String call_id) {
//        JSONObject obj = new JSONObject();
//
////        if (MyApplication.getInstance().getSessionManager().getApplicationIsKilled()) {
////
////
////            if (socket == null || !socket.connected()) {
////
////
////                try {
////
////                    socket = IO.socket(Common.CHAT_SERVER_URL);
////                    if (!socket.connected())
////
////                        socket.connect();
////                } catch (URISyntaxException e) {
////                    e.printStackTrace();
////
////
////                }
////            }
////
////        }
//
//
//        try {
//
//
//            obj.put("from", MyApplication.getInstance().getUserId());
//            obj.put("userName", MyApplication.getInstance().getUserName());
//            obj.put("to", to);
//            obj.put("status", status);
//            obj.put("call_id", call_id);
//
//
//            MyApplication.getInstance().publish("CallEvent2", obj, 0, false);
//            // socket.emit("CallEvent2", obj);
//
//
//
//
//
//
//
//
//
//      /* If this is a reject call, send call end event as well */
//            if (status.contentEquals("Reject")) {
//                JSONObject callEndObj = new JSONObject();
//                callEndObj.put("from", MyApplication.getInstance().getUserId());
//                callEndObj.put("to", to);
//                callEndObj.put("call_id", call_id);
//
//                MyApplication.getInstance().publish("callEnd", obj, 0, false);
//
//
//                // socket.emit("callEnd", callEndObj);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void sendCallEndEvent(Context context, String to, String call_id) {
////        JSONObject obj = new JSONObject();
////        if (MyApplication.getInstance().getSessionManager().getApplicationIsKilled()) {
////
////            if (socket == null || !socket.connected()) {
////                try {
////                    socket = IO.socket(Common.CHAT_SERVER_URL);
////                    if (!socket.connected())
////                        socket.connect();
////                } catch (URISyntaxException e) {
////                    e.printStackTrace();
////
////
////                }
////            }
////
////        }
//        try {
//            JSONObject callEndObj = new JSONObject();
//            callEndObj.put("from", MyApplication.getInstance().getUserId());
//            callEndObj.put("to", to);
//            callEndObj.put("call_id", call_id);
//
//
//            MyApplication.getInstance().publish("callEnd", callEndObj, 0, false);
//
//
////            socket.emit("callEnd", callEndObj);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void callInit(Context context, String callType, String to, String call_id) {
//        JSONObject obj = new JSONObject();
//        SharedPreferences m_prefs =
//                context.getSharedPreferences("global_settings", Context.MODE_PRIVATE);
//        /* Get the mobile number of the user from shared preferences */
//        String mUserId = m_prefs.getString("userId", "");
//        try {
//
//            obj.put("from", MyApplication.getInstance().getUserId());
//            obj.put("userName", MyApplication.getInstance().getUserName());
//            obj.put("to", to);
//            obj.put("callType", callType);
//            obj.put("call_id", call_id);
//
//            MyApplication.getInstance().publish("CallInit2", obj, 0, false);
//            //socket.emit("CallInit2", obj);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    /* This API is used to start audio or Video call */
    public static void startCall(Context context, String callType, String call_id, String callee_caller_id, String callee_caller_name, String callee_caller_image, boolean isIncomingCall, String callerIdentifier, String bookingId) {

        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String roomUrl = sharedPref.getString(keyprefRoomServerUrl,
                context.getString(R.string.pref_room_server_url_default));

        boolean videoCallEnabled = true;
        Intent intent;


        /*
         * CallType----
         *
         * 0-Audio
         * 1-Video
         *
         *
         */


        // Video call enabled flag.
        Log.d("Testtttttttttt", "startCall: callApi "+callType);
        if (callType.equals("0")) {
            videoCallEnabled = false;

            intent = new Intent(context, AudioCallService.class);

        } else {

/*
 * To open the background camera preview
 */
//            Intent i = new Intent(context, LocalCameraView.class);
//
//            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            context.startActivity(i);


            intent = new Intent(context, VideoCallService.class);
        }

       /* -----------------------------------------------------*/
/*
 * Code to make calls work on the MQtt
 */

        intent.putExtra("isIncomingCall", isIncomingCall);

        intent.putExtra("BookingID", bookingId);

        intent.putExtra("callType", callType);

        intent.putExtra("callerId", callee_caller_id);


        intent.putExtra("callerName", callee_caller_name);
        intent.putExtra("callerImage", callee_caller_image);

        intent.putExtra("callerIdentifier", callerIdentifier);

        intent.putExtra(VideoCallService.EXTRA_ROOMID, bookingId);
        intent.putExtra(VideoCallService.EXTRA_CALL_ID, call_id);



        /* -----------------------------------------------------*/

        // Get default codecs.
        String videoCodec = sharedPref.getString(keyprefVideoCodec,
                context.getString(R.string.pref_videocodec_default));
        String audioCodec = sharedPref.getString(keyprefAudioCodec,
                context.getString(R.string.pref_audiocodec_default));

        // Check HW codec flag.
        boolean hwCodec = sharedPref.getBoolean(keyprefHwCodecAcceleration,
                Boolean.valueOf(context.getString(R.string.pref_hwcodec_default)));

        // Check Capture to texture.
        boolean captureToTexture = sharedPref.getBoolean(keyprefCaptureToTexture,
                Boolean.valueOf(context.getString(R.string.pref_capturetotexture_default)));

        // Check Disable Audio Processing flag.
        boolean noAudioProcessing = sharedPref.getBoolean(keyprefNoAudioProcessingPipeline,
                Boolean.valueOf(context.getString(R.string.pref_noaudioprocessing_default)));

        // Check Disable Audio Processing flag.
        boolean aecDump = sharedPref.getBoolean(keyprefAecDump,
                Boolean.valueOf(context.getString(R.string.pref_aecdump_default)));

        // Check OpenSL ES enabled flag.
        boolean useOpenSLES = sharedPref.getBoolean(keyprefOpenSLES,
                Boolean.valueOf(context.getString(R.string.pref_opensles_default)));

        // Get video resolution from settings.
        int videoWidth = 0;
        int videoHeight = 0;
        String resolution = sharedPref.getString(keyprefResolution,
                context.getString(R.string.pref_resolution_default));
        String[] dimensions = resolution.split("[ x]+");
        if (dimensions.length == 2) {
            try {
                videoWidth = Integer.parseInt(dimensions[0]);
                videoHeight = Integer.parseInt(dimensions[1]);
            } catch (NumberFormatException e) {
                videoWidth = 0;
                videoHeight = 0;
            }
        }

        // Get camera fps from settings.
        int cameraFps = 0;
        String fps = sharedPref.getString(keyprefFps, context.getString(R.string.pref_fps_default));
        String[] fpsValues = fps.split("[ x]+");
        if (fpsValues.length == 2) {
            try {
                cameraFps = Integer.parseInt(fpsValues[0]);
            } catch (NumberFormatException e) {

                e.printStackTrace();
            }
        }

        // Check capture quality slider flag.
        boolean captureQualitySlider = sharedPref.getBoolean(keyprefCaptureQualitySlider,
                Boolean.valueOf(context.getString(R.string.pref_capturequalityslider_default)));

        // Get video and audio start bitrate.
        int videoStartBitrate = 0;
        String bitrateTypeDefault = context.getString(R.string.pref_startvideobitrate_default);
        String bitrateType = sharedPref.getString(keyprefVideoBitrateType, bitrateTypeDefault);
        if (!bitrateType.equals(bitrateTypeDefault)) {
            String bitrateValue = sharedPref.getString(keyprefVideoBitrateValue,
                    context.getString(R.string.pref_startvideobitratevalue_default));
            videoStartBitrate = Integer.parseInt(bitrateValue);
        }
        int audioStartBitrate = 0;
        bitrateTypeDefault = context.getString(R.string.pref_startaudiobitrate_default);
        bitrateType = sharedPref.getString(keyprefAudioBitrateType, bitrateTypeDefault);
        if (!bitrateType.equals(bitrateTypeDefault)) {
            String bitrateValue = sharedPref.getString(keyprefAudioBitrateValue,
                    context.getString(R.string.pref_startaudiobitratevalue_default));
            audioStartBitrate = Integer.parseInt(bitrateValue);
        }

        // Check statistics display option.
        boolean displayHud = sharedPref.getBoolean(keyprefDisplayHud,
                Boolean.valueOf(context.getString(R.string.pref_displayhud_default)));

        boolean tracing = sharedPref.getBoolean(keyprefTracing,
                Boolean.valueOf(context.getString(R.string.pref_tracing_default)));

        // Start AppRTCDemo activity.

        Uri uri = Uri.parse(roomUrl);
        intent.setData(uri);

        intent.putExtra(VideoCallService.EXTRA_LOOPBACK, false);
        intent.putExtra(VideoCallService.EXTRA_VIDEO_CALL, videoCallEnabled);
        intent.putExtra(VideoCallService.EXTRA_VIDEO_WIDTH, videoWidth);
        intent.putExtra(VideoCallService.EXTRA_VIDEO_HEIGHT, videoHeight);
        intent.putExtra(VideoCallService.EXTRA_VIDEO_FPS, cameraFps);
        intent.putExtra(VideoCallService.EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED, captureQualitySlider);
        intent.putExtra(VideoCallService.EXTRA_VIDEO_BITRATE, videoStartBitrate);
        intent.putExtra(VideoCallService.EXTRA_VIDEOCODEC, videoCodec);
        intent.putExtra(VideoCallService.EXTRA_HWCODEC_ENABLED, hwCodec);
        intent.putExtra(VideoCallService.EXTRA_CAPTURETOTEXTURE_ENABLED, captureToTexture);
        intent.putExtra(VideoCallService.EXTRA_NOAUDIOPROCESSING_ENABLED, noAudioProcessing);
        intent.putExtra(VideoCallService.EXTRA_AECDUMP_ENABLED, aecDump);
        intent.putExtra(VideoCallService.EXTRA_OPENSLES_ENABLED, useOpenSLES);
        intent.putExtra(VideoCallService.EXTRA_AUDIO_BITRATE, audioStartBitrate);
        intent.putExtra(VideoCallService.EXTRA_AUDIOCODEC, audioCodec);
        intent.putExtra(VideoCallService.EXTRA_DISPLAY_HUD, displayHud);
        intent.putExtra(VideoCallService.EXTRA_TRACING, tracing);
        intent.putExtra(VideoCallService.EXTRA_CMDLINE, false);
        intent.putExtra(VideoCallService.EXTRA_RUNTIME, 0);
        context.startService(intent);

    }

    /* This API is used to initiate Audio Video Call */
//    public static void initiateCall(Context context, String to, String callType) {


    public static void initiateCall(Context context, String to, String receiverName,
                                    String receiverImage, String callType,
                                    String receiverIdentifier, String callId, String bookingId, boolean isInComing) {

    /* Initiate call init socket API */





        /*
         * MQtt
         */
        // callInit(context, callType, to, call_id);
/*
 * To identify that the the call screen to be opened is for the outgoing call
 */

        startCall(context, callType, callId, to, receiverName, receiverImage, isInComing, receiverIdentifier,bookingId);

    /* Save this call id in shared preferences */
        SharedPreferences m_prefs =
                context.getSharedPreferences("global_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = m_prefs.edit();
        editor.putString("call_id", callId);
        editor.apply();
    }


}