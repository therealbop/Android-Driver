package com.karry.side_screens.bankDetails.pojoforBank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by embed on 30/11/18.
 */

public class ConnectAccountCurrencyListSelection implements Serializable {

    @SerializedName("currencyID")
    @Expose
    private String currencyID;

    @SerializedName("selected")
    @Expose
    private boolean selected = false;

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
