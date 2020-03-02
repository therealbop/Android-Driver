package com.karry.side_screens.history;


import android.util.Log;

import com.karry.side_screens.history.new_model.HistoryPojo;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.network.NetworkService;
import com.karry.utility.Utility;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;



public class HistoryPresenter implements HistoryContract.HistoryPresenter {

    private String TAG=HistoryPresenter.class.getSimpleName();
    private SimpleDateFormat XAxisFormat;
    private int differenceDays = 0;
    private ArrayList<Float> totalsForBar;
    private ArrayList<BarEntry> barEntries;


    @Inject NetworkService networkService;

    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject Gson gson;

    private HistoryContract.HistoryView historyView;

    @Inject
    public HistoryPresenter() {


    }

    public void attachView(HistoryFragment view){
        this.historyView = view;
        init();
    }

    private void init(){
        XAxisFormat = new SimpleDateFormat("EEE", Locale.US);
        totalsForBar = new ArrayList<>();
        barEntries = new ArrayList<>();
        initDays();
    }



    @Override
    public void getOrders(String startDate, final int selectedTabPosition, final int tabcount)
    {
        Utility.printLog("HistoryResult--"+preferenceHelperDataSource.getSessionToken());
        if(historyView !=null)
            historyView.showProgress();

        Observable<Response<ResponseBody>> order=networkService.order(preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getSessionToken(),startDate);
        order.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(historyView !=null)
                            historyView.hideProgress();

                            Utility.printLog("HistoryRes"+value.code());
                            if(value.code()==200){
                                String result;
                                try {
                                    result = value.body().string();
                                    Utility.printLog("HistoryRes-"+result);
                                    HistoryPojo historyPojo=gson.fromJson(result,HistoryPojo.class);
                                    if(historyPojo.getData()!=null)
                                    handleDate(historyPojo, selectedTabPosition, tabcount);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }




                    }

                    @Override
                    public void onError(Throwable e) {
                        if(historyView !=null)
                            historyView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(historyView !=null)
                            historyView.hideProgress();
                    }
                });
    }


    private void initDays() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String currentDay = XAxisFormat.format(c.getTime()).toUpperCase();
        differenceDays = 0;
        switch (currentDay) {
            case "SUN":
                differenceDays = 0;
                break;
            case "MUN":
                differenceDays = 1;
                break;
            case "TUE":
                differenceDays = 2;
                break;
            case "WED":
                differenceDays = 3;
                break;
            case "THU":
                differenceDays = 4;
                break;
            case "FRI":
                differenceDays = 5;
                break;
            case "SAT":
                differenceDays = 6;
                break;
        }

        ArrayList<String> currenCycleDays = new ArrayList<>();
        c.add(Calendar.DATE, -differenceDays);
        for (int i = 0; i <= differenceDays; i++) {
            currenCycleDays.add(XAxisFormat.format(c.getTime()).toUpperCase());
            c.add(Calendar.DATE, +1);
            Log.d(TAG, "currentCycleDays: " + currenCycleDays.get(i));
        }

        ArrayList<String> pastCycleDays = new ArrayList<>();
        pastCycleDays.add("SUN");
        pastCycleDays.add("MON");
        pastCycleDays.add("TUE");
        pastCycleDays.add("WED");
        pastCycleDays.add("THR");
        pastCycleDays.add("FRI");
        pastCycleDays.add("SAT");

        historyView.onDayInitialized(differenceDays,currenCycleDays,pastCycleDays);
    }

    private void handleDate(HistoryPojo historyPojo, int selectedTabPosition, int tabcount) {
        double amountEarned = 0;
        String displayTotal = "";
        for (int i = 0; i < historyPojo.getData().getTotalEarningData().size(); i++) {
            amountEarned += Double.parseDouble(historyPojo.getData().getTotalEarningData().get(i).getAmount());
        }

        totalsForBar.clear();
        for (int i = 0; i < historyPojo.getData().getTotalEarningData().size(); i++) {
            String amt = historyPojo.getData().getTotalEarningData().get(i).getAmount();
            if (amt != null && !amt.isEmpty()) {
                if (!amt.equals("NaN")) {
                    totalsForBar.add(Float.parseFloat(amt));
                } else {
                    totalsForBar.add(0.0f);
                }
            } else {
                totalsForBar.add(0.0f);
            }
        }

        if(historyPojo.getData().getAppointmentData().size()>0) {
            displayTotal = historyPojo.getData().getAppointmentData().get(0).getCurrencySbl();
            if(historyPojo.getData().getAppointmentData().get(0).getCurrencyAbbr().equals("1"))
                displayTotal=displayTotal+" "+amountEarned;
            else
                displayTotal=amountEarned+" "+displayTotal;
        }

        barEntries.clear();
        float highestvalue = 0;
        int highestPosition = 0;

        if (selectedTabPosition == tabcount) {
            for (int i = 0; i <= differenceDays; i++) {
                barEntries.add(new BarEntry(i, totalsForBar.get(i)));

                if (totalsForBar.get(i) > highestvalue) {
                    highestPosition = i;
                    highestvalue = totalsForBar.get(i);
                }
            }
        } else {
            for (int i = 0; i < 7; i++) {
                barEntries.add(new BarEntry(i, totalsForBar.get(i)));

                if (totalsForBar.get(i) > highestvalue) {
                    highestPosition = i;
                    highestvalue = totalsForBar.get(i);
                }
            }
        }

        historyView.setBarChartVal(historyPojo.getData().getAppointmentData(),barEntries,Double.toString(amountEarned),highestPosition,displayTotal);
    }

}