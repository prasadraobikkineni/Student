package com.studentparty.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.studentparty.R;
import com.studentparty.controller.Constants;

public class Splashscreen extends AppCompatActivity {

    private Handler mProgressHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        mProgressHandler.postDelayed(mSplashScreen, Constants.SPLASH_TIME_OUT);
    }

    private final Runnable mSplashScreen = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(Splashscreen.this, LoginScreen.class);
            startActivity(i);

            // close this activity
            finish();
        }
    };
}
