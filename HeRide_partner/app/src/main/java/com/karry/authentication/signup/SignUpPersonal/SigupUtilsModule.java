package com.karry.authentication.signup.SignUpPersonal;

import com.karry.dagger.ActivityScoped;

import dagger.Module;
import dagger.Provides;

/**
 * Created by embed on 19/1/18.
 */
@Module
public class SigupUtilsModule
{
    @Provides
    @ActivityScoped
    SignUpPersonalModel signUpPersonalCheck(){return new SignUpPersonalModel();}

}
