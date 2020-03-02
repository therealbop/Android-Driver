package com.karry.utility.path_plot;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>RxRoutePathObserver</h1>
 * This class is used to observe the path to be plotted
 * @author 3Embed
 * @since on 03-01-2018.
 */
public class RxRoutePathObserver extends Observable<LatLongBounds>
{
    private Observer<?super LatLongBounds> observer;

    @Override
    protected void subscribeActual(Observer<? super LatLongBounds> observer)
    {
        this.observer = observer;
    }

    /**
     * <h2>publishData</h2>
     * This method is used to publish the data for network
     * @param data data to be pushed
     */
    void notifyRoutePath(LatLongBounds data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}