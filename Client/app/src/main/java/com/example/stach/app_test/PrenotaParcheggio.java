package com.example.stach.app_test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class PrenotaParcheggio extends Fragment {
    int index;
    View view;
    ProgressDialog caricamento;
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

        view = inflater.inflate(R.layout.fragment_prenota_parcheggio, container, false);
        //get information
        index = getArguments().getInt("indice", -1);

        TextView informazioni =  view.findViewById(R.id.textViewViaParcheggio);
        informazioni.setText(Parametri.parcheggi_vicini.get(index).getIndirizzo_format());
        RadioButton rd = view.findViewById(R.id.RadioAuto);
        String str = String.valueOf(Parametri.parcheggi_vicini.get(index).getPostiLiberi()[TipoPosto.AUTO]);
        rd.setText(str);
        rd = view.findViewById(R.id.RadioCamper);
        str = String.valueOf(Parametri.parcheggi_vicini.get(index).getPostiLiberi()[TipoPosto.CAMPER]);
        rd.setText(str);
        rd = view.findViewById(R.id.RadioMoto);
        str = String.valueOf(Parametri.parcheggi_vicini.get(index).getPostiLiberi()[TipoPosto.MOTO]);
        rd.setText(str);
        rd = view.findViewById(R.id.RadioAutobus);
        str = String.valueOf(Parametri.parcheggi_vicini.get(index).getPostiLiberi()[TipoPosto.AUTOBUS]);
        rd.setText(str);
        rd = view.findViewById(R.id.RadioDisabile);
        str = String.valueOf(Parametri.parcheggi_vicini.get(index).getPostiLiberi()[TipoPosto.DISABILE]);
        rd.setText(str);
        Button btn = view.findViewById(R.id.BtnPrenota);

        //Prenoto
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                Prenotazione( view);

            }
        });
        return view;
    }

    public void Prenotazione(View view) {
        // Avverto l'utente del tentativo di ricezione dei dati per i parcheggi
        caricamento = ProgressDialog.show(getContext(), "Recupero dati parcheggi",
               "Prenotazione in corso...", true);
        int tipo_parcheggio = -1;
        RadioButton rdAuto = view.findViewById(R.id.RadioAuto);
        RadioButton rdMoto = view.findViewById(R.id.RadioMoto);
        RadioButton rdCamper = view.findViewById(R.id.RadioCamper);
        RadioButton rdAutobus = view.findViewById(R.id.RadioAutobus);
        RadioButton rdDisabile = view.findViewById(R.id.RadioDisabile);
        for (int i = 0; i < 5; i++)
        {
            if (rdAuto.isChecked())
                tipo_parcheggio = TipoPosto.AUTO;
            if (rdMoto.isChecked())
                tipo_parcheggio = TipoPosto.MOTO;
            if (rdCamper.isChecked())
                tipo_parcheggio = TipoPosto.CAMPER;
            if (rdAutobus.isChecked())
                tipo_parcheggio = TipoPosto.AUTOBUS;
            if (rdDisabile.isChecked())
                tipo_parcheggio = TipoPosto.DISABILE;
        }
        if (tipo_parcheggio == -1)
        {
            caricamento.dismiss();
            Toast.makeText(this.getContext(),"Selezionare un tipo di parcheggio!",Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject postData = new JSONObject();

        try {
            postData.put("idParcheggio", Parametri.parcheggi_vicini.get(index).getId());
            postData.put("tipoParcheggio",tipo_parcheggio);
            postData.put("token", Parametri.Token);
        } catch (Exception e) {
        }
        Connessione conn = new Connessione(postData, "POST");
        conn.addListener(ListenerConfermaPrenotazione);
        conn.execute(Parametri.IP + "/effettuaPrenotazione");
    }

    private ConnessioneListener ListenerConfermaPrenotazione = new ConnessioneListener() {
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


                try {
                    JSONObject QrCode = new JSONObject(result);
                    String qr = QrCode.getString("QR_Code");
                    String data_scadenza = QrCode.getString("scadenza");
                    Prenotazioni.Qr_code = new ArrayList<>();
                    Prenotazioni.Data_scadenza = new ArrayList<>();
                    Prenotazioni.Qr_code.add(qr);
                    Prenotazioni.Data_scadenza.add(data_scadenza);
                } catch (Exception e) {
                    caricamento.dismiss();
                    Toast.makeText(getContext(), "Errore di risposta del server.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Lascio estrarre la lista dei parcheggi alla MapActivity (si potrebbe pure fare qua senza passarglierli, per ora lascio così)
                caricamento.dismiss();
                // Invio i dati tramite intent
                Toast.makeText(getContext(), "Prenotazione effettutata con successo!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
                return;
            }

        }

    };
}
