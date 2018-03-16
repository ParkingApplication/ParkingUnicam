package com.example.stach.app_test;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.Task;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindYourParkingFragment extends Fragment {

    //questi attributi servono per riconoscere quale da quale activity voglio i risultati con la callback
    //li scrivo nel pacchetto di invio
    int PLACE_PICKER_REQUEST = 1;
    //view del contesto
    private View view;
    //fused location provider
   // private FusedLocationProviderClient mFusedLocationClient;
    private PlaceDetectionClient placeDetectionClient;


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
        //INPUT SEARCH
        inputSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call method
                startPlacePickerInputActivity();
            }
        });
        //AUTOMATIC SEARCH
        //instantiate fused location client
       // mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        placeDetectionClient = Places.getPlaceDetectionClient(this.getContext(), null);
        //setto le proprietà dei bottoni
        automaticSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaceAutomaticPickerInputActivity();
            }
        });
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
        /**
        Task<PlaceLikelihoodBufferResponse> placeResult = placeDetectionClient.getCurrentPlace(null);
        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                }
                likelyPlaces.release();
            }
        });*/
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
