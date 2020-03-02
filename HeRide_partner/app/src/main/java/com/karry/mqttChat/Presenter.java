package com.karry.mqttChat;

import android.content.Context;
import android.content.Intent;
import com.google.gson.Gson;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkService;
import com.karry.utility.Utility;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by DELL on 29-03-2018.
 */

public class Presenter implements ChattingContract.PresenterOperations {

    @Inject
    public Presenter() {
    }

    @Inject Context mContext;
    /*@Inject @Named("CHAT_URL") MessageService messageService;*/
    @Inject NetworkService messageService;
    @Inject ChattingContract.ViewOperations view;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    private String bid,custName,custId,driverId;

    private Observer<ChatData> observer;


    private ArrayList<ChatData> chatDataArry=new ArrayList<>();

    @Override
    public void getData(Intent intent)
    {
        bid = intent.getStringExtra("BID");
        custName = intent.getStringExtra("DRIVER_NAME");
        driverId = preferenceHelperDataSource.getMid();
        custId= intent.getStringExtra("CUST_ID");
    }

    @Override
    public void setActionBar() {
        view.setActionBar(custName);
    }

    @Override
    public void initViews() {
        view.setViews(bid);
        view.setRecyclerView();
        initializeRxJava();
    }

    @Override
    public void getChattingData(final int pageNo) {

        view.showProgress();
        final Observable<Response<ResponseBody>> profile=messageService.chatHistory(preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getSessionToken(), bid, String.valueOf(pageNo));
        profile.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(view!=null)
                            view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200)
                            {
                                String resp = value.body().string();
                                Utility.printLog("chat data is : "+resp+"\n page Index : "+pageNo);
                                jsonObject=new JSONObject(resp);

                                ChatHistoryResponse  chatHistoryResponse = new Gson().fromJson(jsonObject.toString(), ChatHistoryResponse.class);
                                if(chatHistoryResponse.getData()!=null && chatHistoryResponse.getData().size()>0){
                                    chatDataArry.clear();
                                    for(int i=0;i<chatHistoryResponse.getData().size();i++){
                                        if(chatHistoryResponse.getData().get(i).getFromID().equals(driverId)){
                                            chatHistoryResponse.getData().get(i).setCustProType(1);
                                        }else {
                                            chatHistoryResponse.getData().get(i).setCustProType(2);
                                        }
                                        chatDataArry.add(chatHistoryResponse.getData().get(i));
                                    }
                                    Collections.reverse(chatDataArry);
                                    view.updateData(chatDataArry);

                                    /*int page = pageNo+1;
                                    getChattingData(page);*/
                                }

                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                            }

                            Utility.printLog("chatHistory : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("chatHistory : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
//                        view.onError( context.getResources().getString(R.string.serverError));
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }

    @Override
    public void message(String message) {
        if(!message.trim().isEmpty())
        {
            sendMessage(message);
        }
    }


    private void initializeRxJava()
    {

        observer = new Observer<ChatData>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ChatData jsonObject) {

                jsonObject.setCustProType(2);

                chatDataArry.add(jsonObject);
                view.updateData(chatDataArry);


            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        ChatDataObervable.getInstance().subscribe(observer);
    }

    private void sendMessage(final String content){

        final long timeStamp=System.currentTimeMillis();

        if(view!=null)
            view.showProgress();

        Observable<Response<ResponseBody>> message=messageService.message(
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getSessionToken(),
                "1",
                timeStamp+"",
                content,
                driverId,
                bid,
                custId
                );
        message.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(view!=null)
                            view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());

                                ChatData chatData=new ChatData();
                                chatData.setTimestamp(String.valueOf(timeStamp));
                                chatData.setType(1);
                                chatData.setTargetId(custId);
                                chatData.setFromID(driverId);
                                chatData.setContent(content);
                                chatData.setCustProType(1);

                                chatDataArry.add(chatData);
                                view.updateData(chatDataArry);

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());

                            }

                            Utility.printLog("messageApi : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("messageApi : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }
}
