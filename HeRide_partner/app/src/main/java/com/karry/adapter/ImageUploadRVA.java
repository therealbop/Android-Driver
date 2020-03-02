package com.karry.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.karry.authentication.signup.SignUpDocument.SignUpDocumentActivity;
import com.squareup.picasso.Picasso;
import com.heride.partner.R;
import com.karry.utility.Utility;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * <h1>ImageUploadRVA</h1>
 * <p>this is the class for maintain the image, it is the adapter for the image,
 * which can add the image and remove from the list.</p>
 */

public class ImageUploadRVA extends RecyclerView.Adapter<ImageUploadRVA.ViewHolder> {

    private Context context;
    private ArrayList<String> imagefile=new ArrayList<>();
    private ImageView iv_delete,iv_uploaded;
    private RemoveImage removeImage;
    private String type;
    private SignUpDocumentActivity activity;

    /**********************************************************************************************/
    @Inject
    public ImageUploadRVA(Context context)
    {
        this.context=context;
    }

    public void setImageFile(ArrayList<String> imagefile ,String type){
        this.imagefile = imagefile;
        this.type=type;
    }

    public void getRemoveImage(RemoveImage removeImage){
        this.removeImage=removeImage;
    }


    /**********************************************************************************************/
    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);

            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_uploaded = itemView.findViewById(R.id.iv_uploaded);

        }

    }

    /**********************************************************************************************/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        activity=(SignUpDocumentActivity) parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_camera_uploader, parent, false);
        return new ImageUploadRVA.ViewHolder(view);
    }

    /**********************************************************************************************/
    @Override
    public void onBindViewHolder(ImageUploadRVA.ViewHolder holder, final int position) {

        iv_delete.setOnClickListener(v -> removeImage.ImageRemoved(position));

        Utility.printLog("the img url is : "+imagefile.get(position));
        Picasso.get()
                .load(imagefile.get(position))
                .into(iv_uploaded);

        if(type!=null && type.equals("police_licence") && imagefile.size()>0)
            activity.enablePoliceClearenceExpDate();
        if(type!=null && type.equals("children_licence")&& imagefile.size()>0)
            activity.enablechildrenCertificate();
        if(type!=null && type.equals("inspection_report")&& imagefile.size()>0)
            activity.enableInspectionRepo();
        if(type!=null && type.equals("goods_report")&& imagefile.size()>0)
            activity.enableGoods();

    }

    /**********************************************************************************************/
    @Override
    public int getItemCount() {
        return imagefile.size();
    }

    public interface RemoveImage{
        void ImageRemoved(int position);
    }


}
