package com.karry.telecall.audiocall;

public interface AudioCallContract {
    interface Presenter{
        void initCall();
        void endCall(String callID, String callFrom);
        void dropView();
        void dispose();
    }

    interface View{
        void onAnswerSuccess();
        void onRejectSuccess();
    }
}
