package com.karry.app.meterBooking.address;

import android.os.Bundle;

import com.karry.BaseView;
import com.karry.pojo.PlaceAutoCompletePojo;
import com.karry.pojo.bookingAssigned.BookingAssignedDataRideAppts;

import java.util.ArrayList;

/**
 * <h1>AddressSelectContract</h1>
 * This class is used to act as a link between view and presenter
 * @author 3Embed
 * @since on 20-01-2018.
 */

public interface AddressSelectContract
{
    interface View extends BaseView
    {

        void initActionBar();

        void setTitle();

        void addressFetchSuccess(ArrayList<PlaceAutoCompletePojo> placeAutoCompletePojos);

        void latLngFetchSuccess(Bundle bundle);

        /**
         * <h2>setTitleForDropAddress</h2>
         * This method is used to set the title for drop address
         */
        void setTitleForDropAddress();

        /**
         * <h2>showFavAddressListUI</h2>
         * This method is used to show the fav address list UI
         */
        void showFavAddressListUI();
        /**
         * <h2>hideFavAddressListUI</h2>
         * This method is used to hide the fav address list UI
         */
        void hideFavAddressListUI();

        /**
         * <h1>goToLogin</h1>
         * <p>if the Unauthorized error is coming in Api need to go Login Screen.</p>
         */
        void goToLogin(String errMsg);

        void meterBookingSuccess(BookingAssignedDataRideAppts bookingAssignedDataRideAppts);

        /**
         * <h1>hideSoftKeyboard</h1>
         * <p>for disable the keyboard</p>
         */
        void hideSoftKeyboard();

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
         * <h2>filterAddress</h2>
         * used to filter the address with constraint
         */
        void filterAddress();


        double getcount();
    }

    interface Presenter
    {

        void setActionBar();

        void setActionBarTitle();

        /**
         *<h>toggleFavAddressField</h>
         * <p>
         * This method is used to toggle the fav address icon
         * </p>
         * @param showFavAddressList Tells whether to address is fav
         *                           true means need to change UI for fav
         *                           else change UI for non fav
         */
        void toggleFavAddressField(final boolean showFavAddressList);


        void fidLatLng(PlaceAutoCompletePojo placeAutoCompletePojo,boolean toCallApi);



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
         * <h1>getRecentAddress</h1>
         * <p>fetch the recent address before done bookings</p>
         */
        void getRecentAddress();

        /**
         * <h1>networkCheckOnresume</h1>
         * <p>if the app is in onresume state check the network available or not</p>
         */
        void networkCheckOnresume();

        /**
         * <h2>rotateNextKey</h2>
         * used to rotate to next key
         */
        void rotateNextKey();

        /**
         * <h2>getStoredValues</h2>
         * used to get the stored values
         */
        String getServerKey();

        String getLat();
        String getLng();
    }
}
