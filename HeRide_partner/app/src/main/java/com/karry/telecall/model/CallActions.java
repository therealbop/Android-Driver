package com.karry.telecall.model;

public enum  CallActions {
    NEW_CALL("1"),
    NOT_ANSWER_OR_LEFT("2"),
    JOIN_ON_CALL("3"),
    CALL_ENDED("4");
    public String value;
    CallActions(String value) {
        this.value = value;
    }
}
