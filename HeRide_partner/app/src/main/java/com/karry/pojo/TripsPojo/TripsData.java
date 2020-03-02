package com.karry.pojo.TripsPojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 23/5/17.
 */

public class TripsData implements Serializable {

    private ArrayList<Appointments> appointments;

    private ArrayList<TotalEarning> totalEarning;

    public ArrayList<Appointments> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointments> appointments) {
        this.appointments = appointments;
    }

    public ArrayList<TotalEarning> getTotalEarnings() {
        return totalEarning;
    }

    public void setTotalEarnings(ArrayList<TotalEarning> totalEarnings) {
        this.totalEarning = totalEarnings;
    }
}
