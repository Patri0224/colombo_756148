package bookRecommender.rmi;

import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.ConsigliLibri;
import bookRecommender.entita.Librerie;
import bookRecommender.entita.Libri;
import bookRecommender.entita.ValutazioniLibri;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface ServerBookRecommenderInterface extends Remote {

    /**
     * Login per l'utente tramite id e password
     *
     * @param idUtente id dell'utente dato dal database
     * @param password
     * @return Eccezione con:1 se l'utente non esiste, 2 se la password è errata, 3 errore interno, null se tutto ok
     * @throws RemoteException
     */
    Eccezione login(int idUtente, String password) throws RemoteException;

    /**
     * Login per l'utente tramite email e password
     *
     * @param email
     * @param password
     * @return Eccezione con:1 se l'utente non esiste, 2 se la password è errata, 3 errore interno, null se tutto ok
     * @throws RemoteException
     */
    Eccezione login(String email, String password) throws RemoteException;

    /**
     * da email a idUtente
     *
     * @param email email dell'utente
     * @return l'id dell'utente se il esiste, -1 altrimenti
     * @throws RemoteException
     */
    int getIdUtente(String email) throws RemoteException;

    /**
     * Controlla se l'utente esiste nel database dato il suo id
     *
     * @param idUtente
     * @return vero se l'utente esiste, falso altrimenti
     * @throws RemoteException
     */
    boolean ControlloEsisteUtente(int idUtente) throws RemoteException;

    /**
     * Controlla se l'utente esiste nel database dato la sua email
     *
     * @param email
     * @return vero se l'utente esiste, falso altrimenti
     * @throws RemoteException
     */
    boolean ControlloEsisteUtente(String email) throws RemoteException;

    /**
     * Crea un nuovo utente nel database
     *
     * @param email
     * @return Eccezione con:1 se l'utente già esiste, 2, 3, 4 errori SQL, null se tutto ok
     * @throws RemoteException
     */
    Eccezione Registrazione(String email, String password, String nome, String cognome, String codiceFiscale) throws RemoteException;

    /**
     * Modifica la password dell'utente
     *
     * @param idUtente
     * @param password nuova password
     * @return Eccezione con:1 se l'utente non esiste, 2, 3 errori SQL, null se tutto ok
     * @throws RemoteException
     */
    Eccezione ModificaPassword(int idUtente, String password) throws RemoteException;

    /**
     * Rimuove un utente dal database
     *
     * @param idUtente
     * @return Eccezione con:1 se l'utente non esiste, 2, 3 errori SQL, null se tutto ok
     * @throws RemoteException
     */
    Eccezione RimuoviUtente(int idUtente) throws RemoteException;

    String[] GetUtenteRegistrato(int idUtente) throws RemoteException;

    //libri

    /**
     * Restituisce l'array libri presenti nel database che contengono i campi di ricerca
     *
     * @param titolo stringa per cercare nel titolo, può essere null
     * @param autore stringa per cercare nell'autore, può essere null
     * @param anno   anno di pubblicazione, -1 se non specificato
     * @return un array di Libri
     * @throws RemoteException
     */
    Libri[] RicercaLibri(String titolo, String autore, int anno) throws RemoteException;

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
     * @throws RemoteException
     * @throws SQLException
     */
    Libri[] RicercaLibri(String titolo, String autore, int anno, String editore, String categoria, float prezzoMin, float prezzoMax) throws RemoteException;

    /**
     * Restituisce l'array libri dati gli id di quei libri
     *
     * @param ids array di id dei libri
     * @return un array di Libri
     * @throws RemoteException
     */
    Libri[] RicercaLibriDaIds(int[] ids) throws RemoteException;

    //Librerie

    /**
     * Restituisce tutte le librerie di un utente, le librerie contengono gli id dei libri presenti in esse
     *
     * @param idUtente
     * @return un array di Librerie
     * @throws RemoteException
     */
    Librerie[] RicercaLibrerie(int idUtente) throws RemoteException;

    /**
     * @param idUtente id dell'utente
     * @param nomeLibreria nome della libreria da aggiungere
     * @return Eccezione con:1, conflitti integrita, 2 registrazione fallita in principale, 3 errore SQL, 4 errore IdLibriria non ottenuto, 0 tutto ok con idLibreria nel messaggio come String
     * @throws RemoteException
     */
    Eccezione AggiungiLibreria(int idUtente, String nomeLibreria) throws RemoteException;

    Eccezione AggiungiLibroALibreria(int idLibreria, int idLibro) throws RemoteException;

    boolean ControlloEsisteLibreria(int idUtente, String nomeLibreria) throws RemoteException;

    boolean ControlloLibroInLibrerie(int idUtente, int idLibro) throws RemoteException;

    Libri[] RicercaLibriDaLibrerie(int idUtente) throws RemoteException;

    Eccezione RimuoviLibroDaLibreria(int idLibreria, int idLibro) throws RemoteException;

    Eccezione RimuoviLibreria(int idLibreria) throws RemoteException;

    // Consigli
    Eccezione AggiungiConsiglio(ConsigliLibri consiglio) throws RemoteException;

    Eccezione AggiungiLibroAConsiglio(ConsigliLibri consiglio, int idLibroConsigliato) throws RemoteException;

    Libri[] RicercaConsigliDatoLibro(int idLibro) throws RemoteException;

    ConsigliLibri RicercaConsigliDatoUtenteELibro(int idUtente, int idLibro) throws RemoteException;

    //valutazioni
    Eccezione AggiungiValutazione(ValutazioniLibri v) throws RemoteException;

    ValutazioniLibri RicercaValutazione(int idUtente, int idLibro) throws RemoteException;

    ValutazioniLibri RicercaValutazioneMedia(int idLibro) throws RemoteException;


}
