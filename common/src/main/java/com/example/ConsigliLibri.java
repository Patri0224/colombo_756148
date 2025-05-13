package com.example;

import java.io.Serializable;

public class ConsigliLibri  implements Serializable {
    private int id_libro;
    private int id_utente;
    private int[] consigli_libri = new int[3];

    // Costruttore
    public ConsigliLibri(int id_libro, int id_utente, int libro1, int libro2, int libro3) {
        this.id_libro = id_libro;
        this.id_utente = id_utente;
        this.consigli_libri[0] = libro1; // Associazione del primo libro
        this.consigli_libri[1] = libro2; // Associazione del secondo libro
        this.consigli_libri[2] = libro3; // Associazione del terzo libro
    }

    // Metodi getter per accedere ai dati
    public int getIdLibro() {
        return id_libro;
    }

    public int getIdUtente() {
        return id_utente;
    }

    public int[] getConsigliLibri() {
        return consigli_libri;
    }
}

