package com.karry.side_screens.help_center.zendeskHelpIndex;





import com.karry.dagger.ActivityScoped;
import com.karry.side_screens.help_center.zendeskadapter.HelpIndexAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * <h>ZendeskAdapterModule</h>
 * Created by Ali on 2/26/2018.
 */

@Module
public class ZendeskAdapterModule
{
    @ActivityScoped
    @Provides
    HelpIndexAdapter provideHelpIndexAdapter()
    {
        return  new HelpIndexAdapter();
    }
}
