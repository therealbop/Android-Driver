package com.karry.telecall.incommingcalls;

import android.app.Activity;


/**
 * Created by Ali on 10/29/2018.
 */
public interface IncomingCallContract
{
    interface Presenter /*extends BasePresenter*/
    {

        void ansDecline(String call_id, int i);

        void getCallDetails();

        void setWindow(Activity mActivity);

        void checkIsCallerStillWaiting(String callId);

        void dispose();

        void endCall(String call_id, String request);

        void answerCall(String call_id);
    }
    interface View /*extends BaseView*/
    {

        void onSuccessAns();

        void onSuccessDec();
    }
}
