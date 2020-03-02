package com.karry.telecall.callservice;

/**
 * Created by moda on 16/08/17.
 */

public interface AudioCallEvents {
    void onCallHangUp(int val, boolean received);

    void onMute();

    void onSpeaker();

}

