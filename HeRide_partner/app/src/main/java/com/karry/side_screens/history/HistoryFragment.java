package com.karry.side_screens.history;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karry.side_screens.history.new_model.AppointmentData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.karry.app.mainActivity.MainActivity;
import com.heride.partner.R;
import com.karry.utility.AppTypeFace;
import com.karry.utility.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class HistoryFragment extends DaggerFragment implements TabLayout.OnTabSelectedListener ,HistoryContract.HistoryView {

    int tabcount = 12;
    int tabIncrement = 5;
    private HistoryTripsRVA historyTripsRVA;

    private SimpleDateFormat tabDateFormat,apiDateFormat;
    private String selectedWeeks;
    private XAxis xAxis;

    private  int differenceDays = 0;
    private ArrayList<Date> apiStartWeek;
    private ArrayList<AppointmentData> appointments;
    private ArrayList<String> currentCycleDays,pastCycleDays;

    @BindView(R.id.rv_job_home) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.tv_amount_earned) TextView tv_amount_earned;
    @BindView(R.id.mChart) BarChart mChart;
    @BindView(R.id.tv_earned_value)TextView tv_earned_value;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindString(R.string.loading) String loading;

    @Inject AppTypeFace fontUtils;
    @Inject HistoryContract.HistoryPresenter historyPresenter;

    @Inject
    public HistoryFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this,rootView);
        initViews();
        historyPresenter.attachView(this);
        ((MainActivity)getActivity()).setFragmentRefreshListener(this::onResume);
        return rootView;
    }

    private void initViews()
    {
        currentCycleDays=new ArrayList<>();
        pastCycleDays=new ArrayList<>();
        appointments= new ArrayList<>();
        apiStartWeek=new ArrayList<>();
        ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(loading);
        pDialog.setCancelable(false);

        tabDateFormat = new SimpleDateFormat("MMM dd", Locale.US);
        apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        historyTripsRVA = new HistoryTripsRVA(getActivity(),appointments,fontUtils);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(historyTripsRVA);

        tv_amount_earned.setTypeface(fontUtils.getClanaproNarrBook());
        tv_earned_value.setTypeface(fontUtils.getClanaproNarrBook());

    }


    @Override
    public void onDayInitialized(int differenceDays, ArrayList<String> currentCycleDays, ArrayList<String> postCycleDays) {
        this.differenceDays = differenceDays;
        this.currentCycleDays.addAll(currentCycleDays);
        this.pastCycleDays.addAll(postCycleDays);
        initTabLayout(tabcount);
        initBarChart();
    }



    private void initTabLayout(final int selectableTab)
    {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        tabLayout.removeOnTabSelectedListener(this);
        tabLayout.removeAllTabs();
        apiStartWeek.clear();

        for(int i = 0; i <= tabcount ;i++)
        {
            tabLayout.addTab(tabLayout.newTab());
        }

        for(int i=tabcount ; i >= 0 ; i--)
        {
            String startDate;
            String endDate;
            if(i==tabcount)
            {
                endDate = tabDateFormat.format(c.getTime());
                c.add(Calendar.DATE , -differenceDays);
                startDate = tabDateFormat.format(c.getTime());
                apiStartWeek.add(c.getTime());
                c.add(Calendar.DATE , -1);

                if(differenceDays != 0)
                    tabLayout.getTabAt(i).setText(startDate + "-" + endDate);
                else
                    tabLayout.getTabAt(i).setText(startDate);

            }
            else
            {
                endDate = tabDateFormat.format(c.getTime());
                c.add(Calendar.DATE , -6);
                startDate = tabDateFormat.format(c.getTime());
                apiStartWeek.add(c.getTime());
                c.add(Calendar.DATE , -1);
                tabLayout.getTabAt(i).setText(startDate + "-" + endDate);
            }
        }

        Completable.complete()
                .delay(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> tabLayout.getTabAt(selectableTab).select())
                .subscribe();

        tabLayout.addOnTabSelectedListener(this);

        selectedWeeks = tabLayout.getTabAt(tabcount).getText().toString();

    }


    private void initBarChart()
    {
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(true);// scaling can now only be done on x- and y-axis separately
        mChart.setDrawGridBackground(false);// scaling can now only be done on x- and y-axis separately
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);

        xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int selectedTabPosition = tab.getPosition();

        if(selectedTabPosition == 0)
        {
            tabcount += tabIncrement;
            initTabLayout(tabIncrement);
            return;
        }

        if(selectedTabPosition == tabcount)
        {
            int count = differenceDays +1;
            xAxis.setValueFormatter(new IndexAxisValueFormatter(currentCycleDays));
            xAxis.setLabelCount(count);
            mChart.notifyDataSetChanged();
        }
        else
        {
            xAxis.setValueFormatter(new IndexAxisValueFormatter(pastCycleDays));
            xAxis.setLabelCount(7);
            mChart.notifyDataSetChanged();
        }

        selectedWeeks = tabLayout.getTabAt(selectedTabPosition).getText().toString();

        int position = tabcount - selectedTabPosition;
        String apiSelectedDate = apiDateFormat.format(apiStartWeek.get(position));

        historyPresenter.getOrders(apiSelectedDate, selectedTabPosition,tabcount);
    }



    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(int failure) {
        if(failure==401){
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();

        }
    }

    @Override
    public void onFailure() {
        Toast.makeText(getActivity(),getString(R.string.serverError),Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    public void setBarChartVal(ArrayList<AppointmentData> appointments, ArrayList<BarEntry> barEntries, String total, int highestPosition,String displayTotal)
    {
        Utility.printLog("appointments2"+"dad");
        Utility.printLog("appointments"+appointments);
        this.appointments.clear();
        if (appointments!=null)
        this.appointments.addAll(appointments);

        Utility.printLog("appointments1"+barEntries.size());
        historyTripsRVA.notifyDataSetChanged();



        tv_earned_value.setText(displayTotal);


        BarDataSet set1;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0)
        {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(barEntries);
            set1.setLabel("The Week "+ selectedWeeks);
            set1.setHighLightColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
            Highlight highlight = new Highlight(highestPosition,0, 0);
            mChart.highlightValue(highlight, false);

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        }
        else
        {
            set1 = new BarDataSet(barEntries, "The Week "+ selectedWeeks);
            set1.setDrawIcons(false);
            set1.setColors(ContextCompat.getColor(getActivity(),R.color.colorPrimaryLight));
            set1.setHighLightColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.8f);
            data.setHighlightEnabled(true);

            mChart.setData(data);

            Highlight highlight = new Highlight(highestPosition,0, 0);
            mChart.highlightValue(highlight, false);
        }

        mChart.invalidate();

        mChart.animateXY(1000,500);
    }


    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utility.printLog("onDestroyData");
    }
}
