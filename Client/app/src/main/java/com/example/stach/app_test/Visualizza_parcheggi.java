package com.example.stach.app_test;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Visualizza_parcheggi extends Fragment {
/*
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

        final Button[] buttons_indirizzo = new Button[Parametri.parcheggi.length()];
        for (i = 0; i < Parametri.parcheggi.length(); i++) {
            JSONObject parcheggio = null;
            JSONObject indirizzo = null;

            try {
                parcheggio = Parametri.parcheggi.getJSONObject(i);
                indirizzo = parcheggio.getJSONObject("indirizzo");
            } catch (Exception e) {
            }

            buttons_indirizzo[i] = new Button(view.getContext());
            //lista informazioni parcheggio
            final ArrayList<String> listaInformazioniParcheggio = new ArrayList<String>();
            //ritorno la stringa da stampare
            try {
                //riempio la lista
                listaInformazioniParcheggio.add(parcheggio.getString("id"));
                listaInformazioniParcheggio.add(parcheggio.getString("indirizzo"));
                //stampo a video le informazioni fondamentali
                buttons_indirizzo[i].setText(listaInformazioniParcheggio.get(0)+ ": "+listaInformazioniParcheggio.get(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Setto i parametri della text view
            buttons_indirizzo[i].setId(i+1);
            //scrivo le risorse background

            buttons_indirizzo[i].setBackgroundResource(R.drawable.roundedbutton);
            //setto la dimensione

            buttons_indirizzo[i].setTextSize(19);

            //colore

            buttons_indirizzo[i].setTextColor(Color.BLACK);

            buttons_indirizzo[i].setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            //aggiunto text a layout
            //aggiungo la text view al layout personale della prenotazione

            ((LinearLayout) linearLayout).addView(buttons_indirizzo[i]);
            //aggiungo text view per padding al layout generale
            padding = new TextView(view.getContext());
            padding.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            ((LinearLayout) linearLayout).addView(padding);

            //Prenoto
            buttons_indirizzo[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    //passo le informazioni relative alla mia prenotazione
                    //genero il bundle
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("InformazioniParcheggio", listaInformazioniParcheggio);
                    //eseguo la transazione
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    //passo i valori
                    PrenotaParcheggio prenotaParcheggio = new PrenotaParcheggio();
                    prenotaParcheggio.setArguments(bundle);
                    //eseguo la transazione
                    fragmentTransaction.replace(R.id.fram, prenotaParcheggio);
                    //uso backstack perchè android in automatico con il tasto indietro si muove tra activity e non tra fragment
                    //quindi aggiungo nella coda del back stack il frammento delle prenotazioni in modo che all'interno dei dettagli
                    //io possa tornare indietro
                    fragmentTransaction.addToBackStack("Fragment_Visualizza_parcheggi");
                    fragmentTransaction.commit();
                    //Prenotazione(i-1);
                }
            });
        }

        // Inflate the layout for this fragment
        return view;
    }
*/

}