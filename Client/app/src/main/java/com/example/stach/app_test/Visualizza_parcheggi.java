package com.example.stach.app_test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Visualizza_parcheggi extends Fragment {
    TextView info_parcheggio[] = new TextView[Parametri.parcheggi_vicini.size()];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        switch (Parametri.parcheggi_vicini.size()) {
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
                alertDialog.setMessage("Sono stati trovati " + Parametri.parcheggi_vicini.size() + " parcheggi nelle vicinanze!");
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

        // aggiungo text view per padding al layout generale
        TextView padding = new TextView(view.getContext());

        padding.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        ((LinearLayout) linearLayout).addView(padding);

        for (int i = 0; i < Parametri.parcheggi_vicini.size(); i++) {
            Parcheggio parcheggio = Parametri.parcheggi_vicini.get(i);

            info_parcheggio[i] = new TextView(view.getContext());
            info_parcheggio[i].setText(parcheggio.getIndirizzo_format() + "\n" + parcheggio.getInfo());
            info_parcheggio[i].setBackgroundColor(Color.parseColor("#FF47A2F1"));
            info_parcheggio[i].setPaddingRelative(0, 8, 0, 8);
            //Setto l'id della text view
            info_parcheggio[i].setId(i);
            info_parcheggio[i].setTextSize(19);
            info_parcheggio[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            info_parcheggio[i].setTextColor(Color.BLACK);
            info_parcheggio[i].setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            //aggiunto text a layout
            //aggiungo la text view al layout personale della prenotazione
            ((LinearLayout) linearLayout).addView(info_parcheggio[i]);

            // aggiungo text view per padding al layout generale
            padding = new TextView(view.getContext());
            padding.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            ((LinearLayout) linearLayout).addView(padding);

            //Prenoto
            info_parcheggio[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    SetColor(arg0);
                }
            });
        }

        Button btn = new Button(view.getContext());
        btn.setText("SELEZIONA PARCHEGGIO");
        ((LinearLayout) linearLayout).addView(btn);
        // aggiungo text view per padding al layout generale
        padding = new TextView(view.getContext());
        padding.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ((LinearLayout) linearLayout).addView(padding);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ConfermaPrenotazione();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void SetColor(View tw) {
        if (tw.getBackground() instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) tw.getBackground();
            int colorCode = cd.getColor();
            if (colorCode == Color.parseColor("#5FFF6A")) {
                tw.setBackgroundColor(Color.parseColor("#FF47A2F1"));
                return;
            }
        }

        for (int j = 0; j < Parametri.parcheggi_vicini.size(); j++) {
            TextView pr = getActivity().findViewById(j);
            pr.setBackgroundColor(Color.parseColor("#FF47A2F1"));

        }

        tw.setBackgroundColor(Color.parseColor("#5FFF6A"));
    }

    private void ConfermaPrenotazione() {
        int id = -1;

        for (int j = 0; j < Parametri.parcheggi_vicini.size(); j++) {
            TextView pr = getActivity().findViewById(j);
            if (pr.getBackground() instanceof ColorDrawable) {
                ColorDrawable cd = (ColorDrawable) pr.getBackground();
                int colorCode = cd.getColor();
                if (colorCode == Color.parseColor("#5FFF6A")) {
                    id = Parametri.parcheggi_vicini.get(j).getId();
                }
            }
        }

        if (id == -1) {
            Toast.makeText(this.getContext(), "Selezionare un parcheggio!", Toast.LENGTH_LONG).show();
            return;
        }

        Parametri.parcheggi = Parametri.parcheggi_vicini;


        PrenotaParcheggio fragment = PrenotaParcheggio.newInstance(id);
        android.support.v4.app.FragmentManager fmanager =  getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fmanager.beginTransaction();
        fragmentTransaction.replace(R.id.fram, fragment, "PrenotaParcheggio");
        fmanager.popBackStack();
        fragmentTransaction.addToBackStack("PrenotaParcheggio");
        fragmentTransaction.commit();
    }
}