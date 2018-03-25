package com.example.stach.app_test;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //DEFAULT FRAGMENT
        setTitle("Trova parcheggio");
        //assign default fragment
        FindYourParkingFragment fragment = new FindYourParkingFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fram, fragment, "Fragment Find Park");
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
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

    @SuppressWarnings("StatementWithEmptyBody") // <=========================== Sarebbe meglio evitare e correggere StatementWithEmptyBody
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
            fragmentTransaction.commit();
        } else if (id == R.id.nav_your_book) {
            setTitle("Le tue prenotazioni");
            FragmentYour_Book fragment = new FragmentYour_Book();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "Fragment Book");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_logout) {
            //return to login page
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();  // <====================== Bisogna chiudere la main activity altrimenti non si è veramente sloggati.
        } else if (id == R.id.nav_share) {
            //TODO
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setTitle("Trova il parcheggio");
                    FindYourParkingFragment fragment = new FindYourParkingFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fram, fragment, "Fragment Find Park");
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(this, "non hai garantito il permesso", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}