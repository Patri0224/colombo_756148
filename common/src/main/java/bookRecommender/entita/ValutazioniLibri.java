package bookRecommender.entita;

/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
import bookRecommender.entita.Score;

import java.io.Serializable;

public class ValutazioniLibri  implements Serializable {
    private int id_libro;
    private int id_utente;

    private Score score = new Score(); // Array per le valutazioni (0-5)
    /*
    0: Stile
    1: Contenuto
    2: Gradevolezza
    3: Originalità
    4: Edizione
    5: Voto Finale
     */

    // Costruttore
    public ValutazioniLibri(int id_utente, int id_libro) {
        this.id_libro = id_libro;
        this.id_utente = id_utente;
    }

    public ValutazioniLibri(int id_utente, int id_libro,Score score) {
        this.id_libro = id_libro;
        this.id_utente = id_utente;
        this.score = score;

    }

    /**
     * metodo per impostare un punteggio (score) e una nota (note) per un indice specifico
     *
     * @param indice
     * @param punteggio
     * @param nota
     */
    public void setValutazione(int indice, short punteggio, String nota) {
        if (indice >= 0 && indice < score.getPunteggi().length) {
            if (punteggio >= 0 && punteggio <= 5) { // Controllo che il punteggio sia valido
                score.setP(indice, punteggio);
                score.setN(indice, nota);
            } else {
                System.out.println("Punteggio deve essere tra 0 e 5.");
            }
        } else {
            System.out.println("Indice non valido. Deve essere tra 0 e " + (score.getPunteggi().length - 1) + ".");
        }
    }

    /**
     * Metodo per ottenere il punteggio per un indice specifico
     *
     * @param indice
     * @return
     */
    public short getPunteggio(int indice) {
        if (indice >= 0 && indice < score.getPunteggi().length) {
            return score.getP(indice);
        }
        System.out.println("Indice non valido.");
        return -1; // Restituiamo un valore non valido
    }

    public short[] getPunteggi(){
        return score.getPunteggi();
    }

    /**
     * Metodo per ottenere la nota per un indice specifico
     * @param indice
     * @return
     */
    public String getNota(int indice) {
        if (indice >= 0 && indice < score.getNote().length) {
            return score.getN(indice);
        }
        System.out.println("Indice non valido.");
        return null; // Restituiamo null se non valido
    }

    public String[] getNote(){
        return score.getNote();
    }

    public Score getScore(){
        return score;
    }

    // Metodi getter per id_libro e id_utente
    public int getIdLibro() {
        return id_libro;
    }

    public int getIdUtente() {
        return id_utente;
    }
    @Override
    public String toString() {
        String[] categorie = {
                "Stile", "Contenuto", "Gradevolezza", "Originalità", "Edizione", "Voto Finale"
        };

        StringBuilder sb = new StringBuilder();
        sb.append("Valutazioni per il libro ID: ").append(id_libro).append("\n");

        short[] punteggi = score.getPunteggi();
        String[] note = score.getNote();

        for (int i = 0; i < categorie.length; i++) {
            sb.append(String.format("%-15s: %d", categorie[i], punteggi[i]));
            if (note[i] != null && !note[i].isEmpty()) {
                sb.append(" | Nota: ").append(note[i]);
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}

