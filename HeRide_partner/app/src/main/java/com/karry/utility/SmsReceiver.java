package com.karry.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by DELL on 28-10-2017.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListner mListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.

            String messageBody = smsMessage.getMessageBody();

            Log.d("Message"," messageBody:"+messageBody+" Sender:"+sender);
            //Pass on the text to our listener.
            if(mListener!=null)
                mListener.messageReceived(messageBody);
        }

    }

    public static void bindListener(SmsListner listener) {
        mListener = listener;
    }

}
