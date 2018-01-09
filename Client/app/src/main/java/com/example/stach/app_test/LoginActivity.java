package com.example.stach.app_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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
        EditText mail =(EditText) findViewById(R.id.mail);
        EditText password =(EditText) findViewById(R.id.pass);
        //contact server
        //save data
        this.saveData(mail.toString(),password.toString());
    }

    /**
     * This method is use to save user data after login, to allow user to overdrop the login activity if is logged yet.
     */
    public void saveData(String mail, String password){
        //Create file for save login
        File login_file = new File(this.getFilesDir(), "LoginStats.txt");
        try {
            BufferedWriter fos = new BufferedWriter(new FileWriter(login_file.getAbsolutePath() +"/"+"LoginStats.txt"));
            fos.write(mail.trim());
            fos.write(password.trim());
            fos.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG);
        } catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
        }
    }

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
