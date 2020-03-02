package com.karry.side_screens.history.history_invoice;




import com.karry.dagger.ActivityScoped;
import com.karry.utility.AppTypeFace;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class HelpAdapterModule {

    @Provides
    @ActivityScoped
    ArrayList<HelpDataModel> helpDataModelArrayList()
    {
        return new ArrayList<>();
    }

    @Provides
    @ActivityScoped
    HistoryDetailsHelpAdapter historyDetailsHelpAdapter(HistoryInvoiceActivity context, AppTypeFace appTypeface,
                                                        ArrayList<HelpDataModel> helpDataModels)
    {
        return new HistoryDetailsHelpAdapter(context,appTypeface,helpDataModels);
    }
}
