package com.karry.side_screens.portal;


import com.karry.BaseView;

/**
 * <h1>PortalContract</h1>
 * <p>interface for the Portal View and Presenter</p>
 */
public interface PortalContract  {

    /**
     * <h1>PortalView</h1>
     * <p>interface for the view informing the UI changes</p>
     */
    interface PortalView extends BaseView{


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
         * <h1>loadPortalWeb</h1>
         * <p>portal url load in webview</p>
         * @param url : portal url
         */
        void loadPortalWeb(String url);

    }

    interface PortalPresenter{

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
         * <h1>setPortalURL</h1>
         * <p>set the url of portal and pass to view for show</p>
         */
        void setPortalURL();

        /**
         * <h1>webPageLoaded</h1>
         * <p>check whether the web url loaded or not</p>
         * @param loaded : boolean value , if url loaded or not
         */
        void webPageLoaded(boolean loaded);

        /**
         * <h1>checklanguageLocale</h1>
         * <p>check the language stored and check the locale</p>
         */
        void checklanguageLocale();

    }


}
