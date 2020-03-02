package com.karry.authentication.forgotpassword;

import android.widget.SeekBar;


/**
 * <h1>OTPVerificationContract</h1>
 * <p>interface for the OTP verification View and for Implementation</p>
 */
public interface OTPVerificationContract {

    /**
     * <h1>View</h1>
     * <p>the interface for the View</p>
     */
    interface View
    {

        /**
         * <h2>setWhiteColorForAction</h2>
         * This method is used to set the color for action button
         */
        void setWhiteColorForAction();

        /**
         * <h2>setDarkColorForAction</h2>
         *  This method is used to set the color for action button
         */
        void setDarkColorForAction();

        /**
         * <h2>setColorPrimayForAction</h2>
         *  This method is used to set the color for action button
         */
        void setColorPrimayForAction();


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
         * <h2>onSuccessOtpVerified</h2>
         * This method is used to show the success for otp verification
         */
        void onSuccessOtpVerified(String msg);

        /**
         * <h2>onSuccessResendOtp</h2>
         * This method is used to show the success for resend otp
         */
        void onSuccessResendOtp(String msg);

        /**
         * <h2>setTimerText</h2>
         * This method is used to set the timer
         */
        void setTimerText(String timer);

        /**
         * <h2>enableResendButton</h2>
         * This method is used to enable the resend button
         */
        void enableResendButton();

        /**
         * <h2>disableResendButton</h2>
         * This method is used to disable the resend button
         */
        void disableResendButton();

        /**
         * <h2>setOtp</h2>
         * This method is used to set the otp from received message
         */
        void setOtp(String otp);
    }

    /**
     * <h1>Presenter</h1>
     * <p>presenter interface</p>
     */
    interface Presenter
    {
        /**
         * <h2>startTimer</h2>
         * This method is used to start the timer
         * @param seconds seconds
         */
        void startTimer(int seconds);

        /**
         * <h2>validateOtp</h2>
         * This method is used to send the received message
         * @param msg message
         */
        void onSmsReceived(String usedId, String msg, int trigger);


        /**
         * <h2>getCountryInfo</h2>
         * This method is used to validate the phone
         * @param otp otp
         */
        void validateOtp(SeekBar seekBar, String userId, String otp, int trigger);


        /**
         * <h2>resendOtp</h2>
         * This method is used to validate the phone
         * @param userId userId
         */
        void resendOtp(String userId, int trigger);


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
