package com.example.stach.app_test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;


public class Carta_di_credito extends Fragment {
    private EditText t_numero;
    private EditText t_data;
    private EditText t_pin;

    private String numero;
    private String data;
    private String pin;

    public Carta_di_credito() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carta_di_credito, container, false);

        t_numero = (EditText) view.findViewById(R.id.editTextNumeroCarta);
        t_data = (EditText) view.findViewById(R.id.editTextDataScadenza);
        t_pin = (EditText) view.findViewById(R.id.editTextPin);

        // Se sono presenti setto i dati precedentemente collegati all' account dall' utente
        if (Parametri.numero_carta != null )
            t_numero.setText(Parametri.numero_carta);
        if (Parametri.data_di_scadenza != null)
            t_data.setText(Parametri.data_di_scadenza);
        if (Parametri.pin != null)
            t_pin.setText(Parametri.pin);

        Button sendDatiCarta = (Button) view.findViewById((R.id.buttonAggiornaCarta));
        final Fragment frag = this;
        sendDatiCarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Prelevo i dati per il login per inviarli al server.
                numero = t_numero.getText().toString();
                data = t_data.getText().toString();
                pin = t_pin.getText().toString();

                if (numero.length() < 1 || data.length() < 1 || pin.length() < 1)
                {
                    Toast.makeText(getContext(), "ERRORE:\nDevi compilare tutti i campi.", Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject postData = new JSONObject();
                JSONObject autista = new JSONObject();
                JSONObject carta = new JSONObject();

                try {
                    carta.put("numero_carta", numero);
                    carta.put("dataDiScadenza", data);
                    carta.put("pin", pin);
                    autista.put("carta_di_credito", carta);
                    postData.put("autista", autista);
                    postData.put("token", Parametri.Token);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "ERRORE:\nimpossibile leggere i campi appena compilati.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Creo ed eseguo una connessione con il server web
                Connessione conn = new Connessione(postData, "PATCH", getContext(), getActivity(), frag);
                conn.execute(Parametri.IP + "/cambiaCredenziali");
            }
        });

        return view;
    }

    public void AggiornaParametri(){
        Parametri.numero_carta = numero;
        Parametri.data_di_scadenza = data;
        Parametri.pin = pin;
    }
}