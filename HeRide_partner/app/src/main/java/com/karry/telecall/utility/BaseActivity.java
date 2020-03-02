package com.karry.telecall.utility;

/**
 * Created by moda on 04/05/17.
 */


import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

/**
 * This is a base activity extending Fragment activity
 * Main use of this activity is to update the online status of the user
 * Every time an activity is closed user is made offline and global last seen time is saved
 * Every time an activity resumes - User is made online
 */
public class BaseActivity extends FragmentActivity {


    /* Using socket to get the list of favorites */
    //public Socket mSocket;









    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
//        try {
//            mSocket = IO.socket("http://postmenu.cloudapp.net:9001/");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}