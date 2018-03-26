package com.example.stach.app_test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Prenotazione {
    private Date scadenza;
    private int idParcheggio;
    private int idTipo;
    private String codice;

    public Prenotazione(Date scadenza, int idParcheggio, int idTipo, String codice) {
        this.scadenza = scadenza;
        this.idParcheggio = idParcheggio;
        this.idTipo = idTipo;
        this.codice = codice;
    }
    public Prenotazione(JSONArray prenotazioni) {
        Parametri.prenotazioniInCorso = new ArrayList<>();
    for (int i = 0; i < prenotazioni.length();i++) {
        try {
            //Estraggo gli oggeti Json contenuti nel Json array
            JSONObject parametro = prenotazioni.getJSONObject(i);
            int idParcheggio = parametro.getInt("idParcheggio");
            int idPosto = parametro.getInt("idPosto");
            Date data =  stringToDate(parametro.getString("data"), "yyyy-MM-dd HH-mm-ss");
            String codice = parametro.getString("codice");
            this.idParcheggio = idParcheggio;
            this.scadenza = data;
            this.idTipo = idPosto;
            this.codice = codice;
            Parametri.prenotazioniInCorso.add(this);
        }catch(JSONException js){

        }

    }

    }

    public Date getScadenza() {
        return scadenza;
    }

    public int getIdParcheggio() {
        return idParcheggio;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public String getCodice() {
        return codice;
    }

    public long getTempoScadenza() {
        Date now = new Date();
        return scadenza.getTime() - now.getTime();
    }
    private Date stringToDate(String data, String format) {
        if (data == null)
            return null;

        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
        Date stringDate = simpledateformat.parse(data, pos);

        return stringDate;
    }
}
