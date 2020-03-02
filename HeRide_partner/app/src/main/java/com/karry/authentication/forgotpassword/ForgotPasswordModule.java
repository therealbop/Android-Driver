package com.karry.authentication.forgotpassword;


import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by murashid on 01-Feb-18.
 */
@Module
abstract public class ForgotPasswordModule {

    @Binds
    @ActivityScoped
    abstract ForgotPasswordContract.Presenter provideForgotPasswordPresenter(ForgotPasswordPresenter forgotPasswordPresenter);

    @Binds
    @ActivityScoped
    abstract ForgotPasswordContract.View providerForgotPasswordView(ForgotPasswordActivity forgotPasswordActivity);

}
