package com.example.stach.app_test;


import java.util.Date;

public class Prenotazione {
    static int count;
    private final String intro = "Parcheggio: ";
    private Date data;
    private String nomeParcheggio;
    private String dataStringa;

    public Prenotazione() {
        this.data = new Date();
        this.nomeParcheggio = count + "Via Francesco Sforza, MC, 62100";
        this.dataStringa = "Data:" + this.data.toString();
        count++;
    }

    //metto private perchè devo vedere se lo voglio utilizzare
    public String returnValue() {
        return this.intro + this.nomeParcheggio + "\n \n" + this.dataStringa;
    }

    public String getData() {
        return data.toString();
    }

    public String getNomeParcheggio() {
        return nomeParcheggio;
    }
}
