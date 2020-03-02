package com.karry.manager.location;

import com.google.android.gms.common.api.Status;

/**
 * <h1>LocationCallBack</h1>
 * This interface is used to inform for the service provider
 * @author 3Embed
 * @since on 12-01-2018.
 */

public interface LocationCallBack
{
    /**
     * <h2>locationDisabled</h2>
     * This method is triggered when location provider is disabled
     */
    void onLocationServiceDisabled(Status status);

    interface View
    {
        /**
         * <h2>promptUserWithLocationAlert</h2>
         * This method is used to show the user prompt for location
         * @param status status of location
         */
        void promptUserWithLocationAlert(Status status);
    }
}
