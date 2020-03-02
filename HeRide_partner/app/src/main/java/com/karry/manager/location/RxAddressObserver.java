package com.karry.manager.location;

import android.location.Address;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>RxAddressObserver</h1>
 * This class is used to subscribe the address from location
 * @author 3Embed
 * @since on 12-01-2018.
 */

public class RxAddressObserver  extends Observable<Address>
{
    private Observer<?super Address> observer;

    @Inject
    public RxAddressObserver() {

    }

    @Override
    protected void subscribeActual(Observer<? super Address> observer)
    {
        this.observer = observer;
    }

    /**
     * <h2>publishData</h2>
     * This method is used to publish the data for network
     * @param location data to be pushed
     */
    void publishAddress(Address location)
    {
        if(observer!=null)
        {
            observer.onNext(location);
            observer.onComplete();
        }
    }
}
