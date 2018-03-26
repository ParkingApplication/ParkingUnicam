package com.example.stach.app_test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.*;

public class LoginActivity extends AppCompatActivity implements  ConnessioneListener {
    private ProgressDialog caricamento = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void backdoor(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
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

    public void onClickLogin(View view) {
        EditText mail = findViewById(R.id.mail);
        EditText pass = findViewById(R.id.pass);
        String username, password;

        // Prelevo i dati per il login per inviarli al server.
        username = mail.getText().toString();
        password = pass.getText().toString();

        try {
            password = SHA1(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        sendDataForLogin(username, password);
    }

    private void sendDataForLogin(String username, String password) {
        // Avverto l'utente del tentativo di invio dei dati di login al server
        caricamento = ProgressDialog.show(LoginActivity.this, "Login in corso",
                "Connessione con il server in corso...", true);
        caricamento.show();

        JSONObject postData = new JSONObject();
        try {
            postData.put("username", username);
            postData.put("password", password);
        } catch (Exception e) {
            caricamento.dismiss();
            return;
        }

        Connessione conn = new Connessione(postData, "POST");
        conn.addListener(this);
        conn.execute(Parametri.IP + "/login");
    }

    // Intercetta la risposta di sendDataForLogin
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

        if (responseCode.equals("200")) {
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
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return;
            }

            // Salvo i dati di login corretti
            saveData(Parametri.username, Parametri.password);

            caricamento.dismiss();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return;
        }
    }

    private void saveData(String username, String password) {
        try {
            BufferedWriter fos = new BufferedWriter(new FileWriter(Parametri.login_file.getAbsolutePath()));
            fos.write(username + "\n");
            fos.write(password + "\n");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Criptazione SHA1
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
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
}