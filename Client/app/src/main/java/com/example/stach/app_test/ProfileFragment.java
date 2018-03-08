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
    private TextView t_user;
    private TextView t_saldo;

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

        //Comunico con il server per ottenre i dati dell' utente:
        Richiesta_Utente(Parametri.Token);

        t_user = (TextView) view.findViewById(R.id.username);
        t_user.setText(t_user.getText()+"Lorenzo Stacchio");
        t_saldo = (TextView) view.findViewById(R.id.money);
        t_saldo.setText(t_saldo.getText()+"100000.00");
        // return inflate
        return view;
    }



    //METODO DA TESTARE!!
    //Utilizzato per Inviare i dati al server
    public boolean Richiesta_Utente(String token)
    {
        try{
            //Invio i dati al server
            URL url = new URL("Parametri.IP");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");


            String input = "{\"token\":" + token + "}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            //Leggo la risposta dal server
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            line = sb.toString();

            //Verifico i dati che ho letto
            JSONObject jObject = new JSONObject(line);
            JSONArray jArray = jObject.getJSONArray("ARRAYNAME");   //BOH
            String user = null;
            String email = null;
            String password = null;
            String carta_di_credito = null;
            String saldo = null;
            String nome = null;
            String cognome = null;
            String data_nascita = null;
            String telefono = null;
            //User, email, password, carta di ctredito, saldo , nome , cognome , data di nascita , telefono
            for (int i=0; i < jArray.length(); i++)
            {
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array
                    user = oneObject.getString("USER");
                    email = oneObject.getString("EMAIL");
                    password = oneObject.getString("PASSWORD");
                    carta_di_credito = oneObject.getString("CARTA");
                    saldo = oneObject.getString("SALDO");
                    nome = oneObject.getString("NOME");
                    cognome = oneObject.getString("COGNOME");
                    data_nascita = oneObject.getString("NASCITA");
                    telefono = oneObject.getString("TELEFONO");
                } catch (JSONException e) {
                    // Oops
                }
            }
            return true;
        }
        catch(Exception e){ //Devo scrivere eccezione dati login errati
            return false;
        }

    }



}
