package com.example.stach.app_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;
import org.json.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;

public class Connessione extends AsyncTask<String, String, String> {
    JSONObject postData;
    String requestType;
    Context context;
    Activity activity;
    Fragment fragment;
    String responseInfo;

    // This is a constructor that allows you to pass in the JSON body
    public Connessione(JSONObject postData, String requestType, Context context, Activity activity, Fragment fragment) {

        this.requestType = requestType;
        this.context =  context;
        this.activity = activity;
        this.fragment = fragment;
        if (postData != null) {
            this.postData = postData;
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
            urlConnection.setConnectTimeout(7000);
            // Send the post body
            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
            }

            int statusCode = urlConnection.getResponseCode();
            InputStreamReader inputStreamReader = null;

            // Se lo statuscode è 200 leggo la risposta, altrimenti leggo l'errore
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
            responseInfo = response;

            //publishProgress(response, String.valueOf(statusCode));

            return String.valueOf(statusCode);
        } catch (Exception e) {
            // Lo statuscode -145 indica un eccezione nella connessione con il server oppure nella lettura della risposta
            //publishProgress(e.getMessage(), String.valueOf(-145));
            responseInfo = e.getMessage();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result){ // Result è lo status code di ritorno
        // --------------- Connessione assente -------------------

        //LOGIN
        if (result == null && activity instanceof LoginActivity) {
            LoginActivity actv = (LoginActivity)activity;
            actv.caricamento.dismiss();
            Toast.makeText(context, "ERRORE:\nConnessione Assente o server offline.", Toast.LENGTH_LONG).show();
            return;
        }

        //SINGUP
        if (result == null && activity instanceof SignUpActivity) {
            SignUpActivity actv = (SignUpActivity)activity;
            actv.caricamento.dismiss();
            Toast.makeText(context, "ERRORE:\nConnessione Assente o server offline.", Toast.LENGTH_LONG).show();
            return;
        }

        //RECOVERY
        if (result == null && activity instanceof PasswordRecoveryActivity) {
            PasswordRecoveryActivity actv = (PasswordRecoveryActivity)activity;
            actv.caricamento.dismiss();
            Toast.makeText(context, "ERRORE:\nConnessione Assente o server offline.", Toast.LENGTH_LONG).show();
            return;
        }
        //Frammento scelta parcheggio di main activity (non cade in credenziali e carta perché ha caricamento.dismiss)
        if (result == null && activity instanceof MainActivity && fragment instanceof FindYourParkingFragment) {
            FindYourParkingFragment frag = (FindYourParkingFragment) fragment;
            Toast.makeText(context, "ERRORE:\nConnessione Assente o server offline.", Toast.LENGTH_LONG).show();
            frag.caricamento.dismiss();
            return;
        }
        //CREDENZIALI E CARTA (qui cadranno in automatico tutti i fragment di main activity, non serve riscriverlo per ogni fragment)
        if (result == null && activity instanceof MainActivity) {
            Toast.makeText(context, "ERRORE:\nConnessione Assente o server offline.", Toast.LENGTH_LONG).show();
            return;
        }

        // -------------- Status code di risposta (200 o 400) --------------------

        //LOGIN 400
        if (result.equals("400") && activity instanceof LoginActivity) {
            String message = estraiErrore(responseInfo);

            LoginActivity actv = (LoginActivity)activity;
            actv.caricamento.dismiss();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            return;
        }

        //LOGIN 200
        if(result.equals("200") && activity instanceof LoginActivity) {
            String message = "";

            LoginActivity actv = (LoginActivity)activity;

            // Estraggo i miei dati restituiti dal server
            try {
                JSONObject token = new JSONObject(responseInfo);
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

                actv.caricamento.dismiss();
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context, LoginActivity.class));
                return;
            }

            // Salvo i dati di login corretti
            actv.saveData(Parametri.email, Parametri.password);

