package com.karry.utility;

import android.Manifest.permission;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.PowerManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;


public class PowerSaverHelper {
    public enum PowerSaveState {
        ON, OFF, ERROR_GETTING_STATE, IRRELEVANT_OLD_ANDROID_API
    }

    public enum WhiteListedInBatteryOptimizations {
        WHITE_LISTED, NOT_WHITE_LISTED, ERROR_GETTING_STATE, UNKNOWN_TOO_OLD_ANDROID_API_FOR_CHECKING, IRRELEVANT_OLD_ANDROID_API
    }

    public enum DozeState {
        NORMAL_INTERACTIVE, DOZE_TURNED_ON_IDLE, NORMAL_NON_INTERACTIVE, ERROR_GETTING_STATE, IRRELEVANT_OLD_ANDROID_API, UNKNOWN_TOO_OLD_ANDROID_API_FOR_CHECKING
    }

    @NonNull
    public static DozeState getDozeState(@NonNull Context context) {
      if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
        return DozeState.IRRELEVANT_OLD_ANDROID_API;
      }
        if (VERSION.SDK_INT < VERSION_CODES.M) {
            return DozeState.UNKNOWN_TOO_OLD_ANDROID_API_FOR_CHECKING;
        }
        final PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
      if (pm == null) {
        return DozeState.ERROR_GETTING_STATE;
      }
        return pm.isDeviceIdleMode() ? DozeState.DOZE_TURNED_ON_IDLE : pm.isInteractive() ? DozeState.NORMAL_INTERACTIVE : DozeState.NORMAL_NON_INTERACTIVE;
    }

    @NonNull
    public static PowerSaveState getPowerSaveState(@NonNull Context context) {
      if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
        return PowerSaveState.IRRELEVANT_OLD_ANDROID_API;
      }
        final PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
      if (pm == null) {
        return PowerSaveState.ERROR_GETTING_STATE;
      }
        return pm.isPowerSaveMode() ? PowerSaveState.ON : PowerSaveState.OFF;
    }


    @NonNull
    public static WhiteListedInBatteryOptimizations getIfAppIsWhiteListedFromBatteryOptimizations(@NonNull Context context, @NonNull String packageName) {
      if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
        return WhiteListedInBatteryOptimizations.IRRELEVANT_OLD_ANDROID_API;
      }
      if (VERSION.SDK_INT < VERSION_CODES.M) {
        return WhiteListedInBatteryOptimizations.UNKNOWN_TOO_OLD_ANDROID_API_FOR_CHECKING;
      }
        final PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
      if (pm == null) {
        return WhiteListedInBatteryOptimizations.ERROR_GETTING_STATE;
      }
        return pm.isIgnoringBatteryOptimizations(packageName) ? WhiteListedInBatteryOptimizations.WHITE_LISTED : WhiteListedInBatteryOptimizations.NOT_WHITE_LISTED;
    }

    @TargetApi(VERSION_CODES.M)
    @RequiresPermission(permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
    @Nullable
    public static Intent prepareIntentForWhiteListingOfBatteryOptimization(@NonNull Context context, @NonNull String packageName, boolean alsoWhenWhiteListed) {
      if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
        return null;
      }
      if (ContextCompat.checkSelfPermission(context, permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) == PackageManager.PERMISSION_DENIED) {
        return null;
      }
        final WhiteListedInBatteryOptimizations appIsWhiteListedFromPowerSave = getIfAppIsWhiteListedFromBatteryOptimizations(context, packageName);
        Intent intent = null;
        switch (appIsWhiteListedFromPowerSave) {
            case WHITE_LISTED:
              if (alsoWhenWhiteListed) {
                intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
              }
                break;
            case NOT_WHITE_LISTED:
                intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).setData(
                    Uri.parse("package:" + packageName));
                break;
            case ERROR_GETTING_STATE:
            case UNKNOWN_TOO_OLD_ANDROID_API_FOR_CHECKING:
            case IRRELEVANT_OLD_ANDROID_API:
            default:
                break;
        }
        return intent;
    }

    /**
     * registers a receiver to listen to power-save events. returns true iff succeeded to register the broadcastReceiver.
     */
    @TargetApi(VERSION_CODES.M)
    public static boolean registerPowerSaveReceiver(@NonNull Context context, @NonNull BroadcastReceiver receiver) {
      if (VERSION.SDK_INT < VERSION_CODES.M) {
        return false;
      }
        IntentFilter filter = new IntentFilter();
        filter.addAction(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED);
        context.registerReceiver(receiver, filter);
        return true;
    }


}