package com.karry.side_screens.profile;

import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by murashid on 30-Jan-18.
 */
@Module
public abstract class EditProfileModule {

    @Binds
    @ActivityScoped
    abstract EditProfileContract.Presenter providePresenter(EditProfilePresenter presenter);

    @Binds
    @ActivityScoped
    abstract EditProfileContract.View provideView(EditProfileActivity activity);

}
