package com.karry.manager.mqtt_manager;

/**
 * Created by moda on 29/06/17.
 */


public enum MqttEvents {

    Call("Call"),

    CallsAvailability("CallsAvailability"),

    PresenceTopic("PresenceTopic"),
    WillTopic("lastWill");
    public String value;


    MqttEvents(String value) {
        this.value = value;
    }


}