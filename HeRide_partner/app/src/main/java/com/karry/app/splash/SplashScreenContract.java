package com.karry.app.splash;


/**
 * <h1>SplashScreenContract</h1>
 * <p>the interface for the Splash Activity View and Presenetr</p>
 */
class SplashScreenContract {

    /**
     * <h1>SplashScreenView</h1>
     * <p>presenter informing to view through the interface</p>
     */
    interface SplashScreenView{

        /**
         * <h1>startMainActivity</h1>
         * <p>for start the Main Activity if already Login</p>
         */
        void startMainActivity();

        /**
         * <h1>startLoginActivity</h1>
         * <p>for start the Login Activity if not login</p>
         */
        void startLoginActivity();

        /**
         * <h2>networkNotAvailable</h2>
         * This method is triggered when network is not available
         */
        void networkNotAvailable();

        /**
         * <h2>networkAvailable</h2>
         * This method is triggered when network is available
         */
        void networkAvailable();

    }

    /**
     * <h1>SplashScreenPresenter</h1>
     * <p>view will inform the presenter through this interface</p>
     */
    interface SplashScreenPresenter{

        /**
         * <h2>checkConfiguration</h2>
         * <p>check user is logged in or not and
         * based on status it corresponding activity will open</p>
         */
        void checkConfiguration();

        /**
         * <h2>subscribeNetworkObserver</h2>
         * This method is used to check network availability
         */
        void subscribeNetworkObserver();

        /**
         * <h1>clear</h1>
         * <p>compositeDisposable clear</p>
         */
        void clear();


    }
}
