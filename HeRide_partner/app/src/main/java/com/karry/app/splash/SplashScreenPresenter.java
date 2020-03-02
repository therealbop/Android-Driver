package com.karry.app.splash;

import android.os.Handler;
import com.google.firebase.iid.FirebaseInstanceId;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkStateHolder;
import com.karry.network.RxNetworkObserver;
import com.karry.utility.Utility;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.karry.utility.VariableConstant.BOOKING_FLOW_OPEN;

/**
 * <h1>SplashScreenPresenter</h1>
 * <p>the implementations of the Splash Screen is doing in this presenter</p>
 */
public class SplashScreenPresenter implements SplashScreenContract.SplashScreenPresenter {

    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject SplashScreenContract.SplashScreenView splashScreenView;
    @Inject RxNetworkObserver rxNetworkObserver;
    private Disposable networkDisposable;
    @Inject CompositeDisposable compositeDisposable;
    @Inject NetworkStateHolder networkStateHolder;
    private boolean nectActivityOpened = false;

    @Inject
    SplashScreenPresenter() {
    }

    @Override
    public void checkConfiguration() {
        BOOKING_FLOW_OPEN = true;
        preferenceHelperDataSource.setFCMRegistrationId(FirebaseInstanceId.getInstance().getToken());
        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is over
            // Start app
            if(preferenceHelperDataSource.getSessionToken()!=null &&
                    preferenceHelperDataSource.getIsDriverLoggedIn() &&
                    !preferenceHelperDataSource.getSessionToken().isEmpty())
                splashScreenView.startMainActivity();
            else
                splashScreenView.startLoginActivity();
        }, 3000);
    }

    @Override
    public void subscribeNetworkObserver()
    {
     /*   if(networkStateHolder.isConnected()) {
            nectActivityOpened = true;
            Utility.printLog(" network not available....1 "+networkStateHolder.isConnected());
            splashScreenView.networkAvailable();
        }
        else {
            Utility.printLog(" network not available....2 "+networkStateHolder.isConnected());
            nectActivityOpened = false;
      //      new Handler().postDelayed(() -> splashScreenView.networkNotAvailable(), 4000);
            splashScreenView.networkNotAvailable();
        }*/


        rxNetworkObserver.subscribeOn(Schedulers.io());
        rxNetworkObserver.observeOn(AndroidSchedulers.mainThread());
        networkDisposable = rxNetworkObserver.subscribeWith(new DisposableObserver<NetworkStateHolder>() {
            @Override
            public void onNext(NetworkStateHolder networkStateHolder) {
                if(networkStateHolder.isConnected()) {

//                    if(!nectActivityOpened && networkStateHolder.isConnected()) {
                    Utility.printLog(" network not available....3 "+networkStateHolder.isConnected());
//                    nectActivityOpened = true;
                    splashScreenView.networkAvailable();
                }

                else if( !networkStateHolder.isConnected()){
//                else if(nectActivityOpened && !networkStateHolder.isConnected()){
                    Utility.printLog(" network not available....4 "+networkStateHolder.isConnected());
//                    nectActivityOpened = false;
                    splashScreenView.networkNotAvailable();
                }
            }

            @Override
            public void onError(Throwable e) {
                Utility.printLog(" network not available :onerror ");

            }

            @Override
            public void onComplete() {
                compositeDisposable.add(networkDisposable);
            }
        });
    }

    @Override
    public void clear() {
        compositeDisposable.clear();
    }
}
