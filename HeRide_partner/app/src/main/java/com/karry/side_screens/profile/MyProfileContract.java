package com.karry.side_screens.profile;


import com.karry.pojo.Signup.PreferencesList;
import com.karry.side_screens.profile.profile_model.ProfileData;

import java.io.File;
import java.util.ArrayList;

public interface MyProfileContract {

    interface Presenter
    {
        void getProfileDetails();
        void uploadToAmazon(File fileName);
        void validateField(String firstName, String lastName, String profilePic);

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
         * <h>Logout View</h>
         * <p>this method is using to logout the user from the app</p>
         */
        void logout();

        void showKeyboard();


        /**
         * <p>checkRTL</p>
         * <p>check is the laguage support RTL</p>
         */
        void checkRTL();

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
    }

    interface View
    {
        void setProfileImage(String profileImage);
        void startProgressBar();
        void stopProgressBar();
        void setProfileDetails(ProfileData profileData);
        void onFailure(String msg);
        void onFailure();
        void onFirstNameError();
        void onLastNameError();
        void onImageUploadedResult(String url);
        void onSuccessProfileUpdate(String msg);
        void logout();
        void onSuccesLogout();


        void showSoftKeyboard();

        /**
         * <h1>goToLogin</h1>
         * <p>if the Unauthorized error is coming in Api need to go Login Screen.</p>
         */
        void goToLogin(String errMsg);

        /**
         * <h1>supportRTLpencil</h1>
         * <p>if the language dir is RTL then add the pencil correct place for edit option</p>
         */
        void supportRTLpencil();

        /**
         * <h1>notSupportRTLpencil</h1>
         * <p>if the language dir is not RTL then add the pencil correct place for edit option</p>
         */
        void notSupportRTLpencil();

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

        /**
         * <h2>enableLogin</h2>
         * This method is used to show the error with error message
         */
        void onFailure(String msg, String heading);

        /**
         * <h1>driverPreferenceDataForAdapter</h1>
         */
        void driverPreferenceDataForAdapter(ArrayList<PreferencesList> preferencesList);

        /**
         * <h1>driverPreferenceDataForAdapter</h1>
         */
        void vehiclePreferenceDataForAdapter(ArrayList<PreferencesList> preferencesList);


    }
}
