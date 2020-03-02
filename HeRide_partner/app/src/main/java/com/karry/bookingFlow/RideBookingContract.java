package com.karry.bookingFlow;

import android.content.Intent;
import android.location.Location;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.karry.BaseView;
import com.karry.manager.location.LocationCallBack;
import com.karry.pojo.bookingAssigned.BookingAssignedDataRideAppts;
import com.karry.pojo.bookingAssigned.CustVehicleDetails;
import com.karry.pojo.bookingAssigned.TowTrayService;
import com.karry.pojo.bookingAssigned.TowtrayServiceSelectData;
import com.karry.utility.path_plot.LatLongBounds;

import java.util.ArrayList;

/**
 * <h1>RideBookingContract</h1>
 * <p>interface used in the Ride Booking View and Presenter</p>
 */
public interface RideBookingContract
{
    /**
     * <h1>RideBookingView</h1>
     * <p>interface which presenter inform to view for the changes after Implementation</p>
     */
    interface RideBookingView extends BaseView,LocationCallBack.View
    {

        /**
         * <h1>initActionBar</h1>
         * <p>method for initialize the ActionBar</p>
         */
        void initActionBar();

        /**
         * <h1>setTitle</h1>
         * <p>method for set the title of ActionBar</p>
         */
        void setTitle();

        /**
         * <h1>setText</h1>
         * <p>set the text in the Text views</p>
         * @param bookingAssignedDataRideAppts : assigned booking data for set into the UI
         */
        void setText(BookingAssignedDataRideAppts bookingAssignedDataRideAppts,String driver_name, String payment );

        /**
         * <h1>setBookigForSomeOne</h1>
         * <p>if the driver get the booking for someone else need to dispaly the details</p>
         */
        void setBookigForSomeOne();

        /**
         * <h1>setOnTheWayText</h1>
         * <p>set text change in On the way status</p>
         */
        void setOnTheWayText();

        /**
         * <h1>setArrivedText</h1>
         * <p>set text change in Arrived status</p>
         */
        void setArrivedText();

        /**
         * <h1>setArrivedText</h1>
         * <p>set text change in Arrived status</p>
         */
        void setArrivedTextTowtray();

        /**
         * <h1>setStartTripText</h1>
         * <p>set text change in start trip status</p>
         */
        void setStartTripText();

        /**
         * <h1>setStartTripTextTowtray</h1>
         * <p>set text change in start trip status</p>
         */
        void setStartTripTextTowtray();

        /**
         * <h1>setRunningTimer</h1>
         * <p>setting the running time in view</p>
         * @param time : the running time
         */
        void setRunningTimer(String time);

        /**
         * <h1>startInvoice</h1>
         * <p>start the Invoice Activity </p>
         * @param bid:booking id
         */
        void startInvoice(String bid);

        /**
         * <h1>moveGoogleMapToLocation</h1>
         * <p>the map location set</p>
         * @param newLatitude : Latitude
         * @param newLongitude : Longitude
         */
        void moveGoogleMapToLocation(double newLatitude, double newLongitude);


        /**
         * <h1>goToLogin</h1>
         * <p>for show the message and go to loginPage</p>
         * @param errMsg : error message
         */
        void goToLogin(String errMsg);

        /**
         * <h1>apiFailure</h1>
         * <p>if the status update is error, then the error message showing</p>
         * @param msg : error message
         */
        void apiFailure(String msg);

        /**
         * <h1>setCarMarker</h1>
         * <p>set the car marker through piccaso  </p>
         * @param latLng : latitude and longitude for the marker set
         * @param width : width of car marker
         * @param height : height of marker
         * @param carUrl : marker image url
         */
        void setCarMarker(LatLng latLng, int width, int height, String carUrl);

        /**
         * <h1>setCurrentLocation</h1>
         * <p>set the current location in map</p>
         * @param latLng
         */
        void setCurrentLocation(LatLng latLng);


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
         * <h1>dropLocDisable</h1>
         * <p>if the drop location is not available then hide the navigation and drop Address</p>
         */
        void dropLocDisable();

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
        void setDistanceShow(String distance);

        /**
         * <h1>setCarMove</h1>
         * <p>set the car movement for the icon</p>
         * @param latLng : Latitude and longitude
         * @param bearing : car movement
         */
        void setCarMove(LatLng latLng, float bearing);

