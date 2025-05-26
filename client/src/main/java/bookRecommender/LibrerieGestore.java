/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
package bookRecommender;

import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.Librerie;
import bookRecommender.entita.Libri;
import bookRecommender.rmi.ServerBookRecommenderInterface;
import graphics.PopupError;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Gestore delle librerie, permette di gestire le librerie dell'utente
 * e di interagire con il server per aggiungere, rimuovere e cercare librerie e libri in esse.
 */
public class LibrerieGestore {
    private static LibrerieGestore instance = null;
    private final ServerBookRecommenderInterface stub;
    private Librerie[] librerie = new Librerie[0];
    private final UtenteGestore utenteGestore;

    /**
     * Costruttore privato per il singleton
     *
     * @param stub interfaccia remota del server per le operazioni sulle librerie
     */
    private LibrerieGestore(ServerBookRecommenderInterface stub) {
        this.stub = stub;

        this.utenteGestore = UtenteGestore.GetInstance();
        if (utenteGestore.UtenteLoggato()) {
            try {
                librerie = stub.RicercaLibrerie(utenteGestore.GetIdUtente());
            } catch (RemoteException e) {
                librerie = new Librerie[0];
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Crea un'istanza di LibrerieGestore se non esiste già,
     *
     * @param stub interfaccia remota del server per le operazioni sulle librerie
     * @return l'istanza di LibrerieGestore
     */
    public static LibrerieGestore CreateInstance(ServerBookRecommenderInterface stub) {
        if (instance == null) {
            instance = new LibrerieGestore(stub);
        }
        return instance;
    }

    /**
     * Restituisce l'istanza di LibrerieGestore se esiste,
     *
     * @return l'istanza di LibrerieGestore o null se non esiste
     */
    public static LibrerieGestore GetInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    /**
     * Carica le librerie dell'utente loggato dal server.
     *
     * @return Eccezione che indica il risultato dell'operazione:
     */
    public Eccezione caricaLibrerie() {
        if (utenteGestore.UtenteLoggato()) {
            try {
                librerie = stub.RicercaLibrerie(utenteGestore.GetIdUtente());
                return new Eccezione(0, "Caricamento librerie avvenuto con successo");
            } catch (RemoteException e) {
                librerie = new Librerie[0];
                return new Eccezione(2, "Errore durante il caricamento delle librerie" + e.getMessage());
            }
        } else {
            return new Eccezione(1, "Utente non Loggato");
        }
    }

    /**
     * Restituisce l'array di librerie dell'utente loggato salvate in memoria.
     *
     * @return array di Librerie
     */
    public Librerie[] GetLibrerie() {
        if (librerie.length == 0) {
            Eccezione e = caricaLibrerie();
            if (e.getErrorCode() > 0) PopupError.mostraErrore(e.getMessage());
        }
        return librerie;
    }

    /**
     * Restituisce il numero di librerie dell'utente loggato.
     *
     * @return numero di librerie
     */
    public int GetNumeroLibrerie() {
        if (librerie.length == 0) {
            Eccezione e = caricaLibrerie();
            if (e.getErrorCode() > 0) PopupError.mostraErrore(e.getMessage());
        }
        return librerie.length;
    }

    /**
     * Restituisce una libreria specifica in base al nome.
     *
     * @param nomeLibreria nome della libreria da cercare
     * @return Librerie oggetto della libreria trovata, o null se non esiste
     */
    public Librerie GetLibreria(String nomeLibreria) {
        for (Librerie value : librerie) {
            if (value.getNome().equals(nomeLibreria)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Aggiunge una nuova libreria all'utente loggato e lo comunica al server.
     *
     * @param nomeLibreria nome della libreria da aggiungere
     * @return Eccezione che indica il risultato dell'operazione:
     */
    public Eccezione AggiungiLibreria(String nomeLibreria) {
        if (utenteGestore.UtenteLoggato()) {
            try {
                Eccezione ecc = stub.AggiungiLibreria(utenteGestore.GetIdUtente(), nomeLibreria);
                if (ecc.getErrorCode() == 0) {
                    caricaLibrerie();
                }
                return ecc;
            } catch (RemoteException e) {
                return new Eccezione(2, "Errore durante l'aggiunta della libreria" + e.getMessage());
            }
        } else {
            return new Eccezione(1, "Utente non Loggato");
        }
    }

    /**
     * Rimuove una libreria specifica dell'utente loggato e lo comunica al server.
     *
     * @param idLibreria id della libreria da rimuovere
     * @return Eccezione che indica il risultato dell'operazione:
     */
    public Eccezione RimuoviLibreria(int idLibreria) {
        if (utenteGestore.UtenteLoggato()) {
            try {
                Eccezione ecc = stub.RimuoviLibreria(idLibreria);
                if (ecc.getErrorCode() == 0) {
                    caricaLibrerie();
                    return ecc;
                } else {
                    return ecc;
                }
            } catch (RemoteException e) {
                return new Eccezione(2, "Errore durante la rimozione della libreria" + e.getMessage());
            }
        } else {
            return new Eccezione(1, "Utente non Loggato");
        }
    }

    /**
     * Aggiunge un libro a una libreria specifica dell'utente loggato e lo comunica al server.
     *
     * @param idLibreria id della libreria a cui aggiungere il libro
     * @param idLibro    id del libro da aggiungere
     * @return Eccezione che indica il risultato dell'operazione:
     */
    public Eccezione AggiungiLibroALibreria(int idLibreria, int idLibro) {
        if (utenteGestore.UtenteLoggato()) {
            try {
                Eccezione ecc = stub.AggiungiLibroALibreria(idLibreria, idLibro);
                if (ecc.getErrorCode() == 0) {
                    caricaLibrerie();
                    return ecc;
                } else {
                    return ecc;
                }
            } catch (RemoteException e) {
                return new Eccezione(2, "Errore durante l'aggiunta del libro alla libreria" + e.getMessage());
            }
        } else {
            return new Eccezione(1, "Utente non Loggato");
        }
    }

    /**
     * Aggiunge un libro a una libreria specifica dell'utente loggato
     *
     * @param nomeLibreria nome della libreria a cui aggiungere il libro
     * @param idLibro      id del libro da aggiungere
     * @return Eccezione che indica il risultato dell'operazione:
     */
    public Eccezione AggiungiLibroALibreria(String nomeLibreria, int idLibro) {
        Librerie libreria = GetLibreria(nomeLibreria);
        if (libreria != null) {
            return AggiungiLibroALibreria(libreria.getIdLibreria(), idLibro);
        } else {
            return new Eccezione(1, "Libreria non trovata");
        }

    }

    /**
     * Restituisce un array di libri presenti in una libreria specifica dell'utente loggato.
     *
     * @param nomeLibreria nome della libreria da cui ottenere i libri
     * @return array di Libri presenti nella libreria, o un array vuoto se la libreria non esiste
     */
    public Libri[] GetLibriDaLibreria(String nomeLibreria) {
        Librerie libreria = GetLibreria(nomeLibreria);
        LibriRicercaGestore libriRicercaGestore = LibriRicercaGestore.GetInstance();
        if (libreria != null) {
            int[] idLibri = libreria.getIdLibri();
            return libriRicercaGestore.CercaLibriMultipli(idLibri);
        } else {
            return new Libri[0];
        }
    }

    /**
     * Rimuove un libro da una libreria specifica dell'utente loggato e lo comunica al server.
     *
     * @param idLibreria id della libreria da cui rimuovere il libro
     * @param idLibro    id del libro da rimuovere
     * @return Eccezione che indica il risultato dell'operazione:
     */
    public Eccezione RimuoviLibroDaLibreria(int idLibreria, int idLibro) {
        if (utenteGestore.UtenteLoggato()) {
            try {
                Eccezione ecc = stub.RimuoviLibroDaLibreria(idLibreria, idLibro);
                if (ecc.getErrorCode() == 0) {
                    caricaLibrerie();
                    return ecc;
                } else {
                    return ecc;
                }
            } catch (RemoteException e) {
                return new Eccezione(2, "Errore durante la rimozione del libro dalla libreria" + e.getMessage());
            }
        } else {
            return new Eccezione(1, "Utente non Loggato");
        }
    }

    /**
     * Rimuove un libro da una libreria specifica dell'utente loggato
     *
     * @param nomeLibreria nome della libreria da cui rimuovere il libro
     * @param idLibro      id del libro da rimuovere
     * @return Eccezione che indica il risultato dell'operazione:
     */
    public Eccezione RimuoviLibroDaLibreria(String nomeLibreria, int idLibro) {
        Librerie libreria = GetLibreria(nomeLibreria);
        if (libreria != null) {
            return RimuoviLibroDaLibreria(libreria.getIdLibreria(), idLibro);
        } else {
            return new Eccezione(3, "Libreria non trovata");
        }
    }

    /**
     * Controlla se una libreria esiste
     *
     * @param nomeLibreria nome della libreria da controllare
     * @return false se la libreria non esiste, true altrimenti
     */
    public boolean ControlloEsisteLibreria(String nomeLibreria) {
        if (utenteGestore.UtenteLoggato()) {
            try {
                return stub.ControlloEsisteLibreria(utenteGestore.GetIdUtente(), nomeLibreria);
            } catch (RemoteException e) {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Controlla se un libro è presente in una libreria
     *
     * @param idLibro id del libro da controllare
     * @return false se il libro non è presente in una libreria, true altrimenti
     */
    public boolean ControlloLibroInLibrerie(int idLibro) {
        if (utenteGestore.UtenteLoggato()) {
            try {
                return stub.ControlloLibroInLibrerie(utenteGestore.GetIdUtente(), idLibro);
            } catch (RemoteException e) {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Cerca libri in tutte le librerie dell'utente loggato con possibilità di filtrare per titolo, autore e anno.
     *
     * @param titoloRicerca titolo del libro da cercare
     * @param autoreRicerca autore del libro da cercare
     * @param annoR         anno del libro da cercare, -1 se non si vuole filtrare per anno
     * @return array di Libri trovati in tutte le librerie dell'utente loggato
     * @throws RuntimeException se si verifica un errore durante la comunicazione con il server
     */
    public Libri[] GetLibriDaTutteLibrerie(String titoloRicerca, String autoreRicerca, int annoR) throws RuntimeException {
        if (utenteGestore.UtenteLoggato()) {
            try {
                return stub.RicercaLibriDaLibrerie(utenteGestore.GetIdUtente(), titoloRicerca, autoreRicerca, annoR);
            } catch (RemoteException e) {
                throw new RuntimeException("Errore durante la ricerca dei libri nelle librerie: " + e.getMessage());
            }
        }
        return new Libri[0];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LibrerieGestore{");
        sb.append("librerie=[");
        for (Librerie libreria : librerie) {
            sb.append(libreria.toString()).append(", ");
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * Restituisce un array di nomi delle librerie dell'utente loggato.
     *
     * @return array di String contenente i nomi delle librerie
     */
    public String[] getNomeLibrerie() {
        ArrayList<String> lista = new ArrayList<>();
        for (Librerie l : librerie) {
            lista.add(l.getNome());
        }
        return lista.toArray(new String[0]);
    }
}
