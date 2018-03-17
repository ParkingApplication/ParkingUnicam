package com.example.stach.app_test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;

public class PasswordRecoveryActivity extends AppCompatActivity {

    Context context = PasswordRecoveryActivity.this;
    Activity activity = PasswordRecoveryActivity.this;
    static ProgressDialog caricamento = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
    }

    /**
     * This method will send user credentials for registration.
     */
    public void sendDataForPasswordRecovery(View view){
        //Prendo i dati dalla form:
        EditText email = (EditText) findViewById(R.id.editTextEmail);

        String emails = email.getText().toString();

        if (emails.length() < 1)
        {
            Toast.makeText(this, "ERRORE:\nInserire un email prima di procedere.", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject postData = new JSONObject();

        try {
            postData.put("email", emails);
        }catch (Exception e){
            // Gestire l'errore
        }


        // Avverto l'utente del tentativo di invio dei dati per il reset della password al server
        caricamento = ProgressDialog.show(PasswordRecoveryActivity.this, "",
                "Connessione con il server in corso...", true);

        Connessione conn = new Connessione(postData, "POST",context,activity);
        conn.execute(Parametri.IP + "/resetPassword");
    }

    /**
     * This method allow user to return to login activity
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(PasswordRecoveryActivity.this, LoginActivity.class));
        finish();
    }
}
