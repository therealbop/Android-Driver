package com.karry.authentication.vehiclelist;

import com.karry.BaseView;
import com.karry.pojo.SigninDriverVehicle;

import java.util.ArrayList;

/**
 * <h1>VehicleListContract</h1>
 * <p>the interface for the Vehicle List View and Presenter</p>
 */

interface VehicleListContract extends BaseView
{

    /**
     * <h1>VehicleListView</h1>
     * <p>interface for view, informing the presenter to view what changes</p>
     */
    interface VehicleListView extends BaseView {

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
         * <h1>setListData</h1>
         * <p>for clear and initilizing the List of vehicle</p>
         * @param listData :List of vehicle
         */
        void setListData(ArrayList<SigninDriverVehicle> listData);

        /**
         * <h1>notifyAdapter</h1>
         * <p>for notifying the Adapter that data changed</p>
         */
        void notifyAdapter();

        /**
         * <h1>onError</h1>
         * <p>for showing the error message from the API response</p>
         * @param error : error mesaage
         */
        void onError(String error);

        /**
         * <h1>onSuccess</h1>
         * <p>if the default vehicle select API response is success start the mainActivity</p>
         */
        /*void onSuccess(String result);*/
        void onSuccess(String vehicleTypes);

        void onSuccessMain();

        /**
         * <h1>onSuccesLogout</h1>
         * <p>if the Logout API is success start the Login Activity</p>
         */
        void onSuccesLogout();

        /**
         * <h1>onFailure</h1>
         * <p>Logout  API response is error or not success then show the error message</p>
         * @param msg: error message
         */
        void onFailure(String msg);
        /**
         * <h1>goToLogin</h1>
         * <p>if the Unauthorized error is coming in Api need to go Login Screen.</p>
         */
        void goToLogin(String errMsg);

    }

    /**
     * <h1>VehicleListPresenter</h1>
     * <p>view inform to presenter for do some Implementation</p>
     */
    interface VehicleListPresenter{

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

        /*void getList();*/

        /**
         * <h1>confirmOnclick</h1>
         * <p>for select the vehicle</p>
         */
        void confirmOnclick();

        /**
         * <h1>logoutOnclick</h1>
         * <p>for Logout process</p>
         */
        void logoutOnclick();
    }
}
