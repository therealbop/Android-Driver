package com.karry.twilio_call;

import com.karry.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ClientActivityPresenter implements ClientActivityContract.ClientPresenter
{
    @Inject TwilioCallNetworkService networkService;
    @Inject ClientActivityContract.ClientView clientView;
    private CompositeDisposable compositeDisposable;

    @Inject
    ClientActivityPresenter()
    {
        compositeDisposable = new CompositeDisposable();
    }

    public void getCapabilityToken(String phoneNumber)
    {
        Observable<Response<ResponseBody>> request = networkService.makeTwillioCall(phoneNumber);

        request.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        compositeDisposable.add(d);
                    }
                    @Override
                    public void onNext(Response<ResponseBody> value)
                    {
                        Utility.printLog("MyCallResponse"+value.code());
                        switch (value.code())
                        {
                            case 200:
                                try {
                                    String response=value.body().string();
                                    JSONObject profileObject = new JSONObject(response);
                                    String token = profileObject.getString("tocken");
                                    clientView.createDevice(token);
                                    Utility.printLog("MyCallResponse"+token);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;

                            default:
                                clientView.showToast();
                                break;
                        }

                    }
                    @Override
                    public void onError(Throwable e)
                    {
                        e.getStackTrace();

                    }

                    @Override
                    public void onComplete()
                    {

                    }
                });
    }

    @Override
    public void dispose()
    {
        compositeDisposable.clear();
    }
}
