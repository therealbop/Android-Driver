package com.karry.side_screens.invite;



public interface InviteFragmentContract  {

    interface InviteFragmentView
    {

    }

    interface InviteFragPresenter{

        /**
         * <h>Attach CallbakView</h>
         * <p>this method is using to attach the view callback object to presenter</p>
         * @param view reference to View
         */
        void attachView(Object view);

        /**
         * <h>Detach View</h>
         * <p>this method is using to detach the object what we gave in atttach view to avoid possible memory leak</p>
         */
        void detachView();

        /**
         * <h>Get invite code</h>
         * <p></p>
         * @return
         */
        String  getInviteCode();



    }
}
