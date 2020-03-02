package com.karry.side_screens.wallet.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.fcm.MyFirebaseMessagingService;
import com.karry.utility.AppTypeFace;

import butterknife.ButterKnife;


/**
 * <h1>Alerts</h1>
 * <p> class to show different types of alerts </p>
 */
public class Alerts extends Activity {

    private AppTypeFace appTypeface;

    public Alerts(Context context,AppTypeFace appTypeface) {
        this.appTypeface=appTypeface;
    }
    /**
     * <h2>showNetworkAlert</h2>
     * This method is used to show network alert box, for opening settings.
     * @param context
     */
    public void showNetworkAlert(final Context context)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        // Setting Dialog Title
        alertDialog.setTitle(context.getResources().getString(R.string.network_alert_title));
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage(context.getResources().getString(R.string.network_alert_message));
        // On pressing Settings button
        alertDialog.setPositiveButton(context.getResources().getString(R.string.action_settings), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        boolean isbkrnd= MyFirebaseMessagingService.isApplicationSentToBackground(context);
      // Showing Alert Message
        if(isbkrnd == false)
            alertDialog.show();
    }


    /**
     * <h2>problemLoadingAlert</h2>
     * <p>
     *     method to show an alert for loading error
     * </p>
     * @param context
     * @param message
     */
    public void problemLoadingAlert(final Context context, String message)
    {
        final Dialog dialog = new Dialog(context, R.style.AlertDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_ok);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_ok);
        TextView tv_text = (TextView) dialog.findViewById(R.id.tv_text);
        tv_text.setText(message);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public ProgressDialog getProcessDialog(Activity mContext)
    {
        ProgressDialog dialog = new ProgressDialog(mContext, R.style.AlertDialogTheme);
        dialog.setCancelable(true);
        dialog.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        return dialog;
    }

    /**
     * <h2>userPromptWithOneButton</h2>
     * This method is used to show the alert with response
     * @param message message to be shown
     * @param context context of the activity
     */
    public Dialog userPromptWithOneButton(String message, Context context)
    {
        final Dialog dialog = new Dialog(context, R.style.AlertDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_one_button_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        TextView tv_alert_title =  dialog.findViewById( R.id.tv_alert_title);
        TextView tv_alert_body =  dialog.findViewById( R.id.tv_alert_body);
        TextView tv_alert_ok =  dialog.findViewById( R.id.tv_alert_ok);

        tv_alert_title.setTypeface(appTypeface.getPro_narMedium());
        tv_alert_ok.setTypeface(appTypeface.getPro_narMedium());
        tv_alert_body.setTypeface(appTypeface.getPro_News());

        tv_alert_body.setText(message);
        return dialog;
    }

    /**
     * <h2>userPromptWithTwoButtons</h2>
     * This method is used to show the alert with 2 buttons
     * @param context context of the activity
     */
    public Dialog userPromptWithTwoButtons(Activity context)
    {
        Dialog dialog = new Dialog(context, R.style.AlertDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_two_buttons_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialogTheme;
        dialog.setCancelable(false);

        TextView tv_alert_no =  dialog.findViewById( R.id.tv_alert_no);
        TextView tv_alert_title =  dialog.findViewById( R.id.tv_alert_title);
        TextView tv_alert_body =  dialog.findViewById( R.id.tv_alert_body);
        TextView tv_alert_yes =  dialog.findViewById( R.id.tv_alert_yes);

        tv_alert_title.setTypeface(appTypeface.getPro_narMedium());
        tv_alert_yes.setTypeface(appTypeface.getPro_narMedium());
        tv_alert_no.setTypeface(appTypeface.getPro_narMedium());
        tv_alert_body.setTypeface(appTypeface.getPro_News());

        tv_alert_no.setOnClickListener(v ->
                dialog.dismiss());
        return dialog;
    }
}
