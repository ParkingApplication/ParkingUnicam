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
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentYour_Book extends Fragment {
    Prenotazione[] prenotazioni = new Prenotazione[3];
    TextView padding;


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
        //array di buttons
        Button buttonsPrenotazioni[] = new Button[prenotazioni.length];
        //array di linear Layout
        //View linearLayoutArray []= new View[prenotazioni.length];
        for (int i = 0; i < prenotazioni.length; i++) {
            //index is the solution
            final int index=i;
            //LAYOUT PERSONALE PRENOTAZIONE
            //TEXT VIEW
            //creo la text view
            buttonsPrenotazioni[index] = new Button(view.getContext());
            //ritorno la stringa da stampare
            buttonsPrenotazioni[index].setText(prenotazioni[index].getNomeParcheggio().substring(0,10)+"...  >");
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
                    bundle.putString("NomeParcheggio", prenotazioni[index].getNomeParcheggio());
                    bundle.putString("oraPrenotazioneParcheggio",  prenotazioni[index].getData());
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
            //aggiunto text a layout
            buttonsPrenotazioni[index].setLayoutParams(new LinearLayout.LayoutParams(
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
