/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
package bookRecommender;

import bookRecommender.eccezioni.Eccezione;
import bookRecommender.rmi.ServerBookRecommenderInterface;
import graphics.PopupError;

/**
 * Gestore delle operazioni relative agli utenti del sistema, come registrazione, login, logout e gestione dei dati utente.
 * Questa classe implementa il pattern Singleton per garantire che ci sia una sola istanza di UtenteGestore durante l'esecuzione dell'applicazione.
 */
public class UtenteGestore {
    private static UtenteGestore instance = null;
    private int idUtente = -1;
    private String email;
    private String passwordCriptata;
    private String codiceFiscale;
    private String nome;
    private String cognome;
    private final ServerBookRecommenderInterface stub;

    private UtenteGestore(ServerBookRecommenderInterface stub) {
        this.stub = stub;
    }

    public static synchronized UtenteGestore CreateInstance(ServerBookRecommenderInterface stub) {
        if (instance == null) {
            instance = new UtenteGestore(stub);
            return instance;
        }
        return instance;
    }

    public static synchronized UtenteGestore GetInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    /**
     * Controlla se la password rispetta i requisiti di sicurezza:
     *
     * @param password la password da controllare
     * @return true se la password è valida, false altrimenti
     */
    public static boolean ControlloCaratteriPassword(String password) {
        if (password.length() < 8)
            return false;
        if (!password.matches(".*[A-Z].*")) // almeno una maiuscola
            return false;
        if (!password.matches(".*[a-z].*")) // almeno una minuscola
            return false;
        if (!password.matches(".*[0-9].*")) // almeno un numero
            return false;
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) // almeno un carattere speciale
            return false;

