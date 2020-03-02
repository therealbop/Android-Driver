package com.karry.app.meterBooking.address.auto_complete_prediction_utils;


import android.location.Location;

public   class QueryWithCurrentLocation {
    public final String  query;
    public final Location location;

    public QueryWithCurrentLocation(String query, Location location) {
        this.query = query;
        this.location = location;
    }
}
