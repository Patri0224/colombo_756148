package bookRecommender.entita;

import java.io.Serializable;

public class Librerie  implements Serializable {
    private String nome;
    private int[] idLibro;
    private int count; // Contatore per il numero di libri attualmente presenti

    // Costruttore
    public Librerie(String nome, int[] idLibro) {
        this.nome = nome;
        this.idLibro = idLibro;
        this.count = idLibro.length; // Inizializza il contatore con la lunghezza dell'array
    }

    // Metodi getter per accedere ai dati
    public String getNome() {
        return nome;
    }

    public int[] getIdLibro() {
        return idLibro;
    }

    /**
     * aggiunge un nuovo idLibro nell'array di libri se non esistente
     * @param nuovoIdLibro
     */
    public boolean aggiungiLibro(int nuovoIdLibro) {
        // Controllo se il libro è già presente
        for (int i = 0; i < count; i++) {
            if (idLibro[i] == nuovoIdLibro) {
                System.out.println("Il libro è già presente nella libreria.");
                return false; // Esci dal metodo se il libro è già presente
            }
        }

        // Crea un nuovo array con una dimensione maggiore
        int[] nuovoArray = new int[count + 1];
        // Copia gli elementi esistenti nel nuovo array
        System.arraycopy(idLibro, 0, nuovoArray, 0, count);
        idLibro = nuovoArray; // Sostituisci l'array vecchio con quello nuovo

        // Aggiungi il nuovo libro in coda
        idLibro[count] = nuovoIdLibro;
        count++; // Incrementa il contatore
        return true;
    }
}


