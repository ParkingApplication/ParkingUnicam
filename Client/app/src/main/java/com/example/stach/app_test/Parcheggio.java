package com.example.stach.app_test;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONObject;

public class Parcheggio {
    private int id;
    private String indirizzo;
    private LatLng coordinate;
    private int[] postiTotali;
    private int[] postiLiberi;

    // Prezzi orari standard del parcheggio
    private double prezzoFestivi;
    private double prezzoLavorativi;

    // Costruttore parcheggio
    public Parcheggio(int id, String indirizzo, LatLng coordinate, int[] postiTotali, int[] postiLiberi, double prezzoFestivi, double prezzoLavorativi) {
        this.postiTotali = new int[TipoPosto.N_POSTI];
        this.postiLiberi = new int[TipoPosto.N_POSTI];

        if (postiTotali.length != TipoPosto.N_POSTI || postiLiberi.length != TipoPosto.N_POSTI)
            throw new Error("Parametri errati");

        this.id = id;
        this.indirizzo = indirizzo;
        this.coordinate = coordinate;

        for (int i = 0; i < TipoPosto.N_POSTI; i++) {
            this.postiTotali[i] = postiTotali[i];
            this.postiLiberi[i] = postiLiberi[i];
        }

        this.prezzoFestivi = prezzoFestivi;
        this.prezzoLavorativi = prezzoLavorativi;
    }

    // Costruttore parcheggio senza posti liberi
    public Parcheggio(int id, String indirizzo, LatLng coordinate, int[] postiTotali, double prezzoFestivi, double prezzoLavorativi) {
        this.postiTotali = new int[TipoPosto.N_POSTI];
        this.postiLiberi = new int[TipoPosto.N_POSTI];

        if (postiTotali.length != TipoPosto.N_POSTI || postiLiberi.length != TipoPosto.N_POSTI)
            throw new Error("Parametri errati");

        this.id = id;
        this.indirizzo = indirizzo;
        this.coordinate = coordinate;

        for (int i = 0; i < TipoPosto.N_POSTI; i++) {
            this.postiTotali[i] = postiTotali[i];
            this.postiLiberi[i] = -1;
        }

        this.prezzoFestivi = prezzoFestivi;
        this.prezzoLavorativi = prezzoLavorativi;
    }

    // Costruttore parcheggio senza posti liberi da stringa formattata in JSon
    public Parcheggio(String JSonobj)throws Exception {
        this.postiTotali = new int[TipoPosto.N_POSTI];
        this.postiLiberi = new int[TipoPosto.N_POSTI];

        // Estraggo tutti i parcheggi
        JSONObject jobj = new JSONObject(JSonobj);
        this.id = jobj.getInt("id");
        JSONObject indr = jobj.getJSONObject("indirizzo");
        this.indirizzo = indr.getString("via") + ", " + indr.getString("n_civico")
                + ", " + indr.getString("cap") + ", " + indr.getString("citta") + " "
                + indr.getString("provincia");
        JSONObject coord = jobj.getJSONObject("coordinate");
        double x = coord.getDouble("x");
        double y = coord.getDouble("y");
        this.prezzoLavorativi = jobj.getDouble("tariffaOrariaLavorativi");
        this.prezzoFestivi = jobj.getDouble("tariffaOrariaFestivi");
        this.coordinate = new LatLng(x, y);
        int[] posti = new int[TipoPosto.N_POSTI];

        this.postiTotali[TipoPosto.AUTO] = jobj.getInt("nPostiMacchina");
        this.postiTotali[TipoPosto.AUTOBUS] = jobj.getInt("nPostiAutobus");
        this.postiTotali[TipoPosto.CAMPER] = jobj.getInt("nPostiCamper");
        this.postiTotali[TipoPosto.MOTO] = jobj.getInt("nPostiMoto");
        this.postiTotali[TipoPosto.DISABILE] = jobj.getInt("nPostiDisabile");

    }

    public int getId() {
        return id;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public LatLng getCoordinate() {
        return coordinate;
    }

    public int[] getPostiTotali() {
        return postiTotali;
    }

    public int[] getPostiLiberi() {
        return postiLiberi;
    }

    public void setPostiLiberi(int[] postiLiberi) {
        for (int i = 0; i < TipoPosto.N_POSTI; i++) {
            this.postiLiberi[i] = postiLiberi[i];
        }
    }

    public double getPrezzoFestivi() {
        return prezzoFestivi;
    }

    public double getPrezzoLavorativi() {
        return prezzoLavorativi;
    }
}
