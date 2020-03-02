package com.karry.utility;

import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkService;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

interface AcknowledgementCallback {
    void callback(String bid);
}


public class AcknowledgeHelper {
    private String TAG = "AcknowledgeHelper";

    PreferenceHelperDataSource preferenceHelperDataSource;
    NetworkService dispatcherService;

    @Inject
    public AcknowledgeHelper(PreferenceHelperDataSource dataSource, NetworkService dispatcherService) {
        preferenceHelperDataSource=dataSource;
        this.dispatcherService=dispatcherService;
    }


    public void  bookingAckApi(String bid, final AcknowledgementCallback callback){

        Utility.printLog("testing mqtt ack req params : "+preferenceHelperDataSource.getSessionToken()+" \n"+
                preferenceHelperDataSource.getLanguage()+" \n"+bid);
        final Observable<Response<ResponseBody>> bookingAck=dispatcherService.bookingAck(preferenceHelperDataSource.getSessionToken(),
                preferenceHelperDataSource.getLanguage(),bid);
        bookingAck.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        try {
                            JSONObject jsonObject;
                            switch (value.code()) {
                                case VariableConstant.RESPONSE_CODE_SUCCESS:
                                    jsonObject = new JSONObject(value.body().string());
                                    Utility.printLog("bookingAck  ......." + jsonObject.toString());
                                    callback.callback(jsonObject.getString("message"));
                                    break;

                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    Utility.printLog("bookingAck : Catch :"+jsonObject.toString());
                                    break;
                            }
                        }catch (Exception e)
                        {
                            Utility.printLog("bookingAck : Catch :"+e.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Utility.printLog("bookingAck : Error :"+e.getMessage());
                    }
                    @Override
                    public void onComplete() {
                    }
                });

    }


    public interface AcknowledgementCallback {
        void callback(String bid);
    }
}