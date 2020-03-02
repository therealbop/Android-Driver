package com.karry.side_screens.help_center.zendeskadapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.side_screens.help_center.zendeskpojo.SpinnerRowItem;
import com.karry.utility.AppTypeFace;
import com.karry.utility.Utility;

import java.util.ArrayList;

/**
 * <h>SpinnerAdapter</h>
 * Created by Ali on 12/29/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<SpinnerRowItem>
{

    private Context mContext;
    AppTypeFace appTypeface;
    private ArrayList<SpinnerRowItem> spinnerRowItems;
    public SpinnerAdapter(Context mContext, int resouceId, int textviewId, ArrayList<SpinnerRowItem> spinnerRowItems, AppTypeFace appTypeface) {
        super(mContext,resouceId,textviewId,spinnerRowItems);
        this.mContext = mContext;
        this.spinnerRowItems = spinnerRowItems;
        this.appTypeface = appTypeface;
    }


    @Override
    public int getCount() {
        return spinnerRowItems.size();
    }

    @Override
    public long getItemId(int i) {
        return spinnerRowItems.get(i).getColorId();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        return getCustomView(position, convertView, parent);

    }

    private View getCustomView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_adapter, parent,false);
            holder = new ViewHolder();
            holder.ivSpinnerPriority = convertView.findViewById(R.id.ivSpinnerPriority);
            holder.tvSpinnerPriority = convertView.findViewById(R.id.tvSpinnerPriority);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        holder.tvSpinnerPriority.setText(spinnerRowItems.get(position).getPriority());
        holder.ivSpinnerPriority.setBackgroundColor(Utility.getColor(mContext,spinnerRowItems.get(position).getColorId()));
        holder.tvSpinnerPriority.setTypeface(appTypeface.getPro_News());
        return convertView;
    }

    private class ViewHolder {
        private TextView ivSpinnerPriority;
        private TextView tvSpinnerPriority;
    }
}
