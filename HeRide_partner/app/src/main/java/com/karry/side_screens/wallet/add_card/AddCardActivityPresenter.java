package com.karry.side_screens.wallet.add_card;

import android.content.Context;

import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkStateHolder;
import com.karry.network.NetworkService;
import com.karry.utility.DataParser;
import com.karry.utility.Utility;
import com.stripe.android.model.Card;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class AddCardActivityPresenter implements AddCardActivityContract.AddCardPresenter
{
    @Inject NetworkStateHolder networkStateHolder;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
//    @Inject
//    @Named("Single")
//    CompositeDisposable compositeDisposable;
    @Inject Context context;
    @Inject AddCardActivityContract.AddCardview addCardview;


    @Inject
    AddCardActivityPresenter()
    {

    }


    @Override
    public void addCardService(String cardToken)
    {
        networkService.addCard(
                preferenceHelperDataSource.getSessionToken()
                , preferenceHelperDataSource.getLanguage(), preferenceHelperDataSource.getGmail(), cardToken)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        Utility.printLog("MyCardDetailsIs" + responseBodyResponse.code());
                        switch (responseBodyResponse.code()) {
                            case 200:
                                addCardview.onResponse(DataParser.fetchSuccessMessage(responseBodyResponse), cardToken);
                                break;

                            default:
                                addCardview.onError(DataParser.fetchErrorMessage(responseBodyResponse));
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utility.printLog("MyCardDetailsIs3");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Utility.printLog("MyCardDetailsIs4");
                    }
                });

    }


    @Override
    public void validateCardDetails(Card card)
    {
        if(card==null)
            addCardview.onInvalidOfCard();
        else
            addCardview.onValidOfCard();
    }

    @Override
    public String stripeKeyGetter()
    {
       return preferenceHelperDataSource.getStripeKey();
    }

    @Override
    public void hideKeyboardAndClearFocus() {
        addCardview.hideSoftKeyboard();
    }

    @Override
    public void showKeyboard() {
        addCardview.showSoftKeyboard();
    }




    /*class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        private Exception exception;

        protected Void doInBackground(String... urls) {
            String cardToken = urls[0];
            Observable<Response<ResponseBody>> request = networkService.addCard(
                    preferenceHelperDataSource.getSessionToken()
                    , preferenceHelperDataSource.getLanguage(), preferenceHelperDataSource.getGmail(), cardToken);
            request.subscribeOn(Schedulers.io());
            request.observeOn(AndroidSchedulers.mainThread());
            request.subscribe(new Observer<Response<ResponseBody>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Utility.printLog("MyCardDetailsIs==" + d.isDisposed());

                }

                @Override
                public void onNext(Response<ResponseBody> value) {

                    Utility.printLog("MyCardDetailsIs" + value.code());
                    switch (value.code()) {
                        case 200:
                            addCardview.onResponse(DataParser.fetchSuccessMessage(value), cardToken);
                            break;

                        default:
                            addCardview.onError(DataParser.fetchErrorMessage(value));
                            break;
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    Utility.printLog("MyCardDetailsIs--");
                }
            });
            return null;

        }


        protected void onPostExecute(Void feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }*/
}
