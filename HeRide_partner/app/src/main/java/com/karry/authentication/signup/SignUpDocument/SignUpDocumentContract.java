package com.karry.authentication.signup.SignUpDocument;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.karry.BaseView;

import java.io.File;
import java.util.ArrayList;

/**
 * <h1>SignUpDocumentContract</h1>
 * <p>the interface for SignUp Document View and the Presenter</p>
 */

public interface SignUpDocumentContract {

    /**
     * <h1>SignUpDocumentView</h1>
     * <p>the interface for View, which after Implementation inform to view </p>
     */
    interface SignUpDocumentView extends BaseView{

        /**
         * <h1>initActionBar</h1>
         * <p>method for initialize the ActionBar</p>
         */
        void initActionBar();

        /**
         * <h1>setTitle</h1>
         * <p>method for set the title of ActionBar</p>
         */
        void setTitle();

        /**
         * <h1>amazonUploadSuccess</h1>
         * <p>inform the view, that the Image is uploaded successfully and need to show the Image</p>
         * @param url : the Image URL
         */
        void amazonUploadSuccess(String url);

        /**
         * <h1>signUpSuccess</h1>
         * <p>which show a success message after the signUp Api.</p>
         * @param driverId
         */
        void signUpSuccess(String driverId, String phone, String countryCode);

        /**
         * <h1>enableNext</h1>
         * <p>to inform that the validation check done and all fields are valid and enable next button</p>
         */
        void enableNext();

        /**
         * <h1>disableNext</h1>
         * <p>to inform that the validation check done and all fields are Not valid and disable next button</p>
         */
        void disableNext();

        void checkBoxHandler();

        void openWebView(String url);
    }

    /**
     * <h1>SignUpDocumentPresenter</h1>
     * <p>the Implementation which the view inform to presenter need to do. </p>
     */
    interface SignUpDocumentPresenter{

        /**
         * <h1>getVehicleData</h1>
         * <p>the data fetch from the Vehicle Activity and Assign to the Model class</p>
         * @param mBundle : data from the SignUp Vahicle
         */
        void getVehicleData(Bundle mBundle);

        /**
         * <h1>setActionBar</h1>
         * <p>ActionBar initialize</p>
         */
        void setActionBar();

        /**
         * <h1>setActionBarTitle</h1>
         * <p>for set the title of the Activity</p>
         */
        void setActionBarTitle();

        /**
         * <h1>amzonUpload</h1>
         * <p>upload the image in amazon account</p>
         * @param file : file name for upload
         * @param context : activity
         * @param Bucket_folder : the bitbucket folder for upload the image
         */
        void amzonUpload(File file, Context context, String Bucket_folder);

        /**
         * <h1>getDocumentString</h1>
         * <p>passes the Document list for make a string format with seperate with coma</p>
         * @param DocumentList : list of document
         * @return : the string of document list seperated with coma
         */
        String getDocumentString(ArrayList<String> DocumentList);

        /**
         *<h1>signUpAPI</h1>
         * <p>API call for the SignUp</p>
         */
        void signUpAPI(
                String al_licence_img_front, String al_licence_img_back, String al_vehicle_reg,
                String al_vehicle_insurance,
                String al_police_clearance,
                String al_inspection_report,
                String al_goods_insurance,
                String al_children_card,
                String driverLicenseDate,
                String motorInsuImageDate,
                String registrationDate,
                String policeClearanceDate,
                String inspectionReportDate,
                String goodsInTransitDate,
                String workWithChildrenDate
        );

        void compositeDisposableClear();

        /**
         *<h1>cropImage</h1>
         * <p>the result of crop Image pass to Uplad in to Amazon</p>
         * @param data : the result of crop Image
         * @param bucketFolder : folder of amazon for upload the image
         */
        void cropImage(Intent data, String bucketFolder);

        /**
         * <h1>validateDocument</h1>
         * <p>validate the mandatory field filled or not for enable finish</p>
         */
//        void validateDocument(int licence, int reg, int insurance);


        void validateDocument(int count_licence_img, int count_vehicle_reg, int count_vehicle_insurance, int count_police_clearance,
                              int count_inspection_report, int count_goods_insurance, int count_children_card,
                              String date_licence, String date_police, String date_children, String date_registartion,
                              String date_insurance, String date_inspection, String date_goodsInsurance);


        void isPrivacypolicyAccepted(boolean isAccepted);

        void setWebUrl(String from);

    }


}
