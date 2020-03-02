package com.karry.side_screens.history.history_invoice;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.side_screens.help_center.zendeskTicketDetails.HelpIndexTicketDetails;
import com.karry.utility.AppTypeFace;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryDetailsHelpAdapter extends RecyclerView.Adapter<HistoryDetailsHelpAdapter.ViewHolder>
{
    private static final String TAG = "HistoryDetailsHelpAdapter";
    private AppTypeFace appTypeface;
    private ArrayList<HelpDataModel> helpDataModels;
    private int historyType;
    private Context mContext;
    private String bid;

    /**
     * This is the constructor of our adapter.
     * //@param reasons contains an array list.
     * //@param rv_OnItemViewsClickNotifier reference of OnItemViewClickNotifier Interface.
     */
    @Inject
    HistoryDetailsHelpAdapter(Context context, AppTypeFace appTypeface, ArrayList<HelpDataModel> helpDataModels)
    {
        this.appTypeface = appTypeface;
        this.mContext = context;
        this.helpDataModels = helpDataModels;
    }



    @Override
    public int getItemCount()
    {
        return helpDataModels.size();
    }



    @Override
    public HistoryDetailsHelpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View viewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_details_item, parent, false);
        return new HistoryDetailsHelpAdapter.ViewHolder(viewList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HelpDataModel helpDataModel = helpDataModels.get(position);
        holder.tv_help_list.setText(helpDataModel.getName());
        holder.view.setOnClickListener(v ->
        {
            String subject = mContext.getResources().getString(R.string.bookingID)
                    .concat(helpDataModel.getBid()
                            .concat(":")
                            .concat(helpDataModel.getName()));

            Intent intent = new Intent(mContext, HelpIndexTicketDetails.class);
            intent.putExtra("ISTOAddTICKET",true);
            intent.putExtra("SUBJECT",subject);
            mContext.startActivity(intent);
        });
    }




    /**
     * <h1>ListViewHolder</h1>
     * <p>
     *   This method is used to initialize the views
     * </p>
     */
    class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_help_list) TextView tv_help_list;
        View view;

        ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view=itemView;
            tv_help_list.setTypeface(appTypeface.getPro_News());

        }
    }
}