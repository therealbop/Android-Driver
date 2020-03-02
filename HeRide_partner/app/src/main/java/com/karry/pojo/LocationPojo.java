package com.karry.pojo;

/**
 * Created by embed on 10/6/17.
 */

public class LocationPojo {

    private int errNum;

    private String errMsg;

    private String statusCode;

    private LocationPojoData data;

    private int errFlag;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

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

    public LocationPojoData getData() {
        return data;
    }

    public void setData(LocationPojoData data) {
        this.data = data;
    }

    public int getErrFlag() {
        return errFlag;
    }

    public void setErrFlag(int errFlag) {
        this.errFlag = errFlag;
    }
}
