package com.example.stach.app_test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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
    private final int TIMER = 10 * 1000; // 10 secondi

    private ProgressDialog caricamento = null;
    private boolean launchPrenotaizoni = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

        // Recupero i dati delle mie prenotaizoni (per il timer delle scadenze)
        GetDatiPrenotazioni(false);
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
        // Se il menù è aperto lo chiudo
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
            setTitle("Settings");
            OptionsMenu fragment = new OptionsMenu();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "settings");
            fragmentTransaction.addToBackStack("Settings_Fragment");
            fragmentTransaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager sfm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = sfm.beginTransaction();

        //choose fragment
        if (id == R.id.nav_profile) {
            setTitle("Il tuo profilo");
            ProfileFragment fragment = new ProfileFragment();
            fragmentTransaction.replace(R.id.fram, fragment, "Fragment Profile");
            fragmentTransaction.addToBackStack("Profile_Fragment");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_card) {
            setTitle("Aggiorna dati carta");
            Carta_di_credito fragment = new Carta_di_credito();
            fragmentTransaction.replace(R.id.fram, fragment, "Fragment Carta");
            fragmentTransaction.addToBackStack("Card_Fragment");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_findPark) {
            setTitle("Trova parcheggio");
            FindYourParkingFragment fragment = new FindYourParkingFragment();
            fragmentTransaction.replace(R.id.fram, fragment, "Fragment Find Park");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_your_book) {
            GetDatiPrenotazioni(true);
        } else if (id == R.id.nav_logout) {
            Parametri.login_file.delete();
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
        if (Parametri.prenotazioniInCorso != null && Parametri.parcheggi != null)
            for (int i = 0; i < Parametri.prenotazioniInCorso.size(); i++)
                if (Parametri.prenotazioniInCorso.get(i).getTempoScadenza() <= 0) {
                    new AlertDialog.Builder(this)
                            .setTitle("Penotazione scaduta")
                            .setMessage("La tua prenotazone nel parcheggio " + Parametri.parcheggi.get(i).getIndirizzo() + " è scaduta.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).create().show();
                    Parametri.prenotazioniInCorso.remove(i);
                } else if (Parametri.prenotazioniInCorso.get(i).getTempoScadenza() < Parametri.TEMPO_AVVISO)
                    new AlertDialog.Builder(this)
                            .setTitle("Avviso scadenza")
                            .setMessage("La tua prenotazone nel parcheggio " + Parametri.parcheggi.get(i).getIndirizzo()
                                    + " scadrà tra " + ((Parametri.TEMPO_AVVISO / 1000) / 60) + " minuti.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).create().show();
                else
                    GetDatiPrenotazioni(false);

        handler.postDelayed(runnable, TIMER);
    }

    public void GetDatiPrenotazioni(boolean forLaunchPrenotaizoni) {
        JSONObject postData = new JSONObject();

        try {
            postData.put("token", Parametri.Token);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        launchPrenotaizoni = forLaunchPrenotaizoni;

        if (launchPrenotaizoni) {
            caricamento = ProgressDialog.show(MainActivity.this, "Recupero dati",
                    "Connessione con il server in corso...", true);

        }

        Connessione connPre = new Connessione(postData, "POST");
        connPre.addListener(ListenerGetPrenotazioni);
        connPre.execute(Parametri.IP + "/getPrenotazioniInAttoUtente");
    }

    private ConnessioneListener ListenerGetPrenotazioni = new ConnessioneListener() {
        @Override
        public void ResultResponse(String responseCode, String result) {
            if (responseCode == null) {
                if (launchPrenotaizoni) {
                    caricamento.dismiss();
                    launchPrenotaizoni = false;
                }
                Toast.makeText(getApplicationContext(), "Errore di ricezione delle prenotazioni in corso.\nIl server non risponde.", Toast.LENGTH_LONG).show();
                return;
            }

            if (responseCode.equals("400")) {
                if (launchPrenotaizoni) {
                    caricamento.dismiss();
                    launchPrenotaizoni = false;
                }
                String message = Connessione.estraiErrore(result);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                    if (launchPrenotaizoni) {
                        caricamento.dismiss();
                        launchPrenotaizoni = false;
                    }
                    Toast.makeText(getApplicationContext(), "Errore di risposta del server.\nImpossibile visualizzare le prenotazioni.", Toast.LENGTH_LONG).show();
                    return;
                }

                Parametri.prenotazioniInCorso = prenotazioni;

                if (Parametri.parcheggi == null) {
                    Connessione connPar = new Connessione(new JSONObject(), "POST");
                    connPar.addListener(ListenerGetParcheggi);
                    connPar.execute(Parametri.IP + "/getAllParcheggi");
                } else if (launchPrenotaizoni) {
                    caricamento.dismiss();
                    setTitle("Le tue prenotazioni");
                    FragmentYour_Book fragment = new FragmentYour_Book();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fram, fragment, "Fragment Book");
                    fragmentTransaction.addToBackStack("Fragment_Book");
                    fragmentTransaction.commit();
                    launchPrenotaizoni = false;
                }
            }
        }

    };

    private ConnessioneListener ListenerGetParcheggi = new ConnessioneListener() {
        @Override
        public void ResultResponse(String responseCode, String result) {
            if (responseCode == null) {
                if (launchPrenotaizoni) {
                    caricamento.dismiss();
                    launchPrenotaizoni = false;
                }
                Toast.makeText(getApplicationContext(), "Errore di ricezione dei parcheggi.\nIl server non risponde.", Toast.LENGTH_LONG).show();
                return;
            }

            if (responseCode.equals("400")) {
                if (launchPrenotaizoni) {
                    caricamento.dismiss();
                    launchPrenotaizoni = false;
                }
                String message = Connessione.estraiErrore(result);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                    if (launchPrenotaizoni) {
                        caricamento.dismiss();
                        launchPrenotaizoni = false;
                    }
                    Toast.makeText(getApplicationContext(), "Errore di risposta del server.\nImpossibile visualizzare le prenotazioni.", Toast.LENGTH_LONG).show();
                    return;
                }

                Parametri.parcheggi = par;

                if (launchPrenotaizoni) {
                    caricamento.dismiss();

                    setTitle("Le tue prenotazioni");
                    FragmentYour_Book fragment = new FragmentYour_Book();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fram, fragment, "Fragment Book");
                    fragmentTransaction.addToBackStack("Fragment_Book");
                    fragmentTransaction.commit();
                    launchPrenotaizoni = false;
                }
            }
        }
    };
}
