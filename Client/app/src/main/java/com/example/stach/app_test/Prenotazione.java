package com.example.stach.app_test;


import java.util.Date;

public class Prenotazione {
    private final String intro = "Parcheggio in ";
    private Date data;
    private String nomeParcheggio;

    public Prenotazione() {
        this.data = new Date();
        this.nomeParcheggio = "Via Francesco Sforza,MC,62100";
    }

    public String returnValue() {
        return this.intro + this.nomeParcheggio + this.data.getTime();
    }

}
