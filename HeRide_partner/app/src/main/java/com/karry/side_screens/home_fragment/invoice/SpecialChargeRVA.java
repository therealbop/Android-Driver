package com.karry.side_screens.home_fragment.invoice;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.pojo.invoice.BookingDetailsDataInvoiceExtra;
import com.karry.pojo.invoice.BookingDetailsDataTowTruckServices;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * <h1>SpecialChargeRVA</h1>
 * <p>this is the class for maintain the special charges, display the Special charges in invoice</p>
 */

public class SpecialChargeRVA extends RecyclerView.Adapter<SpecialChargeRVA.ViewHolder> {

    private Context context;
    private ArrayList<BookingDetailsDataInvoiceExtra> specialChargeList;
    private ArrayList<BookingDetailsDataTowTruckServices> bookingDetailsDataTowTruckServices;
    private TextView tv_specialCharge_title,tv_specialCharge_fare;
    private String currency,currencyAbbr;


    /**********************************************************************************************/
    @Inject
    public SpecialChargeRVA(Context context)
    {
        this.context=context;
    }

    public void setData(ArrayList<BookingDetailsDataInvoiceExtra> specialChargeList,
                                     String currency, String currencyAbbr){
        this.specialChargeList = specialChargeList;
        this.currency = currency;
        this.currencyAbbr = currencyAbbr;
    }

    public void setTowtryData(ArrayList<BookingDetailsDataTowTruckServices> bookingDetailsDataTowTruckServices,
                        String currency, String currencyAbbr){
        this.bookingDetailsDataTowTruckServices = bookingDetailsDataTowTruckServices;
        this.currency = currency;
        this.currencyAbbr = currencyAbbr;
    }

    /**********************************************************************************************/
    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
            tv_specialCharge_title = itemView.findViewById(R.id.tv_specialCharge_title);
            tv_specialCharge_fare = itemView.findViewById(R.id.tv_specialCharge_fare);
        }

    }

    /**********************************************************************************************/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_special_charge, parent, false);
        return new SpecialChargeRVA.ViewHolder(view);
    }

    /**********************************************************************************************/
    @Override
    public void onBindViewHolder(SpecialChargeRVA.ViewHolder holder, final int position) {


        if(currencyAbbr.matches("1"))
        {
            if(specialChargeList!=null) {
                tv_specialCharge_title.setText(specialChargeList.get(position).getTitle());
                tv_specialCharge_fare.setText(currency.concat(specialChargeList.get(position).getFee()));
            }else {
                tv_specialCharge_title.setText(bookingDetailsDataTowTruckServices.get(position).getTitle());
                tv_specialCharge_fare.setText(currency.concat(bookingDetailsDataTowTruckServices.get(position).getFee()));
            }

        }else {
            if(specialChargeList!=null) {
                tv_specialCharge_title.setText(specialChargeList.get(position).getTitle());
                tv_specialCharge_fare.setText(specialChargeList.get(position).getFee().concat(currency));
            }else {
                tv_specialCharge_title.setText(bookingDetailsDataTowTruckServices.get(position).getTitle());
                tv_specialCharge_fare.setText(bookingDetailsDataTowTruckServices.get(position).getFee().concat(currency));
            }
        }

    }

    /**********************************************************************************************/
    @Override
    public int getItemCount() {
        if(specialChargeList!=null) {
            return specialChargeList.size();
        }else {
            return bookingDetailsDataTowTruckServices.size();
        }

    }
}
