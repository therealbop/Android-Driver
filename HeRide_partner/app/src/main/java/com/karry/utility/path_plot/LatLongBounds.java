package com.karry.utility.path_plot;


import com.google.android.gms.maps.model.PolylineOptions;
import com.karry.app.bookingRequest.eta_model.Location;

/**
 * <h1>LatLongBounds</h1>
 * used to hold the latLng bounds
 * @author 3Embed
 * @since on 02-04-2018.
 */
public class LatLongBounds
{

    private Location northeast,southwest;
    private PolylineOptions polylineOptions;
    private EstimatedDistanceTime estimatedDistanceTime;

    public PolylineOptions getPolylineOptions() {
        return polylineOptions;
    }

    public void setNortheast(Location northeast) {
        this.northeast = northeast;
    }

    public void setSouthwest(Location southwest) {
        this.southwest = southwest;
    }

    public void setPolylineOptions(PolylineOptions polylineOptions) {
        this.polylineOptions = polylineOptions;
    }

    public Location getNortheast() {
        return northeast;
    }

    public Location getSouthwest() {
        return southwest;
    }

    public EstimatedDistanceTime getEstimatedDistanceTime() {
        return estimatedDistanceTime;
    }

    public void setEstimatedDistanceTime(EstimatedDistanceTime estimatedDistanceTime) {
        this.estimatedDistanceTime = estimatedDistanceTime;
    }
}
