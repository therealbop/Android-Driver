package com.karry.authentication.signup.SignUpPersonal;


import android.content.Context;
import android.content.Intent;

import com.karry.BaseView;
import com.karry.authentication.signup.SignUpData;
import com.karry.countrypic.countrypic.CountryPicker;
import com.karry.pojo.Signup.Gender;
import com.karry.pojo.Signup.StateData;

import java.io.File;
import java.util.ArrayList;

/**
 * <h1>SignUpPersonalContract</h1>
 * <p>interface for the Sign up Personal Data, Included the interface for View and the presenter.</p>
 */
public interface SignUpPersonalContract
{

    /**
     * <h1>SignUpPersonalView</h1>
     * <p>interface for the View of Personal Data Sign up</p>
     */
    interface SignUpPersonalView extends BaseView
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
         * <h1>hideSoftKeyboard</h1>
         * <p>This method is used to hide the keyboard</p>
         */
        void hideSoftKeyboard();

        /**
         * <h1>showSoftKeyboard</h1>
         * <p>method to hide the Keyboard</p>
         */
        void showSoftKeyboard();

        /**
         * <h1>onGettingOfCountryInfo</h1>
         * <p>set the date of phone number depends on the default country</p>
         * @param countryFlag : country Flag
         * @param countryCode : country Code
         * @param phoneMinLength : minimum length of the phone Number
         * @param phoneMaxLength : maximum length of the phone Number
         * @param isDefault      : isDefault
         */
        void onGettingOfCountryInfo(int countryFlag, String countryCode, int phoneMinLength, int phoneMaxLength, boolean isDefault);

        /**
         * <h1>clearFocus</h1>
         * <p>clear the foccus from every view when hide the keyboard</p>
         */
        void clearFocus();

        /**
         * <h1>validFName</h1>
         * <p>the method for inform the view that First name is valid</p>
         */
        void validFName();

        /**
         * <h1>invalidFName</h1>
         * <p>the method for inform the view that First name is Invalid</p>
         */
        void invalidFName();

        /**
         * <h1>phoneEmptyError</h1>
         * <p>the method for inform the view that the phone number is empty</p>
         */
        void phoneEmptyError();

        /**
         * <h1>PhoneLengthValid</h1>
         * <p>the method for inform the view that the phone number length is correct</p>
         */
        void PhoneLengthValid();

        /**
         * <h1>PhoneLengthInValid</h1>
         * <p>the method for inform the view that the phone number length is not correct</p>
         */
        void PhoneLengthInValid();

        /**
         * <h1>phoneAPIValide</h1>
         * <p>the method for inform the view that the phone number not registered already</p>
         */
        void phoneAPIValide();

        /**
         * <h1>phoneAPIInValide</h1>
         * <p>the method for inform the view that the phone number registered already</p>
         * @param msg: the error message from API
         */
        void phoneAPIInValide(String msg);

        /**
         * <h1>emailEmptyError</h1>
         * <p>the method for inform the view that Email is empty</p>
         */
        void emailEmptyError();


        /**
         * <h1>emailFormatValid</h1>
         * <p>the method for inform the view that Email format is valid</p>
         */
        void emailFormatValid();

        /**
         * <h1>emailFormatInValid</h1>
         * <p>the method for inform the view that Email format is not valid</p>
         */
        void emailFormatInValid();

        /**
         * <h1>emailAPIValid</h1>
         * <p>the method for inform the view that Email not registered already</p>
         */
        void emailAPIValid();

        /**
         * <h1>emailAPIInValid</h1>
         * <p>the method for inform the view that Email registered already</p>
         * @param msg: the error message from the API response
         */
        void emailAPIInValid(String msg);

        /**
         * <h1>passwordEmpty</h1>
         * <p>the method for inform the view that password is empty</p>
         */
        void passwordEmpty();

        /**
         * <h1>passwordNotEmpty</h1>
         * <p>the method for inform the view that password is  Not empty</p>
         */
        void passwordNotEmpty();

        /**
         * <h1>confirmPasswordEmpty</h1>
         * <p>the method for inform the view that confirm password is empty</p>
         */
        void confirmPasswordEmpty();

        /**
         * <h1>passwordNotEmpty</h1>
         * <p>the method for inform the view that confirm password is  Not empty</p>
         */
        void confirmPasswordNotEmpty();

        /**
         * <h1>passMatching</h1>
         * <p>inform the view that the password and confirm password is matching</p>
         */
        void passMatching();

        /**
         * <h1>passNotMatching</h1>
         * <p>inform the view that the password and confirm password is Not matching</p>
         */
        void passNotMatching();

        /**
         * <h1>enableNext</h1>
         * <p>to inform that the validation check done and all fields are valid and enable next button</p>
         */
        void enableNext();

        /**
         * <h1>disableNext</h1>
         * <p>to inform that the validation check done and all fields are Not valid and disable next button</p>
         */
        void disableNext();

        /**
         * <h1>amazonUploadSuccess</h1>
         * <p>inform the view, that the Image is uploaded successfully and need to show the Image</p>
         * @param url : the Image URL
         */
        void amazonUploadSuccess(String url);

        /**
         * <h1>getStateList</h1>
         * <p>the list pass to select the state from the API response</p>
         * @param stateList : List of state
         */
        void getStateList(ArrayList<StateData> stateList);

        /**
         * <h1>setState</h1>
         * <p>for set the state after select the state</p>
         * @param state : selected state for set the text
         */
        void setState(String state);

        /**
         * <h1>getGenderList</h1>
         * <p>pass the genderlist for show the list for selection</p>
         * @param genderList :the gender List
         */
        void getGenderList(ArrayList<Gender> genderList);

