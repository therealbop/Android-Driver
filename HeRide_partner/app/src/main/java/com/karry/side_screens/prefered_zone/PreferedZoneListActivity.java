package com.karry.side_screens.prefered_zone;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.karry.adapter.PreferenceZoneListAdapter;
import com.heride.partner.R;
import com.karry.pojo.AreaZone.AreaZone;
import com.karry.pojo.AreaZone.AreaZoneDataZones;
import com.karry.utility.AppTypeFace;
import com.karry.utility.DialogHelper;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class PreferedZoneListActivity extends DaggerAppCompatActivity implements PreferedZoneListContract.PreferedZoneView {

    @Inject
    AppTypeFace appTypeFace;
    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_logout) TextView tv_reset;
    @BindView(R.id.rv_preferencezone_list) RecyclerView rv_preferencezone_list;
    @BindView(R.id.tv_selection) TextView tv_selection;
    @BindString(R.string.Pref_zone) String title;
    @BindString(R.string.emptyZone) String emptyZone;
    @BindString(R.string.reset) String reset;
    @Inject PreferedZoneListContract.PreferedZonePresenter preferedZonePresenter;
    @BindString(R.string.ok) String ok;
    @BindString(R.string.message) String message;
    @Inject
    DialogHelper dialogHelper;
    @Inject
    PreferenceZoneListAdapter preferenceZoneListAdapter;

    @BindView(R.id.progressBar) ProgressBar progressBar;

    public static int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefered_zone);

        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        ButterKnife.bind(this);
        preferedZonePresenter.setActionBar();

        dialogHelper.getDialogPreferenceSelectCallbackHelper(new DialogHelper.DialogPreferenceSelect() {
            @Override
            public void oneSelection() {
                preferedZonePresenter.patchAreaZone("1");
            }

            @Override
            public void multileSelection() {
                preferedZonePresenter.patchAreaZone("n");
            }

            @Override
            public void deletePreferences() {
                preferedZonePresenter.deleteMasterPreferedareazone();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        /*preferedZonePresenter.checkPreferenceCheck(selectedPosition);*/
    }

    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
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
        tv_reset.setTypeface(appTypeFace.getPro_News());
        tv_reset.setText(reset);
        preferedZonePresenter.setActionBarTitle();

    }

    @Override
    public void setTitle() {
        tv_title.setText(title);
    }

    @Override
    public void goToLogin(String errMsg) {
        DialogHelper.customAlertDialogSignupSuccess(this,message,errMsg,ok);
    }

    @Override
    public void zoneListForAdapter(AreaZone areaZone) {

        rv_preferencezone_list.setLayoutManager(new LinearLayoutManager(this));
        rv_preferencezone_list.setAdapter(preferenceZoneListAdapter);
        preferenceZoneListAdapter.setData(areaZone.getData().getAreaZones());
        preferenceZoneListAdapter.notifyDataSetChanged();


        preferenceZoneListAdapter.getNotifyPreferneceCheck(new PreferenceZoneListAdapter.NotifyPreferneceCheck() {
            @Override
            public void preferenceChange(AreaZoneDataZones preferencesList, int position) {
                /*Intent intent = new Intent(PreferedZoneListActivity.this, PreferedZoneSelectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(VariableConstant.PREFERENCE_ZONE,preferencesList);
                intent.putExtras(bundle);
                startActivity(intent);*/


                preferedZonePresenter.checkPreferenceCheck(preferencesList, position);

               /* areaZone.getData().getAreaZones().get(position).setPrefredZone(preferencesList.isPrefredZone());
                preferenceZoneListAdapter.setData(areaZone.getData().getAreaZones());
                preferenceZoneListAdapter.notifyDataSetChanged();*/


            }
        });

    }

    @Override
    public void areaZoneRefresh(AreaZone areaZone) {
        preferenceZoneListAdapter.setData(areaZone.getData().getAreaZones());
        /*preferenceZoneListAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void preferenceZoneSeletionSuccess() {
        finish();
    }

    @Override
    public void zoneSelected() {
        DialogHelper.alertForPreferenceZone(this,appTypeFace, VariableConstant.SELECT_PREFERED_ZONE_SELECT);
    }

    @Override
    public void emptyZoneSelected() {
        Utility.BlueToast(this,emptyZone);
    }

    @Override
    public void enableReset() {
        tv_reset.setVisibility(View.VISIBLE);
    }

    @Override
    public void disableReset() {
        tv_reset.setVisibility(View.GONE);
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

    @OnClick({R.id.tv_selection, R.id.tv_logout})
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.tv_selection:
                preferedZonePresenter.validatePreferenceZone();
                break;

            case R.id.tv_logout:
                DialogHelper.alertForPreferenceZone(this,appTypeFace, VariableConstant.SELECT_PREFERED_ZONE_DELETE);
                break;

        }
    }
}
