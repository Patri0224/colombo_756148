package bookRecommender;

import bookRecommender.eccezioni.Eccezione;
import bookRecommender.rmi.ServerBookRecommenderInterface;

public class UtenteGestore {
    private int idUtente = -1;
    private String email;
    private String passwordCriptata;
    private String codiceFiscale;
    private String nome;
    private String cognome;
    private final ServerBookRecommenderInterface stub;

    public UtenteGestore(ServerBookRecommenderInterface stub) {
        this.stub = stub;
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
        if (ControlloCodiceFiscale(codiceFiscale))
            codiceFiscale = codiceFiscale;
        else
            return new Eccezione(2, "Codice fiscale non valido");
        if (ControlloCaratteriPassword(password))
            password = CrittografiaPassword(password);
        else
            return new Eccezione(3, "Password non valida");

        Eccezione ecc = null;
        try {
            ecc = stub.Registrazione(email, password, nome, cognome, codiceFiscale);
            if (ecc.getErrorCode() == 0) {
                idUtente = GetIdUtente(email);
                this.email = email;
                this.nome = nome;
                this.cognome = cognome;
                this.codiceFiscale = codiceFiscale;
                this.passwordCriptata = password;
                return ecc;
            }
        } catch (Exception e) {
            return new Eccezione(5, "Remote Error" + e.getMessage());
        }
        if (ecc.getErrorCode() == 1)//utente esiste già
            return ecc;
        if (ecc.getErrorCode() > 1)
            return new Eccezione(4, "Errore SQL" + ecc.getMessage());
        else
            return new Eccezione(20, "last");

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
                return -1;
            }
        }
    }

    public Eccezione Login(String email, String password) {
        Eccezione ecc = null;
        try {
            ecc = stub.login(email, password);
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

    public Eccezione Login(int idUtente, String password) {

        Eccezione ecc = null;
        try {
            ecc = stub.login(idUtente, password);
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
            e.printStackTrace();
        }
    }

    public String CrittografiaPassword(String password) {
        // Implement password encryption logic here
        return password; // Placeholder, replace with actual encryption logic
    }

    public boolean ControlloCodiceFiscale(String codiceFiscale) {
        // Implement codice fiscale validation logic here
        return true; // Placeholder, replace with actual validation logic
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
