package com.karry.side_screens.help_center.zendeskHelpIndex;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.data.source.sqlite.SQLiteDataSource;
import com.karry.manager.mqtt_manager.MQTTManager;
import com.karry.network.NetworkService;
import com.heride.partner.R;
import com.karry.side_screens.help_center.zendeskTicketDetails.HelpIndexTicketDetails;
import com.karry.side_screens.help_center.zendeskadapter.HelpIndexAdapter;
import com.karry.side_screens.help_center.zendeskpojo.OpenClose;
import com.karry.utility.AppTypeFace;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class ZendeskHelpIndex extends DaggerAppCompatActivity implements
        ZendeskHelpIndexContract.ZendeskView
{
    @Inject
    AppTypeFace appTypeface;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    SQLiteDataSource addressDataSource;
    @Inject
    MQTTManager mqttManager;
    @Inject
    NetworkService networkService;

    @Inject
    HelpIndexAdapter helpIndexAdapter;
    @Inject
    ZendeskHelpIndexContract.Presenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_chat_with_us) TextView tv_chat_with_us;
    @BindView(R.id.rlHelpIndex)RelativeLayout rlHelpIndex;
    @BindView(R.id.recyclerHelpIndex)RecyclerView recyclerHelpIndex;
    @BindView(R.id.progressbarHelpIndex)ProgressBar progressbarHelpIndex;
    @BindDrawable(R.drawable.back_white_btn) Drawable back_white_btn;
    @BindString(R.string.helpcenter) String helpcenter;

    private ArrayList<OpenClose> openCloses = new ArrayList<>();
    private int closeSize,openSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_index);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        ButterKnife.bind(this);
        initializeToolBar();
        initializeView();
    }

    private void initializeView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerHelpIndex.setLayoutManager(layoutManager);
        helpIndexAdapter.onCreateIndex(this,openCloses,appTypeface,preferenceHelperDataSource);
        recyclerHelpIndex.setAdapter(helpIndexAdapter);
        showProgress();
        presenter.onToGetZendeskTicket();
    }

    /*
    initialize toolBar
     */
    private void initializeToolBar()
    {

        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(back_white_btn);
        }
        tv_title.setTypeface(appTypeface.getPro_narMedium());
        tv_chat_with_us.setTypeface(appTypeface.getPro_narMedium());
        tv_title.setText(helpcenter);

        ImageView tvAddNewTicket = findViewById(R.id.ivFilter);
        tvAddNewTicket.setVisibility(View.VISIBLE);
        tvAddNewTicket.setImageDrawable(getResources().getDrawable(R.drawable.add));

        tvAddNewTicket.setOnClickListener(view -> {
            Intent intent = new Intent(ZendeskHelpIndex.this, HelpIndexTicketDetails.class);
            intent.putExtra("ISTOAddTICKET",true);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up,R.anim.stay_still);
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onError(String error)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(this.getString(R.string.alert));
        alertDialog.setMessage(this.getString(R.string.sessionExp));
        alertDialog.setNegativeButton(R.string.ok,
                (dialog, which) -> {
//                    ExpireSession.refreshApplication(this,mqttManager, preferenceHelperDataSource, addressDataSource);
                });
        alertDialog.show();

    }

    @Override
    public void hideChat() {
        tv_chat_with_us.setVisibility(View.GONE);
    }


    @Override
    public void onGetTicketSuccess() {

    }

    @Override
    public void onEmptyTicket()
    {
        rlHelpIndex.setVisibility(View.VISIBLE);
        recyclerHelpIndex.setVisibility(View.GONE);
        /*tv_chat_with_us.setVisibility(View.VISIBLE);*/
    }





    @Override
    public void onTicketStatus(OpenClose openClose, int openCloseSize, boolean isOpenClose)
    {
        if(isOpenClose)
            openSize = openCloseSize;
        else
            closeSize = openCloseSize;

        helpIndexAdapter.openCloseSize(openSize,closeSize);
        openCloses.add(openClose);
    }

    @Override
    public void onNotifyData() {
        rlHelpIndex.setVisibility(View.GONE);
        recyclerHelpIndex.setVisibility(View.VISIBLE);
        helpIndexAdapter.notifyDataSetChanged();
    }


    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressbarHelpIndex.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressbarHelpIndex.setVisibility(View.GONE);
    }


    @OnClick({R.id.tv_chat_with_us})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.tv_chat_with_us:
                Intent zendesk = new Intent(this, ZopimChatActivity.class);
                startActivity(zendesk);
                break;

        }
    }
}
