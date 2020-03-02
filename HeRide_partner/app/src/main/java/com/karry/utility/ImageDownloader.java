package com.karry.utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by murashid on 29-Aug-16.
 */
public class ImageDownloader {

    String imagename;
    String requestedUrl;
    ImageView imageView;
    Context context;

    public ImageDownloader(String imagename, String requestedUrl, ImageView imageView, Context context) {
        this.imagename = imagename;
        this.requestedUrl = requestedUrl;
        this.imageView = imageView;
        this.context = context;
    }

    public ImageDownloader(Context context) {
        this.context = context;
    }

    public void setAndSaveImage() {
        if (checkifImageExists(imagename)) {
            File file = getImage("/" + imagename + ".jpg");
            String path = file.getAbsolutePath();
            if (path != null) {
                Bitmap b = BitmapFactory.decodeFile(path);
                imageView.setImageBitmap(b);
            }
        } else {
            new GetImages(requestedUrl, imageView, imagename).execute();
        }

    }

    public String saveToSdCard(Bitmap bitmap, String filename) {

        String stored = null;

        File folder;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            folder = new File(Environment.getExternalStorageDirectory() + "/" + VariableConstant.PARENT_FOLDER + "/Media/Images/Card");
        } else {
            folder = new File(context.getFilesDir() + "/" + VariableConstant.PARENT_FOLDER + "/Media/Images/Card");
        }
        folder.mkdir();
        File file = new File(folder.getAbsoluteFile(), filename + ".jpg");
        if (file.exists())
            return stored;

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            stored = "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stored;
    }

    public File getImage(String imagename) {

        File mediaImage = null;
        try {
            File root;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                root = new File(Environment.getExternalStorageDirectory() + "/" + VariableConstant.PARENT_FOLDER + "/Media/Images/Card");
            } else {
                root = new File(context.getFilesDir() + "/" + VariableConstant.PARENT_FOLDER + "/Media/Images/Card");
            }
            if (!root.exists())
                return null;
            mediaImage = new File(root.getPath() + "/" + imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mediaImage;
    }

    public boolean checkifImageExists(String imagename) {
        Bitmap b = null;
        File file = getImage("/" + imagename + ".jpg");
        if (file != null) {
            String path = file.getAbsolutePath();

            if (path != null)
                b = BitmapFactory.decodeFile(path);

            if (b == null || b.equals("")) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public void shareImage(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(share, "Share Image"));
    }

    class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl, imagename_;
        private ImageView view;
        private Bitmap bitmap;
        private FileOutputStream fos;
        private GetImages(String requestUrl, ImageView view, String _imagename_) {
            this.requestUrl = requestUrl;
            this.view = view;
            this.imagename_ = _imagename_;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                Log.d("mura", "doInBackground: ");
            } catch (Exception ex) {
                Log.d("mura", "doInBackground: " + ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (!checkifImageExists(imagename_)) {
                view.setImageBitmap(bitmap);
                saveToSdCard(bitmap, imagename_);
            }
        }
    }
}
