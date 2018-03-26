package com.example.stach.app_test;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PrenotaParcheggio extends Fragment {
    private int index;
    private int tipo_parcheggio;
    private View view;
    private ProgressDialog caricamento;

    // Intervallo ed handler per aggiornare i posti liberi
    private final int TIMER = 5 * 1000; // 5 secondi
    private Handler handler = new Handler();

    public static PrenotaParcheggio newInstance(int indice) {
        PrenotaParcheggio fragment = new PrenotaParcheggio();
        Bundle args = new Bundle();
        args.putInt("ID", indice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_prenota_parcheggio, container, false);

        int id = getArguments().getInt("ID", -1);

        for (index = 0; index < Parametri.parcheggi.size(); index++)
            if (Parametri.parcheggi.get(index).getId() == id)
                break;

        TextView informazioni = view.findViewById(R.id.textViewViaParcheggio);
        informazioni.setText(Parametri.parcheggi.get(index).getIndirizzo());
        RadioButton rd = view.findViewById(R.id.RadioAuto);
        String str = String.valueOf(Parametri.parcheggi.get(index).getPostiLiberi()[TipoPosto.AUTO]);
        rd.setText(str);
        rd = view.findViewById(R.id.RadioCamper);
        str = String.valueOf(Parametri.parcheggi.get(index).getPostiLiberi()[TipoPosto.CAMPER]);
        rd.setText(str);
        rd = view.findViewById(R.id.RadioMoto);
        str = String.valueOf(Parametri.parcheggi.get(index).getPostiLiberi()[TipoPosto.MOTO]);
        rd.setText(str);
        rd = view.findViewById(R.id.RadioAutobus);
        str = String.valueOf(Parametri.parcheggi.get(index).getPostiLiberi()[TipoPosto.AUTOBUS]);
        rd.setText(str);
        rd = view.findViewById(R.id.RadioDisabile);
        str = String.valueOf(Parametri.parcheggi.get(index).getPostiLiberi()[TipoPosto.DISABILE]);
        rd.setText(str);
        Button btn = view.findViewById(R.id.BtnPrenota);

        // Prenoto
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Prenotazione(view);
            }
        });

        ChiediPostiLiberi();
        return view;
    }

    // Funzione per l'aggiornamento automatico dei posti liberi
    private Runnable runnable = new Runnable() {
        public void run() {
            ChiediPostiLiberi();
        }
    };

    public void Prenotazione(View view) {
        // Avverto l'utente del tentativo di invio dei dati per la prenotazione
        caricamento = ProgressDialog.show(getContext(), "Invio prenotazione",
                "Invio prenotazione in corso...", true);

        tipo_parcheggio = -1;

        RadioButton rdAuto = view.findViewById(R.id.RadioAuto);
        RadioButton rdMoto = view.findViewById(R.id.RadioMoto);
        RadioButton rdCamper = view.findViewById(R.id.RadioCamper);
        RadioButton rdAutobus = view.findViewById(R.id.RadioAutobus);
        RadioButton rdDisabile = view.findViewById(R.id.RadioDisabile);

        for (int i = 0; i < 5; i++) {
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

        if (tipo_parcheggio == -1) {
            caricamento.dismiss();
            Toast.makeText(this.getContext(), "Selezionare un tipo di parcheggio!", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject postData = new JSONObject();

        try {
            postData.put("idParcheggio", Parametri.parcheggi.get(index).getId());
            postData.put("tipoParcheggio", tipo_parcheggio);
            postData.put("token", Parametri.Token);
        } catch (Exception e) {
            Toast.makeText(this.getContext(), "Errore nell' elaborazione dei dati da inviare!", Toast.LENGTH_LONG).show();
            return;
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
                if (Parametri.prenotazioniInCorso == null)
                    Parametri.prenotazioniInCorso = new ArrayList<>();

                Prenotazione pren = null;

                try {
                    JSONObject risp = new JSONObject(result);
                    String code = risp.getString("QR_Code");
                    Date data_scadenza = stringToDate(risp.getString("scadenza"), "yyyy-MM-dd HH:mm:ss");
                    pren = new Prenotazione(data_scadenza, Parametri.parcheggi.get(index).getId(), tipo_parcheggio, code);
                } catch (Exception e) {
                    caricamento.dismiss();
                    Toast.makeText(getContext(), "Errore di risposta del server.", Toast.LENGTH_LONG).show();
                    return;
                }

                Parametri.prenotazioniInCorso.add(pren);

                caricamento.dismiss();
                Toast.makeText(getContext(), "Prenotazione effettutata con successo!", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
                return;
            }
        }

    };

    private void ChiediPostiLiberi() {
        JSONObject postData = new JSONObject();

        try {
            postData.put("id", Parametri.parcheggi.get(index).getId());
            postData.put("token", Parametri.Token);
        } catch (Exception e) {
            Toast.makeText(this.getContext(), "Errore nell' elaborazione dei dati da inviare!", Toast.LENGTH_LONG).show();
            return;
        }

        Connessione conn = new Connessione(postData, "POST");
        conn.addListener(ListenerPostiLiberi);
        conn.execute(Parametri.IP + "/getPostiLiberiParcheggio");
        handler.postDelayed(runnable, TIMER);
    }

    private ConnessioneListener ListenerPostiLiberi = new ConnessioneListener() {
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
                int[] postiLib = new int[TipoPosto.N_POSTI];

                try {
                    JSONObject posti = (new JSONObject(result).getJSONObject("postiLiberi"));

                    postiLib[TipoPosto.AUTO] = posti.getInt("nPostiMacchina");
                    postiLib[TipoPosto.AUTOBUS] = posti.getInt("nPostiAutobus");
                    postiLib[TipoPosto.CAMPER] = posti.getInt("nPostiCamper");
                    postiLib[TipoPosto.MOTO] = posti.getInt("nPostiMoto");
                    postiLib[TipoPosto.DISABILE] = posti.getInt("nPostiDisabile");
                } catch (Exception e) {
                    caricamento.dismiss();
                    Toast.makeText(getContext(), "Errore di risposta del server.", Toast.LENGTH_LONG).show();
                    return;
                }

                Parametri.parcheggi.get(index).setPostiLiberi(postiLib);

                if (caricamento != null)
                    if (caricamento.isShowing())
                        caricamento.dismiss();

                AggiornaPostiLiberi();
                return;
            }
        }

    };

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

    private void AggiornaPostiLiberi() {
        RadioButton rd = view.findViewById(R.id.RadioAuto);
        String str = String.valueOf(Parametri.parcheggi.get(index).getPostiLiberi()[TipoPosto.AUTO]);
        rd.setText(str);
        rd = view.findViewById(R.id.RadioCamper);
        str = String.valueOf(Parametri.parcheggi.get(index).getPostiLiberi()[TipoPosto.CAMPER]);
        rd.setText(str);
        rd = view.findViewById(R.id.RadioMoto);
        str = String.valueOf(Parametri.parcheggi.get(index).getPostiLiberi()[TipoPosto.MOTO]);
        rd.setText(str);
        rd = view.findViewById(R.id.RadioAutobus);
        str = String.valueOf(Parametri.parcheggi.get(index).getPostiLiberi()[TipoPosto.AUTOBUS]);
        rd.setText(str);
        rd = view.findViewById(R.id.RadioDisabile);
        str = String.valueOf(Parametri.parcheggi.get(index).getPostiLiberi()[TipoPosto.DISABILE]);
        rd.setText(str);

        //Toast.makeText(getContext(), "Posti liberi aggiornati.", Toast.LENGTH_SHORT).show();
    }

    private Date stringToDate(String data, String format) {
        Date stringDate = null;

        if (data == null)
            return null;

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
        simpledateformat.setTimeZone(tz);

        try {
            stringDate = simpledateformat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return stringDate;
    }
}
