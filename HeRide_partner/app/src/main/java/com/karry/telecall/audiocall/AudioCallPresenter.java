package com.karry.telecall.audiocall;

import android.content.Context;

import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.telecall.callingapi.CallingService;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class AudioCallPresenter implements AudioCallContract.Presenter {

    private String TAG = AudioCallPresenter.class.getSimpleName();

    @Inject AudioCallContract.View view;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    CallingService callingService;
    @Inject
    Context mContext;


    private CompositeDisposable compositeDisposable;

    @Inject
    AudioCallPresenter(){
        compositeDisposable = new CompositeDisposable();
    }


    @Override
    public void initCall() {

    }

    @Override
    public void endCall(String callID, String callFrom) {

        Observable<Response<ResponseBody>> request =
                callingService.endCall(preferenceHelperDataSource.getAuthTokenCall(),
                        preferenceHelperDataSource.getLanguageSettings().getCode(),callID,callFrom);
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        try {

                           /* String response = value.body().string();
                            Log.d(TAG, "onNext: endCallResponse: "+response);*/
                            if(view != null)
                                view.onRejectSuccess();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void dispose() {
        compositeDisposable.clear();
    }


}
