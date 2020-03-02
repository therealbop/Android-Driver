package com.karry.side_screens.wallet.wallet_transaction_activity;


import com.karry.side_screens.wallet.wallet_transaction_activity.model.TransctionsItem;

import java.util.ArrayList;

public interface WalletTransactionContract
{

    interface WalletTrasactionView
    {
        void walletTransactionsApiSuccessViewNotifier(String allTransactionsAL);

        /**
         * <h2>showToastNotifier</h2>
         * <p> method to trigger activity/fragment show progress dialog interface </p>
         * @param msg: message to be shown along with the progress dialog
         */
        void showProgressDialog(String msg);

        /**
         * <h2>showToastNotifier</h2>
         * <p> method to trigger activity/fragment showToast interface to show test </p>
         * @param msg: message to be shown in toast
         * @param duration: toast duration
         */
        void showToast(String msg, int duration);

        /**
         * <h2>showAlertNotifier</h2>
         * <p> method to trigger activity/fragment showAlertNotifier interface to show alert </p>
         * @param title: alert title to be setList
         * @param msg: alert message to be displayed
         */
        void showAlert(String title, String msg);


        /**
         * <h2>noInternetAlert</h2>
         * <p> method to trigger activity/fragment alert interface to show alert that there isnot internet connectivity </p>
         */
        void noInternetAlert();

        /**
         * <H>Hide Progress bar</H>
         * <p>This method is using to hide the progress bar</p>
         */
        void hideProgressDialog();



        /**
         * <h>Set All transaction data to display</h>
         * <p>this method is using to set the all transaction data</p>
         * @param allTransactionsAL all transaction data
         */
        void setAllTransactionsAL(ArrayList<TransctionsItem> allTransactionsAL);

        /**
         * <h>Set debit Transactions data to display</h>
         * <p>this method is using to set the debit  transaction data</p>
         * @param debitTransactionsAL debit transaction data
         */
        void setDebitTransactionsAL(ArrayList<TransctionsItem> debitTransactionsAL);

        /**
         * <h>Set credit Transactions data to display</h>
         * <p>this method is using to set the credit  transaction data</p>
         * @param creditTransactionsAL credit transaction data
         */
        void setCreditTransactionsAL(ArrayList<TransctionsItem> creditTransactionsAL);

    }

    interface WalletTransactionPresenter
    {
        /**
         * <h>Show notification</h>
         * <p>this method is using to  Show notification to user</p>
         * @param msg notification message
         * @param duration duration
         */
        void showToastNotifier(String msg, int duration);

        /**
         *<h>Initialize transaction Api call</h>
         * <P>this method is using to initialize the Api call</P>
         * @param isFromOnRefresh it from refresh
         * @param txnType for Debit -1 , Credit-2 ,All-3
         * @param pageIndex for first time 1 after that increase by 1
         */
        void initLoadTransactions(int txnType, boolean isFromOnRefresh,int pageIndex);

    }
}
