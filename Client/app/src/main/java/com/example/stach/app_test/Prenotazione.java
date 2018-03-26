package com.example.stach.app_test;

import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Prenotazione {
    private Date scadenza;
    private int idParcheggio;
    private int idTipo;
    private String codice;

    public Prenotazione(Date scadenza, int idParcheggio, int idTipo, String codice) throws Exception {
        if (scadenza == null)
            throw new Exception("Data scadenza non può essere null.");

        this.scadenza = scadenza;
        this.idParcheggio = idParcheggio;
        this.idTipo = idTipo;
        this.codice = codice;
    }

    public Prenotazione(String prenotazione) throws Exception {
        JSONObject jobj = new JSONObject(prenotazione);

        this.idParcheggio = jobj.getInt("idParcheggio");
        this.idTipo = jobj.getInt("idPosto");
        this.scadenza = stringToDate(jobj.getString("data"), "yyyy-MM-dd HH:mm:ss");
        if (scadenza == null)
            throw new Exception("Formato data scadenza prenotazione errato.");

        this.codice = jobj.getString("codice");
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
