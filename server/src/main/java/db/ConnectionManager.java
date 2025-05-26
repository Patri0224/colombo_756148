/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
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

