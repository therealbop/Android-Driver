package com.karry.side_screens.help_center.zendeskTicketDetails;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.data.source.sqlite.SQLiteDataSource;
import com.karry.manager.mqtt_manager.MQTTManager;
import com.karry.network.NetworkService;
import com.heride.partner.R;
import com.karry.side_screens.help_center.zendeskpojo.ZendeskHistory;
import com.karry.utility.Utility;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h>HelpIndexContractImpl</h>
 * Created by Ali on 2/26/2018.
 */

public class HelpIndexContractImpl implements HelpIndexContract.presenter
{

    @Inject HelpIndexContract.HelpView helpView;
    @Inject
    NetworkService lspServices;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    SQLiteDataSource addressDataSource;
    @Inject
    MQTTManager mqttManager;
    @Inject Context mActivity;
    private Gson gson;
    @Inject HelpIndexContractImpl() {
        gson = new Gson();
    }



    @Override
    public void onPriorityImage(Context mContext, String priority, ImageView ivHelpCenterPriorityPre) {

        if(priority.equalsIgnoreCase(mContext.getString(R.string.priorityUrgent)))
            ivHelpCenterPriorityPre.setBackgroundColor(Utility.getColor(mContext,R.color.red_login_dark));
        else if(priority.equalsIgnoreCase(mContext.getString(R.string.priorityHigh)))
            ivHelpCenterPriorityPre.setBackgroundColor(Utility.getColor(mContext,R.color.saffron));
        else if(priority.equalsIgnoreCase(mContext.getString(R.string.priorityNormal)))
            ivHelpCenterPriorityPre.setBackgroundColor(Utility.getColor(mContext, R.color.green_continue));
        else if(priority.equalsIgnoreCase(mContext.getString(R.string.priorityLow)))
            ivHelpCenterPriorityPre.setBackgroundColor(Utility.getColor(mContext,R.color.livemblue3498));

    }

    @Override
    public void callApiToCommentOnTicket(String trim, int zenId)
    {
       Observable<Response<ResponseBody>>observable = lspServices.commentOnTicket(preferenceHelperDataSource.getSessionToken(),
                preferenceHelperDataSource.getLanguage(), String.valueOf(zenId),trim,preferenceHelperDataSource.getRequesterId());

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        int code = responseBodyResponse.code();

                        switch (code)
                        {
                            case 200:

                                break;
                            case 498:

                                break;
                            case 440:

                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void callApiToCreateTicket(final String trim, final String subject, final String priority, Activity activity)
    {

        Observable<Response<ResponseBody>>observable = lspServices.createTicket(
                preferenceHelperDataSource.getSessionToken(),
                preferenceHelperDataSource.getLanguage(),
                subject,trim,"open",priority,"problem",
                preferenceHelperDataSource.getRequesterId());

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse)
                    {
                        int code = responseBodyResponse.code();

                        Utility.printLog("code is : "+code);
                        String response;
                        try {


                            switch (code)
                            {
                                case 200:
                                    response = responseBodyResponse.body().string();
                                    Log.d("TAG", "RESULTonSuccess: "+response);
                                    helpView.onZendeskTicketAdded(response);
                                    JSONObject jsonObjectsuccess = new JSONObject(response);
                                    Utility.BlueToast(activity,jsonObjectsuccess.getString("message"));
                                    helpView.success();
                                    break;

                                 default:
                                     response = responseBodyResponse.errorBody().string();
                                     Log.d("TAG", "RESULTonSuccess: "+response);
                                     JSONObject jsonObject = new JSONObject(response);
                                     Utility.BlueToast(activity,jsonObject.getString("message"));
                                     helpView.success();
                                     break;
                               /* case 498:
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void callApiToGetTicketInfo(final int zenId)
    {
        Observable<Response<ResponseBody>> observable = lspServices.onToGetZendeskHistory(preferenceHelperDataSource.getSessionToken(),
                preferenceHelperDataSource.getLanguage(), String.valueOf(zenId));

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
                        JSONObject jsonObject;
                        try
                        {


                            switch (code)
                            {
                                case 200:
                                    response = responseBodyResponse.body().string();
                                    Log.d("HELPINDEX", "onNextResponseINFOTICKET: "+response);
                                    ZendeskHistory zendeskHistory = gson.fromJson(response,ZendeskHistory.class);

                                    Date date = new Date(zendeskHistory.getData().getTimeStamp() * 1000L);
                                    String dateTime[] = Utility.getFormattedDate(date,preferenceHelperDataSource).split(",");
                                    String timeToSet =  dateTime[0]+" | "+dateTime[1];
                                    helpView.onTicketInfoSuccess(zendeskHistory.getData().getEvents(),timeToSet,
                                            zendeskHistory.getData().getSubject(),zendeskHistory.getData().getPriority(),zendeskHistory.getData().getType());
                                    helpView.hideProgress();
                                    break;
                                /*case 498:
                                    ExpireSession.refreshApplication(mActivity,mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;
                                case 440:
                                    ExpireSession.refreshApplication(mActivity,mqttManager, preferenceHelperDataSource, addressDataSource);
                                    break;*/
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

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
