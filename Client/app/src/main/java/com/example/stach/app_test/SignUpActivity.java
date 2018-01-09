package com.example.stach.app_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    /**
     * This method will send user credentials for registration.
     */
    public void sendDataForSignUp(View view){
        //fa cose
    }

    /**
     * This method allow user to return to login activity
     */
    public void returnToLogin(View view){
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
    }
}
