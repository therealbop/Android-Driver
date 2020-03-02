package com.karry.side_screens.history;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.side_screens.history.history_invoice.HistoryInvoiceActivity;
import com.karry.side_screens.history.new_model.AppointmentData;
import com.karry.utility.AppTypeFace;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HistoryTripsRVA extends RecyclerView.Adapter<HistoryTripsRVA.ViewHolder> {

    private Context context;
    private Context mContext;
    private ArrayList<AppointmentData> appointments;
    private AppTypeFace fontUtils;


    /**********************************************************************************************/
    HistoryTripsRVA(Context context, ArrayList<AppointmentData> appointments, AppTypeFace fontUtils) {
        setHasStableIds(true);
        this.context = context;
        this.appointments = appointments;
        this.fontUtils=fontUtils;
    }

    /**********************************************************************************************/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_currentupcoming_job, parent, false);

        return new ViewHolder(view);
    }

    /**********************************************************************************************/
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final int index = holder.getAdapterPosition();
        holder.setIsRecyclable(false);

        if(appointments.get(position).getStatus().equals("5") ||appointments.get(position).getStatus().equals("4"))
            holder.iv_hist_appointment_cancel.setImageResource(R.drawable.my_booking_cancel_logo);

        holder.cv_singlerow_job.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, HistoryInvoiceActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("data",appointments.get(index));
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        });

        holder.tv_job_id.setText(context.getResources().getString(R.string.id) + " " + appointments.get(position).getBookingId());
        holder.tv_pickup_loc.setText(appointments.get(position).getPickupAddress());
        holder.tv_pickup_loc.setSelected(true);
        if(appointments.get(position).getDropAddress()!=null && !"".equals(appointments.get(position).getDropAddress())) {
            holder.tv_drop_loc.setText(appointments.get(position).getDropAddress());
            holder.tv_drop_loc.setSelected(true);
        }
        else
            holder.tv_drop_loc.setVisibility(View.GONE);

        if(appointments.get(position).getCurrencyAbbr().matches("1"))
            holder.tv_deliveryfee.setText(appointments.get(position).getCurrencySbl().concat(" ").concat(appointments.get(position).getInvoice().getTotal()));
        else
            holder.tv_deliveryfee.setText(appointments.get(position).getInvoice().getTotal().concat(" ").concat(appointments.get(position).getCurrencySbl()));


        holder.tv_date_time.setText(Utility.convertUTCToServerFormat(appointments.get(position).getTimeStamp(), VariableConstant.TIME_FORMAT_TIME_DISPLAY1));
    }

    /**********************************************************************************************/
    @Override
    public int getItemCount() {
        return appointments.size();
    }

    /**********************************************************************************************/
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_timeleft) LinearLayout ll_timeleft ;
        @BindView(R.id.tv_job_id)TextView tv_job_id ;
        @BindView(R.id.tv_deliveryfee)TextView tv_deliveryfee ;
        @BindView(R.id.tv_time_left_txt)TextView tv_time_left_txt ;
        @BindView(R.id.tv_deliverytime)TextView tv_deliverytime ;
        @BindView(R.id.tv_pickup_loc)TextView tv_pickup_loc ;
        @BindView(R.id.tv_drop_loc)TextView tv_drop_loc ;
        @BindView(R.id.tv_date_time)TextView tv_date_time ;
        @BindView(R.id.iv_hist_appointment_cancel)ImageView iv_hist_appointment_cancel ;
        @BindView(R.id.cv_singlerow_job)CardView cv_singlerow_job ;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ll_timeleft.setVisibility(View.GONE);
            tv_job_id.setTypeface(fontUtils.getClanaproNarrBook());
            tv_deliveryfee.setTypeface(fontUtils.getClanaproNarrBook());
            tv_time_left_txt.setTypeface(fontUtils.getClanaproNarrBook());
            tv_deliverytime.setTypeface(fontUtils.getClanaproNarrBook());
            tv_pickup_loc.setTypeface(fontUtils.getClanaproNarrBook());
            tv_drop_loc.setTypeface(fontUtils.getClanaproNarrBook());
            tv_date_time.setTypeface(fontUtils.getClanaproNarrBook());

        }
    }
}
