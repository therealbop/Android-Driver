package com.karry.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.karry.app.mainActivity.MainActivity;
import com.karry.authentication.login.LoginActivity;
import com.heride.partner.R;


public class MyAlertDialogs {


    /******************************************************/

    /**
     * custom method to show an alert dialog for invalid or expired session token
     *
     * @param title:   String to set as alert dialog title
     * @param message: String to set as alert dialog msg
     */
    public void adNotificationAlert(final Activity mActivity,
                                    String title, String message, final boolean isInvalidToken) {
        Log.i("MyAlertDialogs", "adNotificationAlert called");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity, 5);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setNegativeButton(mActivity.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isInvalidToken) {
                            SessionManager sessionManager = new SessionManager(mActivity.getApplicationContext());
/*
                            sessionManager.clearSharedPrefs();
                            VariableConstants.isLogout = true;
                            mActivity.stopService(ApplicationController.getLocationServiceIntent());*/
                            Intent LoginIntent = new Intent(mActivity, LoginActivity.class);
                            LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            dialog.dismiss();
                            mActivity.startActivity(LoginIntent);
                            mActivity.finish();
                            mActivity.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                        } else {
                            dialog.dismiss();
                        }
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    /*****************************************************/


    /**
     * custom mehtod to show an alert dialog that the customer has canceled the
     * current booking and on click of ok finsih the current activity and move to Home Fragment
     *
     * @param title:   String to be set as title of alert dialog
     * @param message: String to be set as Message of alert dialog
     */
    public void adFinishCrntActivity(final Activity mActivity, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity, 5);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton(mActivity.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mActivity.finish();
                        mActivity.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    /******************************************************/

    public void adMoveToMainActivity(final Activity mActivity, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, 5);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(mActivity.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(mActivity, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mActivity.startActivity(intent);
                        mActivity.finish();
                        mActivity.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                    }
                });

        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }
    /*********************************************************/


}
