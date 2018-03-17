package com.example.stach.app_test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import org.json.JSONObject;

public class Cambia_credenziali extends Fragment {
    private EditText t_nome;
    private EditText t_cognome;
    private EditText t_data;
    private EditText t_telefono;
    private EditText t_email;
    private EditText t_vecchia_password;
    private EditText t_nuova_password;
    private EditText t_username;

    // è inutile prendere qui context e activity perché avviene prima della creazione della classe stessa e quindi sono sempre NULL

    public Cambia_credenziali() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cambia_credenziali, container, false);

        t_nome = (EditText) view.findViewById(R.id.nuova_Nome);
        t_cognome = (EditText) view.findViewById(R.id.nuova_Cognome);
        t_data = (EditText) view.findViewById(R.id.nuova_Data_Nascita);
        t_telefono = (EditText) view.findViewById(R.id.nuova_Telefono);
        t_email = (EditText) view.findViewById(R.id.nuova_Email);
        t_vecchia_password = (EditText) view.findViewById(R.id.vecchia_password);
        t_nuova_password = (EditText) view.findViewById(R.id.nuova_password);
        t_username = (EditText) view.findViewById(R.id.nuova_Username);

        t_nome.setText(Parametri.nome);
        t_cognome.setText(Parametri.cognome);
        t_data.setText(Parametri.data_nascita);
        t_telefono.setText(Parametri.telefono);
        t_email.setText(Parametri.email);
        t_username.setText(Parametri.username);

        Button sendCredenziali = (Button) view.findViewById((R.id.nuova_buttonCommit));
        sendCredenziali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = Parametri.password;

                // Prelevo i dati per il login per inviarli al server.
                String nome = t_nome.getText().toString();
                String cognome = t_cognome.getText().toString();
                String data = t_data.getText().toString();
                String telefono = t_telefono.getText().toString();
                String email = t_email.getText().toString();
                String cur_password = t_vecchia_password.getText().toString();
                String new_password = t_nuova_password.getText().toString();
                String username = t_username.getText().toString();

                if (new_password.length() > 0 && cur_password.compareTo(password) == 0)
                    password = new_password;

                JSONObject postData = new JSONObject();
                JSONObject autista = new JSONObject();

                try {
                    autista.put("username", username);
                    autista.put("password", password);
                    autista.put("email", email);
                    autista.put("nome", nome);
                    autista.put("cognome", cognome);
                    autista.put("dataDiNascita", data);
                    autista.put("telefono", telefono);
                    postData.put("autista", autista);
                    postData.put("token", Parametri.Token);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "ERRORE:\nimpossibile leggere i campi appena compilati.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Creo ed eseguo una connessione con il server web
                Connessione conn = new Connessione(postData, "PATCH", getContext(), getActivity());
                conn.execute(Parametri.IP + "/cambiaCredenziali");
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
