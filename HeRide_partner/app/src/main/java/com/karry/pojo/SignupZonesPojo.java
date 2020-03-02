package com.karry.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 16/5/17.
 */

public class SignupZonesPojo implements Serializable {

    private String errNum;

    private String errMsg;

    private ArrayList<SignupZonedata> data;

    private int errFlag;

    public String getErrNum() {
        return errNum;
    }

    public void setErrNum(String errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public ArrayList<SignupZonedata> getData() {
        return data;
    }

    public void setData(ArrayList<SignupZonedata> data) {
        this.data = data;
    }

    public int getErrFlag() {
        return errFlag;
    }

    public void setErrFlag(int errFlag) {
        this.errFlag = errFlag;
    }
}