            actv.caricamento.dismiss();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context, MainActivity.class));
            activity.finish();
            return;
        }

        //SIGNUP 400
        if (activity instanceof SignUpActivity && result.equals("400")) {
            String message = estraiErrore(responseInfo);

            SignUpActivity actv = (SignUpActivity)activity;
            actv.caricamento.dismiss();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            return;
        }

        //SIGNUP 200
        if (activity instanceof SignUpActivity && result.equals("200")) {
            String message = estraiSuccessful(responseInfo);

            SignUpActivity actv = (SignUpActivity)activity;
            actv.caricamento.dismiss();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context, LoginActivity.class));
            activity.finish();
            return;
        }

        //RECOVERY 400
        if (activity instanceof PasswordRecoveryActivity && result.equals("400")) {
            String message = estraiErrore(responseInfo);

            PasswordRecoveryActivity actv = (PasswordRecoveryActivity)activity;
            actv.caricamento.dismiss();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            return;
        }

        //RECOVERY 200
        if (activity instanceof PasswordRecoveryActivity && result.equals("200")) {
            String message = estraiSuccessful(responseInfo);

            PasswordRecoveryActivity actv = (PasswordRecoveryActivity)activity;
            actv.caricamento.dismiss();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context, LoginActivity.class));
            activity.finish();
            return;
        }

        /** DA QUI IN POI CI SONO TUTTI FRAGMENT DI MainActivity, quindi per distinguerli bisogna controllare fragment
        */
        if ( result.equals("400") && fragment instanceof FindYourParkingFragment) {
            FindYourParkingFragment frag = (FindYourParkingFragment) fragment;
            frag.caricamento.dismiss();
            String message = estraiErrore(responseInfo);
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            return;
        }
        //CREDENZIALI E CARTA 400 #(stesso discorso di sopra, la risposta di errore è formatata ugualmente per ogni fragment del MainActivity)
        if (activity instanceof MainActivity && result.equals("400")) {
            String message = estraiErrore(responseInfo);
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            return;
        }

        //CREDENZIALI 200
        if (activity instanceof MainActivity && result.equals("200") && fragment instanceof Cambia_credenziali) {
            String message = estraiSuccessful(responseInfo);
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();

            MainActivity actv = (MainActivity) activity;

            // Converto il fragment nel suo tipo corretto ed aggiorno le credenziali dell' utente in Parametri
            Cambia_credenziali frag = (Cambia_credenziali) fragment;
            frag.AggiornaParametri();

            // Chiudo il fragment delle credenziali
            actv.onBackPressed();
            return;
        }

        //CARTA 200
        if (activity instanceof MainActivity && result.equals("200") && fragment instanceof Carta_di_credito) {
            String message = estraiSuccessful(responseInfo);
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();

            MainActivity actv = (MainActivity)activity;

            // Converto il fragment nel suo tipo corretto ed aggiorno le credenziali della carta in Parametri
            Carta_di_credito frag = (Carta_di_credito) fragment;
            frag.AggiornaParametri();

            // Chiudo il fragment delle credenziali
            actv.onBackPressed();
            return;
        }
        //CARTA 200
        if (activity instanceof MainActivity && result.equals("200") && fragment instanceof FindYourParkingFragment) {
            FindYourParkingFragment frag = (FindYourParkingFragment) fragment;
            frag.caricamento.dismiss();
            try {
                JSONObject ris = new JSONObject(responseInfo);
                Parametri.parcheggi = ris.getJSONArray("parcheggi");
                //eseguo la transazione
                FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //passo i valori
                Visualizza_parcheggi visualizza_parcheggi = new Visualizza_parcheggi();
                //eseguo la transazione
                fragmentTransaction.replace(R.id.fram, visualizza_parcheggi);
                //uso backstack perchè android in automatico con il tasto indietro si muove tra activity e non tra fragment
                //quindi aggiungo nella coda del back stack il frammento delle prenotazioni in modo che all'interno dei dettagli
                //io possa tornare indietro
                fragmentTransaction.commit();
            }catch (Exception e){

            }



            return;
        }
    }

    /**
     *  Utilizzare solo in caso di dover informare l'utente durante la trasmissione o elaborazione dei dati,
     *  questo significa che l'utente va avvisato di un eventuale risposta solo alla fine della trasmissione
     *  la suddetta risposta deve essere inviata tramite onPostExecute e non qui.
     */
    @Override
    protected void onProgressUpdate(String... progress) {

    }

    // Funzione per l'estrazione automatica del dato JSon error
    private String estraiErrore(String data){
        String result = "";

        try {
            JSONObject response = new JSONObject(data);
            JSONObject error = new JSONObject(response.getString("error"));
            result = error.getString("info");
        } catch (Exception e) {
            result = "Impossibile leggere la risposta del server.";
        }

        return result;
    }

    // Funzione per l'estrazione automatica del dato JSon successful
    private String estraiSuccessful(String data){
        String result = "";

        try {
            JSONObject response = new JSONObject(data);
            JSONObject error = new JSONObject(response.getString("successful"));
            result = error.getString("info");
        } catch (Exception e) {
            result = "Impossibile leggere la risposta del server.";
        }

        return result;
    }
}