package com.karry.authentication.signup.SignUpDocument;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.material.textfield.TextInputLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.karry.adapter.ImageUploadRVA;
import com.heride.partner.R;
import com.karry.authentication.forgotpassword.OTPVerificationActivity;
import com.karry.authentication.signup.SignUpWeb.RegisterWebActivity;
import com.karry.utility.AppTypeFace;
import com.karry.utility.ImageEditUpload;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import dagger.android.support.DaggerAppCompatActivity;
import eu.janmuller.android.simplecropimage.CropImage;

import static com.karry.utility.VariableConstant.CAMERA_PIC;
import static com.karry.utility.VariableConstant.CHILDREN_CARD_EXPIRY;
import static com.karry.utility.VariableConstant.COUNTRY_CODE;
import static com.karry.utility.VariableConstant.CROP_IMAGE;
import static com.karry.utility.VariableConstant.FROM;
import static com.karry.utility.VariableConstant.GALLERY_PIC;
import static com.karry.utility.VariableConstant.GOODS_INSPECTION;
import static com.karry.utility.VariableConstant.INSPECTION;
import static com.karry.utility.VariableConstant.INSURANCE;
import static com.karry.utility.VariableConstant.LICENCE;
import static com.karry.utility.VariableConstant.MOBILE;
import static com.karry.utility.VariableConstant.POLICE_CLEAR;
import static com.karry.utility.VariableConstant.PRIVACY;
import static com.karry.utility.VariableConstant.REGISTRATION;
import static com.karry.utility.VariableConstant.TITLE;
import static com.karry.utility.VariableConstant.TRIGGER;
import static com.karry.utility.VariableConstant.URL;
import static com.karry.utility.VariableConstant.USER_ID;


/**
 * <h1>SignUpDocumentActivity</h1>
 * <p>the Sign up Document View </p>
 */
