package com.karry.pojo.bank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by murashid on 29-Aug-17.
 */

public class StripeDetailsPojo implements Serializable{

    @SerializedName("legal_entity")
    @Expose
    private LegalEntity legal_entity;


    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("external_accounts")
    @Expose
    private ExternalAccounts external_accounts;


    public LegalEntity getLegal_entity() {
        return legal_entity;
    }

    public void setLegal_entity(LegalEntity legal_entity) {
        this.legal_entity = legal_entity;
    }

    public ExternalAccounts getExternal_accounts() {
        return external_accounts;
    }

    public void setExternal_accounts(ExternalAccounts external_accounts) {
        this.external_accounts = external_accounts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
