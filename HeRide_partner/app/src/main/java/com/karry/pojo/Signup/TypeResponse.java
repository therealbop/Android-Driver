package com.karry.pojo.Signup;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 15/5/17.
 */

public class TypeResponse implements Serializable {

    private String errNum;

    private String errMsg;

    private ArrayList<Type> data;

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

    public ArrayList<Type> getData() {
        return data;
    }

    public void setData(ArrayList<Type> data) {
        this.data = data;
    }

    public int getErrFlag() {
        return errFlag;
    }

    public void setErrFlag(int errFlag) {
        this.errFlag = errFlag;
    }
}
