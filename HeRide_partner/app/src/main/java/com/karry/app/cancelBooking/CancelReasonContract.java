package com.karry.app.cancelBooking;


import com.karry.BaseView;

import java.util.ArrayList;

/**
 * <h1>CancelReasonContract</h1>
 * <p>the interface which use for the Cancel Reason View and BookingPopUpPresenter.</p>
 */
public interface CancelReasonContract {

    interface CancelReasonView extends BaseView{

        void initActionBar();

        void setTitle();

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

        void goToLogin(String errMsg);

        void apiFailure(String msg);

        void cancellationReasonSuccess(ArrayList<String> reasons);

        void cancelSuccess();

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

    }

    interface CancelReasonPresenter{

        void setActionBar();

        void setActionBarTitle();

        void hideKeyboardAndClearFocus();

        void showKeyboard();

        void getCancelationReason();

        void cancelBooking(String reason);

        /**
         * <h1>networkCheckOnresume</h1>
         * <p>if the app is in onresume state check the network available or not</p>
         */
        void networkCheckOnresume();

        /**
         * <h2>subscribeNetworkObserver</h2>
         * This method is used to check network availability
         */
        void subscribeNetworkObserver();

    }
}
