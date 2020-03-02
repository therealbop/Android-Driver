package com.karry.authentication.forgotpassword;



import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by murashid on 01-Feb-18.
 */

@Module
public abstract class OTPVerificationModule {

    @Binds
    @ActivityScoped
    abstract OTPVerificationContract.Presenter provideOTPVerificationPresenter(OTPVerificationPresenter forgotPasswordPresenter);

    @Binds
    @ActivityScoped
    abstract OTPVerificationContract.View provideOTPVerificationView(OTPVerificationActivity otpVerificationActivity);

}
