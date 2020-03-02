package com.karry.authentication.signup.SignUpVehicle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.karry.BaseView;
import com.karry.authentication.signup.SignUpData;
import com.karry.pojo.Signup.City;
import com.karry.pojo.Signup.ColorData;
import com.karry.pojo.Signup.MakeData;
import com.karry.pojo.Signup.ModelData;
import com.karry.pojo.Signup.PreferencesList;
import com.karry.pojo.Signup.Services;
import com.karry.pojo.Signup.TypeAndSpecialitiesData;
import com.karry.pojo.Signup.YearData;

import java.io.File;
import java.util.ArrayList;


/**
 * <h1>SignupVehicleContract</h1>
 * <p>interface for the Vehicle SignUp View and for the Presenter</p>
 */
public interface SignupVehicleContract {

    /**
     * <h1>SignupVehicleView</h1>
     * <p>the interface for the View, Presenter will inform after Implementation</p>
     */
    interface SignupVehicleView extends BaseView {

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
         * <h1>getCitylist</h1>
         * <p>the list pass to select the City from the API response</p>
         * @param cityData : List of city
         */
        void getCitylist(ArrayList<City> cityData);

        /**
         * <h1>setCityText</h1>
         * <p>for set the city after select the city</p>
         * @param city : selected city for set the text
         * @param City_ID : selected cityID for future use
         */
        void setCityText(String city, String City_ID);

        /**
         * <h1>getTypelist</h1>
         * <p>the list pass to select the Type from the API response</p>
         * @param typeAndSpecialitiesData : List of Type and Services
         */
        void getTypelist(ArrayList<TypeAndSpecialitiesData> typeAndSpecialitiesData);

        /**
         * <h1>setTypeText</h1>
         * <p>for set the city after select the Type of vehicle</p>
         * @param Type : selected vehicle type for set the text
         * @param Type_ID : selected vehicle Type for future use
         */
        void setTypeText(String Type, String Type_ID);

        /**
         * <h1>getServicelist</h1>
         * <p>the list pass to select the Services from the API response</p>
         * @param services : List of Services
         */
        void getServicelist(ArrayList<Services> services);

        /**
         * <h1>setServiceText</h1>
         * <p>for set the service after select the vehicle service</p>
         * @param services : selected vehicle service for set the text
         * @param Service_ID : selected vehicle service id for future use
         */
        void setServiceText(String services, String Service_ID);

        /**
         * <h1>getMakelist</h1>
         * <p>the list pass to select the vehicle make from the API response</p>
         * @param makeModelData : List of vehicle make
         */
        void getMakelist(ArrayList<MakeData> makeModelData);

        /**
         * <h1>setMakeText</h1>
         * <p>for set the vehicle make after select the make</p>
         * @param make : selected vehicle make for set the text
         * @param Make_ID : selected vehicle make id for future use
         */
        void setMakeText(String make, String Make_ID);

        /**
         * <h1>getModellist</h1>
         * <p>the list pass to select the vehicle model from the API response depend the make</p>
         * @param models : List of vehicle model
         */
        void getModellist(ArrayList<ModelData> models);

        /**
         * <h1>setModelText</h1>
         * <p>for set the vehicle model after select </p>
         * @param model : selected vehicle model for set the text
         * @param Model_ID : selected vehicle model id for future use
         */
        void setModelText(String model, String Model_ID);

        /**
         * <h1>clearTypeService</h1>
         * <p>if the city change then the type and service is clearing</p>
         */
        void clearTypeService();

        /**
         * <h1>clearMakeModel</h1>
         * <p>if the year change then make and model of vehicle need to clear</p>
         */
        void clearMakeModel();

        /**
         * <h1>clearModel</h1>
         * <p>if the make change then the model need to clear</p>
         */
        void clearModel();

        /**
         * <h1>validPlateNo</h1>
         * <p>if the plate number valid</p>
         */
        void validPlateNo();

        /**
         * <h1>InvalidPlateNo</h1>
         * <p>if the plate number Invalid</p>
         */
        void InvalidPlateNo();

        /**
         * <h1>enableNext</h1>
         * <p>if the mandatory fields are filled enable the next text</p>
         */
        void enableNext();

        /**
         * <h1>disableNext</h1>
         * <p>if the mandatory fields are Not filled disable the next text</p>
         */
        void disableNext();

        /**
         * <h1>amazonUploadSuccess</h1>
         * <p>if the image upload success in amazon account</p>
         * @param url :the image url
         */
        void amazonUploadSuccess(String url);

        /**
         * <h1>getYearList</h1>
         * <p>the list pass to select the year from the API response</p>
         * @param yearList : List of year
         */
        void getYearList(ArrayList<YearData> yearList);

        /**
         * <h1>getColorList</h1>
         * <p>the list pass to select the color</p>
         * @param colorList : List of color
         */
        void getColorList(ArrayList<ColorData> colorList);

        /**
         * <h1>setYear</h1>
         * <p>set the text after select the year</p>
         * @param year : selected year
         */
        void setYear(String year);

        /**
         * <h1>setColor</h1>
         * <p>set the text after select the color</p>
         * @param color : selected color
         */
        void setColor(String color);

        /**
         * <h1>hideSoftKeyboard</h1>
         * <p>This method is used to hide the keyboard</p>
         */
        void hideSoftKeyboard();

        /**
         * <h1>vehicleImgErr</h1>
         * <p>if the vehicle image is not available </p>
         */
        void vehicleImgErr();

