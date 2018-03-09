package com.example.stach.app_test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Detail_Book extends Fragment {


    public Detail_Book() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail__book, container, false);
        //prendo gli argomenti passati
        Bundle bundle = getArguments();
        //prendo i miei valori
        String parcheggio = bundle.getString("NomeParcheggio");
        String ora = bundle.getString("oraPrenotazioneParcheggio");
        //ASSEGNO I VALORI ALLE TEXT VIEW
        TextView nomeParcheggio = (TextView) view.findViewById(R.id.nomeParcheggio);
        nomeParcheggio.setText(parcheggio);
        TextView oraPrenotazioneParcheggio = (TextView) view.findViewById(R.id.oraPrenotazioneParcheggio);
        oraPrenotazioneParcheggio.setText(ora);
        return view;
    }

    /**
     * Funzione per ritornare alle prenotazioni
     * @param view
     */
    public void eraseBooking(View view) {
        FragmentYour_Book fragment = new FragmentYour_Book();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fram, fragment, "Fragment Book");
        fragmentTransaction.commit();
    }

    /**
     * Funzione per aprire google maps e navigare fino al parcheggio prenotato
     * @param view
     */
    public void navBooking(View view){

    }
}
