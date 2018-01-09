package com.example.stach.app_test;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.w3c.dom.Text;

public class GPSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * This method take the user coordinate and showing in google maps
     */
    public void getUserPosition(View view) {
        /**
         EditText latitude = (EditText) findViewById(R.id.latitude);
         EditText longitude = (EditText) findViewById(R.id.longitude);
         String latitudeString = latitude.toString();
         String longitudeString = longitude.toString();
         // Create a Uri from an intent string. Use the result to create an Intent.
         Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + latitudeString + "," + longitudeString);
         // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
         Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
         // Make the Intent explicit by setting the Google Maps package
         mapIntent.setPackage("com.google.android.apps.maps");
         // Attempt to start an activity that can handle the Intent
         startActivity(mapIntent);
         */
        EditText latitude = (EditText) findViewById(R.id.latitude);
        EditText longitude = (EditText) findViewById(R.id.longitude);
        String latitudeString = latitude.toString();
        String longitudeString = longitude.toString();
        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }


    }

    /**
     * This method take the current position and showing in google maps
     */
    public void getCurrentPosition(View view) {
        //da cambiare: mettere locazione corrente
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
        startActivity(intent);

    }
}
