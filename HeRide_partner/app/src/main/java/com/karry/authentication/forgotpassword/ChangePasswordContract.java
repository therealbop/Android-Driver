package com.karry.authentication.forgotpassword;

import android.widget.SeekBar;


/**
 * Created by murashid on 01-Feb-18.
 */

public interface ChangePasswordContract {

    interface View
    {
        /**
         * <h2>startProgressBar</h2>
         * This method is used to start the progressBar
         */
        void startProgressBar();

        /**
         * <h2>stopProgressBar</h2>
         * This method is used to stop the progresssBar
         */
        void stopProgressBar();

        /**
         * <h2>onFailure</h2>
         * This method is used to show the error with error message
         */
        void onFailure(String title, String msg);


        /**
         * <h2>onSuccess</h2>
         * This method is used to show the success for phone verification
         */
        void onSuccess(String msg);

        /**
         * <h2>enableClick</h2>
         * This method is used to senable half the click option
         */
        void enableHalfClick();


        /**
         * <h2>enableClick</h2>
         * This method is used to senable the click option
         */
        void enableClick();


          /**
         * <h2>invalidPassword</h2>
         * This method is used to senable the click option
         */
        void invalidPassword();

          /**
         * <h2>invalidConformPassword</h2>
         * This method is used to senable the click option
         */
        void invalidConformPassword();




    }

    interface Presenter
    {
        /**
         * <h2>validatePassword</h2>
         * This method is used to validate the phone
         * @param pass country code
         * @param confPass phone number
         */
        void validatePassword(String userId, String pass, String confPass);

        /**
         * <h2>setSeekBarProgress</h2>
         * This method is used to validate the email
         * @param seekBar seekBar
         * @param progress progress
         */
        void setSeekBarProgress(SeekBar seekBar, int progress);

        /**
         * <h2>onDestroyView</h2>
         * This method is called when onDestroy called
         */
        void onDestroyView();

    }
}
