package com.karry.authentication.vehiclelist;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.karry.data.source.local.PreferencesHelper;
import com.heride.partner.R;
import com.karry.authentication.login.model.VehiclesDetails;
import com.karry.utility.SessionManager;
import com.karry.utility.VariableConstant;

import java.util.ArrayList;

import javax.inject.Inject;



public class VehicleListRCA extends RecyclerView.Adapter {

    private ArrayList<VehiclesDetails> vDataList;
    @Inject Context context;
    private PreferencesHelper preferencesHelper;


    /**********************************************************************************************/
    @Inject
    VehicleListRCA(Context context) {
        this.context = context;
        preferencesHelper = new PreferencesHelper(context);
    }

    public void setData(ArrayList<VehiclesDetails> data){
        vDataList=data;
    }

    /**********************************************************************************************/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_select_vechicle, parent, false);
        return new VehicleViewHolder(view);
    }

    /**********************************************************************************************/
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VehicleViewHolder) {

            VehicleViewHolder vehicle = (VehicleViewHolder) holder;
            vehicle.tv_plateno_value.setText(vDataList.get(position).getPlateNo());
            vehicle.tv_type_value.setText(vDataList.get(position).getVehicleType());
            vehicle.tv_model_value.setText(vDataList.get(position).getVehicleModel());
            /*buttons.add(holder.rb_selectveh);
            views.add(holder.view_green);*/
            final int index = holder.getAdapterPosition();

            if (vDataList.get(position).isSelected()) {
                vehicle.rb_selectveh.setChecked(true);
                vehicle.view_green.setBackgroundResource(R.drawable.single_sel_veh_leftgreen);

            } else {
                vehicle.rb_selectveh.setChecked(false);
                vehicle.view_green.setBackgroundResource(R.drawable.single_sel_veh_leftgray);
            }


            vehicle.ll_vechicle.setOnClickListener(v -> onVehicleSelect(position));
            vehicle.rb_selectveh.setOnClickListener(v -> onVehicleSelect(position));
        }

    }
    private void onVehicleSelect(int index){
        resetAll();
        vDataList.get(index).setSelected(true);
        SessionManager.getSessionManager(context).setTypeId(vDataList.get(index).getTypeId());
        VehicleListRCA.this.notifyDataSetChanged();
        VariableConstant.VECHICLESELECTED = true;
        VariableConstant.VECHICLEID = vDataList.get(index).getId();
        VariableConstant.VECHICLE_TYPE_ID = vDataList.get(index).getTypeId();
        if(vDataList.get(index).getVehicleMapIcon()!=null)
            preferencesHelper.setCarIcon(vDataList.get(index).getVehicleMapIcon());
            preferencesHelper.setSelectedVehicle(vDataList.get(index));
    }

    private void resetAll() {
        for (int i = 0; i < vDataList.size(); i++) {
            vDataList.get(i).setSelected(false);

        }
    }

    /**********************************************************************************************/
    @Override
    public int getItemCount() {
        return vDataList.size();
    }

    /**********************************************************************************************/
    public class VehicleViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_vechicle;
        private TextView tv_plateno_vl, tv_type_vl, tv_model_vl;
        private TextView tv_plateno_value, tv_type_value, tv_model_value;
        private RadioButton rb_selectveh;
        private View view_green;

        VehicleViewHolder(View itemView) {
            super(itemView);

            Typeface ClanaproNarrMedium = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrMedium.otf");
            Typeface ClanaproNarrNews = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrNews.otf");

            ll_vechicle = itemView.findViewById(R.id.ll_vechicle);

            tv_plateno_vl = itemView.findViewById(R.id.tv_plateno_vl);
            tv_plateno_vl.setTypeface(ClanaproNarrNews);

            tv_type_vl = itemView.findViewById(R.id.tv_type_vl);
            tv_type_vl.setTypeface(ClanaproNarrNews);

            tv_model_vl = itemView.findViewById(R.id.tv_model_vl);
            tv_model_vl.setTypeface(ClanaproNarrNews);

            tv_plateno_value = itemView.findViewById(R.id.tv_plateno_value);
            tv_plateno_value.setTypeface(ClanaproNarrMedium);

            tv_type_value = itemView.findViewById(R.id.tv_type_value);
            tv_type_value.setTypeface(ClanaproNarrMedium);

            tv_model_value = itemView.findViewById(R.id.tv_model_value);
            tv_model_value.setTypeface(ClanaproNarrMedium);

            view_green = itemView.findViewById(R.id.view_green);

            rb_selectveh = itemView.findViewById(R.id.rb_selectveh);

        }

    }
}
