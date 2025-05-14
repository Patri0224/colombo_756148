package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static DB instance;
    private Connection connection;

    private DB() throws IOException, SQLException {
        DBConfigLoader config = new DBConfigLoader();
        this.connection = DriverManager.getConnection(config.getDbUrl(), config.getUsername(), config.getPassword());
    }

    public static Connection getDBConnection() throws SQLException, IOException {
        if(instance==null){
            instance = new DB();
        }
        return(instance.connection);
    }
}
