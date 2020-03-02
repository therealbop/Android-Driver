package com.karry.side_screens.prefered_zone.preferedZoneSelect;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;
import com.heride.partner.R;
import com.karry.pojo.AreaZone.AreaZoneDataZones;
import com.karry.side_screens.prefered_zone.PreferedZoneListActivity;
import com.karry.utility.AppTypeFace;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class PreferedZoneSelectActivity extends DaggerAppCompatActivity implements
        PreferedZoneSelectContract.PreferedZoneSelectView, OnMapReadyCallback {


    @Inject
    AppTypeFace appTypeFace;
    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_selection) TextView tv_selection;
    private GoogleMap googleMap;
    private PolygonOptions polygonOptions;
    private LatLngBounds latLngBounds;
    private LatLng latLng;
    AreaZoneDataZones areaZoneDataZones;

    @Inject PreferedZoneSelectContract.PreferedZoneSelectPresenter preferedZoneSelectPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefered_zone_select);

        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        ButterKnife.bind(this);
        initializeMap();
        preferedZoneSelectPresenter.checkData(getIntent().getExtras());
        preferedZoneSelectPresenter.setActionBar();
    }

    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white_btn);
        }
        tv_title.setTypeface(appTypeFace.getPro_narMedium());
        tv_selection.setTypeface(appTypeFace.getPro_narMedium());
        preferedZoneSelectPresenter.setActionBarTitle();
    }

    @Override
    public void setTitle(String heading_title, String bottom_title) {
        tv_title.setText(heading_title);
        tv_selection.setText(bottom_title);
    }

    @Override
    public void drawAreaZones(PolygonOptions polygonOptions, LatLngBounds latLngBounds,
                              LatLng latLng,AreaZoneDataZones areaZoneDataZones) {
        this.polygonOptions = polygonOptions;
        this.latLng = latLng;
        this.areaZoneDataZones = areaZoneDataZones;
    }

    /**********************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    /**
     *<h1>initializeMap</h1>
     * <p>initialization of Map</p>
     */
    private void initializeMap()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.map);
        SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
        supportmapfragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (this.googleMap == null)
            return;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                !=  PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }


        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.googleMap.getUiSettings().setTiltGesturesEnabled(true);
        this.googleMap.setMyLocationEnabled(false);

        if(polygonOptions!=null) {
            googleMap.addPolygon(polygonOptions);
        }else {
            preferedZoneSelectPresenter.checkData(getIntent().getExtras());
            googleMap.addPolygon(polygonOptions);
        }

        if(latLng!=null)
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            googleMap.getUiSettings().setZoomControlsEnabled(false);
        }

        /*LatLngBounds latLngBounds = new LatLngBounds(southWestLocation, northEastLocation);
        // Obtain a movement description object
        // offset from edges of the map in pixels
        int padding = 220;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding);
        // Move the map
        googleMap.moveCamera(cu);*/


    }



    @Override
    public void setCurrentLocation(LatLng latLng) {
        if(latLng!=null)
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            googleMap.getUiSettings().setZoomControlsEnabled(false);
        }
    }

    @OnClick({R.id.tv_selection})
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.tv_selection:
                PreferedZoneListActivity.selectedPosition = areaZoneDataZones.getPosition();
                onBackPressed();
                break;

        }
    }

}
