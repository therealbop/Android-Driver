package com.karry.utility;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by DELL on 19-09-2017.
 */

public class PicassoMarker implements Target
{
    Marker mMarker;
    public PicassoMarker(Marker marker) {
        mMarker = marker;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
    {
        try
        {
            mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Callback indicating the image could not be successfully loaded.
     * <p>
     * <strong>Note:</strong> The passed {@link Drawable} may be {@code null} if none has been
     * specified via {@link RequestCreator#error(Drawable)}
     * or {@link RequestCreator#error(int)}.
     *
     * @param e
     * @param errorDrawable
     */
    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    public Marker getmMarker() {
        return mMarker;
    }

    public void setmMarker(Marker mMarker) {
        this.mMarker = mMarker;
    }



    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
