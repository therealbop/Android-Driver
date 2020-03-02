package com.karry.dagger;

import android.content.Context;

import com.google.gson.Gson;
import com.karry.countrypic.countrypic.CountryPicker;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.manager.booking.BookingManager;
import com.karry.manager.booking.RxDriverCancelledObserver;
import com.karry.manager.booking.RxDriverQueuePosition;
import com.karry.manager.booking.RxDropLocationUpdateObserver;
import com.karry.manager.location.RxAddressObserver;
import com.karry.manager.location.RxLocationObserver;
import com.karry.manager.mqtt_manager.MQTTManager;
import com.karry.mqttChat.ChatDataObervable;
import com.karry.network.NetworkStateHolder;
import com.karry.network.RxNetworkObserver;
import com.karry.network.NetworkService;
import com.heride.partner.BuildConfig;
import com.karry.utility.AcknowledgeHelper;
import com.karry.utility.AppPermissionsRunTime;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;
import com.karry.utility.Upload_file_AmazonS3;
import com.karry.utility.VariableConstant;
import com.karry.side_screens.wallet.adapter.Alerts;
import com.karry.utility.path_plot.RxRoutePathObserver;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by embed on 18/1/18.
 */

@Module
class UtilityModule {

    private static final String AMAZON_POOL_ID = "AMAZON_POOL_ID";



    @Provides
    @Singleton
    CountryPicker provideCountryPickerInstance() {return CountryPicker.newInstance("");}

    @Provides
    @Singleton
    AppTypeFace provideAppTypeFace(Context context) { return new AppTypeFace(context);}

    @Provides
    @Singleton
    DialogHelper provideDialogHelper() { return new DialogHelper();}

    @Provides
    @Singleton
    CompositeDisposable compositeDisposable(){return  new CompositeDisposable();}

    @Provides
    @Singleton
    Upload_file_AmazonS3 provideAmazon(Context context)
    {
        return  new Upload_file_AmazonS3(context);
    }


    /*@Provides
    @Singleton
    ImageEditUpload imageEditUpload(Context context){return  new ImageEditUpload(context);}*/

    /********************************* Location Provider ***************************************/
    @Provides
    @Singleton
    RxLocationObserver provideRxLocationObserver(){
        return RxLocationObserver.getInstance();
    }

    /**********************************Address Provider **********************************************/

    @Provides
    @Singleton
    RxNetworkObserver provideRxNetworkObserver(){
        return new RxNetworkObserver();
    }


    @Provides
    @Singleton
    Gson provideGson()
    {
        return new Gson();
    }

    @Provides
    AcknowledgeHelper getAcknowledgeHelper(PreferenceHelperDataSource dataSource,
                                           NetworkService dispatcherService){
        return new AcknowledgeHelper(dataSource,dispatcherService);
    }

    @Provides
    @Singleton
    RxDriverCancelledObserver rxDriverCancelledObserver(){
        return new RxDriverCancelledObserver();
    }

    @Provides
    @Singleton
    RxDropLocationUpdateObserver rxDropLocationUpdateObserver(){
        return new RxDropLocationUpdateObserver();
    }

    @Provides
    @Singleton
    RxDriverQueuePosition rxDriverQueuePositionObserver(){
        return new RxDriverQueuePosition();
    }

    @Provides
    @Singleton
    ChatDataObervable chatDataObervable(){
        return new ChatDataObervable();
    }

    @Provides
    @Singleton
    BookingManager provideBookingManager(Gson gson, RxDriverCancelledObserver rxDriverCancelledObserver,
                                         RxDropLocationUpdateObserver rxDropLocationUpdateObserver,
                                         ChatDataObervable chatDataObervable,
                                         RxDriverQueuePosition rxDriverQueuePosition){
        return new BookingManager(gson, rxDriverCancelledObserver,rxDropLocationUpdateObserver,
                chatDataObervable,rxDriverQueuePosition);
    }

    @Provides
    @Singleton
    NetworkStateHolder networkStateHolder(){
        return  new NetworkStateHolder();
    }

    @Provides
    @Singleton
    MQTTManager mqttManager(Context context, AcknowledgeHelper acknowledgeHelper,
                            PreferenceHelperDataSource helperDataSource, BookingManager bookingManager,
                            NetworkStateHolder networkStateHolder,RxNetworkObserver rxNetworkObserver)
    {
        return new MQTTManager(context,acknowledgeHelper,helperDataSource,bookingManager,networkStateHolder,rxNetworkObserver);
    }

    /**********************************************************************************************/
    @Provides
    @Singleton
    RxAddressObserver provideRxAddressObserver(){
        return new RxAddressObserver();
    }

    @Provides
    @Singleton
    Alerts provideAlerts(Context context, AppTypeFace appTypeface){return new Alerts(context,appTypeface);}

    @Provides
    @Singleton
    RxRoutePathObserver provideRxRoutePathObserver()
    {
        return new RxRoutePathObserver();
    }

    @Provides
    @Singleton
    AppPermissionsRunTime appPermissionsRunTime(Alerts alerts)
    {
        return new AppPermissionsRunTime(alerts);
    };

}
