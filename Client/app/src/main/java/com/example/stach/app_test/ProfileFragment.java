package com.example.stach.app_test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private ImageView mImageView;
    private TextView t_nome;
    private TextView t_cognome;
    private TextView t_data;
    private TextView t_telefono;
    private TextView t_email;
    private TextView t_password;
    private TextView t_saldo;
    private TextView t_user;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_profile, container, false);
        //injection of user information in structure elements
        mImageView = (ImageView) view.findViewById(R.id.image_profile);
        mImageView.setImageResource(R.mipmap.ic_profile);



        t_nome = (TextView) view.findViewById(R.id.Nome);
        t_nome.setText(t_nome.getText()+Parametri.nome );

        t_cognome = (TextView) view.findViewById(R.id.Cognome);
        t_cognome.setText(t_cognome.getText()+Parametri.cognome );

        t_telefono = (TextView) view.findViewById(R.id.Telefono);
        t_telefono.setText(t_telefono.getText()+Parametri.telefono );

        t_data = (TextView) view.findViewById(R.id.Data_Nascita);
        t_data.setText(t_data.getText()+Parametri.data_nascita );

        t_email = (TextView) view.findViewById(R.id.Email);
        t_email.setText(t_email.getText()+Parametri.email );

        t_saldo = (TextView) view.findViewById(R.id.Saldo);
        t_saldo.setText(t_saldo.getText()+Parametri.saldo );


        t_user = (TextView) view.findViewById(R.id.Username);
        t_user.setText(t_user.getText()+Parametri.username );

        // return inflate

        return view;

    }
    void Prova(){}






}
