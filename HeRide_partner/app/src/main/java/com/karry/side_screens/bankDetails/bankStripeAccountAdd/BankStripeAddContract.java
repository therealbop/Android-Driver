package com.karry.side_screens.bankDetails.bankStripeAccountAdd;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.karry.BaseView;
import com.karry.pojo.bank.StripeDetailsPojo;
import com.karry.side_screens.bankDetails.pojoforBank.ConnectAccountCountryList;

import java.io.File;
import java.util.ArrayList;

/**
 * <h1>BankStripeAddContract</h1>
 * <p>interface for Add Bank Stripe Account Activity and BookingPopUpPresenter.</p>
 */
public interface BankStripeAddContract {

    /**
     * <h1>BankStripeAddView</h1>
     * <p>interface for the view</p>
     */
    interface BankStripeAddView extends BaseView{

        void initActionBar();

        void setTitle();

        void amazonUploadSuccess(String url);

        void editTextErr(String errorEditText);

        void getIpAddress(String ipAddress);

        void hideSoftKeyboard();

        void addStripeSuccess(String msg);

        void setCountryListforSelect(ArrayList<ConnectAccountCountryList> countryListforSelect);

        void setCountryText(String country, String countryCode);

        void setText(StripeDetailsPojo stripeDetailsPojo);

    }

    /**
     * <h1>BankStripeAddPresenter</h1>
     * <p>interface for the BookingPopUpPresenter</p>
     */
    interface BankStripeAddPresenter{

        /**
         * setting the toolBar
         */
        void setActionBar();

        /**
         * <p>setting teh ActionBar titles</p>
         */
        void setActionBarTitle();

        /**
         * <h1>postConnectAccountAPI</h1>
         * <p>the ConnectAccount API call for Add the Stripe Account</p>
         */
        void postConnectAccountAPI(String Fname, String Lname, String[] DOB, String PersonalId, String Address, String City,
                                   String State, String PostalCode, String DocumentURL,String ipAddress);

        /**
         * <h1>cropImage</h1>
         * <p>after crop the image send the data to upload.</p>
         * @param data
         */
        void cropImage(Intent data);

        void amzonUpload(File file, Context context, String Bucket_folder);

        void validateData(String Fname, String Lname, String DOB, String PersonalId, String Address, String City,
                          String State, String PostalCode, String DocumentURL);

        void fetchIP();

        void hideKeyboardAndClearFocus();


        void accountCountryAPI();



        void onCountrySelected(Intent data);

        void getData(Bundle bundle);
    }
}
