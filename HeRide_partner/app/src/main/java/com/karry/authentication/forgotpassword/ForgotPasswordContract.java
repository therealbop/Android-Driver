package com.karry.authentication.forgotpassword;

import android.widget.SeekBar;

import com.karry.countrypic.countrypic.CountryPicker;


/**
 * Created by murashid on 01-Feb-18.
 */

public interface ForgotPasswordContract {

    interface View
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
         * <h2>onGettingOfCountryInfo</h2>
         * This method is used to get the country Info
         * @param countryFlag flag of the country
         * @param countryCode country code
         * @param phoneMaxLength phone number max length
         */
        void onGettingOfCountryInfo(int countryFlag, String countryCode, int phoneMaxLength, boolean isDefault);

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
        void onFailure(String msg,String title);


        /**
         * <h2>onSuccessPhoneValidation</h2>
         * This method is used to show the success for phone validation
         */
        void onSuccessPhoneValidation(String countryCode, String mobile, String userId);

        /**
         * <h2>onSuccessUpdate</h2>
         * This method is used to show the success for Email validation
         */
        void onSuccessEmailValidation(String message);

         /**
         * <h2>disableClick</h2>
         * This method is used to disable the click option
         */
        void disableClick();

         /**
         * <h2>enableClick</h2>
         * This method is used to senable the click option
         */
        void enableClick();

    }

    interface Presenter
    {

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
         * <h2>getCountryInfo</h2>
         * This method is used to get the country information
         * @param countryPicker country picker
         */
        void getCountryInfo(CountryPicker countryPicker);

        /**
         * <h2>setCountryPicker</h2>
         * This method is used to get the country information by setting CountryPicker
         * @param countryPicker country picker
         */
        void setCountryPicker(CountryPicker countryPicker);

          /**
         * <h2>validatePhone</h2>
         * This method is used to validate the phone
         * @param countryCode country code
         * @param phoneNumber phone number
         */
          void validatePhone(String countryCode, String phoneNumber, boolean forApiCall);

          /**
         * <h2>validateEmail</h2>
         * This method is used to validate the email
         * @param email email
         */
        void validateEmail(String email, boolean forApiCall);

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
