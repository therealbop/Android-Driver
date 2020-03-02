package com.karry.app.mainActivity;


import android.content.Intent;

import com.karry.BaseView;

public interface MainActivityContract
{
    interface MainActivityView extends BaseView
    {
        /**
         * <h1>setProfilePicImg</h1>
         * <p>set the name and Profile pic in the navigation drawer</p>
         * @param profilePic :profile picture URL
         * @param name : Name of the Driver
         */
        void setProfilePicImg(String profilePic, String name);

        /**
         * <h2>hideSoftKeyboard</h2>
         * This method is used to hide the keyboard
         */
        void hideSoftKeyboard();

        /**
         * <h2>clearFocus</h2>
         * This method is used to clear the focus on all edit texts
         */

        void showSoftKeyboard();

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
         * <h1>mandatoryUpdateDialog</h1>
         * <p>show the mandatory dialog</p>
         */
        void mandatoryUpdateDialog(boolean isMandatoryUpdateEnable);

        /**
         * <h1>onSuccesLogout</h1>
         * <p>if tha Logout API is success</p>
         */
        void onSuccesLogout();

        /**
         * <h1>openBookingPopUp</h1>
         */
        void openBookingPopUp(String data);

        void openLiveChat(String myName);


    }

    interface MainActivityPresenter
    {
        /**
         * <h1>getProfilePicImg</h1>
         * <p>get the profile Pic and name from already stored</p>
         */
        void getProfilePicImg();

        /**
         * <h1>getAppConfig</h1>
         * <p>API call for getting APP config</p>
         */
        void getAppConfig();

        /**
         * <h1>hideKeyboardAndClearFocus</h1>
         * <p>for hide the keyboard</p>
         */
        void hideKeyboardAndClearFocus();

        /**
         * <h1>showKeyboard</h1>
         * <p>for show the Keyboard</p>
         */
        void showKeyboard();

        /**
         * <h2>subscribeNetworkObserver</h2>
         * This method is used to check network availability
         */
        void subscribeNetworkObserver();

        /**
         * <h1>clear</h1>
         * <p>compositeDisposable clear</p>
         */
        void clear();

        /**
         *<h1>getWalletAmount</h1>
         * <p>fetch the wallet amount</p>
         */
        String getWalletAmount();

        /**
         * <h1>logOut</h1>
         * <p>logout API call</p>
         */
        void logout();

        /**
         * <h1>networkCheckOnresume</h1>
         * <p>if the app is in onresume state check the network available or not</p>
         */
        void networkCheckOnresume();

        /**
         * <h1>getBroadCastReciever</h1>
         */
        void getBroadCastReciever(Intent intent);

        void preferenceZoneClick();

        void checkForNetwork(boolean isConnected);

        void getLiveChat();

        boolean checkZenDesk();
    }
}
