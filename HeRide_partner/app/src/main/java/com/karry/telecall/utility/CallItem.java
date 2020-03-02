package com.karry.telecall.utility;

/**
 * Created by moda on 26/07/17.
 */

public class CallItem {


    private String receiverImage;
    private String receiverName;
    private String receiverUid;
    private String callInitiateTime;
    private String callId;
    private String callType;


    private boolean callNotAllowed;

    public boolean isCallNotAllowed() {
        return callNotAllowed;
    }

    public void setCallNotAllowed(boolean callNotAllowed) {
        this.callNotAllowed = callNotAllowed;
    }

    private boolean receiverInContacts;

    public boolean isReceiverInContacts() {
        return receiverInContacts;
    }

    public void setReceiverInContacts(boolean receiverInContacts) {
        this.receiverInContacts = receiverInContacts;
    }

    /**
     * Can be receiver phoneNumber,email or the userName
     */


    private String receiverIdentifier;

    private boolean callInitiated;

    //    private int callType;


    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }


    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getCallInitiateTime() {
        return callInitiateTime;
    }

    public void setCallInitiateTime(String callInitiateTime) {
        this.callInitiateTime = callInitiateTime;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public boolean isCallInitiated() {
        return callInitiated;
    }

    public void setCallInitiated(boolean callInitiated) {
        this.callInitiated = callInitiated;
    }

    public String getReceiverIdentifier() {
        return receiverIdentifier;
    }

    public void setReceiverIdentifier(String receiverIdentifier) {
        this.receiverIdentifier = receiverIdentifier;
    }
}
