package com.karry.telecall.utility;

/**
 * Created by moda on 04/05/17.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;

//import com.servicegenie.pro.telecall.utility.AsyncHttpURLConnection;

/**
 * AsyncTask that converts an AppRTC room URL into the set of signaling
 * parameters to use with that room.
 */
public class RoomParametersFetcher {
    private static final String TAG = "RoomRTCClient";
    private static final int TURN_HTTP_TIMEOUT_MS = 5000;
    private final RoomParametersFetcherEvents events;
    private final String roomUrl;
    private final String roomMessage;
    private AsyncHttpURLConnection httpConnection;

    /**
     * Room parameters fetcher callbacks.
     */
    public interface RoomParametersFetcherEvents {
        /**
         * Callback fired once the room's signaling parameters
         * SignalingParameters are extracted.
         */
        void onSignalingParametersReady(final AppRTCClient.SignalingParameters params);

        /**
         * Callback for room parameters extraction error.
         */
        void onSignalingParametersError(final String description);
    }

    public RoomParametersFetcher(String roomUrl, String roomMessage,
                                 final RoomParametersFetcherEvents events) {
        this.roomUrl = roomUrl;
        this.roomMessage = roomMessage;
        this.events = events;

    }

    public void makeRequest() {


        httpConnection = new AsyncHttpURLConnection(
                "POST", roomUrl, roomMessage,
                new AsyncHttpURLConnection.AsyncHttpEvents() {
                    @Override
                    public void onHttpError(String errorMessage) {
                        Log.e(TAG, "Room connection error: " + errorMessage);
                        events.onSignalingParametersError(errorMessage);
                    }

                    @Override
                    public void onHttpComplete(String response) {
                        roomHttpResponseParse(response);
                    }
                });
        httpConnection.send();
    }

    private void roomHttpResponseParse(String response) {


        try {
            LinkedList<IceCandidate> iceCandidates = null;
            SessionDescription offerSdp = null;
            JSONObject roomJson = new JSONObject(response);

            String result = roomJson.getString("result");
            if (!result.equals("SUCCESS")) {
                events.onSignalingParametersError("Room response error: " + result);
                return;
            }
            response = roomJson.getString("params");
            roomJson = new JSONObject(response);
            String roomId = roomJson.getString("room_id");
            String clientId = roomJson.getString("client_id");
            String wssUrl = roomJson.getString("wss_url");
            String wssPostUrl = roomJson.getString("wss_post_url");
            boolean initiator = (roomJson.getBoolean("is_initiator"));
            if (!initiator) {
                iceCandidates = new LinkedList<IceCandidate>();
                String messagesString = roomJson.getString("messages");
                JSONArray messages = new JSONArray(messagesString);
                for (int i = 0; i < messages.length(); ++i) {
                    String messageString = messages.getString(i);
                    JSONObject message = new JSONObject(messageString);
                    String messageType = message.getString("type");

                    if (messageType.equals("offer")) {
                        offerSdp = new SessionDescription(
                                SessionDescription.Type.fromCanonicalForm(messageType),
                                message.getString("sdp"));
                    } else if (messageType.equals("candidate")) {
                        IceCandidate candidate = new IceCandidate(
                                message.getString("id"),
                                message.getInt("label"),
                                message.getString("candidate"));
                        iceCandidates.add(candidate);
                    } else {
                        Log.e(TAG, "Unknown message: " + messageString);
                    }
                }
            }


            LinkedList<PeerConnection.IceServer> iceServers =
                    iceServersFromPCConfigJSON(roomJson.getString("pc_config"));
//            boolean isTurnPresent = false;
//            for (PeerConnection.IceServer server : iceServers) {
//
//
//                if (server.uri.startsWith("turn:")) {
//                    isTurnPresent = true;
//                    break;
//                }
//            }
//
//
//            /**********/
//
//
//            /*
//             * Code b4 adding custom apprtc server
//             */
//            // Request TURN servers.
////            if (!isTurnPresent) {
////
////                LinkedList<PeerConnection.IceServer> turnServers =
////                        requestTurnServers(roomJson.getString("ice_server_url"));
////
////
////
////
////                for (PeerConnection.IceServer turnServer : turnServers) {
////                  //  Log.d(TAG, "TurnServer: " + turnServer);
////                    iceServers.add(turnServer);
////                }
////            }
//            /**********/
//
//
//            if (!isTurnPresent) {
//                JSONArray turnServerOverride = roomJson.getJSONArray("turn_server_override");
//
//
//                if (turnServerOverride.length() > 0) {
//
//
//                    /*
//                     * For credentials on our server turn
//                     */
//
//
//                    JSONObject turn = turnServerOverride.getJSONObject(2);
//                    String username = turn.getString("username");
//
//                    String credential = turn.getString("credential");
//                    JSONArray urls = turn.getJSONArray("urls");
//
//
//                    for (int i = 0; i < urls.length(); i++) {
//
//
//                        iceServers.add(new PeerConnection.IceServer(urls.getString(i), username, credential));
//
//                    }
//
//                    /*
//                     * For backup have added Xirsys server as well.
//                     *
//                     */
//                    turn = turnServerOverride.getJSONObject(0);
//                    urls = turn.getJSONArray("urls");
//                    username = turn.getString("username");
//
//                    credential = turn.getString("credential");
//
//                    for (int i = 0; i < urls.length(); i++) {
//
//
//                        iceServers.add(new PeerConnection.IceServer(urls.getString(i), username, credential));
//
//
//                    }
//
//
//                    JSONArray stunUrls = turnServerOverride.getJSONObject(1).getJSONArray("urls");
//                    iceServers.add(new PeerConnection.IceServer(stunUrls.getString(0), "", ""));
//                    iceServers.add(new PeerConnection.IceServer(stunUrls.getString(1), "", ""));
//
//
//                }
//
//            }


            AppRTCClient.SignalingParameters params = new AppRTCClient.SignalingParameters(
                    iceServers, initiator,
                    clientId, wssUrl, wssPostUrl,
                    offerSdp, iceCandidates);
            events.onSignalingParametersReady(params);
        } catch (JSONException e) {
            events.onSignalingParametersError(
                    "Room JSON parsing error: " + e.toString());
        }


//        catch (IOException e) {
//            events.onSignalingParametersError("Room IO error: " + e.toString());
//        }
    }


