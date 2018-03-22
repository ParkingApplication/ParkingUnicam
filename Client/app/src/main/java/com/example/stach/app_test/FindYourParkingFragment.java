package com.example.stach.app_test;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
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

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindYourParkingFragment extends Fragment {
    //progressbar
    static ProgressDialog caricamento = null;
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
        String locationProviders = Settings.Secure.getString(this.getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!(locationProviders.contains("gps")) || locationProviders == null || locationProviders.equals("")) {
            AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
            alertDialog.setTitle("Attiva il gps");
            alertDialog.setMessage("Per cercare un parcheggio è necessario attivare la localizzazione automatica di android, clicca su \"AttivaGPS\" per abilitare il gps");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Attiva GPS",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
            alertDialog.show();
            //quando utente clicca indietro nell'alert dialog
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    startActivity(new Intent(getContext(),MainActivity.class));
                    getActivity().finish();
                }
            });
        }
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
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this.getContext(), "permission not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {//permission granted
            //Toast.makeText(this.getContext(), "permission granted", Toast.LENGTH_SHORT).show();
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            String locality = getCityFromLatLong(Double.toString(location.getLatitude()),
                                    Double.toString(location.getLongitude()));
                            sendDataForViewPark(locality);


                        }
                    });

        }
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
            postData.put("citta", "Roma");
            postData.put("token", Parametri.Token);
        } catch (Exception e) {
        }
        // Avverto l'utente del tentativo di invio dei dati di login al server
        caricamento = ProgressDialog.show(this.getActivity(), "",
                "Ricerca parcheggi in corso...", true);
        // Creo ed eseguo una connessione con il server web
        Connessione conn = new Connessione(postData, "POST", this.getContext(), this.getActivity(), this);
        conn.execute(Parametri.IP + "/getParcheggiPerCitta");
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
                //TextView indirizzo = (TextView) view.findViewById(R.id.indirizzo);
                LatLng latLong = place.getLatLng();

                String locality = getCityFromLatLong(Double.toString(latLong.latitude),
                        Double.toString(latLong.latitude));
                sendDataForViewPark(locality);


            }
        }
    }


}
