package com.karry.side_screens.support;


import com.karry.BaseView;
import com.karry.pojo.SupportData;

import java.util.ArrayList;

/**
 * <h1>SupportFragmentContract</h1>
 * <p>this is the interface contain for SupportFragment and for the BookingPopUpPresenter</p>
 */
public interface SupportFragmentContract {

    /**
     * <h1>SupportFragView</h1>
     * <p>interface for SupportFragment</p>
     */
    interface SupportFragView extends BaseView {

        /**
         * <h1>onFailure</h1>
         * <p>to inform the Api call is failure</p>
         * @param failureMsg :failureReason
         */
        void onFailure(String failureMsg);

        /**
         * <h1>getSupportDetails</h1>
         * <p>to pass the API is success and pass data</p>
         * @param supportDatas : the response list
         */
        void getSupportDetails(ArrayList<SupportData> supportDatas);

    }

    /**
     * <h1>SupportFragPresenter</h1>
     * <p>interface for the BookingPopUpPresenter Implementation.</p>
     */
    interface SupportFragPresenter{

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
         * <h1>callSupportApi</h1>
         * <p>this method is used to call the master support api.</p>
         */
        void callSupportApi();

        void compositeDisposableClear();

    }
}
