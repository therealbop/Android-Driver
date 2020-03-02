package com.karry.side_screens.history.history_invoice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HistoryInvoiceReceipt implements Serializable
{

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("value")
    @Expose
    private String value;

    public HistoryInvoiceReceipt(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