        /**
         * <h1>bookingCancelledPas</h1>
         * <p>if the passenger cancelled the booking need to start home Activity</p>
         */
        void bookingCancelledPas();

        /**
         * <h1>showDropLocUpdated</h1>
         * <p>if the customer update the dropLocation show the message</p>
         */
        void showDropLocUpdated();

        /**
         * <h1>showDropLocUpdated</h1>
         * <p>if the customer update the dropLocation show the message</p>
         */
        void showDropLocAddress(String dropAddress);

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
         * <h1>setFlagPickupDrop</h1>
         * <p>set the pickup or drop location flag</p>
         * @param icon :flag icon
         * @param point : latitude and longitude
         */
        void setFlagPickupDrop(BitmapDescriptor icon,LatLng point);

        void googlePathPlot(LatLongBounds latLongBounds);


        void setServiceSelection(TowTrayService towtrayServiceSelectData);

        /**
         * <h1>showTowing</h1>
         * <p>for show the towing service start button</p>
         */
        void showTowing();

        /**
         * <h1>hideTowing</h1>
         * <p>for hide the towing service start button</p>
         */
        void hideTowing();

        void setCustVehSet(CustVehicleDetails custVehSet);

        void enableChat();

        void launchCallsScreen(String callId, String s);
    }

    /**
     * <h1>RideBookingPresenter</h1>
     * <p>the interface for inform to presenter implementation of data</p>
     */
    interface RideBookingPresenter extends LocationCallBack
    {
        /**
         * <h1>getRideBookingData</h1>
         * <p>fetch the data from the home screen and set the text</p>
         * @param rideBookingData :
         */
        void getRideBookingData(String rideBookingData);

        /**
         * <h1>getPreviousDistance</h1>
         * <p>get the distance from the server</p>
         */
        void getPreviousDistance();

        /**
         * <h1>setActionBar</h1>
         * <p>ActionBar initialize</p>
         */
        void setActionBar();

        /**
         * <h1>setActionBarTitle</h1>
         * <p>for set the title of the Activity</p>
         */
        void setActionBarTitle();

        /**
         * <h1>getCurrentLocation</h1>
         * <p>for start the location provider</p>
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
         * <h1>findCurrentLocation</h1>
         */
        void findCurrentLocation();

        /**
         * <h1>checkCurrentLocation</h1>
         * <p>check the current location and move</p>
         */
        void checkCurrentLocation();

        /**
         * <h1>updateBookingStatusRide</h1>
         * <p>the status update API and response handling</p>
         */
        void updateBookingStatusRide();

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

        /**
         * <h1>findBackgroundSeconds</h1>
         * <p>if the app is in background the time in seconds calculate and start the timer on forground</p>
         */
        void findBackgroundSeconds();

        /**
         * <h1>storeCurrentTime</h1>
         * <p>store the time if the app is pause state</p>
         */
        void storeCurrentTime();

        /**
         * <h1>getVehicleMoveBearing</h1>
         * <p>for the movement of the vehicle and bearing</p>
         * @param location : current location
         * @param projection : projection of map
         */
        void getVehicleMoveBearing(Location location, Projection projection);

        /**
         * <h1>clearComposite</h1>
         * <p>clear the compositeDisposable</p>
         */
        void clearComposite();

        /**
         * <h2>subscribeNetworkObserver</h2>
         * This method is used to check network availability
         */
        void subscribeNetworkObserver();

        /**
         * <h1>networkCheckOnresume</h1>
         * <p>if the app is in onresume state check the network available or not</p>
         */
        void networkCheckOnresume();

        /**
         * <h1>Set the pickup and Drop of location marker</h1>
         */
        /*void pickupDropMarkerSet();*/


        void onTowTryService(Intent intent);

        /**
         * <h1>postTowTruckServicesAPI</h1>
         * <p>Api call for the service selected</p>
         * @param serviceID
         */
        void postTowTruckServicesAPI(String serviceID);


        /**
         * <h1>startTowingApi</h1>
         * <p>start towing service, call API for start</p>
         */
        void startTowingApi();

        void checkChatEnable();

        void callInitialize(String audio, String customerId);

        String getCustomerId();

        String getName();
        String getProfilePic();
        String getPhone();

        boolean isTowtruck();

    }

}
