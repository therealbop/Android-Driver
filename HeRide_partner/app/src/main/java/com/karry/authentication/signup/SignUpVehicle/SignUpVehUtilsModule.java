package com.karry.authentication.signup.SignUpVehicle;

import android.content.Context;

import com.karry.adapter.PreferenceListAdapter;
import com.karry.dagger.ActivityScoped;
import com.karry.utility.AppTypeFace;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by embed on 19/1/18.
 */

@Module
public class SignUpVehUtilsModule {
    @Provides
    @ActivityScoped
    SignupVehModel signupVehModelCheck(){return  new SignupVehModel();}

    @Provides
    @ActivityScoped
    @Named("PreferenceListAdapter")//this is for multiple object creation.
    PreferenceListAdapter preferenceListAdapter(Context context, AppTypeFace appTypeFace)
    {
        return new PreferenceListAdapter(context,appTypeFace);
    }
}
