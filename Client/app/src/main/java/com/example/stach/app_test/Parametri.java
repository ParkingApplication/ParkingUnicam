package com.example.stach.app_test;

import android.os.Handler;

import java.io.File;
import java.util.List;

public class Parametri {
    // Dati server e connessione
    static String IP = "http://2.226.207.189:5666";
    static String Token = null;
    static File login_file;

    // Dati account utente
    static String id = null;
    static String username = null;
    static String email = null;
    static String password = null;
    static String nome = null;
    static String cognome = null;
    static String data_nascita = null;
    static String telefono = null;
    static String saldo = null;

    // Dati carta di credito
    static String numero_carta = null;
    static String data_di_scadenza = null;
    static String pin = null;

    // Parcheggi e prenotazioni
    static List<Parcheggio> parcheggi = null;
    static List<Parcheggio> parcheggi_vicini = null;
    static List<Prenotazione> prenotazioniInCorso = null;
}
