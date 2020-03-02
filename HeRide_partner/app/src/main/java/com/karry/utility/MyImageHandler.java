package com.karry.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 3embed on 26/07/16.
 */
public class MyImageHandler {

    private static MyImageHandler myImageHandler;

    private MyImageHandler() {

    }

    /***********************************************************/


    public static MyImageHandler getInstance() {
        if (myImageHandler == null) {
            myImageHandler = new MyImageHandler();
        }
        return myImageHandler;
    }
    /***********************************************************/


    /**
     * custom method to create a directory if exist else clear it if required
     *
     * @param mContext:          calling activity reference
     * @param folderNameAndPath: path and name of the folder to be created
     * @param isToClearDir:      boolean to indicate whether to clear directory or not
     * @return
     */
    public File getAlbumStorageDir(Context mContext, String folderNameAndPath, boolean isToClearDir) {
        File newDir;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            newDir = new File(Environment.getExternalStorageDirectory() + "/" + folderNameAndPath);
        } else {
            newDir = new File(mContext.getFilesDir() + "/" + folderNameAndPath);
        }

        if (!newDir.isDirectory()) {
            newDir.mkdirs();
            Log.i("MyImageHandler", "getAlbumStorageDir created successfully: ");
        } else if (isToClearDir) {
            File[] newDirectory = newDir.listFiles();

            Log.i("MyImageHandler", "getAlbumStorageDir to clear dir files : " + newDirectory.length);

            if (newDirectory.length > 0) {
                for (int i = 0; i < newDirectory.length; i++) {
                    newDirectory[i].delete();
                }
                Log.i("MyImageHandler", "getAlbumStorageDir to clear dir successfully:");
            } else {
                Log.i("MyImageHandler", "getAlbumStorageDir to clear dir no pics: " + newDirectory.length);
            }
        } else {
            Log.i("MyImageHandler", "getAlbumStorageDir not to clear dir: ");
        }
        return newDir;
    }
    /***********************************************************/


    /**
     * custom method to download the images from the given url and save it as the given file
     *
     * @param murl:  url of the image to be downloaded
     * @param mFile: to save the downloaded file
     */

    public void addBitmapToSdCardFromURL(String murl, File mFile) {
        try {
            URL url = new URL(murl);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar

            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(mFile.getAbsolutePath());

            byte data[] = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*************************************************************/

    /**
     * custom method to transform inage into circle
     *
     * @param bitmap
     * @return: Bitmap transformed into circle
     */
    public Bitmap getCircleCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
    /*****************************************************/
}
