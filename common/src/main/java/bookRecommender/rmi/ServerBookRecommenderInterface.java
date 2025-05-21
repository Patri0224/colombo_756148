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
     * @param password password dell'utente già criptata
     * @return Eccezione con:1 se l'utente non esiste, 2 se la password è errata, 3 errore interno, 0 con messaggio id dell'utente
     * @throws RemoteException altre eccezioni
     */
    Eccezione login(int idUtente, String password) throws RemoteException;

    /**
     * Login per l'utente tramite email e password
     *
     * @param email    email dell'utente
     * @param password password dell'utente già criptata
     * @return Eccezione con:1 se l'utente non esiste, 2 se la password è errata, 3 errore interno, 0 con messaggio id dell'utente
     * @throws RemoteException altre eccezioni
     */
    Eccezione login(String email, String password) throws RemoteException;

    /**
     * da email a idUtente
     *
     * @param email email dell'utente
     * @return l'id dell'utente se il esiste, -1 altrimenti
     * @throws RemoteException tutte le eccezioni
     */
    int getIdUtente(String email) throws RemoteException;

    /**
     * Controlla se l'utente esiste nel database dato il suo id
     *
     * @param idUtente id dell'utente
     * @return vero se l'utente esiste, falso altrimenti
     * @throws RemoteException tutte le eccezioni
     */
    boolean ControlloEsisteUtente(int idUtente) throws RemoteException;

    /**
     * Controlla se l'utente esiste nel database dato la sua email
     *
     * @param email email dell'utente
     * @return vero se l'utente esiste, falso altrimenti
     * @throws RemoteException tutte le eccezioni
     */
    boolean ControlloEsisteUtente(String email) throws RemoteException;

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
    Eccezione Registrazione(String email, String password, String nome, String cognome, String codiceFiscale) throws RemoteException;

    /**
     * Modifica la password dell'utente
     *
     * @param idUtente id dell'utente
     * @param password nuova password
     * @return Eccezione con:1 se l'utente non esiste, 2, 3 errori SQL, 0 tutto ok
     * @throws RemoteException altre eccezioni
     */
    Eccezione ModificaPassword(int idUtente, String password) throws RemoteException;

    /**
     * Rimuove un utente dal database
     *
     * @param idUtente id dell'utente
     * @return Eccezione con:1 se l'utente non esiste, 2, 3 errori SQL, 0 se tutto ok
     * @throws RemoteException altre eccezioni
     */
    Eccezione RimuoviUtente(int idUtente) throws RemoteException;

    /**
     * Restituisce i dati dell'utente dato il suo id
     *
     * @param idUtente id dell'utente
     * @return un array di stringhe con i dati dell'utente [0] email, [1] nome, [2] cognome, [3] codice fiscale
     * @throws RemoteException tutte le eccezioni
     */
    String[] GetUtenteRegistrato(int idUtente) throws RemoteException;

    //libri

    /**
     * Restituisce l'array libri presenti nel database che contengono i campi di ricerca
     *
     * @param titolo stringa per cercare nel titolo, può essere null
     * @param autore stringa per cercare nell'autore, può essere null
     * @param anno   anno di pubblicazione, -1 se non specificato
     * @return un array di Libri
     * @throws RemoteException tutte le eccezioni
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
     * @throws RemoteException tutte le eccezioni
     */
    Libri[] RicercaLibri(String titolo, String autore, int anno, String editore, String categoria, float prezzoMin, float prezzoMax) throws RemoteException;

    /**
     * Restituisce l'array libri dati gli id di quei libri
     *
     * @param ids array di id dei libri
     * @return un array di Libri
     * @throws RemoteException tutte le eccezioni
     */
    Libri[] RicercaLibriDaIds(int[] ids) throws RemoteException;

    //Librerie

    /**
     * Restituisce tutte le librerie di un utente, le librerie contengono gli id dei libri presenti in esse
     *
     * @param idUtente id dell'utente
     * @return un array di Librerie
     * @throws RemoteException tutte le eccezioni
     */
    Librerie[] RicercaLibrerie(int idUtente) throws RemoteException;

    /**
     * Aggiunge una vuota libreria al database
     * @param idUtente     id dell'utente
     * @param nomeLibreria nome della libreria da aggiungere
     * @return Eccezione con:1, conflitti integrita, 2 registrazione fallita in principale, 3 errore SQL, 4 errore IdLibriria non ottenuto, 0 tutto ok con idLibreria nel messaggio come String
     * @throws RemoteException altre eccezioni
     */
    Eccezione AggiungiLibreria(int idUtente, String nomeLibreria) throws RemoteException;
    /**
     * Aggiunge un libro alla libreria
     *
     * @param idLibreria id della libreria
     * @param idLibro    id del libro da aggiungere
     * @return Eccezione con:1, conflitti integrita, 2 registrazione fallita in principale, 3 errore SQL, 4 errore IdLibriria non ottenuto, 0 tutto ok con idLibreria nel messaggio come String
     * @throws RemoteException altre eccezioni
     */
    Eccezione AggiungiLibroALibreria(int idLibreria, int idLibro) throws RemoteException;
    /**
     * Controlla se la libreria esiste
     *
     * @param idUtente     id dell'utente
     * @param nomeLibreria nome della libreria da controllare
     * @return vero se la libreria esiste, falso altrimenti
     * @throws RemoteException tutte le eccezioni
     */
    boolean ControlloEsisteLibreria(int idUtente, String nomeLibreria) throws RemoteException;
    /**
     * Controlla se un libro è presente in una libreria
     *
     * @param idUtente id dell'utente
     * @param idLibro  id del libro da controllare
     * @return vero se il libro è presente in una libreria, falso altrimenti
     * @throws RemoteException tutte le eccezioni
     */
    boolean ControlloLibroInLibrerie(int idUtente, int idLibro) throws RemoteException;
    /**
     * Restituisce i libri presenti in tutte le librerie dell'utente
     *
     * @param idUtente id dell'utente
     * @return un array di Libri
     * @throws RemoteException tutte le eccezioni
     */
    Libri[] RicercaLibriDaLibrerie(int idUtente) throws RemoteException;
    /**
     * Rimuove un libro dalla libreria
     *
     * @param idLibreria id della libreria
     * @param idLibro   id del libro da rimuovere
     * @return Eccezione 0 tutto ok, 3 errore SQL, 2 errore eliminazione non avvenuta
     * @throws RemoteException altre eccezioni
     */
    Eccezione RimuoviLibroDaLibreria(int idLibreria, int idLibro) throws RemoteException;
    /**
     * Rimuove una libreria
     *
     * @param idLibreria id della libreria da rimuovere
     * @return Eccezione 0 tutto ok, 3 errore SQL, 2 errore eliminazione non avvenuta
     * @throws RemoteException altre eccezioni
     */
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
