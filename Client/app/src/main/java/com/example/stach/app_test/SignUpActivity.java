package com.example.stach.app_test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements TextWatcher {

    Context context = SignUpActivity.this;
    Activity activity = SignUpActivity.this;
    static ProgressDialog caricamento = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }

    /**
     * This method will send user credentials for registration.
     */
    public void sendDataForSignUp(View view){



        //Prendo i dati dalla form:
        EditText nome = (EditText) findViewById(R.id.nome);
        EditText cognome = (EditText) findViewById(R.id.cognome);
        EditText dataDinascita = (EditText) findViewById(R.id.data_nascita);
        EditText telefono = (EditText) findViewById(R.id.telefono);
        EditText username = (EditText) findViewById(R.id.username);
        EditText mail = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        EditText passwordr = (EditText) findViewById(R.id.repPass);

        //Converto i dati in stringa per inviarli al server
        String nomes = nome.getText().toString();
        String cognomes = cognome.getText().toString();
        String dataDinascitas = dataDinascita.getText().toString();
        String telefonos = telefono.getText().toString();
        String usernames = username.getText().toString();
        String mails = mail.getText().toString();
        String passwords = password.getText().toString();
        String passwordrs = passwordr.getText().toString();

        JSONObject postData = new JSONObject();

        JSONObject autista = new JSONObject();


        try {
            autista.put("username", usernames);
            autista.put("password", passwords);
            autista.put("email", mails);
            autista.put("nome", nomes);
            autista.put("cognome", cognomes);
            autista.put("dataDiNascita", dataDinascitas);
            autista.put("telefono", telefonos);
            postData.put("autista", autista);
        }catch (Exception e){
            // Gestire l'errore come se fosse l'ultimo
        }


// Avverto l'utente del tentativo di invio dei dati di login al server
        caricamento = ProgressDialog.show(SignUpActivity.this, "",
                "Connessione con il server in corso...", true);
        Connessione conn = new Connessione(postData, "POST",context,activity);
        conn.execute(Parametri.IP + "/signup");



    }


    /**
     * This method allow user to return to login activity
     */
    public void returnToLogin(View view){
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }






    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
