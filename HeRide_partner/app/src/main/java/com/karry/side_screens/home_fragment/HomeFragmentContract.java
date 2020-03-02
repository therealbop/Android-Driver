package com.karry.side_screens.home_fragment;


import android.location.Location;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.karry.BaseView;
import com.karry.manager.location.LocationCallBack;
import com.karry.pojo.bookingAssigned.BookingAssignedDataRideAppts;
import com.karry.pojo.bookingAssigned.BookingAssignedResponse;

/**
 * <h1>HomeFragmentContract</h1>
 * <p>interface for the Home Fragment View and BookingPopUpPresenter</p>
 */
public interface HomeFragmentContract
{

    /**
     * <h1>HomeFragmentView</h1>
     * <p>the interface for home Fragment View</p>
     */
    interface HomeFragmentView extends BaseView,LocationCallBack.View
    {
        /**
         * <h1>setRideBookigView</h1>
         * <p>enable only RideBooking if available in Service</p>
         */
        void setRideBookigView();

        /**
         * <h1>setShipmentView</h1>
         * <p>enable only Shipment if available in Service</p>
         */
        void setShipmentView();

        /**
         * <h1>setShipmentRideBookingView</h1>
         * <p>enable both (Shipment and Ride Booking) if available in Service</p>
         */
        void setShipmentRideBookingView();

        /**
         * <h1>setMeterBooking</h1>
         * <p>enable Meter Booking if available in Ride Booking Service</p>
         */
        void setMeterBooking();

        /**
         * <h1>hideMeterBooking</h1>
         * <p>disable Meter Booking if not available in Ride Booking Service</p>
         */
        void hideMeterBooking();

        /**
         * <h1>drawAreaZones</h1>
         * @param polygonOptions :
         */
        void drawAreaZones(PolygonOptions polygonOptions);

        /**
         * <h1>goToLogin</h1>
         * <p>if the Unauthorized error is coming in Api need to go Login Screen.</p>
         */
        void goToLogin(String errMsg);

        void moveGoogleMapToLocation(double newLatitude, double newLongitude);

        void bookingEnabled(BookingAssignedDataRideAppts bookingAssignedDataRideAppts, String masterStatus);

        void callBookingAssigned();

        /**
         * <h1>getBookingsAssignedSuccess</h1>
         * <p>informing the view that booking Assigned API is success</p>
         * @param bookingAssignedResponse: the api response
         */
        void getBookingsAssignedSuccess(BookingAssignedResponse bookingAssignedResponse);


        /**
         * <h1>driverOnline</h1>
         * <p>if the driver is getting status 3, setting driver online</p>
         */
        void driverOnline();

        /**
         * <h1>drierOffline</h1>
         * <p>if the driver is getting status 4, setting driver offline</p>
         */
        void drierOffline();

        /**
         * <h1>driverBusy</h1>
         * <p>if the driver is getting status 5, setting driver busy</p>
         */
        void driverBusy();

        /*void checkOnlineOffline(boolean check);*/

        void setCurrentLocation(LatLng latLng);

        /**
         * <h1>changeDriverStatus</h1>
         * <p>manage the driver status</p>
         * @param status : online(3) or offline(4) or Busy(5)
         */
        void changeDriverStatus(int status);

        void locationUpdated(Location location);

        void setCarMove(LatLng latLng, float bearing);

        void setCarMarker(LatLng latLng,double width,double height,
                          String carUrl);


        /**
         * <h1>startRideBooking</h1>
         * <p>method to open the Ride Booking Activity</p>
         */
        void startRideBooking(String rideBookingData);

        /**
         * <h1>the map location is place to current location directly</h1>
         * @param location :LatLng
         */
        void moveCurrentLoc(Location location);

        /**
         * <h1>setInvoice</h1>
         * <p>if the pending review booking available open the Invoice Screen</p>
         * @param bookingID : booking id
         */
        void setInvoice(String bookingID);

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
         * <h1>setStartTripText</h1>
         * <p>set text change in start trip status</p>
         */
        void setStartTripText();

        /**
         * <h1>dropLocDisable</h1>
         * <p>if the drop location is not available then hide the navigation and drop Address</p>
         */
        void dropLocDisable();

