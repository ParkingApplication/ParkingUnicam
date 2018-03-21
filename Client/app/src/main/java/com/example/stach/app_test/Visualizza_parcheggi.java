package com.example.stach.app_test;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;


public class Visualizza_parcheggi extends Fragment {

    private TextView padding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visualizza_parcheggi, container, false);
        View linearLayout = view.findViewById(R.id.linearLayoutVisualizza);
        final ArrayList<Parcheggi> listParcheggi = (ArrayList<Parcheggi>) getArguments().getSerializable("listParking");
        TextView[] textParcheggi = new TextView[listParcheggi.size()];
        for (Parcheggi p: listParcheggi){
            //index is the solution
            final int index=listParcheggi.indexOf(p);
            //LAYOUT PERSONALE PRENOTAZIONE
            //TEXT VIEW
            //creo la text view
            textParcheggi[index] = new TextView(view.getContext());
            //ritorno la stringa da stampare
            textParcheggi[index].setText(listParcheggi.get(index).getNome()+" "+listParcheggi.get(index).getId());
            //Setto i parametri della text view
            textParcheggi[index].setId(index);
            //scrivo le risorse background
            textParcheggi[index].setBackgroundResource(R.drawable.roundedbutton);
            //setto la dimensione
            textParcheggi[index].setTextSize(19);
            //colore
            textParcheggi[index].setTextColor(Color.BLACK);
            textParcheggi[index].setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));


            //LE TEXT VIEW CI SONO MA NON SI VEDONO



            //aggiunto text a layout
            //aggiungo la text view al layout personale della prenotazione
            ((LinearLayout) linearLayout).addView(textParcheggi[index]);
            //aggiungo text view per padding al layout generale
            padding = new TextView(view.getContext());
            padding.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            ((LinearLayout) linearLayout).addView(padding);
        }

        // Inflate the layout for this fragment
        return view;
    }
}



