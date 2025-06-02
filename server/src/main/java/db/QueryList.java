/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
package db;

import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueryList {
    private final ConnectionManager connectionManager;
    private final ThreadLocal<Connection> connThreadLocal = new ThreadLocal<>();

    public QueryList(ConnectionManager conMgr) {
        this.connectionManager = conMgr;
    }

    public void Connect() {
        try {
            connThreadLocal.set(connectionManager.getConnection());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void Disconnect() {
        try {
            Connection conn = connThreadLocal.get();
            if (conn != null) {
                connectionManager.endConnection(conn);
                connThreadLocal.remove(); // evita memory leak
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //utenti

    /**
     * Controlla se l'utente esiste nel database dato il suo id
     *
     * @param idUtente id dell'utente
     * @return vero se l'utente esiste, falso altrimenti
     * @throws SQLException da gestire fuori
     */
    public boolean ControlloEsisteUtente(int idUtente) throws SQLException {
        Connection conn = connThreadLocal.get();
        String sql = "SELECT EXISTS (SELECT 1 FROM UTENTI_REGISTRATI_PRINCIPALE WHERE utente_id = ?) AS esiste_utente";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("esiste_utente");
                }
            }
        }
        return false;
    }

    /**
     * Controlla se l'utente esiste nel database dato la sua email
     *
     * @param email email dell'utente
     * @return vero se l'utente esiste, falso altrimenti
     * @throws SQLException da gestire fuori
     */
    public boolean ControlloEsisteUtente(String email) throws SQLException {
        Connection conn = connThreadLocal.get();
        int id = GetIdUtenteDaEmail(email);
        if (id == -1) return false;
        return ControlloEsisteUtente(id);
    }

    /**
     * da email a idUtente
     *
     * @param email email dell'utente
     * @return l'id dell'utente se il esiste, -1 altrimenti
     * @throws SQLException da gestire fuori
     */
    public int GetIdUtenteDaEmail(String email) throws SQLException {
        Connection conn = connThreadLocal.get();
        String sql = "SELECT utente_id FROM UTENTI_REGISTRATI_PRINCIPALE WHERE mail = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("utente_id");
                }
            }
        }
        return -1; // Utente non trovato
    }

    /**
     * Controlla se la combinazione email e password vanno bene e restituisce l'id dell'utente
     *
     * @param email    email dell'utente
     * @param password password criptata
     * @return Eccezione con codice 0 e come messaggio l'id dell'utente, altrimenti il codice di errore
     * @throws SQLException da gestire fuori
     */
    public Eccezione ControlloPasswordUtente(String email, String password) throws SQLException {
        int id = GetIdUtenteDaEmail(email);
        if (id == -1) return new Eccezione(1, "Utente non esistente");
        return ControlloPasswordUtente(id, password);
    }

    /**
     * Controlla se la combinazione idUtente e password vanno bene e restituisce l'id dell'utente
     *
     * @param idUtente id dell'utente
     * @param password password criptata
     * @return Eccezione con codice 0 e come messaggio l'id dell'utente, altrimenti il codice di errore
     * @throws SQLException da gestire fuori
     */
    public Eccezione ControlloPasswordUtente(int idUtente, String password) throws SQLException {
        Connection conn = connThreadLocal.get();
        if (!ControlloEsisteUtente(idUtente)) return new Eccezione(1, "Utente non esistente");
        String sql = "SELECT passwd FROM UTENTI_REGISTRATI_PRINCIPALE WHERE utente_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("passwd");
                    if (storedPassword.equals(password)) {
                        return new Eccezione(0, "" + idUtente); // Password corretta
                    } else {
                        return new Eccezione(2, "Password errata");
                    }
                } else {
                    return new Eccezione(3, "errore interno");
                }
            }
        }
    }

    /**
     * Registrazione tramite parametri e ritorna l'id
     *
     * @param email         email dell'utente (max 100 caratteri)
     * @param password      password criptata (max 200 caratteri)
     * @param nome          nome dell'utente(max 100 caratteri)
     * @param cognome       cognome dell'utente(max 100 caratteri)
     * @param codiceFiscale codice fiscale dell'utente(max 16 caratteri)
     * @return Eccezione con codice 0 e come messaggio l'id dell'utente, altrimenti il codice di errore
     * @throws SQLException da gestire fuori per il controllo utente
     */
    public Eccezione Registrazione(String email, String password, String nome, String cognome, String codiceFiscale) throws SQLException {
        Connection conn = connThreadLocal.get();
        if (ControlloEsisteUtente(email)) return new Eccezione(1, "Utente già esistente");
        String sql = "INSERT INTO UTENTI_REGISTRATI_PRINCIPALE (mail, passwd) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted <= 0) {
                return new Eccezione(2, "Registrazione fallita a principale");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(4, "Conflitti di integrità: " + e.getMessage());
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
        int id = GetIdUtenteDaEmail(email);
        sql = "INSERT INTO UTENTI_REGISTRATI_DETTAGLI (utente_id, nome, cognome, codice_fiscale) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, nome);
            stmt.setString(3, cognome);
            stmt.setString(4, codiceFiscale);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                return new Eccezione(0, "" + id); //tutto ok
            } else {
                return new Eccezione(2, "Registrazione fallita nei dettagli");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(3, "Conflitti di integrità: " + e.getMessage());
        } catch (SQLException e) {
            return new Eccezione(4, "Errore SQL: " + e.getMessage());
        }
    }

    /**
     * Recupera i dettagli dell'utente registrato
     *
     * @param idUtente id dell'utente
     * @return un array di stringhe contenente l'email, nome, cognome e codice fiscale dell'utente
     * @throws SQLException da gestire fuori
     */
    public String[] GetUtenteRegistrato(int idUtente) throws SQLException {
        Connection conn = connThreadLocal.get();
        String sql = "SELECT p.mail, d.nome, d.cognome, d.codice_fiscale FROM UTENTI_REGISTRATI_DETTAGLI d JOIN UTENTI_REGISTRATI_PRINCIPALE p ON d.utente_id=p.utente_id WHERE d.utente_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new String[]{
                            rs.getString("mail"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("codice_fiscale")
                    };
                }
            }
        }

        return null; // Nessun utente trovato
    }

    /**
     * Modifica la password dell'utente
     *
     * @param idUtente id dell'utente
     * @param password nuova password criptata
     * @return Eccezione con codice 0 se tutto ok, altrimenti il codice di errore
     * @throws SQLException da gestire fuori per il controllo utente
     */
    public Eccezione ModificaPassword(int idUtente, String password) throws SQLException {
        Connection conn = connThreadLocal.get();
        if (!ControlloEsisteUtente(idUtente)) return new Eccezione(1, "Utente non esistente");
        String sql = "UPDATE UTENTI_REGISTRATI_PRINCIPALE SET passwd = ? WHERE utente_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, password);
            stmt.setInt(2, idUtente);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                return new Eccezione(0, ""); // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Modifica fallita");
            }
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    /**
     * Rimuove l'utente dal database
     *
     * @param idUtente id dell'utente
     * @return Eccezione con codice 0 se tutto ok, altrimenti il codice di errore
     * @throws SQLException da gestire fuori per il controllo utente
     */
    public Eccezione RimuoviUtente(int idUtente) throws SQLException {
        Connection conn = connThreadLocal.get();
        if (!ControlloEsisteUtente(idUtente)) return new Eccezione(1, "Utente non esistente");
        String sql = "DELETE FROM UTENTI_REGISTRATI_PRINCIPALE WHERE utente_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                return new Eccezione(0, ""); // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Rimozione fallita");
            }
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    //libri

    /**
     * Ricerca i libri in base ai parametri forniti
     *
     * @param titolo titolo del libro, null o "" se non specificato
     * @param autore autore del libro, null o "" se non specificato
     * @param anno   anno di pubblicazione, -1 se non specificato
     * @return un array di Libri che soddisfano i criteri di ricerca
     * @throws SQLException da gestire fuori
     */
    public Libri[] RicercaLibri(String titolo, String autore, int anno) throws SQLException {
        Connection conn = connThreadLocal.get();
        String dataSql = """
                SELECT p.libro_id, p.titolo, p.anno_pubblicazione, p.mese_pubblicazione, p.autori, d.descrizione, d.categorie, d.editori, d.prezzo
                FROM libri_principale p JOIN libri_dettagli d ON p.libro_id = d.libro_id
                WHERE (? IS NULL OR LOWER(p.titolo) LIKE LOWER(CONCAT('%', ?, '%')))
                AND (? IS NULL OR LOWER(p.autori) LIKE LOWER(CONCAT('%', ?, '%')))
                AND (? IS NULL OR p.anno_pubblicazione = ?)""";
        String countSql = """
                SELECT COUNT(*) FROM libri_principale p JOIN libri_dettagli d ON p.libro_id = d.libro_id
                WHERE (? IS NULL OR LOWER(p.titolo) LIKE LOWER(CONCAT('%', ?, '%')))
                AND (? IS NULL OR LOWER(p.autori) LIKE LOWER(CONCAT('%', ?, '%')))
                AND (? IS NULL OR p.anno_pubblicazione = ?)""";
        int count = 0;
        try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
            int idx = 1;
            idx = setStringParam(countStmt, idx, titolo);
            idx = setStringParam(countStmt, idx, autore);
            setIntParam(countStmt, idx, anno);

            try (ResultSet rsCount = countStmt.executeQuery()) {
                if (rsCount.next()) {
                    count = rsCount.getInt(1);
                }
            }
        }

        if (count == 0) {
            return new Libri[0];  // Nessun risultato
        }

        Libri[] risultati = new Libri[count];

        try (PreparedStatement dataStmt = conn.prepareStatement(dataSql)) {
            int idx = 1;
            idx = setStringParam(dataStmt, idx, titolo);
            idx = setStringParam(dataStmt, idx, autore);
            setIntParam(dataStmt, idx, anno);

            try (ResultSet rs = dataStmt.executeQuery()) {
                int i = 0;
                while (rs.next()) {
                    Libri libro = new Libri();
                    libro.setId(rs.getInt("libro_id"));
                    libro.setTitolo(rs.getString("titolo"));
                    libro.setAnnoPubblicazione(rs.getInt("anno_pubblicazione"));
                    libro.setMesePubblicazione(rs.getString("mese_pubblicazione"));
                    libro.setAutori(rs.getString("autori"));
                    libro.setDescrizione(rs.getString("descrizione"));
                    libro.setCategorie(rs.getString("categorie"));
                    libro.setEditore(rs.getString("editori"));
                    libro.setPrezzoPartenza(rs.getBigDecimal("prezzo").floatValue());

                    risultati[i] = libro;
                    i++;
                }
            }
        }

        return risultati;
    }

    /**
     * Ricerca i libri in base ai parametri forniti
     *
     * @param ids    array di id dei libri da cercare, se vuoto o null ritorna un array vuoto
     * @param titolo titolo del libro, null o "" se non specificato
     * @param autore autore del libro, null o "" se non specificato
     * @param anno   anno di pubblicazione, -1 se non specificato
     * @return un array di Libri che soddisfano i criteri di ricerca
     * @throws SQLException da gestire fuori
     */
    public Libri[] RicercaLibriDaIds(int[] ids, String titolo, String autore, int anno) throws SQLException {
        Connection conn = connThreadLocal.get();
        if (ids.length == 0) return new Libri[0];
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (int id : ids) {
            if (id < 0) i++;
            else str.append(id).append(", ");
        }
        str = new StringBuilder(str.substring(0, str.length() - 2));
        String dataSql = """
                SELECT p.libro_id, p.titolo, p.anno_pubblicazione, p.mese_pubblicazione, p.autori, d.descrizione, d.categorie, d.editori, d.prezzo
                FROM libri_principale p JOIN libri_dettagli d ON p.libro_id = d.libro_id
                WHERE (? IS NULL OR LOWER(p.titolo) LIKE LOWER(CONCAT('%', ?, '%')))
                AND (? IS NULL OR LOWER(p.autori) LIKE LOWER(CONCAT('%', ?, '%')))
                AND (? IS NULL OR p.anno_pubblicazione = ?)
                AND p.libro_id IN (""" + str + ")";
        String countSql = """
                SELECT COUNT(*) FROM libri_principale p JOIN libri_dettagli d ON p.libro_id = d.libro_id
                WHERE (? IS NULL OR LOWER(p.titolo) LIKE LOWER(CONCAT('%', ?, '%')))
                AND (? IS NULL OR LOWER(p.autori) LIKE LOWER(CONCAT('%', ?, '%')))
                AND (? IS NULL OR p.anno_pubblicazione = ?)
                AND p.libro_id IN (""" + str + ")";
        int count = 0;
        try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
            int idx = 1;
            idx = setStringParam(countStmt, idx, titolo);
            idx = setStringParam(countStmt, idx, autore);
            setIntParam(countStmt, idx, anno);

            try (ResultSet rsCount = countStmt.executeQuery()) {
                if (rsCount.next()) {
                    count = rsCount.getInt(1);
                }
            }
        }

        if (count == 0) {
            return new Libri[0];  // Nessun risultato
        }

        Libri[] risultati = new Libri[count];

        try (PreparedStatement dataStmt = conn.prepareStatement(dataSql)) {
            int idx = 1;
            idx = setStringParam(dataStmt, idx, titolo);
            idx = setStringParam(dataStmt, idx, autore);
            setIntParam(dataStmt, idx, anno);

            try (ResultSet rs = dataStmt.executeQuery()) {
                i = 0;
                while (rs.next()) {
                    Libri libro = new Libri();
                    libro.setId(rs.getInt("libro_id"));
                    libro.setTitolo(rs.getString("titolo"));
                    libro.setAnnoPubblicazione(rs.getInt("anno_pubblicazione"));
                    libro.setMesePubblicazione(rs.getString("mese_pubblicazione"));
                    libro.setAutori(rs.getString("autori"));
                    libro.setDescrizione(rs.getString("descrizione"));
                    libro.setCategorie(rs.getString("categorie"));
                    libro.setEditore(rs.getString("editori"));
                    libro.setPrezzoPartenza(rs.getBigDecimal("prezzo").floatValue());

                    risultati[i] = libro;
                    i++;
                }
            }
        }

        return risultati;
    }

    /**
     * Ricerca i libri in base ai parametri forniti
     *
     * @param titolo    titolo del libro, null o "" se non specificato
     * @param autore    autore del libro, null o "" se non specificato
     * @param anno      anno di pubblicazione, -1 se non specificato
     * @param editore   editore del libro, null o "" se non specificato
     * @param categoria categoria del libro, null o "" se non specificato
     * @param prezzoMin prezzo minimo del libro, -1 se non specificato
     * @param prezzoMax prezzo massimo del libro, -1 se non specificato
     * @return un array di Libri che soddisfano i criteri di ricerca
     * @throws SQLException da gestire fuori
     */
    public Libri[] RicercaLibri(String titolo, String autore, int anno, String editore, String categoria, float prezzoMin, float prezzoMax) throws SQLException {
        Connection conn = connThreadLocal.get();
        String dataSql = """
                SELECT p.libro_id, p.titolo, p.anno_pubblicazione, p.mese_pubblicazione, p.autori,
                       d.descrizione, d.categorie, d.editori, d.prezzo
                FROM libri_principale p
                JOIN libri_dettagli d ON p.libro_id = d.libro_id
                WHERE (? IS NULL OR LOWER(p.titolo) LIKE LOWER(CONCAT('%', ?, '%')))
                  AND (? IS NULL OR LOWER(p.autori) LIKE LOWER(CONCAT('%', ?, '%')))
                  AND (? IS NULL OR p.anno_pubblicazione = ?)
                  AND (? IS NULL OR LOWER(d.editori) LIKE LOWER(CONCAT('%', ?, '%')))
                  AND (? IS NULL OR LOWER(d.categorie) LIKE LOWER(CONCAT('%', ?, '%')))
                  AND (? IS NULL OR d.prezzo >= ?)
                  AND (? IS NULL OR d.prezzo <= ?)
                """;

        String countSql = """
                SELECT COUNT(*)
                FROM libri_principale p
                JOIN libri_dettagli d ON p.libro_id = d.libro_id
                WHERE (? IS NULL OR LOWER(p.titolo) LIKE LOWER(CONCAT('%', ?, '%')))
                  AND (? IS NULL OR LOWER(p.autori) LIKE LOWER(CONCAT('%', ?, '%')))
                  AND (? IS NULL OR p.anno_pubblicazione = ?)
                  AND (? IS NULL OR LOWER(d.editori) LIKE LOWER(CONCAT('%', ?, '%')))
                  AND (? IS NULL OR LOWER(d.categorie) LIKE LOWER(CONCAT('%', ?, '%')))
                  AND (? IS NULL OR d.prezzo >= ?)
                  AND (? IS NULL OR d.prezzo <= ?)
                """;

        int count = 0;

        try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
            int idx = 1;
            idx = setStringParam(countStmt, idx, titolo);
            idx = setStringParam(countStmt, idx, autore);
            idx = setIntParam(countStmt, idx, anno);
            idx = setStringParam(countStmt, idx, editore);
            idx = setStringParam(countStmt, idx, categoria);
            idx = setFloatParam(countStmt, idx, prezzoMin);
            setFloatParam(countStmt, idx, prezzoMax);

            try (ResultSet rsCount = countStmt.executeQuery()) {
                if (rsCount.next()) {
                    count = rsCount.getInt(1);
                }
            }
        }

        if (count == 0) return new Libri[0];

        Libri[] risultati = new Libri[count];

        try (PreparedStatement dataStmt = conn.prepareStatement(dataSql)) {
            int idx = 1;
            idx = setStringParam(dataStmt, idx, titolo);
            idx = setStringParam(dataStmt, idx, autore);
            idx = setIntParam(dataStmt, idx, anno);
            idx = setStringParam(dataStmt, idx, editore);
            idx = setStringParam(dataStmt, idx, categoria);
            idx = setFloatParam(dataStmt, idx, prezzoMin);
            setFloatParam(dataStmt, idx, prezzoMax);

            try (ResultSet rs = dataStmt.executeQuery()) {
                int i = 0;
                while (rs.next()) {
                    Libri libro = new Libri();
                    libro.setId(rs.getInt("libro_id"));
                    libro.setTitolo(rs.getString("titolo"));
                    libro.setAnnoPubblicazione(rs.getInt("anno_pubblicazione"));
                    libro.setMesePubblicazione(rs.getString("mese_pubblicazione"));
                    libro.setAutori(rs.getString("autori"));
                    libro.setDescrizione(rs.getString("descrizione"));
                    libro.setCategorie(rs.getString("categorie"));
                    libro.setEditore(rs.getString("editori"));
                    libro.setPrezzoPartenza(rs.getBigDecimal("prezzo").floatValue());

                    risultati[i++] = libro;
                }
            }
        }

        return risultati;
    }

    private int setStringParam(PreparedStatement stmt, int idx, String val) throws SQLException {
        if (val == null || val.isEmpty()) {
            stmt.setNull(idx++, Types.VARCHAR);
            stmt.setNull(idx++, Types.VARCHAR);
        } else {
            stmt.setString(idx++, val);
            stmt.setString(idx++, val);
        }
        return idx;
    }

    private int setIntParam(PreparedStatement stmt, int idx, int val) throws SQLException {
        if (val == -1) {
            stmt.setNull(idx++, Types.INTEGER);
            stmt.setNull(idx++, Types.INTEGER);
        } else {
            stmt.setInt(idx++, val);
            stmt.setInt(idx++, val);
        }
        return idx;
    }

    private int setFloatParam(PreparedStatement stmt, int idx, float val) throws SQLException {
        if (val == -1) {
            stmt.setNull(idx++, Types.FLOAT);
            stmt.setNull(idx++, Types.FLOAT);
        } else {
            stmt.setFloat(idx++, val);
            stmt.setFloat(idx++, val);
        }
        return idx;
    }

    /**
     * Restituisce l'array libri dati gli id di quei libri
     *
     * @param ids array di id dei libri da cercare
     * @return un array di Libri senza ripetizioni
     * @throws SQLException da gestire fuori
     */
    public Libri[] RicercaLibriDaIds(int[] ids) throws SQLException {
        Connection conn = connThreadLocal.get();

        if (ids.length == 0) return new Libri[0];
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (int id : ids) {
            if (id < 0) i++;
            else str.append(id).append(", ");
        }
        if(str.isEmpty()) return new Libri[0];
        str = new StringBuilder(str.substring(0, str.length() - 2));
        String dataSql = """
                SELECT p.libro_id, p.titolo, p.anno_pubblicazione, p.mese_pubblicazione, p.autori, d.descrizione, d.categorie, d.editori, d.prezzo
                FROM libri_principale p JOIN libri_dettagli d ON p.libro_id = d.libro_id
                WHERE p.libro_id IN (""" + str + ")";
        Libri[] risultati = new Libri[ids.length - i];
        i = 0;
        try (PreparedStatement dataStmt = conn.prepareStatement(dataSql)) {
            try (ResultSet rs = dataStmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("libro_id");
                    String titolo = (rs.getString("titolo"));
                    int anno = (rs.getInt("anno_pubblicazione"));
                    String mese = (rs.getString("mese_pubblicazione"));
                    String aut = (rs.getString("autori"));
                    String desc = (rs.getString("descrizione"));
                    String cat = (rs.getString("categorie"));
                    String editor = (rs.getString("editori"));
                    float prezzo = (rs.getBigDecimal("prezzo").floatValue());
                    risultati[i++] = new Libri(id, titolo, aut, desc, cat, editor, prezzo, mese, anno);
                }
            }
        }
        Libri[] risultatiFinali = new Libri[i];
        System.arraycopy(risultati, 0, risultatiFinali, 0, i);
        return risultatiFinali;
    }

    //librerie

    /**
     * Restituisce tutte le librerie di un utente, le librerie contengono gli id dei libri presenti in esse
     *
     * @param idUtente id dell'utente
     * @return un array di Librerie
     * @throws SQLException da gestire fuori
     */
    public Librerie[] RicercaLibrerie(int idUtente) throws SQLException {
        Connection conn = connThreadLocal.get();
        ArrayList<Librerie> librerie = new ArrayList<>();
        String sql = """
                SELECT l.libreria_id,l.nome_libreria,c.libro_id
                FROM librerie l LEFT JOIN contenuto_libreria c ON l.libreria_id = c.libreria_id
                WHERE l.utente_id = ?""";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idLibreria = rs.getInt("libreria_id");
                    String nomeLibreria = rs.getString("nome_libreria");
                    int idLibro = rs.getInt("libro_id");
                    boolean esistente = false;
                    for (Librerie lib : librerie) {
                        if (lib.getIdLibreria() == idLibreria && idLibro != 0) {
                            lib.aggiungiLibroInLocale(idLibro);
                            esistente = true;
                        }
                    }
                    if (!esistente) {
                        Librerie lib = new Librerie(idLibreria, nomeLibreria, new ArrayList<>());
                        if (idLibro != 0)
                            lib.aggiungiLibroInLocale(idLibro);
                        librerie.add(lib);

                    }
                }
                return librerie.toArray(new Librerie[0]);
            }
        }

    }

    /**
     * Restituisce i libri presenti nelle librerie di un utente
     *
     * @param idUtente id dell'utente
     * @return un array di Libri
     * @throws SQLException da gestire fuori
     */
    public Libri[] RicercaLibriDaLibrerie(int idUtente,String titoloRicerca, String autoreRicerca, int annoR) throws SQLException {
        Connection conn = connThreadLocal.get();
        String sql = """
                SELECT c.libro_id
                FROM librerie l JOIN contenuto_libreria c ON l.libreria_id = c.libreria_id
                WHERE l.utente_id = ?""";
        ArrayList<Integer> ids = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int libroId = rs.getInt("libro_id");
                    if (libroId != 0)
                        ids.add(libroId);
                }
            }
        }
        if (ids.isEmpty()) return new Libri[0];

        int[] uniqueIds = ids.stream().distinct().mapToInt(i -> i).toArray();
        Libri[] libri = RicercaLibriDaIds(uniqueIds, titoloRicerca, autoreRicerca, annoR);
        return libri;
    }

    /**
     * Controlla se la libreria esiste nel database
     *
     * @param idUtente     id dell'utente
     * @param nomeLibreria nome della libreria
     * @return vero se la libreria esiste, falso altrimenti
     * @throws SQLException da gestire fuori
     */
    public boolean ControlloEsisteLibreria(int idUtente, String nomeLibreria) throws SQLException {
        Connection conn = connThreadLocal.get();
        String sql = "SELECT EXISTS (SELECT 1 FROM librerie WHERE utente_id = ? AND nome_libreria = ?) AS esiste_libreria";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.setString(2, nomeLibreria);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("esiste_libreria");
                }
            }
        }
        return false;
    }

    /**
     * Controlla se il libro esiste nella libreria dell'utente
     *
     * @param idUtente id dell'utente
     * @param idLibro  id del libro
     * @return vero se il libro esiste, falso altrimenti
     * @throws SQLException da gestire fuori
     */
    public boolean ControlloLibroInLibrerie(int idUtente, int idLibro) throws SQLException {
        Connection conn = connThreadLocal.get();
        String sql = "SELECT EXISTS (SELECT 1 FROM contenuto_libreria WHERE libreria_id IN (SELECT libreria_id FROM librerie WHERE utente_id = ?) AND libro_id = ?) AS esiste_libro";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idLibro);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("esiste_libro");
                }
            }
        }
        return false;
    }

    /**
     * aggiunge una libreria senza libri al database
     *
     * @param idUtente     id dell'utente
     * @param nomeLibreria nome della libreria
     * @return Eccezione con codice 0 e come messaggio l'id della libreria, altrimenti il codice di errore
     */
    public Eccezione AggiungiLibreria(int idUtente, String nomeLibreria) {
        Connection conn = connThreadLocal.get();
        String sql = "INSERT INTO librerie (utente_id, nome_libreria) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idUtente);
            stmt.setString(2, nomeLibreria);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted == 0) {
                return new Eccezione(2, "Registrazione fallita nei dettagli");
            }

            // Ottieni il libreria_id generato
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Eccezione(0, "" + generatedKeys.getInt(1));
                } else {
                    return new Eccezione(4, "ID libreria non ottenuto");
                }
            }


        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(1, "Conflitti di integrità: " + e.getMessage());
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    /**
     * aggiunge un libro alla libreria
     *
     * @param idLibreria id della libreria
     * @param idLibro    id del libro
     * @return Eccezione con codice 0 se tutto ok, altrimenti il codice di errore
     */
    public Eccezione AggiungiLibroALibreria(int idLibreria, int idLibro) {
        Connection conn = connThreadLocal.get();
        String sql = "INSERT INTO contenuto_libreria (libreria_id, libro_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLibreria);
            stmt.setInt(2, idLibro);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                return new Eccezione(0, ""); // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Aggiunta fallita");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(1, "Conflitti di integrità: " + e.getMessage());
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    /**
     * Rimuove un libro dalla libreria
     *
     * @param idLibreria id della libreria
     * @param idLibro    id del libro
     * @return Eccezione con codice 0 se tutto ok, altrimenti il codice di errore
     */
    public Eccezione RimuoviLibroDaLibreria(int idLibreria, int idLibro) {
        Connection conn = connThreadLocal.get();
        String sql = "DELETE FROM contenuto_libreria WHERE libreria_id = ? AND libro_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLibreria);
            stmt.setInt(2, idLibro);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                return new Eccezione(0, ""); // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Rimozione fallita");
            }
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    /**
     * Rimuove una libreria dal database
     *
     * @param idLibreria id della libreria
     * @return Eccezione con codice 0 se tutto ok, altrimenti il codice di errore
     */
    public Eccezione RimuoviLibreria(int idLibreria) {
        Connection conn = connThreadLocal.get();
        String sql = "DELETE FROM librerie WHERE libreria_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLibreria);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                return new Eccezione(0, ""); // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Rimozione fallita");
            }
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    //consigli

    /**
     * Aggiunge un consiglio al database
     *
     * @param consiglio oggetto ConsigliLibri contenente i dati del consiglio
     * @return Eccezione con codice 0 se tutto ok, 2 aggiunta fallita, 1 conflitti di integrità, 3 errore SQL,4 nessun libro è stato consigliato
     */
    public Eccezione AggiungiConsiglio(ConsigliLibri consiglio) {
        Connection conn = connThreadLocal.get();
        int[] idLibri = consiglio.getConsigliLibri();
        boolean empty = true;
        for (int l : idLibri) {
            if (l != -1) {
                empty = false;
                break;
            }
        }
        if (empty) return new Eccezione(4, "nessun libro è stato consigliato");
        String sql = "INSERT INTO consigli (utente_id, libro_id_riguardante) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, consiglio.getIdUtente());
            stmt.setInt(2, consiglio.getIdLibro());
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted <= 0) {
                return new Eccezione(2, "Aggiunta fallita tabella consigli");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(1, "Conflitti di integrità tabella consigli: " + e.getMessage());
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL tabella consigli: " + e.getMessage());
        }

        sql = "INSERT INTO contenuto_consiglio (utente_id, libro_id_riguardante, libro_id_consigliato) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int libroId : consiglio.getConsigliLibri()) {
                if (libroId != -1) {
                    stmt.setInt(1, consiglio.getIdUtente());
                    stmt.setInt(2, consiglio.getIdLibro());
                    stmt.setInt(3, libroId);
                    stmt.addBatch();
                }
            }
            int[] rowsInserted = stmt.executeBatch();
            int rowsCount = 1;
            for (int rows : rowsInserted) {
                if (rows == 0) {
                    rowsCount--;
                }
            }
            if (rowsCount > 0) {
                return new Eccezione(0, "aggiunta consiglio con libri"); // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Aggiunta fallita tabella contenuto_consiglio");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(1, "Conflitti di integrità tabella contenuto_consiglio: " + e.getMessage());
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL tabella contenuto_consiglio: " + e.getMessage());
        }
    }

    /**
     * Aggiunge un libro a un consiglio esistente
     *
     * @param idUtente           id dell'utente
     * @param idRiguardante      id del libro riguardante
     * @param idLibroConsigliato id del libro consigliato
     * @return Eccezione con codice 0 se tutto ok, 1 se ci sono conflitti di integrità, 2 se l'aggiunta fallisce, 3 se c'è un errore SQL
     */
    public Eccezione AggiungiLibroAConsiglio(int idUtente, int idRiguardante, int idLibroConsigliato) {
        Connection conn = connThreadLocal.get();
        String sql = "INSERT INTO contenuto_consiglio (utente_id, libro_id_riguardante, libro_id_consigliato) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idRiguardante);
            stmt.setInt(3, idLibroConsigliato);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                return new Eccezione(0, ""); // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Aggiunta fallita");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(1, "Conflitti di integrità: " + e.getMessage());
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    /**
     * Restituisce i libri consigliati da parte di tutti gli utenti per un libro specifico tenendo i duplicati
     *
     * @param idLibro id del libro
     * @return un array di Libri consigliati
     * @throws SQLException da gestire fuori
     */
    public Libri[] RicercaConsigliDatoLibro(int idLibro) throws SQLException {
        Connection conn = connThreadLocal.get();
        String sql = "SELECT libro_id_consigliato FROM contenuto_consiglio WHERE libro_id_riguardante = ?";
        ArrayList<Integer> ids = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLibro);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("libro_id_consigliato"));
                }
            }
        }
        if (ids.isEmpty()) return new Libri[0];

        int[] uniqueIds = ids.stream().distinct().mapToInt(i -> i).toArray();
        Libri[] libri = RicercaLibriDaIds(uniqueIds);

        //restituisce i libri anche duplicati
        Map<Integer, Libri> idToLibro = new HashMap<>();
        for (Libri libro : libri) {
            idToLibro.put(libro.getId(), libro); // Supponendo getId() esista
        }

        Libri[] risultato = new Libri[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            risultato[i] = idToLibro.get(ids.get(i));
        }

        return risultato;
    }

    /**
     * Restituisce i libri consigliati da parte di un utente per un libro specifico
     *
     * @param idUtente id dell'utente
     * @param idLibro  id del libro
     * @return un oggetto ConsigliLibri contenente i libri consigliati
     * @throws SQLException da gestire fuori
     */
    public ConsigliLibri RicercaConsigliDatoUtenteELibro(int idUtente, int idLibro) throws SQLException {
        Connection conn = connThreadLocal.get();
        String sql = "SELECT libro_id_consigliato FROM contenuto_consiglio WHERE utente_id = ? AND libro_id_riguardante = ?";
        ConsigliLibri consiglio = new ConsigliLibri(idLibro, idUtente);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idLibro);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int libroIdConsigliato = rs.getInt("libro_id_consigliato");
                    consiglio.aggiungiConsiglio(libroIdConsigliato);
                }
            }
        }
        return consiglio;
    }

    //valutazione

    /**
     * Aggiunge una valutazione al database
     *
     * @param v oggetto ValutazioniLibri contenente i dati della valutazione
     * @return Eccezione con codice 0 se tutto ok, 1 se ci sono conflitti di integrità, 2 se l'aggiunta fallisce, 3 se c'è un errore SQL
     */
    public Eccezione AggiungiValutazione(ValutazioniLibri v) {
        Connection conn = connThreadLocal.get();
        String sql = """
                    INSERT INTO recensioni (
                        utente_id, libro_id,
                        note_contenuto, note_stile, note_gradevolezza, note_originalita, note_edizione, note_generale,
                        contenuto, stile, gradevolezza, originalita, edizione
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // ID utente e libro
            stmt.setInt(1, v.getIdUtente());
            stmt.setInt(2, v.getIdLibro());

            String[] note = v.getNote();
            short[] punteggi = v.getPunteggi();

            // Le note (default null se mancanti)
            stmt.setString(3, note[1]);  // contenuto
            stmt.setString(4, note[0]);  // stile
            stmt.setString(5, note[2]);  // gradevolezza
            stmt.setString(6, note[3]);  // originalità
            stmt.setString(7, note[4]);  // edizione
            stmt.setString(8, note[5]);  // generale

            // I punteggi (valori tra 1 e 5)
            stmt.setShort(9, punteggi[1]);  // contenuto
            stmt.setShort(10, punteggi[0]); // stile
            stmt.setShort(11, punteggi[2]); // gradevolezza
            stmt.setShort(12, punteggi[3]); // originalità
            stmt.setShort(13, punteggi[4]); // edizione

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                return new Eccezione(0, "tutto ok"); // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Aggiunta fallita");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(1, "Conflitti di integrità: " + e.getMessage());
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    /**
     * Ricerca la valutazione di un libro fatta da un singolo utente
     *
     * @param idUtente id dell'utente
     * @param idLibro  id del libro
     * @return ValutazioniLibri con la valutazione dell'utente
     * @throws SQLException eccezione da gestire fuori
     */
    public ValutazioniLibri RicercaValutazione(int idUtente, int idLibro) throws SQLException {
        Connection conn = connThreadLocal.get();
        String sql = "SELECT * FROM recensioni WHERE utente_id = ? AND libro_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idLibro);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    short[] punteggi = new short[6];
                    String[] note = new String[6];

                    // NOTE: ordine coerente con Score
                    note[1] = rs.getString("note_contenuto");
                    note[0] = rs.getString("note_stile");
                    note[2] = rs.getString("note_gradevolezza");
                    note[3] = rs.getString("note_originalita");
                    note[4] = rs.getString("note_edizione");
                    note[5] = rs.getString("note_generale");

                    punteggi[1] = rs.getShort("contenuto");
                    punteggi[0] = rs.getShort("stile");
                    punteggi[2] = rs.getShort("gradevolezza");
                    punteggi[3] = rs.getShort("originalita");
                    punteggi[4] = rs.getShort("edizione");
                    punteggi[5] = (short) ((punteggi[1] + punteggi[0] + punteggi[2] + punteggi[3] + punteggi[4]) / 5);

                    Score score = new Score(punteggi, note);
                    return new ValutazioniLibri(idUtente,idLibro, score);
                }
            }
        }
        return null;
    }

    /**
     * Ricerca la valutazione media di un libro
     *
     * @param idLibro id del libro su qui si vuole la media
     * @return ValutazioniLibri con la media delle valutazioni
     * @throws SQLException eccezione da gestire fuori
     */
    public ValutazioniLibri RicercaValutazioneMedia(int idLibro) throws SQLException {
        Connection conn = connThreadLocal.get();
        String sql = "SELECT AVG(contenuto) AS avg_contenuto, AVG(stile) AS avg_stile, AVG(gradevolezza) AS avg_gradevolezza, AVG(originalita) AS avg_originalita, AVG(edizione) AS avg_edizione FROM recensioni WHERE libro_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLibro);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    short[] punteggi = new short[6];
                    String[] note = new String[6];

                    // NOTE: ordine coerente con Score
                    note[1] = "Media contenuto";
                    note[0] = "Media stile";
                    note[2] = "Media gradevolezza";
                    note[3] = "Media originalità";
                    note[4] = "Media edizione";
                    note[5] = "Media generale";

                    punteggi[1] = rs.getShort("avg_contenuto");
                    punteggi[0] = rs.getShort("avg_stile");
                    punteggi[2] = rs.getShort("avg_gradevolezza");
                    punteggi[3] = rs.getShort("avg_originalita");
                    punteggi[4] = rs.getShort("avg_edizione");
                    punteggi[5] = (short) ((punteggi[1] + punteggi[0] + punteggi[2] + punteggi[3] + punteggi[4]) / 5);

                    Score score = new Score(punteggi, note);
                    return new ValutazioniLibri(-1, idLibro, score);
                }
            }
        }
        return null;
    }
}
