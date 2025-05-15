package db;

import java.io.*;
import java.io.IOException;
import java.util.Properties;

public class DBConfigLoader{
    private String dbUrlTemplate0;
    private String dbbUrlBookRecommenderDB;
    private String username;
    private String password;

    protected DBConfigLoader() throws IOException{
        loadDBConfig();
    }

    protected void loadDBConfig() throws IOException {
        try(InputStream in = getClass().getResourceAsStream("/db.properties")){
            Properties properties = new Properties();
            properties.load(in);
            dbUrlTemplate0 = properties.getProperty("db.urlTemplate0");
            dbbUrlBookRecommenderDB = properties.getProperty("db.utlBookRecommenderDB");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
        }
    }

    protected String getDbUrlTemplate0() {
        return dbUrlTemplate0;
    }
    protected String getDbUrlBookRecommenderDB(){
        return dbbUrlBookRecommenderDB;
    }
    protected String getUsername() {
        return username;
    }
    protected String getPassword() {
        return password;
    }
}
