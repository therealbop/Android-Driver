package com.karry.utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.heride.partner.R;
import com.karry.side_screens.wallet.adapter.Alerts;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.karry.utility.VariableConstant.PERMISSION_BLOCKED;
import static com.karry.utility.VariableConstant.PERMISSION_DENIED;
import static com.karry.utility.VariableConstant.PERMISSION_GRANTED;


/**
 * Created by PrashantSingh on 3/14/16.
 */
public class AppPermissionsRunTime {
    //private ArrayList<String> requestedPermissionsList = null;
    private static ArrayList<String> requiredPermissionsList = null;
    private static ArrayList<String> requiredPermissionMsgs = null;

    private final int REQUEST_CODE_PERMISSIONS = VariableConstant.REQUEST_CODE;
    private List<String> permissionsNeeded=null;
    private List<String> permissionsList=null;
    private androidx.appcompat.app.AlertDialog dialog_parent=null;
    private Alerts alerts;


    @Inject
    public AppPermissionsRunTime(Alerts alerts){
        this.alerts = alerts;
    }
    public enum Permission
    {
        LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, PHONE, RECORD_AUDIO, READ_CONTACT, READ_SMS, RECEIVE_SMS
    }

    /**
     * custom method to check, add and request for run time permissions
     *
     * @param mActivity:               reference of current activity
     * @param rqstedPermissionsList:   list of the permissions requested
     * @param PERMISSION_REQUEST_CODE: int type to check the corresponding response on onRequestPermissionsResult
     * @return boolean: true if all requested permissions are already granted else false
     */
    public static boolean checkPermission(final Activity mActivity, ArrayList<MyPermissionConstants> rqstedPermissionsList, final int PERMISSION_REQUEST_CODE) {
        if (requiredPermissionsList == null) {
            requiredPermissionsList = new ArrayList<String>();
            requiredPermissionMsgs = new ArrayList<String>();
        } else {
            requiredPermissionsList.clear();
            requiredPermissionMsgs.clear();
        }


        if (rqstedPermissionsList != null && rqstedPermissionsList.size() > 0) {
            for (MyPermissionConstants requestedPermission : rqstedPermissionsList) {

                switch (requestedPermission) {
                    // to get device Id
                    case PERMISSION_READ_PHONE_STATE:
                        addPermission(Manifest.permission.READ_PHONE_STATE, mActivity);
                        break;

                    case PERMISSION_CALL:
                        addPermission(Manifest.permission.CALL_PHONE, mActivity);
                        break;


                    // to access fine & corase location along with phone state for device id
                    case PERMISSION_ACCESS_FINE_LOCATION:
                        addPermission(Manifest.permission.ACCESS_FINE_LOCATION, mActivity);
                        break;

                    case PERMISSION_ACCESS_COARSE_LOCATION:
                        addPermission(Manifest.permission.ACCESS_COARSE_LOCATION, mActivity);
                        break;


                    case PERMISSION_CAMERA:
                        addPermission(Manifest.permission.CAMERA, mActivity);
                        break;


                    case PERMISSION_WRITE_EXTERNAL_STORAGE:
                        addPermission(Manifest.permission.READ_EXTERNAL_STORAGE, mActivity);
                        break;

                    case PERMISSION_READ_EXTERNAL_STORAGE:
                        addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, mActivity);
                        break;


                    case PERMISSION_SEND_SMS:
                        addPermission(Manifest.permission.SEND_SMS, mActivity);
                        break;

                    case PERMISSION_RECEIVE_SMS:
                        addPermission(Manifest.permission.RECEIVE_SMS, mActivity);
                        break;

                    case PERMISSION_READ_SMS:
                        addPermission(Manifest.permission.READ_SMS, mActivity);
                        break;


                    case PERMISSSION_READ_CALENDAR:
                        addPermission(Manifest.permission.READ_CALENDAR, mActivity);
                        break;

                    case PERMISSION_WRITE_CALENDAR:
                        addPermission(Manifest.permission.WRITE_CALENDAR, mActivity);
                        break;


                    case PERMISSSION_READ_CONTACTS:
                        addPermission(Manifest.permission.READ_CONTACTS, mActivity);
                        break;

                    case PERMISSION_WRITE_CONTACTS:
                        addPermission(Manifest.permission.WRITE_CONTACTS, mActivity);
                        break;

                    default:
                        break;
                }
            }
        }

