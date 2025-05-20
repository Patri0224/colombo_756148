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
     * @param password dare password in chiaro
     * @return Eccezione con:1 se l'utente non esiste, 2 se la password è errata, 3 errore interno, null se tutto ok
     * @throws RemoteException
     */
    @Override
    public Eccezione login(int idUtente, String password) throws RemoteException, SQLException {
        q.Connect();
        Eccezione ec = q.ControlloPasswordUtente(idUtente, password);
        q.Disconnect();
        return ec;
    }

    /**
     * Login per l'utente tramite email e password
     *
     * @param email
     * @param password
     * @return Eccezione con:1 se l'utente non esiste, 2 se la password è errata, 3 errore interno, null se tutto ok
     * @throws RemoteException
     */
    @Override
    public Eccezione login(String email, String password) throws RemoteException, SQLException {
        q.Connect();
        Eccezione ec = q.ControlloPasswordUtente(email, password);
        q.Disconnect();
        return ec;
    }

    /**
     * da email a idUtente
     *
     * @param email email dell'utente
     * @return l'id dell'utente se il esiste, -1 altrimenti
     * @throws RemoteException
     */
    @Override
    public int getIdUtente(String email) throws RemoteException, SQLException {
        q.Connect();
        int id = q.GetIdUtenteDaEmail(email);
        q.Disconnect();
        return id;
    }

    /**
     * Controlla se l'utente esiste nel database dato il suo id
     *
     * @param idUtente
     * @return vero se l'utente esiste, falso altrimenti
     * @throws RemoteException
     */
    @Override
    public boolean ControlloEsisteUtente(int idUtente) throws RemoteException, SQLException {
        q.Connect();
        boolean b = q.ControlloEsisteUtente(idUtente);
        q.Disconnect();
        return b;
    }

    @Override
    public String[] GetUtenteRegistrato(int idUtente) throws RemoteException, SQLException {
        q.Connect();
        String[] utente = q.GetUtenteRegistrato(idUtente);
        q.Disconnect();
        return utente;
    }

    /**
     * Controlla se l'utente esiste nel database dato la sua email
     *
     * @param email
     * @return vero se l'utente esiste, falso altrimenti
     * @throws RemoteException
     */
    @Override
    public boolean ControlloEsisteUtente(String email) throws RemoteException, SQLException {
        q.Connect();
        boolean b = q.ControlloEsisteUtente(email);
        q.Disconnect();
        return b;
    }

    /**
     * Crea un nuovo utente nel database
     *
     * @param email
     * @param password
     * @param nome
     * @param cognome
     * @param codiceFiscale
     * @return Eccezione con:1 se l'utente già esiste, 2, 3, 4 errori SQL, null se tutto ok
     * @throws RemoteException
     */
    @Override
    public Eccezione Registrazione(String email, String password, String nome, String cognome, String codiceFiscale) throws RemoteException, SQLException {
        q.Connect();
        Eccezione ec = q.Registrazione(email, password, nome, cognome, codiceFiscale);
        q.Disconnect();
        return ec;

    }

    /**
     * Modifica la password dell'utente
     *
     * @param idUtente
     * @param password nuova password
     * @return Eccezione con:1 se l'utente non esiste, 2, 3 errori SQL, null se tutto ok
     * @throws RemoteException
     */
    @Override
    public Eccezione ModificaPassword(int idUtente, String password) throws RemoteException, SQLException {
        q.Connect();
        Eccezione ec = q.ModificaPassword(idUtente, password);
        q.Disconnect();
        return ec;
    }

    /**
     * Rimuove un utente dal database
     *
     * @param idUtente
     * @return Eccezione con:1 se l'utente non esiste, 2, 3 errori SQL, null se tutto ok
     * @throws RemoteException
     */
    @Override
    public Eccezione RimuoviUtente(int idUtente) throws RemoteException, SQLException {
        q.Connect();
        Eccezione ec = q.RimuoviUtente(idUtente);
        q.Disconnect();
        return ec;

    }

    /**
     * Restituisce l'array libri presenti nel database che contengono i campi di ricerca
     *
     * @param titolo stringa per cercare nel titolo, può essere null
     * @param autore stringa per cercare nell'autore, può essere null
     * @param anno   anno di pubblicazione, 0 se non specificato
     * @return un array di Libri
     * @throws RemoteException
     */
    @Override
    public Libri[] RicercaLibri(String titolo, String autore, int anno) throws RemoteException, SQLException {
        q.Connect();
        Libri[] libri = q.RicercaLibri(titolo, autore, anno);
        q.Disconnect();
        return libri;
    }

    /**
     * Restituisce l'array libri dati gli id di quei libri
     *
     * @param ids array di id dei libri
     * @return un array di Libri
     * @throws RemoteException
     */
    @Override
    public Libri[] RicercaLibriDaIds(int[] ids) throws RemoteException, SQLException {
        q.Connect();
        Libri[] libri = q.RicercaLibriDaIds(ids);
        q.Disconnect();
        if (libri == null) {
            return new Libri[0];
        }
        return libri;
    }

    /**
     * Restituisce tutte le librerie di un utente, le librerie contengono gli id dei libri presenti in esse
     *
     * @param idUtente
     * @return un array di Librerie
     * @throws RemoteException
     */
    @Override
    public Librerie[] RicercaLibrerie(int idUtente) throws RemoteException, SQLException {
        q.Connect();
        Librerie[] librerie = q.RicercaLibrerie(idUtente);
        q.Disconnect();
        if (librerie == null) {
            return new Librerie[0];
        }
        return librerie;
    }

    /**
     * @param idUtente id dell'utente
     * @param libreria libreria da aggiungere
     * @return Eccezione con:1, conflitti integrita, 2 registrazione fallita in principale, 3 errore SQL, 4 errore IdLibriria non ottenuto, null se tutto ok
     * @throws RemoteException
     */
    @Override
    public Eccezione AggiungiLibreria(int idUtente, Librerie libreria) throws RemoteException {
        q.Connect();
        Eccezione ec = q.AggiungiLibreria(idUtente, libreria);
        q.Disconnect();
        return ec;
    }

    @Override
    public Eccezione AggiungiLibroALibreria(int idLibreria, int idLibro) throws RemoteException {
        q.Connect();
        Eccezione ec = q.AggiungiLibroALibreria(idLibreria, idLibro);
        q.Disconnect();
        return ec;
    }


    @Override
    public boolean ControlloEsisteLibreria(int idUtente, String nomeLibreria) throws RemoteException, SQLException {
        q.Connect();
        boolean b = q.ControlloEsisteLibreria(idUtente, nomeLibreria);
        q.Disconnect();
        return b;
    }


    @Override
    public boolean ControlloLibroInLibrerie(int idUtente, int idLibro) throws RemoteException, SQLException {
        q.Connect();
        boolean b = q.ControlloLibroInLibrerie(idUtente, idLibro);
        q.Disconnect();
        return b;
    }


    @Override
    public Libri[] RicercaLibriDaLibrerie(int idUtente) throws RemoteException, SQLException {
        q.Connect();
        Libri[] libri = q.RicercaLibriDaLibrerie(idUtente);
        q.Disconnect();
        if (libri == null) {
            return new Libri[0];
        }
        return libri;
    }

    @Override
    public Eccezione RimuoviLibroDaLibreria(int idLibreria, int idLibro) throws RemoteException {
        q.Connect();
        Eccezione ec = q.RimuoviLibroDaLibreria(idLibreria, idLibro);
        q.Disconnect();
        return ec;
    }

    @Override
    public Eccezione RimuoviLibreria(int idLibreria) throws RemoteException {
        q.Connect();
        Eccezione ec = q.RimuoviLibreria(idLibreria);
        q.Disconnect();
        return ec;
    }


    @Override
    public Eccezione AggiungiConsiglio(ConsigliLibri consiglio) throws RemoteException {
        q.Connect();
        Eccezione ec = q.AggiungiConsiglio(consiglio);
        q.Disconnect();
        return ec;
    }


    @Override
    public Eccezione AggiungiLibroAConsiglio(ConsigliLibri consiglio, int idLibroConsigliato) throws RemoteException {
        q.Connect();
        Eccezione ec = q.AggiungiLibroAConsiglio(consiglio, idLibroConsigliato);
        q.Disconnect();
        return ec;
    }


    @Override
    public Libri[] RicercaConsigliDatoLibro(int idLibro) throws RemoteException, SQLException {
        q.Connect();
        Libri[] libri = q.RicercaConsigliDatoLibro(idLibro);
        q.Disconnect();
        if (libri == null) {
            return new Libri[0];
        }
        return libri;
    }


    @Override
    public ConsigliLibri RicercaConsigliDatoUtenteELibro(int idUtente, int idLibro) throws RemoteException, SQLException {
        q.Connect();
        ConsigliLibri c = q.RicercaConsigliDatoUtenteELibro(idUtente, idLibro);
        q.Disconnect();
        return c;
    }

    @Override
    public Eccezione AggiungiValutazione(ValutazioniLibri v) throws RemoteException {
        q.Connect();
        Eccezione ec = q.AggiungiValutazione(v);
        q.Disconnect();
        return ec;
    }


    @Override
    public ValutazioniLibri RicercaValutazione(int idUtente, int idLibro) throws RemoteException, SQLException {
        q.Connect();
        ValutazioniLibri v = q.RicercaValutazione(idUtente, idLibro);
        q.Disconnect();
        return v;
    }

    @Override
    public ValutazioniLibri RicercaValutazioneMedia(int idLibro) throws RemoteException, SQLException {
        q.Connect();
        ValutazioniLibri v = q.RicercaValutazioneMedia(idLibro);
        q.Disconnect();
        return v;
    }
}
