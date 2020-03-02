package com.karry.side_screens.help_center.zendeskpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h>ZendeskHistory</h>
 * Created by Ali on 12/29/2017.
 */

public class ZendeskHistory implements Serializable
{

    @SerializedName("data")
    @Expose
    private ZendeskHistoryData data;

    public ZendeskHistoryData getData() {
        return data;
    }


}
