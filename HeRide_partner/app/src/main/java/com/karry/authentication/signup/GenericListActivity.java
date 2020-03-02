package com.karry.authentication.signup;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.karry.adapter.GenericListAdapter;
import com.heride.partner.R;

import java.util.ArrayList;

import static com.karry.utility.VariableConstant.DATA;
import static com.karry.utility.VariableConstant.TITLE;
import static com.karry.utility.VariableConstant.TYPE;

/**
 * Created by Admin on 8/1/2017.
 */

public class GenericListActivity extends AppCompatActivity {
    private RecyclerView recyclerList;
    private GenericListAdapter adapter;
    private Typeface ClanaproNarrMedium,ClanaproNarrNews;
    private TextView tv_done;
    private String type;
    private ArrayList data;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_an_operator);
        overridePendingTransition(R.anim.bottem_slide_down, R.anim.stay_activity);

        ClanaproNarrMedium = Typeface.createFromAsset(getAssets(), "fonts/ClanPro-NarrMedium.otf");
        ClanaproNarrNews = Typeface.createFromAsset(getAssets(), "fonts/ClanPro-NarrNews.otf");

        Bundle mBundle = getIntent().getExtras();
        type = mBundle.getString(TYPE);
        String title = mBundle.getString(TITLE);
        data = (ArrayList) mBundle.getSerializable(DATA);

        adapter = new GenericListAdapter(GenericListActivity.this, data, type);

        initActionBar(title);
        initializeViews();

        if (data != null) {

            recyclerList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void initializeViews() {

        recyclerList = findViewById(R.id.rv_operators_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(GenericListActivity.this);
        recyclerList.setLayoutManager(layoutManager);
        //recyclerList.addItemDecoration(new SimpleItemDecorative(getApplicationContext()));
    }

    private void initActionBar(String title) {
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_signup_close);
        }
        ImageView iv_search;
        TextView tv_title;

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_title.setTypeface(ClanaproNarrMedium);

        tv_done = findViewById(R.id.tv_done);
        tv_done.setTypeface(ClanaproNarrMedium);
        tv_done.setVisibility(View.VISIBLE);
        tv_done.setOnClickListener(adapter);


        iv_search = findViewById(R.id.iv_search);
        iv_search.setVisibility(View.GONE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(TYPE, type);
        intent.putExtra(DATA, data);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.stay_activity, R.anim.bottem_slide_up);
    }

    public void sendResult(ArrayList list, String type) {
        Intent intent = new Intent();
        intent.putExtra(TYPE, type);
        intent.putExtra(DATA, list);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }
}
