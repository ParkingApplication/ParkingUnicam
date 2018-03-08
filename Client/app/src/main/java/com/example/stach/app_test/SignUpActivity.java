package com.example.stach.app_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
        String email = "email";
        String password = "password";
        String username = "user";
        String nome = "nome";
        String cognome = "cognome";
        String dataDiNascita = "data";
        String telefono = "cellulare bello";

        if(Invio_dati(email,password,username,nome,cognome,dataDiNascita,telefono))
        {}//Va avanti .-.

    }
    //METODO DA TESTARE!!
    //Utilizzato per Inviare i dati al server
    public boolean Invio_dati(String mail,String password, String username, String nome, String cognome, String dataDiNascita, String telefono)
    {
        try{
            //Invio i dati al server
            URL url = new URL(Parametri.IP + "/Signup");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");


            String input = "{\"email\":" + mail + ",\"password\":" + password + ",\"username\":" + username +
                    ",\"nome\":" + nome + ",\"cognome\":" + cognome + ",\"dataDiNascita\":" + dataDiNascita +
                    ",\"telefono\":" + telefono +
                    "}";

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
            JSONArray jArray = jObject.getJSONArray("ARRAYNAME");   //BOH
            String result = null;

            for (int i=0; i < jArray.length(); i++)
            {
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array
                    result = oneObject.getString("RESULT");  //OK o NO

                } catch (JSONException e) {
                    // Oops
                }
            }
            if(result.equals("success"))
                return true;
            else
                return false;
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
