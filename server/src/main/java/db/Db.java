package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

class Db {
    private Connection connection;
    private static final HashMap<Integer, Db> mappaIstanze = new HashMap<>();


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

    protected static Connection getDBConnection(int x) throws SQLException, IOException {
        if(!mappaIstanze.containsKey(x)){
            mappaIstanze.put(x, new Db(x));
        }
        return(mappaIstanze.get(x).connection);
    }
}
