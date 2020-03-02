package com.karry.side_screens.support.supportSubCategory;


import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SupportSubCatDaggerModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(SupportSubCategoryActivity supportSubCategoryActivity);

    @ActivityScoped
    @Binds
    abstract SupportSubCatContract.SupportSubCatView getView(SupportSubCategoryActivity supportSubCategoryActivity);

    @ActivityScoped
    @Binds
    abstract SupportSubCatContract.SupportSubCatPresenter getPresenter(SupportSubCategoryPresenter supportSubCategoryPresenter);


}


