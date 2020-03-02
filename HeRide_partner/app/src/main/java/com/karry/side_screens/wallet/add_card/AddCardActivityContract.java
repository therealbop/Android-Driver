package com.karry.side_screens.wallet.add_card;


import com.stripe.android.model.Card;

public class AddCardActivityContract
{
    public interface AddCardview
    {
        /**
         * <h>API Response</h>
         * <p>this method is using to display response to the user</p>
         * @param message response message
         */
        void onResponse(String message, String cardToken);

        void onError(String errormsg);

        /**
         * <h>Card validation success</h>
         * <p>this method is using to define card validation success action</p>
         */
        void onValidOfCard();

        /**
         * <h>Invalid card response</h>
         * <p>this method is using to define invalid card action</p>
         */
        void onInvalidOfCard();


        /**
         * <h>Start MainActivity</h>
         * <p>this method is using to start MainActivity</p>
         */
        void startMainActivity();

        /**
         * <h>Finish Activity</h>
         * <p>this method is using to finish the current Activity</p>
         */
        void finishActivity();


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
    }

    public interface AddCardPresenter
    {
        /**
         * <h2>addCardService</h2>
         *<p> This method is used to call the add card API</p>
         */
        void addCardService(String cardToken);

        /**
         * <h2>validateCardDetails</h2>
         * <p> This method is used to validate the card details </p>
         * @param card Card details
         */
        void validateCardDetails(Card card);

        /**
         * <h>Stripe Getter</h>
         * <p>this method is using to get Stripe key from sharedPreference</p>
         * @return stripe key
         */
        String stripeKeyGetter();


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
    }
}
