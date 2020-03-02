package com.karry.authentication.vehicleTypeList;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.karry.data.source.local.PreferencesHelper;
import com.heride.partner.R;
import com.karry.pojo.VehicleTypeList;
import com.karry.utility.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;


public class VehicleTypeListRCA extends RecyclerView.Adapter {

    private ArrayList<VehicleTypeList> vDataList;
    @Inject Context context;
    private PreferencesHelper preferencesHelper;
    private selectVehicleType selectVehicleType;


    /**********************************************************************************************/
    @Inject
    VehicleTypeListRCA(Context context) {
        this.context = context;
        preferencesHelper = new PreferencesHelper(context);
    }

    public void setData(ArrayList<VehicleTypeList> data){
        vDataList=data;
    }

    public void getSelectVehicleType(selectVehicleType  selectVehicleType){
        this.selectVehicleType = selectVehicleType;
    }

    /**********************************************************************************************/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_select_vechicletype, parent, false);
        return new VehicleViewHolder(view);
    }

    /**********************************************************************************************/
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VehicleViewHolder) {
            VehicleViewHolder vehicle = (VehicleViewHolder) holder;
            vehicle.tv_veh_type.setText(vDataList.get(position).getVehicleTypeName());

            String currencySymbol;
            if(vDataList.get(position).getCurrencyAbbr().matches("1")){
                currencySymbol = vDataList.get(position).getCurrencySymbol().concat(" ");
                vehicle.tv_milege_fare.setText(currencySymbol.concat(vDataList.get(position).getMileagePrice().concat("/").concat(vDataList.get(position).getDistanceMetrics())));
                vehicle.tv_minimumfare.setText(currencySymbol.concat(vDataList.get(position).getMinimumFare()));

            }else {
                currencySymbol = " ".concat(vDataList.get(position).getCurrencySymbol());
                vehicle.tv_milege_fare.setText(vDataList.get(position).getMileagePrice().concat("/").concat(vDataList.get(position).getDistanceMetrics()).concat(currencySymbol));
                vehicle.tv_minimumfare.setText(vDataList.get(position).getMinimumFare().concat(currencySymbol));
            }

            if(vDataList.get(position).getVehicleImgOn()!=null) {
                Picasso.get()
                        .load(vDataList.get(position).getVehicleImgOn())
                        .placeholder(R.drawable.signup_vechicle_default_image)
                        .transform(new CircleTransformation())
                        .into(vehicle.iv_vehicle);
            }

            if(vDataList.get(position).isDefaultVehicleType()) {
                vehicle.cb_vehicle_type.setChecked(true);
                selectVehicleType.vehicleTypeAdd(position);
            }

            vehicle.cb_vehicle_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(vehicle.cb_vehicle_type.isChecked() && !vDataList.get(position).isDefaultVehicleType()){
                        selectVehicleType.vehicleTypeAdd(position);
                    }else if(!vDataList.get(position).isDefaultVehicleType()){
                        selectVehicleType.vehicleTypeRemove(position);
                    }

                    if(vDataList.get(position).isDefaultVehicleType()){
                        vehicle.cb_vehicle_type.setChecked(true);
                    }
                }
            });
            
        }

    }


    /**********************************************************************************************/
    @Override
    public int getItemCount() {
        return vDataList.size();
    }

    /**********************************************************************************************/
    public class VehicleViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_vehicle;
        private TextView tv_veh_type,tv_milege_fare_head,tv_milege_fare,tv_minimumfare_head,tv_minimumfare;
        private CheckBox cb_vehicle_type;

        VehicleViewHolder(View itemView) {
            super(itemView);

            Typeface ClanaproNarrMedium = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrMedium.otf");
            Typeface ClanaproNarrNews = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrNews.otf");

            tv_veh_type = itemView.findViewById(R.id.tv_veh_type);
            tv_veh_type.setTypeface(ClanaproNarrMedium);

            tv_milege_fare_head = itemView.findViewById(R.id.tv_milege_fare_head);
            tv_milege_fare_head.setTypeface(ClanaproNarrNews);

            tv_milege_fare = itemView.findViewById(R.id.tv_milege_fare);
            tv_milege_fare.setTypeface(ClanaproNarrNews);

            tv_minimumfare_head = itemView.findViewById(R.id.tv_minimumfare_head);
            tv_minimumfare_head.setTypeface(ClanaproNarrNews);

            tv_minimumfare = itemView.findViewById(R.id.tv_minimumfare);
            tv_minimumfare.setTypeface(ClanaproNarrNews);

            cb_vehicle_type = itemView.findViewById(R.id.cb_vehicle_type);
            iv_vehicle = itemView.findViewById(R.id.iv_vehicle);

        }

    }


    public interface selectVehicleType{
        void vehicleTypeAdd(int position);
        void vehicleTypeRemove(int position);
    }
}
