package com.karry.side_screens.wallet.changeCard;


import android.content.Context;

import com.google.gson.Gson;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkStateHolder;
import com.karry.network.NetworkService;
import com.karry.utility.DataParser;
import com.karry.utility.Utility;
import com.karry.side_screens.wallet.changeCard.model.CardDetailsDataModel;
import com.karry.side_screens.wallet.changeCard.model.CardInfoModel;
import com.karry.side_screens.wallet.changeCard.model.GetCardsPOJO;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h1>ChangeCardPresenter</h1>
 * This is used to call the API and link between view and model,
 * @author 3Embed
 * @since on 2/12/2018.
 */
class ChangeCardPresenter implements ChangeCardContract.Presenter
{
    private static final String TAG = "ChangeCardPresenter";
    @Inject Context mContext;
    @Inject Gson gson;
    @Inject NetworkService networkService;
    @Inject CompositeDisposable compositeDisposable;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject ChangeCardContract.View view;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    ChangeCardPresenter() {
    }

    @Override
    public void getAllCards()
    {
//        if (networkStateHolder.isConnected())
//        {
            view.showProgressDialog();
            Observable<Response<ResponseBody>> request = networkService.getPaymentService
                    (preferenceHelperDataSource.getSessionToken(),
                            preferenceHelperDataSource.getLanguage());
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            Utility.printLog(TAG+" get cards "+value.code());
                            switch (value.code())
                            {
                                case 200:
                                    responseParser(value);
                                    break;

                                case 500:
                                case 509:
                                    view.badGateWayError();
                                    break;

                                default:
                                    view.errorResponse();
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.errorResponse();
                        }

                        @Override
                        public void onComplete() {
                            view.dismissProgressDialog();
                        }
                    });
//        } else
//            view.internetError();
    }

    @Override
    public void deleteCard(String cardId,int position,List<CardInfoModel> cardList)
    {
//        if (networkStateHolder.isConnected())
//        {
            Observable<Response<ResponseBody>> request = networkService.deleteCard(
                    preferenceHelperDataSource.getSessionToken(),
                    preferenceHelperDataSource.getLanguage(),cardId);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            Utility.printLog(" AddCardResponse CodePayment1"+value.code());
                            switch (value.code())
                            {
                                case 200:
                                    if(cardList.get(position).getDefaultCard())
                                        preferenceHelperDataSource.setDefaultCardDetails(null);
                                    view.deleteItemData(position);
                                    Utility.printLog(" AddCardResponse CodePayment1"+ DataParser.fetchSuccessMessage(value));
                                    break;

                                case 500:
                                case 509:
                                    view.badGateWayError();
                                    break;

                                default:
                                    Utility.printLog("AddCardResponseCodePayment1"+ DataParser.fetchErrorMessage(value));
                                    view.errorResponse();
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.errorResponse();
                        }

                        @Override
                        public void onComplete() {
                            view.dismissProgressDialog();
                        }
                    });
//        } else
//            view.internetError();
    }

    /**
     * <h2>responseParser</h2>
     * This method is used to perform the work, that we will do after getting the response from server.
     */
    private void responseParser(Response<ResponseBody> value) {
        String dataObject = DataParser.fetchDataObjectString(value);
        Utility.printLog("GetCardresponse"+dataObject);
        GetCardsPOJO getCardsPOJO = gson.fromJson(dataObject, GetCardsPOJO.class);
        view.clearRowItem();
        if (getCardsPOJO.getCards().length > 0)
        {
            for (int i = 0; i < getCardsPOJO.getCards().length; i++)
            {
                Utility.printLog("CardDetail1"+getCardsPOJO.getCards()[i].toString());
                CardInfoModel item = new CardInfoModel(getCardsPOJO.getCards()[i].getBrand(), getCardsPOJO.getCards()[i].getLast4(), getCardsPOJO.getCards()[i].getExpMonth(),
                        getCardsPOJO.getCards()[i].getExpYear(), getCardsPOJO.getCards()[i].getId(), getCardsPOJO.getCards()[i].getDefault(),
                        getCardsPOJO.getCards()[i].getName(), getCardsPOJO.getCards()[i].getFunding(),getCardsPOJO.getCards()[i].getBrand());//id

                if (getCardsPOJO.getCards()[i].getDefault())
                    preferenceHelperDataSource.setDefaultCardDetails(getCardsPOJO.getCards()[i]);

                view.responseItem(item);
            }
        }
//        else
//            preferenceHelperDataSource.setDefaultCardDetails(null);
    }

    public void makeCardDefault(CardInfoModel cardInfoModel)
    {
//        if (networkStateHolder.isConnected()) {
            Observable<Response<ResponseBody>> request = networkService.makeDefaultCard(
                    preferenceHelperDataSource.getSessionToken(),
                    preferenceHelperDataSource.getLanguage(), cardInfoModel.getCard_id());
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {

                            Utility.printLog("DELETECARDACTIVITYRESPONSE"+value.code());
                            switch (value.code())
                            {
                                case 200:
                                    CardDetailsDataModel cardDetailsDataModel=new CardDetailsDataModel();
                                    String response=value.body().toString();

//                                    preferenceHelperDataSource.setDefaultCardDetails();
                                        Utility.printLog("DefaultCardDet"+DataParser.fetchSuccessMessage(value));
//                                    deleteCardView.OnResponse(DataParser.fetchSuccessMessage(value));
                                    break;
                                default:
//                                    deleteCardView.OnResponse(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            view.dismissProgressDialog();
                        }
                    });
//        }
//        else
//            view.internetError();
    }
}

