package com.karry.side_screens.help_center.zendeskTicketDetails;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.karry.data.source.local.PreferenceHelperDataSource;
import com.heride.partner.R;
import com.karry.side_screens.help_center.zendeskadapter.HelpIndexRecyclerAdapter;
import com.karry.side_screens.help_center.zendeskadapter.SpinnerAdapter;
import com.karry.side_screens.help_center.zendeskpojo.SpinnerRowItem;
import com.karry.side_screens.help_center.zendeskpojo.ZendeskDataEvent;
import com.karry.utility.AppTypeFace;
import com.karry.utility.Utility;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h>HelpIndexTicketDetails</h>
 * Created by Ali on 2/26/2018.
 */

public class HelpIndexTicketDetails extends DaggerAppCompatActivity implements HelpIndexContract.HelpView
{
    public static final Integer[] priorityColor = {
            R.color.red_login_dark,
            R.color.saffron,
            R.color.green_continue ,
            R.color.livemblue3498
    };
    //private String[] priorityTitles;


    @Inject
    AppTypeFace appTypeface;

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject HelpIndexContract.presenter  presenter;
    @Inject
    HelpIndexRecyclerAdapter helpIndexRecyclerAdapter;

    @BindArray(R.array.ticketPriority)String[] priorityTitles;
    @BindView(R.id.etHelpIndexSubjectPre)EditText etHelpIndexSubjectPre;
    @BindView(R.id.etWriteMsg)EditText etWriteMsg;
    @BindView(R.id.etHelpIndexSubject)EditText etHelpIndexSubject;
    @BindView(R.id.tvHelpIndexDateNTimePre)TextView tvHelpIndexDateNTimePre;
    @BindView(R.id.spinnerHelpIndexPre)TextView spinnerHelpIndexPre;
    @BindView(R.id.tvHelpIndexImageText)TextView tvHelpIndexImageText;
    @BindView(R.id.tvHelpIndexCustName)TextView tvHelpIndexCustName;
    @BindView(R.id.tvHelpIndexDateNTime)TextView tvHelpIndexDateNTime;
    @BindView(R.id.ivHelpIndexImage)ImageView ivHelpIndexImage;
    @BindView(R.id.cardHelpIndexTicket)
    CardView cardHelpIndexTicket;
    @BindView(R.id.ivHelpCenterPriority) ImageView ivHelpCenterPriority;
    @BindView(R.id.spinnerHelpIndex) Spinner spinnerHelpIndex;

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.cardHelpIndexTicketPre)
    CardView cardHelpIndexTicketPre;
    @BindView(R.id.tvHelpIndexImageTextPre) TextView tvHelpIndexImageTextPre;
    @BindView(R.id.ivHelpIndexImagePre) ImageView ivHelpIndexImagePre;
    @BindView(R.id.ivHelpCenterPriorityPre) ImageView ivHelpCenterPriorityPre;
    @BindView(R.id.tvHelpIndexCustNamePre) TextView tvHelpIndexCustNamePre;
    @BindView(R.id.tvHelpIndexSend) TextView tvHelpIndexSend;
    @BindView(R.id.rlTextInput) RelativeLayout rlTextInput;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindDrawable(R.drawable.selector_signup_close)  Drawable back_white_btn;

    private String subject,priority;
    private int zenId;
    private ArrayList<SpinnerRowItem> rowItems;
    private boolean isToAddTicket;
    private ArrayList<ZendeskDataEvent> zendeskDataEvents = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_help_index_ticket);
        ButterKnife.bind(this);
        isToAddTicket = getIntent().getBooleanExtra("ISTOAddTICKET",false);
        if(getIntent().getExtras()!=null)
        {
            zenId = getIntent().getIntExtra("ZendeskId",0);
        }
        if(getIntent().getExtras()!=null)
        {
            String str = getIntent().getStringExtra("COMINGFROM");
            Log.i("StringValue", "onCreate: "+str);
            if(str != null && str.equals("FragmentHelp"))
            {
                etHelpIndexSubject.setText(getIntent().getStringExtra("SUBJECT"));
            }
            //
        }

        if(getIntent().getExtras()!=null)
        {
            etHelpIndexSubject.setText(getIntent().getStringExtra("SUBJECT"));
        }
        initializeArrayList();
        initializeToolBar();
        initializeView();

    }

    private void initializeArrayList() {
        rowItems = new ArrayList<>();
        rowItems.add(new SpinnerRowItem(priorityColor[0],priorityTitles[0]));
        rowItems.add(new SpinnerRowItem(priorityColor[1],priorityTitles[1]));
        rowItems.add(new SpinnerRowItem(priorityColor[2],priorityTitles[2]));
        rowItems.add(new SpinnerRowItem(priorityColor[3],priorityTitles[3]));
    }

    private void initializeView()
    {
        if(isToAddTicket)
        {
            etHelpIndexSubject.setTypeface(appTypeface.getPro_News());
            tvHelpIndexDateNTime.setTypeface(appTypeface.getPro_News());
            tvHelpIndexCustName.setTypeface(appTypeface.getPro_News());
            tvHelpIndexImageText.setTypeface(appTypeface.getPro_News());
            priority = rowItems.get(0).getPriority();
            SpinnerAdapter adapter = new SpinnerAdapter(this,R.layout.spinner_adapter,R.id.tvSpinnerPriority, rowItems,appTypeface);
            spinnerHelpIndex.setAdapter(adapter);

            spinnerHelpIndex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("TAG", "onItemSelected: "+i);
                    priority = rowItems.get(i).getPriority();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            cardHelpIndexTicket.setVisibility(View.VISIBLE);
            String name = preferenceHelperDataSource.getMyName();//+" "+sharedPrefs.getLastName();
            //preferenceHelperDataSource.getCustomerEmail()

            tvHelpIndexCustName.setText(name);
            char c = name.charAt(0);
            tvHelpIndexImageText.setText(c+"");
            Date date = new Date(System.currentTimeMillis());
            String dateTime[] = Utility.getFormattedDate(date,preferenceHelperDataSource).split(",");
            String timeToSet =  dateTime[0]+" | "+dateTime[1];
            tvHelpIndexDateNTime.setText(timeToSet);

        }else
        {
            etHelpIndexSubjectPre.setEnabled(false);
            spinnerHelpIndexPre.setTypeface(appTypeface.getPro_narMedium());
            tvHelpIndexCustNamePre.setTypeface(appTypeface.getPro_News());
            tvHelpIndexImageTextPre.setTypeface(appTypeface.getPro_News());
            etHelpIndexSubjectPre.setTypeface(appTypeface.getPro_News());
            tvHelpIndexDateNTimePre.setTypeface(appTypeface.getPro_News());
            cardHelpIndexTicketPre.setVisibility(View.VISIBLE);
            String name = preferenceHelperDataSource.getMyName();//+" "+sharedPrefs.getLastName();
            tvHelpIndexCustNamePre.setText(name);
            char c = name.charAt(0);
            tvHelpIndexImageTextPre.setText(c+"");
            showProgress();
            presenter.callApiToGetTicketInfo(zenId);
        }

        helpIndexRecyclerAdapter.onHelpIndexRecyclerAdapter(this,zendeskDataEvents,appTypeface,preferenceHelperDataSource);
        RecyclerView recyclerViewHelpIndex = findViewById(R.id.recyclerViewHelpIndex);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewHelpIndex.setLayoutManager(linearLayoutManager);
        recyclerViewHelpIndex.setAdapter(helpIndexRecyclerAdapter);
        etWriteMsg = findViewById(R.id.etWriteMsg);

        etWriteMsg.setTypeface(appTypeface.getPro_News());
        tvHelpIndexSend.setTypeface(appTypeface.getPro_News());
        recyclerViewHelpIndex.setNestedScrollingEnabled(false);

    }

    @OnClick(R.id.tvHelpIndexSend)
    public void msgSendText()
    {
        String trim = etWriteMsg.getText().toString().trim();
        if(!trim.isEmpty())
        {
            if(isToAddTicket)
            {
                subject = etHelpIndexSubject.getText().toString().trim();
                if(!subject.isEmpty())
                {
                    presenter.callApiToCreateTicket(trim,subject,priority,this);
                    // setAndNotifyAdapter(sharedPrefs.getName(),trim);
                    etHelpIndexSubject.setEnabled(false);
                    isToAddTicket = false;
                    hideSoftKeyboard();
                }
                else
                    Toast.makeText(HelpIndexTicketDetails.this,"Please add subject",Toast.LENGTH_SHORT).show();
                etWriteMsg.setText("");

            }
            else
                {
                presenter.callApiToCommentOnTicket(trim,zenId);
                etWriteMsg.setText("");
                // setAndNotifyAdapter(sharedPrefs.getName(),trim);
                hideSoftKeyboard();
            }

        }
    }



    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setAndNotifyAdapter(String name, String trim)
    {
        long timeStsmp = System.currentTimeMillis()/1000;
        ZendeskDataEvent dataEvent = new ZendeskDataEvent();
        dataEvent.setBody(trim);
        dataEvent.setName(name);
        dataEvent.setTimeStamp(timeStsmp);
        zendeskDataEvents.add(dataEvent);
        helpIndexRecyclerAdapter.notifyDataSetChanged();
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

        if(isToAddTicket)
            tv_title.setText(R.string.newTicket);
        else
            tv_title.setText(R.string.ticket);
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
        overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
    }



    @Override
    public void onZendeskTicketAdded(String msg)
    {
        onBackPressed();
    }


    @Override
    public void onError(String errMsg) {
        showProgress();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(this.getString(R.string.alert));
        alertDialog.setMessage(errMsg);
        alertDialog.setNegativeButton(R.string.ok,
                (dialog, which) -> {
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    @Override
    public void success() {
        onBackPressed();
    }

    @Override
    public void onTicketInfoSuccess(ArrayList<ZendeskDataEvent> events, String timeToSet, String subject, String priority, String type) {


        zendeskDataEvents.addAll(events);
        helpIndexRecyclerAdapter.notifyDataSetChanged();
        etHelpIndexSubjectPre.setText(subject);
        tvHelpIndexDateNTimePre.setText(timeToSet);
        this.subject = subject;
        this.priority = priority;
        spinnerHelpIndexPre.setText(priority);
        presenter.onPriorityImage(this,priority,ivHelpCenterPriorityPre);
        if(!"open".equalsIgnoreCase(type))
        {
            rlTextInput.setVisibility(View.GONE);
        }
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
}
