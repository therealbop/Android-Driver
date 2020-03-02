package com.karry.telecall.callservice;

/**
 * Created by moda on 16/08/17.
 */

/**
 * Call control interface for container activity.
 */
public interface VideoCallEvents {
    void onCallHangUp(int val, boolean received);

    void onCameraSwitch();

    void onMute();

    void onVideoShow();
}
