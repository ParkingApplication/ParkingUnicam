package com.example.stach.app_test;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.support.v4.app.Fragment;
import org.json.JSONObject;



public class Cambia_credenziali extends Fragment {
    Context context = getContext();
    Activity activity = getActivity();
    public Cambia_credenziali() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cambia_credenziali, container, false);
    }

    /**
     * This method allow contact server to change password
     *
     *          new user password
     * @return
     *         true if password changes
     */

    public void sendDataChangePassword(View view){
        EditText mail = (EditText) view.findViewById(R.id.nuova_Email);
        EditText password = (EditText) view.findViewById(R.id.nuova_password);

        // Prelevo i dati per il login per inviarli al server.
        String user = mail.getText().toString();
        String password1 = password.getText().toString();
        // Inserisco i dati del login in un HashMap così da poterli convertire facilmente in JSonObject in seguito
        JSONObject postData = new JSONObject();
        try {
            postData.put("username", user);
            postData.put("password", password1);
        }catch (Exception e){}


        // Creo ed eseguo una connessione con il server web
        Connessione conn = new Connessione(postData, "POST",context,activity);
        conn.execute(Parametri.IP + "/login");

    }




}
