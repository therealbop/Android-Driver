package com.karry.manager.booking;


import com.karry.pojo.QueuePosition;

import io.reactivex.Observable;
import io.reactivex.Observer;


public class RxDriverQueuePosition extends Observable<QueuePosition>
{
    private Observer<?super QueuePosition> observer;

    @Override
    protected void subscribeActual(Observer<? super QueuePosition> observer) {
        this.observer = observer;
    }

    /**
     * <h2>publishDriverCancelDetails</h2>
     * This method is used to publish the driver details
     * @param data driver details to be pushed
     */
    void publishDriverQueuePositon(QueuePosition data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}
