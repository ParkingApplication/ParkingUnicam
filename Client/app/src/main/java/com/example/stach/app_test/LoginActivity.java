package com.example.stach.app_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.*;
import android.os.AsyncTask;
import org.json.*;
import java.util.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;

public class LoginActivity extends AppCompatActivity implements CustomCallback {
    //text file for save login in local file
    File login_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.recruitData();
    }

    //METODO DA TESTARE!!!!!!!!!!!!!!!!!!
    //Metodo utilizzato per loggare
    public boolean Login(String username,String password)
    {

        try{

            //Invio i dati al server
            URL url = new URL(Parametri.IP + "/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");


            String input = "{\"username\":" + username + ",\"password\":" + password+ "}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            //Leggo la risposta dal server
            BufferedReader reader = new BufferedReader(new InputStreamReader (conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            line = sb.toString();

            //Verifico i dati che ho letto
            JSONObject jObject = new JSONObject(line);
            JSONObject Autistajs = new JSONObject(jObject.getString("autista"));

            try{

            }
            catch(Exception e) {

            }

            String result = null;
            String token = null;
            token = jObject.getString("token");


            if(token != null) {
                Parametri.Token = token;
                return true;
            }
            else
                return false;
        }
        catch(Exception e){ //Devo scrivere eccezione dati login errati
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            return false;
        }

    }
    /**
     * This method will send data to server to verify user credentials.
     */
    public void sendDataForLogin(View view) {
        EditText mail = (EditText) findViewById(R.id.mail);
        EditText password = (EditText) findViewById(R.id.pass);
        Toast.makeText(this,"LOGIN è partito",Toast.LENGTH_SHORT).show();

      /*  //Server
        if(Login(mail.toString(),password.toString()))  //Da ricontrollare
        {
            this.saveData(mail.getText().toString(), password.getText().toString());
            //devo inviare un bundle alla prossima activity con i dati di login salvati in locale per sapere di chi è l'account
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            //il finish serve per far chiudere l'activity e non far tornare indietro l'utente, ci potrà tornare se farà log-out
            finish();
        }
        else
        {
            Toast.makeText(this,"Dati di login errati",Toast.LENGTH_SHORT).show();
        }
        */

        String user = mail.toString();
        String password1 = password.toString();
        Map<String, String> postData = new HashMap<>();
        postData.put("username", user);
        postData.put("password",password1);
        Connessione conn = new Connessione(postData, "POST", this);
        conn.doInBackground(Parametri.IP + "/login");


        this.saveData(mail.getText().toString(), password.getText().toString());
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();

    }

    @Override
    public void execute(String response, int statusCode)
    {
        try {
            JSONObject jObject = new JSONObject(response);
            JSONObject Autistajs = new JSONObject(jObject.getString("autista"));
            Toast.makeText(this,"Autista loggato" + response,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }

    /**
     * This method is use to save user data after login, to allow user to overdrop the login activity if is logged yet.
     */
    public void saveData(String mail, String password) {
       login_file = new File(this.getFilesDir(), "LoginStats.txt");
        //try to write in file
        try {
            BufferedWriter fos = new BufferedWriter(new FileWriter(login_file.getAbsolutePath()));
            fos.write(mail + " ");
            fos.write(password + " ");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is use to recruit user data before login, to allow user to overdrop the login activity if is logged yet.
     */
    public void recruitData() {
        EditText mail = (EditText) findViewById(R.id.mail);
        EditText pass = (EditText) findViewById(R.id.pass);
        //try to read in file
        try {
            String appoggio;
            BufferedReader fos1 = new BufferedReader(new FileReader(login_file.getAbsolutePath()));
            StringBuilder sb = new StringBuilder();
            while ((appoggio = fos1.readLine()) != null) {
                sb.append(appoggio);
            }
            fos1.close();
            //tokenize string builder
            StringTokenizer sT = new StringTokenizer(sb.toString());
            //assignment
            String mailResult = sT.nextToken();
            String passwordResult = sT.nextToken();
            //put result
            mail.setText(mailResult);
            pass.setText(passwordResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method allow user to go signUp activity
     */
    public void goToSignUp(View view) {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    /**
     * This method allow user to go map google activity
     */
    public void goToMap(View view){
        startActivity(new Intent(LoginActivity.this,MapActivity.class));
    }
}
