package com.karry.telecall.utility;

/**
 * Created by moda on 04/05/17.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;

import de.tavendo.autobahn.WebSocket.WebSocketConnectionObserver;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;

/**
 * WebSocket client implementation.
 * <p/>
 * <p>All public methods should be called from a looper executor thread
 * passed in a constructor, otherwise exception will be thrown.
 * All events are dispatched on the same thread.
 */

public class WebSocketChannelClient {
    private static final String TAG = "WSChannelRTCClient";
    private static final int CLOSE_TIMEOUT = 1000;
    private final WebSocketChannelEvents events;
    private final LooperExecutor executor;
    private WebSocketConnection ws;
    private WebSocketObserver wsObserver;
    private String wsServerUrl;
    private String postServerUrl;
    private String roomID;
    private String clientID;
    private WebSocketConnectionState state;
    private final Object closeEventLock = new Object();
    private boolean closeEvent;
    // WebSocket send queue. Messages are added to the queue when WebSocket
    // client is not registered and are consumed in register() call.
    private final LinkedList<String> wsSendQueue;

    /**
     * Possible WebSocket connection states.
     */
    public enum WebSocketConnectionState {
        NEW, CONNECTED, REGISTERED, CLOSED, ERROR
    }

    /**
     * Callback interface for messages delivered on WebSocket.
     * All events are dispatched from a looper executor thread.
     */
    public interface WebSocketChannelEvents {
        void onWebSocketMessage(final String message);

        void onWebSocketClose();

        void onWebSocketError(final String description);
    }

    public WebSocketChannelClient(LooperExecutor executor, WebSocketChannelEvents events) {
        this.executor = executor;
        this.events = events;
        roomID = null;
        clientID = null;
        wsSendQueue = new LinkedList<String>();
        state = WebSocketConnectionState.NEW;
    }

    public WebSocketConnectionState getState() {
        return state;
    }

    public void connect(final String wsUrl, final String postUrl) {
        checkIfCalledOnValidThread();
        if (state != WebSocketConnectionState.NEW) {
            Log.e(TAG, "WebSocket is already connected.");
            return;
        }
        wsServerUrl = wsUrl;
        postServerUrl = postUrl;
        closeEvent = false;


        ws = new WebSocketConnection();
        wsObserver = new WebSocketObserver();
        try {
            ws.connect(new URI(wsServerUrl), wsObserver);
        } catch (URISyntaxException e) {
            reportError("URI error: " + e.getMessage());
        } catch (WebSocketException e) {
            reportError("WebSocket connection error: " + e.getMessage());
        }
    }

    public void register(final String roomID, final String clientID) {
        checkIfCalledOnValidThread();
        this.roomID = roomID;
        this.clientID = clientID;
        if (state != WebSocketConnectionState.CONNECTED) {
            Log.w(TAG, "WebSocket register() in state " + state);
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("cmd", "register");
            json.put("roomid", roomID);
            json.put("clientid", clientID);

            ws.sendTextMessage(json.toString());
            state = WebSocketConnectionState.REGISTERED;
            // Send any previously accumulated messages.
            for (String sendMessage : wsSendQueue) {
                send(sendMessage);
            }
            wsSendQueue.clear();
        } catch (JSONException e) {
            reportError("WebSocket register JSON error: " + e.getMessage());
        }
    }

    public void send(String message) {
        checkIfCalledOnValidThread();
        switch (state) {
            case NEW:
            case CONNECTED:
                // Store outgoing messages and send them after websocket client
                // is registered.

                wsSendQueue.add(message);
                return;
            case ERROR:
            case CLOSED:
                Log.e(TAG, "WebSocket send() in error or closed state : " + message);
                return;
            case REGISTERED:
                JSONObject json = new JSONObject();
                try {
                    json.put("cmd", "send");
                    json.put("msg", message);
                    message = json.toString();

                    ws.sendTextMessage(message);
                } catch (JSONException e) {
                    reportError("WebSocket send JSON error: " + e.getMessage());
                }
                break;
        }
        return;
    }

    // This call can be used to send WebSocket messages before WebSocket
    // connection is opened.
    public void post(String message) {
        checkIfCalledOnValidThread();
        sendWSSMessage("POST", message);
    }

    public void disconnect(boolean waitForComplete) {
        checkIfCalledOnValidThread();

        if (state == WebSocketConnectionState.REGISTERED) {
            // Send "bye" to WebSocket server.
            send("{\"type\": \"bye\"}");
            state = WebSocketConnectionState.CONNECTED;
            // Send http DELETE to http WebSocket server.
            sendWSSMessage("DELETE", "");
        }
        // Close WebSocket in CONNECTED or ERROR states only.
        if (state == WebSocketConnectionState.CONNECTED
                || state == WebSocketConnectionState.ERROR) {
            ws.disconnect();
            state = WebSocketConnectionState.CLOSED;

            // Wait for websocket close event to prevent websocket library from
            // sending any pending messages to deleted looper thread.
            if (waitForComplete) {
                synchronized (closeEventLock) {
                    while (!closeEvent) {
                        try {
                            closeEventLock.wait(CLOSE_TIMEOUT);
                            break;
                        } catch (InterruptedException e) {
                            Log.e(TAG, "Wait error: " + e.toString());
                        }
                    }
                }
            }
        }

    }

    private void reportError(final String errorMessage) {
        Log.e(TAG, errorMessage);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (state != WebSocketConnectionState.ERROR) {
                    state = WebSocketConnectionState.ERROR;
                    events.onWebSocketError(errorMessage);
                }
            }
        });
    }

    // Asynchronously send POST/DELETE to WebSocket server.
    private void sendWSSMessage(final String method, final String message) {
        String postUrl = postServerUrl + "/" + roomID + "/" + clientID;

        AsyncHttpURLConnection httpConnection = new AsyncHttpURLConnection(
                method, postUrl, message, new AsyncHttpURLConnection.AsyncHttpEvents() {
            @Override
            public void onHttpError(String errorMessage) {
                reportError("WS " + method + " error: " + errorMessage);
            }

            @Override
            public void onHttpComplete(String response) {
            }
        });
        httpConnection.send();
    }

    // Helper method for debugging purposes. Ensures that WebSocket method is
    // called on a looper thread.
    private void checkIfCalledOnValidThread() {
        if (!executor.checkOnLooperThread()) {
            throw new IllegalStateException(
                    "WebSocket method is not called on valid thread");
        }
    }

    private class WebSocketObserver implements WebSocketConnectionObserver {
        @Override
        public void onOpen() {

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    state = WebSocketConnectionState.CONNECTED;
                    // Check if we have pending register request.
                    if (roomID != null && clientID != null) {
                        register(roomID, clientID);
                    }
                }
            });
        }

        @Override
        public void onClose(WebSocketCloseNotification code, String reason) {

            synchronized (closeEventLock) {
                closeEvent = true;
                closeEventLock.notify();
            }
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    if (state != WebSocketConnectionState.CLOSED) {
                        state = WebSocketConnectionState.CLOSED;
                        events.onWebSocketClose();
                    }
                }
            });
        }

        @Override
        public void onTextMessage(String payload) {

            final String message = payload;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    if (state == WebSocketConnectionState.CONNECTED
                            || state == WebSocketConnectionState.REGISTERED) {
                        events.onWebSocketMessage(message);
                    }
                }
            });
        }

        @Override
        public void onRawTextMessage(byte[] payload) {
        }

        @Override
        public void onBinaryMessage(byte[] payload) {
        }
    }

}