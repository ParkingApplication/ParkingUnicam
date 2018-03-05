package com.example.stach.app_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindYourParkingFragment extends Fragment {

    private final int REQUEST_CODE_PLACEPICKER = 1;

    public FindYourParkingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get layout
        View view = inflater.inflate(R.layout.fragment_find_your_parking, container, false);
        //get buttons from xml
        ImageButton automaticSearch = (ImageButton) view.findViewById(R.id.btnAutomaticLocation);
        ImageButton inputSearch = (ImageButton) view.findViewById(R.id.btnInputLocation);
        //setto le proprietà dei bottoni
        //AUTOMATIC SEARCH
        automaticSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Automatic_Search_Fragment automaticSearchFragment = new Automatic_Search_Fragment();
                //eseguo la transazione
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //eseguo la transazione
                fragmentTransaction.replace(R.id.fram, automaticSearchFragment);
                //uso backstack perchè android in automatico con il tasto indietro si muove tra activity e non tra fragment
                //quindi aggiungo nella coda del back stack il frammento delle prenotazioni in modo che all'interno dei dettagli
                //io possa tornare indietro
                fragmentTransaction.addToBackStack("Fragment_Automatic_Search");
                fragmentTransaction.commit();
            }
        });

        //INPUT SEARCH
        inputSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call method
                startPlacePickerActivity();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void startPlacePickerActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = intentBuilder.build((MainActivity)getActivity());
            startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
