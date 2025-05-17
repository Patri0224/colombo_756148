package bookRecommender.entita;

import java.io.Serializable;

public class ConsigliLibri implements Serializable {
    private int id_libro;
    private int id_utente;
    private int[] consigli_libri = new int[]{-1, -1, -1}; // Inizializza un array di 3 elementi a 0

    // Costruttore
    public ConsigliLibri(int id_libro, int id_utente) {
        this.id_libro = id_libro;
        this.id_utente = id_utente;
    }

    public ConsigliLibri(int id_libro, int id_utente, int libro1, int libro2, int libro3) {
        this.id_libro = id_libro;
        this.id_utente = id_utente;
        this.consigli_libri[0] = libro1; // Associazione del primo libro
        this.consigli_libri[1] = libro2; // Associazione del secondo libro
        this.consigli_libri[2] = libro3; // Associazione del terzo libro
    }

    // Metodi getter per accedere ai dati
    public void aggiungiConsiglio(int libro) {
        for (int i = 0; i < consigli_libri.length; i++) {
            if (consigli_libri[i] == -1) {
                consigli_libri[i] = libro;
                break;
            }
        }
    }

    public int getIdLibro() {
        return id_libro;
    }

    public int getIdUtente() {
        return id_utente;
    }

    /**
     * Restituisce i libri consigliati
     * id validi solo se diversi da -1
     *
     * @return
     */
    public int[] getConsigliLibri() {
        return consigli_libri;
    }
}