    // Requests & returns a TURN ICE Server based on a request URL.  Must be run
    // off the main thread!
    private LinkedList<PeerConnection.IceServer> requestTurnServers(String url)
            throws IOException, JSONException {


        /*************************************/
        LinkedList<PeerConnection.IceServer> turnServers = new LinkedList<PeerConnection.IceServer>();
//        Log.d(TAG, "Request TURN from: " + url);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("REFERER", "https://appr.tc");

//        connection.setRequestProperty("REFERER", "https://apprtc-181214.appspot.com");
        //    connection.setRequestProperty("REFERER", "https://apprtc-185308.appspot.com");

        connection.setConnectTimeout(TURN_HTTP_TIMEOUT_MS);
        connection.setReadTimeout(TURN_HTTP_TIMEOUT_MS);
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Non-200 response when requesting TURN server from " + url + " : "
                    + connection.getHeaderField(null));
        }
        InputStream responseStream = connection.getInputStream();
        String response = drainStream(responseStream);
        connection.disconnect();
//        Log.d(TAG, "TURN response: " + response);
        JSONObject responseJSON = new JSONObject(response);
        JSONArray iceServers = responseJSON.getJSONArray("iceServers");
        for (int i = 0; i < iceServers.length(); ++i) {
            JSONObject server = iceServers.getJSONObject(i);
            JSONArray turnUrls = server.getJSONArray("urls");
            String username = server.has("username") ? server.getString("username") : "";
            String credential = server.has("credential") ? server.getString("credential") : "";
            for (int j = 0; j < turnUrls.length(); j++) {
                String turnUrl = turnUrls.getString(j);
                turnServers.add(new PeerConnection.IceServer(turnUrl, username, credential));
            }
        }
        /*************************************/





        /*XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX*/


