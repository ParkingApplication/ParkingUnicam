package com.example.stach.app_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
        String email = "";
        String password = "";
        if(Invio_dati(email,password))
        {}//Va avanti .-.

    }

    public boolean Invio_dati(String mail,String password)
    {
        try{
            URL url = new URL("Parametri.IP");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");


            String input = "{\"email\":" + mail + ",\"password\":" + password+ "}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            //Gestione risposta.
            return true;
        }
        catch(Exception e){ //Devo scrivere eccezione dati login errati
            return false;
        }

    }
    /**
     * This method allow user to return to login activity
     */
    public void returnToLogin(View view){
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
    }
}
