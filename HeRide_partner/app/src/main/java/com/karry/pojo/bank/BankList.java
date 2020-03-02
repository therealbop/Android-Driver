package com.karry.pojo.bank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by murashid on 29-Aug-17.
 */

public class BankList implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("object")
    @Expose
    private String object;

    @SerializedName("account")
    @Expose
    private String account;

    @SerializedName("account_holder_name")
    @Expose
    private String account_holder_name;

    @SerializedName("account_holder_type")
    @Expose
    private String account_holder_type;

    @SerializedName("bank_name")
    @Expose
    private String bank_name;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("default_for_currency")
    @Expose
    private String default_for_currency;

    @SerializedName("fingerprint")
    @Expose
    private String fingerprint;

    @SerializedName("last4")
    @Expose
    private String last4;

    @SerializedName("routing_number")
    @Expose
    private String routing_number;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount_holder_name() {
        return account_holder_name;
    }

    public void setAccount_holder_name(String account_holder_name) {
        this.account_holder_name = account_holder_name;
    }

    public String getAccount_holder_type() {
        return account_holder_type;
    }

    public void setAccount_holder_type(String account_holder_type) {
        this.account_holder_type = account_holder_type;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDefault_for_currency() {
        return default_for_currency;
    }

    public void setDefault_for_currency(String default_for_currency) {
        this.default_for_currency = default_for_currency;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getRouting_number() {
        return routing_number;
    }

    public void setRouting_number(String routing_number) {
        this.routing_number = routing_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
