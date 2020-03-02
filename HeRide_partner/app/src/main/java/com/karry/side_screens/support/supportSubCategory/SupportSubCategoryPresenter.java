package com.karry.side_screens.support.supportSubCategory;


import javax.inject.Inject;

public class SupportSubCategoryPresenter
        implements SupportSubCatContract.SupportSubCatPresenter {

    @Inject SupportSubCatContract.SupportSubCatView supportSubCatView;

    @Inject
    SupportSubCategoryPresenter() {
    }

    /**********************************************************************************************/
    @Override
    public void setActionBar() {
        supportSubCatView.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        supportSubCatView.setTitle();
    }
}
