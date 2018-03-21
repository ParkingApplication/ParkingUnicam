package com.example.stach.app_test;


public class Parcheggi {
    private String id;
    private String nome;
    private String indirizzo;
    private String posti;
    private static int count;

    public Parcheggi() {
        this.nome = "davack";
        this.id = String.valueOf(count++);
        this.indirizzo = "via d'avackaldo";
        this.posti = String.valueOf(count * 4);
    }

    public String getNome(){
        return this.nome;
    }
    public String getId(){
        return this.id;
    }
    public String getIndirizzo(){
        return this.indirizzo;
    }
    public String getPosti(){
        return this.posti;
    }
}
