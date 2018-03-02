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
import android.widget.TextView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentYour_Book extends Fragment {
    Prenotazione[] prenotazioni = new Prenotazione[3];
    TextView padding;
    //utilizzato poi
    String parcheggioAttuale;
    String data;

    public FragmentYour_Book() {
        //le creo casuali
        for (int i = 0; i < prenotazioni.length; i++) {
            prenotazioni[i] = new Prenotazione();
        }
    }

    //FUNZIONA TUTTO I AM A GENIUS
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your__book, container, false);
        View linearLayout = view.findViewById(R.id.linearInternalBook);
        //LinearLayout layout = (LinearLayout) findViewById(R.id.info);
        //arrai di textVIEW
        Button buttonsPrenotazioni[] = new Button[prenotazioni.length];
        //array di linear Layout
        //View linearLayoutArray []= new View[prenotazioni.length];
        for (int i = 0; i < prenotazioni.length; i++) {
            //LAYOUT PERSONALE PRENOTAZIONE
            //Creo un linear layout per ogni prenotazione
            //linearLayoutArray[i] = new View(view.getContext());
            //TEXT VIEW
            //creo la text view
            buttonsPrenotazioni[i] = new Button(view.getContext());
            //prendo le variabili per passarle al fragment dei dettagli
            parcheggioAttuale = prenotazioni[i].getNomeParcheggio();
            data = prenotazioni[i].getData();
            //ritorno la stringa da stampare
            buttonsPrenotazioni[i].setText(prenotazioni[i].returnValue());
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
                    //TO FIX: VEDE SEMPRE L'ULTIMO PARCHEGGIO E MAI QUELLO GIUSTO
                    String parcheggioAttualeInside = parcheggioAttuale;
                    String oraPrenotazioneParcheggioInside = data;
                    //passo le informazioni relative alla mia prenotazione
                    //genero il bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("NomeParcheggio", parcheggioAttuale.getBytes().toString());
                    bundle.putString("oraPrenotazioneParcheggio", data.getBytes().toString());
                    //eseguo la transazione
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    //passo i valori
                    Detail_Book detail_book = new Detail_Book();
                    detail_book.setArguments(bundle);
                    //eseguo la transazione
                    fragmentTransaction.replace(R.id.fram, detail_book);
                    fragmentTransaction.commit();
                }
            });
            //aggiunto text a layout
            buttonsPrenotazioni[i].setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            //aggiungo la lineaar layout personalizzata al linear layout master
            // ((LinearLayout)  linearLayout).addView((LinearLayout) linearLayoutArray[i]);
            //aggiungo la text view al layout personale della prenotazione
            ((LinearLayout) linearLayout).addView(buttonsPrenotazioni[i]);
            //TO FIX, AGGIUNGI BUTTON


            //TEXT VIEW PADDING LINEAR GENERALE
            //aggiungo text view per padding al layout generale
            padding = new TextView(view.getContext());
            padding.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            ((LinearLayout) linearLayout).addView(padding);

        }
        return view;

    }

    /**
     * This method allows to get user books
     *
     * @return list of current and recent books
     */
    public List<String> showBooks() {
        return new ArrayList<String>();
    }

    /**
     * This method allow user to remove a past or current book
     *
     * @return true if book was removed
     */
    public boolean removeBooks() {
        return true;
    }

}
