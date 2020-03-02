package com.karry.side_screens.wallet.changeCard;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.heride.partner.R;
import com.karry.utility.AppTypeFace;

import com.karry.side_screens.wallet.adapter.Alerts;
import com.karry.side_screens.wallet.add_card.AddCardActivity;
import com.karry.side_screens.wallet.changeCard.model.CardInfoModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karry.utility.VariableConstant.CARD_BRAND;
import static com.karry.utility.VariableConstant.CARD_TOKEN;
import static com.karry.utility.VariableConstant.LAST_DIGITS;

/**
 * <h1>ChangeCardActivity</h1>
 * This class is used for choosing the card from card list
 * @author embed on 9/12/15.
 * @version v1.0
 */
public class ChangeCardActivity extends DaggerAppCompatActivity implements ChangeCardContract.View
{
    @BindView(R.id.tv_payment_add_card) TextView tv_payment_add_card;
    @BindView(R.id.rv_payment_cards) RecyclerView rv_payment_cards;
    @BindString(R.string.network_problem) String networkProblem;
    @BindString(R.string.bad_gateway) String badGateWay;
    @BindString(R.string.paymentMethod) String paymentMethod;

    @Inject Alerts alerts;
    @Inject AppTypeFace appTypeface;
    @Inject ChangeCardContract.Presenter presenter;

    @BindView(R.id.toolbar)Toolbar toolBar;
    @BindView(R.id.tv_title) TextView tvAbarTitle;
    @BindDrawable(R.drawable.back_white_btn)  Drawable back_white_btn;
    @Inject CardsListAdapter adapter;
    private List<CardInfoModel> cardList;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_payment);
        initialize();
    }

    /**
     * <h2>initialize</h2>
     * <p>
     * initialize the views
     * </p>
     */
    private void initialize()
    {
        ButterKnife.bind(this);
        initToolBar();
        pDialog = alerts.getProcessDialog(this);
        cardList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_payment_cards.setLayoutManager(layoutManager);
        rv_payment_cards.setAdapter(adapter);
    }

    /* <h2>initToolBar</h2>
    * <p> method to initialize customer toolbar </p>
    */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void initToolBar()
    {
        setSupportActionBar(toolBar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(back_white_btn);
        }

        tvAbarTitle.setTypeface(appTypeface.getPro_narMedium());
        tvAbarTitle.setText(paymentMethod);
    }

    @OnClick({R.id.tv_payment_add_card})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_payment_add_card:
                Intent intent = new Intent(ChangeCardActivity.this, AddCardActivity.class);
                startActivity(intent);
                ChangeCardActivity.this.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;
        }
    }


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
    protected void onResume() {
        super.onResume();
        cardList.clear();
        presenter.getAllCards();
    }

    @Override
    public void showProgressDialog() {
        pDialog.show();
    }

    @Override
    public void errorResponse() {

    }

    @Override
    public void clearRowItem() {

    }

    @Override
    public void responseItem(CardInfoModel item) {
        cardList.add(item);
        adapter.provideChangeViewAndList(this,cardList);
    }

    @Override
    public void onClickOfDelete(int position) {
        CardInfoModel row_details = cardList.get(position);
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_card_pop_up);
        String cardId=row_details.getCard_id();
        TextView tv_delete_card_header=dialog.findViewById(R.id.tv_delete_card_header);
        TextView tv_delete_card_message=dialog.findViewById(R.id.tv_delete_card_message);
        tv_delete_card_header.setTypeface(appTypeface.getPro_narMedium());
        tv_delete_card_message.setTypeface(appTypeface.getPro_News());
        dialog.findViewById(R.id.btn_delete_card_delete).setOnClickListener(view -> {
            pDialog.show();
            presenter.deleteCard(cardId,position,cardList);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void deleteItemData(int position)
    {
        cardList.remove(position);
        adapter.provideChangeViewAndList(this,cardList);
    }

    @Override
    public void onClickOfItem(int position)
    {
        CardInfoModel row_details = cardList.get(position);
        presenter.makeCardDefault(row_details);
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(LAST_DIGITS, row_details.getCard_numb().trim().substring
                (row_details.getCard_numb().trim().length() - 4));
        bundle.putString(CARD_TOKEN,row_details.getCard_id());
        bundle.putString(CARD_BRAND,row_details.getBrand());
        resultIntent.putExtras(bundle);
        setResult(RESULT_OK, resultIntent);
        this.onBackPressed();
    }

    @Override
    public void internetError()
    {
        Toast.makeText(this, networkProblem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void badGateWayError()
    {
        Toast.makeText(this, badGateWay, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dismissProgressDialog()
    {
        pDialog.dismiss();
    }
}
