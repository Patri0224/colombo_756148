package bookRecommender;

import bookRecommender.eccezioni.Eccezione;
import bookRecommender.rmi.ServerBookRecommenderInterface;
import graphics.PopupError;

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

    public Eccezione Login(String email, String password) {
        Eccezione ecc = null;
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
            e.printStackTrace();
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

    public Eccezione Login(int idUtente, String password) {

        Eccezione ecc = null;
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

    public void GetDati() {
        try {
            String[] str = stub.GetUtenteRegistrato(idUtente);
            this.email = str[0];
            this.nome = str[1];
            this.cognome = str[2];
            this.codiceFiscale = str[3];
        } catch (Exception e) {
            PopupError.mostraErrore(e.getMessage());
            e.printStackTrace();
        }
    }

    public String CrittografiaPassword(String password) {
        String hashed = PasswordUtils.crittografaPassword(password);
        return password; // Placeholder, replace with actual encryption logic
    }

    public boolean ControlloCodiceFiscale(String codiceFiscale) {
        // Implement codice fiscale validation logic here
        return true; // Placeholder, replace with actual validation logic
    }

    public void Logout() {
        idUtente = -1;
        email = null;
        passwordCriptata = null;
        codiceFiscale = null;
        nome = null;
        cognome = null;

    }

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
