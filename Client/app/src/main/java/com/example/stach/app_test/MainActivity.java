package com.example.stach.app_test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Timer scadenza prenotazioni
    private Handler handler = new Handler();
    private final int TIMER = 7 * 1000; // 7 secondi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //DEFAULT FRAGMENT
        setTitle("Trova parcheggio");
        //assign default fragment
        FindYourParkingFragment fragment = new FindYourParkingFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fram, fragment, "Fragment Find Park");
        fragmentTransaction.commit();

        // Recupero i dati delle mie prenotaizoni e dei parcheggi
        GetPrenotazioni();
        GetParcheggi();
    }

    // Funzione per l'aggiornamento automatico dei posti liberi
    private Runnable runnable = new Runnable() {
        public void run() {
            ControllaScadenze();
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //choose fragment
        if (id == R.id.nav_profile) {
            setTitle("Il tuo profilo");
            ProfileFragment fragment = new ProfileFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "Fragment Profile");
            fragmentTransaction.addToBackStack("Profile_Fragment");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_card) {
            setTitle("Aggiorna dati carta");
            Carta_di_credito fragment = new Carta_di_credito();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "Fragment Carta");
            fragmentTransaction.addToBackStack("Card_Fragment");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_findPark) {
            setTitle("Trova il parcheggio");
            FindYourParkingFragment fragment = new FindYourParkingFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "Fragment Find Park");
            fragmentTransaction.addToBackStack("Fragment_Find_Park");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_your_book) {
            setTitle("Le tue prenotazioni");
            FragmentYour_Book fragment = new FragmentYour_Book();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "Fragment Book");
            fragmentTransaction.addToBackStack("Fragment_Book");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_logout) {
            Parametri.login_file.delete();
            //return to login page
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_share) {
            //TODO
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, TIMER);
    }

    // Ogni tot secondi viene chiamata per controllare le scadenze delle prenotazioni in corso
    private void ControllaScadenze() {
        if (Parametri.prenotazioniInCorso != null)
            for (int i = 0; i < Parametri.prenotazioniInCorso.size(); i++)
                if (Parametri.prenotazioniInCorso.get(i).getTempoScadenza() <= 0) {
                    new AlertDialog.Builder(this)
                            .setTitle("La tua prenotazione è scaduta")
                            .setMessage("la tua prenotazone nel parcheggio " + Parametri.parcheggi.get(i).getIndirizzo() + " è scaduta.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).create().show();
                    Parametri.prenotazioniInCorso.remove(i);
                }
        handler.postDelayed(runnable, TIMER);
    }

    public void GetPrenotazioni() {
        JSONObject richiesta = new JSONObject();

        try {
            richiesta.put("token", Parametri.Token);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Creo ed eseguo una connessione con il server web
        Connessione conn = new Connessione(richiesta, "POST");
        conn.addListener(ListenerGetPrenotazioni);
        conn.execute(Parametri.IP + "/getPrenotazioniInAttoUtente");
    }

    private ConnessioneListener ListenerGetPrenotazioni = new ConnessioneListener() {
        @Override
        public void ResultResponse(String responseCode, String result) {
            if (responseCode == null) {
                Toast.makeText(getApplicationContext(), "Errore di ricezione delle prenotazioni in corso,\nil server non risponde.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (responseCode.equals("400")) {
                String message = Connessione.estraiErrore(result);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                return;
            }

            if (responseCode.equals("200")) {
                List<Prenotazione> prenotazioni = new ArrayList<>();

                try {
                    JSONArray prenotazioniInAtto = (new JSONObject(result).getJSONArray("prenotazioniInAtto"));

                    for (int i = 0; i < prenotazioniInAtto.length(); i++)
                        prenotazioni.add(new Prenotazione(prenotazioniInAtto.getJSONObject(i).toString()));

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                Parametri.prenotazioniInCorso = prenotazioni;
            }
        }

    };

    public void GetParcheggi() {
        JSONObject postData = new JSONObject();
        Connessione conn = new Connessione(postData, "POST");
        conn.addListener(ListenerGetParcheggi);
        conn.execute(Parametri.IP + "/getAllParcheggi");
    }

    private ConnessioneListener ListenerGetParcheggi = new ConnessioneListener() {
        @Override
        public void ResultResponse(String responseCode, String result) {
            if (responseCode == null) {
                Toast.makeText(getApplicationContext(), "Errore di ricezione dei parcheggi,\nil server non risponde.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (responseCode.equals("400")) {
                String message = Connessione.estraiErrore(result);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                return;
            }

            if (responseCode.equals("200")) {
                ArrayList<Parcheggio> par = new ArrayList<Parcheggio>();

                try {
                    JSONObject allparcheggi = new JSONObject(result);
                    JSONArray parcheggi = allparcheggi.getJSONArray("parcheggi");

                    for (int i = 0; i < parcheggi.length(); i++)
                        par.add(new Parcheggio(parcheggi.get(i).toString()));

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                Parametri.parcheggi = par;
            }
        }
    };

}
