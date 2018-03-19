package com.example.stach.app_test;


import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
        view =inflater.inflate(R.layout.choose_parkingfragment, container, false);
        //prendo gli argomenti passati
        Bundle bundle = getArguments();
        //prendo i miei valori
        String latitudine = bundle.getString("latitudine");
        String longitudine = bundle.getString("longitudine");
        //ASSEGNO I VALORI ALLE TEXT VIEW
        TextView località = (TextView) view.findViewById(R.id.resultFromGPS);
        località.setText(getCityFromLatLong(latitudine,longitudine));
        return view;
    }

    private String getCityFromLatLong(String lat, String lng){
        Geocoder gcd = new Geocoder(this.getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            return addresses.get(0).getLocality();
        }
        else {
           System.out.print("Non ci sono parametri validi per trovare la città");
           return "No city";
        }
    }

    /**Method to call server
     * for get parking in certain citY
     */
    public void sendDataForViewPark() {

        TextView località = (TextView) view.findViewById(R.id.resultFromGPS);
        // Prelevo i dati per il login per inviarli al server.
        String citta = località.getText().toString();
        // Inserisco i dati del login in un HashMap così da poterli convertire facilmente in JSonObject in seguito
        JSONObject postData = new JSONObject();
        try {
            postData.put("città", citta);
        }catch (Exception e){}

        // Avverto l'utente del tentativo di invio dei dati di login al server
        caricamento = ProgressDialog.show(this.getActivity(), "",
                "Connessione con il server in corso...", true);

        // Creo ed eseguo una connessione con il server web
        Connessione conn = new Connessione(postData, "POST",this.getContext(),this.getActivity(), null);
        conn.execute(Parametri.IP + "/login");

    }
}