        /**
         * <h1>setGender</h1>
         * <p>inform the view that gender has selected and show selected Gender</p>
         * @param gender : the selected Gender (male or Female)
         */
        void setGender(String gender);

        /**
         * <h1>dobError</h1>
         * <p>date of birth has not se;ected</p>
         */
        void dobError();

        /**
         * <h1>profilePicError</h1>
         * <p>error if the profile pic not selected</p>
         */
        void profilePicError();

        /**
         * <h1>startVehicleSignUp</h1>
         * <p>start the Vehicle SignUp Activity and send the data</p>
         * @param signUpData :data from the Personal SignUp
         */
        void startVehicleSignUp(SignUpData signUpData);

        /**
         * <h1>validReferralCode</h1>
         * <p>if the referral code is valid</p>
         */
        void validReferralCode();

        /**
         * <h1>invalidReferralCode</h1>
         * <p>if the referral code is not valid</p>
         * @param errmsg : error message
         */
        void invalidReferralCode(String errmsg);

    }

    interface  SignUpPersonalPresenter
    {

        /**
         * <h1>getCountryInfo</h1>
         * <p>country picker and which check the data of Phone number, the max and min number of phone</p>
         * @param countryPicker : countryPicker
         */
        void getCountryInfo(CountryPicker countryPicker);

        /**
         *<h1>cropImage</h1>
         * <p>the result of crop Image pass to Uplad in to Amazon</p>
         * @param data : the result of crop Image
         */
        void cropImage(Intent data);

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
         * <h1>hideKeyboardAndClearFocus</h1>
         * <p>Informing to view that to hide the keyboard and clear the all focus from the EditText</p>
         */
        void hideKeyboardAndClearFocus();

        /**
         * <h1>showKeyboard</h1>
         * <p>informing to view that to enable the keyboard</p>
         */
        void showKeyboard();

        /**
         * <h1>addListenerForCountry</h1>
         * <p>to select the Country code and flag</p>
         * @param countryPicker :countryPicker
         * @param context : activity
         */
        void addListenerForCountry(CountryPicker countryPicker, Context context);

        /**
         * <h1>validateFname</h1>
         * <p>for validate the first name</p>
         * @param fname : first name
         */
        void validateFname(String fname);

        /**
         * <h1>validatePhone</h1>
         * <p>for validate the phone number count</p>
         * @param phone : phone number
         * @param min : minimum number of phone
         * @param max : maximum number of phone
         */
        void validatePhone(String countryCode,String phone, int min, int max);

        /**
         * <h1>validateAPIPhone</h1>
         * <p>to validate the phone number through API, check whether already registerd or not</p>
         * @param phone : phone number
         * @param countryCode : country Code
         */
        void validateAPIPhone(String phone, String countryCode);

        /**
         * <h1>validateEmailFormat</h1>
         * <p>validate the email is in correct format</p>
         * @param email : entered email ID
         */
        void validateEmailFormat(String email);

        /**
         * <h1>validateAPIEmail</h1>
         * <p>validate the Email through API whether already registered or not</p>
         * @param email
         */
        void validateAPIEmail(String email);

        /**
         * <h1>validatePass</h1>
         * <p>check whether the password empty or not</p>
         * @param password : entered Password.
         */
        void validatePass(String password);

        /**
         * <h1>validatePass</h1>
         * <p>check the both password are not empty and same</p>
         * @param password : password
         * @param confirmPass : confirm password
         */
        void validatePass(String password, String confirmPass);

        /**
         * <h1>validateAllFieldsFlags</h1>
         * <p>validate the mandatory fields are filled or not</p>
         */
        void validateAllFieldsFlags();

        /**
         * <h1>amzonUpload</h1>
         * <p>upload the image in amazon account</p>
         * @param file : file name for upload
         * @param context : activity
         * @param Bucket_folder : the bitbucket folder for upload the image
         */
        void amzonUpload(File file, Context context, String Bucket_folder);

        /**
         * <h1>stateCheck</h1>
         * <p>Api call for get the list of State</p>
         */
        void stateCheck();

        /**
         * <h1>onStateSelected</h1>
         * <p>handle the data from onActivity Result after select the State</p>
         * @param data : data which selected the state
         */
        void onStateSelected(Intent data);

        /**
         *<h1>genderCheck</h1>
         * <p>when try to select the gender fetch the list of gender</p>
         */
        void genderCheck();

        /**
         * <h1>onGenderSelected</h1>
         * <p>handle the data from onActivity Result after select the gender</p>
         * @param data
         */
        void onGenderSelected(Intent data);

        /**
         * <h1>validateAndStartActivity</h1>
         * <p>validate the date of birth and the profilePic</p>
         */
        void validateAndStartActivity(String fName, String lName, String phone,
                                      String email, String password,
                                      String referralCode, String countryCode,
                                      String profilePic, String state, String dob, String gender);

        /**
         * <h1>referralCodeValidation</h1>
         * <p>for API of referal code validate</p>
         * @param refCode : referral code
         */
        void referralCodeValidation(String refCode);

        /**
         * <h1>refCodeEmptyValidation</h1>
         * <p>for API of referal code validate is empty or not</p>
         * @param refCode : referral code
         */
        void refCodeEmptyValidation(String refCode);

        /**
         * <h2>getCurrentLocation</h2>
         * This methos is used to get the current location of user
         */
        void getCurrentLocation();

        /**
         * <h2>disposeObservables</h2>
         * This method is used to dispose the observables
         */
        void disposeObservables();

        /**
         * <h1>referralCodeAPI</h1>
         * <p>API for referal code validation</p>
         * @param refCode : referral code
         */
        void referralCodeAPI(String refCode,SignUpData signUpData);


    }
}
