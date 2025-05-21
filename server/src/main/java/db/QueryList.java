package db;

import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueryList {
    private final ConnectionManager connectionManager;
    private Connection conn;

    public QueryList(ConnectionManager conMgr) {
        this.connectionManager = conMgr;

    }

    public void Connect() {
        try {
            conn = connectionManager.getConnection();
        } catch (SQLException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void Disconnect() {
        try {
            connectionManager.endConnection(conn);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    //utenti
    public boolean ControlloEsisteUtente(int idUtente) throws SQLException {
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

    public boolean ControlloEsisteUtente(String email) throws SQLException {
        int id = GetIdUtenteDaEmail(email);
        if (id == -1) return false;
        return ControlloEsisteUtente(id);
    }

    public int GetIdUtenteDaEmail(String email) throws SQLException {
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

    public Eccezione ControlloPasswordUtente(String email, String password) throws SQLException {
        int id = GetIdUtenteDaEmail(email);
        if (id == -1) return new Eccezione(1, "Utente non esistente");
        return ControlloPasswordUtente(id, password);
    }

    public Eccezione ControlloPasswordUtente(int idUtente, String password) throws SQLException {
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
     * @param email
     * @param password
     * @param nome
     * @param cognome
     * @param codiceFiscale
     * @return Eccezione con codice 0 e come messaggio l'id dell'utente, altrimenti il codice di errore
     * @throws SQLException
     */
    public Eccezione Registrazione(String email, String password, String nome, String cognome, String codiceFiscale) throws SQLException {
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

    public String[] GetUtenteRegistrato(int idUtente) throws SQLException {
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

    //pass non criptata
    public Eccezione ModificaPassword(int idUtente, String password) throws SQLException {
        if (!ControlloEsisteUtente(idUtente)) return new Eccezione(1, "Utente non esistente");
        String sql = "UPDATE UTENTI_REGISTRATI_PRINCIPALE SET passwd = ? WHERE utente_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, password);
            stmt.setInt(2, idUtente);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                return null; // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Modifica fallita");
            }
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    public Eccezione RimuoviUtente(int idUtente) throws SQLException {
        if (!ControlloEsisteUtente(idUtente)) return new Eccezione(1, "Utente non esistente");
        String sql = "DELETE FROM UTENTI_REGISTRATI_PRINCIPALE WHERE utente_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                return null; // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Rimozione fallita");
            }
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    //libri
    public Libri[] RicercaLibri(String titolo, String autore, int anno) throws SQLException {

        //test
       /* if (anno == 0) {
            Libri[] l = new Libri[2];
            l[0] = new Libri(1, "titolo", "autore", "descrizione", "categorie", "editori", 10.0f, "mese", 2023);
            l[1] = new Libri(2, "titolo", "autore", "descrizione", "categorie", "editori", 10.0f, "mese", 2023);
            return l;
        }*/
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
            idx = setIntParam(countStmt, idx, anno);

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
            idx = setIntParam(dataStmt, idx, anno);

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

    public Libri[] RicercaLibri(String titolo, String autore, int anno, String editore, String categoria, float prezzoMin, float prezzoMax) throws SQLException {
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
            idx = setFloatParam(countStmt, idx, prezzoMax);

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
            idx = setFloatParam(dataStmt, idx, prezzoMax);

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
        if (val == null) {
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

    public Libri[] RicercaLibriDaIds(int[] ids) throws SQLException {

        if (ids.length == 0) return new Libri[0];
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (int id : ids) {
            str.append(id).append(", ");
            if (id < 0) i++;
        }
        str = new StringBuilder(str.substring(0, str.length() - 2));
        String dataSql = """
                SELECT p.libro_id, p.titolo, p.anno_pubblicazione, p.mese_pubblicazione, p.autori, d.descrizione, d.categorie, d.editori, d.prezzo
                FROM libri_principale p JOIN libri_dettagli d ON p.libro_id = d.libro_id
                WHERE p.libro_id IN (""" + str + ")";
        Libri[] risultati = new Libri[ids.length - i];

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
                    risultati[i] = new Libri(id, titolo, aut, desc, cat, editor, prezzo, mese, anno);
                }
            }
        }
        return risultati;
    }

    //librerie
    public Librerie[] RicercaLibrerie(int idUtente) throws SQLException {
        ArrayList<Librerie> librerie = new ArrayList<>();
        String sql = """
                SELECT l.libreria_id,l.nome_libreria,c.libro_id
                FROM librerie l JOIN contenuto_libreria c ON l.libreria_id = c.libreria_id
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
                        if (lib.getIdLibreria() == idLibreria) {
                            lib.aggiungiLibroInLocale(idLibro);
                            esistente = true;
                        }
                    }
                    if (!esistente) {
                        Librerie lib = new Librerie(idLibreria, nomeLibreria, new ArrayList<>());
                        lib.aggiungiLibroInLocale(idLibro);
                        librerie.add(lib);

                    }
                }
                return librerie.toArray(new Librerie[0]);
            }
        }

    }

    public Libri[] RicercaLibriDaLibrerie(int idUtente) throws SQLException {
        String sql = """
                SELECT c.libro_id
                FROM librerie l JOIN contenuto_libreria c ON l.libreria_id = c.libreria_id
                WHERE l.utente_id = ?""";
        ArrayList<Integer> ids = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("libro_id"));
                }
            }
        }
        if (ids.isEmpty()) return new Libri[0];

        int[] uniqueIds = ids.stream().distinct().mapToInt(i -> i).toArray();
        return RicercaLibriDaIds(uniqueIds);
    }

    public boolean ControlloEsisteLibreria(int idUtente, String nomeLibreria) throws SQLException {
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

    public boolean ControlloLibroInLibrerie(int idUtente, int idLibro) throws SQLException {
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
     * @param idUtente
     * @param nomeLibreria
     * @return Eccezione con codice 0 e come messaggio l'id della libreria, altrimenti il codice di errore
     */
    public Eccezione AggiungiLibreria(int idUtente, String nomeLibreria) {
        String sql = "INSERT INTO librerie (utente_id, nome_libreria) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idUtente);
            stmt.setString(2, nomeLibreria);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted == 0) {
                return new Eccezione(2, "Registrazione fallita nei dettagli");
            }

            // Ottieni il libreria_id generato
            int libreriaId;
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

    public Eccezione AggiungiLibroALibreria(int idLibreria, int idLibro) {
        String sql = "INSERT INTO contenuto_libreria (libreria_id, libro_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLibreria);
            stmt.setInt(2, idLibro);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                return null; // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Aggiunta fallita");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(1, "Conflitti di integrità: " + e.getMessage());
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    public Eccezione RimuoviLibroDaLibreria(int idLibreria, int idLibro) {
        String sql = "DELETE FROM contenuto_libreria WHERE libreria_id = ? AND libro_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLibreria);
            stmt.setInt(2, idLibro);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                return null; // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Rimozione fallita");
            }
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    public Eccezione RimuoviLibreria(int idLibreria) {
        String sql = "DELETE FROM librerie WHERE libreria_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLibreria);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                return null; // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Rimozione fallita");
            }
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    //consigli
    public Eccezione AggiungiConsiglio(ConsigliLibri consiglio) {
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
                return null; // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Aggiunta fallita tabella contenuto_consiglio");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(1, "Conflitti di integrità tabella contenuto_consiglio: " + e.getMessage());
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL tabella contenuto_consiglio: " + e.getMessage());
        }
    }

    public Eccezione AggiungiLibroAConsiglio(ConsigliLibri consiglio, int idLibroConsigliato) {
        String sql = "INSERT INTO contenuto_consiglio (utente_id, libro_id_riguardante, libro_id_consigliato) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, consiglio.getIdLibro());
            stmt.setInt(2, consiglio.getIdLibro());
            stmt.setInt(3, idLibroConsigliato);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                return null; // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Aggiunta fallita");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(1, "Conflitti di integrità: " + e.getMessage());
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    public Libri[] RicercaConsigliDatoLibro(int idLibro) throws SQLException {
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

    public ConsigliLibri RicercaConsigliDatoUtenteELibro(int idUtente, int idLibro) throws SQLException {
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
    public Eccezione AggiungiValutazione(ValutazioniLibri v) {
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
                return null; // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Aggiunta fallita");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(1, "Conflitti di integrità: " + e.getMessage());
        } catch (SQLException e) {
            return new Eccezione(3, "Errore SQL: " + e.getMessage());
        }
    }

    public ValutazioniLibri RicercaValutazione(int idUtente, int idLibro) throws SQLException {
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
                    return new ValutazioniLibri(idLibro, idUtente, score);
                }
            }
        }
        return null;
    }

    public ValutazioniLibri RicercaValutazioneMedia(int idLibro) throws SQLException {
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
                    return new ValutazioniLibri(idLibro, -1, score);
                }
            }
        }
        return null;
    }
}
