package com.karry.adapter;

//import static com.couchbase.lite.util.Log.TAG;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.karry.app.meterBooking.address.AddressSelectContract;
import com.heride.partner.R;
import com.karry.pojo.PlaceAutoCompletePojo;
import com.karry.utility.AppTypeFace;
import com.karry.utility.Utility;
import java.util.ArrayList;

public class RecentAddressAdapter extends
        RecyclerView.Adapter<RecentAddressAdapter.PlaceViewHolder> {
    private Activity mContext;
    private ArrayList<PlaceAutoCompletePojo> placeAutoCompletePojos;
    private AppTypeFace appTypeFace;
    private AddressSelected addressSelected;
    private String TAG = "RecentAddressAdapter ";
    private AddressSelectContract.Presenter presenter;

    /**
     * <h2>PlaceAutoCompleteAdapter</h2>
     * This method is constructor
     * @param context context of the activity from which this is called
     */
    public RecentAddressAdapter(Activity context, AddressSelectContract.Presenter presenter )
    {
        mContext=context;
        appTypeFace = new AppTypeFace(mContext);
        this.presenter=presenter;

    }

    public void getAddressData(AddressSelected addressSelected)
    {
        this.addressSelected = addressSelected;
    }

    public void setData(ArrayList<PlaceAutoCompletePojo> placeAutoCompletePojos){
        this.placeAutoCompletePojos = new ArrayList<>();
        this.placeAutoCompletePojos = placeAutoCompletePojos;
    }

    /**
     * <h2>PlaceViewHolder</h2>
     * This method is used for defining the android widgets
     */
    class PlaceViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_drop_clock_icon;
        RelativeLayout rl_drop_address_layout;
        TextView tv_select_address, tv_select_address_fav_title;
        PlaceViewHolder(View itemView)
        {
            super(itemView);
            iv_drop_clock_icon = itemView.findViewById(R.id.iv_select_address_left_icon);
            iv_drop_clock_icon.setImageResource(R.drawable.svg_location_middle);
            rl_drop_address_layout = itemView.findViewById(R.id.rl_select_address_layout);
            tv_select_address = itemView.findViewById(R.id.tv_select_address);
            tv_select_address_fav_title = itemView.findViewById(R.id.tv_select_address_fav_title);
        }
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.item_drop_address, viewGroup, false);
        return new PlaceViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder mPredictionHolder, final int i) {



        Utility.printLog(TAG+" Address item pos "+i);
        Utility.printLog(TAG+" Address item size "+placeAutoCompletePojos.size());
        if(i<placeAutoCompletePojos.size())
        {
            mPredictionHolder.tv_select_address.setText(""+placeAutoCompletePojos.get(i).getAddress());
            mPredictionHolder.tv_select_address.setTypeface(appTypeFace.getPro_News());
            mPredictionHolder.tv_select_address_fav_title.setVisibility(View.GONE);
            //to add the click listener for the layout and notify
            //mPredictionHolder.rl_drop_address_layout.setOnClickListener(v -> mListener.onPlaceClick(mResultList,i));


            mPredictionHolder.rl_drop_address_layout.setOnClickListener(view -> {
                addressSelected.addressData(placeAutoCompletePojos.get(i)/*.getPlaceId(),
                        placeAutoCompletePojos.get(i).getAddress()*/);
            });
        }
        //to add the click listener for the layout and notify
    }
    @Override
    public int getItemCount() {
        if(placeAutoCompletePojos != null)
            return placeAutoCompletePojos.size();
        else
            return 0;
    }

    public interface AddressSelected{
        void addressData(
            PlaceAutoCompletePojo placeAutoCompletePojo/*String placeId, String address*/);
    }
}
