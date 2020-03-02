package com.karry.app.bookingRequest;


import android.app.Activity;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.karry.utility.AppTypeFace;
import com.karry.utility.path_plot.LatLongBounds;

public interface BookingPopUpContract {

    interface BookingPoUpView {

        /**
         * <h1>setTextData</h1>
         * <p>set the text for the booking popup</p>
         * @param bookingDataResponse : response from the ACK
         * @param pickupTime : time
         */
        void setTextData(BookingDataResponse bookingDataResponse,String pickupTime,String payment_method);

        /**
         * <h1>onSuccess</h1>
         * <p>API success</p>
         */
        void onSuccess();

        void onError(String error);

        void showProgressbar();

        void dismissProgressbar();

        void onTimerChanged(int progress, String time);

        void onFinish();

        void startMusicPlayer();

        /**
         * <h1>enableCorporateBook</h1>
         * <p>if the booking is corporate enable the text</p>
         */
        void enableCorporateBook();

        /**
         * <h1>disableCorporateBook</h1>
         * <p>if the booking is not corporate disable the text</p>
         */
        void disableCorporateBook();

        /**
         * <h1>hidePreferences</h1>
         * <p>hide the view of preferences if the preference is empty</p>
         */
        void hidePreferences();

        void googlePathPlot(LatLongBounds latLongBounds);

        /**
         * <h1>moveGoogleMapToLocation</h1>
         * <p>the map location set</p>
         * @param newLatitude : Latitude
         * @param newLongitude : Longitude
         */
        void moveGoogleMapToLocation(double newLatitude, double newLongitude, String carUrl);

        /**
         * <h1>setFlagPickupDrop</h1>
         * <p>set the pickup or drop location flag</p>
         * @param icon :flag icon
         * @param point : latitude and longitude
         */
        void setFlagPickupDrop(BitmapDescriptor icon, LatLng point, String distaceTime);

        /**
         * <h1>setFlagPickupDrop</h1>
         * <p>set the pickup or drop location flag</p>
         * @param icon :flag icon
         * @param point : latitude and longitude
         */
        void setFlagDrop(BitmapDescriptor icon, LatLng point/*, String distaceTime*/);


        void setTowTray();

        void disableDropLocation();

        void enableRentalBooking();

        void enableHotelBooking();

    }

    interface BookingPopUpPresenter {

        /**
         * <h1>getData</h1>
         * <p>get the data from the activity</p>
         */
        void getData();

        /**
         * <h1></h1>
         * @param response
         */
        void updateApptRequest(String response);

        /**
         * <h1>findCurrentLocation</h1>
         */
        void findCurrentLocation();

        /**
         * <h1>reboundMap</h1>
         * <p>if the map is zoom and out, for bound again </p>
         */
        void reboundMap();

        void setServiceListDialog(Activity mActivity, AppTypeFace appTypeFace);
    }

}

