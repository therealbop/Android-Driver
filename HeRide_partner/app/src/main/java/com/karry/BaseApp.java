package com.karry;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

/*
 * <h2>BaseApp</h2>
 *
 * */
public class
BaseApp extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getCacheDir(), "responses");
    }

}
