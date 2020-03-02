package com.karry.utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.TextView;


import com.karry.adapter.BookingCancelRVA;
import com.karry.adapter.MultipleSelectionServiceRVA;
import com.karry.authentication.login.LoginActivity;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.heride.partner.R;
import com.karry.pojo.bookingAssigned.TowTrayService;
import com.karry.pojo.bookingAssigned.TowtrayServiceSelectData;
import com.karry.service.LocationUpdateService;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.karry.utility.VariableConstant.SERVICELIST;


public class DialogHelper {

    private static AlertDialog alertDialog = null;
    private static ArrayList<TowtrayServiceSelectData> towtrayServiceSelectData = null;
    private static String serviceID="";
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    public DialogHelper() {
    }

    private static TypeCall typeCall;

    public void getTypeCall(TypeCall typeCall){
        DialogHelper.typeCall = typeCall;
    }

    public interface TypeCall{
        void inAppCall();
    }

    private static TollService tollService;


    public void getTollDialogHelper(TollService tollService) {
        DialogHelper.tollService = tollService;
    }

    public interface TollService{
        void skipToll();
        void payedToll(String toll);
    }


    private static DialogCallbackHelper dialogCallbackHelper;
    private static DialogLogoutCallBackHelper dialogLogoutCallBackHelper;
    private static DialogPreferenceSelect dialogPreferenceSelect;
    private static BookingAccept bookingAccept;
    private static ServiceSelected serviceSelected;


    public void getDialogCallbackHelper(DialogCallbackHelper dialogCallbackHelper) {
        DialogHelper.dialogCallbackHelper = dialogCallbackHelper;
    }

    public void getDialogLogoutCallbackHelper(DialogLogoutCallBackHelper dialogLogoutCallBackHelper) {
        DialogHelper.dialogLogoutCallBackHelper = dialogLogoutCallBackHelper;
    }

    public void getDialogPreferenceSelectCallbackHelper(DialogPreferenceSelect dialogPreferenceSelect) {
        DialogHelper.dialogPreferenceSelect = dialogPreferenceSelect;
    }

    public void getBookingAccept(BookingAccept bookingAccept) {
        DialogHelper.bookingAccept = bookingAccept;
    }

    public void getServiceSelected(ServiceSelected serviceSelected) {
        DialogHelper.serviceSelected = serviceSelected;
    }

