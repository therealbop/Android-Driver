package com.karry.authentication.signup.SignUpDocument;

import android.app.Activity;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by embed on 20/1/18.
 */

@Module
public abstract class SignUpDocumentModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(SignUpDocumentActivity signUpDocumentActivity);

    @ActivityScoped
    @Binds
    abstract SignUpDocumentContract.SignUpDocumentPresenter getPresenter(SignUpDocumentPresenter signUpDocumentPres);

    @ActivityScoped
    @Binds
    abstract SignUpDocumentContract.SignUpDocumentView getView(SignUpDocumentActivity signUpDocumentActivity);
}
