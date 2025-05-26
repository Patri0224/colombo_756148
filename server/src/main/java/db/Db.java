/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce la creazione a due database differenti {@code template0} e {@code bookrecommenderdb} per permettere operazioni di
 * connessione in {@link DbBuilder} e {@link ConnectionProvider}
 */

class Db {
    private Connection connection;

    /**
     * Costruttore privato
     *
     * @param x Sceglie se ritornare una connessione a template0 {@code int 0} o a bookrecommenderdb {@code int 1}
     * @throws IOException propagata da {@link DBConfigLoader#getInstanceDBConfigLoader()}
     * @throws SQLException causata da {@link DriverManager}
     */
    private Db(int x) throws IOException, SQLException {
        DBConfigLoader config = DBConfigLoader.getInstanceDBConfigLoader();
        switch (x){
            case 0:
                this.connection = DriverManager.getConnection(config.getDbUrlTemplate0(), config.getUsername(), config.getPassword());
                break;
            case 1:
                this.connection = DriverManager.getConnection(config.getDbUrlBookRecommenderDB(), config.getUsername(), config.getPassword());
                break;
        }
    }

    /**
     * Esegue il costruttore
     *
     * @param x parametro da passare al costruttore
     * @return istanza di {@link Db#Db}
     * @throws SQLException propagata da {@link Db#Db}
     * @throws IOException propagata da {@link Db#Db}
     */
    public static synchronized Connection getConnection(int x) throws SQLException, IOException {
        Db db = new Db(x);
        return(db.connection);
    }
}
