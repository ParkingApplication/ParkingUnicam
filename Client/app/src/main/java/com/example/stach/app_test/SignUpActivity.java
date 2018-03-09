package com.example.stach.app_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignUpActivity extends AppCompatActivity implements CustomCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }

    /**
     * This method will send user credentials for registration.
     */
    public void sendDataForSignUp(View view){




    }
    //Callback
    @Override
    public void execute(String response, int statusCode)
    {

        try {
            JSONObject result = new JSONObject(response);
            JSONObject codice = new JSONObject(result.getString("successful"));
            if(codice.getString("codice") == "200") {
                Toast.makeText(this,"SignUp Riuscito!" + response,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
            else
            {
                Toast.makeText(this,"ERRORE!" + response,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }



        } catch (Exception e) {

        }
    }

    /**
     * This method allow user to return to login activity
     */
    public void returnToLogin(View view){
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
    }
}
