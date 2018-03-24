package com.example.stach.app_test;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.*;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMyLocationButtonClickListener,
        OnMyLocationClickListener, OnMapReadyCallback, OnMarkerClickListener {

    private GoogleMap mGoogleMap;
    private SupportMapFragment mapFrag;
    private LocationRequest mLocationRequest;
    private LocationManager lmanager;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private FusedLocationProviderClient mFusedLocationClient;

    private List<Parcheggio> parcheggi;
    private Marker scelta = null;

    // Per mettere il listener sul mio bottone
    private ViewGroup infoWindow;

    public MapActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        lmanager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        this.parcheggi = new ArrayList<Parcheggio>();

        // Prelevo i dati passati tramite intent
        Intent intent = getIntent();
        List<String> parcheggi = intent.getStringArrayListExtra("parcheggi");

        // Estraggo tutti i parcheggi
        try {
            for (String obj : parcheggi) {
                this.parcheggi.add(new Parcheggio(obj));
            }
        } catch (Exception e) {
            // Gestire l'errore e/o chiudere tutto
            finish();
        }

        // Salvo i parcheggi in Parametri
        if (!this.parcheggi.isEmpty())
            Parametri.parcheggi = this.parcheggi;

        Button ButtonCerca = findViewById(R.id.buttonPosition);
        ButtonCerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonSearchClick();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        // Fermo l'aggiornamento della posizione
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            mFusedLocationClient = null;
        }
    }

    /** DA PROBLEMI (PARTE POCO PRIMA O POCO DOPO OnStart)
    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Controllo che il GPS sia acceso
            if (lmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false)
                checkGPS();
            else {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            }

        } else {
            checkLocationPermission();
        }
    }
*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mLocationRequest = new LocationRequest();
        // Un minuto di intervallo (ogni minuto prende la posizione corrente in automatico)
        mLocationRequest.setInterval(1000 * 60 * 1);
        mLocationRequest.setFastestInterval(1000 * 60 * 1);
        // Precisione gps (consuma troppa batteria ma probabilmente non è inerente alle specifiche del nostro progetto)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Controllo che il GPS sia acceso
            if (lmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false)
                checkGPS();

            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnMyLocationButtonClickListener(this);
            mGoogleMap.setOnMyLocationClickListener(this);
            mGoogleMap.setOnMarkerClickListener(this);

            // Metto tutti i parcheggi nella mappa come Marker blu
            for (Parcheggio p : parcheggi) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(p.getCoordinate());
                markerOptions.title(p.getIndirizzo());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mGoogleMap.addMarker(markerOptions);
            }

        } else {
            checkLocationPermission();
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                //Toast.makeText(getApplicationContext(), "Tu sei qui: " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();

                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                // Creo un maker della mia posizione corrente
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("La tua posizione");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    // Richiedo i permessi per accedere alla posizione
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Attiva i permessi per il GPS")
                        .setMessage("Per cercare un parcheggio è necessario autorizzare quest' applicaizone all' accesso della posizione corrente.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                        // Se l'utente preme indietro baro in stile stacchio
                        //startActivity(new Intent(getApplicationContext(), MapActivity.class));
                        //finish();
                    }
                }).create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            // Controllo che il GPS sia acceso
            if (lmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false)
                checkGPS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permessi garantiti
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // Controllo che il GPS sia acceso
                        if (lmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false)
                            checkGPS();
                        else {
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                            mGoogleMap.setMyLocationEnabled(true);
                        }
                    }
                } else {
                    // Permessi negati, disabilitare qui le funzioni per il gps
                    Toast.makeText(this, "Permessi negati.\nNon puoi utilizzare correttamente quest applicazione", Toast.LENGTH_LONG).show();
                    checkLocationPermission();
                }
                return;
            }
        }
    }

    // Serve a distinguere i contorlli di varie azioni (in questo caso ne abbiamo solo una per l'attivazione del gps)
    private final int ACTION_LOCATION_SETTING = 100;

    // Controlla se il GPS è accesso
    private void checkGPS() {
        if (lmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
            new AlertDialog.Builder(this)
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
                    //startActivity(new Intent(getApplicationContext(), MapActivity.class));
                    //finish();
                }
            }).create().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_LOCATION_SETTING:
                if (lmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
                    Toast.makeText(this, "GPS spento.\nNon puoi utilizzare correttamente quest applicazione.", Toast.LENGTH_LONG).show();
                    checkGPS();
                } else {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    } else
                        checkLocationPermission();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // Click sul bottone in alto a destra per visualizzare la tua posizione
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getApplicationContext(), "Tu sei qua.", Toast.LENGTH_SHORT).show();
        return false;
    }

    // Click sul pallino blu della tua posizione (non il marker, il pallino piccolo di google maps)
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        //Toast.makeText(getApplicationContext(), "Le tue coordinate: " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    // Click su un marker qualsiasi presente nella mappa (i marker sono i palloncini colorati che indicano posizioni specifiche sulla mappa)
    @Override
    public boolean onMarkerClick(final Marker marker) {
        scelta = marker;

        Toast.makeText(getApplicationContext(), "Hai scelto " + marker.getTitle(), Toast.LENGTH_LONG).show();
        // Sposto la visuale e faccio lo zoom sulla posizione cliccata
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(scelta.getPosition(), 11));

        return false;
    }

    public void ButtonSearchClick() {
        if (scelta != null) {
            if (!mCurrLocationMarker.equals(scelta)) { // L'utente ha scelto un parcheggio
                new AlertDialog.Builder(this)
                        .setTitle("Selezione parcheggio")
                        .setMessage(scelta.getTitle() + ".\n\nVuoi cercare posto in questo parcheggio ?")
                        .setPositiveButton("Cerca posto",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // QUI ESEGUIRE RICERCA POSTI ETC
                                        // I dati sono in scelta
                                    }
                                }).setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                    }
                }).create().show();
            }
            else { // L'utente ha scelto la sua posizione attuale
                new AlertDialog.Builder(this)
                        .setTitle("Selezione parcheggio")
                        .setMessage("Vuoi cercare i parcheggi più vicini alla tua posizione in automatico ?")
                        .setPositiveButton("Cerca",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // QUI ESEGUIRE RICERCA POSTI ETC
                                        // I dati sono in scelta
                                    }
                                }).setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                    }
                }).create().show();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "Devi prima selezionare un parcheggio.", Toast.LENGTH_SHORT).show();
    }
}