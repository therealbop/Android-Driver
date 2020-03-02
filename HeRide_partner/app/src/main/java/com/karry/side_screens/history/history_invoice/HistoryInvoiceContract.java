package com.karry.side_screens.history.history_invoice;


import com.karry.BaseView;
import com.karry.side_screens.history.new_model.AppointmentData;

public interface HistoryInvoiceContract
{

    interface HistoryInvoiceView extends BaseView
    {
        void hideFeeDetails();
        void showFeeDetails();
        void setCardPaymentType();
        void setCashPaymentType();
        void setCreditPaymentType();
        void setCorporatePaymentType();
        void setCancelationFee();
        void hideDropLocation();


        /**
         * <h1>enableWallet</h1>
         * <p>enable the wallet if amount detected from the wallet</p>
         * @param walletValue :wallet amount with wallet detected value
         */
        void enableWallet(String walletValue);

        /**
         * <h1>enableCard</h1>
         * <p>enable the card if amount detected from the card</p>
         * @param cardValue :card amount with card detected amount
         */
        void enableCard(String cardValue);

        /**
         * <h1>enableCash</h1>
         * <p>enable the cash if cash booking</p>
         * @param cashValue :cash amount with cash
         */
        void enableCash(String cashValue);

        /**
         * <h1>enableCorporateWallet</h1>
         * <p>enable the wallet if corporate booking</p>
         * @param walletValue :cash amount with cash
         */
        void enableCorporateWallet(String walletValue);

        /**
         * <h1>disableWallet</h1>
         * <p>disable the wallet</p>
         */
        void disableWallet();

        /**
         * <h1>disableCard</h1>
         * <p>disable the Card</p>
         */
        void disableCard();

        /**
         * <h1>disableCash</h1>
         * <p>disable the Cash</p>
         */
        void disableCash();

        /**
         * <h1>disablePayment</h1>
         * <p>disable the Payment</p>
         */
        void disablePayment();

        /**
         * <h1>disableCorporateBooking</h1>
         * <p>disable the disableCorporateBooking</p>
         */
        void disableCorporateBooking();


        /**
         * <h1>hideAppCommision</h1>
         */
        void hideAppCommision();

        /**
         * <h1>goToLogin</h1>
         * <p>for show the message and go to loginPage</p>
         * @param errMsg : error message
         */
        void goToLogin(String errMsg);

        /**
         * <h1>apiFailure</h1>
         * <p>if the status update is error, then the error message showing</p>
         * @param msg : error message
         */
        void apiFailure(String msg);

        /**
         * <h1>setHelpDetailsList</h1>
         * use to update the adapter of help
         */
        void setHelpDetailsList();


    }

    interface HistoryInvoicePresenter
    {

        void isMinFare(String isMinFeeApplied, String status);
        void checkPaymentType(String type, boolean isCorporateBooking);
        void isDropLacationEmpty(String droplocation);

        /**
         * <h1>checkPaymentType</h1>
         * @param appointmentData : appoinment data
         */
        void checkPaymentType(AppointmentData appointmentData);

        /**
         * <h1>checkAppCommission</h1>
         * @param appointmentData : appoinment data
         */
        void checkAppCommission(AppointmentData appointmentData);

        /**
         * <h2>getHelpDetails</h2>
         * use to get the help list
         */
        void getHelpDetails();

        boolean checkhelpCenter();
    }
}
