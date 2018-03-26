package com.example.stach.app_test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentYour_Book extends Fragment {
    Prenotazione[] prenotazioni = new Prenotazione[3];
    TextView padding;
    private ProgressDialog caricamento = null;

    public FragmentYour_Book() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final List<Parcheggio> parcheggi = new ArrayList<>();
        for (int i = 0; i < Parametri.parcheggi.size();i++)
        {
            for (int j = 0; j < Parametri.prenotazioniInCorso.size();j++)
            {
                if (Parametri.parcheggi.get(i).getId() == Parametri.prenotazioniInCorso.get(j).getIdParcheggio())
                {

                    parcheggi.add(Parametri.parcheggi.get(i));

                    break;
                }
            }
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your__book, container, false);

        View linearLayout = view.findViewById(R.id.linearLayoutVisualizza);


        //array di buttons
        Button buttonsPrenotazioni[] = new Button[Parametri.prenotazioniInCorso.size()];
        //array di linear Layout
        for (int i = 0; i < Parametri.prenotazioniInCorso.size(); i++) {
            //index is the solution
            final int index=i;
            //LAYOUT PERSONALE PRENOTAZIONE
            //TEXT VIEW
            //creo la text view
            buttonsPrenotazioni[index] = new Button(view.getContext());
            //ritorno la stringa da stampare
            buttonsPrenotazioni[index].setText(parcheggi.get(i).getIndirizzo());
            //Setto i parametri della text view
            buttonsPrenotazioni[index].setId(index);
            //scrivo le risorse background
            buttonsPrenotazioni[index].setBackgroundResource(R.drawable.roundedbutton);
            //setto la dimensione
            buttonsPrenotazioni[index].setTextSize(19);
            //colore
            buttonsPrenotazioni[index].setTextColor(Color.parseColor("#FFFFFF"));
            //Setto la funzione da chiamare per mostrare i dettagli della prenotazione
            buttonsPrenotazioni[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //passo le informazioni relative alla mia prenotazione
                    //genero il bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("NomeParcheggio", parcheggi.get(index).getIndirizzo());
                    bundle.putString("oraPrenotazioneParcheggio", Parametri.prenotazioniInCorso.get(index).toString());
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
            });
            ((LinearLayout) linearLayout).addView(buttonsPrenotazioni[index]);

        }
        return view;

    }



}