        /**
         * <h1>showDialog</h1>
         * <p>showing the dialog with message</p>
         * @param message : message to show in dialog
         */
        void showDialog(String message);

        /**
         * <h1>onResume</h1>
         * <p>Activity onResume</p>
         */
        void onResume();

        /**
         * <h1>setAddress</h1>
         * <p>set the address to display</p>
         * @param :pickupAddrss or DropAddress
         */
        void setAddress(String address);

        /**
         * <h1>setWalletData</h1>
         * @param cashBalance :wallet balance
         * @param softLimit : soft limit
         * @param hardLimit : Hard Limit
         */
        void setWalletData(String cashBalance, String softLimit, String hardLimit);

        /**
         * <h1>rechargeWallet</h1>
         * <p>for show the wallet popup if the balance crossed</p>
         * @param cashBalance : wallet balance
         * @param softLimit : soft limit from the admin
         * @param hardLimit : hatd limit from the admin
         */
        void rechargeWallet(String cashBalance,String softLimit,String hardLimit);

        /**
         * <h1>addSergeInMap</h1>
         * @param location : location
         * @param text :serge text for show
         */
        void addSergeInMap(final LatLng location, final String text);

        /**
         * <h1>setQueuePosition</h1>
         * <p>set the queue position </p>
         * @param position : position
         */
        void setQueuePosition(String position);

        /**
         * <h1>hideQueuePosition</h1>
         * <p>hide the queue position</p>
         */
        void hideQueuePosition();

        void meterBookingSuccess(BookingAssignedDataRideAppts bookingAssignedDataRideAppts);
    }

    /**
     * <h1>HomeFragmentPresenter</h1>
     * <p>the interface for the presenter</p>
     */
    interface HomeFragmentPresenter extends LocationCallBack
    {

        /**
         * <h1>getServiceView</h1>
         * <p>check and enable the view which services are available for the selected vehicle</p>
         */
        void getServiceView();

        /**
         * <h1>checkMeterBooking</h1>
         * <p>check the meterBooking is available or not for the vehicle selected</p>
         * @param masterStatus : master status
         */
        void checkMeterBooking(String masterStatus);

        /**
         * <h>Attach CallbakView</h>
         * <p>this method is using to attach the view callback object to presenter</p>
         * @param view reference to View
         */
        void attachView(Object view);

        /**
         * <h>Detach View</h>
         * <p>this method is using to detach the object what we gave in atttach view to avoid possible memory leak</p>
         */
        void detachView();

        /**
         * <h1>setDriverStatus</h1>
         * <p>set the online offline status</p>
         * @param status: online or offline state
         */
        void setDriverStatus(int status);

        /**
         * <h1>getBookingsAssigned</h1>
         * <p>API call for the Booking list related data</p>
         */
        void getBookingsAssigned();

        void findCurrentLocation();

        /**
         * <h1>checkCurrentLocation</h1>
         * <p>check the current location</p>
         */
        void checkCurrentLocation();

        /**
         * <h2>getCurrentLocation</h2>
         * This method is used to get the current location of user
         */
        void getCurrentLocation();

        /**
         * <h2>disposeObservables</h2>
         * This method is used to dispose the observables
         */
        void disposeObservables();

        void handleResultFromIntents(String latitude, String longitude, String address);

        void getVehicleMoveBearing(Location location, Projection projection);

        void addCarMarker();

        void clearComposite();

        /**
         * <h1>getRideBookingData</h1>
         */
        void getRideBookingData();

        void getAreaZone();

        /**
         * <h1>setStatusChangeText</h1>
         * <p>which check the status match and set the view</p>
         * @param status :ride booking status
         */
        void setStatusChangeText(String status);

        /**
         * <h1>checkRideCancel</h1>
         * <p>check the Ride Booking canceled or not from passenger</p>
         */
        void checkRideCancel();

        /**
         * <h1>checkDropUpdate</h1>
         * <p>check the Ride Booking Drop location updated or not from passenger</p>
         */
        void checkDropUpdate();

        /**
         * <h1>checkBookingPopUp</h1>
         * <p>check whether the booking popup media player ring</p>
         */
        void checkBookingPopUp();

        void startMeterBookingAPI();

    }
}
