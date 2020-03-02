package com.karry.authentication.login;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by murashid on 30-Jan-18.
 */
@Module
public abstract class LoginModule {

    @Binds
    @ActivityScoped
    abstract LoginContract.Presenter provideLoginPresenter(LoginPresenter loginPresenter);

    @Binds
    @ActivityScoped
    abstract LoginContract.View provideLoginView(LoginActivity loginActivity);


}
