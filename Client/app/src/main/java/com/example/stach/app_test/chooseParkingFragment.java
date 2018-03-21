package com.example.stach.app_test;


import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class chooseParkingFragment extends Fragment {
    private View view;
    static ProgressDialog caricamento = null;

    public chooseParkingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.choose_parkingfragment, container, false);
        //prendo gli argomenti passati
        Bundle bundle = getArguments();
        //prendo i miei valori
        String latitudine = bundle.getString("latitudine");
        String longitudine = bundle.getString("longitudine");
        Button visualizzaParcheggi = view.findViewById(R.id.vaiAVisualizzazioneParcheggi);
        visualizzaParcheggi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInformationToView(v);
            }
        });
        //ASSEGNO I VALORI ALLE TEXT VIEW
        TextView text = (TextView) view.findViewById(R.id.resultFromGPS);
        text.setText(getCityFromLatLong(latitudine, longitudine));
        //sendDataForViewPark(getCityFromLatLong(latitudine, longitudine));
        return view;
    }

    private String getCityFromLatLong(String lat, String lng) {
        Geocoder gcd = new Geocoder(this.getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            return addresses.get(0).getLocality();
        } else {
            System.out.print("Non ci sono parametri validi per trovare la città");
            return "No city";
        }
    }

    /**
     * Method to call server
     * for get parking in certain citY
     */
    public void sendDataForViewPark(String citta) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("città", citta);
        } catch (Exception e) {
        }
        // Avverto l'utente del tentativo di invio dei dati di login al server
        caricamento = ProgressDialog.show(this.getActivity(), "",
                "Connessione con il server in corso...", true);
        // Creo ed eseguo una connessione con il server web
        Connessione conn = new Connessione(postData, "POST", this.getContext(), this.getActivity(), null);
        conn.execute(Parametri.IP + "/getParcheggiPerCitta");
    }

    /**
     * This methos it's used to send information about parking to another activity
     */
    public void sendInformationToView(View view) {
        ArrayList<Parcheggi> parcheggi = new ArrayList<Parcheggi>();
        //Set di bundle per visualizzazione parcheggi
        parcheggi.add(new Parcheggi());
        parcheggi.add(new Parcheggi());
        parcheggi.add(new Parcheggi());
        //define bundles
        Bundle args = new Bundle();
        args.putSerializable("listParking", parcheggi);
        //eseguo la transazione
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //passo i valori
        Visualizza_parcheggi visualizza_parcheggi = new Visualizza_parcheggi();
        visualizza_parcheggi.setArguments(args);
        //eseguo la transazione
        fragmentTransaction.replace(R.id.fram, visualizza_parcheggi);
        //uso backstack perchè android in automatico con il tasto indietro si muove tra activity e non tra fragment
        //quindi aggiungo nella coda del back stack il frammento delle prenotazioni in modo che all'interno dei dettagli
        //io possa tornare indietro
        fragmentTransaction.commit();
    }

}

