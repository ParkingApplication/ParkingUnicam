package com.example.stach.app_test;
import android.content.Intent;
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
            }
        }, 2000); // 2 seconds
    }
/**
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

        //Im trying to set socket's connection
        try {

            Socket connessione = null;

            connessione = new Socket("192.168.1.67" , 5050); //<--crasha qua

            Toast.makeText(this, "Connessione riuscita", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Connessione non riuscita", Toast.LENGTH_SHORT).show();
        }
    }*/



}
