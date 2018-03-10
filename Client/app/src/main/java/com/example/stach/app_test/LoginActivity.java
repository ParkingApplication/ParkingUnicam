package com.example.stach.app_test;

import android.app.ProgressDialog;
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
import org.json.*;
import java.util.*;

public class LoginActivity extends AppCompatActivity implements CustomCallback {
    //text file for save login in local file
    File login_file;
    ProgressDialog dialog = null;

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
        Toast.makeText(this, "LOGIN è partito", Toast.LENGTH_SHORT).show();

        // Prelevo i dati per il login per inviarli al server.
        String user = mail.getText().toString();
        String password1 = password.getText().toString();
        // Inserisco i dati del login in un HashMap così da poterli convertire facilmente in JSonObject in seguito
        Map<String, String> postData = new HashMap<>();
        postData.put("username", user);
        postData.put("password", password1);

        // Avverto l'utente del tentativo di invio dei dati di login al server
        dialog = ProgressDialog.show(LoginActivity.this, "",
                "Connessione con il server in corso...", true);

        // Creo ed eseguo una connessione con il server web
        Connessione conn = new Connessione(postData, "POST", this);
        conn.execute(Parametri.IP + "/login");

        // Ha senso salvare questi dati senza aver verificato che siano corretti ?
        this.saveData(mail.getText().toString(), password.getText().toString());
    }

    // Callback passata al task asincrono di tipo Connessione
    @Override
    public void execute(String response, int statusCode) {
        // Chiudo la dialog del login in cirso
        dialog.dismiss();

        if (statusCode == -145) // Errore durante la connessione con il server, segnalarlo all' utente
        {
            /**
             * Possibili cause dell' errore:
             *
             * - l'utente non ha la connessione alla rete
             * - il server è offline
             * - errori di rete (vari)
             */
        } else {
            if (statusCode == 400)  // Errore segnalato dal server
            {
                /**
                 * Tutte le cause dell' errore:
                 *
                 * - dati di login errati
                 * - dati mancanti
                 * - riscontrati problemi con il database
                 */

                //  Leggo l'errore di risposta inviatomi dal server
                JSONObject error = null;
                String errore = "";

                try {
                    JSONObject res = new JSONObject(response);
                    error = new JSONObject(res.getString("error"));

                    errore = error.getString("info"); // Salvo l'informazione dell' error nella stringa errore
                } catch (Exception e) {
                    // Segnalare l'errore
                }

                // Segnalare l'errore contenuto in "errore" all' utente

            } else {
                try {
                    JSONObject token = new JSONObject(response);
                    JSONObject autistajs = new JSONObject(token.getString("autista"));

                    //  ERRATO, FA CRASHARE L'APP, non si possono usare toast o altri oggetti in un task asincrono come questo

                    /*while (token.getString("token") == null) {
                        Toast.makeText(this, "Login errato!" + response, Toast.LENGTH_SHORT).show();
                    }*/

                    Parametri.Token = token.getString("token");
                    Parametri.id = autistajs.getString("id");
                    Parametri.username = autistajs.getString("username");
                    Parametri.nome = autistajs.getString("nome");
                    Parametri.cognome = autistajs.getString("cognome");
                    Parametri.data_nascita = autistajs.getString("data_nascita");
                    Parametri.email = autistajs.getString("email");
                    Parametri.password = autistajs.getString("password");
                    Parametri.saldo = autistajs.getString("saldo");
                    Parametri.telefono = autistajs.getString("telefono");

                    //  Chiudo l'activitt corrente e passo alla MainActivity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } catch (Exception e) {
                    // Segnalare l'errore
                }
            }
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
    public void goToMap(View view) {
        startActivity(new Intent(LoginActivity.this, MapActivity.class));
    }
}