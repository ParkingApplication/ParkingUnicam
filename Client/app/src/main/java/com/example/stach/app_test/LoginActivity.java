package com.example.stach.app_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
    }

    /**
     * This method will send data to server to verify user credentials.
     */
    public void sendDataForLogin(View view){
        EditText editT =(EditText) findViewById(R.id.mail);
        editT.setText("DovreiInviareIdatiMaNonHoUnserver");
    }
    /**
     * This method allow user to go signUp activity
     */
    public void goToSignUp(View view){
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }
    /**
     * This method allow user to go gps activity
     */
    public void goToGps(View view){
        startActivity(new Intent(LoginActivity.this, GPSActivity.class));
    }

}
