package com.karry.authentication.login;

import android.os.Bundle;
import android.widget.SeekBar;

import com.karry.authentication.login.model.VehiclesDetails;
import com.karry.pojo.VehicleTypes;

import java.util.ArrayList;

/**
 * <h1>LoginContract</h1>
 * This interface is used to interact between model and view
 * @author  3Embed.
 * @since 31-01-2018
 */

public interface LoginContract {

    interface View
    {
        /**
         * set the values of username and password if user already logged in
         * @param username username
         * @param pass password
         */
        void setLoginCreds(String username, String pass);

        /**
         * <h2>enableLogin</h2>
         * This method is used to disable the error in username field
         */
        void setDisableUserError();

        /**
         * <h2>enableLogin</h2>
         * This method is used to disable the error in password field
         */
        void setDisablePasswordError();

        /**
         * <h2>setDisableVehPinError</h2>
         * This method is used to disable the error in vehicle No
         */
        void setDisableVehPinError();

        /**
         * <h2>enableLogin</h2>
         * This method is used to enable the login button with full progress
         */
        void enableLogIn();

        /**
         * <h2>enableLogin</h2>
         * This method is used to enable the login button with half progress
         */
        void enableHalf();

        /**
         * <h2>enableThirty</h2>
         * This method is used to enable the login button with 33% progress
         */
        void enableThirty();

        /**
         * <h2>enableSixty</h2>
         * This method is used to enable the login button with 66% progress
         */
        void enableSixty();



        /**
         * <h2>enableLogin</h2>
         * This method is used to disable the login button
         */
        void disableLoginIn();

        /**
         * <h2>enableLogin</h2>
         * This method is used to set the error in user field
         */
        void onUsernameError(String msg);

        /**
         * <h2>enableLogin</h2>
         * This method is used to set the error in password field
         */
        void onPasswordError(String msg);


        /**
         * <h2>onVehNoError</h2>
         * This method is used to set the error in Vehicle no field
         */
        void onVehNoError(String msg);

        /**
         * <h2>enableLogin</h2>
         * This method is used to start the progressBar
         */
        void startProgressBar();

        /**
         * <h2>enableLogin</h2>
         * This method is used to stop the progresssBar
         */
        void stopProgressBar();

        /**
         * <h2>enableLogin</h2>
         * This method is used to show the error with error message
         */
        void onFailure(String msg, String heading);


        /**
         * <h2>enableLogin</h2>
         * This method is used to show the success
         */
        void onSuccessLogin(ArrayList<VehiclesDetails> list);

        /**
         * <h2>gotoVehicleTypeActivity</h2>
         */
        void gotoVehicleTypeActivity(VehicleTypes vehicleTypes);

        /**
         * <h1>setUserNamePass</h1>
         * @param userName
         * @param password
         * @param veh_no
         */
        void setUserNamePass(String userName, String password, String veh_no);

        /**
         * <h1>hideSoftKeyboard</h1>
         * <p>This method is used to hide the keyboard</p>
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
         * <h1>setLanguageDialog</h1>
         * <p>set the language list and open the dialog</p>
         * @param indexSelected : list of lanuage
         */
        void setLanguageDialog(int indexSelected);

        /**
         * <h1>setLanguage</h1>
         */
        void setLanguage(String language,boolean restart);

        void openWebView(String url);

    }

    interface Presenter
    {
        /**
         * <h2>enableLogin</h2>
         * This method is used to set the seek_bar progress
         */
        void setSeekBarProgress(SeekBar seekBar, int progress);

        /**
         * <h2>enableLogin</h2>
         * This method is used to store the fcm token
         */
        void storeFcmToken();

        /**
         * <h2>enableLogin</h2>
         * This method is used to check if user already login
         */
        void checkIsAlreadyLogin();

        /**
         * <h2>enableLogin</h2>
         * This method is used to get the device id
         */
        void getDeviceId();

        /**
         * <h2>enableLogin</h2>
         * This method is used to call the api when user click the login button
         */
        void onClickLoginButton(String username, String password, String vehNo);

        /**
         * <h2>enableLogin</h2>
         * This method is used to validate the field
         */
        void validateField(String emailPhone, String password, String vehNo);

        /**
         * <h2>enableLogin</h2>
         * This method is used to disable all dependency from presenter/
         */
        void onDestoryView();

        void getUsernamePass();

        void checkLoginResponse();

        /**
         * <h1>currentLocation</h1>
         * <p>fetch the current location</p>
         */
        void currentLocation();

        /**
         * <h1>checkMessage</h1>
         * @param data : String from the 401 error
         */
        void checkMessage(Bundle data);

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
         * <h1>clear</h1>
         * <p>compositeDisposable clear</p>
         */
        void clear();

        /**
         * <h1>unSubScribeFCMTopics</h1>
         * <p>if the fcm topis are subscribed need to unsubscribe</p>
         */
        void unSubScribeFCMTopics();

        /**
         * <h1>getLanguages</h1>
         * <p>api call for get the languages</p>
         */
        void getLanguages();

        /**
         * <h1>languageChanged</h1>
         * @param langCode
         * @param langName
         * @param dir
         */
        void languageChanged(String langCode, String langName, int dir);

        /**
         *
         */
        void setWebUrl(String from);
    }


}
