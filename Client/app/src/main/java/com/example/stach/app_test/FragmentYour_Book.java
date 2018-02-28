package com.example.stach.app_test;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
    TextView padding;
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
        //arrai di textVIEW
        TextView textPrenotazioni[] = new TextView[prenotazioni.length];
        //array di linear Layout
        View linearLayoutArray []= new View[prenotazioni.length];
        for (int i=0; i<prenotazioni.length; i++){
            //LAYOUT PERSONALE PRENOTAZIONE
            //Creo un linear layout per ogni prenotazione
            linearLayoutArray[i] = new View(view.getContext());
            //TEXT VIEW
            //creo la text view
            textPrenotazioni[i] = new TextView(view.getContext());
            //ritorno la stringa da stampare
            textPrenotazioni[i].setText(prenotazioni[i].returnValue());
            //Setto i parametri della text view
            textPrenotazioni[i].setId(i);
            //scrivo le risorse background
            textPrenotazioni[i].setBackgroundResource(R.drawable.roundedbutton);
            //setto la dimensione
            textPrenotazioni[i].setTextSize(19);
            //colore
            textPrenotazioni[i].setTextColor(Color.parseColor("#FFFFFF"));
            //aggiunto text a layout
            textPrenotazioni[i].setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            //aggiungo la text view al layout personale della prenotazione
            ((LinearLayout)  linearLayoutArray[i]).addView(textPrenotazioni[i]);
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
