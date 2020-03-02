package com.karry.side_screens.prefered_zone.preferedZoneSelect;


import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;
import com.karry.data.source.local.PreferencesHelper;
import com.karry.network.NetworkService;
import com.heride.partner.R;
import com.karry.pojo.AreaZone.AreaZoneDataZones;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class PreferedZoneSelectPresenter implements PreferedZoneSelectContract.PreferedZoneSelectPresenter{

    @Inject   PreferedZoneSelectContract.PreferedZoneSelectView preferedZoneView;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject   Context context;
    @Inject
    NetworkService networkService;
    private ArrayList<List<LatLng>> Polygon = new ArrayList<>();
    AreaZoneDataZones areaZoneDataZones;

    @Inject
    PreferedZoneSelectPresenter(){

    }

    @Override
    public void setActionBar() {
        preferedZoneView.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        /*preferedZoneView.setTitle();*/

    }

    @Override
    public void checkData(Bundle mBundle) {
        areaZoneDataZones = (AreaZoneDataZones) mBundle.getSerializable(VariableConstant.PREFERENCE_ZONE);
        Utility.printLog("the size is :  "+ areaZoneDataZones.getTitle());
        if(areaZoneDataZones.isPrefredZone()) {
            preferedZoneView.setTitle(areaZoneDataZones.getTitle(), context.getResources().getString(R.string.un_select));
        }else {
            preferedZoneView.setTitle(areaZoneDataZones.getTitle(), context.getResources().getString(R.string.select));
        }
        List<LatLng> polygonPath  = new ArrayList<>();
        Double lat_total=0.0;
        Double lng_total=0.0;

        for(int j=0;j<areaZoneDataZones.getPaths().size();j++)
        {
            Utility.printLog("the zone latlng points are :j:  "+Double.parseDouble(areaZoneDataZones.getPaths().get(j).getLat()));
            Utility.printLog("the zone latlng points are :j:  "+Double.parseDouble(areaZoneDataZones.getPaths().get(j).getLng()));
            polygonPath.add(new LatLng(Double.parseDouble(areaZoneDataZones.getPaths().get(j).getLat()),
                    Double.parseDouble(areaZoneDataZones.getPaths().get(j).getLng())));

            lat_total+=Double.parseDouble(areaZoneDataZones.getPaths().get(j).getLat());
            lng_total+=Double.parseDouble(areaZoneDataZones.getPaths().get(j).getLng());


        }
        Double lat_avg = lat_total/areaZoneDataZones.getPaths().size();
        Double lnt_avg = lng_total/areaZoneDataZones.getPaths().size();

        Polygon.add(polygonPath);
        countPolygonPoints(Polygon.get(0),areaZoneDataZones.getSurgePrice(),null,new LatLng(lat_avg,lnt_avg));
        /*preferedZoneView.setCurrentLocation(new LatLng(lat_avg,lnt_avg));*/

    }


    /**
     * <h1>countPolygonPoints</h1>
     * @param arrayPoints :latlng array for polygon
     */
    private void countPolygonPoints(List<LatLng> arrayPoints,String serge, LatLngBounds latLngBounds, LatLng latLng)
    {

        Utility.printLog("the zone latlng points are : "+arrayPoints);
        if (arrayPoints.size() >= 3)
        {
            PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.addAll(arrayPoints);
            polygonOptions.strokeColor(context.getResources().getColor(R.color.zoneBorder));
            polygonOptions.strokeWidth(5);

            float sergeValue =Float.parseFloat(serge);

            if(sergeValue==1)
                polygonOptions.fillColor(context.getResources().getColor(R.color.transparent));
            else if(sergeValue>1 && sergeValue<=1.5)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_35));
            else if(sergeValue>1.5 && sergeValue<=2)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_40));
            else if(sergeValue>2 && sergeValue<=2.5)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_45));
            else if(sergeValue>2.5 && sergeValue<=3)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_50));
            else if(sergeValue>3 && sergeValue<=3.5)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_55));
            else if(sergeValue>3.5 && sergeValue<=4)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_60));
            else if(sergeValue>4 && sergeValue<=4.5)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_65));
            else if(sergeValue>4.5 && sergeValue<=5)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_70));
            else if(sergeValue>5)
                polygonOptions.fillColor(context.getResources().getColor(R.color.zonefill_75));

            preferedZoneView.drawAreaZones(polygonOptions,latLngBounds,latLng,areaZoneDataZones);
        }
    }

    @Override
    public void checkCurrentLocation() {
        /*LatLng latLng = new LatLng(preferencesHelper.getCurrLatitude(),preferencesHelper.getCurrLongitude());
        preferedZoneView.setCurrentLocation(latLng);*/
    }
}
