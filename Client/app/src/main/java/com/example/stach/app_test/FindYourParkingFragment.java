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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindYourParkingFragment extends Fragment {

    int PLACE_PICKER_REQUEST = 1;
    private View view;

    public FindYourParkingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get layout
        view = inflater.inflate(R.layout.fragment_find_your_parking, container, false);
        //get buttons from xml
        ImageButton automaticSearch = (ImageButton) view.findViewById(R.id.btnAutomaticLocation);
        ImageButton inputSearch = (ImageButton) view.findViewById(R.id.btnInputLocation);
        //se ci sono informazioni dall'activity vuol dire che ho precedentemente preparato
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
                startPlacePickerActivity(view);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void startPlacePickerActivity(View view) {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = intentBuilder.build((MainActivity) getActivity());
            //restituisco i risultati all'activity
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
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
