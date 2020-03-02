package com.karry.side_screens.history.history_invoice;


import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.side_screens.history.new_model.HistoryExtraFees;
import com.karry.utility.AppTypeFace;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistorySpecailChargeAdapter extends RecyclerView.Adapter<HistorySpecailChargeAdapter.SpecialChargeViewholder> {

    private ArrayList<HistoryExtraFees> historyInvoiceReceipts;
    private String currencyAbbreviation;
    private String currencySymbol;
    AppTypeFace appTypeFace;

    HistorySpecailChargeAdapter(ArrayList<HistoryExtraFees> historyInvoiceReceipts,String currencyAbbr
    ,String currencySymbol,AppTypeFace appTypeFace) {
        this.currencyAbbreviation=currencyAbbr;
        this.currencySymbol=currencySymbol;
        this.historyInvoiceReceipts = historyInvoiceReceipts;
        this.appTypeFace = appTypeFace;
    }

    @Override
    public SpecialChargeViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receipt_layout, parent, false);

        return new SpecialChargeViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(SpecialChargeViewholder holder, int position)
    {
        holder.tv_hist_receipt_value.setTypeface(appTypeFace.getPro_News());
        holder.tv_hist_receipt_label.setTypeface(appTypeFace.getPro_News());

        if(currencyAbbreviation!=null && currencyAbbreviation.equals("1"))
            holder.tv_hist_receipt_value.setText(currencySymbol+" "+historyInvoiceReceipts.get(position).getFee());
        else
            holder.tv_hist_receipt_value.setText(historyInvoiceReceipts.get(position).getFee()+" "+currencySymbol);

        holder.tv_hist_receipt_label.setText(historyInvoiceReceipts.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
        return historyInvoiceReceipts.size();
    }

    class SpecialChargeViewholder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_hist_receipt_label) TextView tv_hist_receipt_label;
        @BindView(R.id.tv_hist_receipt_value) TextView tv_hist_receipt_value;

        SpecialChargeViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
