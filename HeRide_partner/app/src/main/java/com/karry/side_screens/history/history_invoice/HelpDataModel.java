package com.karry.side_screens.history.history_invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

class HelpDataModel implements Serializable {

    @SerializedName("desc")
    @Expose
    private String desc;

    @SerializedName("subcat")
    @Expose
    private String[] subcat;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("bid")
    @Expose
    private String bid;

    public String getDesc ()
    {
        return desc;
    }

    public void setDesc (String desc)
    {
        this.desc = desc;
    }

    public String[] getSubcat ()
    {
        return subcat;
    }

    public void setSubcat (String[] subcat)
    {
        this.subcat = subcat;
    }

    public String getLink ()
    {
        return link;
    }

    public void setLink (String link)
    {
        this.link = link;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }
}
