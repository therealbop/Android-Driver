package com.karry.app.meterBooking;

import android.content.Intent;
import android.location.Location;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.karry.BaseView;
import com.karry.manager.location.LocationCallBack;
import com.karry.pojo.bookingAssigned.BookingAssignedDataRideAppts;

/**
 * <h1>DriverMeterContract</h1>
 * <p>the interfaces for Driver Meter View and presenter.</p>
 */

public interface DriverMeterContract {

    /**
     * <h1>DriverMeterView</h1>
     * <p>interface which is for the View</p>
     */
    interface DriverMeterView extends BaseView{

        /**
         * <h1>goToLogin</h1>
         * <p>if the Unauthorized error is coming in Api need to go Login Screen.</p>
         */
        void goToLogin(String errMsg);

        /**
         * <h1>apiFailure</h1>
         * <p>if the MeterBooking complete Api call fails</p>
         * @param msg : API error message
         */
        void apiFailure(String msg);

        /**
         * <h1>setRunningTimer</h1>
         * <p>setting the running time in view</p>
         * @param time : the running time
         */
        void setRunningTimer(String time);

        /**
         * <h1>moveGoogleMapToLocation</h1>
         * <p>set the current location in the map</p>
         * @param newLatitude : current Latitude
         * @param newLongitude : current Longitude
         */
        void moveGoogleMapToLocation(double newLatitude, double newLongitude);

        /**
         * <h1>setAddress</h1>
         * <p>set the drop Address</p>
         * @param Address : Drop Address
         */
        void setAddress(String Address);

        /**
         * <h1>connectGoogleMap</h1>
         * <p>the navigation of google map</p>
         * @param uri :the navigation url
         */
        void connectGoogleMap(String uri);

        /**
         * <h1>connectWaze</h1>
         * <p>the navigation of Waze</p>
         * @param uri : the navigation Uri
         */
        void connectWaze(String uri);

        /**
         * <h1>fareSet</h1>
         * <p>the values set in the Bottom layout</p>
         * @param meterFare :meter fare
         * @param timeFare : time fare
         * @param distanceFare : distance fare
         * @param totalFare : total fare
         */
        void fareSet(String meterFare, String timeFare, String distanceFare, String totalFare);

        /**
         * <h1>showInvoice</h1>
         * <p>after complete the trip, atart activity if invoice, and passing the data </p>
         * @param driverMeterInvoiceData : Invoice  data
         */
        void showInvoice(DriverMeterInvoiceData driverMeterInvoiceData);

        /**
         * <h1>navigationEnable</h1>
         * @param enable
         */
        void navigationEnable(boolean enable);

        /**
         * <h1>setCurrentLocation</h1>
         * <p>set the current location in map</p>
         * @param latLng
         */
        void setCurrentLocation(LatLng latLng);

        /**
         * <h1>setCarMarker</h1>
         * <p>set the car marker through piccaso  </p>
         * @param latLng : latitude and longitude for the marker set
         * @param width : width of car marker
         * @param height : height of marker
         * @param carUrl : marker image url
         */
        void setCarMarker(LatLng latLng,double width,double height,
                          String carUrl);

        /**
         * <h1>locationUpdated</h1>
         * <p>when location change then the presenter informing to view for the change of icon move</p>
         * @param location : new updated location
         */
        void locationUpdated(Location location);

        /**
         * <h1>setDistanceShow</h1>
         * <p>for set the distance traveled </p>
         * @param distance : calculated distance
         */
        void setDistanceShow(Double distance);

        /**
         * <h1>setCarMove</h1>
         * <p>set the car movement for the icon</p>
         * @param latLng : Latitude and longitude
         * @param bearing : car movement
         */
        void setCarMove(LatLng latLng, float bearing);

        /**
         * <h2>networkNotAvailable</h2>
         * This method is triggered when network is not available
         */
        void networkNotAvailable();

        /**
         * <h2>networkAvailable</h2>
         * This method is triggered when network is available
         */
        void networkAvailable();

        /**
         * <h1>minimumFareGreatUI</h1>
         * <p>if the minimum fare is greater compare to calculated fare then the UI need to set</p>
         */
        void minimumFareGreatUI();

        /**
         * <h1>calculatedFareGreatUI</h1>
         * <p>if the calculated fare is greater compare to minimum fare then the UI need to set</p>
         */
        void calculatedFareGreatUI();
    }

    /**
     * <h1>DriverMeterPresenter</h1>
     * <p>the interface for the presenter</p>
     */
    interface DriverMeterPresenter extends LocationCallBack {

        /**
         * <h1>setBookingAssignedDataRideAppts</h1>
         * @param bookingAssignedDataRideAppts : pojo class
         */
        void setBookingAssignedDataRideAppts(BookingAssignedDataRideAppts bookingAssignedDataRideAppts);

        void completeBooking(String bid);

        /**
         * <h1>startTimer</h1>
         * <p>timer runner and calculate the time fare</p>
         * @param seconds : time in seconds for start
         */
        void startTimer(String seconds);

        /**
         * <h1>enableRunTime</h1>
         * <p>enable or disable the timer run</p>
         * @param enable : value is boolean
         */
        void enableRunTime(boolean enable);

        void findCurrentLocation();

        void fetchAddress(Intent data, String bid);

        /*void getStoredValues();*/

        /**
         * <h1>startGoogleMap</h1>
         * <p>set the url for the google map and inform to view</p>
         */
        void startGoogleMap();

        /**
         * <h1>startWaze</h1>
         * <p>set the url for the waze map and inform to view</p>
         */
        void startWaze();

        /**
         * <h1>checkCurrentLocation</h1>
         * <p>check the current location and move</p>
         */
        void checkCurrentLocation();


        /**
         * <h2>getCurrentLocation</h2>
         * This methos is used to get the current location of user
         */
        void getCurrentLocation();


        /**
         * <h2>disposeObservables</h2>
         * This method is used to dispose the observables
         */
        void disposeObservables();

        /**
         * <h1>addCarMarker</h1>
         * <p>set the car marker height and width an location also</p>
         */
        void addCarMarker();

        /**
         * <h1>getVehicleMoveBearing</h1>
         * <p>for the movement of the vehicle and bearing</p>
         * @param location : current location
         * @param projection : projection of map
         */
        void getVehicleMoveBearing(Location location, Projection projection);

        void setDropLocationAPI(String dropAddress, String dropLatLng, String dropPlaceID, String bid);

        /**
         * <h2>subscribeNetworkObserver</h2>
         * This method is used to check network availability
         */
        void subscribeNetworkObserver();

        /**
         * <h1>clearComposite</h1>
         * <p>clear the compositeDisposable</p>
         */
        void clearComposite();

        /**
         * <h1>getPreviousDistance</h1>
         * <p>get the distance from the server</p>
         */
        void getPreviousDistance();

        /**
         * <h1>storeCurrentTime</h1>
         * <p>store the time if the app is pause state</p>
         */
        void storeCurrentTime();

        /**
         * <h1>findBackgroundSeconds</h1>
         * <p>if the app is in background the time in seconds calculate and start the timer on forground</p>
         */
        void findBackgroundSeconds();

        /**
         * <h1>networkCheckOnresume</h1>
         * <p>if the app is in onresume state check the network available or not</p>
         */
        void networkCheckOnresume();

    }
}
