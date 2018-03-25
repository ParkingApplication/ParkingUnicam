package com.example.stach.app_test;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONObject;

public class Parcheggio {
    private int id;
    private String indirizzo;
    private String indirizzo_format;
    private LatLng coordinate;

    private int[] postiLiberi;

    // Prezzi orari standard del parcheggio
    private double prezzoFestivi;
    private double prezzoLavorativi;

    // Costruttore parcheggio
    public Parcheggio(int id, String indirizzo, LatLng coordinate, int[] postiTotali, int[] postiLiberi, double prezzoFestivi, double prezzoLavorativi) {

        this.postiLiberi = new int[TipoPosto.N_POSTI];

        if (postiTotali.length != TipoPosto.N_POSTI || postiLiberi.length != TipoPosto.N_POSTI)
            throw new Error("Parametri errati");

        this.id = id;
        this.indirizzo = indirizzo;
        this.coordinate = coordinate;

        for (int i = 0; i < TipoPosto.N_POSTI; i++) {

            this.postiLiberi[i] = postiLiberi[i];
        }

        this.prezzoFestivi = prezzoFestivi;
        this.prezzoLavorativi = prezzoLavorativi;
    }

    // Costruttore parcheggio senza posti liberi
    public Parcheggio(int id, String indirizzo, LatLng coordinate, int[] postiTotali, double prezzoFestivi, double prezzoLavorativi) {

        this.postiLiberi = new int[TipoPosto.N_POSTI];

        if (postiTotali.length != TipoPosto.N_POSTI || postiLiberi.length != TipoPosto.N_POSTI)
            throw new Error("Parametri errati");

        this.id = id;
        this.indirizzo = indirizzo;
        this.coordinate = coordinate;

        for (int i = 0; i < TipoPosto.N_POSTI; i++) {

            this.postiLiberi[i] = -1;
        }

        this.prezzoFestivi = prezzoFestivi;
        this.prezzoLavorativi = prezzoLavorativi;
    }

    // Costruttore parcheggio senza posti liberi da stringa formattata in JSon
    public Parcheggio(String JSonobj)throws Exception {

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

        this.postiLiberi[TipoPosto.AUTO] = jobj.getInt("nPostiMacchina");
        this.postiLiberi[TipoPosto.AUTOBUS] = jobj.getInt("nPostiAutobus");
        this.postiLiberi[TipoPosto.CAMPER] = jobj.getInt("nPostiCamper");
        this.postiLiberi[TipoPosto.MOTO] = jobj.getInt("nPostiMoto");
        this.postiLiberi[TipoPosto.DISABILE] = jobj.getInt("nPostiDisabile");

        //Setto i parcheggi per farli vedere più sexy
        this.indirizzo_format = "Città: " + indr.getString("citta") + "\n" +"Provincia: "+ indr.getString("provincia")
                + "\n" + "Via: "+ indr.getString("via") + "\n" + "CAP: " + indr.getString("cap") ;

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

    public String getIndirizzo_format(){return indirizzo_format;}
}
