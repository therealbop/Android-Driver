package com.karry.telecall.utility;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by Ali on 1/10/2019.
 */
public class RxCallInfo extends Observable<String> {

    private static RxCallInfo rxInfo;

    private Observer<? super String> observer;

    public static RxCallInfo getInstance()
    {
        if(rxInfo == null)
        {
            rxInfo = new RxCallInfo();
            return rxInfo;
        }
        else
            return rxInfo;
    }

    @Override
    protected void subscribeActual(Observer<? super String> observer) {
        this.observer = observer;
    }

    public void emitData(String jsonObject)
    {
        Log.d("TAG", "emitData: "+jsonObject);
        observer.onNext(jsonObject);
        observer.onComplete();
    }
}
