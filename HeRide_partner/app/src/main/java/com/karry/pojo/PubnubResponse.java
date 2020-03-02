package com.karry.pojo;

import java.io.Serializable;



public class PubnubResponse implements Serializable {
    private String amount;
    private String dt;
    private String pickZone;
    private String dropZone;
    private String dropDt;
    private String bid;
    private String adr1;
    private String drop1;
    private String paymentType;
    private String helpers;
    private int a;
    private long serverTime;
    private String chn;
    private String dis;
    private String msg;
    private int ExpiryTimer;
    private int PingId;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getHelpers() {
        return helpers;
    }

    public void setHelpers(String helpers) {
        this.helpers = helpers;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getPickZone() {
        return pickZone;
    }

    public void setPickZone(String pickZone) {
        this.pickZone = pickZone;
    }

    public String getDropZone() {
        return dropZone;
    }

    public void setDropZone(String dropZone) {
        this.dropZone = dropZone;
    }

    public String getDropDt() {
        return dropDt;
    }

    public void setDropDt(String dropDt) {
        this.dropDt = dropDt;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getAdr1() {
        return adr1;
    }

    public void setAdr1(String adr1) {
        this.adr1 = adr1;
    }

    public String getDrop1() {
        return drop1;
    }

    public void setDrop1(String drop1) {
        this.drop1 = drop1;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public String getChn() {
        return chn;
    }

    public void setChn(String chn) {
        this.chn = chn;
    }

    public int getExpiryTimer() {
        return ExpiryTimer;
    }

    public void setExpiryTimer(int expiryTimer) {
        ExpiryTimer = expiryTimer;
    }

    public int getPingId() {
        return PingId;
    }

    public void setPingId(int pingId) {
        PingId = pingId;
    }
}
