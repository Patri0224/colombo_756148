/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
package bookRecommender.entita;

import java.io.Serializable;

public class ConsigliLibri implements Serializable {
    private final int id_libro;
    private final int id_utente;
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
     * @return array di libri consigliati
     */
    public int[] getConsigliLibri() {
        return consigli_libri;
    }
    /**
     * Restituisce true se l'array di libri consigliati è vuoto (contiene solo -1
     *
     * @return true/false
     */
    public boolean isEmpty() {
        for (int i : consigli_libri) {
            if (i != -1) {
                return false;
            }
        }
        return true;
    }
}

