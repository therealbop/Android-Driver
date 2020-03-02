package com.karry.authentication.signup.SignUpDocument;

import android.content.Context;

import com.karry.adapter.ImageUploadRVA;
import com.karry.dagger.ActivityScoped;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;



@Module
public class ImageAdapterModule {

    @Provides
    @ActivityScoped
    @Named ("imageUploadRVA")//this is for multiple object creation.
    ImageUploadRVA imageUploadRVA(Context context)
    {
        return new ImageUploadRVA(context);
    }

}
