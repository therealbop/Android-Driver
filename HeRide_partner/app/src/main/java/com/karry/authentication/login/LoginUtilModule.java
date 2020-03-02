package com.karry.authentication.login;


import com.karry.authentication.login.model.LanguagesList;
import com.karry.dagger.ActivityScoped;
import com.karry.network.NetworkErrorDialog;
import com.karry.utility.AppTypeFace;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.karry.utility.VariableConstant.LOGIN;

@Module
public class LoginUtilModule {

    @Provides
    @ActivityScoped
    NetworkErrorDialog networkErrorDialog(AppTypeFace appTypeFace, LoginActivity activity){
        return new NetworkErrorDialog(activity,appTypeFace);
    }

    @Provides
    @Named(LOGIN)
    @ActivityScoped
    ArrayList<LanguagesList> languagesLists(){return new ArrayList<>();}
}
