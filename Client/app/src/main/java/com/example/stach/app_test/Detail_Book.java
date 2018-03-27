package com.example.stach.app_test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
        int index = Integer.parseInt(bundle.getString("indexPrenotazione"));

        TextView nomeParcheggio = view.findViewById(R.id.nomeParcheggio);
        TextView oraPrenotazioneParcheggio = view.findViewById(R.id.oraPrenotazioneParcheggio);
        TextView informazioni = view.findViewById(R.id.textViewInfo);

        nomeParcheggio.setText(parcheggio);

        if (index >= 0 && index <= Parametri.prenotazioniInCorso.size()) {
            oraPrenotazioneParcheggio.setText(DateFormat.format("dd MMMM yyyy HH:mm:ss", Parametri.prenotazioniInCorso.get(index).getScadenza()).toString());
            informazioni.setText("Posto prenotato: " + TipoPosto.getNomeTipoPosto(Parametri.prenotazioniInCorso.get(index).getIdTipo()));
        }
        return view;
    }
}