    /**
     * method for showing simple alertDialog for message
     * @param message message
     */
    public static void showWaringMessage(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.alert));
        builder.setMessage(message);
        builder.setPositiveButton(context.getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * method for showing custome alertDialog
     * @param mActivity context
     * @param title title
     * @param msg message
     * @param action action
     */
    public static void customAlertDialog(final Activity mActivity, String title, String msg, String action) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.alert_dialog_simple_message, null);
        alertDialogBuilder.setView(view);
        TextView tvTitle = view.findViewById(R.id.tv_main_title);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvOk = view.findViewById(R.id.tvUpdate);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        View view1 = view.findViewById(R.id.view);
        tvCancel.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);

        tvTitle.setText(title);
        tvMsg.setText(msg);
        tvOk.setText(action);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Animator[] animHide = {null};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.show();
            view.post(() -> {
                int cx = view.getWidth() / 2;
                int cy = view.getHeight() / 2;
                float finalRadius = (float) Math.hypot(cx, cy);
                Animator animVisible = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                animHide[0] = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                animVisible.start();

            });
        } else {
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
            alertDialog.show();
        }


        tvOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();

                            }
                        });
                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    } else {
                        alertDialog.dismiss();
                    }
                } else {
                    alertDialog.dismiss();
                }
            }
        });

        //alertDialog.setCancelable(false);
    }


    /**
     * method for showing custome alertDialog and closing the current activity
     * @param mActivity context
     * @param title title
     * @param msg message
     * @param action action
     */
    public static void customAlertDialogCloseActivity(final Activity mActivity, String title, String msg, String action) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.alert_dialog_simple_message, null);
        alertDialogBuilder.setView(view);
        TextView tvTitle = view.findViewById(R.id.tv_main_title);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvOk = view.findViewById(R.id.tvUpdate);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        View view1 = view.findViewById(R.id.view);
        tvCancel.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);

        tvTitle.setText(title);
        tvMsg.setText(msg);
        tvOk.setText(action);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Animator[] animHide = {null};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.show();
            view.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;
                    float finalRadius = (float) Math.hypot(cx, cy);
                    Animator animVisible = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    animHide[0] = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                    animVisible.start();

                }
            });
        } else {
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
            alertDialog.show();
        }

        tvOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();
                                mActivity.finish();

                                Intent intentLogin = new Intent(mActivity, LoginActivity.class);
                                intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle mBundle = new Bundle();
                                mBundle.putString("msg", "");
                                intentLogin.putExtras(mBundle);
                                mActivity.startActivity(intentLogin);
                                mActivity.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);

                            }
                        });
                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    } else {
                        alertDialog.dismiss();
                        mActivity.finish();
                    }
                } else {
                    alertDialog.dismiss();
                    mActivity.finish();

                    Intent intentLogin = new Intent(mActivity, LoginActivity.class);
                    intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("msg", "");
                    intentLogin.putExtras(mBundle);
                    mActivity.startActivity(intentLogin);
                    mActivity.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);

                }
            }
        });

        alertDialog.setCancelable(false);
    }


    /**
     *<h1>customAlertDialogLogout</h1>
     * <p>Logout dialog</p>
     */
    public static void customAlertDialogLogout(final Activity mActivity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.alert_dialog_logout, null);
        alertDialogBuilder.setView(view);
        TextView tvTitle = view.findViewById(R.id.tv_main_title);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvLogout = view.findViewById(R.id.tvLogout);
        TextView tvCancel = view.findViewById(R.id.tvCancel);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Animator[] animHide = {null};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.show();
            view.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;
                    float finalRadius = (float) Math.hypot(cx, cy);
                    Animator animVisible = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    animHide[0] = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                    animVisible.start();

                }
            });
        } else {
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
            alertDialog.show();
        }


        tvLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();
                                dialogLogoutCallBackHelper.Logout();

                            }
                        });
                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    } else {
                        alertDialog.dismiss();
                        dialogLogoutCallBackHelper.Logout();
                    }
                } else {
                    alertDialog.dismiss();
                    dialogLogoutCallBackHelper.Logout();
                    mActivity.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                }
            }
        });


        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();
                            }
                        });

                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    } else {
                        alertDialog.dismiss();
                    }
                } else {
                    alertDialog.dismiss();
                    mActivity.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                }
            }
        });

        alertDialog.setCancelable(false);
    }

    public interface DialogCallbackHelper {
        /*void Logout();*/

        void walletFragOpen();

        void changeLanguage(String langCode, String langName, int dir);
    }

    public interface DialogLogoutCallBackHelper{
        void Logout();

    }

    public interface DialogPreferenceSelect{

        void oneSelection();

        void multileSelection();

        void deletePreferences();


    }


    /**
     * method for showing custome alertDialog and closing the current activity
     * @param mActivity context
     * @param title title
     * @param msg message
     * @param action action
     */

    public static void customAlertDialogSignupSuccess(final Activity mActivity, String title, String msg, String action) {
        if (Utility.isMyServiceRunning(LocationUpdateService.class, mActivity)) {
            Intent stopIntent = new Intent(mActivity, LocationUpdateService.class);
            stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
            mActivity.startService(stopIntent);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.alert_dialog_simple_message, null);
        alertDialogBuilder.setView(view);
        TextView tvTitle = view.findViewById(R.id.tv_main_title);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvOk = view.findViewById(R.id.tvUpdate);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        View view1 = view.findViewById(R.id.view);
        tvCancel.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);

        tvTitle.setText(title);
        tvMsg.setText(msg);
        tvOk.setText(action);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Animator[] animHide = {null};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.show();
            view.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;
                    float finalRadius = (float) Math.hypot(cx, cy);
                    Animator animVisible = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    animHide[0] = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                    animVisible.start();

                }
            });
        } else {
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
            alertDialog.show();
        }

        tvOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();
                                Intent intentLogin = new Intent(mActivity, LoginActivity.class);
                                intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle mBundle = new Bundle();
                                mBundle.putString("msg", "");
                                intentLogin.putExtras(mBundle);
                                mActivity.startActivity(intentLogin);
                            }
                        });
                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    } else {
                        alertDialog.dismiss();
                        alertDialog.dismiss();
                        Intent intentLogin = new Intent(mActivity, LoginActivity.class);
                        intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("msg", "");
                        intentLogin.putExtras(mBundle);
                        mActivity.startActivity(intentLogin);
                    }
                } else {
                    alertDialog.dismiss();
                    alertDialog.dismiss();
                    Intent intentLogin = new Intent(mActivity, LoginActivity.class);
                    intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("msg", "");
                    intentLogin.putExtras(mBundle);
                    mActivity.startActivity(intentLogin);
                }
            }
        });

        alertDialog.setCancelable(false);
    }


    /**
     * <h1>customAlertDialogCall</h1>
     * <p>the method is used to show the Call Dialog.</p>
     * @param mActivity : the Activity
     * @param phoneNo : phone number to contact
     */
    public static void customAlertDialogCall(final Activity mActivity, String phoneNo,AppTypeFace appTypeFace,boolean isTwilioEnable, boolean isTowingStart) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.alert_dialog_logout, null);
        alertDialogBuilder.setView(view);

        TextView tv_main_title = view.findViewById(R.id.tv_main_title);
        tv_main_title.setVisibility(View.GONE);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        tvMsg.setText(mActivity.getResources().getString(R.string.call_passenger));
        tvMsg.setTypeface(appTypeFace.getPro_narMedium());
        tvMsg.setGravity(Gravity.CENTER);

        TextView tvLogout = view.findViewById(R.id.tvLogout);
        tvLogout.setTypeface(appTypeFace.getPro_News());
        tvLogout.setText(mActivity.getResources().getString(R.string.call));
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        tvCancel.setTypeface(appTypeFace.getPro_News());



        if(isTowingStart){
            tvMsg.setText(phoneNo);
            tvLogout.setText(mActivity.getResources().getString(R.string.yes_alert));
            tvCancel.setText(mActivity.getResources().getString(R.string.no_alert));

        }

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Animator[] animHide = {null};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.show();
            view.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;
                    float finalRadius = (float) Math.hypot(cx, cy);
                    Animator animVisible = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    animHide[0] = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                    animVisible.start();

                }
            });
        } else {
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
            alertDialog.show();
        }


        tvLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();


                                if(!isTowingStart) {
                                    if (isTwilioEnable) {
                                        typeCall.inAppCall();
                                        /*Intent intent = new Intent(mActivity, ClientActivity.class);
                                        intent.putExtra(PHONE_NUMBER, phoneNo.trim());
                                        intent.putExtra(PHONE_IMAGE_URL, "");
                                        mActivity.startActivity(intent);*/
                                    } else
                                        mActivity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo)));

                                }else {
                                    serviceSelected.startTowingService();
                                }
                            }
                        });
                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    }
                    else
                    {
                        alertDialog.dismiss();
                        if(!isTowingStart) {
                            if (isTwilioEnable) {

                                typeCall.inAppCall();
                                /*Intent intent = new Intent(mActivity, ClientActivity.class);
                                intent.putExtra(PHONE_NUMBER, phoneNo.trim());
                                intent.putExtra(PHONE_IMAGE_URL, "");
                                mActivity.startActivity(intent);*/
                            } else
                                mActivity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo)));
                        }else {
                            serviceSelected.startTowingService();
                        }

                    }
                }
                else
                {
                    alertDialog.dismiss();
                    if(!isTowingStart) {
                        if (isTwilioEnable) {
                            typeCall.inAppCall();
                           /* Intent intent = new Intent(mActivity, ClientActivity.class);
                            intent.putExtra(PHONE_NUMBER, phoneNo.trim());
                            intent.putExtra(PHONE_IMAGE_URL, "");
                            mActivity.startActivity(intent);*/
                        } else
                            mActivity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo)));

                    }else {
                        serviceSelected.startTowingService();
                    }

                    mActivity.overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
                }
            }
        });


        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if ( animHide[0]!=null)
                    {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();
                            }
                        });
                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    }
                    else
                    {
                        alertDialog.dismiss();
                    }
                }
                else
                {
                    alertDialog.dismiss();
                    mActivity.overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
                }
            }
        });

        alertDialog.setCancelable(false);
    }


    /**
     * custom method to show alert dialog
     * mContext: reference of calling activity
     */
    public static void aDialogOnPermissionDenied(final Context mContext)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(mContext.getResources().getString(R.string.alert));
        alertDialogBuilder.setMessage(mContext.getResources().getString(R.string.reGrantPermissionMsg));
        alertDialogBuilder.setPositiveButton(mContext.getResources().getString(R.string.appSetting),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                        Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
                        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(settingsIntent);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    /******************************************************/

    interface DialongOkClickListener
    {
        void onClick();
    }



    /*****************************************************************************************/
    /**
     *<h1>mandatoryUpdateDialog</h1>
     * <p>show mandatory update dialog if the version is different</p>
     */
    public static Dialog mandatoryUpdateDialog(final Activity mActivity, boolean isMandatoryUpdateEnable) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.alert_dialog_simple_message, null);
        alertDialogBuilder.setView(view);

        TextView tvTitle = view.findViewById(R.id.tv_main_title);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        TextView tvUpdate = view.findViewById(R.id.tvUpdate);
        View view1 = view.findViewById(R.id.view);


        if(isMandatoryUpdateEnable){
            tvCancel.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }else {
            tvCancel.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
        }

        tvTitle.setText(mActivity.getResources().getString(R.string.alert));
        tvMsg.setText(mActivity.getResources().getString(R.string.mandatory_update));
        tvCancel.setText(mActivity.getResources().getString(R.string.not_now));
        tvUpdate.setText(mActivity.getResources().getString(R.string.Update));

        final AlertDialog alertDialog = alertDialogBuilder.create();
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Animator[] animHide = {null};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /*alertDialog.show();*/
            view.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;
                    float finalRadius = (float) Math.hypot(cx, cy);
                    Animator animVisible = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    animHide[0] = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                    animVisible.start();

                }
            });
        } else {
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
            /*alertDialog.show();*/
        }


        tvUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Uri uri = Uri.parse("market://details?id=" +mActivity.getPackageName());
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                try {
                                    mActivity.startActivity(goToMarket);
                                } catch (ActivityNotFoundException e)
                                {
                                    mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + mActivity.getPackageName())));

                                }

                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();

                            }
                        });
                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    } else {
                        alertDialog.dismiss();
                    }
                } else {
                    alertDialog.dismiss();
                }
            }
        });

        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if ( animHide[0]!=null)
                    {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();
                            }
                        });
                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    }
                    else
                    {
                        alertDialog.dismiss();
                    }
                }
                else
                {
                    alertDialog.dismiss();
                    mActivity.overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
                }
            }
        });

        alertDialog.setCancelable(false);
        return alertDialog;
    }



    /*****************************************************************************************/
    /**
     *<h1>mandatoryUpdateDialog</h1>
     * <p>show mandatory update dialog if the version is different</p>
     */
    public static Dialog walletLimitWarning(final Activity mActivity, String cashBalance,String softLimit,String hardLimit) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.alert_dialog_simple_message, null);
        alertDialogBuilder.setView(view);

        TextView tvTitle = view.findViewById(R.id.tv_main_title);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        TextView tvUpdate = view.findViewById(R.id.tvUpdate);
        View view1 = view.findViewById(R.id.view);

        Double walletBalance = Double.parseDouble(cashBalance);
        Double walletSoftLimit = Double.parseDouble(softLimit);
        Double walletHardLimit = Double.parseDouble(hardLimit);

        tvTitle.setText(mActivity.getResources().getString(R.string.alert));
        tvCancel.setText(mActivity.getResources().getString(R.string.not_now));
        tvUpdate.setText(mActivity.getResources().getString(R.string.recharge));


        if(walletBalance <= walletHardLimit){
            tvCancel.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            tvMsg.setText(mActivity.getResources().getString(R.string.lessthanHard));
        }else if(walletBalance <= walletSoftLimit){
            tvCancel.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            tvMsg.setText(mActivity.getResources().getString(R.string.lessthanSoft));
        }

        final AlertDialog alertDialog = alertDialogBuilder.create();
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Animator[] animHide = {null};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;
                    float finalRadius = (float) Math.hypot(cx, cy);
                    Animator animVisible = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    animHide[0] = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                    animVisible.start();
                }
            });



            /*alertDialog.show();*/
            /*view.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;
                    float finalRadius = (float) Math.hypot(cx, cy);
                    Animator animVisible = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    animHide[0] = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                    animVisible.start();

                }
            });*/
        } else {
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
            /*alertDialog.show();*/
        }


        tvUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {

                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();
                                dialogCallbackHelper.walletFragOpen();

                            }
                        });
                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    } else {
                        alertDialog.dismiss();
                        dialogCallbackHelper.walletFragOpen();
                    }
                } else {
                    alertDialog.dismiss();
                    dialogCallbackHelper.walletFragOpen();
                }
            }
        });

        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if ( animHide[0]!=null)
                    {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();
                            }
                        });
                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    }
                    else
                    {
                        alertDialog.dismiss();
                    }
                }
                else
                {
                    alertDialog.dismiss();
                    mActivity.overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
                }
            }
        });

        alertDialog.setCancelable(false);
        return alertDialog;
    }

    /**
     *<h1>customAlertDialogLogout</h1>
     * <p>Logout dialog</p>
     */
    public static void languageSelectDialog(final Activity mActivity, AppTypeFace appTypeFace, ArrayList<LanguagesList> languagesList, int indexSelected) {

        ArrayList languageListTemp = new ArrayList<>();
        for(int language = 0; language< languagesList.size(); language++)
        {
            languageListTemp.add(languagesList.get(language).getName());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        builder.setTitle(mActivity.getString(R.string.selectLanguage));
        builder.setSingleChoiceItems((CharSequence[]) languageListTemp.toArray(new CharSequence[languagesList.size()]),
                indexSelected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String langCode = languagesList.get(languagesList.indexOf(languagesList.get(i))).getCode();
                        String langName = languagesList.get(languagesList.indexOf(languagesList.get(i))).getName();
                        int dir = Utility.changeLanguageConfig(langCode, mActivity);

                        dialogCallbackHelper.changeLanguage(langCode, langName, dir);

                        if (alertDialog != null && alertDialog.isShowing())
                            alertDialog.dismiss();

                    }
                });

        alertDialog = builder.create();
        alertDialog.show();
    }




    /**
     *<h1>alertForPreferenceZone</h1>
     */
    public static void alertForPreferenceZone(final Activity mActivity, AppTypeFace appTypeFace, Integer trigger) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.alert_dialog_logout, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);
        TextView tvTitle = view.findViewById(R.id.tv_main_title);
        tvTitle.setTypeface(appTypeFace.getPro_narMedium());
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        tvMsg.setTypeface(appTypeFace.getPro_News());
        tvMsg.setVisibility(View.VISIBLE);
        TextView tvLogout = view.findViewById(R.id.tvLogout);
        tvLogout.setTypeface(appTypeFace.getPro_narMedium());
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        tvCancel.setTypeface(appTypeFace.getPro_narMedium());


        if(trigger==VariableConstant.SELECT_PREFERED_ZONE_SELECT){
            tvMsg.setText(mActivity.getResources().getString(R.string.preferencezonemsg));
            tvLogout.setText(mActivity.getResources().getString(R.string.multipe));
            tvCancel.setText(mActivity.getResources().getString(R.string.one));
        }else {
            tvMsg.setText(mActivity.getResources().getString(R.string.deletepreferencezonemsg));
            tvLogout.setText(mActivity.getResources().getString(R.string.yes_alert));
            tvCancel.setText(mActivity.getResources().getString(R.string.no_alert));
        }

        final AlertDialog alertDialog = alertDialogBuilder.create();

        final Animator[] animHide = {null};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.show();
            view.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;
                    float finalRadius = (float) Math.hypot(cx, cy);
                    Animator animVisible = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    animHide[0] = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                    animVisible.start();

                }
            });
        } else {
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
            alertDialog.show();
        }


        tvLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();

                                if(trigger==VariableConstant.SELECT_PREFERED_ZONE_SELECT){
                                    dialogPreferenceSelect.multileSelection();
                                }else {
                                    dialogPreferenceSelect.deletePreferences();
                                }


                            }
                        });
                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    } else {
                        alertDialog.dismiss();
                        if(trigger==VariableConstant.SELECT_PREFERED_ZONE_SELECT){
                            dialogPreferenceSelect.multileSelection();
                        }else {
                            dialogPreferenceSelect.deletePreferences();
                        }

                    }
                } else {
                    alertDialog.dismiss();
                    if(trigger==VariableConstant.SELECT_PREFERED_ZONE_SELECT){
                        dialogPreferenceSelect.multileSelection();
                    }else {
                        dialogPreferenceSelect.deletePreferences();
                    }

                    mActivity.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                }
            }
        });


        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();
                                if(trigger==VariableConstant.SELECT_PREFERED_ZONE_SELECT)
                                    dialogPreferenceSelect.oneSelection();
                            }
                        });

                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    } else {
                        alertDialog.dismiss();
                        if(trigger==VariableConstant.SELECT_PREFERED_ZONE_SELECT)
                            dialogPreferenceSelect.oneSelection();
                    }
                } else {
                    alertDialog.dismiss();
                    if(trigger==VariableConstant.SELECT_PREFERED_ZONE_SELECT)
                        dialogPreferenceSelect.oneSelection();
                    mActivity.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                }
            }
        });

        alertDialog.setCancelable(false);
    }


    /**
     * <h1>BookingAccept</h1>
     * <p>interface for inform the selection of accept or reject</p>
     */
    public interface BookingAccept{

        void accepted();
        void rejected();
    }

    public interface ServiceSelected{

        void serviceSelected(String serviceID);
        void canceled();
        void startTowingService();
    }

    /**
     *<h1>showServices</h1>
     * @param mActivity
     */
    public static void showServices(final Activity mActivity, AppTypeFace appTypeFace, ArrayList<String> serviceList) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_service_list, null);
        alertDialogBuilder.setView(view);
        TextView tvTitle = view.findViewById(R.id.tv_main_title);
        RecyclerView rv_services = view.findViewById(R.id.rv_services);
        TextView tv_reject = view.findViewById(R.id.tv_reject);
        TextView tv_accept = view.findViewById(R.id.tv_accept);
        TextView tv_service_error = view.findViewById(R.id.tv_service_error);

        tvTitle.setTypeface(appTypeFace.getPro_narMedium());
        tv_reject.setTypeface(appTypeFace.getPro_narMedium());
        tv_accept.setTypeface(appTypeFace.getPro_narMedium());
        tv_service_error.setTypeface(appTypeFace.getPro_News());

        BookingCancelRVA bookingCancelRVA = new BookingCancelRVA(mActivity);
        if(serviceList!=null && serviceList.size()>0){
            tv_service_error.setVisibility(View.GONE);
            rv_services.setVisibility(View.VISIBLE);


            rv_services.setLayoutManager(new LinearLayoutManager(mActivity));
            rv_services.setNestedScrollingEnabled(true);
            bookingCancelRVA.setClickEnable(false);
            bookingCancelRVA.setCancelReasons(serviceList);
            rv_services.setAdapter(bookingCancelRVA);



        }else {
            tv_service_error.setVisibility(View.VISIBLE);
            rv_services.setVisibility(View.GONE);
        }

        final AlertDialog alertDialog = alertDialogBuilder.create();

        final Animator[] animHide = {null};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.show();
            view.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;
                    float finalRadius = (float) Math.hypot(cx, cy);
                    Animator animVisible = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    animHide[0] = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                    animVisible.start();

                }
            });
        } else {
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
            alertDialog.show();
        }


        tv_accept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();
                                bookingAccept.accepted();

                            }
                        });
                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    } else {
                        alertDialog.dismiss();
                        bookingAccept.accepted();
                    }
                } else {
                    alertDialog.dismiss();
                    bookingAccept.accepted();
                    mActivity.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                }
            }
        });


        tv_reject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();
                                bookingAccept.rejected();
                            }
                        });

                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    } else {
                        alertDialog.dismiss();
                        bookingAccept.rejected();
                    }
                } else {
                    alertDialog.dismiss();
                    bookingAccept.rejected();
                    mActivity.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                }
            }
        });

        alertDialog.setCancelable(false);
    }




    /**
     *<h1>serviceSelectionForCompleteBooking</h1>
     */
    public static void serviceSelectionForCompleteBooking(final Activity mActivity, AppTypeFace appTypeFace, TowTrayService serviceList) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_towtruck_service_select, null);
        alertDialogBuilder.setView(view);
        TextView tvTitle = view.findViewById(R.id.tv_main_title);
        tvTitle.setText(mActivity.getResources().getString(R.string.select_service_provided));
        RecyclerView rv_services = view.findViewById(R.id.rv_services);
        TextView  tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_done = view.findViewById(R.id.tv_done);
        TextView tv_service_error = view.findViewById(R.id.tv_service_error);

        tvTitle.setTypeface(appTypeFace.getPro_narMedium());
        tv_cancel.setTypeface(appTypeFace.getPro_narMedium());
        tv_done.setTypeface(appTypeFace.getPro_narMedium());
        tv_service_error.setTypeface(appTypeFace.getPro_News());







        if(serviceList!=null && serviceList.getData().size()>0){
            tv_service_error.setVisibility(View.GONE);
            rv_services.setVisibility(View.VISIBLE);


            rv_services.setLayoutManager(new LinearLayoutManager(mActivity));
            rv_services.setNestedScrollingEnabled(true);
            MultipleSelectionServiceRVA multipleSelectionServiceRVA = new MultipleSelectionServiceRVA(mActivity, serviceList.getData(),SERVICELIST);
            rv_services.setAdapter(multipleSelectionServiceRVA);
            multipleSelectionServiceRVA.getCallBackForServiceSelect(new MultipleSelectionServiceRVA.callBackForServiceSelect() {
                @Override
                public void ServiceSelected(ArrayList<TowtrayServiceSelectData> towtrayServiceSelectedData) {
                    towtrayServiceSelectData = new ArrayList<>();
                    serviceID = "";
                    towtrayServiceSelectData = towtrayServiceSelectedData;

                    for(int i=0;i<towtrayServiceSelectData.size();i++){
                        if(towtrayServiceSelectedData.get(i).isSelected()){

                            if(serviceID.isEmpty()) {
                                serviceID = towtrayServiceSelectData.get(i).getId();
                            }
                            else {
                                serviceID = serviceID.concat(",").concat(towtrayServiceSelectData.get(i).getId());
                            }

                        }


                    }



                }
            });

        }else {
            tv_service_error.setVisibility(View.VISIBLE);
            rv_services.setVisibility(View.GONE);
        }

        final AlertDialog alertDialog = alertDialogBuilder.create();

        final Animator[] animHide = {null};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.show();
            view.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;
                    float finalRadius = (float) Math.hypot(cx, cy);
                    Animator animVisible = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    animHide[0] = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                    animVisible.start();

                }
            });
        } else {
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
            alertDialog.show();
        }



        tv_done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

               /* if(towtrayServiceSelectData!=null && !serviceID.matches("")){*/

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (animHide[0] != null) {
                            animHide[0].addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    alertDialog.dismiss();
                                    Utility.printLog("the serviceID : "+serviceID);
                                    serviceSelected.serviceSelected(serviceID);


                                }
                            });
                            if (!animHide[0].isStarted())
                                animHide[0].start();
                        } else {
                            alertDialog.dismiss();
                            Utility.printLog("the serviceID : "+serviceID);
                            serviceSelected.serviceSelected(serviceID);
                        }
                    } else {
                        alertDialog.dismiss();
                        Utility.printLog("the serviceID : "+serviceID);
                        serviceSelected.serviceSelected(serviceID);
                        mActivity.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                    }


                /*}else {
                    Utility.BlueToast(mActivity,mActivity.getResources().getString(R.string.selectServices));
                }*/


            }
        });




        tv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();
                                serviceSelected.canceled();

                            }
                        });

                        if(!animHide[0].isStarted())
                            animHide[0].start();
                    } else {
                        alertDialog.dismiss();
                        serviceSelected.canceled();

                    }
                } else {
                    serviceSelected.canceled();
                    alertDialog.dismiss();
                    mActivity.overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                }
            }
        });

        alertDialog.setCancelable(false);
    }
}
