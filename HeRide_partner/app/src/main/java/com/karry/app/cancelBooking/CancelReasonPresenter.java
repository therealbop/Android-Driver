package com.karry.app.cancelBooking;


import android.content.Context;

import com.google.gson.Gson;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkService;
import com.karry.network.NetworkStateHolder;
import com.karry.network.RxNetworkObserver;
import com.karry.pojo.CancelPojo;
import com.karry.utility.DataParser;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karry.utility.VariableConstant.RESPONSE_UNAUTHORIZED;

/**
 * <h1>CancelReasonPresenter</h1>
 * <p>the Implementation of the Cancel Booking </p>
 */
class CancelReasonPresenter implements CancelReasonContract.CancelReasonPresenter {

    @Inject CancelReasonContract.CancelReasonView cancelReasonView;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferencesHelper;
    @Inject Context context;

    private boolean networkAvailable = true;
    @Inject CompositeDisposable compositeDisposable;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject RxNetworkObserver rxNetworkObserver;


    @Inject
    public CancelReasonPresenter() {
    }

    @Override
    public void setActionBar() {
        cancelReasonView.initActionBar();
    }


    @Override
    public void setActionBarTitle() {
        cancelReasonView.setTitle();
    }

    @Override
    public void hideKeyboardAndClearFocus() {
        cancelReasonView.hideSoftKeyboard();
    }

    @Override
    public void showKeyboard() {
        cancelReasonView.showSoftKeyboard();
    }

    @Override
    public void getCancelationReason() {
        cancelReasonView.showProgress();

        Observable<Response<ResponseBody>> cancellationReason = networkService.getCancellationReasonRide
                (preferencesHelper.getSessionToken(),
                        preferencesHelper.getLanguage());

        cancellationReason.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            switch (value.code()) {
                                case VariableConstant.RESPONSE_CODE_SUCCESS:
                                    String resp=value.body().string();
                                    Utility.printLog("getCancellationReasonRide success response is  : \n" +resp);
                                    CancelPojo cancelPojo = new Gson().fromJson(resp, CancelPojo.class);
                                    cancelReasonView.cancellationReasonSuccess(cancelPojo.getData());
                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    cancelReasonView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    String err=value.errorBody().string();
                                    Utility.printLog("getCancellationReasonRide error response is  : \n" +err);
                                    cancelReasonView.apiFailure(DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        cancelReasonView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        cancelReasonView.hideProgress();
                    }
                });

    }

    @Override
    public void cancelBooking(String reason) {

        cancelReasonView.showProgress();

        Observable<Response<ResponseBody>> bookingCancel = networkService.bookingCancel
                (preferencesHelper.getSessionToken(),
                        preferencesHelper.getLanguage(),
                        preferencesHelper.getBookingId(),
                        reason);

        bookingCancel.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            switch (value.code()) {
                                case VariableConstant.RESPONSE_CODE_SUCCESS:
                                    String resp=value.body().string();
                                    Utility.printLog("cancelBooking success response is  : \n" +resp);
                                    cancelReasonView.cancelSuccess();

                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    cancelReasonView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    String err=value.errorBody().string();
                                    Utility.printLog("cancelBooking error response is  : \n" +err);
                                    cancelReasonView.apiFailure(DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        cancelReasonView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        cancelReasonView.hideProgress();
                    }
                });
    }


    @Override
    public void subscribeNetworkObserver()
    {
        Observer<NetworkStateHolder> observer = new Observer<NetworkStateHolder>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
                compositeDisposable.add(d);
            }
            @Override
            public void onNext(NetworkStateHolder value)
            {
                Utility.printLog(""+" network not avalable "+value.isConnected());
                networkAvailable = networkStateHolder.isConnected();
                try {
                    if(value.isConnected())
                        cancelReasonView.networkAvailable();
                    else
                        cancelReasonView.networkNotAvailable();
                }catch (Exception e){
                    Utility.printLog(""+ Arrays.toString(e.getStackTrace()));
                }

            }
            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
            }
            @Override
            public void onComplete()
            {}
        };
        rxNetworkObserver.subscribeOn(Schedulers.io());
        rxNetworkObserver.observeOn(AndroidSchedulers.mainThread());
        rxNetworkObserver.subscribe(observer);
    }

    @Override
    public void networkCheckOnresume() {
        if(networkAvailable)
            cancelReasonView.networkAvailable();
        else
            cancelReasonView.networkNotAvailable();
    }
}
