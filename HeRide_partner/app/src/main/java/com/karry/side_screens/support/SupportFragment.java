package com.karry.side_screens.support;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.karry.adapter.SupportRVA;
import com.karry.app.mainActivity.MainActivity;
import com.karry.dagger.ActivityScoped;
import com.heride.partner.R;
import com.karry.pojo.SupportData;
import com.karry.utility.Utility;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

/**
 *<h1>SupportFragment</h1>
 *<p>the class which is the fragment for Support.</p>
 */
@ActivityScoped
public class SupportFragment extends DaggerFragment implements
        SupportFragmentContract.SupportFragView {

    @BindView(R.id.rvSupport) RecyclerView rvSupport;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @Inject SupportFragmentContract.SupportFragPresenter supportFragPresenter;
    @Inject SupportRVA supportRVA;
    private Unbinder unbinder;

    private String TAG = "SupportFragment";
    private ArrayList<SupportData> supportDatas;



    @Inject
    public SupportFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.support_fragment, container, false);

        unbinder = ButterKnife.bind(this,rootView);
        supportFragPresenter.attachView(this);
        initLayout();
        supportFragPresenter.callSupportApi();
        ((MainActivity)getActivity()).setFragmentRefreshListener(this::onResume);
        return rootView;
    }


    /**
     * <h1>initLayout</h1>
     * <p>initlize the view</p>
     */
    public void initLayout() {
        rvSupport.setLayoutManager(new LinearLayoutManager(getActivity()));
        supportDatas = new ArrayList<>();
        supportRVA.setSupportData(supportDatas,getActivity());
        rvSupport.setAdapter(supportRVA);
    }


    @Override
    public void onFailure(String msg) {
        Utility.BlueToast(getActivity() ,msg);
    }

    @Override
    public void getSupportDetails(ArrayList<SupportData> supportDatas) {
        this.supportDatas.addAll(supportDatas);
        supportRVA.notifyDataSetChanged();
    }


    /**********************************************************************************************/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        supportFragPresenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        supportFragPresenter.compositeDisposableClear();
    }
}
