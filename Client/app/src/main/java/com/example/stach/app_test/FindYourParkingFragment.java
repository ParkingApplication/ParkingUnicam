package com.example.stach.app_test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindYourParkingFragment extends Fragment {
    private int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    //location provider
    private FusedLocationProviderClient mFusedLocationClient;
    //questi attributi servono per riconoscere quale da quale activity voglio i risultati con la callback
    //li scrivo nel pacchetto di invio
    int PLACE_PICKER_REQUEST = 1;
    //view del contesto
    private View view;
    //fused location provider


    public FindYourParkingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get layout
        view = inflater.inflate(R.layout.fragment_find_your_parking, container, false);
        //set fusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        //get buttons from xml
        ImageButton automaticSearch = (ImageButton) view.findViewById(R.id.btnAutomaticLocation);
        ImageButton inputSearch = (ImageButton) view.findViewById(R.id.btnInputLocation);
        //INPUT SEARCH
        inputSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call method
                startPlacePickerInputActivity();
            }
        });
        //AUTOMATIC SEARCH
        automaticSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaceAutomaticPickerInputActivity();
            }
        });
        //instantiate fused location client
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Questo metodo consente di far partire una activity per scegliere manualmente la posizione in cui si vuole cercare parcheggio.
     */
    private void startPlacePickerInputActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = intentBuilder.build((MainActivity) getActivity());
            //restituisco i risultati all'activity
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Questo metodo consente di far partire una activity per scegliere automaticamente la posizione in cui si vuole cercare parcheggio.
     */
    private void startPlaceAutomaticPickerInputActivity() {
        //TOFIX: CHECK PERMISSION TO USE THIS THINGS
        Toast.makeText(this.getContext(), "sono dentro", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this.getContext(), "permission not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {//permission granted
            Toast.makeText(this.getContext(), "permission granted", Toast.LENGTH_SHORT).show();
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Bundle bundle = new Bundle();
                            bundle.putString("latitudine", Double.toString(location.getLatitude()));
                            bundle.putString("longitudine", Double.toString(location.getLongitude()));
                            //eseguo la transazione
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            //passo i valori
                            chooseParkingFragment choose_fragment = new chooseParkingFragment();
                            choose_fragment.setArguments(bundle);
                            //eseguo la transazione
                            fragmentTransaction.replace(R.id.fram, choose_fragment);
                            //uso backstack perchè android in automatico con il tasto indietro si muove tra activity e non tra fragment
                            //quindi aggiungo nella coda del back stack il frammento delle prenotazioni in modo che all'interno dei dettagli
                            //io possa tornare indietro
                            fragmentTransaction.addToBackStack("Fragment_Decide_Location");
                            fragmentTransaction.commit();
                        }
                    });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        //Toast.makeText(this.getContext(), "fuori", Toast.LENGTH_LONG).show();
        if (requestCode == PLACE_PICKER_REQUEST) {
            //Toast.makeText(this.getContext(), "stoQUadientro", Toast.LENGTH_LONG).show();
            if (resultCode == MainActivity.RESULT_OK) {
                //prendo i risultati
                Place place = PlacePicker.getPlace(this.getContext(), data);
                //da place posso prendere nome, indirizzo, latitudine e tutto quello che mi serve
                String toastMsg = String.format("Place: %s", place.getName());
                TextView indirizzo = (TextView) view.findViewById(R.id.indirizzo);
                //stampo a video indirizzo scelto
                indirizzo.setText(toastMsg);
            }
        }
    }


}
