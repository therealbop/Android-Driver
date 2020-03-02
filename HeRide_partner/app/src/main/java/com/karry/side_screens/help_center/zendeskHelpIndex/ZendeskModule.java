package com.karry.side_screens.help_center.zendeskHelpIndex;




import com.karry.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h>ZendeskModule</h>
 * Created by Ali on 2/26/2018.
 */
@Module
public interface ZendeskModule
{
    @ActivityScoped
    @Binds
    ZendeskHelpIndexContract.Presenter providePresenter(ZendeskHelpIndexImpl zendeskHelpIndex);

    @ActivityScoped
    @Binds
    ZendeskHelpIndexContract.ZendeskView provideView(ZendeskHelpIndex zendeskHelpIndex);
}
