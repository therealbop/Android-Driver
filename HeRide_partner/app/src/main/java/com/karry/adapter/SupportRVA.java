package com.karry.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heride.partner.R;
import com.karry.pojo.SupportData;
import com.karry.authentication.signup.SignUpWeb.RegisterWebActivity;
import com.karry.side_screens.support.supportSubCategory.SupportSubCategoryActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.karry.utility.VariableConstant.FROM;
import static com.karry.utility.VariableConstant.SUPPORT;
import static com.karry.utility.VariableConstant.TITLE;
import static com.karry.utility.VariableConstant.URL;


/**
 * <h1>SupportRVA</h1>
 * <p>this is the Adapter for the Support data showing , with category and its sub Categories.</p>
 */
public class SupportRVA extends RecyclerView.Adapter<SupportRVA.ViewHolder> implements View.OnClickListener {

    private ArrayList<SupportData> supportDatas;
    private Context context;
    private Activity mActivity;


    @Inject
    SupportRVA(Context context) {
        this.context = context;
    }

    public void setSupportData(ArrayList<SupportData> supportDatas,Activity mActivity){
        this.supportDatas = supportDatas;
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_support_text, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(supportDatas.get(position).getName());
        holder.tvName.setOnClickListener(this);
        holder.tvName.setTag(holder);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvName) {
            int adapterPosition = ((ViewHolder) v.getTag()).getAdapterPosition();
            /*if (isMainCategory) {
                if (supportDatas.get(adapterPosition).getSubcat().size() > 0) {
                    Intent intent = new Intent(mActivity, SupportSubCategoryActivity.class);
                    intent.putExtra("data", supportDatas.get(adapterPosition).getSubcat());
                    intent.putExtra("title", supportDatas.get(adapterPosition).getName());
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                } else {
                    Toast.makeText(context, context.getString(R.string.nosubCategory), Toast.LENGTH_SHORT).show();
                }
            } else {
                Intent intent = new Intent(mActivity, RegisterWebActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("Title",supportDatas.get(adapterPosition).getName());
                mBundle.putString("From", "SignUp");
                mBundle.putString("URL", supportDatas.get(adapterPosition).getLink());
                intent.putExtras(mBundle);
                mActivity.startActivity(intent);
            }*/

            if (supportDatas.get(adapterPosition).getSubcat() != null && supportDatas.get(adapterPosition).getSubcat().size() > 0) {
                Intent intent = new Intent(mActivity, SupportSubCategoryActivity.class);
                intent.putExtra("data", supportDatas.get(adapterPosition).getSubcat());
                intent.putExtra("title", supportDatas.get(adapterPosition).getName());
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            } else {
                Intent intent = new Intent(mActivity, RegisterWebActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString(TITLE,supportDatas.get(adapterPosition).getName());
                mBundle.putString(URL, supportDatas.get(adapterPosition).getLink());
                mBundle.putString(FROM, SUPPORT);
                intent.putExtras(mBundle);
                mActivity.startActivity(intent);
            }
        }
    }

    @Override
    public int getItemCount() {
        return supportDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }

}
