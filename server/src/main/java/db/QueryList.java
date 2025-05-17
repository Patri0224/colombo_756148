package db;

import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.ConsigliLibri;
import bookRecommender.entita.Librerie;
import bookRecommender.entita.Libri;

import java.sql.*;
import java.util.ArrayList;

public class QueryList {
    private Connection conn = null;

    public QueryList(Connection conn) {
        this.conn = conn;
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
        String sql = "SELECT utente_id FROM UTENTI_REGISTRATI_PRINCIPALE WHERE email = ?";
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

    //pass non criptata
    public Eccezione ControlloPasswordUtente(int idUtente, String password) throws SQLException {
        if (!ControlloEsisteUtente(idUtente)) return new Eccezione(1, "Utente non esistente");
        String sql = "SELECT passwd FROM UTENTI_REGISTRATI_PRINCIPALE WHERE utente_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("passwd");
                    if (storedPassword.equals(password)) {//cambiare con metodo di controllo per password
                        return null; // Nessuna eccezione, password corretta
                    } else {
                        return new Eccezione(2, "Password errata");
                    }
                } else {
                    return new Eccezione(3, "errore interno");
                }
            }
        }
    }

    //pass non criptata
    public Eccezione Registrazione(String email, String password, String nome, String cognome, String codiceFiscale) throws SQLException {
        if (ControlloEsisteUtente(email)) return new Eccezione(1, "Utente già esistente");
        String sql = "INSERT INTO UTENTI_REGISTRATI_PRINCIPALE (email, passwd) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                // Nessuna eccezione, registrazione avvenuta con successo
            } else {
                return new Eccezione(2, "Registrazione fallita a principale");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(1, "Conflitti di integrità: " + e.getMessage());
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
                return null; // Nessuna eccezione, tutto ok
            } else {
                return new Eccezione(2, "Registrazione fallita nei dettagli");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return new Eccezione(3, "Conflitti di integrità: " + e.getMessage());
        } catch (SQLException e) {
            return new Eccezione(4, "Errore SQL: " + e.getMessage());
        }
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
        String dataSql = "SELECT p.libro_id, p.titolo, p.anno_pubblicazione, p.mese_pubblicazione, p.autori, d.descrizione, d.categorie, d.editori, d.prezzo " + "FROM libri_principale p JOIN libri_dettagli d ON p.libro_id = d.libro_id " + "WHERE (? IS NULL OR LOWER(p.titolo) LIKE LOWER(CONCAT('%', ?, '%'))) " + "AND (? IS NULL OR LOWER(p.autori) LIKE LOWER(CONCAT('%', ?, '%'))) " + "AND (? IS NULL OR p.anno_pubblicazione = ?)";
        String countSql = "SELECT COUNT(*) " + "FROM libri_principale p JOIN libri_dettagli d ON p.libro_id = d.libro_id " + "WHERE (? IS NULL OR LOWER(p.titolo) LIKE LOWER(CONCAT('%', ?, '%'))) " + "AND (? IS NULL OR LOWER(p.autori) LIKE LOWER(CONCAT('%', ?, '%'))) " + "AND (? IS NULL OR p.anno_pubblicazione = ?)";
        int count = 0;
        try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
            // Impostazione parametri per count
            if (titolo == null) {
                countStmt.setNull(1, java.sql.Types.VARCHAR);
                countStmt.setNull(2, java.sql.Types.VARCHAR);
            } else {
                countStmt.setString(1, titolo);
                countStmt.setString(2, titolo);
            }

            if (autore == null) {
                countStmt.setNull(3, java.sql.Types.VARCHAR);
                countStmt.setNull(4, java.sql.Types.VARCHAR);
            } else {
                countStmt.setString(3, autore);
                countStmt.setString(4, autore);
            }

            if (anno == 0) {
                countStmt.setNull(5, java.sql.Types.INTEGER);
                countStmt.setNull(6, java.sql.Types.INTEGER);
            } else {
                countStmt.setInt(5, anno);
                countStmt.setInt(6, anno);
            }

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
            // Impostazione parametri per estrazione dati
            if (titolo == null) {
                dataStmt.setNull(1, java.sql.Types.VARCHAR);
                dataStmt.setNull(2, java.sql.Types.VARCHAR);
            } else {
                dataStmt.setString(1, titolo);
                dataStmt.setString(2, titolo);
            }

            if (autore == null) {
                dataStmt.setNull(3, java.sql.Types.VARCHAR);
                dataStmt.setNull(4, java.sql.Types.VARCHAR);
            } else {
                dataStmt.setString(3, autore);
                dataStmt.setString(4, autore);
            }

            if (anno == 0) {
                dataStmt.setNull(5, java.sql.Types.INTEGER);
                dataStmt.setNull(6, java.sql.Types.INTEGER);
            } else {
                dataStmt.setInt(5, anno);
                dataStmt.setInt(6, anno);
            }

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

    public Libri[] RicercaLibriDaIds(int[] ids) throws SQLException {

        if (ids.length == 0) return new Libri[0];
        String str = "";
        int i = 0;
        for (int id : ids) {
            str += id + ", ";
            if (id < 0) i++;
        }
        str = str.substring(0, str.length() - 2);
        String dataSql = "SELECT p.libro_id, p.titolo, p.anno_pubblicazione, p.mese_pubblicazione, p.autori, d.descrizione, d.categorie, d.editori, d.prezzo " + "FROM libri_principale p JOIN libri_dettagli d ON p.libro_id = d.libro_id " + "WHERE p.libro_id IN (" + str + ")";
        Libri[] risultati = new Libri[ids.length - i];

        try (PreparedStatement dataStmt = conn.prepareStatement(dataSql)) {
            try (ResultSet rs = dataStmt.executeQuery()) {
                int l = 0;
                while (rs.next()) {
                    int id = rs.getInt("libro_id");
                    String titolo = (rs.getString("titolo"));
                    int anno = (rs.getInt("anno_pubblicazione"));
                    String mese = (rs.getString("mese_pubblicazione"));
                    String aut = (rs.getString("autori"));
                    String desc = (rs.getString("descrizione"));
                    String cat = (rs.getString("categorie"));
                    String editor = (rs.getString("editori"));
                    Float prezzo = (rs.getBigDecimal("prezzo").floatValue());
                    risultati[i] = new Libri(id, titolo, aut, desc, cat, editor, prezzo, mese, anno);
                    l++;
                }
            }
        }
        return risultati;
    }

    //librerie
    public Librerie[] RicercaLibrerie(int idUtente) throws SQLException {
        ArrayList<Librerie> librerie = new ArrayList<>();
        String sql = "SELECT l.libreria_id,l.nome_libreria,c.libro_id\n" + "FROM librerie l JOIN contenuto_libreria c ON l.libreria_id = c.libreria_id\n" + "WHERE l.utente_id = ?";
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
                        Librerie lib = new Librerie(idLibreria, nomeLibreria, new ArrayList<Integer>());
                        lib.aggiungiLibroInLocale(idLibro);
                        librerie.add(lib);

                    }
                }
                return librerie.toArray(new Librerie[0]);
            }
        }

    }

    public Eccezione AggiungiLibreria(int idUtente, Librerie libreria) {
        String sql = "INSERT INTO librerie (utente_id, nome_libreria) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idUtente);
            stmt.setString(2, libreria.getNome());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted == 0) {
                return new Eccezione(2, "Registrazione fallita nei dettagli");
            }

            // Ottieni il libreria_id generato
            int libreriaId;
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    libreriaId = generatedKeys.getInt(1);
                } else {
                    return new Eccezione(4, "ID libreria non ottenuto");
                }
            }

            // Ora inserisci ogni libro nella tabella contenuto_libreria
            sql = "INSERT INTO contenuto_libreria (libreria_id, libro_id) VALUES (?, ?)";
            try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                for (Integer libroId : libreria.getIdLibro()) {
                    stmt2.setInt(1, libreriaId);
                    stmt2.setInt(2, libroId);
                    stmt2.addBatch();
                }
                stmt2.executeBatch();
            }

            return null;

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
            if (rowsInserted > 0) {
                // Nessuna eccezione, tutto ok
            } else {
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

    public ConsigliLibri[] RicercaConsigliDatoLibro(int idLibro) throws SQLException {
        String sql = "SELECT utente_id, libro_id_riguardante, libro_id_consigliato FROM contenuto_consiglio WHERE libro_id_riguardante = ?";
        ArrayList<ConsigliLibri> consigli = new ArrayList<>();
        return null;
    }

    //valutazione

}
