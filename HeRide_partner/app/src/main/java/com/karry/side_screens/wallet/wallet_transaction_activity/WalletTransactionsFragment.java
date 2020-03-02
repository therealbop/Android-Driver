package com.karry.side_screens.wallet.wallet_transaction_activity;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.karry.dagger.ActivityScoped;
import com.heride.partner.R;
import com.karry.side_screens.wallet.adapter.WalletTransactionsAdapter;
import com.karry.side_screens.wallet.wallet_transaction_activity.model.TransctionsItem;


import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/**
 * <h1>WalletTransactionsFragment</h1>
 * <p> Fragment to show wallet all transactions list according to fragment instances for the view pager of WalletTransActivity </p>
 */
@ActivityScoped
public class WalletTransactionsFragment extends DaggerFragment
{
    private ArrayList<TransctionsItem> transactionsAL;

//    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvTransactions)  RecyclerView rvTransactions;
    @BindView(R.id.llNoTransactions) LinearLayout llNoTransactions;
    @BindView(R.id.iv_moreData)
    ImageView iv_moreData;
    private WalletTransActivity walletTransActivity;
    private WalletTransactionsAdapter walletTransactionsRVA;
    int pageIndex=0;

    @Inject
    public WalletTransactionsFragment()
    {

    }


    /**
     * <h2>getNewInstance</h2>
     * <p> method to return the instance of this fragment </p>
     */
    public static WalletTransactionsFragment getNewInstance()
    {
        WalletTransactionsFragment walletTransactionsFragment = new WalletTransactionsFragment();
        Bundle args = new Bundle();
        walletTransactionsFragment.setArguments(args);
        return walletTransactionsFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        walletTransActivity = (WalletTransActivity)getActivity();
        transactionsAL=new ArrayList<>();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View  view = inflater.inflate(R.layout.fragment_wallet_tansactions_list, container, false);
        ButterKnife.bind(this,view);
        initViews();
        return view;
    }



    /**
     * <h2>initViews</h2>
     * <p> method to notify adapter or update views if the transactions list size changed </p>
     */
    private void initViews()
    {
        rvTransactions.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvTransactions.setLayoutManager(llm);

        walletTransactionsRVA = new WalletTransactionsAdapter(getActivity(), transactionsAL);
        rvTransactions.setAdapter(walletTransactionsRVA);
        /*swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> walletTransActivity.loadTransactions(false));

      rvTransactions.addOnScrollListener(new RecyclerView.OnScrollListener()
      {
          @Override
          public void onScrollStateChanged(RecyclerView recyclerView, int newState)
          {
              super.onScrollStateChanged(recyclerView, newState);
              LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
              int viewPosition = manager.findLastVisibleItemPosition();
              int itemCount = manager.getItemCount();
              if (itemCount > 0 && viewPosition == itemCount - 2)
              {
                  walletTransActivity.loadTransactions(false);
              }
          }
      });*/


        rvTransactions.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int viewPosition = manager.findLastVisibleItemPosition();
                int itemCount = manager.getItemCount();
                iv_moreData.setVisibility(View.GONE);
                if (itemCount %10==0  )
                {
                    iv_moreData.setVisibility(View.VISIBLE);
                    iv_moreData.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            walletTransActivity.loadTransactions(false,++pageIndex);

                        }
                    });

                }

            }
        });

        updateView();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("test", "onResume : ");

    }


    /**
     * <h2>notifyDataSetChangedCustom</h2>
     * <p> method to notify adapter or update views if the transactions list size changed </p>
     */
    public void notifyDataSetChangedCustom(ArrayList<TransctionsItem> _transactionsAL)
    {

        if (transactionsAL!=null && _transactionsAL!=null) {
// transactionsAL.clear();
            transactionsAL.addAll(_transactionsAL);
        }
        updateView();
    }

    /**
     * <h2>hideRefreshingLayout</h2>
     * <p> method to hide refresh layout </p>
     */
    public void hideRefreshingLayout()
    {
        /*if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);*/
    }


    /**
     * <h2>updateView</h2>
     * <p> method to show or hide the list and notItems views according to the size of the list </p>
     */
    private void updateView()
    {
        if(transactionsAL.size() > 0)
        {
            llNoTransactions.setVisibility(View.GONE);
            rvTransactions.setVisibility(View.VISIBLE);
        }
        else
        {
            rvTransactions.setVisibility(View.GONE);
            llNoTransactions.setVisibility(View.VISIBLE);
        }
        walletTransactionsRVA.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }


    @Override
    public void onDetach()
    {
        super.onDetach();
    }

}
