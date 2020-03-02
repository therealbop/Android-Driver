package com.karry.mqttChat;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h>ChatDataObervable</h>
 * Created by Ali on 12/22/2017.
 */

public class ChatDataObervable extends Observable<ChatData>
{
    private static ChatDataObervable chatDataObervable;

    Observer<?super ChatData> observer;

    public static ChatDataObervable getInstance()
    {
        if(chatDataObervable==null)
        {
            chatDataObervable  = new ChatDataObervable();
            return chatDataObervable;
        }
        else
        {
            return chatDataObervable;
        }
    }
    @Override
    protected void subscribeActual(Observer<? super ChatData> observer)
    {
        this.observer = observer;
    }
    public void emitData(ChatData cData)
    {
        observer.onNext(cData);
        observer.onComplete();
    }

}
