package com.example.stach.app_test;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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

    private TextView padding;
    int i;
    TextView info_parcheggio[] = new TextView[Parametri.parcheggi_vicini.size()];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        switch (Parametri.parcheggi_vicini.size())
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
                alertDialog.setMessage("Sono stati trovati" + Parametri.parcheggi_vicini.size() + " parcheggi nelle vicinanze!");
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


        for (i = 0; i < Parametri.parcheggi_vicini.size(); i++) {
            Parcheggio parcheggio = null;
            parcheggio = Parametri.parcheggi_vicini.get(i);
            info_parcheggio[i] = new TextView(view.getContext());
            info_parcheggio[i].setText(parcheggio.getIndirizzo_format());

            info_parcheggio[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            //Setto l'id della text view
            info_parcheggio[i].setId(i);

            //setto la dimensione
            info_parcheggio[i].setTextSize(19);

            //colore
            info_parcheggio[i].setTextColor(Color.BLACK);

            info_parcheggio[i].setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            //aggiunto text a layout
            //aggiungo la text view al layout personale della prenotazione

            ((LinearLayout) linearLayout).addView(info_parcheggio[i]);
            //aggiungo text view per padding al layout generale
            padding = new TextView(view.getContext());
            padding.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            ((LinearLayout) linearLayout).addView(padding);

            //Prenoto
            info_parcheggio[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {

                    SetColor(arg0);
                   /* //passo le informazioni relative alla mia prenotazione
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
                    //Prenotazione(i-1);*/
                }
            });
        }
        Button btn = new Button(view.getContext());
        btn.setText("Conferma Prenotazione");
        // Inflate the layout for this fragment
        return view;
    }
    private void SetColor(View tw)
    {
        for(int j = 0; j < Parametri.parcheggi_vicini.size(); j++)
        {
            TextView pr = (TextView)getActivity().findViewById(j);
            pr.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }
        tw.setBackgroundColor(Color.parseColor("#49FF3C"));
    }


}