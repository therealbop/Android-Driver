package com.karry.side_screens.home_fragment.invoice;


import android.graphics.Bitmap;
import android.os.Bundle;

import com.karry.BaseView;
import com.karry.pojo.invoice.BookingDetailsPojo;

/**
 * <h1>InvoiceContract</h1>
 * <p>the interface for the Invoice Activity view and BookingPopUpPresenter</p>
 */
public interface InvoiceContract {

    /**
     * <h1>InvoiceView</h1>
     * <p>the interface for the Invoice View</p>
     */
    interface InvoiceView extends BaseView{

        /**
         * <h1>setValues</h1>
         * <p>the UI data set</p>
         * @param bookingDetailsPojo : response of the getBookingDetail API
         */
        void setValues(BookingDetailsPojo bookingDetailsPojo);

        /**
         * <h1>setCardPaymentType</h1>
         * <p>set the view for card payment type</p>
         */
        void setCardPaymentType();

        /**
         * <h1>setCashPaymentType</h1>
         * <p>set the view for Cash payment type</p>
         */
        void setCashPaymentType();

        /**
         * <h1>setCreditPaymentType</h1>
         * <p>set the view for wallet payment type</p>
         */
        void setCreditPaymentType();

        /**
         * <h1>setCorporateBookng</h1>
         * <p>if the booking is corporate booking show the UI</p>
         */
        void setCorporateBookng();

        /**
         * <h1>setMeterBookingView</h1>
         * <p>set the view for meterBooking Invoice</p>
         */
        void setMeterBookingView();

        /**
         * <h1>setRideBookingView</h1>
         * <p>set the view for RideBooking Invoice</p>
         */
        void setRideBookingView();

        /**
         * set the total with currency symbol
         * @param total : value for show total view
         */
        void setTotal(String total);

        /**
         * <h1>goToLogin</h1>
         * <p>if the Unauthorized error is coming in Api need to go Login Screen.</p>
         * @param errMsg: response error message
         */
        void goToLogin(String errMsg);

        void apiFailure(String msg);

        void finishActivity();


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
         * <h1>disableCorporateBooking</h1>
         * <p>disable the disableCorporateBooking</p>
         */
        void disableCorporateBooking();

        void clearSignature();

        void hideSignature(Bitmap signBitmap, boolean signatureUrl);

        void showSignature();

        void onSignatureApprove(Bitmap bitmap);



    }

    /**
     * <h1>InvoicePresenter</h1>
     * <p>the Interface for the Invoice BookingPopUpPresenter</p>
     */
    interface InvoicePresenter{

        /**
         * <h1>meterBookingInvoiceData</h1>
         * <p>for getting the booking invoice data after complete booking </p>
         * @param data :invoice data
         */
        void getmeterBookingInvoiceData(Bundle data);

        /**
         * <h1>completeInvoice</h1>
         * <p>api call for send the invoice to mail in the case of meter booking</p>
         * @param email : email for only meter booking
         * @param rating : passenger ratting for only ride booking
         */
        void completeInvoice(String email, String rating);

        /**
         * <h1>getTotal</h1>
         * <p>fetch the total with currency symbol</p>
         */
        void getTotal();

        /**
         * <h2>subscribeNetworkObserver</h2>
         * This method is used to check network availability
         */
        void subscribeNetworkObserver();

        /**
         * <h1>clearComposite</h1>
         * <p>clear the compositeDisposable</p>
         */
        void clearComposite();


        void refresh();

        void onBackPressSign();

        void onSigned(Bitmap signBitmap);

        void onSignatureApprove();

        void openSignaturePad();



    }
}
