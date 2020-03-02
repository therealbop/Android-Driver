package com.karry.side_screens.support.supportSubCategory;


/**
 * <h1>SupportSubCatContract</h1>
 * <p>interface for Support Sub Category Activity</p>
 */
public interface SupportSubCatContract {

    /**
     * <h1>SupportSubCatView</h1>
     * <p>interface for the Support Sub Category View</p>
     */
    interface SupportSubCatView{

        void initActionBar();

        void setTitle();

    }

    /**
     * <h1>SupportSubCatPresenter</h1>
     * <p>interface fotr the Support Sub BookingPopUpPresenter</p>
     */
    interface SupportSubCatPresenter{

        void setActionBar();

        void setActionBarTitle();

    }

}
