package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    public static Connection connettiDB() throws IOException, SQLException {
        DBConfigLoader config = new DBConfigLoader();
        config.loadDBConfig();

        return DriverManager.getConnection(config.dbUrl, config.username, config.password);
    }
}
