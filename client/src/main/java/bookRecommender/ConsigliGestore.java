package bookRecommender;

import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.ConsigliLibri;
import bookRecommender.entita.Libri;
import bookRecommender.rmi.ServerBookRecommenderInterface;
import graphics.PopupError;

import java.rmi.RemoteException;

public class ConsigliGestore {
    private static ConsigliGestore instance = null;
    private final ServerBookRecommenderInterface stub;
    private final UtenteGestore utenteGestore;

    private ConsigliGestore(ServerBookRecommenderInterface stub) {
        this.stub = stub;
        this.utenteGestore = UtenteGestore.GetInstance();
    }

    public static ConsigliGestore CreateInstance(ServerBookRecommenderInterface stub) {
        if (instance == null) {
            instance = new ConsigliGestore(stub);
        }
        return instance;
    }

    public static ConsigliGestore GetInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    /**
     * Aggiunge un libro a un consiglio esistente
     *
     * @param idLibroRiguardante id del libro riguardante
     * @param idLibroConsigliato id del libro consigliato
     * @return Eccezione con codice 0 se tutto ok, 1 se ci sono conflitti di integrità, 2 se l'aggiunta fallisce, 3 se c'è un errore SQL, 4 errore nella ricerca dei consigli, 5 se il libro è già presente nei consigli, 6 utente non loggato, 7 libro riguardante non valido, 8 libro consigliato non presente in nessuna libreria, 9 remote error
     */
    public Eccezione AggiungiLibroAConsiglio(int idLibroRiguardante, int idLibroConsigliato) {
        if (!utenteGestore.UtenteLoggato()) {
            return new Eccezione(6, "Utente non loggato");
        }
        if (idLibroRiguardante == -1 || idLibroConsigliato == -1) {
            return new Eccezione(7, "Id libro non valido");
        }
        if(idLibroRiguardante == idLibroConsigliato) {
            return new Eccezione(8, "Non puoi consigliare un libro a se stesso");
        }
        /*LibrerieGestore libGes = LibrerieGestore.GetInstance();
        if (libGes.ControlloLibroInLibrerie(idLibroConsigliato)) {
            return new Eccezione(8, "Il libro consigliato non è presente in nessuna libreria");
        }*/
        try {
            return stub.AggiungiLibroAConsiglio(utenteGestore.GetIdUtente(), idLibroRiguardante, idLibroConsigliato);
        } catch (Exception e) {
            return new Eccezione(9, "Remote problem : " + e.getMessage());
        }
    }

    /**
     * Aggiunge un consiglio al database dati il libro riguardante e i libri consigliati
     *
     * @param idLibroRiguardante il libro riguardante
     * @param idLibriConsigliati i libri consigliati
     * @return Eccezione con codice 0 se tutto ok, 1 se ci sono conflitti di integrità, 2 se l'aggiunta fallisce, 3 se c'è un errore SQL, 4 se non ci sono libri consigliati, 5 utente non loggato, 6 libro riguardante non valido, 7 libro consigliato non presente in nessuna libreria, 8 errore remoto
     */
    public Eccezione AggiungiLibriAConsiglio(int idLibroRiguardante, int[] idLibriConsigliati) {
        if (!utenteGestore.UtenteLoggato()) {
            return new Eccezione(5, "Utente non loggato");
        }
        if (idLibroRiguardante == -1 || idLibriConsigliati.length == 0) {
            return new Eccezione(6, "Id libro non valido");
        }
        LibrerieGestore libGes = LibrerieGestore.GetInstance();
        for (int i : idLibriConsigliati) {
            if (libGes.ControlloLibroInLibrerie(i)) {
                return new Eccezione(7, "Il libro consigliato non è presente in nessuna libreria");
            }
        }
        try {
            ConsigliLibri c = new ConsigliLibri(idLibroRiguardante, utenteGestore.GetIdUtente(), idLibriConsigliati[0], idLibriConsigliati[1], idLibriConsigliati[2]);
            return stub.AggiungiConsiglio(c);
        } catch (Exception e) {
            return new Eccezione(8, "Remote problem : " + e.getMessage());
        }
    }

    /**
     * Aggiunge un consiglio al database dati il libro riguardante e i libri consigliati
     *
     * @param consiglio il consiglio già fatto
     * @return Eccezione con codice 0 se tutto ok, 1 se ci sono conflitti di integrità, 2 se l'aggiunta fallisce, 3 se c'è un errore SQL, 4 se non ci sono libri consigliati, 5 utente non loggato, 6 libro riguardante non valido, 7 libro consigliato non presente in nessuna libreria, 8 errore remoto
     */
    public Eccezione AggiungiConsiglio(ConsigliLibri consiglio) {
        if (!utenteGestore.UtenteLoggato()) {
            return new Eccezione(5, "Utente non loggato");
        }
        if (consiglio.getIdLibro() == -1) {
            return new Eccezione(6, "Id libro non valido");
        }
        LibrerieGestore libGes = LibrerieGestore.GetInstance();
        for (int i : consiglio.getConsigliLibri()) {
            if (i == -1) {
                if (libGes.ControlloLibroInLibrerie(i)) {
                    return new Eccezione(7, "Il libro consigliato non è presente in nessuna libreria");
                }
            }
        }

        try {
            return stub.AggiungiConsiglio(consiglio);
        } catch (Exception e) {
            return new Eccezione(9, "Remote problem : " + e.getMessage());
        }
    }

    public Libri[] RicercaConsigliDatoLibro(int idLibro) {
        if (idLibro == -1) {
            PopupError.mostraErrore("Id libro non valido");
            return null;
        }
        try {
            return stub.RicercaConsigliDatoLibro(idLibro);
        } catch (Exception e) {
            PopupError.mostraErrore(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Libri[] RicercaConsigliDatoUtenteELibro(int idLibro) throws RemoteException {
        if (!utenteGestore.UtenteLoggato()) {
            PopupError.mostraErrore("utente non loggato2");
            return null;
        }
        if (idLibro == -1) {
            PopupError.mostraErrore("Id libro non valido");
            return null;
        }
        try {
            return stub.RicercaConsigliDatoUtenteELibro(utenteGestore.GetIdUtente(), idLibro);
        } catch (Exception e) {
            PopupError.mostraErrore(e.getMessage());
            e.printStackTrace();
            return new Libri[0];
        }
    }
}

