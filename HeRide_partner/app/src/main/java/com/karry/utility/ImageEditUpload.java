package com.karry.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.widget.Button;

import com.heride.partner.R;

import java.io.File;
import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.N;


public class ImageEditUpload implements ActionSheet.ActionSheetListener {

    private Activity context;
    private String state;
    private Fragment fragment ;


    public ImageEditUpload(Activity context, Fragment fragment) {

        this.context = context;
        this.fragment = fragment;
        DocumentUpload();
    }



    /**<h1>DocumentUpload</h1>
     *<p>permission for the document select edit</p>
     */
    private void DocumentUpload() {

        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow
            ArrayList<AppPermissionsRunTime.MyPermissionConstants> myPermissionConstantsArrayList = new ArrayList<>();
            //appPermissionsRunTime = new AppPermissionsRunTime();

            myPermissionConstantsArrayList.add(AppPermissionsRunTime.MyPermissionConstants.PERMISSION_CAMERA);
            myPermissionConstantsArrayList.add(AppPermissionsRunTime.MyPermissionConstants.PERMISSION_WRITE_EXTERNAL_STORAGE);
            myPermissionConstantsArrayList.add(AppPermissionsRunTime.MyPermissionConstants.PERMISSION_READ_EXTERNAL_STORAGE);

            int REQUEST_CODE_PERMISSION_MULTIPLE = 123;
            if (AppPermissionsRunTime.checkPermission(context, myPermissionConstantsArrayList, REQUEST_CODE_PERMISSION_MULTIPLE)) {
                actionSheetToChoosePic();
            }
        } else {
            // Pre-Marshmallow
            actionSheetToChoosePic();
        }

    }

    /**
     * <h1>actionSheetToChoosePic</h1>
     * <p>this is the method for action sheet , will display the options for choose the way to upload svg_profile pic</p>
     */
    private void actionSheetToChoosePic() {
        mShowImageOptions();

    }


    private void mShowImageOptions() {
        final Dialog mDialog = new Dialog(context);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.white)));
        mDialog.setContentView(R.layout.profile_pic_options);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));


        Button btnCamera = (Button) mDialog.findViewById(R.id.camera);
        Button btnCancel = (Button) mDialog.findViewById(R.id.cancel);
        Button btnGallery = (Button) mDialog.findViewById(R.id.gallery);

        btnCamera.setOnClickListener(view -> {
            takePicFromCamera();
            mDialog.dismiss();
        });

        btnGallery.setOnClickListener(view -> {
            openGallery();
            mDialog.dismiss();
        });


        btnCancel.setOnClickListener(view -> mDialog.dismiss());

        mDialog.show();

    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

        clearOrCreateDir();
        switch (index) {

            case 0:
                openGallery();
                break;

            case 1:
                takePicFromCamera();
                break;

            case 2:
                /*SignupPersonalActvity ref = (SignupPersonalActvity) context;
                ref.remove();*/
                break;
            default:
                break;
        }
    }

    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");

        if(fragment !=null )
        {
            fragment.startActivityForResult(photoPickerIntent, VariableConstant.GALLERY_PIC);
        }
        else
        {
            context.startActivityForResult(photoPickerIntent, VariableConstant.GALLERY_PIC);
        }

    }

    /**
     * <h1>clearOrCreateDir</h1>
     * <p>this is the method for the directory creation in internal or external storage </p>
     */
    private void clearOrCreateDir() {
        try {
            state = Environment.getExternalStorageState();
            File cropImagesDir;
            File[] cropImagesDirectory;
            File profilePicsDir;
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                cropImagesDir = new File(Environment.getExternalStorageDirectory() + "/" + VariableConstant.PARENT_FOLDER + "/Media/Images/CropImages");
                profilePicsDir = new File(Environment.getExternalStorageDirectory() + "/" + VariableConstant.PARENT_FOLDER + "/Media/Images/Profile_Pictures");
            } else {
                cropImagesDir = new File(context.getFilesDir() + "/" + VariableConstant.PARENT_FOLDER + "/Media/Images/CropImages");
                profilePicsDir = new File(context.getFilesDir() + "/" + VariableConstant.PARENT_FOLDER + "/Media/Images/Profile_Pictures");
            }

            if (!cropImagesDir.isDirectory()) {
                cropImagesDir.mkdirs();
            } else {
                cropImagesDirectory = cropImagesDir.listFiles();

                if (cropImagesDirectory.length > 0) {
                    for (File aCropImagesDirectory : cropImagesDirectory) {
                        aCropImagesDirectory.delete();
                    }
                    Utility.printLog("RegistrationAct CropImages cleared successfully:");
                } else {
                    Utility.printLog("RegistrationAct CropImages Dir empty  or null: " + cropImagesDirectory.length);
                }
            }

            if (!profilePicsDir.isDirectory()) {
                profilePicsDir.mkdirs();
                Utility.printLog("RegistrationAct profilePicsDir is created:" + profilePicsDir);
            } else {
                File[] profilePicsDirectory = profilePicsDir.listFiles();

                if (profilePicsDirectory.length > 0) {
                    for (File aProfilePicsDirectory : profilePicsDirectory) {

                        aProfilePicsDirectory.delete();
                    }
                    Utility.printLog("RegistrationAct profilePicsDir cleared successfully:");
                } else {
                    Utility.printLog("RegistrationAct profilePicsDir empty  or null: " + profilePicsDirectory.length);
                }
            }
        } catch (Exception e) {
            Utility.printLog("RegistrationAct Error while creating newfile:" + e);
        }
    }

    /**
     * <h1>takePicFromCamera</h1>
     * <p>this is the method for take image from the camera</p>
     */
    private void takePicFromCamera() {
        Utility.printLog("RegistrationAct Inside takePicFromCamera():");
        try {
            Log.d("camerapic", "takePicFromCamera: ");
            String takenNewImage = "";
            state = Environment.getExternalStorageState();
            takenNewImage = VariableConstant.PARENT_FOLDER + String.valueOf(System.nanoTime()) + ".png";


            File newFile;
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                newFile = new File(Environment.getExternalStorageDirectory() , takenNewImage);
                VariableConstant.newFile = newFile;
            } else {
                newFile = new File(context.getFilesDir() , takenNewImage);
                VariableConstant.newFile = newFile;
            }


            /*Uri newProfileImageUri;
            if (Build.VERSION.SDK_INT >= 24) {
                newProfileImageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", newFile);
            } else {
                newProfileImageUri = Uri.fromFile(newFile);
            }*/


            if(Build.VERSION.SDK_INT>=N)
                VariableConstant.newProfileImageUri = FileProvider.getUriForFile(context, context.getPackageName(),VariableConstant.newFile);
            else
                VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);



            /*VariableConstant.newProfileImageUri = newProfileImageUri;*/

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, VariableConstant.newProfileImageUri);
            intent.putExtra("return-data", true);

            if(fragment !=null )
            {
                fragment.startActivityForResult(intent, VariableConstant.CAMERA_PIC);
            }
            else
            {
                context.startActivityForResult(intent, VariableConstant.CAMERA_PIC);
            }

        } catch (ActivityNotFoundException e) {
            Utility.printLog("RegistrationAct cannot take picture: " + e);
        }
    }

}
