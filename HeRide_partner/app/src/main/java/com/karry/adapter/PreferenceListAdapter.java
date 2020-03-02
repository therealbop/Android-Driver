package com.karry.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.pojo.Signup.PreferencesList;
import com.karry.utility.AppTypeFace;

import java.util.ArrayList;

import javax.inject.Inject;


public class PreferenceListAdapter extends RecyclerView.Adapter<PreferenceListAdapter.ViewHolder> {

    private ArrayList<PreferencesList> data;
    private Context context;
    private AppTypeFace appTypeFace;

    private TextView tv_Support;
    private SwitchCompat sc_Support;
    private ImageView iv_icon;

    private ArrayList<PreferencesList> preferencesList;
    private int lastCheckedPosition = 0;
    private int from;

    private NotifyPreferneceCheck notifyPreferneceCheck;


    @Inject
    public PreferenceListAdapter(Context context,AppTypeFace appTypeFace) {
        this.context = context;
        this.appTypeFace = appTypeFace;
    }

    //selection=>0 -- from signUp
    //selection=>1 -- from profile
    public void setData(ArrayList<PreferencesList> data, int from) {
        this.data = data;
        this.from = from;
    }

    public void getNotifyPreferneceCheck(NotifyPreferneceCheck notifyPreferneceCheck){
        this.notifyPreferneceCheck = notifyPreferneceCheck;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            tv_Support = itemView.findViewById(R.id.tv_Support);
            sc_Support = itemView.findViewById(R.id.sc_Support);
            iv_icon = itemView.findViewById(R.id.iv_icon);

        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_select_preference, parent, false);
        return new PreferenceListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        tv_Support.setText(data.get(position).getName());


        switch (from){
            case 0:
                iv_icon.setVisibility(View.GONE);
                tv_Support.setTypeface(appTypeFace.getPro_narMedium());
                sc_Support.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b)
                            data.get(position).setSelected(true);
                        else
                            data.get(position).setSelected(false);

                        notifyPreferneceCheck.preferenceChange(data);
                    }
                });
                break;
            case 1:
                tv_Support.setTypeface(appTypeFace.getPro_News());
                tv_Support.setTextColor(context.getResources().getColor(R.color.text_color));

                /*Picasso.get()
                        .load(data.get(position).getIcon())
                        .placeholder(R.drawable.svg_checkbox)//signup_profile_default_image)
                        .error(R.drawable.svg_checkbox);*///signup_profile_default_image)

                sc_Support.setVisibility(View.GONE);
                break;

        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface NotifyPreferneceCheck{
        void preferenceChange(ArrayList<PreferencesList> preferencesList);
    }
}


