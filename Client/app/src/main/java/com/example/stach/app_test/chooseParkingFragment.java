package com.example.stach.app_test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class chooseParkingFragment extends Fragment {
    private View view;

    public chooseParkingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.choose_parkingfragment, container, false);
        //prendo gli argomenti passati
        Bundle bundle = getArguments();
        //prendo i miei valori
        String latitudine = bundle.getString("latitudine");
        String longitudine = bundle.getString("longitudine");
        //ASSEGNO I VALORI ALLE TEXT VIEW
        TextView nomeParcheggio = (TextView) view.findViewById(R.id.resultFromGPS);
        nomeParcheggio.setText(latitudine+" "+ longitudine);
        return view;
    }

}
