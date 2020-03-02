package com.karry.app.mainActivity;

import androidx.fragment.app.FragmentManager;

import com.karry.authentication.login.model.LanguagesList;
import com.karry.dagger.ActivityScoped;
import com.karry.network.NetworkErrorDialog;
import com.karry.utility.ActivityUtils;
import com.karry.utility.AppTypeFace;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.karry.utility.VariableConstant.LOGIN;

/**
 * Created by embed on 5/2/18.
 */

@Module
public class MainActivityUtilModule {


    @Provides
    @ActivityScoped
    ActivityUtils provideActivityUtils(FragmentManager fragmentManager)
    {
        return new ActivityUtils(fragmentManager);
    }

    @Provides
    @ActivityScoped
    NetworkErrorDialog networkErrorDialog(AppTypeFace appTypeFace,MainActivity activity){
        return new NetworkErrorDialog(activity,appTypeFace);
    }

    @Provides
    @Named(LOGIN)
    @ActivityScoped
    ArrayList<LanguagesList> languagesLists(){return new ArrayList<>();}
}
