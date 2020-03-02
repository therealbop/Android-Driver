package com.karry.utility;

import android.annotation.SuppressLint;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;

public class RxMultipleSubscription
{
    private ConnectableObservable<String> connectableObservable;
    private ObservableEmitter<String> emitor;

    @SuppressLint("CheckResult")
    public RxMultipleSubscription()
    {
        Observable<String> observable = Observable.create(e -> emitor=e);
        connectableObservable = observable.publish();
        connectableObservable.share();
        connectableObservable.replay();
        connectableObservable.connect();
    }

    public ConnectableObservable<String> getObservable() {
        return connectableObservable;
    }

    public void postData(String flag)
    {
      if (emitor != null) {
        emitor.onNext(flag);
      }
    }
}

