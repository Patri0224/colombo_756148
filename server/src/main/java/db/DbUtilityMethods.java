package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface DbUtilityMethods {
    void nukeDb() throws SQLException, IOException;
    Connection getconnBookRecommenderDB();
}

