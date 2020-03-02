package com.karry.side_screens.bankDetails.pojoforBank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 30/11/18.
 */

public class ConnectAccountCurrencyList implements Serializable {

    @SerializedName("default_currency")
    @Expose
    private String default_currency;

    @SerializedName("currency")
    @Expose
    private ArrayList<String> currency;


    @SerializedName("currencySelection")
    @Expose
    private ArrayList<ConnectAccountCurrencyListSelection> currencySelection;

    @SerializedName("position")
    @Expose
    private int position = -1;

    public String getDefault_currency() {
        return default_currency;
    }

    public void setDefault_currency(String default_currency) {
        this.default_currency = default_currency;
    }

    public ArrayList<String> getCurrency() {
        return currency;
    }

    public void setCurrency(ArrayList<String> currency) {
        this.currency = currency;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<ConnectAccountCurrencyListSelection> getCurrencySelection() {
        return currencySelection;
    }

    public void setCurrencySelection(ArrayList<ConnectAccountCurrencyListSelection> currencySelection) {
        this.currencySelection = currencySelection;
    }
}
