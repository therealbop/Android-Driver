package com.karry.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

public class CircleTransformation implements Transformation {

    @Override
    public Bitmap transform(Bitmap source) {

        try {
            int size = Math.min(source.getWidth(), source.getHeight());


            Bitmap finalBitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(finalBitmap);
            Paint paint = new Paint();

            BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float radius = size / 2f;
            canvas.drawCircle(radius, radius, radius, paint);

            source.recycle();
            return finalBitmap;
        }catch (NullPointerException e){
            return null;
        }

        
       /* //////
        int dim = 20;

        Canvas canvas1 = new Canvas();

        // placeholder for final image
        Bitmap result = Bitmap.createBitmap(dim, dim, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(result);
        Paint paint1 = new Paint();
        paint.setFilterBitmap(false);

        // resize image fills the whole canvas
        canvas.drawBitmap(source, null, new Rect(0,  0, dim, dim), paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(Util.getMaskImage(), 0, 0, paint);
        paint.setXfermode(null);

        if(result != source) {
            source.recycle();
        }

        return result;*/



    }

    @Override
    public String key() {
        return "circle";
    }
}