        return true;
    }

    /**
     * Registra un nuovo utente nel sistema.
     * Controlla la validità del codice fiscale e della password prima di procedere con la registrazione.
     *
     * @param email         l'email dell'utente
     * @param password      la password dell'utente
     * @param codiceFiscale il codice fiscale dell'utente
     * @param nome          il nome dell'utente
     * @param cognome       il cognome dell'utente
     * @return un oggetto Eccezione che indica il risultato della registrazione
     */
    public Eccezione Registrazione(String email, String password, String codiceFiscale, String nome, String cognome) {
        if (!ControlloCodiceFiscale(codiceFiscale)) {
            return new Eccezione(2, "Codice fiscale non valido");
        }

        if (!ControlloCaratteriPassword(password)) {
            return new Eccezione(3, "Password non valida");
        }

        String passwordCriptata = CrittografiaPassword(password);

        try {
            Eccezione ecc = stub.Registrazione(email, passwordCriptata, nome, cognome, codiceFiscale);
            if (ecc.getErrorCode() == 0) {
                idUtente = GetIdUtente(email);
                this.email = email;
                this.nome = nome;
                this.cognome = cognome;
                this.codiceFiscale = codiceFiscale;
                this.passwordCriptata = passwordCriptata;
            }
            return ecc;
        } catch (Exception e) {
            return new Eccezione(5, "Remote Error: " + e.getMessage());
        }
    }

    /**
     * Ottiene l'ID dell'utente in base all'email.
     * Se l'utente è già loggato, restituisce l'ID memorizzato.
     *
     * @param email l'email dell'utente
     * @return l'ID dell'utente o -1 in caso di errore
     */
    public int GetIdUtente(String email) {
        if (idUtente != -1)
            return idUtente; //se l'utente è già loggato

        try {
            idUtente = stub.getIdUtente(email);
            return idUtente;
        } catch (Exception e) {
            try {//riprova una volta
                idUtente = stub.getIdUtente(email);
                return idUtente;
            } catch (Exception e1) {
                PopupError.mostraErrore(e.getMessage());
                return -1;
            }
        }
    }

    /**
     * Ottiene l'ID dell'utente in base all'email memorizzata nell'istanza.
     * Se l'utente è già loggato, restituisce l'ID memorizzato.
     *
     * @return l'ID dell'utente o -1 in caso di errore
     */
    public int GetIdUtente() {
        if (idUtente != -1)
            return idUtente;
        if (email == null || email.isEmpty()) {
            return -1;
        }
        try {
            idUtente = stub.getIdUtente(email);
            return idUtente;
        } catch (Exception e) {
            try {//riprova una volta
                idUtente = stub.getIdUtente(email);
                return idUtente;
            } catch (Exception e1) {
                PopupError.mostraErrore(e.getMessage());
                return -1;
            }
        }
    }

    public String GetNome() {
        return nome;
    }

    public String GetCognome() {
        return cognome;
    }

    public String GetEmail() {
        return email;
    }

    public String GetCF() {
        return codiceFiscale;
    }

    /**
     * Esegue il login dell'utente utilizzando l'email e la password.
     * Se il login ha successo, memorizza le informazioni dell'utente nell'istanza.
     *
     * @param email    l'email dell'utente
     * @param password la password dell'utente
     * @return un oggetto Eccezione che indica il risultato del login
     */
    public Eccezione Login(String email, String password) {
        Eccezione ecc;
        try {
            ecc = stub.login(email, CrittografiaPassword(password));
            if (ecc.getErrorCode() == 0) {
                this.email = email;
                this.passwordCriptata = CrittografiaPassword(password);
                idUtente = GetIdUtente(email);
                GetDati();
                return ecc;
            }
        } catch (Exception e) {
            return new Eccezione(5, "Remote Error" + e.getMessage());
        }
        if (ecc.getErrorCode() == 1)//utente non esiste
            return ecc;
        if (ecc.getErrorCode() == 2)//password errata
            return ecc;
        if (ecc.getErrorCode() > 2)
            return new Eccezione(4, "Errore SQL" + ecc.getMessage());
        return new Eccezione(20, "last");
    }

    /**
     * Esegue il login dell'utente utilizzando l'ID utente e la password.
     * Se il login ha successo, memorizza le informazioni dell'utente nell'istanza.
     *
     * @param idUtente l'ID dell'utente
     * @param password la password dell'utente
     * @return un oggetto Eccezione che indica il risultato del login
     */
    public Eccezione Login(int idUtente, String password) {

        Eccezione ecc;
        try {
            ecc = stub.login(idUtente, CrittografiaPassword(password));
            if (ecc.getErrorCode() == 0) {
                this.idUtente = idUtente;
                this.passwordCriptata = CrittografiaPassword(password);
                GetDati();
                return ecc;
            }

        } catch (Exception e) {
            return new Eccezione(5, "Remote Error" + e.getMessage());
        }
        if (ecc.getErrorCode() == 1)//utente non esiste
            return ecc;
        if (ecc.getErrorCode() == 2)//password errata
            return ecc;
        if (ecc.getErrorCode() > 2)
            return new Eccezione(4, "Errore SQL" + ecc.getMessage());
        return new Eccezione(20, "lasr");

    }

    /**
     * Recupera i dati dell'utente registrato in base all'ID utente.
     * Aggiorna le informazioni dell'utente nell'istanza.
     */
    public void GetDati() {
        try {
            String[] str = stub.GetUtenteRegistrato(idUtente);
            this.email = str[0];
            this.nome = str[1];
            this.cognome = str[2];
            this.codiceFiscale = str[3];
        } catch (Exception e) {
            PopupError.mostraErrore(e.getMessage());
        }
    }

    public String CrittografiaPassword(String password) {
        return PasswordUtils.crittografaPassword(password);
    }

    public boolean ControlloCodiceFiscale(String codiceFiscale) {
        if (codiceFiscale == null) return false;

        // Controllo lunghezza
        if (codiceFiscale.length() != 16) return false;

        // Controllo solo lettere e numeri
        if (!codiceFiscale.matches("^[A-Z0-9]+$")) return false;

        // Controllo formato: 6 lettere, 2 cifre, 1 lettera, 2 cifre, 1 lettera, 3 cifre, 1 lettera
        if (!codiceFiscale.matches("^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$")) return false;

        return true;
    }

    public void Logout() {
        idUtente = -1;
        email = null;
        passwordCriptata = null;
        codiceFiscale = null;
        nome = null;
        cognome = null;

    }

    /**
     * Rimuove l'utente dal sistema.
     * Se la rimozione ha successo, resetta le informazioni dell'utente nell'istanza.
     *
     * @return un oggetto Eccezione che indica il risultato della rimozione
     */
    public Eccezione RimuoviUtente() {
        try {
            Eccezione ecc = stub.RimuoviUtente(idUtente);
            if (ecc.getErrorCode() == 0) {
                idUtente = -1;
                email = null;
                passwordCriptata = null;
                codiceFiscale = null;
                nome = null;
                cognome = null;
                return ecc;
            }
            return ecc;
        } catch (Exception e) {
            return new Eccezione(5, "Remote Error" + e.getMessage());
        }
    }

    /**
     * Modifica la password dell'utente.
     *
     * @param password la nuova password da impostare
     * @return un oggetto Eccezione che indica il risultato della modifica
     */
    public Eccezione ModificaPassword(String password) {
        if (ControlloCaratteriPassword(password))
            password = CrittografiaPassword(password);
        else
            return new Eccezione(3, "Password non valida");
        try {
            Eccezione ecc = stub.ModificaPassword(idUtente, password);
            if (ecc.getErrorCode() == 0) {
                this.passwordCriptata = password;
                return ecc;
            }
            return ecc;
        } catch (Exception e) {
            return new Eccezione(5, "Remote Error" + e.getMessage());
        }
    }

    public boolean UtenteLoggato() {
        return idUtente != -1;
    }

    @Override
    public String toString() {
        return "UtenteGestore{" +
               "idUtente=" + idUtente +
               ", email='" + email + '\'' +
               ", passwordCriptata='" + passwordCriptata + '\'' +
               ", codiceFiscale='" + codiceFiscale + '\'' +
               ", nome='" + nome + '\'' +
               ", cognome='" + cognome + '\'' +
               '}';
    }


}
