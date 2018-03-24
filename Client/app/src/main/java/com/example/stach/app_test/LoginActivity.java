package com.example.stach.app_test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
public class LoginActivity extends AppCompatActivity implements  ConnessioneListener {
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
        Connessione conn = new Connessione(postData, "POST");
        conn.addListener(this);
        conn.execute(Parametri.IP + "/login");

        // Bisogna salvare i dati dentro Connessione dopo aver effettuato il Login con successo
        //this.saveData(mail.getText().toString(), password.getText().toString());

    }


    /**
     * This method is use to save user data after login, to allow user to overdrop the login activity if is logged yet.
     */
    public void saveData(String mail, String password) {
        login_file = new File(this.getFilesDir(), "LoginStats.txt");
        //try to write in file
        try {
            BufferedWriter fos = new BufferedWriter(new FileWriter(login_file.getAbsolutePath()));
            fos.write(mail + "\n");
            fos.write(password + "\n");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backdoor(View view){
        startActivity(new Intent(context, MainActivity.class));
        finish();
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
        finish();
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

    @Override
    public void ResultResponse(String responseCode, String result) {
        if (responseCode == null) {
            caricamento.dismiss();
            Toast.makeText(getApplicationContext(), "ERRORE:\nConnessione Assente o server offline.", Toast.LENGTH_LONG).show();
            return;
        }

        if (responseCode.equals("400")) {
            String message = Connessione.estraiErrore(result);
            caricamento.dismiss();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            return;
        }

        if(responseCode.equals("200")) {
            String message = "";

            // Estraggo i miei dati restituiti dal server
            try {
                JSONObject token = new JSONObject(result);
                JSONObject autistajs = new JSONObject(token.getString("autista"));
                JSONObject carta = null;

                Parametri.Token = token.getString("token");
                Parametri.id = autistajs.getString("id");
                Parametri.username = autistajs.getString("username");
                Parametri.nome = autistajs.getString("nome");
                Parametri.cognome = autistajs.getString("cognome");
                Parametri.data_nascita = autistajs.getString("dataDiNascita");
                Parametri.email = autistajs.getString("email");
                Parametri.password = autistajs.getString("password");
                Parametri.saldo = autistajs.getString("saldo");
                Parametri.telefono = autistajs.getString("telefono");

                message = "Benvenuto " + Parametri.nome + ".";

                // Tento l'estrazione dei dati della carta di credito
                if (autistajs.has("carta_di_credito")) {
                    carta = new JSONObject(autistajs.getString("carta_di_credito"));

                    if (carta.has("numero_carta"))
                        Parametri.numero_carta = carta.getString("numero_carta");
                    if (carta.has("dataDiScadenza"))
                        Parametri.data_di_scadenza = carta.getString("dataDiScadenza");
                    if (carta.has("pin"))
                        Parametri.pin = carta.getString("pin");
                }

            } catch (Exception e) {
                message = "Errore di risposta del server.";

                caricamento.dismiss();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return;
            }

            // Salvo i dati di login corretti (da fixare)
            saveData(Parametri.email, Parametri.password);

            caricamento.dismiss();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return;
        }
    }
}