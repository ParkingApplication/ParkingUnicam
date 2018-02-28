package com.example.stach.app_test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentYour_Book extends Fragment {
    Prenotazione[] prenotazioni = new Prenotazione[3];

    public FragmentYour_Book() {
        //le creo casuali
       for (int i =0; i<prenotazioni.length; i++){
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

        TextView textPrenotazioni[] = new TextView[prenotazioni.length];
        for (int i=0; i<prenotazioni.length; i++){
            //creo la text view
            textPrenotazioni[i] = new TextView(view.getContext());
            //ritorno la stringa da stampare
            textPrenotazioni[i].setText(prenotazioni[i].returnValue());
            //Setto i parametri della text view
            textPrenotazioni[i].setId(i);
            textPrenotazioni[i].setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            //aggiungo la text view al layout
            ((LinearLayout) linearLayout).addView( textPrenotazioni[i]);
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
