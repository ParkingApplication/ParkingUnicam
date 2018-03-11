package com.example.stach.app_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.*;
import java.util.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;
import java.lang.Object;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.example.stach.app_test.LoginActivity.caricamento;


public class Connessione extends AsyncTask<String, String, String> {
    JSONObject postData;

    String requestType;
    Context context ;

    // This is a constructor that allows you to pass in the JSON body
    public Connessione(Map<String, String> postData, String requestType, Context context) {

        this.requestType = requestType;
        this.context =  context;
        if (postData != null) {
            this.postData = new JSONObject(postData);
        }
    }

    // This is a function that we are overriding from AsyncTask. It takes Strings as parameters because that is what we defined for the parameters of our async task
    @Override
    protected String doInBackground(String... params) {
        try {
            // This is getting the url from the string we passed in
            URL url = new URL(params[0]);

            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json; charset= utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod(requestType);

            // Send the post body
            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
            }

            int statusCode = urlConnection.getResponseCode();
            InputStreamReader inputStreamReader = null;

            // Se lo statuscode è 200 leggo la risposta, altrimenti leggo l'errore (IMPORTANTE, SENZA DI QUESTO CRASHEREBBE AD OGNI RISPOSTA RICEVUTA CON STATUS 400)
            if (statusCode == 200)
                inputStreamReader = new InputStreamReader (urlConnection.getInputStream());
            else    // Leggo dall' ErrorStream
                inputStreamReader = new InputStreamReader (urlConnection.getErrorStream());

            // Leggo la risposta dal server
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String response = null;



            while ((response = reader.readLine()) != null)
                sb.append(response + "\n");

            response = sb.toString();

            publishProgress(response, String.valueOf(statusCode));
            // Richiamo la callback per gestire la risposta inviatami dal server
            //callback.execute(response, statusCode);
            return String.valueOf(statusCode);
        } catch (Exception e) {
            // Lo statuscode -145 indica un eccezione nella connessione con il server oppure nella lettura della risposta
           // callback.execute(e.getMessage(), -145);
            publishProgress(e.getMessage(), String.valueOf(-145));
        }

        return null;
    }
    @Override
    protected void onProgressUpdate(String... progress) {
        if (progress[1].equals("-145")) // Errore durante la connessione con il server, segnalarlo all' utente
        {

            /**
             * Possibili cause dell' errore:
             *
             * - l'utente non ha la connessione alla rete
             * - il server è offline
             * - errori di rete (vari)
             */
        } else {
            if (progress[1].equals("400"))  // Errore segnalato dal server
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
                    JSONObject res = new JSONObject(progress[0]);
                    error = new JSONObject(res.getString("error"));

                    errore = error.getString("info"); // Salvo l'informazione dell' error nella stringa errore
                } catch (Exception e) {
                    // Segnalare l'errore
                }

                // Segnalare l'errore contenuto in "errore" all' utente

            } else {
                try {
                    JSONObject token = new JSONObject(progress[0]);
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



                } catch (Exception e) {
                   // context.startActivity(new Intent(context, MainActivity.class));
                }
            }
        }


    }
    @Override
    protected void onPostExecute(String result){

        if (result.equals("-145"))
            Toast.makeText(context, "ERRORE:\nConnessione Assente", Toast.LENGTH_LONG).show();
        if (result.equals("400"))
            Toast.makeText(context, "ERRORE:\nDati di login errati o mancanti", Toast.LENGTH_LONG).show();
        if(!(result.equals("400") || result.equals("-145")))
            context.startActivity(new Intent(context, MainActivity.class));
        caricamento.dismiss();

    }

}