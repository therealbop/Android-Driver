package com.karry.bookingFlow.model;


import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class LocationModel {

    private double vehicleImageWidth, VehicleImageHeight;
    private double driverMarkerWidth , driverMarkerHeight;
    private double currentLatitude ,currentLongitude;
    private double mapCenterLatitude ,mapCenterLongitude;
    private String nearestDriverLatLngEachType = "";
    private double prevMapCenterLatitude , prevMapCenterLongitude;
    private Location lastKnownLocation;
    private boolean moveToCurrLocation;
    private HashMap<String, String> etaOfEachType = new HashMap<>();
    private String vehicleName = "", vehicle_url = "", selectedVehicleId = "";
    private HashMap<String, String> driversMarkerIconUrls = new HashMap<>();
    private boolean isFromOnCreateView = false;
    private ArrayList<String> vehicleIdsHavingDrivers = new ArrayList<>();
    private boolean isFavAddress;
    private boolean favFieldShowing=false;
    private boolean toAnimate;//created to restrict first time animation of homepage
    private int favoriteType;
    private int bookingType;
    private String amount,timeFare,distFare,distance,duration;

    @Inject
    public LocationModel() {
    }

    public boolean isToAnimate() {
        return toAnimate;
    }

    public void setToAnimate(boolean toAnimate) {
        this.toAnimate = toAnimate;
    }

    public HashMap<String, String> getDriversMarkerIconUrls() {
        return driversMarkerIconUrls;
    }

    public void setDriversMarkerIconUrls(HashMap<String, String> driversMarkerIconUrls) {
        this.driversMarkerIconUrls = driversMarkerIconUrls;
    }


    public HashMap<String, String> getEtaOfEachType() {
        return etaOfEachType;
    }

    public void setEtaOfEachType(HashMap<String, String> etaOfEachType) {
        this.etaOfEachType = etaOfEachType;
    }

    public boolean isMoveToCurrLocation() {
        return moveToCurrLocation;
    }

    public void setMoveToCurrLocation(boolean moveToCurrLocation) {
        this.moveToCurrLocation = moveToCurrLocation;
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public double getPrevMapCenterLatitude() {
        return prevMapCenterLatitude;
    }

    public void setPrevMapCenterLatitude(double prevMapCenterLatitude) {
        this.prevMapCenterLatitude = prevMapCenterLatitude;
    }

    public double getPrevMapCenterLongitude() {
        return prevMapCenterLongitude;
    }

    public void setPrevMapCenterLongitude(double prevMapCenterLongitude) {
        this.prevMapCenterLongitude = prevMapCenterLongitude;
    }

    public double getMapCenterLatitude() {
        return mapCenterLatitude;
    }

    public void setMapCenterLatitude(double mapCenterLatitude) {
        this.mapCenterLatitude = mapCenterLatitude;
    }

    public double getMapCenterLongitude() {
        return mapCenterLongitude;
    }

    public void setMapCenterLongitude(double mapCenterLongitude) {
        this.mapCenterLongitude = mapCenterLongitude;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getNearestDriverLatLngEachType() {
        return nearestDriverLatLngEachType;
    }

    public void setNearestDriverLatLngEachType(String nearestDriverLatLngEachType) {
        this.nearestDriverLatLngEachType = nearestDriverLatLngEachType;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }
    public String getVehicle_url() {
        return vehicle_url;
    }

    public void setVehicle_url(String vehicle_url) {
        this.vehicle_url = vehicle_url;
    }

    public String getSelectedVehicleId() {
        return selectedVehicleId;
    }

    public void setSelectedVehicleId(String selectedVehicleId) {
        this.selectedVehicleId = selectedVehicleId;
    }
    public boolean isFromOnCreateView() {
        return isFromOnCreateView;
    }

    public void setFromOnCreateView(boolean fromOnCreateView) {
        isFromOnCreateView = fromOnCreateView;
    }
    public ArrayList<String> getVehicleIdsHavingDrivers() {
        return vehicleIdsHavingDrivers;
    }

    public boolean isFavAddress() {
        return isFavAddress;
    }

    public void setFavAddress(boolean favAddress) {
        isFavAddress = favAddress;
    }

    public double getVehicleImageWidth() {
        return vehicleImageWidth;
    }

    public void setVehicleImageWidth(double vehicleImageWidth) {
        this.vehicleImageWidth = vehicleImageWidth;
    }

    public double getVehicleImageHeight() {
        return VehicleImageHeight;
    }

    public void setVehicleImageHeight(double vehicleImageHeight) {
        this.VehicleImageHeight = vehicleImageHeight;
    }

    public double getDriverMarkerWidth() {
        return driverMarkerWidth;
    }

    public void setDriverMarkerWidth(double driverMarkerWidth) {
        this.driverMarkerWidth = driverMarkerWidth;
    }

    public double getDriverMarkerHeight() {
        return driverMarkerHeight;
    }

    public void setDriverMarkerHeight(double driverMarkerHeight) {
        this.driverMarkerHeight = driverMarkerHeight;
    }
}
