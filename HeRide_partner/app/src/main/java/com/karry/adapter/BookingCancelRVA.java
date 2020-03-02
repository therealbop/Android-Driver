package com.karry.adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heride.partner.R;

import java.util.ArrayList;

import javax.inject.Inject;


/**
 * <h1>BookingCancelRVA</h1>
 */
public class BookingCancelRVA extends RecyclerView.Adapter<BookingCancelRVA.ViewHolder> {

    ComentsCancel comentsCancel;
    private Context context;
    private View view;
    private ArrayList<String> str_cancel_reasons = new ArrayList<>();
    private TextView last_select_txt;
    private boolean clickEnable = true;

    @Inject
    public BookingCancelRVA(Context context) {
        this.context = context;

    }

    public void  setCancelReasons(ArrayList<String> str_cancel_reasons){
        this.str_cancel_reasons = str_cancel_reasons;
    }

    public void setClickEnable(boolean clickEnable){
        this.clickEnable = clickEnable;
    }

    public void setComentsCancel( ComentsCancel comentsCancel){
        this.comentsCancel = comentsCancel;
    }

    /**********************************************************************************************/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cancel_reason, parent, false);
        return new BookingCancelRVA.ViewHolder(view);

    }

    /**********************************************************************************************/
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.tv_cancel_reason.setText(str_cancel_reasons.get(position));


        if(clickEnable) {
            holder.tv_cancel_reason.setOnClickListener(v -> {
                if (last_select_txt != null) {
                    last_select_txt.setTextColor(context.getResources().getColor(R.color.color2));
                }
                holder.tv_cancel_reason.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                comentsCancel.oncommment(holder.tv_cancel_reason.getText().toString());

                last_select_txt = holder.tv_cancel_reason;
            });
        }

    }

    /**********************************************************************************************/
    @Override
    public int getItemCount() {
        return str_cancel_reasons.size();
    }

    /**
     * interface for cancel , if select any reason then the comment will appear
     */
    public interface ComentsCancel {
        void oncommment(String reason);
    }

    /**********************************************************************************************/
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_cancel_reason;

        public ViewHolder(View itemView) {
            super(itemView);

            Typeface ClanaproNarrNews = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrNews.otf");
            tv_cancel_reason = itemView.findViewById(R.id.tv_cancel_reason);
            tv_cancel_reason.setTypeface(ClanaproNarrNews);

        }
    }


}
