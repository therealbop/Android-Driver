package com.karry.utility;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.heride.partner.R;

public class BitmapCustomMarker
{
    private String serge;
    private Context context;
    private Marker driverOnTheWayMarker;
    private AppTypeFace appTypeFace;

    public BitmapCustomMarker(Context context, String serge, AppTypeFace appTypeFace)
    {
        this.context=context;
        this.serge=serge;
        this.appTypeFace = appTypeFace;
        Utility.printLog("serge in marker "+serge);
    }

    public Bitmap createBitmap()
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout=inflater.inflate(R.layout.serge_background, null);
        RelativeLayout lyBitmap= layout.findViewById(R.id.map_image);
        TextView tv_serge= layout.findViewById(R.id.tv_serge);
        tv_serge.setTypeface(appTypeFace.getOrbitronBold());
        tv_serge.setText(serge);
        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();
        layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        layout.layout(0, 0, lyBitmap.getMeasuredWidth(), lyBitmap.getMeasuredHeight());
        return layout.getDrawingCache();
    }
}


