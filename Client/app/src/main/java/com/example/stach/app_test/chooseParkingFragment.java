package com.example.stach.app_test;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


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
        TextView località = (TextView) view.findViewById(R.id.resultFromGPS);
        località.setText(latitudine+" "+ longitudine);
        getCityFromLatLong(latitudine,longitudine);
        return view;
    }

    private void getCityFromLatLong(String lat, String lng){
        Geocoder gcd = new Geocoder(this.getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            TextView località = (TextView) view.findViewById(R.id.resultFromGPS);
            località.setText(addresses.get(0).getLocality());
        }
        else {
            // do your stuff
        }
    }
}
