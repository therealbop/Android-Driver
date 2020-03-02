package com.karry.side_screens.prefered_zone;


import com.karry.BaseView;
import com.karry.pojo.AreaZone.AreaZone;
import com.karry.pojo.AreaZone.AreaZoneDataZones;

/**
 * <h1>PreferedZoneListContract</h1>
 */
interface PreferedZoneListContract {

    /**
     * <h1>PreferedZoneView</h1>
     */
    interface PreferedZoneView extends BaseView {


        /**
         * <h1>initActionBar</h1>
         * <p>method for initialize the ActionBar</p>
         */
        void initActionBar();

        /**
         * <h1>setTitle</h1>
         * <p>method for set the title of ActionBar</p>
         */
        void setTitle();

        /**
         * <h1>goToLogin</h1>
         * <p>if the Unauthorized error is coming in Api need to go Login Screen.</p>
         */
        void goToLogin(String errMsg);


        /**
         * <h1>zoneListForAdapter</h1>
         * @param areaZone : pojo class
         */
        void zoneListForAdapter(AreaZone areaZone);


        void areaZoneRefresh(AreaZone areaZone);

        /**
         * <h1>preferenceZoneSeletionSuccess</h1>
         * <p>if the selection success</p>
         */
        void preferenceZoneSeletionSuccess();

        /**
         * <h1>zoneSelected</h1>
         * <p>if the zone is selected</p>
         */
        void zoneSelected();

        /**
         * <h1>emptyZoneSelected</h1>
         * <p>if the zone is not selected</p>
         */
        void emptyZoneSelected();

        void enableReset();

        /**
         * <h1>disableReset</h1>
         * <p>the reset button </p>
         */
        void disableReset();
    }

    /**
     * <h1>PreferedZonePresenter</h1>
     */
    interface PreferedZonePresenter  {


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
         * <h1>getAreaZone</h1>
         */
        void getAreaZone();

        void checkPreferenceCheck(AreaZoneDataZones areaZoneDataZones, int position);

        /**
         * <h1>patchAreaZone</h1>
         * @param preferenceType :1 and n are the number of booking.
         */
        void patchAreaZone(String preferenceType);

        /**
         * <h1>validatePreferenceZone</h1>
         * <p>check the whether the zone selected or not</p>
         */
        void validatePreferenceZone();


        /**
         * <h1>deleteMasterPreferedareazone</h1>
         * <p>if the prefered zone need remove</p>
         */
        void deleteMasterPreferedareazone();





    }


}
