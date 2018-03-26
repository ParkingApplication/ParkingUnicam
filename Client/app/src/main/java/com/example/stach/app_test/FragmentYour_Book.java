package com.example.stach.app_test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class FragmentYour_Book extends Fragment {
    // Parcheggi associati alle mie prenotazioni
    private List<Parcheggio> parcheggi = new ArrayList<>();
    private Button buttonsPrenotazioni[] = null;

    public FragmentYour_Book() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your__book, container, false);
        View linearLayout = view.findViewById(R.id.linearInternalBook);

        if (Parametri.prenotazioniInCorso != null) {
            for (int i = 0; i < Parametri.prenotazioniInCorso.size(); i++) {
                for (int j = 0; j < Parametri.parcheggi.size(); j++) {
                    if (Parametri.prenotazioniInCorso.get(i).getIdParcheggio() == Parametri.parcheggi.get(j).getId()) {
                        parcheggi.add(Parametri.parcheggi.get(j));
                        break;
                    }
                }
            }

            buttonsPrenotazioni = new Button[Parametri.prenotazioniInCorso.size()];

            //array di linear Layout
            for (int i = 0; i < Parametri.prenotazioniInCorso.size(); i++) {
                //LAYOUT PERSONALE PRENOTAZIONE
                //TEXT VIEW
                //creo la text view
                buttonsPrenotazioni[i] = new Button(view.getContext());
                //ritorno la stringa da stampare
                buttonsPrenotazioni[i].setText(parcheggi.get(i).getIndirizzo() + "\n" + Parametri.prenotazioniInCorso.get(i).getScadenza().toString());
                //Setto i parametri della text view
                buttonsPrenotazioni[i].setId(i);
                //scrivo le risorse background
                buttonsPrenotazioni[i].setBackgroundResource(R.drawable.roundedbutton);
                //setto la dimensione
                buttonsPrenotazioni[i].setTextSize(19);
                //colore
                buttonsPrenotazioni[i].setTextColor(Color.parseColor("#FFFFFF"));
                //Setto la funzione da chiamare per mostrare i dettagli della prenotazione
                buttonsPrenotazioni[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int i;
                        for (i = 0; i < buttonsPrenotazioni.length; i++)
                            if (buttonsPrenotazioni.equals(view))
                                break;

                        if (i < buttonsPrenotazioni.length) {
                            //passo le informazioni relative alla mia prenotazione
                            //genero il bundle
                            Bundle bundle = new Bundle();
                            bundle.putString("NomeParcheggio", parcheggi.get(i).getIndirizzo());
                            bundle.putString("oraPrenotazioneParcheggio", Parametri.prenotazioniInCorso.get(i).toString());
                            //eseguo la transazione
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            //passo i valori
                            Detail_Book detail_book = new Detail_Book();
                            detail_book.setArguments(bundle);
                            //eseguo la transazione
                            fragmentTransaction.replace(R.id.fram, detail_book);
                            //uso backstack perchè android in automatico con il tasto indietro si muove tra activity e non tra fragment
                            //quindi aggiungo nella coda del back stack il frammento delle prenotazioni in modo che all'interno dei dettagli
                            //io possa tornare indietro
                            fragmentTransaction.addToBackStack("Fragment_book");
                            fragmentTransaction.commit();
                        }
                    }
                });

                ((LinearLayout) linearLayout).addView(buttonsPrenotazioni[i]);
            }
        }
        return view;
    }
}
