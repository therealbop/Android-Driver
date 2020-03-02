package com.karry.side_screens.help_center.zendeskpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h>SpinnerRowItem</h>
 * Created by Ali on 12/29/2017.
 */

public class SpinnerRowItem implements Serializable
{
    @SerializedName("colorId")
    @Expose
    private int colorId;

    @SerializedName("priority")
    @Expose
    private String priority;

    public SpinnerRowItem(int colorId, String priority) {
        this.colorId = colorId;
        this.priority = priority;
    }

    public int getColorId() {
        return colorId;
    }

    public String getPriority() {
        return priority;
    }
}
