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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
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

    //Metodo utilizzato per loggare
    public boolean Login(String mail,String password)
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
     * This method will send data to server to verify user credentials.
     */
    public void sendDataForLogin(View view) {
        EditText mail = (EditText) findViewById(R.id.mail);
        EditText password = (EditText) findViewById(R.id.pass);

        //Server
        if(Login(mail.toString(),password.toString()))  //Da ricontrollare
        {
            this.saveData(mail.getText().toString(), password.getText().toString());
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
        else
        {
            Toast.makeText(this,"Dati di login errati",Toast.LENGTH_SHORT).show();
        }
        this.saveData(mail.getText().toString(), password.getText().toString());
        startActivity(new Intent(LoginActivity.this,MainActivity.class));

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
