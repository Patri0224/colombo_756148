package db;

import java.sql.Connection;

/**
 * Interfaccia che esporta i metodi di {@link ConnectionProvider} e che consente di ottenere e restituire connessioni con
 * {@code bookrecommenderdb}
 */

public interface ConnectionManager {
    /**
     * Fornisce una connessione per svolgere query
     *
     * @return {@code Connection} a {@code bookrecommenderdb}
     * @throws InterruptedException
     */
    Connection getConnection() throws InterruptedException;

    /**
     * Restituisce la connessione al gestore dopo aver svolto la query
     *
     * @param c {@code Connection} da restituire
     * @throws InterruptedException
     */
    void endConnection(Connection c) throws InterruptedException;
}

