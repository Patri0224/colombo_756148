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
import java.sql.Connection;
import java.sql.SQLException;

public class ImpServer implements ServerBookRecommenderInterface, Serializable {
    private final transient Connection conn;

    public ImpServer(Connection conn) throws RemoteException {
        this.conn = conn;
    }


    /**
     * Login per l'utente tramite id e password
     *
     * @param idUtente id dell'utente dato dal database
     * @param password
     * @return Eccezione con:1 se l'utente non esiste, 2 se la password è errata, 3 errore interno, null se tutto ok
     * @throws RemoteException
     */
    @Override
    public Eccezione login(int idUtente, String password) throws RemoteException, SQLException {
        return QueryList.ControlloPasswordUtente(idUtente, password, conn);
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
        return QueryList.ControlloPasswordUtente(email, password, conn);
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
        return QueryList.GetIdUtenteDaEmail(email, conn);
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
        return QueryList.ControlloEsisteUtente(idUtente, conn);
    }

    @Override
    public String[] GetUtenteRegistrato(int idUtente) throws RemoteException, SQLException {
        return QueryList.getUtenteRegistrato(idUtente, conn);
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
        return QueryList.ControlloEsisteUtente(email, conn);
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
        return QueryList.Registrazione(email, password, nome, cognome, codiceFiscale, conn);
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
        return QueryList.ModificaPassword(idUtente, password, conn);
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
        return QueryList.RimuoviUtente(idUtente, conn);
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
        return QueryList.RicercaLibri(titolo, autore, anno, conn);
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
        return QueryList.RicercaLibriDaIds(ids, conn);
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
        return QueryList.RicercaLibrerie(idUtente, conn);
    }

    /**
     * @param idUtente id dell'utente
     * @param libreria libreria da aggiungere
     * @return Eccezione con:1, conflitti integrita, 2 registrazione fallita in principale, 3 errore SQL, 4 errore IdLibriria non ottenuto, null se tutto ok
     * @throws RemoteException
     */
    @Override
    public Eccezione AggiungiLibreria(int idUtente, Librerie libreria) throws RemoteException {
        return QueryList.AggiungiLibreria(idUtente, libreria, conn);
    }

    @Override
    public Eccezione AggiungiLibroALibreria(int idLibreria, int idLibro) throws RemoteException {
        return QueryList.AggiungiLibroALibreria(idLibreria, idLibro, conn);
    }


    @Override
    public boolean ControlloEsisteLibreria(int idUtente, String nomeLibreria) throws RemoteException, SQLException {
        return QueryList.ControlloEsisteLibreria(idUtente, nomeLibreria, conn);
    }


    @Override
    public boolean ControlloLibroInLibrerie(int idUtente, int idLibro) throws RemoteException, SQLException {
        return QueryList.ControlloLibroInLibrerie(idUtente, idLibro, conn);
    }


    @Override
    public Libri[] RicercaLibriDaLibrerie(int idUtente) throws RemoteException, SQLException {
        return QueryList.RicercaLibriDaLibrerie(idUtente, conn);
    }

    @Override
    public Eccezione RimuoviLibroDaLibreria(int idLibreria, int idLibro) throws RemoteException {
        return QueryList.RimuoviLibroDaLibreria(idLibreria, idLibro, conn);
    }

    @Override
    public Eccezione RimuoviLibreria(int idLibreria) throws RemoteException {
        return QueryList.RimuoviLibreria(idLibreria, conn);
    }


    @Override
    public Eccezione AggiungiConsiglio(ConsigliLibri consiglio) throws RemoteException {
        return QueryList.AggiungiConsiglio(consiglio, conn);
    }


    @Override
    public Eccezione AggiungiLibroAConsiglio(ConsigliLibri consiglio, int idLibroConsigliato) throws RemoteException {
        return QueryList.AggiungiLibroAConsiglio(consiglio, idLibroConsigliato, conn);
    }


    @Override
    public Libri[] RicercaConsigliDatoLibro(int idLibro) throws RemoteException, SQLException {
        return QueryList.RicercaConsigliDatoLibro(idLibro, conn);
    }


    @Override
    public ConsigliLibri RicercaConsigliDatoUtenteELibro(int idUtente, int idLibro) throws RemoteException, SQLException {
        return QueryList.RicercaConsigliDatoUtenteELibro(idUtente, idLibro, conn);
    }

    @Override
    public Eccezione AggiungiValutazione(ValutazioniLibri v) throws RemoteException {
        return QueryList.AggiungiValutazione(v, conn);
    }


    @Override
    public ValutazioniLibri RicercaValutazione(int idUtente, int idLibro) throws RemoteException, SQLException {
        return QueryList.RicercaValutazione(idUtente, idLibro, conn);
    }

    @Override
    public ValutazioniLibri RicercaValutazioneMedia(int idLibro) throws RemoteException, SQLException {
        return QueryList.RicercaValutazioneMedia(idLibro, conn);
    }
}
