package com.karry.mqttChat;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.utility.AppTypeFace;
import com.karry.utility.Utility;

import java.util.ArrayList;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class ChattingActivity extends DaggerAppCompatActivity implements ChattingContract.ViewOperations,View.OnClickListener{

    private ArrayList<ChatData> chatDataArry=new ArrayList<>();
    private ChattingAdapter cAdapter;
    static public boolean isOpen=false;

    @Inject AppTypeFace appTypeface;
    @BindView(R.id.ivBackArrow) ImageView ivBackArrow ;
    @BindView(R.id.chatProgress) ProgressBar chatProgress;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title ;
    @BindView(R.id.tv_all_tool_bar_title2) TextView tv_all_tool_bar_title2 ;
    @BindView(R.id.tvSend) TextView tvSend ;
    @BindView(R.id.etMsg) EditText etMsg;
    @BindView(R.id.rcvChatMsg) RecyclerView rcvChatMsg;
    public static boolean CHAT_ACTIVITY = false;


    @Inject ChattingContract.PresenterOperations presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        ButterKnife.bind(this);

        presenter.getData(getIntent());
        presenter.setActionBar();
        presenter.initViews();
        initScrollListener();
    }

    @OnClick({R.id.tvSend,R.id.ivBackArrow,R.id.rlBackArrow})
    @Override
    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.tvSend:
                presenter.message(etMsg.getText().toString());
                etMsg.setText("");
                break;

            case R.id.ivBackArrow:
            case R.id.rlBackArrow:
                onBackPressed();
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        CHAT_ACTIVITY=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CHAT_ACTIVITY=false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        finish();
    }


    private void scrollToBottom() {
        rcvChatMsg.scrollToPosition(cAdapter.getItemCount() - 1);
    }

    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        chatProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        chatProgress.setVisibility(View.GONE);
    }

    @Override
    public void setActionBar(String custName)
    {
        tv_all_tool_bar_title.setText(custName);
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_News());

    }

    @Override
    public void setViews(String bid) {

        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title2.setTypeface(appTypeface.getPro_News());
        etMsg.setTypeface(appTypeface.getPro_News());
        tvSend.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title2.setVisibility(View.VISIBLE);
        tv_all_tool_bar_title2.setText(getResources().getString(R.string.bid)+" "+bid);
        presenter.getChattingData(0);
    }

    @Override
    public void setRecyclerView()
    {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        cAdapter = new ChattingAdapter(this,chatDataArry);
        rcvChatMsg.setLayoutManager(mLayoutManager);
        rcvChatMsg.setAdapter(cAdapter);
    }

    @Override
    public void updateData(ArrayList<ChatData> chatDataArryList) {
        /*this.chatDataArry.clear();*/
        this.chatDataArry.addAll(chatDataArryList);
        cAdapter.notifyDataSetChanged();
        scrollToBottom();
    }


    private void initScrollListener() {
        rcvChatMsg.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Utility.printLog("the last index is :"+dx +" , "+dy+" ,"+recyclerView.getChildCount());

            }
        });
    }


}
