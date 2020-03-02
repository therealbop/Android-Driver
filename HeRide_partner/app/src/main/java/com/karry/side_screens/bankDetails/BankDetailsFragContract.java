package com.karry.side_screens.bankDetails;


import com.karry.BaseView;
import com.karry.pojo.bank.StripeDetailsPojo;

/**
 * <h1>BankDetailsFragContract</h1>
 * <p>interface for the BankDetails view and presenter.</p>
 */
public interface BankDetailsFragContract {

    /**
     * <h1>BankDetailsFragView</h1>
     * <p>interface for Bank details Fragment View</p>
     */
    interface BankDetailsFragView extends BaseView {

        /**
         * <h1>onFailure</h1>
         * <p>to inform the Api call is failure</p>
         * @param failureMsg :failureReason
         */
        void onFailure(String failureMsg);

        /**
         * <h1>showAddStipe</h1>
         * <p>if the stripe is not added need to to change UI</p>
         */
        void showAddStipe(String msg);

        void sripeAccAddSuccess(StripeDetailsPojo stripeDetailsPojo);

        void openUpdateStripe(StripeDetailsPojo stripeDetailsPojo);

        void openActivityForBankAdd(String countryID);

    }


    /**
     * <h1>BankDetailsFragPresenter</h1>
     * <p>interface for Bank Details Fragment BookingPopUpPresenter.</p>
     */
    interface BankDetailsFragPresenter{

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
         * <h1>getConnectAccount</h1>
         * <p>tro get the Stripe and the bank added</p>
         */
        void getConnectAccount();

        void compositeDisposableClear();

        void updateStripe();

        void addNewBank();



    }
}
