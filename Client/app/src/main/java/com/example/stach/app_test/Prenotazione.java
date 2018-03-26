package com.example.stach.app_test;

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
}
