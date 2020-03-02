package com.karry.adapter;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karry.side_screens.bankDetails.BankBottomSheetFragment;
import com.heride.partner.R;
import com.karry.pojo.bank.BankList;
import com.karry.utility.AppTypeFace;

import java.io.Serializable;
import java.util.ArrayList;

import javax.inject.Inject;



public class BankDetailsRVA extends RecyclerView.Adapter<BankDetailsRVA.ViewHolder> implements View.OnClickListener {

    private static final String TAG = "BankDetailsRVA";
    private ArrayList<BankList> bankLists;
    private RefreshBankDetails refreshBankDetails;
    private FragmentManager fragmentManager;
    CallbackFromBankDetailRVA callbackFromBankDetailRVA;

    @Inject  AppTypeFace appTypeFace;
    @Inject  BankDetailsRVA() {
    }

    public void setCallbackFromBankDetailRVA(CallbackFromBankDetailRVA callbackFromBankDetailRVA){
        this.callbackFromBankDetailRVA = callbackFromBankDetailRVA;
    }

    public void setBankData(ArrayList<BankList> bankLists,FragmentManager fragmentManager){
        this.bankLists = bankLists;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BankDetailsRVA.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_bank_details, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.tvAccountNo.setText("xxxxxxxx"+ bankLists.get(position).getLast4());
        holder.tvAccountHolder.setText(bankLists.get(position).getAccount_holder_name());

        if(bankLists.get(position).getDefault_for_currency().matches("true"))
        holder.iv_delete.setVisibility(View.GONE);

        holder.llBankDetails.setOnClickListener(this);
        holder.iv_delete.setOnClickListener(this);
        holder.llBankDetails.setTag(holder);
        holder.iv_delete.setTag(holder);

    }

    @Override
    public int getItemCount() {
        return bankLists.size();
    }

    @Override
    public void onClick(View v)
    {
        ViewHolder viewHolder = (ViewHolder) v.getTag();
        int position = viewHolder.getAdapterPosition();

        switch (v.getId())
        {
            case R.id.llBankDetails:
                BottomSheetDialogFragment bottomSheetDialogFragment = BankBottomSheetFragment.newInstance(bankLists.get(position)/*,refreshBankDetails*/);
                bottomSheetDialogFragment.show(fragmentManager, bottomSheetDialogFragment.getTag());
                break;

            case R.id.iv_delete:
                callbackFromBankDetailRVA.deleteCheck(bankLists.get(position).getId());
                break;

        }
    }

    public interface RefreshBankDetails extends Serializable {
        void onRefresh();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ivCheck,iv_delete;
        TextView tvAccountNoLabel, tvAccountNo, tvAccountHolderLabel, tvAccountHolder;
        RelativeLayout llBankDetails;

        public ViewHolder(View itemView)
        {
            super(itemView);

            ivCheck = itemView.findViewById(R.id.ivCheck);
            tvAccountNoLabel = itemView.findViewById(R.id.tvAccountNoLabel);
            tvAccountNo = itemView.findViewById(R.id.tvAccountNo);
            tvAccountHolderLabel = itemView.findViewById(R.id.tvAccountHolderLabel);
            tvAccountHolder = itemView.findViewById(R.id.tvAccountHolder);
            llBankDetails = itemView.findViewById(R.id.llBankDetails);
            iv_delete = itemView.findViewById(R.id.iv_delete);

            tvAccountNoLabel.setTypeface(appTypeFace.getPro_News());
            tvAccountHolderLabel.setTypeface(appTypeFace.getPro_News());

            tvAccountNo.setTypeface(appTypeFace.getPro_narMedium());
            tvAccountHolder.setTypeface(appTypeFace.getPro_News());
        }

    }

    /**
     * interface for the click for respond
     */
    public  interface  CallbackFromBankDetailRVA{
        void deleteCheck(String bankID);
        void defaultCheck(String bankID);
    }


}
