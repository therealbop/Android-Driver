package com.karry.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heride.partner.R;
import com.karry.pojo.bookingAssigned.TowtrayServiceSelectData;

import java.util.ArrayList;

import static com.karry.utility.VariableConstant.SERVICELIST;


public class MultipleSelectionServiceRVA extends RecyclerView.Adapter {

    private final Typeface ClanaproNarrNews;
    private ArrayList data;
    private Activity context;
    private String type;
    private ArrayList<TowtrayServiceSelectData> towtrayServiceSelectData;

    private callBackForServiceSelect callBackForServiceSelect;

    public void getCallBackForServiceSelect(callBackForServiceSelect callBackForServiceSelect){
        this.callBackForServiceSelect = callBackForServiceSelect;
    }


    public MultipleSelectionServiceRVA(Activity context, ArrayList data, String type) {
        this.context = context;
        this.data = data;
        this.type = type;
        ClanaproNarrNews = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrNews.otf");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewsd = LayoutInflater.from(context).inflate(R.layout.single_select_operator, parent, false);
        return new MyViewHolder(viewsd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            final MyViewHolder viewHolder = (MyViewHolder) holder;

            if(data.size()>0 && data.size()-1==position){
                viewHolder.view1.setVisibility(View.GONE);
            }

            switch (type){

                case SERVICELIST:
                    towtrayServiceSelectData = data;
                    viewHolder.operator.setText(towtrayServiceSelectData.get(position).getTitle());
                    if (towtrayServiceSelectData.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;
            }


            viewHolder.operator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(type.equals(SERVICELIST)){
                        if(towtrayServiceSelectData.get(position).isSelected()) {
                            viewHolder.operator.setChecked(false);
                            towtrayServiceSelectData.get(position).setSelected(false);
                        }
                        else {
                            viewHolder.operator.setChecked(true);
                            towtrayServiceSelectData.get(position).setSelected(true);
                        }
                        notifyDataSetChanged();
                        callBackForServiceSelect.ServiceSelected(towtrayServiceSelectData);
                        //((RideBookingActivity) context).sendResult(towtrayServiceSelectData, type);
                    }

                }
            });




        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    private class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatRadioButton operator;
        View view1;

        MyViewHolder(View itemView) {
            super(itemView);
            operator = itemView.findViewById(R.id.rb_single_operator);
            view1 = itemView.findViewById(R.id.view1);
            operator.setTypeface(ClanaproNarrNews);

        }


    }



    public interface callBackForServiceSelect{

        void ServiceSelected(ArrayList<TowtrayServiceSelectData> towtrayServiceSelectData);
    }
}
