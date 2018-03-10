package com.example.stach.app_test;

import android.os.AsyncTask;
import org.json.*;
import java.util.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;


public class Connessione extends AsyncTask<String, Void, Void> {
    JSONObject postData;
    CustomCallback callback;
    String requestType;

    // This is a constructor that allows you to pass in the JSON body
    public Connessione(Map<String, String> postData, String requestType, CustomCallback callback) {
        this.callback = callback;
        this.requestType = requestType;

        if (postData != null) {
            this.postData = new JSONObject(postData);
        }
    }

    // This is a function that we are overriding from AsyncTask. It takes Strings as parameters because that is what we defined for the parameters of our async task
    @Override
    protected Void doInBackground(String... params) {
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
            else
                inputStreamReader = new InputStreamReader (urlConnection.getErrorStream());

            // Leggo la risposta dal server
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String response = null;

            while ((response = reader.readLine()) != null)
                sb.append(response + "\n");
            response = sb.toString();


            callback.execute(response, statusCode);

        } catch (Exception e) {
            // Lo statuscode -145 indica un eccezione nella connessione con il server oppure nella lettura della risposta
            callback.execute(e.getMessage(), -145);
        }
        return null;
    }
}