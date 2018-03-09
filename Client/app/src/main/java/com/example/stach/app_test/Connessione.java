package com.example.stach.app_test;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.*;
import java.util.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;
import android.widget.Toast;

public class Connessione extends AsyncTask<String, Void, Void> {

    // This is the JSON body of the post
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

            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.setRequestMethod(requestType);


            // Send the post body
            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());


                writer.write(postData.toString());
                writer.flush();
            }

            int statusCode = urlConnection.getResponseCode();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());


            //Leggo la risposta dal server
            BufferedReader reader = new BufferedReader(new InputStreamReader (urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String response = null;
            while ((response = reader.readLine()) != null)
            {
                sb.append(response + "\n");
            }
            response = sb.toString();


            callback.execute(response, statusCode);

        } catch (Exception e) {
            // Mostrare l'eccezione da qualche parte
        }
        return null;
    }
}