package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionManager {
    Connection getConnection() throws SQLException, IOException, InterruptedException;
    void endConnection(Connection c) throws SQLException, InterruptedException;
}