public class SignUpDocumentActivity extends DaggerAppCompatActivity implements
        SignUpDocumentContract.SignUpDocumentView {

    @Inject SignUpDocumentContract.SignUpDocumentPresenter signUpDocumentPresenter;
    @Inject AppTypeFace appTypeFace;
    private  ImageEditUpload imageEditUpload;

    @BindView(R.id.tv_title) TextView tv_title;
    @BindString(R.string.signup_) String title;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.tv_uploadDoc)  TextView tv_uploadDoc;
    @BindView(R.id.tv_finish)  TextView tv_finish;

    @BindViews({R.id.tv_licence_head,
            R.id.tv_registration_head,
            R.id.tv_insurance_head,
            R.id.tv_policeClear_head,
            R.id.tv_Inspection_Report_head,
            R.id.tv_goodsInsurance_head,
            R.id.tv_childrenCard_head,}) List<TextView> tv_image_head;

    @BindViews({R.id.ll_licence_doc,
            R.id.ll_registration_doc,
            R.id.ll_insurance_doc,
            R.id.ll_policeClear_doc,
            R.id.ll_Inspection_Report_doc,
            R.id.ll_goodsInsurance_doc,
            R.id.ll_childrenCard_doc}) List<LinearLayout> ll_document_upload;

    @BindViews({R.id.rv_licence_doc,
            R.id.rv_registration_doc,
            R.id.rv_insurance_doc,
            R.id.rv_policeClear_doc,
            R.id.rv_Inspection_Report_doc,
            R.id.rv_goodsInsurance_doc,
            R.id.rv_childrenCard_doc}) List<RecyclerView> rv_document_upload;

    @BindViews({R.id.et_licenceExpir,
            R.id.et_policeClear,
            R.id.et_childrenExpr,
            R.id.et_vehRegExpr,
            R.id.et_vehInsExpr,
            R.id.et_inspectionDate,
            R.id.et_goodsTransitInsu}) List<EditText> et_Date;



    @BindArray(R.array.uploadDoc) String[] imageType;
    @BindString(R.string.privacyPolicy) String privacyPolicy;
    @BindString(R.string.privacy_policy_err) String privacy_policy_err;

    private String imageTypeSelected;

    @BindView(R.id.tv_readAndAcept) TextView tv_readAndAcept;
    @BindView(R.id.tv_privacyPolicy) TextView tv_privacyPolicy;
    @BindView(R.id.cb_privacyPolicy) CheckBox cb_privacyPolicy;

    @BindView(R.id.tv_driverDocument_head) TextView tv_driverDocument_head;
    @BindView(R.id.tv_vehicleDocument_head) TextView tv_vehicleDocument_head;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.til_policeClear) TextInputLayout til_policeClear;
    @BindView(R.id.til_childrenExpr) TextInputLayout til_childrenExpr;
    @BindView(R.id.til_goodsTransitInsu) TextInputLayout til_goodsTransitInsu;
    @BindView(R.id.til_inspectionDate) TextInputLayout til_inspectionDate;

    @Inject ImageUploadRVA rva_licence_img;
    @Inject ImageUploadRVA rva_vehicle_reg;
    @Inject ImageUploadRVA rva_vehicle_insurance;
    @Inject ImageUploadRVA rva_police_clearance;
    @Inject ImageUploadRVA rva_inspection_report;
    @Inject ImageUploadRVA rva_goods_insurance;
    @Inject ImageUploadRVA rva_children_card;

    @BindString(R.string.app_name) String app_name;
    @BindColor(R.color.gray) int grey_bg;

    private ArrayList<String> al_licence_img, al_vehicle_reg, al_vehicle_insurance, al_police_clearance, al_inspection_report, al_goods_insurance, al_children_card;
    private int count_licence_img, count_vehicle_reg, count_vehicle_insurance, count_police_clearance, count_inspection_report, count_goods_insurance, count_children_card;

    private Calendar calendar;
    private String dateSelect;
    private String date_licence = "",date_police = "",date_children = "",date_registartion = "",date_insurance = "",date_inspection= "",date_goodsInsurance = "";

    @BindView(R.id.et_inspectionDate) EditText et_inspectionDate;
    @BindView(R.id.et_policeClear) EditText et_policeClear;
    @BindView(R.id.et_childrenExpr) EditText et_childrenExpr;
    @BindView(R.id.et_goodsTransitInsu) EditText et_goodsTransitInsu;

    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_document);
        overridePendingTransition(R.anim.bottem_slide_down, R.anim.stay_activity);

        ButterKnife.bind(this);
        signUpDocumentPresenter.setActionBar();
        initializeViews();
        rva_initialize();
    }

    /**
     * <h1>initializeViews</h1>
     * <p>this is the method for initilizing the view, and set the font type of the UI.</p>
     */
    private void initializeViews() {
        calendar = Calendar.getInstance();

        tv_uploadDoc.setTypeface(appTypeFace.getPro_News());
        tv_finish.setEnabled(false);
        tv_finish.setTypeface(appTypeFace.getPro_narMedium());

        al_licence_img = new ArrayList<>();
        al_vehicle_reg = new ArrayList<>();
        al_vehicle_insurance = new ArrayList<>();
        al_police_clearance = new ArrayList<>();
        al_inspection_report = new ArrayList<>();
        al_goods_insurance = new ArrayList<>();
        al_children_card = new ArrayList<>();


        tv_readAndAcept.setTypeface(appTypeFace.getPro_News());
        tv_privacyPolicy.setTypeface(appTypeFace.getPro_News());
        tv_driverDocument_head.setTypeface(appTypeFace.getPro_News());
        tv_vehicleDocument_head.setTypeface(appTypeFace.getPro_News());

        for(int i=0;i<tv_image_head.size();i++)
        {
            tv_image_head.get(i).setTypeface(appTypeFace.getPro_News());
        }

        for(int i=0;i<et_Date.size();i++)
        {
            et_Date.get(i).setTypeface(appTypeFace.getPro_News());
            et_Date.get(i).setInputType(InputType.TYPE_NULL);
        }
        signUpDocumentPresenter.getVehicleData(getIntent().getExtras());
    }

    public void enablePoliceClearenceExpDate()
    {
        et_policeClear.setEnabled(true);
        til_policeClear.setEnabled(true);
    }

    public void enablechildrenCertificate()
    {
        et_childrenExpr.setEnabled(true);
        til_childrenExpr.setEnabled(true);
    }

    public void enableInspectionRepo()
    {
        et_inspectionDate.setEnabled(true);
        til_inspectionDate.setEnabled(true);
    }

    public void enableGoods()
    {
        et_goodsTransitInsu.setEnabled(true);
        til_goodsTransitInsu.setEnabled(true);
    }


    /**
     * <h1>rva_initilize</h1>
     * <p>this is the method for initialize the recyclerView Adapter for entire image upload.</p>
     */
    private void rva_initialize(){

        LinearLayoutManager lm_licence_img = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        rv_document_upload.get(0).setLayoutManager(lm_licence_img);
        rva_licence_img.setImageFile(al_licence_img,null);
        rva_licence_img.getRemoveImage(position -> {
            al_licence_img.remove(position);
            count_licence_img--;
            rva_licence_img.notifyDataSetChanged();
            ll_document_upload.get(0).setVisibility(View.VISIBLE);
//            signUpDocumentPresenter.validateDocument(count_licence_img,count_vehicle_reg,count_vehicle_insurance);
            signUpDocumentPresenter.validateDocument(count_licence_img, count_vehicle_reg, count_vehicle_insurance, count_police_clearance, count_inspection_report, count_goods_insurance, count_children_card,
                    date_licence,date_police ,date_children,date_registartion,date_insurance,date_inspection,date_goodsInsurance);
        });
        rv_document_upload.get(0).setAdapter(rva_licence_img);
        rva_licence_img.notifyDataSetChanged();

        LinearLayoutManager lm_vehicle_reg = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        rv_document_upload.get(1).setLayoutManager(lm_vehicle_reg);
        rva_vehicle_reg.setImageFile(al_vehicle_reg,null);
        rva_vehicle_reg.getRemoveImage(position -> {
            al_vehicle_reg.remove(position);
            count_vehicle_reg--;
            rva_vehicle_reg.notifyDataSetChanged();
            ll_document_upload.get(1).setVisibility(View.VISIBLE);
            signUpDocumentPresenter.validateDocument(count_licence_img, count_vehicle_reg, count_vehicle_insurance, count_police_clearance, count_inspection_report, count_goods_insurance, count_children_card,
                    date_licence,date_police ,date_children,date_registartion,date_insurance,date_inspection,date_goodsInsurance);        });
        rv_document_upload.get(1).setAdapter(rva_vehicle_reg);
        rva_vehicle_reg.notifyDataSetChanged();

        LinearLayoutManager lm_vehicle_insurance = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        rv_document_upload.get(2).setLayoutManager(lm_vehicle_insurance);
        rva_vehicle_insurance.setImageFile(al_vehicle_insurance,null);
        rva_vehicle_insurance.getRemoveImage(position -> {
            al_vehicle_insurance.remove(position);
            count_vehicle_insurance--;
            rva_vehicle_insurance.notifyDataSetChanged();
            ll_document_upload.get(2).setVisibility(View.VISIBLE);
            signUpDocumentPresenter.validateDocument(count_licence_img, count_vehicle_reg, count_vehicle_insurance, count_police_clearance, count_inspection_report, count_goods_insurance, count_children_card,
                    date_licence,date_police ,date_children,date_registartion,date_insurance,date_inspection,date_goodsInsurance);        });
        rv_document_upload.get(2).setAdapter(rva_vehicle_insurance);
        rva_vehicle_insurance.notifyDataSetChanged();

        LinearLayoutManager lm_police_clearance = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        rv_document_upload.get(3).setLayoutManager(lm_police_clearance);
        rva_police_clearance.setImageFile(al_police_clearance,"police_licence");
        rva_police_clearance.getRemoveImage(position -> {
            al_police_clearance.remove(position);
            count_police_clearance--;
            rva_police_clearance.notifyDataSetChanged();
            ll_document_upload.get(3).setVisibility(View.VISIBLE);
            signUpDocumentPresenter.validateDocument(count_licence_img, count_vehicle_reg, count_vehicle_insurance, count_police_clearance, count_inspection_report, count_goods_insurance, count_children_card,
                    date_licence,date_police ,date_children,date_registartion,date_insurance,date_inspection,date_goodsInsurance);
        });
        rv_document_upload.get(3).setAdapter(rva_police_clearance);
        rva_police_clearance.notifyDataSetChanged();

        LinearLayoutManager lm_inspection_report = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        rv_document_upload.get(4).setLayoutManager(lm_inspection_report);
        rva_inspection_report.setImageFile(al_inspection_report,"inspection_report");
        rva_inspection_report.getRemoveImage(position -> {
            al_inspection_report.remove(position);
            count_inspection_report--;
            rva_inspection_report.notifyDataSetChanged();
            ll_document_upload.get(4).setVisibility(View.VISIBLE);
            signUpDocumentPresenter.validateDocument(count_licence_img, count_vehicle_reg, count_vehicle_insurance, count_police_clearance, count_inspection_report, count_goods_insurance, count_children_card,
                    date_licence,date_police ,date_children,date_registartion,date_insurance,date_inspection,date_goodsInsurance);
        });
        rv_document_upload.get(4).setAdapter(rva_inspection_report);
        rva_inspection_report.notifyDataSetChanged();

        LinearLayoutManager lm_goods_insurance = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        rv_document_upload.get(5).setLayoutManager(lm_goods_insurance);
        rva_goods_insurance.setImageFile(al_goods_insurance,"goods_report");
        rva_goods_insurance.getRemoveImage(position -> {
            al_goods_insurance.remove(position);
            count_goods_insurance--;
            rva_goods_insurance.notifyDataSetChanged();
            ll_document_upload.get(5).setVisibility(View.VISIBLE);
            signUpDocumentPresenter.validateDocument(count_licence_img, count_vehicle_reg, count_vehicle_insurance, count_police_clearance, count_inspection_report, count_goods_insurance, count_children_card,
                    date_licence,date_police ,date_children,date_registartion,date_insurance,date_inspection,date_goodsInsurance);
        });
        rv_document_upload.get(5).setAdapter(rva_goods_insurance);
        rva_goods_insurance.notifyDataSetChanged();

        LinearLayoutManager lm_children_card = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        rv_document_upload.get(6).setLayoutManager(lm_children_card);
        rva_children_card.setImageFile(al_children_card,"children_licence");
        rva_children_card.getRemoveImage(position -> {
            al_children_card.remove(position);
            count_children_card--;
            rva_children_card.notifyDataSetChanged();
            ll_document_upload.get(6).setVisibility(View.VISIBLE);
            signUpDocumentPresenter.validateDocument(count_licence_img, count_vehicle_reg, count_vehicle_insurance, count_police_clearance, count_inspection_report, count_goods_insurance, count_children_card,
                    date_licence,date_police ,date_children,date_registartion,date_insurance,date_inspection,date_goodsInsurance);
        });
        rv_document_upload.get(6).setAdapter(rva_children_card);
        rva_children_card.notifyDataSetChanged();

    }

    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /**
     * <h1>datePicker</h1>
     * <p>which is the default datePicker dialog</p>
     */
    private void datePickerForExpiry()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,selectedDate,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis()+7.884e+11));
        datePickerDialog.show();
    }

    /**
     * <h1>datePicker</h1>
     * <p>which is the default datePicker dialog</p>
     */
    private void datePickerForPassedDate()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,selectedDate,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMinDate((long) (System.currentTimeMillis()-7.884e+11));
        datePickerDialog.show();
    }

    /**
     * <h1>dob</h1>
     * <p>which is the datePicker set date from the picker,
     * used in datePicker().</p>
     */
    private DatePickerDialog.OnDateSetListener selectedDate = (view, year, monthOfYear, dayOfMonth) -> {
        String selectedDate = Utility.sentingDateFormat(dayOfMonth,monthOfYear,year);
        setSelectedDate(selectedDate, dateSelect);
        signUpDocumentPresenter.validateDocument(count_licence_img, count_vehicle_reg, count_vehicle_insurance, count_police_clearance, count_inspection_report, count_goods_insurance, count_children_card,
                date_licence,date_police ,date_children,date_registartion,date_insurance,date_inspection,date_goodsInsurance);
    };

    /**
     * <h1>setSelectedDate</h1>
     * @param date
     * @param from
     */
    public void setSelectedDate(String date, String from) {
        switch (from){
            case LICENCE:
                et_Date.get(0).setText(date);
                date_licence = date;
                break;
            case POLICE_CLEAR:
                et_Date.get(1).setText(date);
                date_police = date;
                break;
            case CHILDREN_CARD_EXPIRY:
                et_Date.get(2).setText(date);
                date_children = date;
                break;
            case REGISTRATION:
                et_Date.get(3).setText(date);
                date_registartion = date;
                break;
            case INSURANCE:
                et_Date.get(4).setText(date);
                date_insurance = date;
                break;
            case INSPECTION:
                et_Date.get(5).setText(date);
                date_inspection = date;
                break;
            case GOODS_INSPECTION:
                et_Date.get(6).setText(date);
                date_goodsInsurance = date;
                break;
        }
    }


    @OnFocusChange({R.id.et_licenceExpir,
            R.id.et_policeClear,
            R.id.et_childrenExpr,
            R.id.et_vehRegExpr,
            R.id.et_vehInsExpr,
            R.id.et_inspectionDate,
            R.id.et_goodsTransitInsu})
    public void onFocusChange(View v, boolean hasFocus) {

        if(hasFocus)
            switch (v.getId())
            {
                case R.id.et_licenceExpir:
                    dateSelect = LICENCE;
                    datePickerForExpiry();
                    break;
                case R.id.et_policeClear:
                    dateSelect = POLICE_CLEAR;
                    datePickerForPassedDate();
                    break;
                case R.id.et_childrenExpr:
                    dateSelect = CHILDREN_CARD_EXPIRY;
                    datePickerForExpiry();
                    break;
                case R.id.et_vehRegExpr:
                    dateSelect = REGISTRATION;
                    datePickerForExpiry();
                    break;
                case R.id.et_vehInsExpr:
                    dateSelect = INSURANCE;
                    datePickerForExpiry();
                    break;
                case R.id.et_inspectionDate:
                    dateSelect = INSPECTION;
                    datePickerForPassedDate();
                    break;
                case R.id.et_goodsTransitInsu:
                    dateSelect = GOODS_INSPECTION;
                    datePickerForExpiry();
                    break;
            }
    }


    @OnClick({R.id.ll_licence_doc,
            R.id.ll_registration_doc,
            R.id.ll_insurance_doc,
            R.id.ll_policeClear_doc,
            R.id.ll_Inspection_Report_doc,
            R.id.ll_goodsInsurance_doc,
            R.id.ll_childrenCard_doc,
            R.id.tv_privacyPolicy,
            R.id.tv_finish,
            R.id.et_licenceExpir,
            R.id.et_policeClear,
            R.id.et_childrenExpr,
            R.id.et_vehRegExpr,
            R.id.et_vehInsExpr,
            R.id.et_inspectionDate,
            R.id.et_goodsTransitInsu,
            R.id.cb_privacyPolicy})
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.et_licenceExpir:
                dateSelect = LICENCE;
                datePickerForExpiry();
                break;
            case R.id.et_policeClear:
                dateSelect = POLICE_CLEAR;
                datePickerForPassedDate();
                break;
            case R.id.et_childrenExpr:
                dateSelect = CHILDREN_CARD_EXPIRY;
                datePickerForExpiry();
                break;
            case R.id.et_vehRegExpr:
                dateSelect = REGISTRATION;
                datePickerForExpiry();
                break;
            case R.id.et_vehInsExpr:
                dateSelect = INSURANCE;
                datePickerForExpiry();
                break;
            case R.id.et_inspectionDate:
                dateSelect = INSPECTION;
                datePickerForPassedDate();
                break;
            case R.id.et_goodsTransitInsu:
                dateSelect = GOODS_INSPECTION;
                datePickerForExpiry();
                break;


            case R.id.ll_licence_doc:
                imageEditUpload = new ImageEditUpload(this,null);
                imageTypeSelected = imageType[0];
                break;
            case R.id.ll_registration_doc:
                imageEditUpload = new ImageEditUpload(this,null);
                imageTypeSelected = imageType[1];
                break;
            case R.id.ll_insurance_doc:
                imageEditUpload = new ImageEditUpload(this,null);
                imageTypeSelected = imageType[2];
                break;
            case R.id.ll_policeClear_doc:
                imageEditUpload = new ImageEditUpload(this,null);
                imageTypeSelected = imageType[3];
                break;
            case R.id.ll_Inspection_Report_doc:
                imageEditUpload = new ImageEditUpload(this,null);
                imageTypeSelected = imageType[4];
                break;
            case R.id.ll_goodsInsurance_doc:
                imageEditUpload = new ImageEditUpload(this,null);
                imageTypeSelected = imageType[5];
                break;
            case R.id.ll_childrenCard_doc:
                imageEditUpload = new ImageEditUpload(this,null);
                imageTypeSelected = imageType[6];
                break;

            case R.id.tv_privacyPolicy:
                signUpDocumentPresenter.setWebUrl(PRIVACY);
                break;

            case R.id.tv_finish:
                checkBoxCheck();
                break;
            case R.id.cb_privacyPolicy:
                signUpDocumentPresenter.isPrivacypolicyAccepted(cb_privacyPolicy.isChecked());
                break;
        }

    }



    @Override
    public void openWebView(String url) {
        Intent intent = new Intent(SignUpDocumentActivity.this, RegisterWebActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString(TITLE,privacyPolicy);
        mBundle.putString(FROM, PRIVACY);
        mBundle.putString(URL, url);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    /**
     * <h1>checkBoxCheck</h1>
     * <p>the method is which check the privacy policy is checked or not and if it checked,
     * it will call the API for signUp or will show the toast message for check the policy.</p>
     */
    private void checkBoxCheck()
    {
        if(cb_privacyPolicy.isChecked())
        {

            if(count_licence_img>1 && count_vehicle_insurance>0 && count_vehicle_reg>0 && count_police_clearance>0 )
            {
                signUpDocumentPresenter.signUpAPI(
                        /*signUpDocumentPresenter.getDocumentString(al_licence_img)*/al_licence_img.get(0),al_licence_img.get(1), signUpDocumentPresenter.getDocumentString(al_vehicle_reg),
                        signUpDocumentPresenter.getDocumentString(al_vehicle_insurance), signUpDocumentPresenter.getDocumentString(al_police_clearance),
                        signUpDocumentPresenter.getDocumentString(al_inspection_report), signUpDocumentPresenter.getDocumentString(al_goods_insurance),
                        signUpDocumentPresenter.getDocumentString(al_children_card),
                        date_licence,date_insurance,date_registartion,date_police,date_inspection,date_goodsInsurance,date_children);
            }

        }
        else {
            Utility.BlueToast(this,privacy_policy_err);
        }
    }

    @Override
    public void checkBoxHandler()
    {
        if(count_licence_img>1 && count_vehicle_insurance>0 && count_vehicle_reg> 0 && count_police_clearance>0 )
            enableNext();

    }



    /**********************************************************************************************/

    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_signup_close);
        }
        tv_title.setTypeface(appTypeFace.getPro_narMedium());
        signUpDocumentPresenter.setActionBarTitle();
    }

    @Override
    public void setTitle() {
        tv_title.setText(title);
    }

    /**********************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay_activity, R.anim.bottem_slide_up);
    }

    /**********************************************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case CAMERA_PIC:
                startCropImage();
                break;

            case GALLERY_PIC:
                try {
                    String takenNewImage;
                    String state = Environment.getExternalStorageState();
                    takenNewImage = app_name + String.valueOf(System.nanoTime()) + ".png";

                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        VariableConstant.newFile = new File(Environment.getExternalStorageDirectory(), takenNewImage);
                    } else {
                        VariableConstant.newFile = new File(getFilesDir(), takenNewImage);
                    }

                    InputStream inputStream = getContentResolver().openInputStream(
                            data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(VariableConstant.newFile.getAbsolutePath()));

                    Utility.copyStream(inputStream, fileOutputStream);

                    fileOutputStream.close();
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);
                    startCropImage();
                } catch (Exception e) {
                    Utility.printLog("Error"+e.getMessage());
                }
                break;

            case CROP_IMAGE:
                if (data!=null)
                    amzonUpload(data);

                break;

            default:
                break;
        }
    }


    /**
     * <h1>startCropImage</h1>
     * <p>intent for Crop the Image selected</p>
     */
    private void startCropImage()
    {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, VariableConstant.newFile.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 0);
        intent.putExtra(CropImage.ASPECT_Y, 0);
        startActivityForResult(intent, CROP_IMAGE);
    }

    /**
     * <h1>amzonUpload</h1>
     * <p>this is the method for assign the amazon folder for the upload image and upload the image.</p>
     * @param data : image data from selected image which pass to upload image.
     */
    private void amzonUpload(Intent data)
    {
        String BUCKETSUBFOLDER = null;

        if(imageTypeSelected.equals(imageType[0])){
            BUCKETSUBFOLDER=VariableConstant.DRIVING_LICENCE;
        }
        if(imageTypeSelected.equals(imageType[1])){
            BUCKETSUBFOLDER=VariableConstant.VEHICLE_REGISTRATION;
        }
        if(imageTypeSelected.equals(imageType[2])){
            BUCKETSUBFOLDER=VariableConstant.VEHICLE_INSURANCE;
        }
        if(imageTypeSelected.equals(imageType[3])){
            BUCKETSUBFOLDER=VariableConstant.POLICE_CLEARNCE;
        }
        if(imageTypeSelected.equals(imageType[4])){
            BUCKETSUBFOLDER=VariableConstant.INSPECTION_REPORT;
        }
        if(imageTypeSelected.equals(imageType[5])){
            BUCKETSUBFOLDER=VariableConstant.GOODS_INSURANCE;
        }
        if(imageTypeSelected.equals(imageType[6])){
            BUCKETSUBFOLDER=VariableConstant.CHILDREN_CARD;
        }

        Utility.printLog("the amazon upload : "+BUCKETSUBFOLDER+"  "+imageTypeSelected);

        signUpDocumentPresenter.cropImage(data,BUCKETSUBFOLDER);

    }
    /**********************************************************************************************/
    @Override
    public void amazonUploadSuccess(String url) {

        Utility.printLog("baby" +url);

        if(imageTypeSelected.equals(imageType[0])){
            al_licence_img.add(count_licence_img,url);
            count_licence_img++;
            rva_licence_img.notifyDataSetChanged();
            Utility.printLog("the count is : "+count_licence_img);
            if(count_licence_img>1)
            {
                ll_document_upload.get(0).setVisibility(View.GONE);
            }
        }

        if(imageTypeSelected.equals(imageType[1])){
            al_vehicle_reg.add(count_vehicle_reg,url);
            count_vehicle_reg++;
            rva_vehicle_reg.notifyDataSetChanged();
            if(count_vehicle_reg>0)
            {
                ll_document_upload.get(1).setVisibility(View.GONE);
            }

        }

        if(imageTypeSelected.equals(imageType[2])){
            al_vehicle_insurance.add(count_vehicle_insurance,url);
            count_vehicle_insurance++;
            rva_vehicle_insurance.notifyDataSetChanged();
            if(count_vehicle_insurance>0)
            {
                ll_document_upload.get(2).setVisibility(View.GONE);
            }
        }
        if(imageTypeSelected.equals(imageType[3])){
            al_police_clearance.add(count_police_clearance,url);
            count_police_clearance++;
            rva_police_clearance.notifyDataSetChanged();
            if(count_police_clearance>0)
            {
                ll_document_upload.get(3).setVisibility(View.GONE);
            }
        }
        if(imageTypeSelected.equals(imageType[4])){
            al_inspection_report.add(count_inspection_report,url);
            count_inspection_report++;
            rva_inspection_report.notifyDataSetChanged();
            if(count_inspection_report>0)
            {
                ll_document_upload.get(4).setVisibility(View.GONE);
            }
        }
        if(imageTypeSelected.equals(imageType[5])){
            al_goods_insurance.add(count_goods_insurance,url);
            count_goods_insurance++;
            rva_goods_insurance.notifyDataSetChanged();
            if(count_goods_insurance>0)
            {
                ll_document_upload.get(5).setVisibility(View.GONE);
            }
        }
        if(imageTypeSelected.equals(imageType[6])){
            al_children_card.add(count_children_card,url);
            count_children_card++;
            rva_children_card.notifyDataSetChanged();
            if(count_children_card>0)
            {
                ll_document_upload.get(6).setVisibility(View.GONE);
            }
        }

        //check whether the document uploaded or not for finish button.
        signUpDocumentPresenter.validateDocument(count_licence_img, count_vehicle_reg, count_vehicle_insurance, count_police_clearance, count_inspection_report, count_goods_insurance, count_children_card,
                date_licence,date_police ,date_children,date_registartion,date_insurance,date_inspection,date_goodsInsurance);
    }

    @Override
    public void signUpSuccess(String message, String phone, String countryCode) {
        Intent intent = new Intent(this, OTPVerificationActivity.class);
        intent.putExtra(MOBILE, phone);
        intent.putExtra(COUNTRY_CODE, countryCode);
        intent.putExtra(USER_ID, message);
        intent.putExtra(TRIGGER, 1);
        startActivity(intent);

    }

    @Override
    public void enableNext() {
        tv_finish.setEnabled(true);
        tv_finish.setBackgroundResource(R.drawable.selector_layout);
    }

    @Override
    public void disableNext() {
        tv_finish.setBackgroundColor(grey_bg);
        tv_finish.setEnabled(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        signUpDocumentPresenter.compositeDisposableClear();

    }
}
