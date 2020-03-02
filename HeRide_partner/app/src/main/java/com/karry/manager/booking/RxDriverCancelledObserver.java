package com.karry.manager.booking;


import com.karry.bookingFlow.model.RideBookingCancel;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>RxDriverCancelledObserver</h1>
 * used to push the driver cancellation model
 * @author :
 * @since on 2/13/2018.
 */

public class RxDriverCancelledObserver extends Observable<RideBookingCancel>
{
    private Observer<?super RideBookingCancel> observer;

    @Override
    protected void subscribeActual(Observer<? super RideBookingCancel> observer) {
        this.observer = observer;
    }

    /**
     * <h2>publishDriverCancelDetails</h2>
     * This method is used to publish the driver details
     * @param data driver details to be pushed
     */
    void publishDriverCancelDetails(RideBookingCancel data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}
