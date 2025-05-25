package bookRecommender;

import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.ConsigliLibri;
import bookRecommender.entita.Librerie;
import bookRecommender.entita.Libri;
import bookRecommender.entita.ValutazioniLibri;
import bookRecommender.rmi.ServerBookRecommenderInterface;
import db.QueryList;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class ImpServer implements ServerBookRecommenderInterface, Serializable {

    private final QueryList q;

    public ImpServer(QueryList query) {
        this.q = query;
    }


    /**
     * Login per l'utente tramite id e password
     *
     * @param idUtente id dell'utente dato dal database
     * @param password password dell'utente già criptata
     * @return Eccezione con:1 se l'utente non esiste, 2 se la password è errata, 3 errore interno, 0 con messaggio id dell'utente
     * @throws RemoteException altre eccezioni
     */
    @Override
    public Eccezione login(int idUtente, String password) throws RemoteException {
        try {
            q.Connect();
            Eccezione ec = q.ControlloPasswordUtente(idUtente, password);
            q.Disconnect();
            return ec;
        } catch (SQLException e) {
            q.Disconnect();
            return new Eccezione(10, "Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Login per l'utente tramite email e password
     *
     * @param email    email dell'utente
     * @param password password dell'utente già criptata
     * @return Eccezione con:1 se l'utente non esiste, 2 se la password è errata, 3 errore interno, 0 con messaggio id dell'utente
     * @throws RemoteException altre eccezioni
     */
    @Override
    public Eccezione login(String email, String password) throws RemoteException {
        try {
            q.Connect();
            Eccezione ec = q.ControlloPasswordUtente(email, password);
            q.Disconnect();
            return ec;
        } catch (SQLException e) {
            q.Disconnect();
            return new Eccezione(10, "Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * da email a idUtente
     *
     * @param email email dell'utente
     * @return l'id dell'utente se il esiste, -1 altrimenti
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public int getIdUtente(String email) throws RemoteException {
        try {
            q.Connect();
            int id = q.GetIdUtenteDaEmail(email);
            q.Disconnect();
            return id;
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Controlla se l'utente esiste nel database dato il suo id
     *
     * @param idUtente id dell'utente
     * @return vero se l'utente esiste, falso altrimenti
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public boolean ControlloEsisteUtente(int idUtente) throws RemoteException {
        try {
            q.Connect();
            boolean b = q.ControlloEsisteUtente(idUtente);
            q.Disconnect();
            return b;
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Restituisce i dati dell'utente dato il suo id
     *
     * @param idUtente id dell'utente
     * @return un array di stringhe con i dati dell'utente [0] email, [1] nome, [2] cognome, [3] codice fiscale
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public String[] GetUtenteRegistrato(int idUtente) throws RemoteException {
        try {
            q.Connect();
            String[] utente = q.GetUtenteRegistrato(idUtente);
            q.Disconnect();
            return utente;
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Controlla se l'utente esiste nel database dato la sua email
     *
     * @param email email dell'utente
     * @return vero se l'utente esiste, falso altrimenti
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public boolean ControlloEsisteUtente(String email) throws RemoteException {
        try {
            q.Connect();
            boolean b = q.ControlloEsisteUtente(email);
            q.Disconnect();
            return b;
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Crea un nuovo utente nel database
     *
     * @param email         email dell'utente max 100 caratteri
     * @param password      password dell'utente max 200 caratteri
     * @param nome          nome dell'utente max 100 caratteri
     * @param cognome       cognome dell'utente max 100 caratteri
     * @param codiceFiscale codice fiscale dell'utente max 16 caratteri
     * @return Eccezione con:1 se l'utente già esiste, 2, 3, 4 errori SQL, ecc 0 con messaggio l'id dell'utente
     * @throws RemoteException altre eccezioni
     */
    @Override
    public Eccezione Registrazione(String email, String password, String nome, String cognome, String codiceFiscale) throws RemoteException {
        try {
            q.Connect();
            Eccezione ec = q.Registrazione(email, password, nome, cognome, codiceFiscale);
            q.Disconnect();
            return ec;
        } catch (SQLException e) {
            q.Disconnect();
            return new Eccezione(10, "Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Modifica la password dell'utente
     *
     * @param idUtente id dell'utente
     * @param password nuova password
     * @return Eccezione con:1 se l'utente non esiste, 2, 3 errori SQL, 0 tutto ok
     * @throws RemoteException altre eccezioni
     */
    @Override
    public Eccezione ModificaPassword(int idUtente, String password) throws RemoteException {
        try {
            q.Connect();
            Eccezione ec = q.ModificaPassword(idUtente, password);
            q.Disconnect();
            return ec;
        } catch (SQLException e) {
            q.Disconnect();
            return new Eccezione(10, "Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Rimuove un utente dal database
     *
     * @param idUtente id dell'utente
     * @return Eccezione con:1 se l'utente non esiste, 2, 3 errori SQL, 0 se tutto ok
     * @throws RemoteException altre eccezioni
     */
    @Override
    public Eccezione RimuoviUtente(int idUtente) throws RemoteException {
        try {
            q.Connect();
            Eccezione ec = q.RimuoviUtente(idUtente);
            q.Disconnect();
            return ec;
        } catch (SQLException e) {
            q.Disconnect();
            return new Eccezione(10, "Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Restituisce l'array libri presenti nel database che contengono i campi di ricerca
     *
     * @param titolo stringa per cercare nel titolo, può essere null
     * @param autore stringa per cercare nell'autore, può essere null
     * @param anno   anno di pubblicazione, -1 se non specificato
     * @return un array di Libri
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public Libri[] RicercaLibri(String titolo, String autore, int anno) throws RemoteException {
        try {
            q.Connect();
            Libri[] libri = q.RicercaLibri(titolo, autore, anno);
            q.Disconnect();
            return libri;
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Restituisce l'array libri presenti nel database che contengono i campi di ricerca
     *
     * @param titolo    stringa per cercare nel titolo, può essere null
     * @param autore    stringa per cercare nell'autore, può essere null
     * @param anno      anno di pubblicazione, -1 se non specificato
     * @param editore   stringa per cercare nell'editore, può essere null
     * @param categoria stringa per cercare nella categoria, può essere null
     * @param prezzoMin prezzo minimo del libro, -1 se non specificato
     * @param prezzoMax prezzo massimo del libro, -1 se non specificato
     * @return un array di Libri
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public Libri[] RicercaLibri(String titolo, String autore, int anno, String editore, String categoria, float prezzoMin, float prezzoMax) throws RemoteException {
        try {
            q.Connect();
            Libri[] libri = q.RicercaLibri(titolo, autore, anno, editore, categoria, prezzoMin, prezzoMax);
            q.Disconnect();
            return libri;
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Restituisce l'array libri dati gli id di quei libri
     *
     * @param ids array di id dei libri
     * @return un array di Libri
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public Libri[] RicercaLibriDaIds(int[] ids) throws RemoteException {
        try {
            q.Connect();
            Libri[] libri = q.RicercaLibriDaIds(ids);
            q.Disconnect();
            if (libri == null) {
                return new Libri[0];
            }
            return libri;
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Restituisce tutte le librerie di un utente, le librerie contengono gli id dei libri presenti in esse
     *
     * @param idUtente id dell'utente
     * @return un array di Librerie
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public Librerie[] RicercaLibrerie(int idUtente) throws RemoteException {
        try {
            q.Connect();
            Librerie[] librerie = q.RicercaLibrerie(idUtente);
            q.Disconnect();
            if (librerie == null) {
                return new Librerie[0];
            }
            return librerie;
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Aggiunge una vuota libreria all'utente
     *
     * @param idUtente     id dell'utente
     * @param nomeLibreria libreria da aggiungere
     * @return Eccezione con:1, conflitti integrita, 2 registrazione fallita in principale, 3 errore SQL, 4 errore IdLibriria non ottenuto, 0 tutto ok con idLibreria nel messaggio come String
     * @throws RemoteException altre eccezioni
     */
    @Override
    public Eccezione AggiungiLibreria(int idUtente, String nomeLibreria) throws RemoteException {
        q.Connect();
        Eccezione ec = q.AggiungiLibreria(idUtente, nomeLibreria);
        q.Disconnect();
        return ec;
    }

    /**
     * Aggiunge un libro alla libreria
     *
     * @param idLibreria id della libreria
     * @param idLibro    id del libro da aggiungere
     * @return Eccezione con:1, conflitti integrita, 2 registrazione fallita in principale, 3 errore SQL, 4 errore IdLibriria non ottenuto, 0 tutto ok con idLibreria nel messaggio come String
     * @throws RemoteException altre eccezioni
     */
    @Override
    public Eccezione AggiungiLibroALibreria(int idLibreria, int idLibro) throws RemoteException {
        q.Connect();
        Eccezione ec = q.AggiungiLibroALibreria(idLibreria, idLibro);
        q.Disconnect();
        return ec;
    }

    /**
     * Controlla se la libreria esiste
     *
     * @param idUtente     id dell'utente
     * @param nomeLibreria nome della libreria da controllare
     * @return vero se la libreria esiste, falso altrimenti
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public boolean ControlloEsisteLibreria(int idUtente, String nomeLibreria) throws RemoteException {
        try {
            q.Connect();
            boolean b = q.ControlloEsisteLibreria(idUtente, nomeLibreria);
            q.Disconnect();
            return b;
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Controlla se un libro è presente in una libreria
     *
     * @param idUtente id dell'utente
     * @param idLibro  id del libro da controllare
     * @return vero se il libro è presente in una libreria, falso altrimenti
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public boolean ControlloLibroInLibrerie(int idUtente, int idLibro) throws RemoteException {
        try {
            q.Connect();
            boolean b = q.ControlloLibroInLibrerie(idUtente, idLibro);
            q.Disconnect();
            return b;
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Restituisce i libri presenti in tutte le librerie dell'utente
     *
     * @param idUtente id dell'utente
     * @return un array di Libri
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public Libri[] RicercaLibriDaLibrerie(int idUtente,String titoloRicerca, String autoreRicerca, int annoR) throws RemoteException {
        try {
            q.Connect();
            Libri[] libri = q.RicercaLibriDaLibrerie(idUtente,titoloRicerca,  autoreRicerca, annoR);
            q.Disconnect();
            if (libri == null) {
                return new Libri[0];
            }
            return libri;
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Rimuove un libro dalla libreria
     *
     * @param idLibreria id della libreria
     * @param idLibro    id del libro da rimuovere
     * @return Eccezione 0 tutto ok, 3 errore SQL, 2 errore eliminazione non avvenuta
     * @throws RemoteException altre eccezioni
     */
    @Override
    public Eccezione RimuoviLibroDaLibreria(int idLibreria, int idLibro) throws RemoteException {
        q.Connect();
        Eccezione ec = q.RimuoviLibroDaLibreria(idLibreria, idLibro);
        q.Disconnect();
        return ec;
    }

    /**
     * Rimuove una libreria
     *
     * @param idLibreria id della libreria da rimuovere
     * @return Eccezione 0 tutto ok, 3 errore SQL, 2 errore eliminazione non avvenuta
     * @throws RemoteException altre eccezioni
     */
    @Override
    public Eccezione RimuoviLibreria(int idLibreria) throws RemoteException {
        q.Connect();
        Eccezione ec = q.RimuoviLibreria(idLibreria);
        q.Disconnect();
        return ec;
    }

    /**
     * Aggiunge un consiglio al database
     *
     * @param consiglio oggetto ConsigliLibri contenente i dati del consiglio
     * @return Eccezione con codice 0 se tutto ok, 1 se ci sono conflitti di integrità, 2 se l'aggiunta fallisce, 3 se c'è un errore SQL, 4 se non ci sono libri consigliati
     * @throws RemoteException altre eccezioni
     */
    @Override
    public Eccezione AggiungiConsiglio(ConsigliLibri consiglio) throws RemoteException {
        q.Connect();
        Eccezione ec = q.AggiungiConsiglio(consiglio);
        q.Disconnect();
        return ec;
    }

    /**
     * Aggiunge un libro a un consiglio esistente
     *
     * @param idUtente           id dell'utente
     * @param idRiguardante      id del libro riguardante
     * @param idLibroConsigliato id del libro consigliato
     * @return Eccezione con codice 0 se tutto ok, 1 se ci sono conflitti di integrità, 2 se l'aggiunta fallisce, 3 se c'è un errore SQL, 4 errore nella ricerca dei consigli, 5 se il libro è già presente nei consigli
     */
    @Override
    public Eccezione AggiungiLibroAConsiglio(int idUtente, int idRiguardante, int idLibroConsigliato) throws RemoteException {
        q.Connect();
        ConsigliLibri cons;

        try {
            cons = q.RicercaConsigliDatoUtenteELibro(idUtente, idRiguardante);
        } catch (SQLException e) {
            q.Disconnect();
            return new Eccezione(4, "err nella ricerca dei consigli" + e.getMessage());
        }
        int[] libri = cons.getConsigliLibri();
        boolean exist = false;
        for (int j : libri) {
            if (j == idLibroConsigliato) {
                q.Disconnect();
                return new Eccezione(5, "Il libro è già presente nei consigli");
            }
            if (j != -1) {
                exist = true;
            }
        }
        Eccezione ec;
        if (exist)
            ec = q.AggiungiLibroAConsiglio(idUtente, idRiguardante, idLibroConsigliato);
        else {
            ec = q.AggiungiConsiglio(new ConsigliLibri(idUtente, idRiguardante, idLibroConsigliato, -1, -1));
        }
        q.Disconnect();
        return ec;
    }

    /**
     * Restituisce i libri consigliati da parte di tutti gli utenti per un libro specifico tenendo i duplicati
     *
     * @param idLibro id del libro
     * @return un array di Libri
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public Libri[] RicercaConsigliDatoLibro(int idLibro) throws RemoteException {
        try {
            q.Connect();
            Libri[] libri = q.RicercaConsigliDatoLibro(idLibro);
            q.Disconnect();
            return libri != null ? libri : new Libri[0];
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Restituisce i libri consigliati da parte di un utente per un libro specifico
     *
     * @param idUtente id dell'utente
     * @param idLibro  id del libro
     * @return un oggetto ConsigliLibri contenente i libri consigliati
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public Libri[] RicercaConsigliDatoUtenteELibro(int idUtente, int idLibro) throws RemoteException {
        try {
            q.Connect();
            ConsigliLibri c = q.RicercaConsigliDatoUtenteELibro(idUtente, idLibro);
            if (c != null) {
                int[] libri = c.getConsigliLibri();
                if (c.isEmpty()) {
                    q.Disconnect();
                    return new Libri[0];
                } else {
                    Libri[] l = q.RicercaLibriDaIds(libri);
                    q.Disconnect();
                    return l;
                }
            } else {
                q.Disconnect();
                return new Libri[0];
            }
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Aggiunge una valutazione al database
     *
     * @param v oggetto ValutazioniLibri contenente i dati della valutazione
     * @return Eccezione con codice 0 se tutto ok, 1 se ci sono conflitti di integrità, 2 se l'aggiunta fallisce, 3 se c'è un errore SQL
     * @throws RemoteException altre eccezioni
     */
    @Override
    public Eccezione AggiungiValutazione(ValutazioniLibri v) throws RemoteException {
        q.Connect();
        Eccezione ec = q.AggiungiValutazione(v);
        q.Disconnect();
        return ec;
    }

    /**
     * Restituisce la valutazione di un libro da parte di un utente
     *
     * @param idUtente id dell'utente
     * @param idLibro  id del libro
     * @return oggetto ValutazioniLibri con i dati della valutazione
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public ValutazioniLibri RicercaValutazione(int idUtente, int idLibro) throws RemoteException {
        try {
            q.Connect();
            ValutazioniLibri v = q.RicercaValutazione(idUtente, idLibro);
            q.Disconnect();
            return v;
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }

    /**
     * Restituisce la valutazione media di un libro
     *
     * @param idLibro id del libro
     * @return oggetto ValutazioniLibri con i dati della valutazione media
     * @throws RemoteException tutte le eccezioni
     */
    @Override
    public ValutazioniLibri RicercaValutazioneMedia(int idLibro) throws RemoteException {
        try {
            q.Connect();
            ValutazioniLibri v = q.RicercaValutazioneMedia(idLibro);
            q.Disconnect();
            return v;
        } catch (SQLException e) {
            q.Disconnect();
            throw new RemoteException("Errore in QueryList: " + e.getMessage());
        }
    }
}