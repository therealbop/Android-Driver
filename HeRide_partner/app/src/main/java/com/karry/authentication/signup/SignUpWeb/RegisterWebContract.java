package com.karry.authentication.signup.SignUpWeb;


import com.karry.BaseView;

/**
 * <h1>RegisterWebContract</h1>
 * <p>the interface for the Web Activity View and presenter</p>
 */
public interface RegisterWebContract {

    /**
     * <h1>RegisterWebView</h1>
     * <p>the interface for thr Web View</p>
     */
    interface RegisterWebView extends BaseView{


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
         * <h1>enableActivityTxt</h1>
         * <p>for enable the next button for clickable</p>
         */
        void enableActivityTxt();

        /**
         * <h1>startNextActivity</h1>
         * <p>start the signUp Activity</p>
         */
        void startNextActivity();
    }

    /**
     * <h1>RegisterWebPresenter</h1>
     * <p>the interface for the Web Implementation</p>
     */
    interface RegisterWebPresenter{

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
         * <h1>webPageLoaded</h1>
         * <p>check whether the web url loaded or not</p>
         * @param loaded : boolean value , if url loaded or not
         */
        void webPageLoaded(boolean loaded);

        /**
         * <h1>success</h1>
         * <p>informing to view that start the SignUp</p>
         */
        void success();


        /**
         * <h1>checklanguageLocale</h1>
         * <p>check the language stored and check the locale</p>
         */
        void checklanguageLocale();
    }
}
