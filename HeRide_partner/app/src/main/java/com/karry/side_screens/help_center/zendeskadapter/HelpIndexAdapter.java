package com.karry.side_screens.help_center.zendeskadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.karry.data.source.local.PreferenceHelperDataSource;
import com.heride.partner.R;
import com.karry.side_screens.help_center.zendeskTicketDetails.HelpIndexTicketDetails;
import com.karry.side_screens.help_center.zendeskpojo.OpenClose;
import com.karry.utility.AppTypeFace;
import com.karry.utility.Utility;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h>HelpIndexAdapter</h>
 * Created by Ali on 2/26/2018.
 */

public class HelpIndexAdapter extends RecyclerView.Adapter
{

    private Context mContext;
    private ArrayList<OpenClose> openCloses;
    AppTypeFace applicationFonts;

    private int openSize,closeSize;
    private PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    public HelpIndexAdapter()
    {

    }

    public void onCreateIndex(Activity zendeskHelpIndex, ArrayList<OpenClose> openCloses, AppTypeFace applicationFonts, PreferenceHelperDataSource preferenceHelperDataSource)
    {
        mContext = zendeskHelpIndex;
        this.openCloses = openCloses;
        this.applicationFonts =applicationFonts;
        this.preferenceHelperDataSource =preferenceHelperDataSource;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.help_index_adapter,parent,false);
        return new ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ViewHolders hldr  = (ViewHolders) holder;

        if(openCloses.get(position).isFirst())
        {
            hldr.tv_chat_with_us.setVisibility(View.VISIBLE);
            hldr.tvOpenCloseStatus.setVisibility(View.VISIBLE);
            String status = mContext.getString(R.string.status)+" : "+openCloses.get(position).getStatus();
            hldr.tvOpenCloseStatus.setText(status);
        }
        else {
            hldr.tvOpenCloseStatus.setVisibility(View.GONE);
            hldr.tv_chat_with_us.setVisibility(View.GONE);
        }
        hldr.tvHelpSubject.setText(openCloses.get(position).getSubject());
        Date date = new Date(openCloses.get(position).getTimeStamp() * 1000L);

        String formattedDate[] = Utility.getFormattedDate(date,preferenceHelperDataSource).split(",");
        hldr.tvHelpDate.setText(formattedDate[0]);
        hldr.tvHelpTime.setText(formattedDate[1]);
        char c = openCloses.get(position).getSubject().charAt(0);
        hldr.tvHelpText.setText(c+"");

        if(openSize>0)
        {
            if(position == openSize-1)
                hldr.idView.setVisibility(View.GONE);
        }
        if(position==(getItemCount()-1)){
            hldr.idView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return openCloses.size();
    }

    public void openCloseSize(int openSize, int closeSize)
    {
        this.openSize = openSize;
        this.closeSize = closeSize;
    }

    public class ViewHolders extends RecyclerView.ViewHolder
    {

        @BindView(R.id.idView)View idView;
        @BindView(R.id.tvOpenCloseStatus)TextView tvOpenCloseStatus;
        @BindView(R.id.tvHelpSubject)TextView tvHelpSubject;
        @BindView(R.id.tvHelpTime)TextView tvHelpTime;
        @BindView(R.id.tvHelpDate)TextView tvHelpDate;
        @BindView(R.id.tvHelpText)TextView tvHelpText;
        @BindView(R.id.tv_chat_with_us)TextView tv_chat_with_us;
        ViewHolders(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            tvOpenCloseStatus.setTypeface(applicationFonts.getPro_News());
            tvHelpSubject.setTypeface(applicationFonts.getPro_News());
            tvHelpTime.setTypeface(applicationFonts.getPro_News());
            tvHelpDate.setTypeface(applicationFonts.getPro_News());
            tv_chat_with_us.setTypeface(applicationFonts.getPro_News());
            itemView.setOnClickListener(view -> {

                Intent intent = new Intent(mContext, HelpIndexTicketDetails.class);
                intent.putExtra("ISTOAddTICKET",false);
                intent.putExtra("ZendeskId",openCloses.get(getAdapterPosition()).getId());
                mContext.startActivity(intent);
                Activity activity= (Activity) mContext;
                activity.overridePendingTransition(R.anim.slide_in_up,R.anim.stay_still);
            });

            tv_chat_with_us.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent zendesk = new Intent(mContext, ZopimChatActivity.class);
                    mContext.startActivity(zendesk);
                    Activity activity= (Activity) mContext;
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }
            });

        }
    }
}
