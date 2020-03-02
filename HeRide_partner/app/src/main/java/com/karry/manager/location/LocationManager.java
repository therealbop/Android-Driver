package com.karry.manager.location;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.karry.utility.Utility;

import javax.inject.Inject;

import static com.karry.utility.LocationUtil.REQUEST_CHECK_SETTINGS;


/**
 * <h>LocationManager</h>
 * <P>
 *     Class to manage the google location api connect services and
 *     provide the updated location to calling activity/fragment
 * </P>
 */
public class LocationManager implements ConnectionCallbacks, OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult>, GpsStatus.Listener
    ,LocationManagerCallback.ProvideCallBacks
{
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final String TAG = "LocationManager";
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates = false;
    /**
     * Activity reference for the activity in which location service is associate with it.
     * */
    private Activity mactivity;

    private LocationManagerCallback.CallBacks location_listener_service = null;

    /**
     * Constructor of the location listener class.
     * */
    @Inject
    public LocationManager(Activity activity) {
        Utility.printLog(TAG+" inside location manager constructor ");
        Utility.printLog(TAG+" inside location manager activity "+activity);
        this.mactivity = activity;
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    /**
     * <h2>buildGoogleApiClient</h2>
     * <P>
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     * </P>
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mactivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        /**
         * Setting up the update interval.*/
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        /**
         * Sets the fastest rate for active location updates. This interval is exact, and your
         *application will never receive updates faster than this value.*/
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        /**
         * Setting the priority as high for better accuracy.*/
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * <h2>buildLocationSettingsRequest</h2>
     * <P>
     * if a device has the needed location settings when location service is off.
     * </P>
     */
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * <h2>checkLocationSettings</h2>
     * <P>
     * Check if the device's location settings are adequate for the app's needs.
     * </P>
     */
    public void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        mLocationSettingsRequest);
        result.setResultCallback(this);
    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     *
     * case 1:SUCCESS: when location is on.
     * case 2:RESOLUTION_REQUIRED: needed to show alert for on/off.
     */
    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(mactivity, REQUEST_CHECK_SETTINGS);
                    Utility.printLog("value of onactiviytyresult: locationutil: "+REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Toast.makeText(mactivity, "Location settings are inadequate, and cannot be fixed here. Dialog not created.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * <h2>startLocationUpdates</h2>
     * <p>
     * Requests location updates from the FusedLocationApi.
     * </p>
     */
    private void startLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(mactivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mactivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    mRequestingLocationUpdates = true;
                }
            });
        }
    }

    /**
     * <h2>stoppingLocationUpdate</h2>
     * <P>
     * Removes location updates from the FusedLocationApi.
     * </P>
     */
    public void stoppingLocationUpdate()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        /**
         * On Connected doing the location Update.*/
        checkLocationSettings();
    }


    @Override
    public void onConnectionSuspended(int i)
    {
        Log.i("Location", "Connection suspended");
        location_listener_service.locationMsg("Location"+"Connection suspended");
    }

    @Override
    public void onLocationChanged(Location location)
    {
        Log.i("Location",""+location.getLatitude()+"  "+location.getLongitude());
        /**
         * On Connect checking if the location is available or not.if Not the again Updating the location .
         * */
        if (location == null)
        {
            reStart_Location_update();
        }else
        {
            location_listener_service.onUpdateLocation(location);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        location_listener_service.locationMsg("Location"+"Connection failed: ConnectionResult.getErrorCode()=" + connectionResult.getErrorCode());
        Log.i("Location", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    /**
     *<h2>stop_Location_Update</h2>
     * <p>
     *     method to stop location updates and disconnect googleApiClient
     * </p>
     */
    public void stop_Location_Update()
    {
        if (mGoogleApiClient!=null&&mGoogleApiClient.isConnected())
        {
            stoppingLocationUpdate();
        }
    }


    /**
     *<h2>reStart_Location_update</h2>
     * <p>
     *     method to restart location updates and reconnect googleApiClient
     *     if its instance already created
     * </p>
     */
    public void reStart_Location_update()
    {
        if (mGoogleApiClient!=null&& mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }


    /**
     *<h2>stop_Location_Update</h2>
     * <p>
     *     method to disconnect googleApiClient
     * </p>
     */
    public void destroy_Location_update()
    {
        if(mGoogleApiClient!=null)
        {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * This method is used to check that our googleApi is connected or not.
     * @return result (Connected/ Disconnected)
     */
    public boolean getHasGoogleApiConnected()
    {
        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
        {
            return  true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onGpsStatusChanged(int event) {
        Utility.printLog("value of gps status: "+event);
    }

    @Override
    public void registerCallbacks(LocationManagerCallback.CallBacks callBacks)
    {
        Utility.printLog(TAG+" inside registerCallbacks");
        this.location_listener_service = callBacks;
    }
}
