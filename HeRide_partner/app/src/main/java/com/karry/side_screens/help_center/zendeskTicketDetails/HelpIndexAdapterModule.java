package com.karry.side_screens.help_center.zendeskTicketDetails;



import com.karry.dagger.ActivityScoped;
import com.karry.side_screens.help_center.zendeskadapter.HelpIndexRecyclerAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * <h>HelpIndexAdapterModule</h>
 * Created by Ali on 2/26/2018.
 */
@Module
public class HelpIndexAdapterModule
{
    @ActivityScoped
    @Provides
    HelpIndexRecyclerAdapter provideHelpAdapter()
    {
     return new HelpIndexRecyclerAdapter();
    }
}
