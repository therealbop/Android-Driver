package com.karry.side_screens.wallet.changeCard;

import com.karry.dagger.ActivityScoped;

import dagger.Module;
import dagger.Provides;

/**
 * <h1>ChangeCardUtilModule</h1>
 * provide to dagger
 *  @author 3Embed
 * @since on 2/12/2018.
 */
@Module
public class ChangeCardUtilModule
{
    @Provides
    @ActivityScoped
    CardsListAdapter provideCardsListAdapter(ChangeCardActivity changeCardActivity){
        return new CardsListAdapter(changeCardActivity);
    }
}
