package db;

import java.io.*;
import java.io.IOException;
import java.util.Properties;

class DBConfigLoader{
    private static DBConfigLoader instance;
    private String dbUrlTemplate0;
    private String dbbUrlBookRecommenderDB;
    private String username;
    private String password;
    private final GetDbCredentials gc = new GetDbCredentials();



    private DBConfigLoader() throws IOException {
        gc.retrieveHost();
        gc.retrieveNome();
        gc.retrievePassword();
        try(InputStream in = getClass().getResourceAsStream("/db.properties")){
            Properties properties = new Properties();
            properties.load(in);
            dbUrlTemplate0 = buildConnectionUrl(properties.getProperty("db.urlTemplate0"), gc.getHost());
            dbbUrlBookRecommenderDB = buildConnectionUrl(properties.getProperty("db.utlBookRecommenderDB"), gc.getHost());
            username = gc.getNome();
            password = gc.getPassword();
        }
    }

    protected static DBConfigLoader getInstanceDBConfigLoader() throws IOException{
        if(instance == null){
            instance = new DBConfigLoader();
        }
        return(instance);
    }

    private String buildConnectionUrl(String str, int port){
        return(str.replace(":xxxxx/", ":" + port + "/"));
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
