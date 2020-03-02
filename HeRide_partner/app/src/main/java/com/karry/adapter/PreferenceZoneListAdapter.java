package com.karry.adapter;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.heride.partner.R;
import com.karry.pojo.AreaZone.AreaZoneDataZones;
import com.karry.utility.AppTypeFace;
import com.karry.utility.Utility;

import java.util.ArrayList;

import javax.inject.Inject;


public class PreferenceZoneListAdapter extends RecyclerView.Adapter<PreferenceZoneListAdapter.ViewHolder> {

    private ArrayList<AreaZoneDataZones> data=new ArrayList<>();
    private AppTypeFace appTypeFace;



    private NotifyPreferneceCheck notifyPreferneceCheck;


    @Inject
    public PreferenceZoneListAdapter(Context context, AppTypeFace appTypeFace) {
        this.appTypeFace = appTypeFace;
    }


    public void setData(ArrayList<AreaZoneDataZones> data) {
        this.data.clear();
        this.data = data;
    }

    public void getNotifyPreferneceCheck(NotifyPreferneceCheck notifyPreferneceCheck){
        this.notifyPreferneceCheck = notifyPreferneceCheck;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cb_zone_name;
        private RelativeLayout rl_item;
        public ViewHolder(View itemView) {
            super(itemView);
            cb_zone_name = itemView.findViewById(R.id.cb_zone_name);
            rl_item = itemView.findViewById(R.id.rl_item);

        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_select_preference_zone, parent, false);
        return new PreferenceZoneListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cb_zone_name.setText(data.get(position).getTitle());
        holder.cb_zone_name.setTypeface(appTypeFace.getPro_News());

        if(data.get(position).isPrefredZone()){
            holder.cb_zone_name.setChecked(true);
        }else {
            holder.cb_zone_name.setChecked(false);
        }




       /* holder.rl_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    data.get(position).setPrefredZone(true);
                    notifyPreferneceCheck.preferenceChange(data.get(position), position);
                }else {
                    data.get(position).setPrefredZone(false);
                    notifyPreferneceCheck.preferenceChange(data.get(position), position);
                }

            }
        });*/


        holder.cb_zone_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.cb_zone_name.isChecked()){
                    data.get(position).setPrefredZone(true);
                    notifyPreferneceCheck.preferenceChange(data.get(position), position);
                }else {
                    data.get(position).setPrefredZone(false);
                    notifyPreferneceCheck.preferenceChange(data.get(position), position);
                }

            }
        });



        /*holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.get(position).setPosition(position);
                notifyPreferneceCheck.preferenceChange(data.get(position));
            }
        });*/


    }

    @Override
    public int getItemCount() {
        Utility.printLog("the content size is : "+data.size());
        return data.size();
    }

    public interface NotifyPreferneceCheck{
        void preferenceChange(AreaZoneDataZones preferencesList, int position);
    }
}