//        /*
//         * Test code for the chinese client not receiving the calls
//         */
//        LinkedList<PeerConnection.IceServer> turnServers = new LinkedList<PeerConnection.IceServer>();
//
//
//        String response = "{\"iceServers\":[{\"url\":\"stun:s2.xirsys.com\"}," +
//                "{\"username\":\"eea3db3e-6ac8-11e7-b215-4536994c74c7\"," +
//                "\"url\":\"turn:s2.xirsys.com:80?transport=udp\",\"credential\":\"eea3dc42-6ac8-11e7-8b50-71eabface3ca\"}," +
//                "{\"username\":\"eea3db3e-6ac8-11e7-b215-4536994c74c7\",\"url\":\"turn:s2.xirsys.com:3478?transport=udp\"," +
//                "\"credential\":\"eea3dc42-6ac8-11e7-8b50-71eabface3ca\"},{\"username\":\"eea3db3e-6ac8-11e7-b215-4536994c74c7\"," +
//                "\"url\":\"turn:s2.xirsys.com:80?transport=tcp\",\"credential\":\"eea3dc42-6ac8-11e7-8b50-71eabface3ca\"}," +
//                "{\"username\":\"eea3db3e-6ac8-11e7-b215-4536994c74c7\",\"url\":\"turn:s2.xirsys.com:3478?transport=tcp\"," +
//                "\"credential\":\"eea3dc42-6ac8-11e7-8b50-71eabface3ca\"},{\"username\":\"eea3db3e-6ac8-11e7-b215-4536994c74c7\"," +
//                "\"url\":\"turns:s2.xirsys.com:443?transport=tcp\",\"credential\":\"7eea3dc42-6ac8-11e7-8b50-71eabface3ca\"}," +
//                "{\"username\":\"eea3db3e-6ac8-11e7-b215-4536994c74c7\",\"url\":\"turns:s2.xirsys.com:5349?transport=tcp\"," +
//                "\"credential\":\"eea3dc42-6ac8-11e7-8b50-71eabface3ca\"}]}";
//
//
//        //String response = "{\"iceServers\":[{\"username\":\"eea3db3e-6ac8-11e7-b215-4536994c74c7\",\"url\":\"turn:s2.xirsys.com:80?transport=udp\",\"credential\":\"eea3dc42-6ac8-11e7-8b50-71eabface3ca\"},{\"username\":\"eea3db3e-6ac8-11e7-b215-4536994c74c7\",\"url\":\"turn:s2.xirsys.com:3478?transport=udp\",\"credential\":\"eea3dc42-6ac8-11e7-8b50-71eabface3ca\"},{\"username\":\"eea3db3e-6ac8-11e7-b215-4536994c74c7\",\"url\":\"turn:s2.xirsys.com:80?transport=tcp\",\"credential\":\"eea3dc42-6ac8-11e7-8b50-71eabface3ca\"},{\"username\":\"eea3db3e-6ac8-11e7-b215-4536994c74c7\",\"url\":\"turn:s2.xirsys.com:3478?transport=tcp\",\"credential\":\"eea3dc42-6ac8-11e7-8b50-71eabface3ca\"},{\"username\":\"eea3db3e-6ac8-11e7-b215-4536994c74c7\",\"url\":\"turns:s2.xirsys.com:443?transport=tcp\",\"credential\":\"7eea3dc42-6ac8-11e7-8b50-71eabface3ca\"},{\"username\":\"eea3db3e-6ac8-11e7-b215-4536994c74c7\",\"url\":\"turns:s2.xirsys.com:5349?transport=tcp\",\"credential\":\"eea3dc42-6ac8-11e7-8b50-71eabface3ca\"}]}";
//
//
//        JSONObject responseJSON = new JSONObject(response);
//
//
//        // responseJSON = responseJSON.getJSONObject("v");
//
//        JSONArray iceServers = responseJSON.getJSONArray("iceServers");
//        for (int i = 0; i < iceServers.length(); ++i) {
//            JSONObject server = iceServers.getJSONObject(i);
//            //  JSONArray turnUrls = server.getJSONArray("urls");
//            String username = server.has("username") ? server.getString("username") : "";
//            String credential = server.has("credential") ? server.getString("credential") : "";
//
//            turnServers.add(new PeerConnection.IceServer(server.getString("url"), username, credential));
//
//        }

        /*XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX*/
        return turnServers;
    }

    // Return the list of ICE servers described by a WebRTCPeerConnection
    // configuration string.
//    private LinkedList<PeerConnection.IceServer> iceServersFromPCConfigJSON(
//            String pcConfig) throws JSONException {
//        JSONObject json = new JSONObject(pcConfig);
//        JSONArray servers = json.getJSONArray("iceServers");
//        LinkedList<PeerConnection.IceServer> ret =
//                new LinkedList<PeerConnection.IceServer>();
//        for (int i = 0; i < servers.length(); ++i) {
//            JSONObject server = servers.getJSONObject(i);
//            String url = server.getString("urls");
//            String credential =
//                    server.has("credential") ? server.getString("credential") : "";
//            ret.add(new PeerConnection.IceServer(url, "", credential));
//        }
//        return ret;
//    }
    private LinkedList<PeerConnection.IceServer> iceServersFromPCConfigJSON(
            String pcConfig) throws JSONException {
        JSONObject json = new JSONObject(pcConfig);
        JSONArray servers = json.getJSONArray("iceServers");
        LinkedList<PeerConnection.IceServer> ret =
                new LinkedList<PeerConnection.IceServer>();


        String username ,credential;

        JSONObject server;

        JSONArray urls;
        for (int i = 0; i < servers.length(); ++i) {
            server = servers.getJSONObject(i);


            urls = server.getJSONArray("urls");



            if(server.has("username")){

                username = server.getString("username");

            }

            else{

                username ="";

            }

            if(server.has("credential")){
                credential = server.getString("credential");

            }

            else{


                credential ="";
            }





            for (int j = 0; j < urls.length(); j++) {


                ret.add(new PeerConnection.IceServer(urls.getString(j), username, credential));


            }


        }
        return ret;
    }

    // Return the contents of an InputStream as a String.
    private static String drainStream(InputStream in) {
        Scanner s = new Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}