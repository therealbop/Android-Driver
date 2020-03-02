package com.karry.side_screens.wallet.adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.heride.partner.R;
import com.karry.side_screens.wallet.wallet_transaction_activity.model.TransctionsItem;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * <h1>WalletTransactionsAdapter</h1>
 * This class is used to inflate the wallet transactions list
 *
 * @since 20/09/17.
 */
public class WalletTransactionsAdapter extends RecyclerView.Adapter<WalletTransactionsAdapter.WalletViewHolder> {

    private Context mContext;
    private ArrayList<TransctionsItem> transactionsAL;
    Typeface ClanaproNarrMedium;
    Typeface ClanaproNarrNews;
    Typeface ClanaproMedium;

    /**
     * <h2>WalletTransactionsAdapter</h2>
     * This is the constructor of our adapter.
     */
    public WalletTransactionsAdapter(Context context, ArrayList<TransctionsItem> _transactionsAL) {
        this.mContext = context;
        this.transactionsAL = _transactionsAL;
        ClanaproNarrMedium = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrMedium.otf");
        ClanaproNarrNews = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrNews.otf");
        ClanaproMedium = Typeface.createFromAsset(context.getAssets(), "fonts/CLANPRO-MEDIUM.OTF");

    }

    @Override
    public WalletTransactionsAdapter.WalletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_transactions, parent, false);
        return new WalletTransactionsAdapter.WalletViewHolder(viewList);

    }

    @Override
    public void onBindViewHolder(WalletViewHolder walletViewHolder, final int position) {
        TransctionsItem walletDataDetailsItem = transactionsAL.get(position);

        if (walletDataDetailsItem.getTxnType().equalsIgnoreCase("DEBIT")) {
            walletViewHolder.iv_wallet_transaction_arrow.setBackgroundColor(mContext.getResources().getColor(R.color.red_light));
            walletViewHolder.iv_wallet_transaction_arrow.setRotation(90);
        } else {
            walletViewHolder.iv_wallet_transaction_arrow.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            walletViewHolder.iv_wallet_transaction_arrow.setRotation(-90);
        }

        walletViewHolder.tv_wallet_transactionId.setText(
                mContext.getString(R.string.transactionID) + " " + walletDataDetailsItem.getTxnId().trim());

        if (walletDataDetailsItem.getTripId().isEmpty() || walletDataDetailsItem.getTripId().equalsIgnoreCase("N/A")) {
            walletViewHolder.tv_wallet_transaction_bid.setVisibility(View.GONE);
        } else {
            walletViewHolder.tv_wallet_transaction_bid.setText(mContext.getString(R.string.bookingID) + walletDataDetailsItem.getTripId());
        }

        if( walletDataDetailsItem.getCurrencyAbbr()==1)
            walletViewHolder.tv_wallet_transaction_amount.setText(VariableConstant.CURRENCY.concat(" ").concat(Utility.getFormattedPrice(String.valueOf(walletDataDetailsItem.getAmount()))));
        else
            walletViewHolder.tv_wallet_transaction_amount.setText(Utility.getFormattedPrice(String.valueOf(walletDataDetailsItem.getAmount())).concat(" ").concat(VariableConstant.CURRENCY));


        walletViewHolder.tv_wallet_transaction_description.setText(walletDataDetailsItem.getTrigger().trim());
        walletViewHolder.tv_wallet_transaction_date.setText(epochTimeConverter(String.valueOf(walletDataDetailsItem.getTxnDate())));
    }

    @Override
    public int getItemCount() {
        return transactionsAL.size();
    }
    //====================================================================

    /**
     * <h2>epochTimeConverter</h2>
     * <p>
     * method to convert received epoch seconds into formatted date time string
     * </p>
     *
     * @param milliSecsString input the milli sec
     * @return returns the date in the format dd MMM yyyy, hh:mm a
     */
    private String epochTimeConverter(String milliSecsString) {
        if (milliSecsString.isEmpty()) {
            return "";
        }
        long milliSecs = Long.parseLong(milliSecsString);
        Date date = new Date(milliSecs * 1000);
        DateFormat format = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US);
        //format.setTimeZone(TimeZone.getTimeZone("US"));
        return format.format(date);
    }
    //====================================================================

    /**
     * <h1>ListViewHolder</h1>
     * <p>
     * This method is used to hold the views
     * </p>
     */
    class WalletViewHolder extends RecyclerView.ViewHolder {
        TextView tv_wallet_transactionId, tv_wallet_transaction_bid, tv_wallet_transaction_amount;
        TextView tv_wallet_transaction_description, tv_wallet_transaction_date;
        ImageView iv_wallet_transaction_arrow;

        WalletViewHolder(View cellView) {
            super(cellView);
            tv_wallet_transactionId = cellView.findViewById(R.id.tv_wallet_transactionId);
            tv_wallet_transactionId.setTypeface(ClanaproNarrNews);

            tv_wallet_transaction_bid = cellView.findViewById(R.id.tv_wallet_transaction_bid);
            tv_wallet_transaction_bid.setTypeface(ClanaproNarrNews);

            tv_wallet_transaction_amount = cellView.findViewById(R.id.tv_wallet_transaction_amount);
            tv_wallet_transaction_amount.setTypeface(ClanaproNarrMedium);

            tv_wallet_transaction_description = cellView.findViewById(R.id.tv_wallet_transaction_description);
            tv_wallet_transaction_description.setTypeface(ClanaproNarrNews);

            tv_wallet_transaction_date = cellView.findViewById(R.id.tv_wallet_transaction_date);
            tv_wallet_transaction_date.setTypeface(ClanaproNarrNews);

            iv_wallet_transaction_arrow = cellView.findViewById(R.id.iv_wallet_transaction_arrow);
        }
    }
    //================================================================/
}
