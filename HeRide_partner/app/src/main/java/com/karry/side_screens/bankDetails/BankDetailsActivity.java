package com.karry.side_screens.bankDetails;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heride.partner.R;

import java.util.Calendar;

public class BankDetailsActivity extends AppCompatActivity {

    TextView tv_accholdname, tv_pid, tv_routing_no, tv_acc_num, tv_state, tv_postal_code, tv_city, tv_address, tv_id_proof, tv_dob;
    TextView tv_accholdname1, tv_pid1, tv_routing_no1, tv_acc_num1, tv_state1, tv_postal_code1, tv_city1, tv_address1, tv_id_proof1, tv_dob1;
    EditText et_accholdname, et_pid, et_routing_no, et_acc_num, et_state, et_postal_code, et_city, et_address, et_id_proof;
    ImageView iv_add_addr;
    TextView et_dob;
    TextView tvSave, tvEdit;
    TextView tv_header;
    TextView tv_delete_acc, tv_set_default;
    LinearLayout ll_btns, ll_pid, ll_dob;
    TextView tv_title;
    private int currentYear;
    private int month;
    private int day;
    private String editedDOBStr;
    private ImageView iv_acc_hold;
    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);

        initActionBar();
//        initLayout();
    }


    /**********************************************************************************************/
    /**
     * <h1>initActionBar</h1>
     * initilize the action bar
     */
    private void initActionBar() {
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_back_btn);
        }


        Typeface ClanaproNarrMedium = Typeface.createFromAsset(getAssets(), "fonts/ClanPro-NarrMedium.otf");
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setTypeface(ClanaproNarrMedium);

        tv_title.setText(getResources().getString(R.string.bank_details));

    }

    /**********************************************************************************************/
    /*<h1>initLayout</h1>
    * <p>initilize the bank details activity</p>*/
    /*private void initLayout()
    {
        Typeface latoRegular = Typeface.createFromAsset(getAssets(), "fonts/ClanPro-NarrMedium.otf");
        Typeface oswalLight = Typeface.createFromAsset(getAssets(), "fonts/ClanPro-NarrNews.otf");

        c=Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        tvSave = (TextView) findViewById(R.id.tvSave);
        tvSave.setTypeface(oswalLight);

        tvEdit = (TextView) findViewById(R.id.tvEdit);
        tvEdit.setTypeface(oswalLight);



        tv_accholdname = (TextView)findViewById(R.id.tv_accholdname);
        tv_accholdname.setTypeface(latoRegular);

        tv_dob = (TextView)findViewById(R.id.tv_dob);
        tv_dob.setTypeface(latoRegular);

        tv_pid = (TextView)findViewById(R.id.tv_pid);
        tv_pid.setTypeface(latoRegular);

        tv_pid = (TextView)findViewById(R.id.tv_pid);
        tv_pid.setTypeface(latoRegular);

        tv_routing_no = (TextView)findViewById(R.id.tv_routing_no);
        tv_routing_no.setTypeface(latoRegular);

        tv_acc_num = (TextView)findViewById(R.id.tv_acc_num);
        tv_acc_num.setTypeface(latoRegular);

        tv_state = (TextView)findViewById(R.id.tv_state);
        tv_state.setTypeface(latoRegular);

        tv_postal_code = (TextView)findViewById(R.id.tv_postal_code);
        tv_postal_code.setTypeface(latoRegular);

        tv_city = (TextView)findViewById(R.id.tv_city);
        tv_city.setTypeface(latoRegular);

        tv_address = (TextView)findViewById(R.id.tv_address);
        tv_address.setTypeface(latoRegular);

        tv_id_proof = (TextView)findViewById(R.id.tv_id_proof);
        tv_id_proof.setTypeface(latoRegular);

        *//**//*

        tv_accholdname1 = (TextView)findViewById(R.id.tv_accholdname1);
        tv_accholdname1.setTypeface(latoRegular);

        tv_pid1 = (TextView)findViewById(R.id.tv_pid1);
        tv_pid1.setTypeface(latoRegular);

        tv_routing_no1 = (TextView)findViewById(R.id.tv_routing_no1);
        tv_routing_no1.setTypeface(latoRegular);

        tv_acc_num1 = (TextView)findViewById(R.id.tv_acc_num1);
        tv_acc_num1.setTypeface(latoRegular);

        tv_state1 = (TextView)findViewById(R.id.tv_state1);
        tv_state1.setTypeface(latoRegular);

        tv_postal_code1 = (TextView)findViewById(R.id.tv_postal_code1);
        tv_postal_code1.setTypeface(latoRegular);

        tv_city1 = (TextView)findViewById(R.id.tv_city1);
        tv_city1.setTypeface(latoRegular);

        tv_address1 = (TextView)findViewById(R.id.tv_address1);
        tv_address1.setTypeface(latoRegular);

       *//* tv_id_proof1 = (TextView)findViewById(R.id.tv_id_proof1);
        tv_id_proof1.setTypeface(latoRegular);*//*

        tv_dob1 = (TextView)findViewById(R.id.tv_dob1);
        tv_dob1.setTypeface(latoRegular);

         *//**//*

        et_accholdname= (EditText) findViewById(R.id.et_accholdname);
        et_accholdname.setTypeface(latoRegular);

        et_pid= (EditText) findViewById(R.id.et_pid);
        et_pid.setTypeface(latoRegular);

        et_routing_no= (EditText) findViewById(R.id.et_routing_no);
        et_routing_no.setTypeface(latoRegular);

        et_acc_num= (EditText) findViewById(R.id.et_acc_num);
        et_acc_num.setTypeface(latoRegular);

        et_state= (EditText) findViewById(R.id.et_state);
        et_state.setTypeface(latoRegular);

        et_dob= (TextView) findViewById(R.id.et_dob);
        et_dob.setTypeface(latoRegular);

        et_postal_code= (EditText) findViewById(R.id.et_postal_code);
        et_postal_code.setTypeface(latoRegular);

        et_city= (EditText) findViewById(R.id.et_city);
        et_city.setTypeface(latoRegular);

        et_address= (EditText) findViewById(R.id.et_address);
        et_address.setTypeface(latoRegular);

       *//* et_id_proof= (EditText) findViewById(R.id.et_id_proof);
        et_id_proof.setTypeface(latoRegular);*//*

        tv_delete_acc= (TextView) findViewById(R.id.tv_delete_acc);
        tv_delete_acc.setTypeface(latoRegular);

        tv_set_default= (TextView) findViewById(R.id.tv_set_default);
        tv_set_default.setTypeface(latoRegular);

        iv_acc_hold = (ImageView)findViewById(R.id.iv_acc_hold);

        ll_btns= (LinearLayout) findViewById(R.id.ll_btns);
        ll_dob= (LinearLayout) findViewById(R.id.ll_dob);
        ll_pid= (LinearLayout) findViewById(R.id.ll_pid);

        iv_add_addr= (ImageView) findViewById(R.id.iv_add_addr);
        iv_add_addr.setOnClickListener(this);

        String add=getIntent().getStringExtra("add");
        if(add.equals("1"))
        {
            tv_title.setText(getResources().getString(R.string.add_account));

            tvSave.setVisibility(View.VISIBLE);
            tvEdit.setVisibility(View.GONE);

            tv_accholdname1.setVisibility(View.GONE);
            tv_pid1.setVisibility(View.GONE);
            tv_routing_no1.setVisibility(View.GONE);
            tv_acc_num1.setVisibility(View.GONE);
            tv_state1.setVisibility(View.GONE);
            tv_postal_code1.setVisibility(View.GONE);
            tv_city1.setVisibility(View.GONE);
            tv_address1.setVisibility(View.GONE);
            tv_dob1.setVisibility(View.GONE);
            ll_btns.setVisibility(View.GONE);

            et_accholdname.setVisibility(View.VISIBLE);
            et_pid.setVisibility(View.VISIBLE);
            et_routing_no.setVisibility(View.VISIBLE);
            et_acc_num.setVisibility(View.VISIBLE);
            et_state.setVisibility(View.VISIBLE);
            et_postal_code.setVisibility(View.VISIBLE);
            et_city.setVisibility(View.VISIBLE);
            et_address.setVisibility(View.VISIBLE);
            et_dob.setVisibility(View.VISIBLE);
        }
        else if(add.equals("2")){

          *//*  BankDetailsResponse bankDetailsResponse=sessionManager.getBankDetailsResponse();
            Utility.printLog("bankdetailresponse "+bankDetailsResponse.getAccount_holder_name());

            tv_title.setText(getResources().getString(R.string.account_details));

            tvSave.setVisibility(View.GONE);
            tvEdit.setVisibility(View.GONE);

            tv_accholdname1.setVisibility(View.VISIBLE);
            tv_accholdname1.setText(bankDetailsResponse.getAccount_holder_name());
            tv_acc_num1.setVisibility(View.VISIBLE);
            tv_acc_num1.setText("*****"+bankDetailsResponse.getLast4());
            tv_routing_no1.setVisibility(View.VISIBLE);
            tv_routing_no1.setText(bankDetailsResponse.getRouting_number());
            ll_btns.setVisibility(View.VISIBLE);

            tv_pid1.setVisibility(View.GONE);
            tv_pid1.setVisibility(View.GONE);
            tv_state1.setVisibility(View.GONE);
            tv_postal_code1.setVisibility(View.GONE);
            tv_city1.setVisibility(View.GONE);
            tv_address1.setVisibility(View.GONE);
            tv_dob1.setVisibility(View.GONE);

            et_accholdname.setVisibility(View.GONE);
            et_pid.setVisibility(View.GONE);
            et_routing_no.setVisibility(View.GONE);
            et_acc_num.setVisibility(View.GONE);
            et_state.setVisibility(View.GONE);
            et_postal_code.setVisibility(View.GONE);
            et_city.setVisibility(View.GONE);
            et_address.setVisibility(View.GONE);
            et_dob.setVisibility(View.GONE);
            iv_acc_hold.setVisibility(View.GONE);

            tv_accholdname.setVisibility(View.VISIBLE);
            tv_acc_num.setVisibility(View.VISIBLE);
            tv_routing_no.setVisibility(View.VISIBLE);

            tv_pid.setVisibility(View.GONE);
            tv_state.setVisibility(View.GONE);
            tv_postal_code.setVisibility(View.GONE);
            tv_city.setVisibility(View.GONE);
            tv_address.setVisibility(View.GONE);
            tv_id_proof.setVisibility(View.GONE);
            tv_dob.setVisibility(View.GONE);
            ll_dob.setVisibility(View.GONE);
            ll_pid.setVisibility(View.GONE);*//*


        }
    }*/

    /**********************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        }
        return super.onOptionsItemSelected(item);
    }

}
