package com.example.stach.app_test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


public class FindYourParkingFragment extends Fragment implements GpsChangeListener {
    // Locaizone corrente
    private Location curLocation = null;
    // GPSTracker personalizzato
    private GPSTracker gpsTracker;
    // View corrente
    private View view;

    ProgressDialog caricamento = null;

    public FindYourParkingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find_your_parking, container, false);

        ImageButton automaticSearch = view.findViewById(R.id.btnAutomaticLocation);
        ImageButton inputSearch = view.findViewById(R.id.btnInputLocation);

        // Ricerca manuale tra tutti i parcheggi presenti nel database (recuperati dal server ovviamente)
        inputSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LanciaMappe();
            }
        });

        // Ricerca automatica della posizione corrente
        automaticSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    checkLocationPermission();
                else if (gpsTracker.isGPSon()) {
                    gpsTracker.StartToGetLocation();
                    Send_Data();
                }
                else
                    checkGPS();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Creo il GPSTracker e mi metto in ascolto sul mio evento GpsLocationChange
        gpsTracker = new GPSTracker(getContext());
        gpsTracker.addListener(this);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            checkLocationPermission();
        else if (gpsTracker.isGPSon())
            gpsTracker.StartToGetLocation();
        else
            checkGPS();
    }

    // Serve a distinguere i contorlli di vari permessi (in questo caso ne abbiamo solo uno per il gps)
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 4;

    // Richiedo i permessi per accedere alla posizione
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Attiva i permessi per il GPS")
                        .setMessage("Per cercare un parcheggio è necessario autorizzare quest' applicaizone all' accesso della posizione corrente.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                        // Se l'utente preme indietro baro in stile stacchio
                        /**
                         * startActivity(new Intent(getContext(), MainActivity.class));
                         * getActivity().finish();
                         */
                    }
                }).create().show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            gpsTracker.StartToGetLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permessi garantiti
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // Controllo che il GPS sia acceso
                        if (!gpsTracker.isGPSon())
                            checkGPS();
                        else
                            gpsTracker.StartToGetLocation();
                    }
                } else {
                    // Permessi negati, disabilitare qui le funzioni per il gps
                    Toast.makeText(getContext(), "Permessi negati.\nNon puoi utilizzare correttamente quest applicazione", Toast.LENGTH_LONG).show();
                    checkLocationPermission();
                }
            }
        }
    }

    // Serve a distinguere i contorlli di varie azioni (in questo caso ne abbiamo solo una per l'attivazione del gps)
    private final int ACTION_LOCATION_SETTING = 100;
    // Controlla se il GPS è accesso
    private void checkGPS() {
        if (!gpsTracker.isGPSon()) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Devi attivare il GPS")
                    .setMessage("Per cercare un parcheggio è necessario attivare la localizzazione automatica.")
                    .setPositiveButton("Attiva GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent locationSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(locationSettingIntent, ACTION_LOCATION_SETTING);
                                }
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.cancel();
                    // Se l'utente preme indietro baro in stile stacchio
                    /**
                     * startActivity(new Intent(getContext(), MainActivity.class));
                     * getActivity().finish();
                     */
                }
            }).create().show();
        } else
            gpsTracker.StartToGetLocation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_LOCATION_SETTING:
                if (!gpsTracker.isGPSon()) {
                    Toast.makeText(getContext(), "GPS spento.\nNon puoi utilizzare correttamente quest applicazione", Toast.LENGTH_LONG).show();
                    checkGPS();
                } else
                    gpsTracker.StartToGetLocation();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void AggiornaIndirizzo() {
        TextView t_indirizzo = view.findViewById(R.id.indirizzo);
        // Ricavo la via dalle coordinate con il Codificatore
        CodificatoreIndirizzi cod = new CodificatoreIndirizzi(getContext());
        t_indirizzo.setText(cod.getIndirizzoFromLocation(curLocation));

    }

    private void LanciaMappe() {
        // Avverto l'utente del tentativo di ricezione dei dati per i parcheggi
        caricamento = ProgressDialog.show(getContext(), "Recupero dati parcheggi",
                "Connessione con il server in corso...", true);

        JSONObject postData = new JSONObject();
        Connessione conn = new Connessione(postData, "POST");
        conn.addListener(ListenerLanciaMappe);
        conn.execute(Parametri.IP + "/getAllParcheggi");
    }

    public void StopGPS() {
        gpsTracker.StopGPS();
    }

    @Override
    public void GpsLocationChange(Location location) {
        curLocation = location;
        if (curLocation != null)
            AggiornaIndirizzo();
    }

    @Override
    public void onPause() {
        super.onPause();
        StopGPS();
    }

    /**
     * Va sistemato perché danno problemi quando si passa da un activity o fragment all' altro
     *
    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            checkLocationPermission();
        else {
            if (gpsTracker.isGPSon()) {
                gpsTracker.StartToGetLocation();
            } else
                checkGPS();
        }
    }
    */

    // Listener con funzione per la ricezione dei risultati della Connessione con il server (per avere la lista dei parcheggi nella mappa)
    private ConnessioneListener ListenerLanciaMappe = new ConnessioneListener() {
        @Override
        public void ResultResponse(String responseCode, String result) {
            if (responseCode == null) {
                caricamento.dismiss();
                Toast.makeText(getContext(), "ERRORE:\nConnessione Assente o server offline.", Toast.LENGTH_LONG).show();
                return;
            }

            if (responseCode.equals("400")) {
                caricamento.dismiss();
                String message = Connessione.estraiErrore(result);
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                return;
            }

            if (responseCode.equals("200")) {
                // Estraggo i dati dei parcheggi restituiti dal server
                ArrayList<String> par = new ArrayList<String>();
                try {
                    JSONObject allparcheggi = new JSONObject(result);
                    JSONArray parcheggi  = allparcheggi.getJSONArray("parcheggi");

                    for (int i = 0; i < parcheggi.length(); i++)
                        par.add(parcheggi.getJSONObject(i).toString());

                } catch (Exception e) {
                    caricamento.dismiss();
                    Toast.makeText(getContext(), "Errore di risposta del server.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Lascio estrarre la lista dei parcheggi alla MapActivity (si potrebbe pure fare qua senza passarglierli, per ora lascio così)
                caricamento.dismiss();
                // Invio i dati tramite intent
                Intent intent = new Intent(getContext(), MapActivity.class);
                intent.putStringArrayListExtra("parcheggi", par);
                startActivity(intent);
                return;
            }
        }
    };

    public void Send_Data()
    {
        // Avverto l'utente del tentativo di ricezione dei dati per i parcheggi
        caricamento = ProgressDialog.show(getContext(), "Recupero dati parcheggi",
                "Ricerca Parcheggi vicini in corso...", true);

        JSONObject postData = new JSONObject();
        try {
            postData.put("lat",  curLocation.getLatitude());
            postData.put("long",  curLocation.getLongitude());
            postData.put("token", Parametri.Token);
        }catch (Exception e){}
        Connessione conn = new Connessione(postData, "POST");
        conn.addListener(ListenerParcheggiVicini);
        conn.execute(Parametri.IP + "/getParcheggiFromCoordinate");

    }


    private ConnessioneListener ListenerParcheggiVicini = new ConnessioneListener() {
        @Override
        public void ResultResponse(String responseCode, String result) {
            if (responseCode == null) {
                caricamento.dismiss();
                Toast.makeText(getContext(), "ERRORE:\nConnessione Assente o server offline.", Toast.LENGTH_LONG).show();
                return;
            }

            if (responseCode.equals("400")) {
                caricamento.dismiss();
                String message = Connessione.estraiErrore(result);
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                return;
            }

            if (responseCode.equals("200")) {
                // Estraggo i dati dei parcheggi restituiti dal server
                ArrayList<Parcheggio> par = new ArrayList<>();

                try {
                    JSONObject allparcheggi = new JSONObject(result);
                    JSONArray parcheggi  = allparcheggi.getJSONArray("parcheggi");

                    for (int i = 0; i < parcheggi.length(); i++)
                        par.add(new Parcheggio(parcheggi.getJSONObject(i).toString()));
                    Parametri.parcheggi_vicini = par;
                } catch (Exception e) {
                    caricamento.dismiss();
                    Toast.makeText(getContext(), "Errore di risposta del server.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Lascio estrarre la lista dei parcheggi alla MapActivity (si potrebbe pure fare qua senza passarglierli, per ora lascio così)
                caricamento.dismiss();
                // Invio i dati tramite intent
                CallFragment();
                return;
            }

        }

    };
    public void CallFragment()
    {
        Visualizza_parcheggi fragment = new Visualizza_parcheggi();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fram, fragment, "Visualizza_parcheggi");
        fragmentTransaction.addToBackStack("Visualizza_parcheggi");
        fragmentTransaction.commit();
    }
}
