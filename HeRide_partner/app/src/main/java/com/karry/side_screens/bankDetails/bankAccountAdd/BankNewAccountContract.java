package com.karry.side_screens.bankDetails.bankAccountAdd;

import android.content.Intent;

import com.karry.BaseView;
import com.karry.side_screens.bankDetails.pojoforBank.ConnectAccountCurrencyListSelection;

import java.util.ArrayList;

/**
 * <h1>BankNewAccountContract</h1>
 * <p>interface for adding the Bank Account Activity and BookingPopUpPresenter</p>
 */
public interface BankNewAccountContract {

    /**
     * <h1>BankNewAccountView</h1>
     * <p>interface for view</p>
     */
    interface BankNewAccountView extends BaseView{

        void initActionBar();

        void setTitle();

        void editTextErr(String errorEditText);

        /**
         * <h1>externalAccountAPISuccess</h1>
         * <p>for inform the Bank Account Add is success</p>
         * @param msg : Success message
         */
        void externalAccountAPISuccess(String msg);

        void setCurrencyListforSelect(ArrayList<ConnectAccountCurrencyListSelection>  currencylist);

        void setCurrencyText(String currency);
    }

    /**
     * <h1>BankNewAccountPresenter</h1>
     * <p>interface for the BookingPopUpPresenter or Implementation</p>
     */
    interface BankNewAccountPresenter{

        /**
         * setting the toolBar
         */
        void setActionBar();

        /**
         * <p>setting teh ActionBar titles</p>
         */
        void setActionBarTitle();


        /**
         * <h1>validateData</h1>
         * <p>Check the data input correct or not</p>
         * @param Name :Name
         * @param AccountNo : Account Number
         * @param RoutingNo : Routing Number
         */
        void validateData(String Name, String AccountNo, String RoutingNo);

        /**
         * <h1>externalAccountAPI</h1>
         * <p>API call for adding the BankAccount </p>
         */
        void externalAccountAPI(String Name, String AccountNo, String RoutingNo);

        void accountCurrencyAPI(String countryID);

        void onCurrencySelected(Intent data);

    }
}