        if (requiredPermissionsList.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.requestPermissions(requiredPermissionsList.toArray(new String[requiredPermissionsList.size()]), PERMISSION_REQUEST_CODE);
            }
            return false;
        } else {
            return true;
        }

    }

    /******************************************************/

    /**
     * custom method to check whether the requested permission has granted or not
     * If hasn't been granted add to requested list
     *
     * @param permission: the requested permission
     * @param mActivity:  current activity reference
     */
    private static void addPermission(final String permission, final Activity mActivity) {
        if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissionsList.add(permission);
        }
    }

    /**
     *
     * @param permission_list: list of required permissions
     * @param activity: calling activity reference
     * @param isFixed:
     * @return true if requested permissions are already granted
     */
    public boolean getPermission(final ArrayList<Permission> permission_list,Activity activity,boolean isFixed)
    {
        /**
         * Creating the List if not created .
         * if created then clear the list for refresh use.*/
        if(permissionsNeeded==null||permissionsList==null)
        {
            permissionsNeeded= new ArrayList<>();
            permissionsList= new ArrayList<>();
        }else
        {
            permissionsNeeded.clear();
            permissionsList.clear();
        }

        if(dialog_parent!=null&&dialog_parent.isShowing())
        {
            dialog_parent.dismiss();
            dialog_parent.cancel();
        }
        for(int count=0;permission_list!=null&&count<permission_list.size();count++)
        {
            switch (permission_list.get(count))
            {
                case LOCATION:
                    if (!   addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION,activity))
                    {
                        permissionsNeeded.add("GPS Fine Location");
                    }
                    if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION,activity))
                    {
                        permissionsNeeded.add("GPS Course Location");
                    }
                    break;
                case RECORD_AUDIO:
                    if (!   addPermission(permissionsList,Manifest.permission.RECORD_AUDIO,activity))
                    {
                        permissionsNeeded.add("Record audio");
                    }
                    break;
                case CAMERA:
                    if (!addPermission(permissionsList, Manifest.permission.CAMERA,activity))
                    {
                        permissionsNeeded.add("Camera");
                    }
                    break;
                case READ_EXTERNAL_STORAGE:
                    if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE,activity))
                    {
                        permissionsNeeded.add("Write to external Storage");
                    }
                    break;
                case WRITE_EXTERNAL_STORAGE:
                    if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE,activity))
                    {
                        permissionsNeeded.add("Read to external Storage");
                    }
                    break;
                case PHONE:
                    if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE,activity))
                    {
                        permissionsNeeded.add("Read Phone State");
                    }
                    break;
                case READ_CONTACT:
                    if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS,activity))
                    {
                        permissionsNeeded.add("Read Contact State");
                    }
                case READ_SMS:
                    if (!addPermission(permissionsList, Manifest.permission.READ_SMS,activity))
                    {
                        permissionsNeeded.add("Read SMS State");
                    }
                case RECEIVE_SMS:
                    if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS,activity))
                    {
                        permissionsNeeded.add("Receive SMS State");
                    }
                    break;
                default:
                    break;
            }

        }
        if (permissionsList.size() > 0&&permissionsNeeded.size() > 0)
        {
            String message = "You need to grant access to " + permissionsNeeded.get(0);
            for (int i = 1; i < permissionsNeeded.size(); i++)
            {
                message = message + ", " + permissionsNeeded.get(i);
            }
            showAlert(message,activity,isFixed);
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * <h2>showAlert</h2>
     * <p>
     *     method to show alert for requested permission has been granted or not
     * </p>
     * @param message: to be display in permission alert
     * @param mActivity: calling activity reference
     * @param isFixed: boolean true if permission has denied permanently
     */
    private void showAlert(final String message, final Activity mActivity, final boolean isFixed)
    {
        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
        alertDialog.setTitle("Note.");
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();
                check_for_Permission(permissionsList.toArray(new String[permissionsList.size()]),mActivity);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                if (isFixed) {
                    Toast.makeText(mActivity, "To Proceed fourther App need " + "\n" + message, Toast.LENGTH_LONG).show();
                    dialog.cancel();
                    mActivity.onBackPressed();
                } else {
                    Toast.makeText(mActivity, "To Proceed fourther App need " + "\n" + message, Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }

            }
        });
        dialog_parent = alertDialog.show();
        dialog_parent.show();
    }

    /**
     * <h2>check_for_Permission</h2>
     * <p>
     *     method to check whether requested permission has granted or not
     * </p>
     * @param permissions: array of permissions to be requested
     * @param mactivity:* calling activity reference
     */
    public void check_for_Permission(String permissions[],Activity mactivity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mactivity.requestPermissions(permissions, REQUEST_CODE_PERMISSIONS);
        }
    }




    /**
     *
     * @param permissionsList: array of requested permissions
     * @param permission: array of requested permissions
     * @param activity: calling activity reference
     * @return
     */
    private boolean addPermission(List<String> permissionsList, String permission, Activity activity)
    {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsList.add(permission);
            return false;
        }else
        {
            return true;
        }
    }
    /******************************************************/

    /**
     * custom method to show alert dialog
     * mContext: reference of calling activity
     */
    public static void aDialogOnPermissionDenied(final Context mContext) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(mContext.getResources().getString(R.string.uhOh));
        alertDialogBuilder.setMessage(mContext.getResources().getString(R.string.reGrantPermissionMsg));
        alertDialogBuilder.setPositiveButton(mContext.getResources().getString(R.string.appSetting),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                        Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
                        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(settingsIntent);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    /******************************************************/

    public enum MyPermissionConstants {
        PERMISSION_READ_PHONE_STATE, PERMISSION_CALL,
        PERMISSION_ACCESS_FINE_LOCATION, PERMISSION_ACCESS_COARSE_LOCATION, PERMISSION_CAMERA,
        PERMISSION_WRITE_EXTERNAL_STORAGE, PERMISSION_READ_EXTERNAL_STORAGE,
        PERMISSION_SEND_SMS, PERMISSION_READ_SMS, PERMISSION_RECEIVE_SMS,
        PERMISSSION_READ_CALENDAR, PERMISSION_WRITE_CALENDAR,
        PERMISSSION_READ_CONTACTS, PERMISSION_WRITE_CONTACTS
    }
    /******************************************************/



    /**
     * <h2>getPermissionStatus</h2>
     * used to get the status of the persmission
     * @param activity activity
     * @param androidPermissionName name of permission
     * @return returns the status
     */
    public int getPermissionStatusCall(Activity activity, String androidPermissionName,boolean mandatePermission)
    {
        if(ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED)
        {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName))
            {
                if(mandatePermission)
                {
                    Dialog alert = alerts.userPromptWithOneButton(activity.getResources().getString(R.string.permission_alert),activity);
                    TextView tv_alert_ok = alert.findViewById(R.id.tv_alert_ok);
                    TextView tv_alert_title = alert.findViewById(R.id.tv_alert_title);
                    tv_alert_ok.setText(activity.getResources().getString(R.string.ok));
                    tv_alert_title.setText(activity.getResources().getString(R.string.alert_call_permision));
                    tv_alert_ok.setOnClickListener(view ->
                    {
                        alert.dismiss();
                        activity.onBackPressed();
                    });
                    alert.show();
                }
                return PERMISSION_BLOCKED;
            }
            return PERMISSION_DENIED;
        }
        return PERMISSION_GRANTED;
    }

    /**
     * <h2>checkIfPermissionNeeded</h2>
     * used to check sdk version to ask run time persmission
     * @return returns if to ask persmission
     */
    public boolean checkIfPermissionNeeded()
    {
        return Build.VERSION.SDK_INT >= 23;
    }

    /**
     * <h2>checkIfPermissionGrant</h2>
     * used to check whether permission is granted
     * @param permission permission name
     * @param activity activity
     * @return returns true if granted
     */
    public boolean checkIfPermissionGrant(String permission, Activity activity)
    {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * <h2>requestForPermission</h2>
     * <p>
     *     method to check whether requested permission has granted or not
     * </p>
     * @param permissions: array of permissions to be requested
     * @param mActivity:* calling activity reference
     */
    public void requestForPermission(String permissions[], Activity mActivity, int requestCode)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            mActivity.requestPermissions(permissions, requestCode);
        }
    }

}