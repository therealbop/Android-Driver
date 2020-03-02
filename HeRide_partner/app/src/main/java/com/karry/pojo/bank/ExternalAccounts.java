package com.karry.pojo.bank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by murashid on 29-Aug-17.
 */

public class ExternalAccounts implements Serializable{

    @SerializedName("data")
    @Expose
    ArrayList<BankList> data;

    public ArrayList<BankList> getData() {
        return data;
    }

    public void setData(ArrayList<BankList> data) {
        this.data = data;
    }
}
