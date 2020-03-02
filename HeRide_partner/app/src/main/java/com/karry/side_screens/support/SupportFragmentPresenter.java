package com.karry.side_screens.support;



import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkService;
import com.karry.pojo.SupportPojo;
import com.karry.utility.Utility;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karry.utility.VariableConstant.RESPONSE_CODE_SUCCESS;

/**
 * <h1>SupportFragmentContract</h1>
 * <p>this is the interface contain for SupportFragment and for the BookingPopUpPresenter</p>
 */
public class SupportFragmentPresenter implements SupportFragmentContract.SupportFragPresenter{

    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject CompositeDisposable compositeDisposable;

    @Nullable
    private SupportFragmentContract.SupportFragView supportFragView;

    @Inject
    SupportFragmentPresenter() { }

    /**********************************************************************************************/
    @Override
    public void attachView(Object view) {
        supportFragView  = (SupportFragmentContract.SupportFragView) view;
    }

    @Override
    public void detachView() {
        supportFragView = null;
    }

    @Override
    public void callSupportApi() {
        supportFragView.showProgress();
        Observable<Response<ResponseBody>> support=networkService.support(
                preferenceHelperDataSource.getSessionToken(),
                preferenceHelperDataSource.getLanguage());

        support.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        try {
                            JSONObject jsonObject;

                            switch (value.code())
                            {
                                case RESPONSE_CODE_SUCCESS:
                                    jsonObject=new JSONObject(value.body().string());
                                    SupportPojo supportPojo = new Gson().fromJson(jsonObject.toString(), SupportPojo.class);
                                    supportFragView.getSupportDetails(supportPojo.getData());
                                    break;
                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    supportFragView.onFailure(value.errorBody().string());
                                    break;
                            }

                            Utility.printLog("support API response is :   "+jsonObject.toString());


                        }catch (Exception e)
                        {
                            Utility.printLog("support : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utility.printLog("the api error is : "+e.getMessage());
                        supportFragView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        supportFragView.hideProgress();
                    }
                });
    }

    @Override
    public void compositeDisposableClear() {
        compositeDisposable.clear();
    }
}
