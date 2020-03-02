package com.karry.manager.booking;

import com.karry.bookingFlow.model.RideBookingDropUpdate;

import io.reactivex.Observable;
import io.reactivex.Observer;



public class RxDropLocationUpdateObserver  extends Observable<RideBookingDropUpdate> {

    private Observer<?super RideBookingDropUpdate> observer;

    @Override
    protected void subscribeActual(Observer<? super RideBookingDropUpdate> observer) {

        this.observer = observer;
    }

    /**
     * <h2>publishDriverDetails</h2>
     * This method is used to publish the driver details
     * @param data driver details to be pushed
     */
    void publishDropUpdate(RideBookingDropUpdate data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}
