package com.example.stach.app_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import org.json.*;
import java.util.*;
import android.app.AlertDialog.Builder;

public class LoginActivity extends AppCompatActivity {
    //text file for save login in local file
    File login_file;
    static ProgressDialog caricamento = null;
    Context context = LoginActivity.this;
    Activity activity = LoginActivity.this;


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

    /**
     * This method will send data to server to verify user credentials.
     */
    public void sendDataForLogin(View view) {

        EditText mail = (EditText) findViewById(R.id.mail);
        EditText password = (EditText) findViewById(R.id.pass);


        // Prelevo i dati per il login per inviarli al server.
        String user = mail.getText().toString();
        String password1 = password.getText().toString();
        try {
            password1 = SHA1(password1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Inserisco i dati del login in un HashMap così da poterli convertire facilmente in JSonObject in seguito
        JSONObject postData = new JSONObject();
        try {
            postData.put("username", user);
            postData.put("password", password1);
        }catch (Exception e){}

        // Avverto l'utente del tentativo di invio dei dati di login al server
        caricamento = ProgressDialog.show(LoginActivity.this, "",
               "Connessione con il server in corso...", true);

        // Creo ed eseguo una connessione con il server web
        Connessione conn = new Connessione(postData, "POST",context,activity);
        conn.execute(Parametri.IP + "/login");

        // Ha senso salvare questi dati senza aver verificato che siano corretti ?
        this.saveData(mail.getText().toString(), password.getText().toString());

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

    public void backdoor(View view){
        startActivity(new Intent(context, MainActivity.class));
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
        finish();
    }

    /**
     * This method allow user to go signUp activity
     */
    public void goToPasswordRecovery(View view) {
        startActivity(new Intent(LoginActivity.this, PasswordRecoveryActivity.class));
        finish();
    }

    /**
     * This method allow user to go map google activity
     */
    public void goToMap(View view) {
        startActivity(new Intent(LoginActivity.this, MapActivity.class));
    }
    //Criptazione SHA1
    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                }
                else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }
}