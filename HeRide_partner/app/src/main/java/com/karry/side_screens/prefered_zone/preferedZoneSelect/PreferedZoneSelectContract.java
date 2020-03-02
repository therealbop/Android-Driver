package com.karry.side_screens.prefered_zone.preferedZoneSelect;


import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;
import com.karry.BaseView;
import com.karry.pojo.AreaZone.AreaZoneDataZones;

/**
 * <h1>PreferedZoneListContract</h1>
 */
interface PreferedZoneSelectContract {

    /**
     * <h1>PreferedZoneView</h1>
     */
    interface PreferedZoneSelectView extends BaseView {


        /**
         * <h1>initActionBar</h1>
         * <p>method for initialize the ActionBar</p>
         */
        void initActionBar();

        /**
         * <h1>setTitle</h1>
         * <p>method for set the title of ActionBar</p>
         */
        void setTitle(String heading_title, String bottom_title);

        /**
         * <h1>drawAreaZones</h1>
         * @param polygonOptions :
         */
        void drawAreaZones(PolygonOptions polygonOptions, LatLngBounds latLngBounds, LatLng zoneLatLng, AreaZoneDataZones areaZoneDataZones);

        void setCurrentLocation(LatLng latLng);

    }

    /**
     * <h1>PreferedZonePresenter</h1>
     */
    interface PreferedZoneSelectPresenter  {


        /**
         * <h1>setActionBar</h1>
         * <p>ActionBar initialize</p>
         */
        void setActionBar();

        /**
         * <h1>setActionBarTitle</h1>
         * <p>for set the title of the Activity</p>
         */
        void setActionBarTitle();

        /**
         * <h1>checkData</h1>
         */
        void checkData(Bundle mBundle);

        /**
         * <h1>checkCurrentLocation</h1>
         * <p>check the current location</p>
         */
        void checkCurrentLocation();


    }


}
