package com.karry.pojo.TripsPojo;

import java.io.Serializable;

/**
 * Created by embed on 23/5/17.
 */

public class TripsPojo implements Serializable {

    private int statusCode;

    private int errNum;

    private String errMsg;

    private TripsData data;

    private int errFlag;

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public TripsData getData() {
        return data;
    }

    public void setData(TripsData data) {
        this.data = data;
    }

    public int getErrFlag() {
        return errFlag;
    }

    public void setErrFlag(int errFlag) {
        this.errFlag = errFlag;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
