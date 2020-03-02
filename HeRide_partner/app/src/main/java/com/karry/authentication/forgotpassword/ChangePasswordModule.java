package com.karry.authentication.forgotpassword;


import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class ChangePasswordModule {

    @Binds
    @ActivityScoped
    abstract ChangePasswordContract.Presenter provideChangePasswordPresenter(ChangePasswordPresenter changePasswordPresenter);

    @Binds
    @ActivityScoped
    abstract ChangePasswordContract.View provideChangePasswordView(ChangePasswordActivity changePasswordActivity);
}
