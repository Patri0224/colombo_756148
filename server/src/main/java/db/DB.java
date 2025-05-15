package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class DB {
    private Connection connection;
    private static final HashMap<Integer, DB> mappaIstanze = new HashMap<>();

    private DB(int x) throws IOException, SQLException {
        DBConfigLoader config = new DBConfigLoader();
        switch (x){
            case 0: this.connection = DriverManager.getConnection(config.getDbUrlTemplate0(), config.getUsername(), config.getPassword()); break;
            case 1: this.connection = DriverManager.getConnection(config.getDbUrlBookRecommenderDB(), config.getUsername(), config.getPassword()); break;
        }
    }

    public static Connection getDBConnection(int x) throws SQLException, IOException {
        if(!mappaIstanze.containsKey(x)){
            mappaIstanze.put(x, new DB(x));
        }
        return(mappaIstanze.get(x).connection);
    }
}
