package com.karry.side_screens.history;


import com.karry.side_screens.history.new_model.AppointmentData;
import com.github.mikephil.charting.data.BarEntry;
import com.karry.BaseView;


import java.util.ArrayList;

public interface HistoryContract
{
    interface HistoryView extends BaseView
    {
        void onDayInitialized(int differenceDays, ArrayList<String> currentCycleDays, ArrayList<String> postCycleDays);
        void onFailure(String msg);
        void onFailure(int failure);
        void onFailure();
        void setBarChartVal(ArrayList<AppointmentData> appointments, ArrayList<BarEntry> barEntries, String total, int highestPosition, String displayTotal);
    }
    interface HistoryPresenter
    {
        void attachView(HistoryFragment view);
        void getOrders(String startDate, int selectedTabPosition, int tabcount);
    }
}