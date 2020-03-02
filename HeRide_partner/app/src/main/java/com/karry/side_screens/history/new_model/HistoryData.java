
package com.karry.side_screens.history.new_model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoryData implements Serializable
{
    @SerializedName("totalEarningData")
    @Expose
    private ArrayList<TotalEarningData> totalEarningData;

    @SerializedName("appointment")
    @Expose
    private ArrayList<AppointmentData> appointment;

    public ArrayList<TotalEarningData> getTotalEarningData ()
    {
        return totalEarningData;
    }

    public void setTotalEarningData (ArrayList<TotalEarningData> totalEarningData)
    {
        this.totalEarningData = totalEarningData;
    }

    public ArrayList<AppointmentData> getAppointmentData()
    {
        return appointment;
    }

    public void setAppointmentData(ArrayList<AppointmentData> appointmentData)
    {
        this.appointment = appointmentData;
    }

}
