package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

class Db {
    private Connection connection;

    public Db(int x) throws IOException, SQLException {
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
    public static synchronized Connection getConnection(int x) throws SQLException, IOException {
        Db db = new Db(x);
        return(db.connection);
    }
}
