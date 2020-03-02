package com.karry.side_screens.profile;

import android.widget.SeekBar;

import com.karry.countrypic.countrypic.CountryPicker;

/**
 * Created by murashid on 07-Feb-18.
 */

public interface EditProfileContract {

    interface View
    {
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
        void onFailure(String msg);

        /**
         * <h2>onFailure</h2>
         * This method is used to show the error
         */
        void onFailure();

        /**
         * <h2>onSuccessPhoneValidation</h2>
         * This method is used to show the success for phone validation
         */
        void onSuccessPhoneValidation(String userId);

        /**
         * <h2>onSuccessUpdate</h2>
         * This method is used to show the success for Email validation
         */
        void onSuccessUpdate(String message);

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

        /**
         * <h2>enableClick</h2>
         * This method is used to senable the click option 33 percentage
         */
        void enableClick33();

        /**
         * <h2>enableClick</h2>
         * This method is used to senable the click option 66 percentage
         */
        void enableClick66();


        /**
         * <h2>onEmailSelection()</h2>
         * This method is used to enable the email change
         */
        void onEmailSelection();


        /**
         * <h2>enableClick</h2>
         * This method is used to senable the phone change
         */
        void onMobileSelection();


        /**
         * <h2>enableClick</h2>
         * This method is used to senable the password change
         */
        void onPasswordSelection();



    }

    interface Presenter
    {
        /**
         * <h2>getCountryInfo</h2>
         * This method is used to get the country information
         * @param type 1 => phone , 2 => email
         */
        void setType(int type);

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
         * <h2>validateEmail</h2>
         * This method is used to validate the email
         * @param field email or phone number
         */
        void validateField(String field, String countryCode, boolean forApiCall);

        /**
         * <h2>validateEmail</h2>
         * This method is used to validate the password
         */
        void validatePasswordField(String oldPassword, String newPassword, String confirmPassword, boolean forApiCall);


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
