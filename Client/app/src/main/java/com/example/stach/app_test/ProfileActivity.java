package com.example.stach.app_test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextView username = (TextView) findViewById(R.id.profile_username);
        final TextView saldo = (TextView) findViewById(R.id.profile_money);
        //listener
        View.OnClickListener l=new View.OnClickListener() {

            public void onClick(View v) {

                username.setText("stach");
                saldo.setText("1000");

            }
        };
        Button btn1 = (Button) findViewById(R.id.profile_recharge);
        btn1.setOnClickListener(l);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }



}
