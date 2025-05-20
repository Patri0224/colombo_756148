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
    private ServerBookRecommenderInterface stub;

    public UtenteGestore(ServerBookRecommenderInterface stub) {
        // Default constructor
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
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        if (ControlloCodiceFiscale(codiceFiscale))
            this.codiceFiscale = codiceFiscale;
        else
            return new Eccezione(2, "Codice fiscale non valido");
        if (ControlloCaratteriPassword(password))
            this.passwordCriptata = CrittografiaPassword(password);
        else
            return new Eccezione(3, "Password non valida");

        Eccezione ecc = null;
        try {
            ecc = stub.Registrazione(email, password, codiceFiscale, nome, cognome);
            if (ecc == null) {
                idUtente = GetIdUtente(email);
                return null;
            }
        } catch (Exception e) {
            return new Eccezione(5, "Remote Error" + e.getMessage());
        }
        if (ecc.getErrorCode() == 1)//utente esiste già
            return ecc;
        if (ecc.getErrorCode() > 1)
            return new Eccezione(4, "Errore SQL" + ecc.getMessage());
        else
            return null;

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

    public Eccezione login(String email,String password){

        this.email = email;
        this.passwordCriptata = CrittografiaPassword(password);
        Eccezione ecc = null;
        try {
            ecc = stub.login(email, password);
            if (ecc == null) {
                idUtente = GetIdUtente(email);
                GetDati();
                return null;
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
        return null;
    }
    public Eccezione Login(int idUtente, String password) {

        this.idUtente = idUtente;
        this.passwordCriptata = CrittografiaPassword(password);
        Eccezione ecc = null;
        try {
            ecc = stub.login(idUtente, password);
            if (ecc == null) {
                GetDati();
                return null;
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
        return null;

    }

    private void GetDati() {
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


}
