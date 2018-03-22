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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Visualizza_parcheggi extends Fragment {

    private TextView padding;
    int i;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        switch (Parametri.parcheggi.length())
        {
            case 0:
                AlertDialog alertDialog0 = new AlertDialog.Builder(this.getContext()).create();
                alertDialog0.setTitle("OPS");
                alertDialog0.setMessage("Non sono stati trovati parcheggi che supportano il nostro sistema nelle vicinanze");
                alertDialog0.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialog0.show();
                break;
            case 1:
                AlertDialog alertDialog1 = new AlertDialog.Builder(this.getContext()).create();
                alertDialog1.setTitle("Congratulazioni");
                alertDialog1.setMessage("È stato trovato un parcheggio nelle vicinanze!");
                alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialog1.show();
                break;
            default:
                AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                alertDialog.setTitle("Congratulazioni");
                alertDialog.setMessage("Sono stati trovati" + Parametri.parcheggi.length() + " parcheggi nelle vicinanze!");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialog.show();
                break;
        }


        View view = inflater.inflate(R.layout.fragment_visualizza_parcheggi, container, false);
        View linearLayout = view.findViewById(R.id.linearLayoutVisualizza);

        final TextView[] text_indirizzo = new TextView[Parametri.parcheggi.length()];
        for (i = 0; i < Parametri.parcheggi.length(); i++) {
            JSONObject parcheggio = null;
            JSONObject indirizzo = null;

            try {
                parcheggio = Parametri.parcheggi.getJSONObject(i);
                indirizzo = parcheggio.getJSONObject("indirizzo");
            } catch (Exception e) {
            }


            text_indirizzo[i] = new TextView(view.getContext());
            //ritorno la stringa da stampare
            try {

                String ind = parcheggio.getString("id") + ": " +
                        indirizzo.getString("citta") + " " +
                        indirizzo.getString("provincia") + " " +
                        indirizzo.getString("cap") + " " +
                        indirizzo.getString("via") + " ";
                text_indirizzo[i].setText(ind);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Setto i parametri della text view
            text_indirizzo[i].setId(i+1);
            //scrivo le risorse background

            text_indirizzo[i].setBackgroundResource(R.drawable.roundedbutton);
            //setto la dimensione

            text_indirizzo[i].setTextSize(19);

            //colore

            text_indirizzo[i].setTextColor(Color.BLACK);

            text_indirizzo[i].setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            //aggiunto text a layout
            //aggiungo la text view al layout personale della prenotazione

            ((LinearLayout) linearLayout).addView(text_indirizzo[i]);
            //aggiungo text view per padding al layout generale
            padding = new TextView(view.getContext());
            padding.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            ((LinearLayout) linearLayout).addView(padding);

            //Prenoto
           text_indirizzo[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {

                    Prenotazione(i-1);
                }
            });
        }

        // Inflate the layout for this fragment
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
                        Toast.makeText(Visualizza_parcheggi.this.getContext(), "ffuture da implementare", Toast.LENGTH_LONG).show();
                    }
                });
        alertDialog.show();
    }
}



