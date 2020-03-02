package com.karry.side_screens.history.history_invoice;


import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.utility.AppTypeFace;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryInvoiceReceiptAdapter extends RecyclerView.Adapter<HistoryInvoiceReceiptAdapter.ReceiptViewHolder>
{
    private ArrayList<HistoryInvoiceReceipt> historyInvoiceReceipts;
    AppTypeFace appTypeFace;

    HistoryInvoiceReceiptAdapter(ArrayList<HistoryInvoiceReceipt> historyInvoiceReceipts,AppTypeFace appTypeFace) {
        this.historyInvoiceReceipts = historyInvoiceReceipts;
        this.appTypeFace = appTypeFace;
    }

    @Override
    public ReceiptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receipt_layout, parent, false);

        return new ReceiptViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReceiptViewHolder holder, int position)
    {
        holder.tv_hist_receipt_label.setTypeface(appTypeFace.getPro_News());
        holder.tv_hist_receipt_label.setText(historyInvoiceReceipts.get(position).getLabel());
        holder.tv_hist_receipt_value.setTypeface(appTypeFace.getPro_News());
        holder.tv_hist_receipt_value.setText(historyInvoiceReceipts.get(position).getValue());

    }

    @Override
    public int getItemCount() {
        return historyInvoiceReceipts.size();
    }

    class ReceiptViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_hist_receipt_label) TextView tv_hist_receipt_label;
        @BindView(R.id.tv_hist_receipt_value) TextView tv_hist_receipt_value;

        ReceiptViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
