package com.example.stach.app_test;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //the handler is to showing an activity presentation for two second and then pass to login
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                finish();
            }
        }, 500); // 0.5 seconds
    }




}
