package bookRecommender;

import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.Score;
import bookRecommender.entita.ValutazioniLibri;
import bookRecommender.rmi.ServerBookRecommenderInterface;

public class ValutazioniGestore {
    private static ValutazioniGestore instance;
    private final ServerBookRecommenderInterface stub;
    private final UtenteGestore utenteGestore;

    private ValutazioniGestore(ServerBookRecommenderInterface stub) {
        this.stub = stub;
        this.utenteGestore = UtenteGestore.GetInstance();
    }

    public static ValutazioniGestore CreateInstance(ServerBookRecommenderInterface stub) {
        if (instance == null) {
            instance = new ValutazioniGestore(stub);
        }
        return instance;
    }

    public static ValutazioniGestore GetInstance() {
        return instance;
    }

    /**
     * Aggiunge una valutazione al libro da parte dell'utente
     *
     * @param idLibro id del libro
     * @param score   oggetto score con le valutazioni
     * @return Eccezione con: 0 se tutto ok, >0 se ci sono errori
     */
    public Eccezione AggiungiValutazione(int idLibro, Score score) {
        if (!utenteGestore.UtenteLoggato()) {
            return new Eccezione(5, "Utente non loggato");
        }
        ValutazioniLibri v = new ValutazioniLibri(utenteGestore.GetIdUtente(), idLibro, score);
        try {
            return stub.AggiungiValutazione(v);
        } catch (Exception e) {
            return new Eccezione(4, "Remote problem : " + e.getMessage());
        }
    }

    /**
     * Aggiunge una valutazione al libro da parte dell'utente
     *
     * @param v oggetto valutazione
     * @return Eccezione con: 0 se tutto ok, >0 se ci sono errori
     */
    public Eccezione AggiungiValutazione(ValutazioniLibri v) {
        if (!utenteGestore.UtenteLoggato()) {
            return new Eccezione(5, "Utente non loggato");
        }
        try {
            return stub.AggiungiValutazione(v);
        } catch (Exception e) {
            return new Eccezione(1, "Errore durante l'aggiunta della valutazione: " + e.getMessage());
        }
    }

    /**
     * Cerca la valutazione che l'utente ha fatto sul libro
     *
     * @param idLibro id del libro
     * @return la valutazione dell'utente sul libro o null se non esiste o se ci sono errori
     */
    public ValutazioniLibri RicercaValutazione(int idLibro) {
        if (!utenteGestore.UtenteLoggato()) {
            return null;
        }
        try {
            return stub.RicercaValutazione(utenteGestore.GetIdUtente(), idLibro);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Cerca la valutazione media del libro
     *
     * @param idLibro id del libro
     * @return la valutazione media del libro o null se non esiste o se ci sono errori
     */
    public ValutazioniLibri RicercaValutazioneMedia(int idLibro) {
        try {
            return stub.RicercaValutazioneMedia(idLibro);
        } catch (Exception e) {
            return null;
        }
    }

}
