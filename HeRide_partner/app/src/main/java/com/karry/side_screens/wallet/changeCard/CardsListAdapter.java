package com.karry.side_screens.wallet.changeCard;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.utility.AppTypeFace;

import com.karry.utility.Utility;
import com.karry.side_screens.wallet.changeCard.model.CardInfoModel;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is an inner class of adapter type, to integrate the list on screen.
 */
public class CardsListAdapter extends RecyclerView.Adapter<CardsListAdapter.ViewHolder>
{
    private Context context;
    private AppTypeFace appTypeface;
    private ChangeCardContract.View  changeCardView;
    private List<CardInfoModel> cards = new ArrayList<>();
    private boolean isFromFragment;

    public CardsListAdapter(Context context)
    {
        this.context = context;
        appTypeface=AppTypeFace.getInstance(context);
    }

    public void provideChangeViewAndList( ChangeCardContract.View changeCardView,List<CardInfoModel> cards)
    {
        Utility.printLog("MyCardDet"+cards.size());
        isFromFragment = false;
        this.changeCardView = changeCardView;
        this.cards = cards;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        CardInfoModel cardInfoModel = cards.get(position);
        assert cardInfoModel != null;
        holder.iv_payment_card.setImageResource(Utility.checkCardType(cardInfoModel.getCard_image()));

        holder.tv_payment_card_number.setText(context.getString(R.string.card_ending_with) + " " +
                cardInfoModel.getCard_numb());
        holder.tv_payment_card_number.setTypeface(appTypeface.getPro_News());

        if (cardInfoModel.getDefaultCard())
            holder.iv_payment_tick.setVisibility(View.VISIBLE);
        else
            holder.iv_payment_tick.setVisibility(View.GONE);

        holder.iv_payment_delete.setOnClickListener(v ->
        {
                changeCardView.onClickOfDelete(position);
        });

        holder.rl_payment_layout.setOnClickListener(v ->
        {
                changeCardView.onClickOfItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.iv_payment_card) ImageView iv_payment_card;
        @BindView(R.id.iv_payment_tick) ImageView iv_payment_tick;
        @BindView(R.id.iv_payment_delete) ImageView iv_payment_delete;
        @BindView(R.id.tv_payment_card_number) TextView tv_payment_card_number;
        @BindView(R.id.rl_payment_layout) RelativeLayout rl_payment_layout;

        ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
