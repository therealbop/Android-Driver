package com.karry.side_screens.wallet.changeCard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>CardDetailsDataModel</h1>
 * Used to hold the cards details
 * @author embed
 * @since on 25/11/15.
 */
public class CardDetailsDataModel implements Serializable
{
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("last4")
    @Expose
    private String last4;

    @SerializedName("brand")
    @Expose
    private String brand;

    @SerializedName("expMonth")
    @Expose
    private String expMonth;

    @SerializedName("funding")
    @Expose
    private String funding;

    @SerializedName("expYear")
    @Expose
    private String expYear;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("isDefault")
    @Expose
    private boolean isDefault;

    @SerializedName("paymentType")
    @Expose
    private int paymentType;

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLast4() {
        return last4;
    }

    public String getBrand() {
        return brand;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public boolean getDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        this.isDefault = aDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunding() {
        return funding;
    }
}
