package bookRecommender.entita;

import java.io.Serializable;
import java.util.ArrayList;

public class Librerie implements Serializable {
    private int idLibreria;
    private String nome;
    private ArrayList<Integer> idLibro;// Contatore per il numero di libri attualmente presenti

    // Costruttore
    public Librerie(int idLibreria, String nome, ArrayList<Integer> idLibro) {
        this.idLibreria = idLibreria;
        this.nome = nome;
        this.idLibro = idLibro; // Inizializza il contatore con la lunghezza dell'array
    }

    // Metodi getter per accedere ai dati
    public String getNome() {
        return nome;
    }

    public int getIdLibreria() {
        return idLibreria;
    }

    public int[] getIdLibro() {
        return idLibro.stream().mapToInt(i -> i).toArray();
    }

    /**
     * aggiunge un nuovo idLibro nell'arraylist di libri se non esistente
     *
     * @param nuovoIdLibro
     */
    public boolean aggiungiLibroInLocale(int nuovoIdLibro) {
        // Controllo se il libro è già presente
        if (idLibro.contains(nuovoIdLibro)) return false;
        idLibro.add(nuovoIdLibro);
        return true;
    }
}