        /**
         * <h1>startVehicleSignUp</h1>
         * <p>start the Vehicle SignUp Activity and send the data</p>
         * @param signUpData :data from the Personal SignUp
         */
        void startDocumentSignUp(SignUpData signUpData);

       /* *//**
         * <h1>validPlateNoAPI</h1>
         * <p>if the API response is valid or if the plate no not already registered</p>
         *//*
        void validPlateNoAPI();*/

        /**
         * <h1>inValidPlateNoAPI</h1>
         * <p>if the API response is Invalid or if the plate no already registered</p>
         * @param errMsg : error message from API response
         */
        void inValidPlateNoAPI(String errMsg);


        /**
         * <h1>driverPreferenceDataForAdapter</h1>
         */
        void driverPreferenceDataForAdapter(ArrayList<PreferencesList> preferencesList);

        /**
         * <h1>driverPreferenceDataForAdapter</h1>
         */
        void vehiclePreferenceDataForAdapter(ArrayList<PreferencesList> preferencesList);


    }

    /**
     * <h1>SignupVehiclePresenter</h1>
     * <p>the interface for the Presenter, view will inform tp presenter have to do these Implementations</p>
     */
    interface SignupVehiclePresenter {

        /**
         * <h1>getPersonalData</h1>
         * <p>the data fetch from the Personal Activity and Assign to the Model class</p>
         * @param mBundle : data from the SigiUp personal
         */
        void getPersonalData(Bundle mBundle);

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
         * <h1>cityOnClick</h1>
         * <p>api call for the list of city available in the state</p>
         */
        void cityOnClick();

        /**
         * <h1>onCitySelected</h1>
         * <p>handle the data from onActivity Result after select the city</p>
         * @param data : data which selected the city
         */
        void onCitySelected(Intent data);

        /**
         * <h1>typeOnClick</h1>
         * <p>check whether the type data available or not. if not available call the api for type and services</p>
         */
        void typeOnClick();

        /**
         * <h1>onTypeSelected</h1>
         * <p>handle the data from onActivity Result after select the Type</p>
         * @param data : data which selected the Type
         */
        void onTypeSelected(Intent data,String Type_ID);

        /**
         * <h1>serviceOnClick</h1>
         * <p>check the service available if available pass the list of services</p>
         */
        void serviceOnClick();

        /**
         * <h1>onServiceSelected</h1>
         * <p>handle the data from onActivity Result after select the Service</p>
         * @param data : data which selected the Service
         */
        void onServiceSelected(Intent data, String Type_ID);

        /**
         * <h1>yearOnClick</h1>
         * <p>check the list of year available or not if not available call the api and pass the list of year</p>
         */
        void yearOnClick();

        /**
         * <h1>onYearSelected</h1>
         * <p>handle the data from onActivity Result after select the Year</p>
         * @param data : data which selected the Year
         */
        void onYearSelected(Intent data);

        /**
         * <h1>colorOnClick</h1>
         * <p>pass the list of color which hard coded</p>
         */
        void colorOnClick();

        /**
         * <h1>onColorSelected</h1>
         * <p>handle the data from onActivity Result after select the Color</p>
         * @param data : data which selected the Color
         */
        void onColorSelected(Intent data);

        /**
         * <h1>makeOnClick</h1>
         * <p>check the list available or not, if not available call the api and pass the list</p>
         * @param year : seleted year
         */
        void makeOnClick(String year);

        /**
         * <h1>onmakeSelected</h1>
         * <p>handle the data from onActivity Result after select the Make</p>
         * @param data : data which selected the Make
         */
        void onmakeSelected(Intent data);

        /**
         * <h1>modelOnClick</h1>
         * <p>check the list available or not, if not available call the api and pass the list, the API call depends on year and make</p>
         * @param year : seleted year
         * @param make : seleted make
         */
        void modelOnClick(String year, String make);

        /**
         * <h1>onModelSelected</h1>
         * <p>handle the data from onActivity Result after select the Model</p>
         * @param data : data which selected the Model
         */
        void onModelSelected(Intent data);

        /**
         * <h1>validateToNext</h1>
         * <p>check whether all mandatory fields are done or not for next Activity</p>
         */
        void validateToNext();

        /**
         * <h1>validatePlateNo</h1>
         * <p>which check the PlateNo</p>
         * @param plateNo : plate no entered in editText
         */
        void validatePlateNo(String plateNo);

        /**
         *<h1>cropImage</h1>
         * <p>the result of crop Image pass to Uplad in to Amazon</p>
         * @param data : the result of crop Image
         */
        void cropImage(Intent data);

        /**
         * <h1>amzonUpload</h1>
         * <p>upload the image in amazon account</p>
         * @param file : file name for upload
         * @param context : activity
         * @param Bucket_folder : the bitbucket folder for upload the image
         */
        void amzonUpload(File file, Context context, String Bucket_folder);

        /**
         * <h1>hideKeyboardAndClearFocus</h1>
         * <p>Informing to view that to hide the keyboard and clear the all focus from the EditText</p>
         */
        void hideKeyboardAndClearFocus();

        /**
         * <h1>validateAndStartActivity</h1>
         * <p>validate the Vehicle and validate Pic</p>
         */
        void validateAndStartActivity(String plateNo, String city_ID, String type_ID,
                                      String service_ID, String make_ID, String model_ID,
                                      String selectedYear, String selectedColor,
                                      String vehicle_pic_url);



        /**
         * <h1>setbookingPreferenceList</h1>
         * @param bookingPreference : selected preferences
         */
        void setDriverBookingPreferenceList(String bookingPreference);

        void setVehicleBookingPreferenceList(String bookingPreference);

        void preferencesAPI(String id);



    }



}
