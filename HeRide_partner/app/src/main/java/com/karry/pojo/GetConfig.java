package com.karry.pojo;

import java.io.Serializable;

/**
 * Created by Admin on 7/20/2017.
 */

public class GetConfig implements Serializable {

    private String errNum;

    private String errMsg;

    private ConfigData data;

    private String errFlag;

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

    public ConfigData getData() {
        return data;
    }

    public void setData(ConfigData data) {
        this.data = data;
    }

    public String getErrFlag() {
        return errFlag;
    }

    public void setErrFlag(String errFlag) {
        this.errFlag = errFlag;
    }
}
