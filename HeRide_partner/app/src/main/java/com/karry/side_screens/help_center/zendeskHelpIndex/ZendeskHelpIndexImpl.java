package com.karry.side_screens.help_center.zendeskHelpIndex;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.data.source.sqlite.SQLiteDataSource;
import com.karry.manager.mqtt_manager.MQTTManager;
import com.karry.network.NetworkService;
import com.karry.side_screens.help_center.zendeskpojo.AllTicket;
import com.karry.side_screens.help_center.zendeskpojo.OpenClose;
import com.karry.side_screens.help_center.zendeskpojo.TicketClose;
import com.karry.side_screens.help_center.zendeskpojo.TicketOpen;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h>ZendeskHelpIndexImpl</h>
 * Created by Ali on 2/26/2018.
 */

public class ZendeskHelpIndexImpl implements ZendeskHelpIndexContract.Presenter
{

    /*http://45.77.190.140:9999/zendesk/user/akbar%40gmail.com*/

    @Inject ZendeskHelpIndexContract.ZendeskView zendeskView;
    @Inject
    NetworkService lspServices;
    @Inject
    Gson gson;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    SQLiteDataSource addressDataSource;
    @Inject
    MQTTManager mqttManager;
    @Inject
    NetworkService networkService;
    @Inject Context mActivity;

    @Inject
    public ZendeskHelpIndexImpl()
    {
        gson = new Gson();
    }

    @Override
    public void onToGetZendeskTicket()
    {
        zendeskView.showProgress();
        Observable<Response<ResponseBody>> observable = lspServices.onToGetZendeskTicket(preferenceHelperDataSource.getSessionToken(),
                preferenceHelperDataSource.getLanguage(),preferenceHelperDataSource.getDriverMail());

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        int code = responseBodyResponse.code();
                        String response;
                        //ErrorHandel errorHandel;
                        try{


                            switch (code)
                            {
                                case 200:
                                    response = Objects.requireNonNull(responseBodyResponse.body()).string();
                                    Log.d("TAG", "onNextTICKETSuccess: "+response);
                                    AllTicket allTicket = gson.fromJson(response,AllTicket.class);


                                    if(allTicket.getData().getClose().size()>0 || allTicket.getData().getOpen().size()>0)
                                    {

                                        zendeskView.hideChat();
                                        if(allTicket.getData().getOpen().size()>0)
                                        {
                                            for(int i = 0;i<allTicket.getData().getOpen().size();i++)
                                            {
                                                TicketOpen ticketOpen = allTicket.getData().getOpen().get(i);
                                                OpenClose openClose = new OpenClose(ticketOpen.getId(),ticketOpen.getTimeStamp()
                                                        ,ticketOpen.getStatus(),ticketOpen.getSubject(),ticketOpen.getType(),
                                                        ticketOpen.getPriority(),ticketOpen.getDescription());

                                                if(i==0)
                                                    openClose.setFirst(true);
                                                // openCloses.add(openClose);
                                                zendeskView.onTicketStatus(openClose,allTicket.getData().getOpen().size(),true);
                                            }
                                        }

                                        if(allTicket.getData().getClose().size()>0)
                                        {
                                            for(int i = 0;i<allTicket.getData().getClose().size();i++)
                                            {
                                                TicketClose ticketClose = allTicket.getData().getClose().get(i);
                                                OpenClose openClose = new OpenClose(ticketClose.getId(),ticketClose.getTimeStamp()
                                                        ,ticketClose.getStatus(),ticketClose.getSubject(),ticketClose.getType(),ticketClose.getPriority()
                                                        ,ticketClose.getDescription());
                                                if(i==0)
                                                {
                                                    openClose.setFirst(true);
                                                }
                                                // openCloses.add(openClose);
                                               // zendeskView.onTicketStatus(openClose, allTicket.getData().getClose().size());
                                                zendeskView.onTicketStatus(openClose,allTicket.getData().getClose().size(),false);
                                            }
                                        }

                                        zendeskView.onNotifyData();
                                        zendeskView.hideProgress();
                                    }else
                                    {
                                        zendeskView.onEmptyTicket();
                                        zendeskView.hideProgress();
                                    }
                                    break;
                                /*case 498:
                                    ExpireSession.refreshApplication(mActivity,mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;
                                case 440:
                                    ExpireSession.refreshApplication(mActivity,mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;*/
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        zendeskView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        zendeskView.hideProgress();
                    }
                });
    }




    public boolean isNetworkAvailable() {
        return false;
    }

    @Override
    public void attachView(Object view) {

    }

    @Override
    public void detachView() {

    }
}
