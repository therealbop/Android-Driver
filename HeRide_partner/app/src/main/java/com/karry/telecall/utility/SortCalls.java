package com.karry.telecall.utility;

/**
 * Created by moda on 04/05/17.
 */



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;


public class SortCalls implements Comparator {

    private Date date1, date2;


    @SuppressWarnings("unchecked")
    public int compare(Object firstObjToCompare, Object secondObjToCompare) {
        String firstDateString = ((CallItem) firstObjToCompare).getCallInitiateTime();//.substring(0,16);
        String secondDateString = ((CallItem) secondObjToCompare).getCallInitiateTime();//.substring(0,16);

        if (secondDateString == null || firstDateString == null) {
            return 0;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);


        try {

            date1 = sdf.parse(firstDateString);
            date2 = sdf.parse(secondDateString);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (date1.after(date2)) return -1;
        else if (date1.before(date2)) return 1;
        else return 0;
    }

}