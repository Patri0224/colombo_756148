/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
package bookRecommender;

import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.Libri;
import bookRecommender.rmi.ServerBookRecommenderInterface;
import graphics.PopupError;

import java.rmi.RemoteException;

public class LibriRicercaGestore {
    private static LibriRicercaGestore instance = null;
    private final ServerBookRecommenderInterface stub;
    private Libri[] libri = new Libri[0];

    private LibriRicercaGestore(ServerBookRecommenderInterface stub) {
        this.stub = stub;
    }

    public static LibriRicercaGestore CreateInstance(ServerBookRecommenderInterface stub) {
        if (instance == null) {
            instance = new LibriRicercaGestore(stub);
        }
        return instance;
    }

    public static LibriRicercaGestore GetInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    public Eccezione RicercaLibri(String titolo, String autore, int annoPubblicazione) {
        try {
            libri = stub.RicercaLibri(titolo, autore, annoPubblicazione);
            return new Eccezione(0, "Ricerca completata con successo");
        } catch (Exception e) {
            return new Eccezione(1, "Errore durante la ricerca: " + e.getMessage());
        }
    }

    public Eccezione RicercaLibri(String titolo, String autore, int annoPubblicazione, String editore, String categoria, float prezzoMin, float prezzoMax) {
        try {
            libri = stub.RicercaLibri(titolo, autore, annoPubblicazione, editore, categoria, prezzoMin, prezzoMax);
            return new Eccezione(0, "Ricerca completata con successo");
        } catch (Exception e) {
            return new Eccezione(1, "Errore durante la ricerca: " + e.getMessage());
        }
    }

    public Libri[] GetLibri() {
        return libri;
    }

    public int GetNumeroLibri() {
        return libri.length;
    }

    public Libri CercaLibro(int idLibro) {
        for (Libri libro : libri) {
            if (libro != null && libro.getId() == idLibro) {
                return libro;
            }
        }
        try {
            Libri[] lib = stub.RicercaLibriDaIds(new int[]{idLibro});
            if (lib == null)
                return null;
            if (lib.length == 1)
                return lib[0];
            else
                return null;
        } catch (RemoteException e) {
            PopupError.mostraErrore(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Libri[] CercaLibriMultipli(int[] idLibri) {
        int i = 0;
        Libri[] libris = new Libri[idLibri.length];
        for (int idLibro : idLibri) {
            for (Libri libro : libri) {
                if (libro != null && libro.getId() == idLibro) {
                    libris[i] = libro;
                    i++;
                }
            }
        }
        if (i == idLibri.length) {
            return libris;
        }
        try {
            Libri[] libs = stub.RicercaLibriDaIds(idLibri);
            return libs;//se non trova nessuno ritorna un array vuoto (creato dallo stub)
        } catch (RemoteException e) {
            PopupError.mostraErrore(e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
