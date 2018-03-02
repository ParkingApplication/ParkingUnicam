package com.example.stach.app_test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
        nomeParcheggio.setText(parcheggio);
        return view;
    }

    /**
     * Funzione per ritornare alle prenotazioni
     * @param view
     */
    public void returnToBook(View view) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentYour_Book book_fragment = new FragmentYour_Book();
        //eseguo la transazione
        fragmentTransaction.replace(R.id.fram, book_fragment);
        fragmentTransaction.commit();
    }
}
