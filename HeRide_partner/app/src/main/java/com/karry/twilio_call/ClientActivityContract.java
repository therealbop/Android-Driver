package com.karry.twilio_call;

public interface ClientActivityContract
{
    interface ClientView
    {
        /**
         * <h>Create Device</h>
         * <p>this method is using to Create the Device Using the capability token</p>
         * @param capabilityToken using to  create device
         */
        void createDevice(String capabilityToken);

        /**
         * <h>showToast</h>
         * <p>this method is using to show toast </p>
         */
        void showToast();
    }

    interface ClientPresenter
    {
        /**
         * <h>Capabiblity Token Getter</h>
         * <p>this method is using to get Capability token</p>
         * @param phoneNumber phone number to be called
         */
        void getCapabilityToken(String phoneNumber);

        /**
         * <h2>dispose</h2>
         * used to dispose
         */
        void dispose();
    }

}
