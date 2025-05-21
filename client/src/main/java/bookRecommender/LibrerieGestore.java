package bookRecommender;

import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.Librerie;
import bookRecommender.entita.Libri;
import bookRecommender.rmi.ServerBookRecommenderInterface;

import java.rmi.RemoteException;

public class LibrerieGestore {
    private static LibrerieGestore instance = null;
    private final ServerBookRecommenderInterface stub;
    private Librerie[] librerie = new Librerie[0];
    private final UtenteGestore utenteGestore;

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

    public static LibrerieGestore CreateInstance(ServerBookRecommenderInterface stub) {
        if (instance == null) {
            instance = new LibrerieGestore(stub);
        }
        return instance;
    }

    public static LibrerieGestore GetInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

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

    public Librerie[] GetLibrerie() {
        if (librerie.length == 0) {
            caricaLibrerie();
        }
        return librerie;
    }

    public int GetNumeroLibrerie() {
        if (librerie.length == 0) {
            caricaLibrerie();
        }
        return librerie.length;
    }

    public Librerie GetLibreria(String nomeLibreria) {
        for (Librerie value : librerie) {
            if (value.getNome().equals(nomeLibreria)) {
                return value;
            }
        }
        return null;
    }

    public Eccezione AggiungiLibreria(String nomeLibreria) {
        if (utenteGestore.UtenteLoggato()) {
            try {
                Eccezione ecc = stub.AggiungiLibreria(utenteGestore.GetIdUtente(), nomeLibreria);
                if (ecc.getErrorCode() == 0) {
                    caricaLibrerie();
                    return ecc;
                } else {
                    return ecc;
                }
            } catch (RemoteException e) {
                return new Eccezione(2, "Errore durante l'aggiunta della libreria" + e.getMessage());
            }
        } else {
            return new Eccezione(1, "Utente non Loggato");
        }
    }

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

    public Eccezione AggiungiLibroALibreria(String nomeLibreria, int idLibro) {
        Librerie libreria = GetLibreria(nomeLibreria);
        if (libreria != null) {
            return AggiungiLibroALibreria(libreria.getIdLibreria(), idLibro);
        } else {
            return new Eccezione(1, "Libreria non trovata");
        }
    }

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

    public Libri[] GetLibriDaTutteLibrerie() {
        if (utenteGestore.UtenteLoggato()) {
            try {
                return stub.RicercaLibriDaLibrerie(utenteGestore.GetIdUtente());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
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

}
