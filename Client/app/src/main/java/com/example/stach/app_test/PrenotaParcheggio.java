package com.example.stach.app_test;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;


public class PrenotaParcheggio extends Fragment {
    int index;

    public static PrenotaParcheggio newInstance(int indice) {
        PrenotaParcheggio fragment = new PrenotaParcheggio();
        Bundle args = new Bundle();
        args.putInt("indice", indice);
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prenota_parcheggio, container, false);
        //get information
        index = getArguments().getInt("indice", -1);

        TextView informazioni = (TextView) view.findViewById(R.id.textViewViaParcheggio);
        informazioni.setText(Parametri.parcheggi_vicini.get(index).getIndirizzo_format());
        RadioButton rd = (RadioButton)view.findViewById(R.id.RadioAuto);
        String str = String.valueOf(Parametri.parcheggi_vicini.get(index).getPostiLiberi()[TipoPosto.AUTO]);
        rd.setText(str);
        rd = (RadioButton)view.findViewById(R.id.RadioCamper);
        str = String.valueOf(Parametri.parcheggi_vicini.get(index).getPostiLiberi()[TipoPosto.CAMPER]);
        rd.setText(str);
        rd = (RadioButton)view.findViewById(R.id.RadioMoto);
        str = String.valueOf(Parametri.parcheggi_vicini.get(index).getPostiLiberi()[TipoPosto.MOTO]);
        rd.setText(str);
        rd = (RadioButton)view.findViewById(R.id.RadioAutobus);
        str = String.valueOf(Parametri.parcheggi_vicini.get(index).getPostiLiberi()[TipoPosto.AUTOBUS]);
        rd.setText(str);
        rd = (RadioButton)view.findViewById(R.id.RadioDisabile);
        str = String.valueOf(Parametri.parcheggi_vicini.get(index).getPostiLiberi()[TipoPosto.DISABILE]);
        rd.setText(str);
        Button btn = (Button) view.findViewById(R.id.BtnPrenota);

        //Prenoto
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                Prenotazione();

            }
        });
        return view;
    }

    public void Prenotazione() {
        // Avverto l'utente del tentativo di ricezione dei dati per i parcheggi
       // ProgressDialog caricamento = ProgressDialog.show(getContext(), "Recupero dati parcheggi",
         //       "Prenotazione in corso...", true);

        JSONObject postData = new JSONObject();
        Toast.makeText(this.getContext(),"Fin qua funziona!",Toast.LENGTH_LONG).show();
        /*try {
            postData.put("lat", curLocation.getLatitude());
            postData.put("long", curLocation.getLongitude());
            postData.put("token", Parametri.Token);
        } catch (Exception e) {
        }
        Connessione conn = new Connessione(postData, "POST");
        conn.addListener(ListenerParcheggiVicini);
        conn.execute(Parametri.IP + "/getParcheggiFromCoordinate");*/
    }
}
