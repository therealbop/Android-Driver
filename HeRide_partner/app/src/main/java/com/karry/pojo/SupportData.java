package com.karry.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 8/3/2017.
 */

public class SupportData implements Serializable {

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("desc")
    @Expose
    private String desc;

    @SerializedName("subcat")
    @Expose
    private ArrayList<SupportData> subcat;

    @SerializedName("link")
    @Expose
    private String link = "";

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<SupportData> getSubcat() {
        return subcat;
    }

    public void setSubcat(ArrayList<SupportData> subcat) {
        this.subcat = subcat;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
