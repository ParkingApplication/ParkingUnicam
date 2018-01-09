package com.example.stach.app_test;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //the handler is to showing an activity presentation for two second and then pass to login
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        }, 2000); // 2 seconds
    }



}
