package com.karry.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ads on 09/05/17.
 */

public class OperatorsPojo implements Serializable {
    private int errNum;
    private int errFlag = -1;
    private String errMsg;
    private ArrayList<OperatorsData> data;

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public int getErrFlag() {
        return errFlag;
    }

    public void setErrFlag(int errFlag) {
        this.errFlag = errFlag;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public ArrayList<OperatorsData> getData() {
        return data;
    }

    public void setData(ArrayList<OperatorsData> data) {
        this.data = data;
    }
}
