package com.karry.manager.location;

import android.annotation.SuppressLint;
import android.location.Location;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.observables.ConnectableObservable;

/**
 * <h1>RxLocationObserver</h1>
 * This class is created to observe the location change of the user
 * @author 3Embed
 * @since on 11-01-2018.
 */
public class RxLocationObserver
{
    private static final String TAG = "RxLocationObserver";
    private Observer<?super Location> observer;

    private static RxLocationObserver utilityVideoCall;
    private String activeCallId, activeCallerId;


    public static RxLocationObserver getInstance()
    {
        if(utilityVideoCall == null)
        {
            utilityVideoCall = new RxLocationObserver();
            return utilityVideoCall;
        }else
            return utilityVideoCall;
    }
   /* @Override
    protected void subscribeActual(Observer<? super Location> observer)
    {
        this.observer = observer;
    }

    *//**
     * <h2>publishData</h2>
     * This method is used to publish the data for network
     * @param location data to be pushed
     *//*
    void publishData(Location location)
    {
        if(observer!=null)
        {
            observer.onNext(location);
            observer.onComplete();
        }
    }*/

  private ConnectableObservable<Location> connectableObservable;
  private ObservableEmitter<Location> emitor;

  @SuppressLint("CheckResult")
  public RxLocationObserver()
  {
    Observable<Location> observable = Observable.create(e -> emitor=e);
    connectableObservable = observable.publish();
    connectableObservable.share();
    connectableObservable.replay();
    connectableObservable.connect();
  }

  public ConnectableObservable<Location> getObservable() {
    return connectableObservable;
  }

  public void publishData(Location location)
  {
    if (emitor != null) {
      emitor.onNext(location);
    }
  }
}
