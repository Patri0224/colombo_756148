/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
package db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Gestisce la creazione delle credenziali di accesso a postgres
 */

class DBConfigLoader {
    private static DBConfigLoader instance;
    private String dbUrlTemplate0;
    private String dbUrlBookRecommenderDB;
    private String username;
    private String password;
    private String template0Parziale;
    private String bookDbParziale;
    private final GetDbCredentials gc = new GetDbCredentials();

    /**
     * Costruttore privato. Popola i campi leggendo il file properties e servendosi dei metodi di {@link GetDbCredentials}
     *
     * @throws IOException causata da inputStream
     */
    private DBConfigLoader() throws IOException {
        gc.retrieveHost();
        gc.retrieveNome();
        gc.retrievePassword();
        //gc.defaultCredentials();
        try (InputStream in = getClass().getResourceAsStream("/db.properties")) {
            Properties properties = new Properties();
            properties.load(in);
            template0Parziale = properties.getProperty("db.urlTemplate0");
            bookDbParziale = properties.getProperty("db.utlBookRecommenderDB");
            dbUrlTemplate0 = buildConnectionUrl(template0Parziale, gc.getHost());
            dbUrlBookRecommenderDB = buildConnectionUrl(bookDbParziale, gc.getHost());
            username = gc.getNome();
            password = gc.getPassword();
        }
    }

    /**
     * Esegue il costruttore
     *
     * @return singleton di {@link DBConfigLoader}
     * @throws IOException propagata dal costruttore
     */
    protected static DBConfigLoader getInstanceDBConfigLoader() throws IOException {
        if (instance == null) {
            instance = new DBConfigLoader();
        }
        return (instance);
    }

    /**
     * Resetta l'istanza di {@link DBConfigLoader}. Serve in caso avvenga un errore sconosciuto nella connessione al db
     */
    protected static void reset(){
        instance = null;
    }

    /**
     * Modifica il parametro host, serve per richiedere soltanto l'host del db all'utente in caso sbagli a inserirlo
     */
    protected void updateHost(){
        gc.retrieveHost();
        dbUrlTemplate0 = buildConnectionUrl(template0Parziale, gc.getHost());
        dbUrlBookRecommenderDB = buildConnectionUrl(bookDbParziale, gc.getHost());
    }

    /**
     * Modifica i parametri nome e password, serve per richiedere soltanto l'username e la password del db all'utente
     * in caso sbagli a inserire uno dei due
     */
    protected void updateNomePassword(){
        gc.retrieveNome();
        gc.retrievePassword();
        username = gc.getNome();
        password = gc.getPassword();
    }

    /**
     * Costruisce la stringa di url per connettersi al db concatenando {@code String str}  {@code Int port}
     *
     * @param str Stringa parziale (url standard) letta dal file
     * @param port Porta specifica da sistema a sistema inserita dall'utente
     * @return {@code String} url intera per la connessione al db
     */
    private String buildConnectionUrl(String str, int port) {
        return (str.replace(":xxxxx/", ":" + port + "/"));
    }

    protected String getDbUrlTemplate0() {
        return dbUrlTemplate0;
    }

    protected String getDbUrlBookRecommenderDB() {
        return dbUrlBookRecommenderDB;
    }

    protected String getUsername() {
        return username;
    }

    protected String getPassword() {
        return password;
    }
}
