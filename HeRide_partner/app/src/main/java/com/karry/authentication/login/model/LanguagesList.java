package com.karry.authentication.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LanguagesList implements Serializable {


    public LanguagesList(String code, String name, int isRTL)
    {
        this.code = code;
        this.name = name;
        this.langDirection = isRTL;
    }

    @SerializedName("langDirection")
    @Expose
    private int langDirection;

    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("active")
    @Expose
    private String active;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("languageId")
    @Expose
    private String languageId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public int getLangDirection() {
        return langDirection;
    }

    public void setLangDirection(int langDirection) {
        this.langDirection = langDirection;
    }
}
