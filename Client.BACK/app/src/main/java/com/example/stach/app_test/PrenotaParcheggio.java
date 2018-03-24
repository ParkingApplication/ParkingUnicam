package com.example.stach.app_test;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class PrenotaParcheggio extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prenota_parcheggio, container, false);
        //get information
        ArrayList<String> informazioniParcheggioPresente = (ArrayList<String>) getArguments().getSerializable("InformazioniParcheggio");
        TextView nome = (TextView) view.findViewById(R.id.nome_parcheggio);
        TextView indirizzo = (TextView) view.findViewById(R.id.indirizzo_parcheggio);
        nome.setText(informazioniParcheggioPresente.get(0)); //città
        indirizzo.setText(informazioniParcheggioPresente.get(1)); //indirizzo
        return view;
    }

    public void Prenotazione(int index)
    {
        JSONObject parcheggio = null;
        JSONObject indirizzo = null;
        String ind = null;
        try {
            parcheggio = Parametri.parcheggi.getJSONObject(index);
            indirizzo = parcheggio.getJSONObject("indirizzo");
            ind = "Hai scelto di prenotare il parcheggio:\nCittà->" + indirizzo.getString("citta")+
                    "\nProvincia->" + indirizzo.getString("provincia")+
                    "\nCAP->" + indirizzo.getString("cap")+
                    "\nVia->" + indirizzo.getString("via");

        } catch (Exception e) {
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
        alertDialog.setTitle("Conferma");
        alertDialog.setMessage(ind);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Conferma Prenotazione",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       /* JSONObject parcheggio = null;
                        JSONObject indirizzo = null;

                        JSONObject postData = new JSONObject();
                        try {
                            parcheggio = Parametri.parcheggi.getJSONObject(i-1);
                            indirizzo = parcheggio.getJSONObject("indirizzo");
                            //idParcheggio, tipoParcheggio
                            postData.put("idParcheggio", indirizzo.getString("id"));
                            postData.put("tipoParcheggio", "1");
                        } catch (Exception e) {
                        }
                        // Avverto l'utente del tentativo di invio dei dati di login al server
                        ProgressDialog caricamento = ProgressDialog.show(Visualizza_parcheggi.this.getActivity(), "",
                                "Ricerca parcheggi in corso...", true);
                        // Creo ed eseguo una connessione con il server web
                        Connessione conn = new Connessione(postData, "POST", Visualizza_parcheggi.this.getContext(), Visualizza_parcheggi.this.getActivity(), Visualizza_parcheggi.this);
                        conn.execute(Parametri.IP + "/effettuaPrenotazione"); */
                        Toast.makeText(PrenotaParcheggio.this.getContext(), "ffuture da implementare", Toast.LENGTH_LONG).show();
                    }
                });
        alertDialog.show();
    }


}
