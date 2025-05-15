package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface DbMethods{
    void nukeDb() throws SQLException, IOException;
    DbBuilder buildDb() throws SQLException, IOException;
    Connection getconnBookRecommenderDB();
}

