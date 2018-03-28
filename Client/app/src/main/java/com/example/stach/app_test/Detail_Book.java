package com.example.stach.app_test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

public class Detail_Book extends Fragment implements ConnessioneListener {
    private Prenotazione prenotazione = null;

    public Detail_Book() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail__book, container, false);

        if (Parametri.prenotazioniInCorso != null) {
            Bundle bundle = getArguments();

            String parcheggio = bundle.getString("NomeParcheggio");
            int id = Integer.parseInt(bundle.getString("idPrenotazione"));

            TextView nomeParcheggio = view.findViewById(R.id.nomeParcheggio);
            TextView oraPrenotazioneParcheggio = view.findViewById(R.id.oraPrenotazioneParcheggio);
            TextView informazioni = view.findViewById(R.id.textViewInfo);

            nomeParcheggio.setText(parcheggio);

            for (int i = 0; i < Parametri.prenotazioniInCorso.size(); i++)
                if (Parametri.prenotazioniInCorso.get(i).getId() == id) {
                    prenotazione = Parametri.prenotazioniInCorso.get(i);
                    break;
                }

            if (prenotazione != null) {
                oraPrenotazioneParcheggio.setText(DateFormat.format("dd MMMM yyyy HH:mm:ss", prenotazione.getScadenza()).toString());
                informazioni.setText("Posto prenotato: " + TipoPosto.getNomeTipoPosto(prenotazione.getIdTipo()));
            }

            Button buttonDeletePrenotazione = view.findViewById(R.id.btnEraseBook);
            buttonDeletePrenotazione.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EliminaPrenotazione();
                }
            });

            Button buttonNaviga = view.findViewById(R.id.btnNavBook);
            buttonNaviga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Naviga();
                }
            });
        }
        else
            Toast.makeText(getContext(), "Riscontrati errori, prenotazione non trovata.", Toast.LENGTH_LONG).show();

        return view;
    }

    private void EliminaPrenotazione() {
        if (prenotazione == null)
            return;

        JSONObject postData = new JSONObject();

        try {
            postData.put("idPrenotazione", prenotazione.getId());
            postData.put("token", Parametri.Token);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Errore nell' elaborazione dei dati da inviare!", Toast.LENGTH_LONG).show();
            return;
        }

        Connessione conn = new Connessione(postData, "DELETE");
        conn.addListener(this);
        conn.execute(Parametri.IP + "/deletePrenotazione");
    }

    private void Naviga() {
        if (Parametri.parcheggi == null)
            return;

        LatLng destinazione = null;

        for (Parcheggio p: Parametri.parcheggi)
            if (p.getId() == prenotazione.getIdParcheggio()) {
                destinazione = p.getCoordinate();
                break;
            }

            if (destinazione != null) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destinazione.latitude + "," + destinazione.longitude + "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                getActivity().finish();
            }
    }

    @Override
    public void ResultResponse(String responseCode, String result) {
        if (responseCode == null) {
            Toast.makeText(getContext(), "ERRORE:\nConnessione Assente o server offline.", Toast.LENGTH_LONG).show();
            return;
        }

        if (responseCode.equals("400")) {
            String message = Connessione.estraiErrore(result);
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            return;
        }

        if (responseCode.equals("200")) {
            String message = Connessione.estraiSuccessful(result);
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            Parametri.prenotazioniInCorso.remove(prenotazione);
            getActivity().onBackPressed();
            return;
        }
    }
}
