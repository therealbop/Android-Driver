package com.karry.manager.location;

import android.location.Location;

/**
 * <h1>LocationManagerCallback</h1>
 * This interface is used to provide callbacks necessary for location
 * @author 3Embed
 * @since on 08-11-2017.
 */

public interface LocationManagerCallback
{
    interface CallBacks
    {
        /**
         * <h2>onUpdateLocation</h2>
         * <p>
         * This method is used to update the location.
         * </p>
         * @param location instance of Location.
         */
        void onUpdateLocation(Location location);
        /**
         * <h2>locationMsg</h2>
         * <p>
         * This method is used to get the message.
         * </p>
         * @param error message error
         */
        void locationMsg(String error);
    }
    interface ProvideCallBacks
    {
       void registerCallbacks(CallBacks callBacks) ;
    }
}
