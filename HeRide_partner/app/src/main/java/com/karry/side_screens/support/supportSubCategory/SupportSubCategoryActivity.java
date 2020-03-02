package com.karry.side_screens.support.supportSubCategory;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.karry.adapter.SupportRVA;
import com.heride.partner.R;
import com.karry.pojo.SupportData;
import com.karry.utility.AppTypeFace;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class SupportSubCategoryActivity extends DaggerAppCompatActivity
        implements SupportSubCatContract.SupportSubCatView{

    @Inject SupportSubCatContract.SupportSubCatPresenter supportSubCatPresenter;
    @Inject AppTypeFace appTypeFace;
    @Inject SupportRVA supportRVA;

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.toolbar) Toolbar toolbar;
    private String title;

    @BindView(R.id.rvSupportSubCateogry) RecyclerView  rvSupportSubCateogry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_sub_category);
        ButterKnife.bind(this);
        initView();

        supportSubCatPresenter.setActionBar();
    }

    private void initView() {

        Intent intent = getIntent();
        ArrayList<SupportData> supportDatas = new ArrayList<>();
        supportDatas.addAll((Collection<? extends SupportData>) intent.getSerializableExtra("data"));

        title = intent.getStringExtra("title");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        rvSupportSubCateogry.setLayoutManager(linearLayoutManager);
        supportRVA.setSupportData(supportDatas,this);
        rvSupportSubCateogry.setAdapter(supportRVA);
        supportRVA.notifyDataSetChanged();
    }

    /**********************************************************************************************/
    @Override
    public void initActionBar() {

        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white_btn);
        }
        tv_title.setTypeface(appTypeFace.getPro_narMedium());
        supportSubCatPresenter.setActionBarTitle();

    }

    @Override
    public void setTitle() {
        tv_title.setText(title);
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
}
