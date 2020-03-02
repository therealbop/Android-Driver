package com.karry.network;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h2>RxNetworkObserver</h2>
 * <P>
 *  Its the network observer class to observe the network
 *  changes in the App.
 * </P>
 * @version 1.0.
 * @author 3Embed.*/
public class RxNetworkObserver extends Observable<NetworkStateHolder>
{
    private Observer<?super NetworkStateHolder> observer;

    @Override
    protected void subscribeActual(Observer<? super NetworkStateHolder> observer)
    {
        this.observer=observer;
    }

    /**
     * <h2>publishData</h2>
     * This method is used to publish the data for network
     * @param data data to be pushed
     */
    public void publishData(NetworkStateHolder data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}


